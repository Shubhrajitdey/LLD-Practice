package Multithread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class ExpiringTaskWorker{
    private final ReentrantLock lock = new ReentrantLock();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean islocked = false;

    public boolean acquireAndExecute(Runnable task,long timeout){
        boolean accuried = lock.tryLock();
        if(accuried){
            islocked = true;

            scheduler.schedule(() ->{
                if(islocked){
                    System.out.println("Timeout reached - singalling the owner to release");
                    islocked = false;
                }
            },timeout,TimeUnit.MILLISECONDS);
            
        }

        return accuried;
    }

    public void unlockSafely() {
        if (lock.isHeldByCurrentThread()) {
            islocked = false;
            lock.unlock();
            System.out.println("Lock released by " + Thread.currentThread().getName());
        }
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
            if(explock.acquireAndExecute(Thread.currentThread(), 2000)){
                System.out.println("Worker1 acquired lock, going idle...");
                try { 
                    Thread.sleep(5000);

                } 
                catch (InterruptedException ignored) {}
                explock.unlockSafely();
            }
        },"worker1");

        Thread worker2 = new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            while (true) {
                if (explock.acquireAndExecute(Thread.currentThread(),2000)) {
                    System.out.println("ActiveUser booked!");
                    explock.unlockSafely();
                    break;
                } else {
                    System.out.println("ActiveUser still waiting...");
                    try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                }
            }
        }, "ActiveUser");
 
        worker1.start();
        worker2.start();
 
        try {
            worker1.join();
            worker2.join();
        } catch (InterruptedException ignored) {}
 
        explock.shutdown();
    }
}
