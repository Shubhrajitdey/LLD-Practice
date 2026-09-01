/*The Factory Pattern is a creational design pattern 
that provides an interface for creating objects but allows subclasses 
to alter the type of objects that will be created.
 */
package creationalpattern;

import java.util.HashMap;
import java.util.Map;

interface PaymentType{
    void pay(int amount);
}
class CreditCard implements PaymentType{
    @Override
    public void pay(int amount) {
        System.out.println("Paid "+amount+" using Credit Card");
    }
}
class DebitCard implements PaymentType{
    @Override
    public void pay(int amount) {
        System.out.println("Paid "+amount+" using Debit Card");
    }
}
class UPIPayment implements PaymentType{
    @Override
    public void pay(int amount) {
        System.out.println("Paid "+amount+" using UPI");
    }
}

// 2. Abstract Creator (Defines the Factory Method)
/*abstract class PaymentProcessor {
    // The Factory Method
    public abstract PaymentType createPayment();

    // Core business logic relying on the product
    public void processOrder(int amount) {
        PaymentType payment = createPayment();
        payment.pay(amount);
    }
}

// 3. Concrete Creators (Subclasses decide which class to instantiate)
class CreditCardProcessor extends PaymentProcessor {
    @Override
    public PaymentType createPayment() {
        return new CreditCard();
    }
}

class UPIProcessor extends PaymentProcessor {
    @Override
    public PaymentType createPayment() {
        return new UPIPayment();
    }
}*/

class PaymentFactory {
    private static final Map<String, PaymentType> registry = new HashMap<>();

    static {
        registry.put("CREDIT_CARD", new CreditCard());
        registry.put("UPI", new UPIPayment());
        registry.put("DEBIT_CARD", new DebitCard());
    }

    public static PaymentType get(String type) {
        PaymentType payment = registry.get(type.toUpperCase());
        if (payment == null) {
            throw new IllegalArgumentException("Unsupported payment type: " + type);
        }
        return payment;
    }
}

class PaymentService{
    void makePayment(String type,int amount){
        PaymentFactory.get(type).pay(amount);
    }
}
    
public class FactoryDesignPattern {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();
        paymentService.makePayment("CREDIT_CARD", 1000);
        paymentService.makePayment("DEBIT_CARD", 2000);
        paymentService.makePayment("UPI", 3000);
    }
}
