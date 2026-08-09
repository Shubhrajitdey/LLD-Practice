/*The Adapter Pattern allows incompatible interfaces to work together 
by acting as a translator or wrapper around an existing class. 
It converts the interface of a class into another interface that a client expects. 
It acts as a bridge between the Target interface (expected by the client) and 
the Adaptee (an existing class with a different interface). 
This structural wrapping enables integration and compatibility across diverse systems.
*/
package structuralpattern;

interface PaymentGateway{
    void processPayment(double amount);
}

class PayUPaymentGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through PayU.");
    }
}

class PayPalPaymentGateway {
    public void makePayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through PayPal.");
    }
}

class PayPalPaymentGatewayAdapter implements PaymentGateway {
    private final PayPalPaymentGateway payPalPaymentGateway;

    public PayPalPaymentGatewayAdapter() {
        this.payPalPaymentGateway = new PayPalPaymentGateway();
    }

    @Override
    public void processPayment(double amount) {
        payPalPaymentGateway.makePayment(amount);
    }
}

class PaymentService {
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public void makePayment(double amount) {
        paymentGateway.processPayment(amount);
    }
}



public class AdapterPattern {
    public static void main(String[] args) {
        PaymentService payUPaymentService = new PaymentService(new PayUPaymentGateway());
        payUPaymentService.makePayment(100.0);

        PaymentService payPalPaymentService = new PaymentService(new PayPalPaymentGatewayAdapter());
        payPalPaymentService.makePayment(200.0);


    }
}
