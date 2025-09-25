package info.cho.passwords.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class SimpleTestRunner {
    private SimpleTestRunner() {
    }

    public static void main(String[] args) throws Exception {
        runTests(Class.forName("info.cho.passwords.PasswordsPluginTest"));
    }

    private static void runTests(Class<?> testClass) throws Exception {
        Object testInstance = testClass.getDeclaredConstructor().newInstance();
        Method beforeEach = null;
        Method afterEach = null;
        List<Method> testMethods = new ArrayList<>();

        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforeEach = method;
            } else if (method.isAnnotationPresent(AfterEach.class)) {
                afterEach = method;
            } else if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

        int executed = 0;
        for (Method testMethod : testMethods) {
            if (beforeEach != null) {
                beforeEach.setAccessible(true);
                beforeEach.invoke(testInstance);
            }
            try {
                testMethod.setAccessible(true);
                testMethod.invoke(testInstance);
                executed++;
            } catch (InvocationTargetException exception) {
                Throwable cause = exception.getCause();
                throw new AssertionError("Test " + testMethod.getName() + " failed", cause);
            } finally {
                if (afterEach != null) {
                    afterEach.setAccessible(true);
                    afterEach.invoke(testInstance);
                }
            }
        }

        System.out.println("Executed " + executed + " tests from " + testClass.getSimpleName());
    }
}
