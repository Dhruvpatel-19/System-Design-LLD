package designprinciples.solid.interfacesegregation;

/*
 - Interface Segregation Principle states that clients should not be forced to depend 
   on methods they do not use. Instead of creating one large interface, we should create 
   multiple smaller, focused interfaces so that implementing classes only need to implement 
   relevant functionality. This reduces unnecessary dependencies and improves maintainability.
 - So, it is about grouping related methods together instead of defining one per interface.
 - Identify ISP Violations: 1. UnsupportedOperationException, 2. Empty Method Implementations, 3. Huge Interfaces
*/

//Bad Example
//Too broad: not every machine supports all operations
interface MachineExample {
    void print();
    void scan();
    void fax();
}
//A basic printer can only print, But because of the large interface, it is forced to implement.
class BasicPrinterExample implements MachineExample {

    @Override
    public void print() {
        System.out.println("Printing...");
    }

    @Override
    public void scan() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fax() {
        throw new UnsupportedOperationException();
    }
}

//Good Example, follows ISP
interface Printer {
    void printBlackAndWhite();
    void printColor();
}
interface Scanner {
    void scan();
}
interface Fax {
    void fax();
}
//Now classes implement only what they need
class BasicPrinter implements Printer {

    @Override
    public void printBlackAndWhite() {
        System.out.println("Black and White Printing...");
    }

    @Override
    public void printColor() {
        System.out.println("Color Printing...");
    }
}
class MultiFunctionPrinter
        implements Printer, Scanner, Fax {

    @Override
    public void printBlackAndWhite() {
        System.out.println("Black and White Printing...");
    }

    @Override
    public void printColor() {
        System.out.println("Color Printing...");
    }

    @Override
    public void scan() {
        System.out.println("Scanning...");
    }

    @Override
    public void fax() {
        System.out.println("Faxing...");
    }
}

//Client
class InterfaceSegregationMain {
    public static void main(String[] args) {

        //use interface references instead of the concrete class
        Printer basicPrinter = new BasicPrinter();
        basicPrinter.printBlackAndWhite();
        basicPrinter.printColor();
       
        System.out.println("----------------");

        MultiFunctionPrinter mfp = new MultiFunctionPrinter();
        Printer printer = mfp;
        Scanner scanner = mfp;
        Fax fax = mfp;

        printer.printBlackAndWhite();
        printer.printColor();
        //printer.scan(); //Compile-time error
        //printer.fax();  //Compile-time error
        scanner.scan();
        fax.fax();

    }
}
