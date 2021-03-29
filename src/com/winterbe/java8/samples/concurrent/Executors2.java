package com.winterbe.java8.samples.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Benjamin Winterberg
 */
public class Executors2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        //test1();
        //test2();
        test3();
    }

    private static void test3() throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        future.get(1, TimeUnit.SECONDS);    // cannot get result within 1 sec, thus TimeoutException
        // exception print out
        /*  Exception in thread "main" java.util.concurrent.TimeoutException
            at java.util.concurrent.FutureTask.get(FutureTask.java:205)
            at com.winterbe.java8.samples.concurrent.Executors2.test3(Executors2.java:34)
            at com.winterbe.java8.samples.concurrent.Executors2.main(Executors2.java:18)
         */
    }

    private static void test2() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        executor.shutdownNow();
        future.get();

        // Exception print out:
        /*
        Exception in thread "main" java.util.concurrent.ExecutionException: java.lang.IllegalStateException: task interrupted
            at java.util.concurrent.FutureTask.report(FutureTask.java:122)
            at java.util.concurrent.FutureTask.get(FutureTask.java:192)
            at com.winterbe.java8.samples.concurrent.Executors2.test2(Executors2.java:51)
            at com.winterbe.java8.samples.concurrent.Executors2.main(Executors2.java:17)
        Caused by: java.lang.IllegalStateException: task interrupted
            at com.winterbe.java8.samples.concurrent.Executors2.lambda$test2$1(Executors2.java:46)
            at java.util.concurrent.FutureTask.run(FutureTask.java:266)
            at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
            at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
            at java.lang.Thread.run(Thread.java:745)
        Caused by: java.lang.InterruptedException: sleep interrupted
            at java.lang.Thread.sleep(Native Method)
            at java.lang.Thread.sleep(Thread.java:340)
            at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
            at com.winterbe.java8.samples.concurrent.Executors2.lambda$test2$1(Executors2.java:42)
            ... 4 more
         */
    }

    private static void test1() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Integer> future = executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                return 123;
            }
            catch (InterruptedException e) {
                throw new IllegalStateException("task interrupted", e);
            }
        });

        System.out.println("future done: " + future.isDone());

        Integer result = future.get();  // this will block the thread and wait until the callable completes

        System.out.println("future done: " + future.isDone());
        System.out.print("result: " + result);

        executor.shutdownNow();
    }

}
