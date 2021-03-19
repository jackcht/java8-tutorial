package com.winterbe.java8.samples.stream;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Benjamin Winterberg
 */
public class Streams6 {

    public static void main(String[] args) throws IOException {
        test1();        // 5.4
        test2();        // 5.4
        test3();        // 4.5
        test4();        // 2.45
    }

    private static void test4() {
        Stream
            .of(new BigDecimal("1.2"), new BigDecimal("3.7"))
            .mapToDouble(BigDecimal::doubleValue)
            .average()          // (1.2+3.7)/2 = 2.45
            .ifPresent(System.out::println);
    }

    private static void test3() {
        IntStream
            .range(0, 10)       // this is [0, 10), i.e. 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
            .average()          // 4.5
            .ifPresent(System.out::println);
    }

    private static void test2() {
        IntStream
            .builder()
            .add(1)
            .add(3)
            .add(5)
            .add(7)
            .add(11)
            .build()
            .average()
            .ifPresent(System.out::println);        // 5.4

    }

    private static void test1() {
        int[] ints = {1, 3, 5, 7, 11};
        Arrays
            .stream(ints)
            .average()
            .ifPresent(System.out::println);        // 5.4
    }
}
