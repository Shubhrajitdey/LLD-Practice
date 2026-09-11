package mixpattern;


interface FormatType{
    public String sendMessage(String s);
}

class BaseFormat implements FormatType{
    @Override
    public String sendMessage(String s){
        return "Applied Base Type on "+s;
    }
}

abstract class DecoratorFormatter implements FormatType{
    protected FormatType formatType;
    public DecoratorFormatter(FormatType formatType) {
        this.formatType = formatType;
    }
}

class BoldFormat extends DecoratorFormatter{
    public BoldFormat(FormatType formatType) {
        super(formatType);
    }
    @Override 
    public String sendMessage(String message){
        return "<B>"+formatType.sendMessage(message)+"<B>";
    }
}

class ItalicFormat extends DecoratorFormatter{
    public ItalicFormat(FormatType formatType) {
        super(formatType);
    }
    @Override 
    public String sendMessage(String message){
        return "<I>"+formatType.sendMessage(message)+"<I>";
    }
}

class StrikeThroughFormat extends DecoratorFormatter{
    public StrikeThroughFormat(FormatType formatType) {
        super(formatType);
    }
    
    @Override 
    public String sendMessage(String message){
        return "<E>"+formatType.sendMessage(message)+"<E>";
    }
}
public class MessageFormat {
    public static void main(String[] args) {
        FormatType applyBoldItalic = new BoldFormat(new ItalicFormat(new BaseFormat()));
        System.out.println(applyBoldItalic.sendMessage("Message Format Test"));

        FormatType applyStrikeThroughItalic = new ItalicFormat(new StrikeThroughFormat(new BaseFormat()));
        System.out.println(applyStrikeThroughItalic.sendMessage("Message Format Test"));
    }
}
