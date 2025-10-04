package algorithms;

import metrics.PerformanceTracker;

public final class Kadane {
    private Kadane() {}

    public static final class Result {
        public final int maxSum, start, end;
        public Result(int maxSum, int start, int end) {
            this.maxSum = maxSum;
            this.start = start;
            this.end = end;
        }
        @Override public String toString() {
            return "sum=" + maxSum + " [" + start + "," + end + "]";
        }
    }

    public static Result maxSubarray(int[] a, PerformanceTracker p) {
        if (a == null || a.length == 0) throw new IllegalArgumentException("array is empty");
        int best = a[0], cur = a[0]; p.arrayAccesses++;
        int curStart = 0, bestL = 0, bestR = 0;

        for (int i = 1; i < a.length; i++) {
            p.arrayAccesses++;
            int extend = cur + a[i];
            p.comparisons++;
            if (extend < a[i]) {
                cur = a[i];
                curStart = i;
            } else {
                cur = extend;
            }
            if (cur > best) {
                best = cur; bestL = curStart; bestR = i;
            }
        }
        return new Result(best, bestL, bestR);
    }
}