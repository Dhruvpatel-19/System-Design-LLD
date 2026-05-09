package behavioralpatterns.visitor;

/*
 - The Visitor Design Pattern is a behavioral design pattern used when:
    a. You have a group of different objects (elements)
    b. You want to perform operations on them
    c. Without changing their classes every time you add a new operation in visitor class

Main operation:
 1. Visitor interface: defines visit methods for every element type
 2. Concrete visitor: implements actual operations
 3. Element interface: defines accept method
 4. Concrete elements: actual objects

 - All the operations are in one place (the visitor), which helps you see how different tasks interact with your objects
*/

//Visitor interface
interface Visitor {
    //need to define different visit methods for diffent classes as their implementation varies
    void visit(Book book);
    void visit(Fruit fruit);
}

//Element interface
interface Item {
    //accept() enables runtime polymorphism and double dispatch
    //it calls the correct overloaded visit()
    void accept(Visitor visitor);
}

//Concrete visitor to calculate totalCost all items
class PriceVisitor implements Visitor {

    //new task 
    private int totalCost = 0;

    @Override
    public void visit(Book book) {
        //optional, shown objects' values for clarity
        totalCost += book.getPrice();
    }

    @Override
    public void visit(Fruit fruit) {
        int cost = fruit.getPricePerKg() * fruit.getWeight();
        totalCost += cost;
    }

    //method to deliver given task
    public int getTotalCost(){
        return totalCost;
    }
}
//Concrete Visitor to parse Json value
class ExportVisitor implements Visitor {
    
    @Override
    public void visit(Book book) {
    System.out.println(
            "Book: " + book.getTitle() +
            ", Price: " + book.getPrice()
        );
    }

    @Override
    public void visit(Fruit fruit) {
        int cost = fruit.getPricePerKg() * fruit.getWeight();

        System.out.println(
            "Fruit: " + fruit.getName() +
            ", Cost: " + cost
        );
    }

}

//Concrete elements
class Book implements Item {
    private int price;
    private String title;

    public Book(int price, String title) {
        this.price = price;
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
class Fruit implements Item {
    private int pricePerKg;
    private int weight;
    private String name;

    public Fruit(int pricePerKg, int weight, String name) {
        this.pricePerKg = pricePerKg;
        this.weight = weight;
        this.name = name;
    }

    public int getPricePerKg() {
        return pricePerKg;
    }

    public int getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

//Client code
class VisitorMain {
    public static void main(String[] args) {

        //initialize array for different items
        Item[] items = {
            new Book(500, "Java"),
            new Fruit(100, 2, "Apple"),
            new Fruit(80, 3, "Banana")
        };

        //priceVisitor for items to calculate totalCost
        PriceVisitor priceVisitor = new PriceVisitor();

        //traverse different items and perform accept and visit operations
        for (Item item : items) {
            item.accept(priceVisitor);
        }
        System.out.println("Total Cost : "+priceVisitor.getTotalCost());

        System.out.println("---------------------------------------");
        ExportVisitor exportVisitor = new ExportVisitor();
        for (Item item : items) {
            item.accept(exportVisitor);
        }
    }
}
