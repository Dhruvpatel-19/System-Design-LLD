package dependencymanagement.dependencyinjection;

/*
 - Dependency Injection is a design pattern in which an object does not create its own dependencies. Instead, someone else 
   provides (injects) those dependencies.
 - Inversion of Control transfers the ownership of object creation to an external entity, while Dependency Injection injects 
   those created dependencies into the objects that need them.

 - Three Types of Dependency Injection:
    1. Constructor Injection (Most Recommended): Dependency cannot be null, Object is immutable after construction, Easy to test
    2. Setter Injection: Useful when dependency is optional
    3. Field Injection (in frameworks like Spring): Generally discouraged because Harder to test, Hidden dependencies, Can't create immutable objects

 - In Spring, when classes are marked with @Component annotation . Spring's IoC container creates objects and injects dependencies.
 - Relationship between IoC and DI:
    Inversion of Control (IoC)
    │
    ├── Dependency Injection (DI)  ← Most common implementation
    │
    ├── Service Locator
    │
    └── Event-based callbacks
- So, IoC is the broader principle: the control of object creation and dependency management is moved away from your classes.
- Dependency Injection is a specific technique to achieve IoC by supplying dependencies from the outside. Frameworks like Spring 
  implement IoC primarily through Dependency Injection, typically using constructor injection.

 - A related SOLID principle is Dependency Inversion Principle (DIP) which states that instead of depending on concrete classes, 
  classes should depend on abstractions (interfaces). For given example, if Car depends on the Engine interface, we can easily 
  replace PetrolEngine with  DieselEngine or ElectricEngine without modifying the Car class.
*/

//Without Dependency Injection
//The Car is responsible for creating an Engine. So Car is tightly coupled with Engine.
//Hard to replace Engine, Every time you want another engine, modify Car
class EngineWithoutDI {
    public void start() {
        System.out.println("Engine started");
    }
}
class CarWithoutDI {

    private EngineWithoutDI engine;

    //Car creates and initializes its own dependency
    public CarWithoutDI() {
        engine = new EngineWithoutDI();      
    }

    public void drive() {
        engine.start();
        System.out.println("Car is moving");
    }
}

//With Dependency Injection
//Someone gives us the engine
class Engine {

    public void start() {
        System.out.println("Engine started");
    }
}
class Car {

    private Engine engine;

    //using constructor injection 
    public Car(Engine engine) {
        this.engine = engine;
    }

    public void drive() {
        engine.start();
        System.out.println("Car is moving");
    }
}


class DependencyInjectionMain {
    public static void main(String[] args) {
        
        //Main acts as a simple IoC container
        //It creates the dependency and injects it into Car

        Engine engine = new Engine();

        Car car = new Car(engine);

        car.drive();
    }
}
