package behavioralpatterns.state;

/*
 - The State Design Pattern is a behavioral design pattern used when an object changes 
   its behavior based on its internal state.
*/


//State interface
interface State {
    //methods for how different states will react
    void insertCoin();
    void pressButton();
}

//Concrete states
class NoCoinState implements State {

    @Override
    public void insertCoin() {
        System.out.println("Coin inserted.");
    }

    @Override
    public void pressButton() {
        System.out.println("Insert coin first.");
    }
}
class HasCoinState implements State {

    @Override
    public void insertCoin() {
        System.out.println("Coin already inserted.");
    }

    @Override
    public void pressButton() {
        System.out.println("Dispensing item...");
    }
}
class DispenseState implements State {

    @Override
    public void insertCoin() {
        System.out.println("Please wait...");
    }

    @Override
    public void pressButton() {
        System.out.println("Item dispensed.");
    }
}

//Context
class VendingMachine {

    //current state
    private State currentState;

    public void setState(State state) {
        this.currentState = state;
    }

    //vending machine will respond according to current state it has
    public void insertCoin() {
        currentState.insertCoin();
    }

    public void pressButton() {
        currentState.pressButton();
    }
}

//Client
class StateMain {

    public static void main(String[] args) {

        VendingMachine machine = new VendingMachine();

        //No coin state
        machine.setState(new NoCoinState());
        machine.pressButton(); //Insert coin first
        System.out.println();

        //Has coin state
        machine.setState(new HasCoinState());
        machine.insertCoin(); //Coin already inserted
        machine.pressButton(); //Dispensing item...
        System.out.println();

        //Dispense state
        machine.setState(new DispenseState());
        machine.pressButton(); //Item dispensed
    }
}
