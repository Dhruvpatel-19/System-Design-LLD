package structuralpatterns.facade;

/* 
 - The Facade Design Pattern is a structural pattern pattern that provides a simple and unified interface to a complex subsystem. 
   It hides the internal complexity of the system, making it easier to use and maintain.
 - Instead of interacting with multiple classes directly, the client talks to one “facade” class.
*/

//Subsystem Classes, complex system
class CPU {
    public void start() {
        System.out.println("CPU is starting...");
    }

    public void execute() {
        System.out.println("CPU is executing instructions...");
    }
}
class Memory {
    public void load() {
        System.out.println("Memory is loading data...");
    }
}
class HardDrive {
    public void read() {
        System.out.println("Hard Drive is reading data...");
    }
}

//Facade Class, Simplified Interface
class ComputerFacade {
    //composition
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;

    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    public void startComputer() {
        System.out.println("Starting computer...");

        cpu.start();
        memory.load();
        hardDrive.read();
        cpu.execute();

        System.out.println("Computer started successfully!");
    }
}

//Client code
class FacadeMain {
    public static void main(String[] args) {
        ComputerFacade computer = new ComputerFacade();
        computer.startComputer();


        //Without facade
        /* 
        CPU cpu = new CPU();
        Memory memory = new Memory();
        HardDrive hd = new HardDrive();

        cpu.start();
        memory.load();
        hd.read();
        cpu.execute();
        */
    }
}
