package designprinciples.solid.liskovsubstitution;

/*
 - Liskov Substitution principle state that Objects of a superclass should be replaceable 
   with objects of its subclasses without affecting the correctness of the program. 
 - In simple terms, If class B extends class A, Then wherever A is used, B should be usable without breaking the program's behavior.
 - Rules to Follow LSP: A subclass should not 1. Throw new exceptions unexpectedly, 2. Remove functionality promised by parent, 
   3. Strengthen preconditions, 4. Weaken postconditions
*/

//Bad example
class BirdExample {

    public void fly() {
        System.out.println("Bird is flying");
    }
}

class PenguinExample extends BirdExample {

    //throws an exception which violates principle
    //subclass changes the expected behavior and cannot safely replace its parent
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguins cannot fly");
    }
}

//Good Design, following LSP
//Separate flying capability from general bird hierarchy
class Bird {
}

class FlyingBird extends Bird {

    public void fly() {
        System.out.println("Flying...");
    }
}

class Sparrow extends FlyingBird {
}

class Penguin extends Bird {
}


//Client code
class LiskovSubstitutionMain {

    public static void letBirdFly(FlyingBird bird) {
        bird.fly();
    }

    public static void main(String[] args) {

        Sparrow sparrow = new Sparrow();

        letBirdFly(sparrow);

        Penguin penguin = new Penguin();

        //Cannot pass Penguin here in letBirdFly
        //Compiler prevents incorrect usage
        //letBirdFly(penguin); //shows error


        //Valid substitutions, a child object can be used wherever its parent type is expected
        Bird bird = new Sparrow();        // OK
        Bird bird2 = new FlyingBird();    // OK
        FlyingBird fb = new Sparrow();    // OK

        //Invalid Direction
        //Sparrow sparrow = new Bird(); // Not allowed, not every Bird is a Sparrow
    }

}
