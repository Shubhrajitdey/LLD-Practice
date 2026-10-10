package Multithread;

import java.util.concurrent.Semaphore;

class DbConnection {
    private final Semaphore semaphore;
    private final int maxCapacity;
    private int activeCount = 0;
    private final Object stateLock = new Object();

    public DbConnection(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        // Fair semaphore ensures FIFO queue ordering
        this.semaphore = new Semaphore(maxCapacity,true);
    }

    public void tryToGet(String query) {
        try {
            semaphore.acquire(); // 1. Wait for a slot

            // 2. Atomically record entry AND print
            synchronized (stateLock) {
                activeCount++;
                int available = maxCapacity - activeCount;
                System.out.println("-> " + Thread.currentThread().getName() 
                        + " ENTERED | " + query 
                        + " | Slots left: " + available + " / " + maxCapacity);
            }

            try {
                Thread.sleep(2000); // DB work
            } finally {
                // 3. Atomically record exit AND print BEFORE releasing the permit
                synchronized (stateLock) {
                    activeCount--;
                    int available = maxCapacity - activeCount;
                    System.out.println("<- " + Thread.currentThread().getName() 
                            + " EXITED  | " + query 
                            + " | Slots left: " + available + " / " + maxCapacity);
                }
                semaphore.release(); // 4. Hand off permit to the next waiting thread
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
public class DbConnectionPoolThread {
    public static void main(String[] args) {
        DbConnection dbConnection = new DbConnection(3);
        for(int i = 1; i < 10; i++){
            final String queryName = "Query-" + i;
            new Thread(() -> {
                dbConnection.tryToGet(queryName);
            }, "Worker-" + i).start();
        }
    }    
}
