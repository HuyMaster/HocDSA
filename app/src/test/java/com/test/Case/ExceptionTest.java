package com.test.Case;

import org.junit.jupiter.api.Test;

public class ExceptionTest {
    @Test
    @SuppressWarnings("all")
    public void test() {
        String a = "hello world";
        try {
            System.out.println(a.length());
            a = null;
            System.out.println(a.length());
        } catch (Exception e) {
            System.out.println("an exception occurred " + e);
        }
    }
}
