package ExceptionHandling;

import java.util.concurrent.*;

public class ResilientApiCallDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        int maxRetries = 2;
        String response = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // 1. Timeout: Fail fast if downstream takes > 200ms
                Future<String> future = executor.submit(() -> {
                    Thread.sleep(300); // Simulates slow external API
                    return "200 OK: Order Confirmed";
                });
                response = future.get(200, TimeUnit.MILLISECONDS);
                break; // Success -> exit retry loop
            } catch (Exception e) {
                // 2. Retry: Catch timeout/failure and retry
                System.out.println("Attempt " + attempt + " failed (" + e.getClass().getSimpleName() + ")");
            }
        }

        // 3. Fallback: Degrade gracefully if all retries fail
        if (response == null) {
            response = "202 ACCEPTED (Fallback): Order queued for async processing";
        }

        System.out.println("Final Response -> " + response);
        executor.shutdown();
    }
}
