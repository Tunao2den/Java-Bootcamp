/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class StdAudio {
    public static final int SAMPLE_RATE = 44100;
    private static final int BYTES_PER_SAMPLE = 2;
    private static final int BITS_PER_SAMPLE = 16;
    private static final int MAX_16_BIT = 32768;
    private static final int SAMPLE_BUFFER_SIZE = 4096;
    private static final int MONO = 1;
    private static final int STEREO = 2;
    private static final boolean LITTLE_ENDIAN = false;
    private static final boolean BIG_ENDIAN = true;
    private static final boolean SIGNED = true;
    private static final boolean UNSIGNED = false;
    private static SourceDataLine line;
    private static byte[] buffer;
    private static int bufferSize;
    private static LinkedList<BackgroundRunnable> backgroundRunnables;
    private static QueueOfDoubles recordedSamples;
    private static boolean isRecording;

    private StdAudio() {
    }

    private static void init() {
        try {
            AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine)AudioSystem.getLine(info);
            line.open(format, 8192);
            buffer = new byte[2730];
        } catch (LineUnavailableException e) {
            System.out.println(e.getMessage());
        }
        line.start();
    }

    private static AudioInputStream getAudioInputStreamFromFile(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }
        try {
            File file = new File(filename);
            if (file.exists()) {
                return AudioSystem.getAudioInputStream(file);
            }
            InputStream is1 = StdAudio.class.getResourceAsStream(filename);
            if (is1 != null) {
                return AudioSystem.getAudioInputStream(is1);
            }
            InputStream is2 = StdAudio.class.getClassLoader().getResourceAsStream(filename);
            if (is2 != null) {
                return AudioSystem.getAudioInputStream(is2);
            }
            URL url = new URL(filename);
            return AudioSystem.getAudioInputStream(url);
        } catch (IOException e) {
            throw new IllegalArgumentException("could not read '" + filename + "'", e);
        } catch (UnsupportedAudioFileException e) {
            throw new IllegalArgumentException("file of unsupported audio file format: '" + filename + "'", e);
        }
    }

    public static void drain() {
        if (bufferSize > 0) {
            line.write(buffer, 0, bufferSize);
            bufferSize = 0;
        }
        line.drain();
    }

    public static void play(double sample) {
        if (Double.isNaN(sample)) {
            throw new IllegalArgumentException("sample is NaN");
        }
        if (sample < -1.0) {
            sample = -1.0;
        }
        if (sample > 1.0) {
            sample = 1.0;
        }
        if (isRecording) {
            recordedSamples.enqueue(sample);
        }
        int s = (int)(32768.0 * sample);
        if (sample == 1.0) {
            s = Short.MAX_VALUE;
        }
        StdAudio.buffer[StdAudio.bufferSize++] = (byte)s;
        StdAudio.buffer[StdAudio.bufferSize++] = (byte)(s >> 8);
        if (bufferSize >= buffer.length) {
            line.write(buffer, 0, buffer.length);
            bufferSize = 0;
        }
    }

    public static void play(double[] samples) {
        if (samples == null) {
            throw new IllegalArgumentException("argument to play() is null");
        }
        for (int i = 0; i < samples.length; ++i) {
            StdAudio.play(samples[i]);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void play(String filename) {
        if (isRecording) {
            double[] samples;
            for (double sample : samples = StdAudio.read(filename)) {
                recordedSamples.enqueue(sample);
            }
        }
        AudioInputStream ais = StdAudio.getAudioInputStreamFromFile(filename);
        DataLine line = null;
        int BUFFER_SIZE = 4096;
        try {
            int count;
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            line = (SourceDataLine)AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
            byte[] samples = new byte[BUFFER_SIZE];
            while ((count = ais.read(samples, 0, BUFFER_SIZE)) != -1) {
                line.write(samples, 0, count);
            }
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (line != null) {
                line.drain();
                line.close();
            }
        }
    }

    public static double[] read(String filename) {
        int READ_BUFFER_SIZE = 4096;
        AudioFormat toAudioFormat = new AudioFormat(44100.0f, 16, 1, true, false);
        AudioInputStream fromAudioInputStream = StdAudio.getAudioInputStreamFromFile(filename);
        AudioFormat fromAudioFormat = fromAudioInputStream.getFormat();
        if (!AudioSystem.isConversionSupported(toAudioFormat, fromAudioFormat)) {
            throw new IllegalArgumentException("system cannot convert from " + fromAudioFormat + " to " + toAudioFormat);
        }
        AudioInputStream toAudioInputStream = AudioSystem.getAudioInputStream(toAudioFormat, fromAudioInputStream);
        try {
            int count;
            QueueOfDoubles queue = new QueueOfDoubles();
            byte[] bytes = new byte[READ_BUFFER_SIZE];
            while ((count = toAudioInputStream.read(bytes, 0, READ_BUFFER_SIZE)) != -1) {
                for (int i = 0; i < count / 2; ++i) {
                    double sample = (double)((short)((bytes[2 * i + 1] & 0xFF) << 8 | bytes[2 * i] & 0xFF)) / 32768.0;
                    queue.enqueue(sample);
                }
            }
            toAudioInputStream.close();
            fromAudioInputStream.close();
            return queue.toArray();
        } catch (IOException ioe) {
            throw new IllegalArgumentException("could not read '" + filename + "'", ioe);
        }
    }

    public static void save(String filename, double[] samples) {
        block22: {
            if (filename == null) {
                throw new IllegalArgumentException("filename is null");
            }
            if (samples == null) {
                throw new IllegalArgumentException("samples[] is null");
            }
            AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, false);
            byte[] data = new byte[2 * samples.length];
            for (int i = 0; i < samples.length; ++i) {
                int temp = (int)(samples[i] * 32768.0);
                if (samples[i] == 1.0) {
                    temp = Short.MAX_VALUE;
                }
                data[2 * i + 0] = (byte)temp;
                data[2 * i + 1] = (byte)(temp >> 8);
            }
            try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
                 AudioInputStream ais = new AudioInputStream(bais, format, samples.length);){
                if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                    if (!AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, ais)) {
                        throw new IllegalArgumentException("saving to WAVE file format is not supported on this system");
                    }
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
                    break block22;
                }
                if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                    if (!AudioSystem.isFileTypeSupported(AudioFileFormat.Type.AU, ais)) {
                        throw new IllegalArgumentException("saving to AU file format is not supported on this system");
                    }
                    AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
                    break block22;
                }
                if (filename.endsWith(".aif") || filename.endsWith(".aiff") || filename.endsWith(".AIF") || filename.endsWith(".AIFF")) {
                    if (!AudioSystem.isFileTypeSupported(AudioFileFormat.Type.AIFF, ais)) {
                        throw new IllegalArgumentException("saving to AIFF file format is not supported on this system");
                    }
                    AudioSystem.write(ais, AudioFileFormat.Type.AIFF, new File(filename));
                    break block22;
                }
                throw new IllegalArgumentException("file extension for saving must be .wav, .au, or .aif");
            } catch (IOException ioe) {
                throw new IllegalArgumentException("unable to save file '" + filename + "'", ioe);
            }
        }
    }

    public static synchronized void stopInBackground() {
        for (BackgroundRunnable runnable : backgroundRunnables) {
            runnable.stop();
        }
        backgroundRunnables = new LinkedList();
    }

    public static synchronized void playInBackground(String filename) {
        BackgroundRunnable runnable = new BackgroundRunnable(filename);
        new Thread(runnable).start();
        backgroundRunnables.add(runnable);
    }

    @Deprecated
    public static synchronized void loopInBackground(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        AudioInputStream ais = StdAudio.getAudioInputStreamFromFile(filename);
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.loop(-1);
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable(){

            @Override
            public void run() {
                while (true) {
                    try {
                        while (true) {
                            Thread.sleep(1000L);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        continue;
                    }
                    break;
                }
            }
        }).start();
    }

    public static void startRecording() {
        if (isRecording) {
            throw new IllegalStateException("startRecording() must not be called twice in a row");
        }
        recordedSamples = new QueueOfDoubles();
        isRecording = true;
    }

    public static double[] stopRecording() {
        if (isRecording) {
            double[] results = recordedSamples.toArray();
            isRecording = false;
            recordedSamples = null;
            return results;
        }
        throw new IllegalStateException("stopRecording() must be called after calling startRecording()");
    }

    public static void main(String[] args) {
        double freq = 440.0;
        for (int i = 0; i <= 44100; ++i) {
            StdAudio.play(0.5 * Math.sin(Math.PI * 2 * freq * (double)i / 44100.0));
        }
        String base = "https://introcs.cs.princeton.edu/java/stdlib/";
        StdAudio.play(base + "test.wav");
        StdAudio.play(base + "test-22050.wav");
        StdAudio.play(base + "test.midi");
        for (int i = 0; i < 10; ++i) {
            StdAudio.play(base + "BaseDrum.wav");
            StdAudio.play(base + "SnareDrum.wav");
        }
        StdAudio.drain();
    }

    static {
        bufferSize = 0;
        backgroundRunnables = new LinkedList();
        recordedSamples = null;
        isRecording = false;
        StdAudio.init();
    }

    private static class QueueOfDoubles {
        private static final int INIT_CAPACITY = 16;
        private double[] a = new double[16];
        private int n = 0;

        private void resize(int capacity) {
            assert (capacity >= this.n);
            double[] temp = new double[capacity];
            for (int i = 0; i < this.n; ++i) {
                temp[i] = this.a[i];
            }
            this.a = temp;
        }

        public void enqueue(double item) {
            if (this.n == this.a.length) {
                this.resize(2 * this.a.length);
            }
            this.a[this.n++] = item;
        }

        public int size() {
            return this.n;
        }

        public double[] toArray() {
            double[] result = new double[this.n];
            for (int i = 0; i < this.n; ++i) {
                result[i] = this.a[i];
            }
            return result;
        }
    }

    private static class BackgroundRunnable
    implements Runnable {
        private volatile boolean exit = false;
        private final String filename;

        public BackgroundRunnable(String filename) {
            this.filename = filename;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            AudioInputStream ais = StdAudio.getAudioInputStreamFromFile(this.filename);
            DataLine line = null;
            int BUFFER_SIZE = 4096;
            try {
                int count;
                AudioFormat audioFormat = ais.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                line = (SourceDataLine)AudioSystem.getLine(info);
                line.open(audioFormat);
                line.start();
                byte[] samples = new byte[BUFFER_SIZE];
                while (!this.exit && (count = ais.read(samples, 0, BUFFER_SIZE)) != -1) {
                    line.write(samples, 0, count);
                }
            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            } finally {
                if (line != null) {
                    line.drain();
                    line.close();
                }
            }
        }

        public void stop() {
            this.exit = true;
        }
    }
}

