/*Implement a furniture ordering system with two styles — Victorian and Modern — 
each producing a matching Sofa, Chair, and Table. The client picks a style once, 
and every piece produced afterward must belong to that same style — no mixing allowed. 
Explain how your design structurally prevents mixing styles by mistake.
 */
package mixpattern;

interface Sofa{
    public void sofaColor();
}
interface Chair{
    public void chairColor();
}
class VictorianSofa implements Sofa{
    @Override
    public void sofaColor(){
        System.err.println("Red victorian sofa");
    }
}
class ModernSofa implements Sofa{
    @Override
    public void sofaColor(){
        System.err.println("Brown mordern sofa");
    }
}
class VictorianChair implements Chair{
    @Override
     public void chairColor(){
        System.err.println("Red victorian chair");
     }
}
class ModernChair implements Chair{
    @Override
    public void chairColor(){
        System.err.println("Brown mordern chair");
    }
}

//factory to group family wise feature
interface FurnitureFactory {
    Sofa createSofa();
    Chair createChair();
}

//concrete implementation of victorian family type
class VictorianFurnitureFactory implements FurnitureFactory {
    public Sofa createSofa() { return new VictorianSofa(); }
    public Chair createChair() { return new VictorianChair(); }
}

//concrete implementation of Modern family type
class ModernFurnitureFactory implements FurnitureFactory {
    public Sofa createSofa() { return new ModernSofa(); }
    public Chair createChair() { return new ModernChair(); }
}

//client side service class to hold the execusion
class FurnitureService {
    private final FurnitureFactory furnitureFactory;
    public FurnitureService(FurnitureFactory furnitureFactory) {
        this.furnitureFactory = furnitureFactory;
    }
    public void buyFurnitureSet(){
        Chair chairobj = furnitureFactory.createChair();
        chairobj.chairColor();
        Sofa sofaobj = furnitureFactory.createSofa();
        sofaobj.sofaColor();
    }
}

public class FurnitureStyleSets {
    public static void main(String[] args) {
        FurnitureService modernfurnitureService = new FurnitureService(new ModernFurnitureFactory());
        modernfurnitureService.buyFurnitureSet();
        FurnitureService victorianfurnitureService = new FurnitureService(new VictorianFurnitureFactory());
        victorianfurnitureService.buyFurnitureSet();
    }
    
}
