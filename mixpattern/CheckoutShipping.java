package mixpattern;

class TaxService{
    public int taxAmount(){
        return 10;
    }
}
class ShippingRateService{
    public int shippingAmount(){
        return 20;
    }
}

class PromoService{
    public int promoService(){
        return 50;
    }
}

class FacadeImplementation{
    int totalprice;
    public int getShippingEstimate(){
        totalprice += new TaxService().taxAmount();
        totalprice += new ShippingRateService().shippingAmount();
        totalprice += new PromoService().promoService();

        return totalprice;

    }
}

public class CheckoutShipping {
    public static void main(String[] args) {
        FacadeImplementation facadeImplementation = new FacadeImplementation();
        System.out.println("Total Price : "+facadeImplementation.getShippingEstimate());
    }
}
