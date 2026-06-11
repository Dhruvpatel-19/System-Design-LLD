package oop.encapsulation;

/*
 - Encapsulation is the process of wrapping data and methods into a single unit (class) and restricting 
   direct access to internal state by using controlled operations.
 - The main idea is Hide implementation details, Expose only what is necessary, Protect object state 
   from invalid modifications.
 - It is not just about making fields private and using getters and setters. It is poor encapsulation 
   where data can still be modifield without validations.
*/

class BankAccount {

    //private modifier
    private double balance;

    public BankAccount(double balance) {
        
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    //getter method
    public double getBalance() {
        return balance;
    }

    //added validations where amount is updated (setter methods)
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        balance -= amount;
    }
}

class EncapsulationMain {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000);

        //account.balance = -5000; // Invalid state

        account.deposit(500);
        account.withdraw(300);

        System.out.println(account.getBalance());
    }
}
