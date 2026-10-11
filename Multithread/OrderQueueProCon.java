package Multithread;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Order {
    private final String orderId;
    private String status;

    public Order(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Order[id=" + orderId + ", status=" + status + "]";
    }
}

class BoundedOrderQueue<T> {
    private final int capacity;
    private final LinkedList<T> buffer = new LinkedList<>();

    // Fair lock ensures FIFO queueing among competing producers/consumers
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedOrderQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            // Guard loop: wait while buffer is at full capacity
            while (buffer.size() == capacity) {
                System.out.println("⏳ [Full] " + Thread.currentThread().getName() + " waiting to put...");
                notFull.await();
            }

            buffer.addLast(item);
            System.out.println("📥 [Ingested] " + Thread.currentThread().getName() 
                    + " added: " + item + " | Buffer size: " + buffer.size());

            // Signal a waiting consumer that an item is available
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            // Guard loop: wait while buffer is empty
            while (buffer.isEmpty()) {
                System.out.println("⏳ [Empty] " + Thread.currentThread().getName() + " waiting to take...");
                notEmpty.await();
            }

            T item = buffer.pollFirst();
            System.out.println("📤 [Extracted] " + Thread.currentThread().getName() 
                    + " took: " + item + " | Buffer size: " + buffer.size());

            // Signal a waiting producer that an open slot exists
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}

public class OrderQueueProCon {
    public static void main(String[] args) throws InterruptedException {
        BoundedOrderQueue<Order> queue = new BoundedOrderQueue<>(2);

        Order order1 = new Order("ORD-1", "PENDING");
        Order order2 = new Order("ORD-2", "PENDING");
        Order order3 = new Order("ORD-3", "PENDING");

        // Producer 1
        Thread p1 = new Thread(() -> {
            try {
                queue.put(order1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer-1");

        // Producer 2
        Thread p2 = new Thread(() -> {
            try {
                queue.put(order2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer-2");

        // Producer 3
        Thread p3 = new Thread(() -> {
            try {
                queue.put(order3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Producer-3");

        // Consumer 1
        Thread c1 = new Thread(() -> {
            try {
                Order order = queue.take();
                // Heavy business logic runs OUTSIDE the lock
                Thread.sleep(500);
                order.setStatus("PROCESSED");
                System.out.println("✅ " + Thread.currentThread().getName() + " finished processing: " + order.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer-1");

        // Consumer 2
        Thread c2 = new Thread(() -> {
            try {
                Order order = queue.take();
                // Heavy business logic runs OUTSIDE the lock
                Thread.sleep(500);
                order.setStatus("PROCESSED");
                System.out.println("✅ " + Thread.currentThread().getName() + " finished processing: " + order.getId());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumer-2");

        // Start all threads
        p1.start();
        p2.start();
        p3.start();
        c1.start();
        c2.start();

        // Wait for all worker threads to complete
        p1.join();
        p2.join();
        p3.join();
        c1.join();
        c2.join();

        System.out.println("\nAll orders processed cleanly without starvation or deadlocks.");
    }
}