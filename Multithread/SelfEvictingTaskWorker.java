package Multithread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class ExpiringTaskWorker{
    private final ReentrantLock lock = new ReentrantLock();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public boolean acquireAndExecute(Runnable task,long timeout){
        if (!lock.tryLock()) {
            return false;
        }
        Thread currentWorker = Thread.currentThread();
        System.out.println("inside acquireAndExecute with thread :"+ currentWorker.getName());
        //future scheduler to interrupt in case task not completed within the time
        ScheduledFuture<?> timeoutFuture = scheduler.schedule(() -> {
            System.out.println("Timeout reached – interrupting " + currentWorker.getName());
            currentWorker.interrupt();
        }, timeout, TimeUnit.MILLISECONDS);
        System.out.println("Scheduler thread timer started to waking up :"+ currentWorker.getName());
        //execute actual task 
        try {
            task.run();
        } finally {
            // Cancel the scheduled timeout task if we finished before the timer fired
            timeoutFuture.cancel(false);
            // Re-clear interrupt flag if it arrived late, and release lock
            lock.unlock();
            System.out.println("Lock released by " + currentWorker.getName());
        }
        return true;
    }
 
    // Graceful shutdown for the scheduler
    public void shutdown() {
        scheduler.shutdownNow();
    }
}


public class SelfEvictingTaskWorker {
    public static void main(String[] args) {
        ExpiringTaskWorker explock = new ExpiringTaskWorker();

        Thread worker1 = new Thread(() ->{
            explock.acquireAndExecute(() ->{
                System.out.println("Worker1 starting the long task.....");
                try { 
                    Thread.sleep(20000);
                    System.out.println("Worker1 completed task.");
                } 
                catch (InterruptedException ignored) {
                    System.out.println("Worker1 was interrupted due to timeout!");
                }
            }, 10000);
        },"worker1");

        Thread worker2 = new Thread(() -> {
            try { 
                Thread.sleep(1000); 
            }catch (InterruptedException ignored) {

            }
            while (true) {
                boolean done = explock.acquireAndExecute(() -> {
                    System.out.println("Worker2 got the lock and executed successfully!");
                }, 2000);

                if (done) {
                    break;
                } else {
                    System.out.println("Worker2 waiting for lock...");
                    try { 
                        Thread.sleep(1000); 
                    }catch(InterruptedException ignored) {

                    }
                }
            }
        }, "Worker-2");
 
        worker1.start();
        worker2.start();
 
        try {
            worker1.join();
            worker2.join();
        } catch (InterruptedException ignored) {}
 
        explock.shutdown();
    }
}
