package com.winterbe.java8.samples.lambda;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @author Benjamin Winterberg
 */
public class Lambda1 {

    public static void main(String[] args) {
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");

        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return b.compareTo(a);
            }
        });

        Collections.sort(names, (String a, String b) -> {
            return b.compareTo(a);
        });

        Collections.sort(names, (String a, String b) -> b.compareTo(a));

        Collections.sort(names, (a, b) -> b.compareTo(a));

        System.out.println(names); //[xenia, peter, mike, anna]
        /*
         Returns a comparator that imposes the reverse of the natural ordering on a collection of objects that implement the Comparable interface. 
         (The natural ordering is the ordering imposed by the objects' own compareTo method.)
         */
        names.sort(Collections.reverseOrder());//[xenia, peter, mike, anna]

        System.out.println(names);

        List<String> names2 = Arrays.asList("peter", null, "anna", "mike", "xenia");
        /*
        <String> Comparator<String> java.util.Comparator.nullsLast(Comparator<? super String> comparator)
        Returns a null-friendly comparator that considers null to be greater than non-null. 
        When both are null, they are considered equal. 
        If both are non-null, the specified Comparator is used to determine the order. 
        If the specified comparator is null, then the returned comparator considers all non-null values to be equal.
         */
        names2.sort(Comparator.nullsLast(String::compareTo));
        System.out.println(names2); //[anna, mike, peter, xenia, null]

        List<String> names3 = null;

        Optional.ofNullable(names3).ifPresent(list -> list.sort(Comparator.naturalOrder()));

        System.out.println(names3); //null
    }

}