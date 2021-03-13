package com.winterbe.java8.samples.misc;

/**
 * @author Benjamin Winterberg
 */
public class Math1 {

    public static void main(String[] args) {
        testMathExact();
        System.out.println("==================================");
        testUnsignedInt();
    }

    private static void testUnsignedInt() {
        try {
            Integer.parseUnsignedInt("-123", 10);
        }
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());     //Illegal leading minus sign on unsigned string -123.
        }

        long maxUnsignedInt = (1l << 32) - 1;
        System.out.println(maxUnsignedInt);     //4294967295

        String string = String.valueOf(maxUnsignedInt);
        System.out.println(string);             //4294967295

        int unsignedInt = Integer.parseUnsignedInt(string, 10);
        System.out.println(unsignedInt);        //-1

        String string2 = Integer.toUnsignedString(unsignedInt, 10);
        System.out.println(string2);            //4294967295

        try {
            Integer.parseInt(string, 10);
        }
        catch (NumberFormatException e) {
            System.err.println("could not parse signed int of " + maxUnsignedInt);  //could not parse signed int of 4294967295
        }
    }


    private static void testMathExact() {
        System.out.println(Integer.MAX_VALUE);      //2147483647
        System.out.println(Integer.MAX_VALUE + 1);  //-2147483648

        try {
            int r = Math.addExact(Integer.MAX_VALUE, 1);
            System.out.println(r);
        }
        catch (ArithmeticException e) {
            System.err.println(e.getMessage());     //integer overflow
        }

        try {
            int t = Math.toIntExact(Long.MAX_VALUE);
            System.out.println(t);
        }
        catch (ArithmeticException e) {
            System.err.println(e.getMessage());     //integer overflow
        }
    }
}
