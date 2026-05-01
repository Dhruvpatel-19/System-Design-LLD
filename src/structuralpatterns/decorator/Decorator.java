package structuralpatterns.decorator;

/*
 - Decorator Pattern is a structural design pattern that lets you add new behavior to an object dynamically (at runtime) 
   without changing its existing code.

Core idea : you wrap the object inside another object (decorator) that adds extra functionality.
 - You order a coffee
 - Then you add milk, sugar, chocolate
 - Each addition wraps the original coffee and enhances it

 - It follows Open/Close principle, provides flexibility and avoid unnecessary subclasses like CoffeeWithMilk, CoffeeWithSugar,
   CoffeeWithMilkAndSugar etc.
*/

//Component, common interface for both original object and decorators
interface Beverage  {
    String getDescription();
    double cost();
}

//Concrete component (Original Object)
class SimpleCoffee implements Beverage  {

    @Override
    public String getDescription() {
        return "Simple Coffee";
    }

    @Override
    public double cost() {
        return 5.0;
    }
}
class SimpleTea implements Beverage  {

    @Override
    public String getDescription() {
        return "Simple Tea";
    }

    @Override
    public double cost() {
        return 5.0;
    }
}

//Decorators, abstract wrappers
abstract class BeverageDecorator implements Beverage  {

    //Composition
    protected Beverage beverage;

    public BeverageDecorator(Beverage beverage) {
        this.beverage = beverage;
    }
}

//Concrete decorators, features
class MilkDecorator extends BeverageDecorator {
    public MilkDecorator(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Milk";
    }

    @Override
    public double cost() {
        return beverage.cost() + 2.0;
    }
}
class SugarDecorator extends BeverageDecorator {
    public SugarDecorator(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Sugar";
    }

    @Override
    public double cost() {
        return beverage.cost() + 1.0;
    }
}

//Client
class DecoratorMain {
    public static void main(String[] args) {
        //Simple coffee
        Beverage coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " -> " + coffee.cost());
        
        //Adding milk
        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " -> " + coffee.cost());

        //Adding sugar
        coffee = new SugarDecorator(coffee);
        System.out.println(coffee.getDescription() + " -> " + coffee.cost());

        //we can also do in following way
        Beverage tea = new SugarDecorator(new MilkDecorator(new SimpleTea()));
        System.out.println(tea.getDescription() + " -> " + tea.cost());
    }
}
