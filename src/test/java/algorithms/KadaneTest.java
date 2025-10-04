package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KadaneTest {
    @Test
    void classicExample() {
        int[] a = {-2,1,-3,4,-1,2,1,-5,4};
        var r = Kadane.maxSubarray(a, new PerformanceTracker());
        assertEquals(6, r.maxSum);
        assertEquals(3, r.start);
        assertEquals(6, r.end);
    }

    @Test
    void allNegative() {
        int[] a = {-8,-3,-6,-2,-5,-4};
        var r = Kadane.maxSubarray(a, new PerformanceTracker());
        assertEquals(-2, r.maxSum);
        assertEquals(3, r.start);
        assertEquals(3, r.end);
    }
}