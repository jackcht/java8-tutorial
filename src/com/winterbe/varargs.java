package com.winterbe;


import java.util.Arrays;

public class varargs {

    public static void main(String[] args){
        testVarargs("1", "2", "3", "4");
        testVarargs();  // this is also valid, nothing printed out
        testVarargs("foo");
        testVarargs("foo", "bar", "baz");
        // this is not recommended, as explicit array creation is preferred
        testVarargs(new String[]{"foo", "var", "baz"});

    }


    public static void testVarargs(String... strings){
        //Arrays.stream(strings).forEach(s -> System.out.println(s));
        Arrays.stream(strings).forEach(System.out::println);
        System.out.println("==========");
    }
}
