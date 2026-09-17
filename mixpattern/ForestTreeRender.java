package mixpattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TreeType{
    private String name;
    private String texture;
    private String mesh;

    TreeType(String name, String texture, String mesh){
        this.texture = texture;
        this.mesh = mesh;
        this.name = name;
    }
    public void display(int x, int y) {
        System.out.println("TreeType: " + name + ", Mesh: " + mesh + ", Texture: " + texture + ", Position: (" + x + ", " + y + ")");
    }
}

class Tree{
    private final int x;
    private final int y;
    private final TreeType treeType;
    
    public Tree(int x, int y, TreeType treeType) {
        this.x = x;
        this.y = y;
        this.treeType = treeType;
    }
    public void display(){
        treeType.display(x,y);
    }

    
}

class TreeTypeFactory{
    private static final Map<String,TreeType> treeTypeMap = new HashMap<>();
    public static TreeType geType(String type,String texture,String mesh){
        if(!treeTypeMap.containsKey(type)){
            TreeType treeType = new TreeType(type,texture,mesh);
            treeTypeMap.put(type,treeType);
            return treeType;
        }
        return treeTypeMap.get(type);
    }
}

class Forest{
    private final List<Tree> treeList = new ArrayList<>();
    public void createForest(int x, int y, String type,String texture, String mesh){
        TreeType treeType = TreeTypeFactory.geType(type,texture,mesh);
        Tree tree = new Tree(x, y, treeType);
        treeList.add(tree);
    }
    public void display(){
        for(Tree treeobj : treeList){
            treeobj.display();
        }
    }

}


public class ForestTreeRender {
    public static void main(String[] args) {
        Forest forest = new Forest();
        for (int i = 1; i < 10; i++) {
            forest.createForest(i*30, i*20, "pine", "shaded", "grey");
        }
        forest.createForest(45, 20, "owk", "shaded", "grey");

        forest.display();
    }
}
