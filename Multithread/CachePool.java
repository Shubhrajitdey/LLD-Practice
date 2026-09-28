package Multithread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CachePool {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();

        AtomicInteger activeThreadsPeak = new AtomicInteger(0);

        System.out.println("Submitting 1,000 short-lived tasks...");

        for (int i = 1; i <= 1000; i++) {
            final int taskId = i;
            executor.submit(() -> {
                // Short-lived operation: quick compute / micro-task
                int sum = 0;
                for (int j = 0; j < 10_000; j++) {
                    sum += j;
                }

                // Log unique threads executing the work
                System.out.println("Task #" + taskId + " executed by: " 
                        + Thread.currentThread().getName());
            });
        }

        // Gracefully shut down
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nAll tasks completed.");
    }
}
