package Multithread;

class CoffeeMachine {
    // Flag to indicate buffer status: true → coffee ready, false → cup empty
    private boolean isCoffeeReady = false;
 
    // Method to be run by the producer thread
    public synchronized void makeCoffee() throws InterruptedException {
        // If coffee is already ready, producer must wait (buffer is full)
        while (isCoffeeReady) {
            // Releases lock and waits until notified by consumer
            wait();
        }
 
        // Simulate coffee preparation
        System.out.println("Brewing coffee… by "+Thread.currentThread().getName());
        Thread.sleep(1000); // Simulate time to make coffee
 
        // Set the buffer to full (coffee is now ready)
        isCoffeeReady = true;
        System.out.println("Coffee ready!");
 
        // Notify the consumer thread that coffee is available
        //notify() as it can create deadlock 
        notifyAll();
    }
 
    // Method to be run by the consumer thread
    public synchronized void drinkCoffee() throws InterruptedException {
        // If no coffee is available, consumer must wait (buffer is empty)
        while (!isCoffeeReady) {
            // Releases lock and waits until notified by producer
            wait();
        }
 
        // Simulate coffee consumption
        System.out.println("Drinking coffee… by : "+Thread.currentThread().getName());
        Thread.sleep(1000); // Simulate time to drink coffee
 
        // Set the buffer to empty (coffee has been consumed)
        isCoffeeReady = false;
        System.out.println("Cup emptied - brew next!");
 
        // Notify the producer thread that it can brew again
        //notify() as it can create deadlock 
        notifyAll();
    }
}

public class CoffeeProducerConsumer {
    public static void main(String[] args) {
        CoffeeMachine coffeeMachine = new CoffeeMachine();

        new Thread(()->{
            try {
                while(true){
                    coffeeMachine.makeCoffee();
                    Thread.sleep(50);
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
