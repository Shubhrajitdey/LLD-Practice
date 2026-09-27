package Multithread;

import java.util.concurrent.*;

// Using the newFixedThreadPool to manage threads
class EmailService {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool with 10 threads

    // Method to send email
    public static void sendEmail(String recipient) {
        executor.execute(() -> {
            System.out.println("Sending email to " + recipient + " on " + Thread.currentThread().getName());
            try {
                // Simulate dummy work (sending an email)
                Thread.sleep(1000);  // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Handle interruption
            }
            System.out.println("Email sent to " + recipient);
        });
    }

    // Main method to simulate sending multiple emails
    public static void main(String[] args) {
        for (int i = 1; i <= 25; i++) {
            sendEmail("user" + i + "@gmail.com");  // Send email to 1000 users
        }
        executor.shutdown();  // Gracefully shut down the executor
    }
}