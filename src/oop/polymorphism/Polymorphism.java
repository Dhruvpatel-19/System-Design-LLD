package oop.polymorphism;

/*
 - Polymorphism means "many forms". It allows the same interface or method call to behave differently depending on the object 
   that invokes it. The same method call can result in different behavior depending on the actual object at runtime.

 - Types:
   1. Compile-Time Polymorphism (Method Overloading)
   2. Runtime Polymorphism (Method Overriding)

 - Benefits:
   1. Loose coupling
   2. Extensibility
   3. Cleaner and maintainable code

- Relationship with other OOP principles:
   Polymorphism typically works together with Abstraction and Inheritance, where abstractions define
   common behavior and inheritance provides specialized implementations.
*/


//Abstract class (can be interface or concrete class) 
abstract class Notification {

    public abstract void send(String message);
}

//Child classes
class EmailNotification extends Notification {

    //overriding parent class method
    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}
class SmsNotification extends Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
class PushNotification extends Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending Push Notification: " + message);
    }
}


class PolymorphismMain {
    public static void main(String[] args) {
        //Method Overriding, behavior decided at runtime
        //Parent reference pointing to child object
        Notification notification = new EmailNotification();
        notification.send("Order Delivered");
    }
}
