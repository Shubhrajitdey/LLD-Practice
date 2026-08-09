/*The Strategy Pattern is a behavioral design pattern that defines a family of algorithms, 
encapsulates each one into a separate class, and makes them interchangeable at runtime depending on the context. */
package behaviouralpattern;

interface Strategy {
    void match(String riderLocation);
}

class NearestDriverStrategy implements Strategy {
    @Override
    public void match(String riderLocation) {
        System.out.println("Matching with the nearest driver to " + riderLocation);
    }
}

class CheapestDriverStrategy implements Strategy {
    @Override
    public void match(String riderLocation) {
        System.out.println("Matching with the cheapest driver to " + riderLocation);
    }
}

class FastestDriverStrategy implements Strategy {
    @Override
    public void match(String riderLocation) {
        System.out.println("Matching with the fastest driver to " + riderLocation);
    }
}

class RideMatchingService {
    private Strategy strategy;

    public RideMatchingService(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void matchDriver(String riderLocation) {
        strategy.match(riderLocation);
    }

}



public class StrategyPattern {
    public static void main(String[] args) {
        RideMatchingService rideMatchingService = new RideMatchingService(new NearestDriverStrategy());
        rideMatchingService.matchDriver("Downtown");

        RideMatchingService rideMatchingService2 = new RideMatchingService(new CheapestDriverStrategy());
        rideMatchingService2.matchDriver("Uptown");

        rideMatchingService.setStrategy(new FastestDriverStrategy());
        rideMatchingService.matchDriver("Midtown");
    }
}
