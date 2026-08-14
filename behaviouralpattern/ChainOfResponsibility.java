package behaviouralpattern;


//abstract handler
abstract class SupportHandler{
    protected SupportHandler nextHandler;
    public void setNextHandler(SupportHandler nextHandler){
        this.nextHandler = nextHandler;
    }
    abstract void handleRequest(String requestType);
}

//concrete handler
class GeneralSupport extends SupportHandler{
    public void handleRequest(String requestType){
        if(requestType.equalsIgnoreCase("general")){
            System.out.println("General support handling query support");
        }else if (nextHandler != null) {
            nextHandler.handleRequest(requestType);
        }
    }
}

//concrete handler
class BillingSupport extends SupportHandler{
    public void handleRequest(String requestType){
        if(requestType.equalsIgnoreCase("refund")){
            System.out.println("Billing support handling query support");
        }else if (nextHandler != null) {
            nextHandler.handleRequest(requestType);
        }
    }
}

// Concrete Handler for Technical Support
class TechnicalSupport extends SupportHandler {
    public void handleRequest(String requestType) {
        if (requestType.equalsIgnoreCase("technical")) {
            System.out.println("TechnicalSupport: Handling technical issue");
        } else if (nextHandler != null) {
            nextHandler.handleRequest(requestType);
        }
    }
}

//concrete handler
class DeliverySupport extends SupportHandler{
    public void handleRequest(String requestType){
        if(requestType.equalsIgnoreCase("delivery")){
            System.out.println("Delivery support handling query support");
        }else if (nextHandler != null) {
            nextHandler.handleRequest(requestType);
        }
    }
}

public class ChainOfResponsibility {
    public static void main(String[] args) {
        SupportHandler general = new GeneralSupport();
        SupportHandler billing = new BillingSupport();
        SupportHandler technical = new TechnicalSupport();
        SupportHandler delivery = new DeliverySupport();

        //client decide Setting up the chain: general -> billing -> technical -> delivery
        general.setNextHandler(billing);
        billing.setNextHandler(technical);
        technical.setNextHandler(delivery);

        general.handleRequest("technical");
    }
}
