package cli;

import algorithms.Kadane;
import metrics.PerformanceTracker;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Random;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        String sizes = arg(args, "sizes", "100,1000,10000");
        String datasets = arg(args, "datasets", "random,neg-heavy");
        String out = arg(args, "out", "");

        try (CSV csv = new CSV(out)) {
            if (csv.enabled()) csv.row("algo","n","dataset","timeMs","comparisons","arrayAccesses","sum[start,end]");
            else header();

            for (String s : sizes.split(",")) {
                int n = Integer.parseInt(s.trim());
                for (String d : datasets.split(",")) {
                    String kind = d.trim();
                    int[] a = makeData(n, kind);
                    PerformanceTracker p = new PerformanceTracker();
                    long t0 = System.nanoTime();
                    Kadane.Result r = Kadane.maxSubarray(a, p);
                    long t1 = System.nanoTime();
                    double ms = (t1 - t0) / 1_000_000.0;

                    if (csv.enabled())
                        csv.row("kadane", n, kind, ms, p.comparisons, p.arrayAccesses, r.toString());
                    else
                        System.out.printf("%-8s %8d %-10s %8.3f %12d %14d %s%n", "kadane", n, kind, ms, p.comparisons, p.arrayAccesses, r);
                }
            }
        }
    }

    private static String arg(String[] args, String k, String def) {
        for (String a : args) {
            if (a.startsWith(k + "=")) {
                return a.substring(k.length() + 1);
            }
        }
        return def;
    }

    private static int[] makeData(int n, String kind) {
        Random rnd = new Random(42 + n + kind.hashCode());
        int[] a = new int[n];
        switch (kind) {
            case "neg-heavy":
                for (int i = 0; i < n; i++) {
                    a[i] = rnd.nextInt(30) - 35;
                }
                for (int i = 0; i < n/10; i++) {
                    a[rnd.nextInt(n)] = rnd.nextInt(20);
                }
                return a;
            default:
                for (int i = 0; i < n; i++) {
                    a[i] = rnd.nextInt(200_001) - 100_000;
                }
                return a;
        }
    }

    private static void header() {
        System.out.printf("%-8s %8s %-10s %8s %12s %14s %s%n",
                "algo","n","dataset","timeMs","comparisons","arrayAccesses","sum[start,end]");
    }

    private static final class CSV implements AutoCloseable {
        private final BufferedWriter out;
        CSV(String path) throws IOException {
            if (path == null || path.isBlank()) {
                out = null;
                return;
            }
            Path p = Path.of(path);
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }
            out = Files.newBufferedWriter(p, StandardCharsets.UTF_8);
        }
        boolean enabled() {
            return out != null;
        }
        void row(Object... cols) throws IOException {
            if (out == null) return;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cols.length; i++) {
                if (i > 0) sb.append(',');
                String s = String.valueOf(cols[i]).replace("\"", "\"\"");
                sb.append('"').append(s).append('"');
            }
            sb.append('\n'); out.write(sb.toString());
        }
        public void close() throws IOException {
            if (out != null) {
                out.close();
            }
        }
    }
}