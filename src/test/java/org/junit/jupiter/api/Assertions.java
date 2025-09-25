package org.junit.jupiter.api;

public final class Assertions {
    private Assertions() {
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, "Expected condition to be true");
    }

    public static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertFalse(boolean condition) {
        assertFalse(condition, "Expected condition to be false");
    }

    public static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(message + " (expected: " + expected + ", actual: " + actual + ")");
        }
    }

    public static void assertEquals(Object expected, Object actual) {
        assertEquals(expected, actual, "Values are not equal");
    }
}
