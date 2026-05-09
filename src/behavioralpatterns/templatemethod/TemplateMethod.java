package behavioralpatterns.templatemethod;


/*
 - It defines the skeleton (template) of an algorithm in a parent class, while allowing subclasses 
   to customize specific steps of that algorithm without changing its overall structure.

Main Components:
 1. Abstract Class: contains template method (final workflow), common methods, abstract methods for customizable steps
 2. Concrete Subclasses: implement the variable steps

 - Ensures a consistent algorithm structure, new subclasses can be added without modifying existing workflow
*/

//Abstract class
abstract class Beverage {
    //Template Method
    //final -> subclasses should not change workflow
    public final void prepareRecipe() {
        if(shouldRoastBeans()) {//hook method
            roastBeans();
        }
        boilWater();
        brew();          //customized step
        pourInCup();
        addCondiments(); //customized step
    }

    //Common methods
    private void boilWater() {
        System.out.println("Boiling water");
    }
    private void roastBeans() {
        System.out.println("Roasting beans");
    }
    private void pourInCup() {
        System.out.println("Pouring into cup");
    }

    //Hook with default implementation
    //optional overridable methods
    protected boolean shouldRoastBeans() {
        return false;
    }

    //Steps that subclasses must implement
    protected abstract void brew();
    protected abstract void addCondiments();
}

//Concrete classes
class Tea extends Beverage {

    @Override
    protected void brew() {
        System.out.println("Steeping tea leaves");
    }

    @Override
    protected void addCondiments() {
        System.out.println("Adding lemon");
    }
}
class Coffee extends Beverage {

    @Override
    protected void brew() {
        System.out.println("Brewing coffee");
    }

    @Override
    protected void addCondiments() {
        System.out.println("Adding sugar and milk");
    }

    //Override hook
    @Override
    protected boolean shouldRoastBeans() {
        return true;
    }
}

//Client
class TemplateMethodMain {
    public static void main(String[] args) {
        System.out.println("Making tea:");
        Beverage tea = new Tea();
        tea.prepareRecipe();

        System.out.println("\nMaking coffee:");
        Beverage coffee = new Coffee();
        coffee.prepareRecipe();

    }
}
