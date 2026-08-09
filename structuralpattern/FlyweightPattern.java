package structuralpattern;
import java.util.*;
class TreeType {
    private String name;
    private String color;
    private String texture;

    public TreeType(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
    }

    public void display(int x, int y) {
        System.out.println("TreeType: " + name + ", Color: " + color + ", Texture: " + texture + ", Position: (" + x + ", " + y + ")");
    }
}

class Tree{
    private int x;
    private int y;
    private TreeType type;

    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void display() {
        type.display(x, y);
    }
}

class TreeFactory {
    private static final Map<String, TreeType> treeTypes = new HashMap<>();

    public static TreeType getTreeType(String name, String color, String texture) {
        String key = name + "-" + color + "-" + texture;
        if (!treeTypes.containsKey(key)) {
            treeTypes.put(key, new TreeType(name, color, texture));
        }
        return treeTypes.get(key);
    }
}

class Forest {
    private List<Tree> trees = new ArrayList<>();

    public void plantTree(int x, int y, String name, String color, String texture) {
        TreeType type = TreeFactory.getTreeType(name, color, texture);
        Tree tree = new Tree(x, y, type);
        trees.add(tree);
    }

    public void display() {
        for (Tree tree : trees) {
            tree.display();
        }
    }
}

public class FlyweightPattern {
    public static void main(String[] args) { 
        Forest forest = new Forest();
        for (int i = 0; i < 5; i++) {
            forest.plantTree(i*20, i*30, "Oak", "Green", "Rough");
            //forest.plantTree(i + 1, i + 1, "Pine", "Dark Green", "Smooth");
        }
        forest.display();
    }
}
