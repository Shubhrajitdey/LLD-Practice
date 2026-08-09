/*The Decorator Pattern is a structural design pattern 
that allows behavior to be added to individual objects, dynamically at runtime, 
without affecting the behavior of other objects from the same class.
It wraps an object inside another object that adds new behaviors 
or responsibilities at runtime, keeping the original object's interface intact.
*/
package structuralpattern;

interface TextStyle{
    String applyStyle(String text);
}

class PlainText implements TextStyle {
    @Override
    public String applyStyle(String text) {
        return text;
    }
}

abstract class TextDecorator implements TextStyle {
    protected TextStyle decoratedText;

    public TextDecorator(TextStyle decoratedText) {
        this.decoratedText = decoratedText;
    }
}

class BoldText extends TextDecorator{
    public BoldText(TextStyle decoratedText) {
        super(decoratedText);
    }

    @Override
    public String applyStyle(String text) {
        return "<b>" + decoratedText.applyStyle(text) + "</b>";
    }
}

class ItalicText extends TextDecorator{
    public ItalicText(TextStyle decoratedText) {
        super(decoratedText);
    }

    @Override
    public String applyStyle(String text) {
        return "<i>" + decoratedText.applyStyle(text) + "</i>";
    }
}

class UnderlineText extends TextDecorator{
    public UnderlineText(TextStyle decoratedText) {
        super(decoratedText);
    }

    @Override
    public String applyStyle(String text) {
        return "<u>" + decoratedText.applyStyle(text) + "</u>";
    }
}



public class DecoratorPattern {
    public static void main(String[] args) {
        TextStyle text = new ItalicText(new BoldText(new PlainText()));
        System.out.println(text.applyStyle("Hello, World!"));
    }
}