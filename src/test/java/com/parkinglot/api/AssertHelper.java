package com.parkinglot.api;

import static org.junit.Assert.fail;

public class AssertHelper {
    public static void assertThrows(Class<?> expectedClazz, Runnable action) {
        // The ****ing JUnit 4 does not contains assertThrows, so we implement it ourselves.
        try {
            action.run();
        } catch (Throwable error) {
            final Class<? extends Throwable> actualClazz = error.getClass();
            if (!expectedClazz.isAssignableFrom(actualClazz)) {
                fail(String.format("Expected throws %s, actual %s", expectedClazz, actualClazz));
            }
            return;
        }

        fail("The action does not throw any exception.");
    }
}
