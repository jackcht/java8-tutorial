package com.winterbe.java8.samples.lambda;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Common standard functions from the Java API.
 *
 * @author Benjamin Winterberg
 */
public class Lambda3 {

    @FunctionalInterface
    interface Fun {
        void foo();
    }

    public static void main(String[] args) throws Exception {

        // Predicates

        Predicate<String> predicate = (s) -> s.length() > 0;

        System.out.println(predicate.test("foo"));              // true
        System.out.println(predicate.negate().test("foo"));     // false

        Predicate<Boolean> nonNull = Objects::nonNull;
        System.out.println(nonNull);        // this is an object
        Predicate<Boolean> isNull = Objects::isNull;
        System.out.println(isNull);         // this is an object

        Predicate<String> isEmpty = String::isEmpty;
        System.out.println(isEmpty);        // this is an object: com.winterbe.java8.samples.lambda.Lambda3$$Lambda$5/81628611@6d03e736
        Predicate<String> isNotEmpty = isEmpty.negate();
        System.out.println(isNotEmpty);     // this is an object: java.util.function.Predicate$$Lambda$2/1747585824@568db2f2


        // Functions
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);

        System.out.println(backToString.apply("123"));     // "123"


        // Suppliers
        Supplier<Person> personSupplier = Person::new;
        System.out.println(personSupplier.get());   // new Person object: com.winterbe.java8.samples.lambda.Person@6acbcfc0


        // Consumers
        Consumer<Person> greeter = (p) -> System.out.println("Hello, " + p.firstName);
        greeter.accept(new Person("Luke", "Skywalker"));    // print out: Hello, Luke



        // Comparators
        Comparator<Person> comparator = (p1, p2) -> p1.firstName.compareTo(p2.firstName);

        Person p1 = new Person("John", "Doe");
        Person p2 = new Person("Alice", "Wonderland");

        // the compare function returns:
        //      Returns:
        //      a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
        System.out.println(comparator.compare(p1, p2));             // 9    ==>      '>0' means p1 > p2
        System.out.println(comparator.reversed().compare(p1, p2));  // -9


        // Runnables

        Runnable runnable = () -> System.out.println(UUID.randomUUID());
        runnable.run();     // print out: 745a8720-465e-4874-9c6d-0a4459ce43d4


        // Callables

        Callable<UUID> callable = UUID::randomUUID;
        System.out.println(callable.call());        // returns: 1ed27f37-3127-46fe-be89-24b8ac17ec7e
    }

}
