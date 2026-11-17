package ExceptionHandling;

import java.io.IOException;

// 1. Custom Exception (Domain-specific error)
class OtpDeliveryFailedException extends RuntimeException {
    public OtpDeliveryFailedException(String userId, String reason) {
        super("OTP delivery failed for user [" + userId + "]: " + reason);
    }
}

class CustomerNotPlusException extends RuntimeException {
    public CustomerNotPlusException(String userId) {
        super("User " + userId + " does not have a Plus subscription.");
    }
}

// 2. Checked Exception Demonstration (External I/O / Recoverable)
class AuditLogger {
    public void logTransaction(String message) throws IOException {
        if (message == null || message.isEmpty()) {
            throw new IOException("Audit log payload cannot be empty");
        }
        System.out.println("[AUDIT LOG]: " + message);
    }
}

// 3. Fail-Fast vs. Fail-Safe Service
class CheckoutService {
    private final AuditLogger auditLogger = new AuditLogger();

    // Fail-Fast: Validate early and halt immediately on invalid state/input
    public void processPaymentFailFast(String userId, double amount, boolean otpDelivered) {
        if (userId == null || amount <= 0) {
            // Unchecked Exception (IllegalArgumentException)
            throw new IllegalArgumentException("Invalid userId or payment amount.");
        }

        if (!otpDelivered) {
            // Custom Exception
            throw new OtpDeliveryFailedException(userId, "SMS Gateway Timeout");
        }

        // Handling Checked Exception
        try {
            auditLogger.logTransaction("Payment of ₹" + amount + " succeeded for " + userId);
        } catch (IOException e) {
            System.err.println("[ALERT] Audit logging failed, but payment went through: " + e.getMessage());
        }
    }

    // Fail-Safe: Degrade gracefully with a fallback instead of crashing
    public String fetchProductRecommendationFailSafe(String userId, boolean dbDown) {
        try {
            if (dbDown) {
                throw new RuntimeException("Recommendation DB unreachable");
            }
            return "Personalized Tech Deals for " + userId;
        } catch (Exception e) {
            System.out.println("[WARN] Falling back to default recommendations due to: " + e.getMessage());
            return "Default Bestsellers Catalog";
        }
    }
}

public class ExceptionHandlingDemo {
    public static void main(String[] args) {
        CheckoutService checkoutService = new CheckoutService();

        // Scenario 1: Happy Path
        System.out.println("--- 1. Happy Path ---");
        checkoutService.processPaymentFailFast("USR-101", 2499.00, true);

        // Scenario 2: Fail-Fast with Custom Exception (OTP Failure)
        System.out.println("\n--- 2. Fail-Fast (Custom Exception) ---");
        try {
            checkoutService.processPaymentFailFast("USR-102", 4999.00, false);
        } catch (OtpDeliveryFailedException e) {
            System.out.println("User Friendly Message: Could not send OTP. Please click 'Resend OTP'.");
            System.out.println("Internal Root Cause: " + e.getMessage());
        }

        // Scenario 3: Fail-Fast with Unchecked Exception (Bad Input)
        System.out.println("\n--- 3. Fail-Fast (Unchecked Exception) ---");
        try {
            checkoutService.processPaymentFailFast(null, -50.0, true);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Error Caught: " + e.getMessage());
        }

        // Scenario 4: Fail-Safe Degradation
        System.out.println("\n--- 4. Fail-Safe Fallback ---");
        String feed = checkoutService.fetchProductRecommendationFailSafe("USR-101", true);
        System.out.println("Displayed to User: " + feed);
    }
}
