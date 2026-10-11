package Multithread;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class CoffeeMachineType {
    private boolean isCoffeeReady = false;
    
    // Pass 'true' to enforce FIFO ordering across all competing threads
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public void makeCoffee() throws InterruptedException {
        lock.lock();
        try {
            while (isCoffeeReady) {
                notFull.await();
            }
            System.out.println("Brewing coffee… by " + Thread.currentThread().getName());
            Thread.sleep(1000);
            isCoffeeReady = true;
            System.out.println("Coffee ready!");
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public void drinkCoffee() throws InterruptedException {
        lock.lock();
        try {
            while (!isCoffeeReady) {
                notEmpty.await();
            }
            System.out.println("Drinking coffee… by : " + Thread.currentThread().getName());
            Thread.sleep(1000);
            isCoffeeReady = false;
            System.out.println("Cup emptied - brew next!");
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }
}
public class CoffeProducerConsumerWithReEnterentLock {
    public static void main(String[] args) {
        CoffeeMachineType coffeeMachine = new CoffeeMachineType();

        new Thread(()->{
            try {
                while(true){
                    coffeeMachine.makeCoffee();
                }
            } catch (InterruptedException e) {}
        }, "Producer Thread 1").start();

        new Thread(()->{
            try {
                while(true){
                    coffeeMachine.drinkCoffee();
                }
            } catch (InterruptedException e) {}
        }, "Consumer Thread 1").start();

        new Thread(()->{
            try {
                while(true){
                    coffeeMachine.makeCoffee();
                }
            } catch (InterruptedException e) {}
        }, "Producer Thread 2").start();

        new Thread(()->{
            try {
                while(true){
                    coffeeMachine.makeCoffee();
                }
            } catch (InterruptedException e) {}
        }, "Producer Thread 3").start();
    }
}
