package com.winterbe.java8.samples.stream;

import java.util.Arrays;

/**
 * @author Benjamin Winterberg
 */
public class Streams9 {

    public static void main(String[] args) {
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
            .stream()
            .filter(s -> s.startsWith("c"))
            .map(String::toUpperCase)
            .sorted()
            .forEach(System.out::println);
        // since we called sorted() before printing, so see C1 before C2
        // C1
        // C2
    }
}
