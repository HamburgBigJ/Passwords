package info.cho.passwords.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Basic test to verify JUnit 5 is working
 */
public class BasicJUnit5Test {

    @Test
    public void testJUnit5IsWorking() {
        assertTrue(true, "JUnit 5 should be working");
    }

    @Test
    public void testAssertions() {
        String expected = "Hello World";
        String actual = "Hello World";
        assertTrue(expected.equals(actual), "Strings should be equal");
    }
}