package designpatterns.behavioralpatterns.strategy;

/*
 - The Strategy Design Pattern is a behavioral design pattern used when you have:
   a.) Multiple ways (algorithms/behaviors) to perform a task 
   b.) And you want to switch between them dynamically at runtime
 - Instead of writing many if-else or switch statements, we separate each behavior into its own class
 
 - Difference from state pattern: Strategy Pattern is used to switch between interchangeable algorithms chosen by the client, 
    whereas State Pattern changes object behavior automatically based on internal state transitions.
*/


//Strategy interface
interface PaymentStrategy {
    void pay(int amount);
}

//Concrete Strategies
class CreditCardPayment implements PaymentStrategy {

    @Override
    public void pay(int amount) {
        System.out.println("Paid $" + amount + " using Credit Card");
    }
}
class PayPalPayment implements PaymentStrategy {

    @Override
    public void pay(int amount) {
        System.out.println("Paid $" + amount + " using PayPal");
    }
}
class UpiPayment implements PaymentStrategy {

    @Override
    public void pay(int amount) {
        System.out.println("Paid $" + amount + " using UPI");
    }
}

//Context class
class ShoppingCart {

    //Payment strategy to be used
    private PaymentStrategy paymentStrategy;

    public ShoppingCart(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    //set strategy dynamically
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void checkout(int amount){ 
        //payment done via used any of the strategies
        paymentStrategy.pay(amount);
    }
}

//Client
class StrategyMain {
    public static void main(String[] args) {
        //ShoppingCart with credit card payment
        ShoppingCart cart = new ShoppingCart(new CreditCardPayment());

        cart.checkout(100);

        //PayPal payment
        cart.setPaymentStrategy(new PayPalPayment());
        cart.checkout(200);

        //UPI payment
        cart.setPaymentStrategy(new UpiPayment());
        cart.checkout(300);
    }
}
