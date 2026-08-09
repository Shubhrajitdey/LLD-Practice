/*The Abstract Factory Pattern is a creational design pattern 
that provides an interface for creating families of related or dependent objects 
without specifying their concrete classes. */
package creationalpattern;


interface PaymentGateway{
    void processPayment(double amount);
}

interface Invoice {
    void generateInvoice(double amount);
}

//india
class RazorpayPaymentGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through Razorpay.");
    }
}
class PayUPaymentGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through PayU.");
    }
}
class RazorpayInvoice implements Invoice {
    @Override
    public void generateInvoice(double amount) {
        System.out.println("Generating Razorpay invoice for $" + amount);
    }
}
class PayUInvoice implements Invoice {
    @Override
    public void generateInvoice(double amount) {
        System.out.println("Generating PayU invoice for $" + amount);
    }       
}

//us
class PayPalPaymentGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through PayPal.");
    }
}
class StripePaymentGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment of $" + amount + " through Stripe.");
    }
}

class PayPalInvoice implements Invoice {
    @Override
    public void generateInvoice(double amount) {
        System.out.println("Generating PayPal invoice for $" + amount);
    }
}
class StripeInvoice implements Invoice {
    @Override
    public void generateInvoice(double amount) {
        System.out.println("Generating Stripe invoice for $" + amount);
    }
}

interface PaymentRegion {
    PaymentGateway createPaymentGateway(String gatewayType);
    Invoice createInvoice(String gatewayType);
}

class IndiaPaymentFactory implements PaymentRegion {

    @Override
    public PaymentGateway createPaymentGateway(String gatewayType) {
        if (gatewayType.equalsIgnoreCase("Razorpay")) {
            return new RazorpayPaymentGateway();
        } else if (gatewayType.equalsIgnoreCase("PayU")) {
            return new PayUPaymentGateway();
        }
        throw new IllegalArgumentException("Unknown payment gateway type: " + gatewayType);
    }

    @Override
    public Invoice createInvoice(String gatewayType) {
        if (gatewayType.equalsIgnoreCase("Razorpay")) {
            return new RazorpayInvoice();
        } else if (gatewayType.equalsIgnoreCase("PayU")) {
            return new PayUInvoice();
        }
        throw new IllegalArgumentException("Unknown invoice type: " + gatewayType);
    }
}
class USPaymentFactory implements PaymentRegion {
    @Override
    public PaymentGateway createPaymentGateway(String gatewayType) {
        if (gatewayType.equalsIgnoreCase("PayPal")) {
            return new PayPalPaymentGateway();
        } else if (gatewayType.equalsIgnoreCase("Stripe")) {
            return new StripePaymentGateway();
        }
        throw new IllegalArgumentException("Unknown payment gateway type: " + gatewayType);
    }

    @Override
    public Invoice createInvoice(String gatewayType) {
        if (gatewayType.equalsIgnoreCase("PayPal")) {
            return new PayPalInvoice();
        } else if (gatewayType.equalsIgnoreCase("Stripe")) {
            return new StripeInvoice();
        }
        throw new IllegalArgumentException("Unknown invoice type: " + gatewayType);
    }
}

class checkOutService {
    private final PaymentRegion paymentRegion;

    public checkOutService(PaymentRegion paymentRegion) {
        this.paymentRegion = paymentRegion;
    }

    public void checkout(String gatewayType, double amount) {
        PaymentGateway paymentGateway = paymentRegion.createPaymentGateway(gatewayType);
        Invoice invoice = paymentRegion.createInvoice(gatewayType);

        paymentGateway.processPayment(amount);
        invoice.generateInvoice(amount);
    }
}


public class AbstractFactoryPattern {
    public static void main(String[] args) {
        checkOutService indiaCheckout = new checkOutService(new IndiaPaymentFactory());
        indiaCheckout.checkout("Razorpay", 100.0);

        checkOutService usCheckout = new checkOutService(new USPaymentFactory());
        usCheckout.checkout("PayPal", 200.0);       
    }
}
