package DependencyInjection;

// 1. Abstraction (Contract)
interface PaymentGateway {
    void pay(double amount);
}

// 2. Concrete Implementations
class RazorpayGateway implements PaymentGateway {
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " via Razorpay.");
    }
}

class StripeGateway implements PaymentGateway {
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " via Stripe.");
    }
}

// 3. Client Class (Depends on abstraction, not concrete classes)
class OrderProcessor {
    private final PaymentGateway paymentGateway;

    // Constructor Injection: Guarantees the dependency is provided and immutable
    public OrderProcessor(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void checkout(String orderId, double amount) {
        System.out.println("Processing Order: " + orderId);
        paymentGateway.pay(amount);
    }
}

// 4. Composition Root (Where dependencies are wired)
public class DependencyInjectionDemo {
    public static void main(String[] args) {
        // Inject Razorpay
        PaymentGateway razorpay = new RazorpayGateway();
        OrderProcessor order1 = new OrderProcessor(razorpay);
        order1.checkout("ORD-101", 1500.00);

        System.out.println("---");

        // Swap to Stripe without touching OrderProcessor code
        PaymentGateway stripe = new StripeGateway();
        OrderProcessor order2 = new OrderProcessor(stripe);
        order2.checkout("ORD-102", 2999.50);

        System.out.println("---");

        // Easy Unit Testing with a Mock/Stub via Lambda
        OrderProcessor testOrder = new OrderProcessor(amt -> System.out.println("[MOCK] Simulated payment of ₹" + amt));
        testOrder.checkout("TEST-001", 100.00);
    }
}