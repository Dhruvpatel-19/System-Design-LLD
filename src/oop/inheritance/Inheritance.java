package oop.inheritance;

/*
 - Inheritance is an OOP mechanism that allows one class to acquire the properties and behaviors of another class, promoting 
   code reuse and establishing an IS-A relationship between classes. It is implemented in Java using the extends keyword for 
   classes and supports method overriding for runtime polymorphism.
 - It provides :
   1. Method Overriding : own implementation of a parent method
   2. Runtime Polymorphism : overridden method to be selected at runtime
   3. Constructor Chaining : when a child object is created, the parent constructor is called first. 
     if not used specifically then default one(no-argument constructor) will be called. But it must be declared in parent class.
   4. Using super : super refers to the immediate parent class
 - Types of Inheritance: 1. Single Inheritance, 2. Multilevel Inheritance, 3. Hierarchical Inheritance, 
   4. Multiple Inheritance (Not Supported with Classes because of Diamond problem, need to use Interface).
*/

//Parent
class Animal {

    protected String name;

    //Parent Constructors
    Animal(){
        System.out.println("Animal Default Constructor Called");
    }

    Animal(String name) {
        this.name = name;
        System.out.println("Animal Constructor Called");
    }

    void eat() {
        System.out.println(name + " is eating");
    }

    void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

//Child Class
class Dog extends Animal {

    Dog(String name) {
        super(name); // Calling parent constructor
        System.out.println("Dog Constructor Called");
    }

    void bark() {
        System.out.println(name + " is barking");
    }

    // Method Overriding
    @Override
    void makeSound() {
        System.out.println("Dog barks");
    }

    void callParentMethod() {
        super.makeSound(); // Calling parent method
    }
}

//Another Child Class (Hierarchical Inheritance)
class Cat extends Animal {

    Cat(String name) {
        super(name);
    }

    @Override
    void makeSound() {
        System.out.println("Cat meows");
    }
}

// Multilevel Inheritance
class Puppy extends Dog {

    Puppy(String name) {
        super(name);
    }

    void play() {
        System.out.println(name + " is playing");
    }
}


class InheritanceMain {
    public static void main(String[] args) {
        System.out.println("===== Single Inheritance =====");
        Dog dog = new Dog("Bruno");
        dog.eat();      // inherited method
        dog.bark();     // own method

        System.out.println("\n===== Method Overriding =====");
        dog.makeSound();

        System.out.println("\n===== Using super =====");
        dog.callParentMethod();

        System.out.println("\n===== Runtime Polymorphism =====");
        Animal animal = new Dog("Rocky");
        animal.makeSound(); // Dog's version called

        System.out.println("\n===== Hierarchical Inheritance =====");
        Cat cat = new Cat("Kitty");
        cat.eat();
        cat.makeSound();

        System.out.println("\n===== Multilevel Inheritance =====");
        Puppy puppy = new Puppy("Tommy");
        puppy.eat();    // from Animal
        puppy.bark();   // from Dog
        puppy.play();   // own method
    }
}
