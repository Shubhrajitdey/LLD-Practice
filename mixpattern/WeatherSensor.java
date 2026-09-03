package mixpattern;

class Sensor{

    private Sensor(){} // private constructure
    
    //lazy loading
    private static class SingletonHelper {
        private static final Sensor INSTANCE = new Sensor(); // making it final making sure thread safe
    }
    public static Sensor getSensor(){
        return SingletonHelper.INSTANCE; // return the instance from public method
    }

    public void work(){
        System.out.println("Sensor activated");
    }
}

/*using thread safe but early loading

class Sensor{
    private Sensor(){} // private constructure
    private static final Sensor instance = new Sensor(); // early loading
    public static Sensor getSensor(){
        return instance; // return the instance from public method
    }
    public void work(){
        System.out.println("Sensor activated");
    }
}*/

/*using thread safe but lazy loading

class Sensor{
    private Sensor(){} // private constructure
    private static Sensor instance; // early loading
    public static synchronized Sensor getSensor(){
        if(instance == null){
            instance = new Sensor();
            return instance;
        }
        return instance;
    }
    public void work(){
        System.out.println("Sensor activated");
    }
}*/

/*using thread safe but double check lazy loading

class Sensor{
    private Sensor(){} // private constructure
    private static volatile Sensor instance; // early loading
    public static Sensor getSensor(){
        if(instance == null){
            synchronized(Sensor.class){
                instance = new Sensor();
                return instance;
            }
        }
        return instance;
    }
    public void work(){
        System.out.println("Sensor activated");
    }
}*/


public class WeatherSensor {
    public static void main(String[] args) {
        Sensor sensorObj = Sensor.getSensor();
        sensorObj.work();
    }
}
