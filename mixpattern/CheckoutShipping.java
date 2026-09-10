package mixpattern;

class TaxService{
    public int taxAmount(){
        return 20;
    }
}
class ShippingRateService{
    public int shippingAmount(){
        return 20;
    }
}

class PromoService{
    public int getDiscount(){
        return 50;
    }
}

class FacadeImplementation {
    private TaxService taxService = new TaxService();
    private ShippingRateService shippingRateService = new ShippingRateService();
    private PromoService promoService = new PromoService();

    public int getShippingEstimate() {
        int total = taxService.taxAmount();
        total += shippingRateService.shippingAmount();
        total -= promoService.getDiscount();
        return total;
    }
}

public class CheckoutShipping {
    public static void main(String[] args) {
        FacadeImplementation facadeImplementation = new FacadeImplementation();
        System.out.println("Total Price : "+facadeImplementation.getShippingEstimate());
    }
}
