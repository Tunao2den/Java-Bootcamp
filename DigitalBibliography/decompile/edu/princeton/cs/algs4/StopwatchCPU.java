/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class StopwatchCPU {
    private static final double NANOSECONDS_PER_SECOND = 1.0E9;
    private final ThreadMXBean threadTimer = ManagementFactory.getThreadMXBean();
    private final long start = this.threadTimer.getCurrentThreadCpuTime();

    public double elapsedTime() {
        long now = this.threadTimer.getCurrentThreadCpuTime();
        return (double)(now - this.start) / 1.0E9;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        StopwatchCPU timer1 = new StopwatchCPU();
        double sum1 = 0.0;
        for (int i = 1; i <= n; ++i) {
            sum1 += Math.sqrt(i);
        }
        double time1 = timer1.elapsedTime();
        StdOut.printf("%e (%.2f seconds)\n", sum1, time1);
        StopwatchCPU timer2 = new StopwatchCPU();
        double sum2 = 0.0;
        for (int i = 1; i <= n; ++i) {
            sum2 += Math.pow(i, 0.5);
        }
        double time2 = timer2.elapsedTime();
        StdOut.printf("%e (%.2f seconds)\n", sum2, time2);
    }
}

