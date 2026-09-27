package Multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskProcessor {
    public String executeComputation(int taskId){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "Task #"+taskId+" Completed By "+Thread.currentThread().getName();
    }
    public static void main(String[] args) {
        TaskProcessor taskProcessor = new TaskProcessor();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<String>> futureresultList = new ArrayList<>();

        System.out.println("Submitting 25 tasks to 10 worker threads...");

        for(int i = 0; i < 25; i++){
            final int taskId = i;
            Callable<String> task = () -> taskProcessor.executeComputation(taskId);
            Future<String> res = executorService.submit(task);
            futureresultList.add(res);
        }

        System.out.println("All tasks submitted! Fetching and printing results:\n");

        for(Future<String> futureObj : futureresultList){
            try {
                String result = futureObj.get();
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } 
        }

        executorService.shutdown();

        System.out.println("All tasks! Fetched and printed results:\n");
    }
}
