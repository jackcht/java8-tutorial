package com.winterbe.java8.samples.stream;

import java.util.Optional;

/**
 * @author Benjamin Winterberg
 */
public class Optional1 {

    public static void main(String[] args) {
        // of() func cannot accept NULL; there's another func called "ofNullable()" which accepts NULL
        Optional<String> optional = Optional.of("bam");

        // isPresent(): this check if null OR not
        System.out.println(optional.isPresent());                       // true

        System.out.println(optional.get());                             // "bam"

        // orElse will return the specified value ("fallback") when NULL
        System.out.println(optional.orElse("fallback"));          // "bam"

        optional.ifPresent((s) -> System.out.println(s.charAt(0)));     // "b"

        Optional<String> optional2 = Optional.empty();      // empty() creates an empty Optional instance
        System.out.println(optional2.isPresent());          // false


    }

}