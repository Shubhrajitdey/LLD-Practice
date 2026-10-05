package Multithread;
import java.util.concurrent.locks.ReentrantLock;
class TicketBooking {
    // Number of seats initially available
    private int availableSeats = 1;
 
    // Dedicated lock for this shared resource
    private final ReentrantLock lock = new ReentrantLock();
 
    // Public method to book a ticket
    public void bookTicket(String user) {
        System.out.println(user + " is trying to book...");
 
        // Acquire the lock – blocks until available
        lock.lock();
        try {
            System.out.println(user + " acquired lock.");
 
            // Critical section: check and update shared state
            if (availableSeats > 0) {
                System.out.println(user + " successfully booked the ticket.");
                availableSeats--;
            } else {
                System.out.println(user + " could not book the ticket. No seats left.");
            }
        } finally {
            // Always release the lock in a finally block
            System.out.println(user + " is releasing the lock.");
            lock.unlock();
        }
    }
}
public class ThreadLocking {
    public static void main(String[] args) {
        TicketBooking bookingSystem = new TicketBooking();
 
        // Create two threads representing two users trying to book at the same time
        Thread user1 = new Thread(() -> bookingSystem.bookTicket("User 1"));
        Thread user2 = new Thread(() -> bookingSystem.bookTicket("User 2"));
 
        // Start both threads
        user1.start();
        user2.start();
 
        // Wait for both threads to finish
        try {
            user1.join();
            user2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }
}
