package ExceptionHandling;

class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String msg) { super(msg); }
}

class SimpleRateLimiter {
    private final int maxRequests;
    private int count = 0;
    private long windowStart = System.currentTimeMillis();

    public SimpleRateLimiter(int maxRequests) { this.maxRequests = maxRequests; }

    public synchronized void checkLimit() {
        long now = System.currentTimeMillis();
        if (now - windowStart > 1000) { // Reset every 1 second
            count = 0;
            windowStart = now;
        }
        if (++count > maxRequests) {
            throw new RateLimitExceededException("429 Too Many Requests");
        }
    }
}

public class ShortRateLimitDemo {
    public static void main(String[] args) {
        SimpleRateLimiter limiter = new SimpleRateLimiter(2); // Max 2 req/sec

        for (int i = 1; i <= 4; i++) {
            try {
                limiter.checkLimit();
                System.out.println("Req " + i + ": [200 OK] Processed");
            } catch (RateLimitExceededException e) {
                System.out.println("Req " + i + ": [FALLBACK] " + e.getMessage() + " -> Queued");
            }
        }
    }
}
