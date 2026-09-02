/*Build a Car object that supports optional features: sunroof, leather seats, GPS, spoiler. 
Some cars will have 2 features set, others up to all 4. 
Implement this such that the client can set only what they need, 
and the final Car object is always fully and correctly constructed. 
Explain why you chose this approach over a large constructor with many parameters. */

package mixpattern;
class Car{
    private final boolean sunroof;
    private final boolean leatherSeats;
    private final boolean gps;
    private final boolean spoiler;


    private Car(CarBuilder builder){
        this.sunroof = builder.sunroof;
        this.leatherSeats = builder.leatherSeats;
        this.gps = builder.gps;
        this.spoiler = builder.spoiler;
    }

    public static CarBuilder builder() {
        return new CarBuilder();
    }

    @Override
    public String toString(){
        return "Car [sunroof=" + sunroof + 
               ", leatherSeats=" + leatherSeats + 
               ", GPS=" + gps + 
               ", spoiler=" + spoiler + "]";
    }
    public static class CarBuilder {
        private boolean sunroof = false;
        private boolean leatherSeats = false;
        private boolean gps = false;
        private boolean spoiler = false;

        public CarBuilder withSunroof(boolean sunroof) {
            this.sunroof = sunroof;
            return this;
        }

        public CarBuilder withLeatherSeats(boolean leatherSeats) {
            this.leatherSeats = leatherSeats;
            return this;
        }

        public CarBuilder withGPS(boolean gps) {
            this.gps = gps;
            return this;
        }

        public CarBuilder withSpoiler(boolean spoiler) {
            this.spoiler = spoiler;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
public class CarCustomization {
    public static void main(String[] args) {
        Car carObj = Car.builder()
            .withGPS(true)
            .withSunroof(true)
            .build();
        System.out.println(carObj);
    }
}
