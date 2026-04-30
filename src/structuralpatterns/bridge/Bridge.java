package structuralpatterns.bridge;

/*
 - Bridge Pattern is a structural design pattern used to separate abstraction from implementation, 
   so both can evolve independently. 
 
 - Without Bridge pattern if we have following classes
    Shapes: Circle, Rectangle
    Colors: Red, Blue
  - We may need classes RedCircle, BlueCircle, RedRectangle, BlueRectangle. As features grow, number of classes grows exponentially.

Core Idea: Bridge Pattern splits a class into two parts:
 1. Abstraction →  defines high-level behavior (e.g., Shape)
 2. Implementor → part can vary independently (e.g., Color)

 - If there are 3-4 independent features then Multiple fields Composition or Chain Bridge approach can be used.
*/

//Implementor
interface Color {
    void applyColor();
}

//Concrete Implementations
class Red implements Color {
    @Override
    public void applyColor() {
        System.out.println("Applying Red Color");
    }
}
class Blue implements Color {
    @Override
    public void applyColor() {
        System.out.println("Applying Blue Color");
    }
}

//Abstraction, high-level control
abstract class Shape {
    //Composition
    protected Color color;

    protected Shape(Color color) {
        this.color = color;
    }

    abstract void draw();
}

//Refined Abstraction, extended abstraction
class Circle extends Shape {

    public Circle(Color color) {
        super(color);
    }

    @Override
    public void draw() {
        System.out.print("Drawing Circle with ");
        color.applyColor();
    }
}

//Client
class BridgeMain {
    public static void main(String[] args) {
        Shape redCircle = new Circle(new Red());
        Shape blueCircle = new Circle(new Blue());

        redCircle.draw();
        blueCircle.draw();
    }
}
