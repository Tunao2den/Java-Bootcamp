/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import java.util.Random;

public final class StdRandom {
    private static Random random;
    private static long seed;

    private StdRandom() {
    }

    public static void setSeed(long s) {
        seed = s;
        random = new Random(seed);
    }

    public static long getSeed() {
        return seed;
    }

    @Deprecated
    public static double uniform() {
        return StdRandom.uniformDouble();
    }

    public static double uniformDouble() {
        return random.nextDouble();
    }

    @Deprecated
    public static int uniform(int n) {
        return StdRandom.uniformInt(n);
    }

    public static int uniformInt(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("argument must be positive: " + n);
        }
        return random.nextInt(n);
    }

    @Deprecated
    public static long uniform(long n) {
        return StdRandom.uniformLong(n);
    }

    public static long uniformLong(long n) {
        if (n <= 0L) {
            throw new IllegalArgumentException("argument must be positive: " + n);
        }
        long r = random.nextLong();
        long m = n - 1L;
        if ((n & m) == 0L) {
            return r & m;
        }
        long u = r >>> 1;
        while (u + m - (r = u % n) < 0L) {
            u = random.nextLong() >>> 1;
        }
        return r;
    }

    @Deprecated
    public static double random() {
        return StdRandom.uniformDouble();
    }

    @Deprecated
    public static int uniform(int a, int b) {
        return StdRandom.uniformInt(a, b);
    }

    public static int uniformInt(int a, int b) {
        if (b <= a || (long)b - (long)a >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
        }
        return a + StdRandom.uniform(b - a);
    }

    @Deprecated
    public static double uniform(double a, double b) {
        return StdRandom.uniformDouble(a, b);
    }

    public static double uniformDouble(double a, double b) {
        if (!(a < b)) {
            throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
        }
        return a + StdRandom.uniform() * (b - a);
    }

    public static boolean bernoulli(double p) {
        if (!(p >= 0.0) || !(p <= 1.0)) {
            throw new IllegalArgumentException("probability p must be between 0.0 and 1.0: " + p);
        }
        return StdRandom.uniformDouble() < p;
    }

    public static boolean bernoulli() {
        return StdRandom.bernoulli(0.5);
    }

    public static double gaussian() {
        double y;
        double x;
        double r;
        while ((r = (x = StdRandom.uniformDouble(-1.0, 1.0)) * x + (y = StdRandom.uniformDouble(-1.0, 1.0)) * y) >= 1.0 || r == 0.0) {
        }
        return x * Math.sqrt(-2.0 * Math.log(r) / r);
    }

    public static double gaussian(double mu, double sigma) {
        return mu + sigma * StdRandom.gaussian();
    }

    public static int geometric(double p) {
        if (!(p >= 0.0)) {
            throw new IllegalArgumentException("probability p must be greater than 0: " + p);
        }
        if (!(p <= 1.0)) {
            throw new IllegalArgumentException("probability p must not be larger than 1: " + p);
        }
        return (int)Math.ceil(Math.log(StdRandom.uniformDouble()) / Math.log(1.0 - p));
    }

    public static int poisson(double lambda) {
        if (!(lambda > 0.0)) {
            throw new IllegalArgumentException("lambda must be positive: " + lambda);
        }
        if (Double.isInfinite(lambda)) {
            throw new IllegalArgumentException("lambda must not be infinite: " + lambda);
        }
        int k = 0;
        double p = 1.0;
        double expLambda = Math.exp(-lambda);
        do {
            ++k;
        } while ((p *= StdRandom.uniformDouble()) >= expLambda);
        return k - 1;
    }

    public static double pareto() {
        return StdRandom.pareto(1.0);
    }

    public static double pareto(double alpha) {
        if (!(alpha > 0.0)) {
            throw new IllegalArgumentException("alpha must be positive: " + alpha);
        }
        return Math.pow(1.0 - StdRandom.uniformDouble(), -1.0 / alpha) - 1.0;
    }

    public static double cauchy() {
        return Math.tan(Math.PI * (StdRandom.uniformDouble() - 0.5));
    }

    public static int discrete(double[] probabilities) {
        if (probabilities == null) {
            throw new IllegalArgumentException("argument array must not be null");
        }
        double EPSILON = 1.0E-14;
        double sum = 0.0;
        for (int i = 0; i < probabilities.length; ++i) {
            if (!(probabilities[i] >= 0.0)) {
                throw new IllegalArgumentException("array entry " + i + " must be non-negative: " + probabilities[i]);
            }
            sum += probabilities[i];
        }
        if (sum > 1.0 + EPSILON || sum < 1.0 - EPSILON) {
            throw new IllegalArgumentException("sum of array entries does not approximately equal 1.0: " + sum);
        }
        block1: while (true) {
            double r = StdRandom.uniformDouble();
            sum = 0.0;
            int i = 0;
            while (true) {
                if (i >= probabilities.length) continue block1;
                if ((sum += probabilities[i]) > r) {
                    return i;
                }
                ++i;
            }
            break;
        }
    }

    public static int discrete(int[] frequencies) {
        if (frequencies == null) {
            throw new IllegalArgumentException("argument array must not be null");
        }
        long sum = 0L;
        for (int i = 0; i < frequencies.length; ++i) {
            if (frequencies[i] < 0) {
                throw new IllegalArgumentException("array entry " + i + " must be non-negative: " + frequencies[i]);
            }
            sum += (long)frequencies[i];
        }
        if (sum == 0L) {
            throw new IllegalArgumentException("at least one array entry must be positive");
        }
        if (sum >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("sum of frequencies overflows an int");
        }
        double r = StdRandom.uniformInt((int)sum);
        sum = 0L;
        for (int i = 0; i < frequencies.length; ++i) {
            if (!((double)(sum += (long)frequencies[i]) > r)) continue;
            return i;
        }
        assert (false);
        return -1;
    }

    public static double exponential(double lambda) {
        if (!(lambda > 0.0)) {
            throw new IllegalArgumentException("lambda must be positive: " + lambda);
        }
        return -Math.log(1.0 - StdRandom.uniformDouble()) / lambda;
    }

    @Deprecated
    public static double exp(double lambda) {
        return StdRandom.exponential(lambda);
    }

    public static void shuffle(Object[] a) {
        StdRandom.validateNotNull(a);
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int r = i + StdRandom.uniformInt(n - i);
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static void shuffle(double[] a) {
        StdRandom.validateNotNull(a);
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int r = i + StdRandom.uniformInt(n - i);
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static void shuffle(int[] a) {
        StdRandom.validateNotNull(a);
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int r = i + StdRandom.uniformInt(n - i);
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static void shuffle(char[] a) {
        StdRandom.validateNotNull(a);
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int r = i + StdRandom.uniformInt(n - i);
            char temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static void shuffle(Object[] a, int lo, int hi) {
        StdRandom.validateNotNull(a);
        StdRandom.validateSubarrayIndices(lo, hi, a.length);
        for (int i = lo; i < hi; ++i) {
            int r = i + StdRandom.uniformInt(hi - i);
            Object temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static void shuffle(double[] a, int lo, int hi) {
        StdRandom.validateNotNull(a);
        StdRandom.validateSubarrayIndices(lo, hi, a.length);
        for (int i = lo; i < hi; ++i) {
            int r = i + StdRandom.uniformInt(hi - i);
            double temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static void shuffle(int[] a, int lo, int hi) {
        StdRandom.validateNotNull(a);
        StdRandom.validateSubarrayIndices(lo, hi, a.length);
        for (int i = lo; i < hi; ++i) {
            int r = i + StdRandom.uniformInt(hi - i);
            int temp = a[i];
            a[i] = a[r];
            a[r] = temp;
        }
    }

    public static int[] permutation(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative: " + n);
        }
        int[] perm = new int[n];
        for (int i = 0; i < n; ++i) {
            perm[i] = i;
        }
        StdRandom.shuffle(perm);
        return perm;
    }

    public static int[] permutation(int n, int k) {
        int r;
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative: " + n);
        }
        if (k < 0 || k > n) {
            throw new IllegalArgumentException("k must be between 0 and n: " + k);
        }
        int[] perm = new int[k];
        int i = 0;
        while (i < k) {
            r = StdRandom.uniformInt(i + 1);
            perm[i] = perm[r];
            perm[r] = i++;
        }
        for (i = k; i < n; ++i) {
            r = StdRandom.uniformInt(i + 1);
            if (r >= k) continue;
            perm[r] = i;
        }
        return perm;
    }

    private static void validateNotNull(Object x) {
        if (x == null) {
            throw new IllegalArgumentException("argument must not be null");
        }
    }

    private static void validateSubarrayIndices(int lo, int hi, int length) {
        if (lo < 0 || hi > length || lo > hi) {
            throw new IllegalArgumentException("subarray indices out of bounds: [" + lo + ", " + hi + ")");
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        if (args.length == 2) {
            StdRandom.setSeed(Long.parseLong(args[1]));
        }
        double[] probabilities = new double[]{0.5, 0.3, 0.1, 0.1};
        int[] frequencies = new int[]{5, 3, 1, 1};
        Object[] a = "A B C D E F G".split(" ");
        StdOut.println("seed = " + StdRandom.getSeed());
        for (int i = 0; i < n; ++i) {
            StdOut.printf("%2d ", StdRandom.uniformInt(100));
            StdOut.printf("%8.5f ", StdRandom.uniformDouble(10.0, 99.0));
            StdOut.printf("%5b ", StdRandom.bernoulli(0.5));
            StdOut.printf("%7.5f ", StdRandom.gaussian(9.0, 0.2));
            StdOut.printf("%1d ", StdRandom.discrete(probabilities));
            StdOut.printf("%1d ", StdRandom.discrete(frequencies));
            StdOut.printf("%11d ", StdRandom.uniformLong(100000000000L));
            StdRandom.shuffle(a);
            for (Object s : a) {
                StdOut.print(s);
            }
            StdOut.println();
        }
    }

    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }
}

