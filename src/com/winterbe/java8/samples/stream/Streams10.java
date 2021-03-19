package com.winterbe.java8.samples.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Benjamin Winterberg
 */
public class Streams10 {

    static class Person {
        String name;
        int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        List<Person> persons =
            Arrays.asList(
                new Person("Max", 18),
                new Person("Peter", 23),
                new Person("Pamela", 23),
                new Person("David", 12));

//        test1(persons);
//        test2(persons);
//        test3(persons);
//        test4(persons);
//        test5(persons);
//        test6(persons);
//        test7(persons);
        test8(persons);
//        test9(persons);
    }

    private static void test1(List<Person> persons) {
        List<Person> filtered =
            persons
                .stream()
                .filter(p -> p.name.startsWith("P"))
                .collect(Collectors.toList());

        System.out.println(filtered);    // [Peter, Pamela]
    }

    private static void test2(List<Person> persons) {
        Map<Integer, List<Person>> personsByAge = persons
            .stream()
            .collect(Collectors.groupingBy(p -> p.age));

        personsByAge
            .forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

        // age 18: [Max]
        // age 23:[Peter, Pamela]
        // age 12:[David]
    }

    private static void test3(List<Person> persons) {
        Double averageAge = persons
            .stream()
            .collect(Collectors.averagingInt(p -> p.age));

        System.out.println(averageAge);     // 19.0
    }

    private static void test4(List<Person> persons) {
        IntSummaryStatistics ageSummary =
            persons
                .stream()
                .collect(Collectors.summarizingInt(p -> p.age));

        System.out.println(ageSummary);
        // IntSummaryStatistics{count=4, sum=76, min=12, average=19,000000, max=23}
    }

    private static void test5(List<Person> persons) {
        String names = persons
            .stream()
            .filter(p -> p.age >= 18)
            .map(p -> p.name)
            .collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

        System.out.println(names);
        // In Germany Max and Peter and Pamela are of legal age.
    }

    private static void test6(List<Person> persons) {
        Map<Integer, String> map = persons
            .stream()
            .collect(Collectors.toMap(
                p -> p.age,     // first parameter: key function for the map
                p -> p.name,    // // second parameter: value function for the map
                (name1, name2) -> name1 + ";" + name2));    // a ‘merge function, used to resolve collision for values associated with the same key

        System.out.println(map);
        // {18=Max, 23=Peter;Pamela, 12=David}  ==> because Peter & Pamela have the same key, some the merge function concatenated the names together
    }

    /**
     * If you want to write your Collector implementation, you need to implement Collector interface and specify its three generic parameters:
     * public interface Collector<T, A, R> {...}
     *  T – the type of objects that will be available for collection,
     *  A – the type of a mutable accumulator object,
     *  R – the type of a final result.
     *
     *  - Supplier<A>        supplier – The supplier function for the new collector
     *  - BiConsumer<A, T>   accumulator – The accumulator function for the new collector
     *  - BinaryOperator<A>  combiner – The combiner function for the new collector
     *  - Function<A, R>     finisher – The finisher function for the new collector
     *  - Characteristics... characteristics – The collector characteristics for the new collector  (3 dots means accepting ZERO or more parameters)
     * *
     * @param persons :
     */
    private static void test7(List<Person> persons) {
        // parameter types: Person (input type), StringJoiner (accumulator object type), String (result type)
        Collector<Person, StringJoiner, String> personNameCollector =
            Collector.of(
                () -> new StringJoiner(" | "),   // supplier
                (j, p) -> j.add(p.name.toUpperCase()),  // accumulator
                (j1, j2) -> j1.merge(j2),               // combiner
                StringJoiner::toString);                // finisher

        String names = persons
            .stream()
            .collect(personNameCollector);

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }

    private static void test8(List<Person> persons) {
        Collector<Person, StringJoiner, String> personNameCollector =
            Collector.of(
                () -> {
                    System.out.println("supplier");     // supplier
                    return new StringJoiner(" | ");
                },
                (j, p) -> {
                    System.out.format("accumulator: p=%s; j=%s\n", p, j);
                    j.add(p.name.toUpperCase());
                },
                (j1, j2) -> {
                    System.out.println("merge!!!!!!!!!!!!!");
                    return j1.merge(j2);
                },
                j -> {
                    System.out.println("finisher");
                    return j.toString();
                });

        // all the print out by triggering the above:
//        supplier
//        accumulator: p=Max; j=
//        accumulator: p=Peter; j=MAX
//        accumulator: p=Pamela; j=MAX | PETER
//        accumulator: p=David; j=MAX | PETER | PAMELA
//        finisher


        String names = persons
            .stream()
            .collect(personNameCollector);

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }

    private static void test9(List<Person> persons) {
        Collector<Person, StringJoiner, String> personNameCollector =
            Collector.of(
                () -> {
                    System.out.println("supplier");
                    return new StringJoiner(" | ");
                },
                (j, p) -> {
                    System.out.format("accumulator: p=%s; j=%s\n", p, j);
                    j.add(p.name.toUpperCase());
                },
                (j1, j2) -> {
                    System.out.println("merge");
                    return j1.merge(j2);
                },
                j -> {
                    System.out.println("finisher");
                    return j.toString();
                });

        String names = persons
            .parallelStream()
            .collect(personNameCollector);

        System.out.println(names);  // MAX | PETER | PAMELA | DAVID
    }
}
