package ExceptionHandling;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Supplier;

// Custom Exception when Circuit Breaker blocks calls
class CircuitBreakerOpenException extends RuntimeException {
    public CircuitBreakerOpenException(String msg) {
        super(msg);
    }
}

// 1. Lightweight Circuit Breaker (Closed -> Open -> Half-Open)
class CircuitBreaker {
    enum State { CLOSED, OPEN, HALF_OPEN }

    private State state = State.CLOSED;
    private int failureCount = 0;
    private final int failureThreshold;
    private final long openTimeoutMillis;
    private long lastFailureTime = 0;

    public CircuitBreaker(int failureThreshold, long openTimeoutMillis) {
        this.failureThreshold = failureThreshold;
        this.openTimeoutMillis = openTimeoutMillis;
    }

    public synchronized <T> T execute(Supplier<T> action) {
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - lastFailureTime > openTimeoutMillis) {
                state = State.HALF_OPEN;
                System.out.println("[CB] Timeout elapsed -> Switching to HALF_OPEN (testing downstream)");
            } else {
                throw new CircuitBreakerOpenException("[CB] Circuit is OPEN! Failing fast without calling downstream.");
            }
        }

        try {
            T result = action.get();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            throw e;
        }
    }

    private void onSuccess() {
        failureCount = 0;
        if (state == State.HALF_OPEN) {
            state = State.CLOSED;
            System.out.println("[CB] Downstream recovered -> Switching back to CLOSED");
        }
    }

    private void onFailure() {
        failureCount++;
        lastFailureTime = System.currentTimeMillis();
        if (failureCount >= failureThreshold || state == State.HALF_OPEN) {
            state = State.OPEN;
            System.out.println("[CB] Failure threshold reached (" + failureCount + ") -> Circuit TRIPPED to OPEN!");
        }
    }
}

// 2. Resilient Client combining Timeout + Retry (Exponential Backoff) + Circuit Breaker + Fallback
class ResilientPaymentClient {
    private final CircuitBreaker circuitBreaker = new CircuitBreaker(2, 1500);
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public String callWithResilience(Supplier<String> remoteCall, int maxRetries, long timeoutMs) {
        try {
            return circuitBreaker.execute(() -> executeWithRetryAndTimeout(remoteCall, maxRetries, timeoutMs));
        } catch (Exception e) {
            // Graceful Fallback when retries exhaust or circuit is open
            System.out.println("[FALLBACK] Triggered due to: " + e.getMessage());
            return "QUEUED_FOR_ASYNC_PROCESSING (Fallback Response)";
        }
    }

    private String executeWithRetryAndTimeout(Supplier<String> remoteCall, int maxRetries, long timeoutMs) {
        int attempt = 0;
        long backoffMs = 200;

        while (true) {
            attempt++;
            try {
                // Enforce strict Timeout on external call
                Future<String> future = executor.submit(remoteCall::get);
                return future.get(timeoutMs, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                System.out.println("[RETRY] Attempt " + attempt + " timed out after " + timeoutMs + "ms");
            } catch (Exception e) {
                System.out.println("[RETRY] Attempt " + attempt + " failed: " + e.getCause());
            }

            if (attempt >= maxRetries) {
                throw new RuntimeException("All " + maxRetries + " retry attempts exhausted.");
            }

            try {
                System.out.println("[BACKOFF] Waiting " + backoffMs + "ms before retry...");
                Thread.sleep(backoffMs);
                backoffMs *= 2; // Exponential backoff
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted during backoff", ie);
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}

// 3. Simulation
public class ResilientSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        ResilientPaymentClient client = new ResilientPaymentClient();
        Random random = new Random();

        // Simulate a flaky downstream payment service (slow/failing initially, recovers later)
        Supplier<String> flakyDownstreamService = () -> {
            try {
                Thread.sleep(400); // Simulates slow network latency (> 250ms timeout)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "PAYMENT_SUCCESS";
        };

        Supplier<String> healthyDownstreamService = () -> "PAYMENT_SUCCESS_FAST";

        System.out.println("=== Request 1: Slow Downstream (Triggers Timeout -> Retries -> Fallback) ===");
        System.out.println("Result: " + client.callWithResilience(flakyDownstreamService, 2, 250));

        System.out.println("\n=== Request 2: Second Failure (Trips Circuit Breaker to OPEN) ===");
        System.out.println("Result: " + client.callWithResilience(flakyDownstreamService, 2, 250));

        System.out.println("\n=== Request 3: Immediate Request While OPEN (Fails Fast, Zero Network Load) ===");
        System.out.println("Result: " + client.callWithResilience(healthyDownstreamService, 2, 250));

        System.out.println("\n--- Sleeping 1.6s for Circuit Breaker Cooldown ---");
        Thread.sleep(1600);

        System.out.println("\n=== Request 4: After Cooldown (HALF_OPEN -> Probes Healthy Service -> Recovers to CLOSED) ===");
        System.out.println("Result: " + client.callWithResilience(healthyDownstreamService, 2, 250));

        client.shutdown();
    }
}