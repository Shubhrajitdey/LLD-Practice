package creationalpattern;
/*The Singleton Pattern ensures that a class has only one instance and provides 
a global point of access to that instance. */


class LazySingletonPattern {
    private LazySingletonPattern() {
        // Private constructor to prevent instantiation
    }

    private static class SingletonHelper {
        private static final LazySingletonPattern INSTANCE = new LazySingletonPattern();
    }
    public static LazySingletonPattern getInstance() {
        return SingletonHelper.INSTANCE;    
    }
}

public class SingletonPattern {
    public static void main(String[] args) {
        LazySingletonPattern singleton = LazySingletonPattern.getInstance();
        System.out.println("Lazy Singleton instance: " + singleton);
        LazySingletonPattern singleton2 = LazySingletonPattern.getInstance();
        System.out.println("Lazy Singleton instance: " + singleton2);
    }
}
