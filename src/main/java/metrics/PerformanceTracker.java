package metrics;

public class PerformanceTracker {
    public long comparisons = 0;
    public long arrayAccesses = 0;

    public void reset() {
        comparisons = 0;
        arrayAccesses = 0;
    }

    @Override public String toString() {
        return "comparisons=" + comparisons + ", arrayAccesses=" + arrayAccesses;
    }
}