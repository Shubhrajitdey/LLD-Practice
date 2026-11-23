package Multithread;

import java.util.concurrent.*;

public class FutureVsFutureTaskDemo {
    public static void main(String[] args) throws Exception {
        // 1. Using Future with ExecutorService (Preferred in production)
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<String> profileFuture = executor.submit(() -> {
            Thread.sleep(1000);
            return "User: Shubhrajit";
        });

        // 2. Using FutureTask with a raw Thread (When no ExecutorService is used)
        Callable<Integer> orderCountTask = () -> {
            Thread.sleep(1500);
            return 12;
        };
        FutureTask<Integer> orderFutureTask = new FutureTask<>(orderCountTask);
        Thread manualThread = new Thread(orderFutureTask);
        manualThread.start();

        // Retrieve both results (blocks until each completes)
        System.out.println(profileFuture.get() + " | Orders: " + orderFutureTask.get());

        executor.shutdown();
    }
}
