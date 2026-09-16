package mixpattern;


//implementor side
interface RenderEngine{
    public String load();
}

class OpenGLRenderer implements  RenderEngine{
    @Override 
    public String load(){
        return "OpenGLRendered";
    }
}

class DirectXRenderer implements  RenderEngine{
    @Override 
    public String load(){
        return "DirectXRenderer";
    }
}


//abstract side
abstract class Shape{
    protected RenderEngine renderEngine;
    public Shape(RenderEngine renderEngine){
        this.renderEngine = renderEngine;
    }
    abstract public void draw();
}

class Circle extends Shape{
    public Circle(RenderEngine renderEngine){
        super(renderEngine);
    }
    @Override 
    public void draw(){
        System.out.println("Circle drawing using " + renderEngine.load());
    }
}

class Square extends Shape{
    public Square(RenderEngine renderEngine){
        super(renderEngine);
    }
    @Override 
    public void draw(){
        System.out.println("Square drawing using " + renderEngine.load());
    }
}

public class ShapeRenderEngine {
    public static void main(String[] args) {
        Circle openGLCircle = new Circle(new OpenGLRenderer());
        openGLCircle.draw();

        Circle directXCircle = new Circle(new DirectXRenderer());
        directXCircle.draw();

        Square openGLSquare = new Square(new OpenGLRenderer());
        openGLSquare.draw();
    }
}
