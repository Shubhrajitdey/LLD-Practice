package Multithread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

class OrderProcessingGateway{
    private volatile boolean isRunning = true;
    private final AtomicLong successfulOrders = new AtomicLong(0);

    private final Map<String, Integer> inventory = new HashMap<>();
    private final Map<String, Double> userWallets = new HashMap<>();
    private final Object transactionLock = new Object();

    public OrderProcessingGateway() {
        inventory.put("LAPTOP_01", 12);
        userWallets.put("USER_101", 5000.0);
    }

    public boolean processOrder(String userId, String itemId, double price) {
        // Fast read of volatile flag: rejects requests immediately if shutting down
        if (!isRunning) {
            throw new IllegalStateException("Gateway is offline. Rejecting request.");
        }

        // 3. synchronized: Multi-variable invariant check and mutation
        synchronized (transactionLock) {
            int stock = inventory.getOrDefault(itemId, 0);
            double balance = userWallets.getOrDefault(userId, 0.0);

            if (stock <= 0 || balance < price) {
                return false; // Insufficient stock or funds
            }

            // Compound state mutation must be atomic together
            inventory.put(itemId, stock - 1);
            userWallets.put(userId, balance - price);
        }

        // Lock-free counter increment outside the critical block
        successfulOrders.incrementAndGet();
        return true;
    }
    public void shutdown() {
        // Single write to volatile variable visible immediately to all workers
        this.isRunning = false;
    }

    public long getSuccessfulOrderCount() {
        return successfulOrders.get();
    }
    public int getRemainingStock(String itemId) {
        synchronized (transactionLock) {
            return inventory.getOrDefault(itemId, 0);
        }
    }

    public double getRemainingBalance(String userId) {
        synchronized (transactionLock) {
            return userWallets.getOrDefault(userId, 0.0);
        }
    }
}

public class ThreadSafety {
    public static void main(String[] args) throws InterruptedException{
        OrderProcessingGateway opg = new OrderProcessingGateway();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                boolean ok = opg.processOrder("USER_101", "LAPTOP_01", 100);
                System.out.println("T1 Order " + (i + 1) + ": " + (ok ? "SUCCESS" : "REJECTED (insufficient funds/stock)"));
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                boolean ok = opg.processOrder("USER_101", "LAPTOP_01", 100);
                System.out.println("T2 Order " + (i + 1) + ": " + (ok ? "SUCCESS" : "REJECTED (insufficient funds/stock)"));
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Total Successful: " + opg.getSuccessfulOrderCount());
    }
}
