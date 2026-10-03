package Multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CachePool {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();

        System.out.println("=== Phase 1: Submitting 5 concurrent tasks ===");
        System.out.println("(All 5 tasks run concurrently, forcing the pool to create new threads)\n");

        List<Future<Integer>> firstBatch = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            firstBatch.add(executor.submit(() -> {
                // Simulate work so tasks overlap and run simultaneously
                Thread.sleep(300);
                System.out.println("Task #" + taskId + " executed by: " + Thread.currentThread().getName());
                return taskId;
            }));
        }

        // Wait for all Phase 1 tasks to finish
        for (Future<Integer> f : firstBatch) {
            f.get();
        }

        System.out.println("\n--- All Phase 1 tasks completed. Worker threads are now IDLE in the pool. ---");
        System.out.println("Sleeping 1 second (threads stay alive in the pool for 60 seconds)...\n");
        Thread.sleep(1000);

        System.out.println("=== Phase 2: Submitting 5 new tasks ===");
        System.out.println("(Notice the pool REUSES pool-1-thread-1 to thread-5 instead of creating new ones)\n");

        List<Future<Integer>> secondBatch = new ArrayList<>();
        for (int i = 6; i <= 10; i++) {
            final int taskId = i;
            secondBatch.add(executor.submit(() -> {
                Thread.sleep(300);
                System.out.println("Task #" + taskId + " executed by: " + Thread.currentThread().getName() + " [REUSED]");
                return taskId;
            }));
        }

        // Wait for all Phase 2 tasks to finish
        for (Future<Integer> f : secondBatch) {
            f.get();
        }

        // Gracefully shut down
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\nAll tasks completed.");
    }
}