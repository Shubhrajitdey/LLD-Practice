package Multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomExecutor {
    public String executeComputation(int taskId) throws Exception {
        // Simulate real work (e.g., an external API or DB call)
        Thread.sleep(150);
        return "Task #" + taskId + " handled by " + Thread.currentThread().getName()+ " ";
    }

    public static void main(String[] args) {
        CustomExecutor processor = new CustomExecutor();

        // 1. Define a bounded custom ThreadPoolExecutor
        ThreadPoolExecutor customExecutor = new ThreadPoolExecutor(
                5,                                     // corePoolSize: 5 base workers always active
                10,                                    // maximumPoolSize: hard ceiling of 10 workers
                30L, TimeUnit.SECONDS,                 // keepAliveTime: reclaim temporary workers after 30s
                new ArrayBlockingQueue<>(100),         // workQueue: hold up to 100 pending tasks
                new ThreadPoolExecutor.CallerRunsPolicy() // Rejection policy: slows down caller if completely full
        );

        List<Future<String>> futuresList = new ArrayList<>();

        System.out.println("Submitting 25 tasks to custom bounded pool...");

        // 2. Submit 25 tasks via loop
        for (int i = 1; i <= 65; i++) {
            final int taskId = i;
            Callable<String> task = () -> processor.executeComputation(taskId);

            Future<String> future = customExecutor.submit(task);
            futuresList.add(future);
        }

        // 3. Process the results
        for (Future<String> future : futuresList) {
            try {
                String result = future.get();
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Execution failed: " + e.getMessage());
            }
        }

        // 4. Always shut down custom executors
        customExecutor.shutdown();
    }
}
