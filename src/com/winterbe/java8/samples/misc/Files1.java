package com.winterbe.java8.samples.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Benjamin Winterberg
 */
public class Files1 {

    public static void main(String[] args) throws IOException {

        testList();     // print all the file names, separated by ";"
        System.out.println("==================");
        testWalk();
        System.out.println("==================");
        testFind();
        System.out.println("==================");

        testLines();
        System.out.println("==================");
        testReadWriteLines();
        System.out.println("==================");
        testWriter();
        System.out.println("==================");

        testReader();
        System.out.println("==================");
        testReaderLines();
        System.out.println("==================");
    }

    private static void testList() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(""))) {
            String joined = stream
                    .map(String::valueOf)
                    .filter(path -> !path.startsWith("."))
                    .sorted()
                    .collect(Collectors.joining("; "));
            System.out.println("list(): " + joined);
        }
    }

    private static void testFind() throws IOException {
        Path start = Paths.get("");
        int maxDepth = 5;
        try (Stream<Path> stream =
                     Files.find(start, maxDepth, (path, attr) -> String.valueOf(path).endsWith(".js"))) {
            String joined = stream
                    .sorted()
                    .map(String::valueOf)
                    .collect(Collectors.joining("; "));
            System.out.println("find(): " + joined);
        }
    }

    private static void testWalk() throws IOException {
        Path start = Paths.get("");
        int maxDepth = 5;

        // the following try() is a "try-with-resources" statement
        //      The try-with-resources statement is a try statement that declares one or more resources.
        //      A resource is an object that must be closed after the program is finished with it.
        //      The try-with-resources statement ensures that each resource is closed at the end of the statement.
        //      Any object that implements java.lang.AutoCloseable, which includes all objects which implement java.io.Closeable, can be used as a resource.
        //  相当于一个try block 加一个Final statement close() the connection in the end
        //  try block的作用任然存在， 加上catch block可以catch try block的exception,但是try-with-resource的exception 会被suppress
        try (Stream<Path> stream = Files.walk(start, maxDepth)) {
            String joined = stream
                    .map(String::valueOf)
                    .filter(path -> path.endsWith(".js"))
                    .collect(Collectors.joining("; "));
            System.out.println("walk(): " + joined);
        }
    }


    private static void testLines() throws IOException {
        // read lines by lines (i.e. save into memory one by one)
        try (Stream<String> stream = Files.lines(Paths.get("res/nashorn1.js"))) {
            stream.filter(line -> line.contains("print"))
                    .map(String::trim)
                    .forEach(System.out::println);
        }
    }

    private static void testReadWriteLines() throws IOException {
        // read all lines from nashorn1 (save all the content into memory)
        List<String> lines = Files.readAllLines(Paths.get("res/nashorn1.js"));
        // add an additional line
        lines.add("print('foobar');");
        // write all lines into another file
        Files.write(Paths.get("res", "nashorn1-modified.js"), lines);
    }

    private static void testWriter() throws IOException {
        Path path = Paths.get("res/output.js");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("print('Hello World');");
        }
    }

    private static void testReader() throws IOException {
        Path path = Paths.get("res/nashorn1.js");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            System.out.println(reader.readLine());
        }
    }

    private static void testReaderLines() throws IOException {
        Path path = Paths.get("res/nashorn1.js");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            // Buffered readers also have access to functional streams.
            // The method lines construct a functional stream upon all lines denoted by the buffered reader
            long countPrints = reader
                    .lines()
                    .filter(line -> line.contains("print"))
                    .count();
            System.out.println(countPrints);
        }
    }
}
