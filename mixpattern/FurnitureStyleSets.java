/*Implement a furniture ordering system with two styles — Victorian and Modern — 
each producing a matching Sofa, Chair, and Table. The client picks a style once, 
and every piece produced afterward must belong to that same style — no mixing allowed. 
Explain how your design structurally prevents mixing styles by mistake.
 */
package mixpattern;

import java.util.*;

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

interface FurnitureType{
    public void selectFurniture();
}

class VictorianFurniture implements FurnitureType{
    @Override
    public void selectFurniture(){
        Sofa sofaobj = new VictorianSofa();
        sofaobj.sofaColor();

        Chair chairObj = new VictorianChair();
        chairObj.chairColor();
    }
}

class MordernFurniture implements FurnitureType{
    @Override
    public void selectFurniture(){
        Sofa sofaobj = new ModernSofa();
        sofaobj.sofaColor();

        Chair chairObj = new ModernChair();
        chairObj.chairColor();
    }
}

class FurnitureFactory {
    
    private static Map<String, FurnitureType> furniMap = new HashMap<>();
    static  {
        furniMap.put("Victorian", new VictorianFurniture());
        furniMap.put("Modern", new MordernFurniture());
    }
    public static FurnitureType getFurnitureType(String type){
        return furniMap.get(type);
    }
}

class FurnitureService {
    public void buyFurnitureSet(String type){
        FurnitureType furnitureType = FurnitureFactory.getFurnitureType(type);
        furnitureType.selectFurniture();
    }
}



public class FurnitureStyleSets {
    public static void main(String[] args) {
        FurnitureService furnitureService = new FurnitureService();
        furnitureService.buyFurnitureSet("Victorian");
        furnitureService.buyFurnitureSet("Modern");
    }
    
}
