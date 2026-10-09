package Multithread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class BookTicket{
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ReentrantLock lock = new ReentrantLock();
    private int seatAvailable = 1;

    public void bookticket(String username,long simulateIdleMs, long maxHoldTimeSec, long waitTimeoutSec){
        try{
            if(lock.tryLock(waitTimeoutSec,TimeUnit.SECONDS)){
                System.out.println("-> " + username + " ACQUIRED lock!");
                Thread currentThread = Thread.currentThread();
                
                ScheduledFuture<?> evictionTask = scheduler.schedule(() -> {
                    System.out.println("Lease expired for " + username + "! interrupting...");
                    currentThread.interrupt();
                }, maxHoldTimeSec, TimeUnit.SECONDS);
                
                try {
                    if(simulateIdleMs > 0){
                        System.out.println(username + " idle for " + (simulateIdleMs / 1000) + "s (lease: " + maxHoldTimeSec + "s)...");
                        Thread.sleep(simulateIdleMs);
                    }
                    if (seatAvailable > 0) {
                        System.out.println("Booking the seat for: " + username);
                        seatAvailable--;
                    } else {
                        System.out.println("No seat available for: " + username);
                    }
                }catch (InterruptedException e) {
                    System.out.println(username + " was EVICTED due to idle timeout! Booking aborted.");
                } finally {
                    evictionTask.cancel(false);
                    Thread.interrupted();
                    System.out.println("<- " + username + " RELEASING lock.");
                    lock.unlock(); 
                }
            }else{
                System.out.println(username+" Could not acquire lock within "+ waitTimeoutSec + " seconds.");
                System.out.println("Booking skip SORRY!! "+username);
            }
        }catch (Exception e) {
            System.out.println(username + " was interrupted while waiting.");
            Thread.currentThread().interrupt();
        }
        
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}

public class BookTicketWithoutGurantee {
    public static void main(String[] args) {
        BookTicket instance = new BookTicket();
        Thread worker1 = new Thread(()->{
            System.out.println("Novi looking for lock...");
            instance.bookticket("Novi",6000,3,1);
        },"Novi-Thread");
        Thread worker2 = new Thread(()->{
            System.out.println("Sinchu looking for lock...");
            instance.bookticket("Sinchu",0,0,2);
        },"Sinchu-Thread");

        Thread worker3 = new Thread(()->{
            System.out.println("Dora looking for lock...");
            instance.bookticket("Dora",0,3,4);
        },"Dora-Thread");

        try {
            worker1.start();
            worker2.start();
            worker3.start();
            

            worker1.join();
            worker2.join();
            worker3.join();

            instance.shutdown();
        } catch (Exception e) {}
        
    }
}
