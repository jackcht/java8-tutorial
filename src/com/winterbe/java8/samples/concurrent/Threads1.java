package com.winterbe.java8.samples.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author Benjamin Winterberg
 */
public class Threads1 {

    public static void main(String[] args) {
        //test1();
        //test2();
        test3();
    }

    private static void test3() {
        Runnable runnable = () -> {
            try {
                System.out.println("Foo " + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(1);
                System.out.println("Bar " + Thread.currentThread().getName());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        /* print out: all the code are run in the new thread (as we're not calling runnable.run() directly)
            it will always print out Thread-0 instead of main
        Foo Thread-0
        Bar Thread-0
         */
    }

    private static void test2() {
        Runnable runnable = () -> {
            try {
                System.out.println("Foo " + Thread.currentThread().getName());
                Thread.sleep(1000);     // this works the same as TimeUnit.SECONDS.sleep(1);
                System.out.println("Bar " + Thread.currentThread().getName());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private static void test1() {
        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Hello " + threadName);
        };

        runnable.run();
        // if I comment out the following 2 line, it will still print out "Hello main", as this program itself is 'main'
        Thread thread = new Thread(runnable);
        thread.start();

        System.out.println("Done!");

        /* print out
        Hello main
        Hello Thread-0
        Done!
        -----OR -----
        Hello main
        Done!
        Hello Thread-0
         */
    }
}
