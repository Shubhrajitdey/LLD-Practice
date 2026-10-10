/*In a distributed or microservice-backed inventory system, ordering locks by ID isn't always feasible (e.g., resources are acquired dynamically based on user cart contents, external API calls, or across multiple microservices where a global total order is hard to enforce).

Instead of strict ordering, production systems break deadlock by breaking the "Hold and Wait" condition:

A thread tries to acquire the first resource.

It attempts to acquire the second resource with a timeout.

If it fails to acquire the second lock, it must not hold onto the first lock! It immediately releases the first lock, waits for a randomized short duration (backoff), and retries from scratch. */
package Multithread;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class InventoryItem {
    private final String sku;
    private int stock = 10;
    final ReentrantLock lock = new ReentrantLock();

    public InventoryItem(String sku) {
        this.sku = sku;
    }

    public String getSku() { return sku; }
    public int getStock() { return stock; }

    public void deduct(int count) { 
        stock -= count; 
    }
}

class OrderService {
    private static final int MAX_RETRIES = 10;

    public boolean reserveBoth(InventoryItem item1, InventoryItem item2, int qty) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            boolean acquired1 = false;
            boolean acquired2 = false;

            try {
                // 1. Try to get first lock with a small timeout
                acquired1 = item1.lock.tryLock(50, TimeUnit.MILLISECONDS);
                // 2. Try to get second lock with a small timeout
                acquired2 = item2.lock.tryLock(50, TimeUnit.MILLISECONDS);

                // 3. If both were successfully acquired
                if (acquired1 && acquired2) {
                    if (item1.getStock() >= qty && item2.getStock() >= qty) {
                        item1.deduct(qty);
                        item2.deduct(qty);
                        System.out.println("✅ " + Thread.currentThread().getName() 
                                + " RESERVED " + qty + " of [" + item1.getSku() + "] & [" + item2.getSku() + "]");
                        return true;
                    } else {
                        System.out.println("❌ " + Thread.currentThread().getName() + " Insufficient stock.");
                        return false;
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } finally {
                // 4. CLEANUP: If we only got one lock (or both), release whatever we hold!
                if (acquired2) {
                    item2.lock.unlock();
                }
                if (acquired1) {
                    item1.lock.unlock();
                }
            }

            // 5. BACKOFF: Failed to get both locks. Sleep a random time to avoid livelock!
            try {
                int backoffMs = ThreadLocalRandom.current().nextInt(20, 80);
                System.out.println("⚠️ " + Thread.currentThread().getName() 
                        + " couldn't get both locks (Attempt " + attempt + "). Backing off for " + backoffMs + "ms...");
                Thread.sleep(backoffMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        System.out.println("⛔ " + Thread.currentThread().getName() + " Exceeded max retries.");
        return false;
    }
}

public class OrderFulfillmentService {
    public static void main(String[] args) throws InterruptedException {
        InventoryItem grocery = new InventoryItem("grocery");
        InventoryItem milk = new InventoryItem("milk");
        OrderService os = new OrderService();

        // Thread 1 reserves milk then grocery
        Thread t1 = new Thread(() -> {
            os.reserveBoth(milk, grocery, 5);
        }, "Thread-1");

        // Thread 2 reserves grocery then milk
        Thread t2 = new Thread(() -> {
            os.reserveBoth(grocery, milk, 5);
        }, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Remaining - Grocery: " + grocery.getStock() + ", Milk: " + milk.getStock());
    }
}