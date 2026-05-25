package designpatterns.structuralpatterns.adapter;


/*

- Adapter pattern is used to convert the interface of a class into another interface that clients expect.
- promotes code reusability by allowing existing functionality to be used in new systems

Core Idea:
- Client expects Interface A
- You have Class B
- Adapter sits in between and translates A <-> B


Variations of Adapter Pattern
1. Class Adapter (Inheritance-based)
- Uses inheritance
- Adapter extends the adaptee and implements target interface
- Since Java doesn't support mutilple inheritance, it is less flexible

class Adapter extends Adaptee implements Target {
    public void request() {
        specificRequest();
    }
}


2. Object Adapter (Composition-based)
- Uses composition, Has-A relationship
- Adapter contains the adaptee
- Flexible and Preferred in Java

class Adapter implements Target {
    private Adaptee adaptee;

    public void request() {
        adaptee.specificRequest();
    }
}
*/

//Target interface, what client expects
interface PaymentProcessor {
    void pay(double amount);
}

//Adaptee, existing incompatible class
class LegacyPaymentSystem {
    public void makePayment(double amount) {
        System.out.println("Payment done using legacy system: " + amount);
    }
}

//Adapter, Composition-based
class PaymentAdapter implements PaymentProcessor {

    //Composition
    private LegacyPaymentSystem legacySystem;

    public PaymentAdapter(LegacyPaymentSystem legacySystem) {
        this.legacySystem = legacySystem;
    }

    @Override
    public void pay(double amount) {
        // adapting method call
        legacySystem.makePayment(amount);
    }
}

//Client code
class AdapterMain {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentAdapter(new LegacyPaymentSystem());
        processor.pay(100);
    }
}