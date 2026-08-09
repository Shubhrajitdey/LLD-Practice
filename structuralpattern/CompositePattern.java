/*The Composite Pattern is a structural design pattern that allows you to compose objects 
into tree structures to represent part-whole hierarchies. It lets clients treat individual objects 
and compositions of objects uniformly.
 */
package structuralpattern;
import java.util.*;
interface CartItem{
    double getPrice();
    void display();
}
class Product implements CartItem{
    private String name;
    private double price;
    
    public Product(String name, double price){
        this.name = name;
        this.price = price;
    }
    
    @Override
    public double getPrice(){
        return price;
    }
    
    @Override
    public void display(){
        System.out.println("Product: " + name + ", Price: " + price);
    }
}
class BundleProduct implements CartItem{
    private String name;
    private List<CartItem> items;
    
    public BundleProduct(String name){
        this.name = name;
        this.items = new ArrayList<>();
    }
    
    public void addItem(CartItem item){
        items.add(item);
    }
    
    @Override
    public double getPrice(){
        double totalPrice = 0;
        for(CartItem item : items){
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }
    
    @Override
    public void display(){
        System.out.println("Bundle: " + name);
        for(CartItem item : items){
            item.display();
        }
    }
}
public class CompositePattern {
    public static void main(String[] args) {
        Product product1 = new Product("Laptop", 1000);
        Product product2 = new Product("Mouse", 50);
        Product product3 = new Product("Keyboard", 80);
        
        BundleProduct bundle1 = new BundleProduct("Office Set");
        bundle1.addItem(product1);
        bundle1.addItem(product2);
        
        BundleProduct bundle2 = new BundleProduct("Gaming Set");
        bundle2.addItem(product1);
        bundle2.addItem(product3);
        
        System.out.println("Price of " + product1.getPrice());
        System.out.println("Price of " + product2.getPrice());
        System.out.println("Price of " + product3.getPrice());
        
        System.out.println("Price of " + bundle1.getPrice());
        System.out.println("Price of " + bundle2.getPrice());
        
        System.out.println("\nDisplaying items in " + bundle1.getPrice() + ":");
        bundle1.display();
        
        System.out.println("\nDisplaying items in " + bundle2.getPrice() + ":");
        bundle2.display();
    }
}
