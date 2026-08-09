/*The Builder Pattern is a creational design pattern 
that separates the construction of a complex object from its representation. 
This allows you to create different types and representations of an object using 
the same construction process. */

package creationalpattern;
import java.util.List;

class BurgerMeal{
    private String burger;
    private String fries;
    private String drink;

    // Optional components
    private String sides;
    private List<String> toppings;
    private boolean cheese;

    public BurgerMeal(Builder builder) {
        this.burger = builder.burger;
        this.fries = builder.fries;
        this.drink = builder.drink;
        this.sides = builder.sides;
        this.toppings = builder.toppings;
        this.cheese = builder.cheese;
    }

    public static Builder builder(String burger, String fries, String drink) {
        return new Builder(burger, fries, drink);
    }

    public static class Builder {
        private String burger;
        private String fries;
        private String drink;
        private String sides;
        private List<String> toppings;
        private boolean cheese;

        public Builder(String burger, String fries, String drink) {
            this.burger = burger;
            this.fries = fries;
            this.drink = drink;
        }

        public Builder setSides(String sides) {
            this.sides = sides;
            return this;
        }

        public Builder setToppings(List<String> toppings) {
            this.toppings = toppings;
            return this;
        }

        public Builder setCheese(boolean cheese) {
            this.cheese = cheese;
            return this;
        }

        public BurgerMeal build() {
            BurgerMeal meal = new BurgerMeal(this);
            return meal;
        }
    }


}

public class BuilderPattern {
    public static void main(String[] args){
        BurgerMeal meal = BurgerMeal.builder("Chicken Burger", "French Fries", "Coke")
                .setSides("Onion Rings")
                .setToppings(List.of("Lettuce", "Tomato", "Pickles"))
                .setCheese(true)
                .build();

        System.out.println("Burger Meal created with the following components:" + meal);
    }
}
