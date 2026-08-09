/*The Factory Pattern is a creational design pattern 
that provides an interface for creating objects but allows subclasses 
to alter the type of objects that will be created.
 */
package creationalpattern;
interface paymentType{
    void pay(int amount);
}
class CreditCard implements paymentType{
    @Override
    public void pay(int amount) {
        System.out.println("Paid "+amount+" using Credit Card");
    }
}
class DebitCard implements paymentType{
    @Override
    public void pay(int amount) {
        System.out.println("Paid "+amount+" using Debit Card");
    }
}
class UPIPayment implements paymentType{
    @Override
    public void pay(int amount) {
        System.out.println("Paid "+amount+" using UPI");
    }
}

class PaymentFactory{
    public paymentType getPaymentType(String type){
        if(type.equals("CreditCard")){
            return new CreditCard();
        }else if(type.equals("DebitCard")){
            return new DebitCard();
        }
        return new UPIPayment();
    }
}

class PaymentService{
    public void makePayment(String type,int amount){
        PaymentFactory paymentFactory = new PaymentFactory();
        paymentType payment = paymentFactory.getPaymentType(type);
        payment.pay(amount);
    }
}
    
public class FactoryDesignPattern {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();
        paymentService.makePayment("CreditCard", 1000);
        paymentService.makePayment("DebitCard", 2000);
        paymentService.makePayment("UPI", 3000);
    }
}
