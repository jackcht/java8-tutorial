package com.winterbe.java8.samples.concurrent;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Benjamin Winterberg
 */
public class Executors3 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        //test0();
        //test1();
        //test2();
        test3();
        //test4();
    }

    private static void test0() throws InterruptedException {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                () -> "task1",
                () -> "task2",
                () -> "task3");

        executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    }
                    catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(System.out::println);

        executor.shutdown();
        // print out
        /*
        task1
        task2
        task3
         */
    }

    private static void test1() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newWorkStealingPool();

        List<Callable<String>> callables = Arrays.asList(
                callable("task1", 2),
                callable("task2", 1),
                callable("task3", 3));

        String result = executor.invokeAny(callables);
        System.out.println(result);

        executor.shutdown();
        // print out
        /*
        callable: ForkJoinPool-1-worker-2   // all the workers are called
        callable: ForkJoinPool-1-worker-3
        callable: ForkJoinPool-1-worker-1
        task2
         */
    }

    private static Callable<String> callable(String result, long sleepSeconds) {
        return () -> {
            String name = Thread.currentThread().getName();
            System.out.println("callable: " + name);
            TimeUnit.SECONDS.sleep(sleepSeconds);
            return result;
        };
    }

    private static void test2() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Scheduling: " + System.nanoTime() + " in " + Thread.currentThread().getName());
        int delay = 3;
        // run once (one-shot action)
        ScheduledFuture<?> future = executor.schedule(task, delay, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1337);
        System.out.println("Now: " + Thread.currentThread().getName());

        // getDelay() to retrieve the remaining delay ==> After this delay has elapsed the task will be executed concurrently.
        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining Delay: %sms\n", remainingDelay);   //Scheduling: 118233920916200

        executor.shutdown();
        // print out
        /*
        Now: main
        Remaining Delay: 1661ms
        Scheduling: 118813404799100 in pool-1-thread-1
         */
    }


    private static void test3() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Scheduling: " + System.nanoTime());
            }
            catch (InterruptedException e) {
                System.err.println("task interrupted");
            }
        };

        executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(10);
        executor.shutdown();
    }

    private static void test4() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        Runnable task = () -> {
            try {
                System.out.println("Starting Task @ " + LocalDateTime.now() + " in " + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                System.err.println("task interrupted");
            }
        };
        //LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(LocalDateTime.now());
        int initialDelay = 0;
        int period = 500;
        // task to run once every second
        //  initialDelay – the time to delay first execution
        //  period – the period between successive executions
        //      If any execution of this task takes longer than its period, then subsequent executions may start late, but will not concurrently execute.
        ScheduledFuture<?> handle = executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);

        // this is the way to run 'concurrent' jobs, run schedule multiple task using the same Executor
        executor.scheduleAtFixedRate(task, 1000, 1000, TimeUnit.MILLISECONDS);

        // SHUT THE CIRCUIT AFTER 15 SECONDS
//        ScheduledFuture<?> future = executor.schedule(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Cancelling the Scheduled job in " + Thread.currentThread().getName() + " @ " + LocalDateTime.now());
//                handle.cancel(true);
//            }
//        }, 15, TimeUnit.SECONDS);
//
//        while(!future.isDone()){
//            TimeUnit.MILLISECONDS.sleep(1000);
//        }
//        executor.shutdown();
//        System.out.println(LocalDateTime.now());


        TimeUnit.SECONDS.sleep(15);
        executor.shutdown();
        System.out.println(LocalDateTime.now());

        /* print out when schedule 2 tasks (Runnable) using the TimeUnit Sleep() in the main thread
        // the log for same thread is diffed by 5 seconds, thread 2 is lagged behind thread 1 by 1 second
        2021-03-28T23:14:03.600
        Starting Task @ 2021-03-28T23:14:03.606 in pool-1-thread-1
        Starting Task @ 2021-03-28T23:14:04.612 in pool-1-thread-2
        Starting Task @ 2021-03-28T23:14:08.613 in pool-1-thread-1
        Starting Task @ 2021-03-28T23:14:09.613 in pool-1-thread-2
        Starting Task @ 2021-03-28T23:14:13.620 in pool-1-thread-1
        Starting Task @ 2021-03-28T23:14:14.621 in pool-1-thread-2
        2021-03-28T23:14:18.615
         */


        /* Print out when schedule One Runnable using the 'handle.cancel', you the log has timestamps diffed by 2500 (i.e. not concurrently executed)
        2021-03-28T23:03:41.065
        Starting Task @ 2021-03-28T23:03:41.071 in pool-1-thread-1
        Starting Task @ 2021-03-28T23:03:43.578 in pool-1-thread-2
        Starting Task @ 2021-03-28T23:03:46.080 in pool-1-thread-2
        Starting Task @ 2021-03-28T23:03:48.588 in pool-1-thread-2
        Starting Task @ 2021-03-28T23:03:51.091 in pool-1-thread-2
        Starting Task @ 2021-03-28T23:03:53.598 in pool-1-thread-2
        Cancelling the Scheduled job in pool-1-thread-1 @ 2021-03-28T23:03:56.078
        task interrupted
        2021-03-28T23:03:56.186
         */
    }


}
