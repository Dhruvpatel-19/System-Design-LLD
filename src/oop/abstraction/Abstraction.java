package oop.abstraction;

/*
 - Abstraction is an OOP principle that focuses on showing only the essential details and hiding the implementation details.
 - Take example of a car: You press the accelerator to increase speed, You press the brake to stop. 
   You don't need to know : How fuel is injected, How pistons move, How the transmission works etc.
 - The car exposes a simple interface while hiding its internal complexity.
 - abstraction is typically achieved using interfaces and abstract classes. It helps reduce complexity, improve maintainability, and promote loose coupling.
*/

//Interface
interface PaymentService {
    void pay(double amount);
}

//Concrete implementations of PaymentService
class CreditCardPayment implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}
class PayPalPayment implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using PayPal");
    }
}

//Client doesn't need to know which payment gateway is used, how authentication happens, How transaction processing works etc.
class AbstractionMain {
    public static void main(String[] args) {
        
        PaymentService payment = new CreditCardPayment();

        payment.pay(100);
    }
}
