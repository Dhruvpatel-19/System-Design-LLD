package behavioralpatterns.command;

/*
 - The Command Design Pattern is a behavioral design pattern that encapsulates a request as an object, 
   thereby decoupling the sender of the request from the receiver and allowing flexible execution of operations.
 - In provides decoupling between invoker(remote) and receiver(object), commands can be stored, reversed or executed later.
 - In future, if new command comes for any object or if new object is added, invoker(remote) doesn't need any modification.
*/

//Command interface, defines execution method
interface Command {
    void execute();
}

//Concrete command, implements command and binds receiver
class TurnOnLightCommand implements Command {
    //receiver object
    private Light light;

    public TurnOnLightCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}
class DimLightCommand implements Command {
    private Light light;

    public DimLightCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.dim();
    }
}
class ChangeColorCommand implements Command {
    private Light light;
    private String color;

    public ChangeColorCommand(Light light, String color) {
        this.light = light;
        this.color = color;
    }

    @Override
    public void execute() {
        light.changeColor(color);
    }
}
class TurnOffLightCommand implements Command {
    private Light light;

    public TurnOffLightCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOff();
    }
}
class TurnOnFanCommand implements Command {
    private Fan fan;

    public TurnOnFanCommand(Fan fan) {
        this.fan = fan;
    }

    @Override
    public void execute() {
        fan.turnOn();
    }
}
class SetFanSpeedCommand implements Command {
    private Fan fan;
    private int speed;

    public SetFanSpeedCommand(Fan fan, int speed) {
        this.fan = fan;
        this.speed = speed;
    }

    public void execute() {
        fan.setSpeed(speed);
    }
}

//Receiver, actual object that does the work
class Light {
    public void turnOn() {
        System.out.println("Light is ON");
    }

    public void turnOff() {
        System.out.println("Light is OFF");
    }

    public void dim() {
        System.out.println("Light is DIMMED");
    }

    public void changeColor(String color) {
        System.out.println("Light color changed to " + color);
    }
}
//Another receiver
class Fan {
    public void turnOn() {
        System.out.println("Fan is ON");
    }

    public void turnOff() {
        System.out.println("Fan is OFF");
    }

    public void setSpeed(int speed) {
        System.out.println("Fan speed set to " + speed);
    }
}


//Invoker, triggers the command
class RemoteControl {

    //concrete command to be executed
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    //execute given command
    public void pressButton() {
        command.execute();
    }
}
class CommandMain {
    public static void main(String[] args) {
        
        Light light = new Light();
        Fan fan = new Fan();

        RemoteControl remote = new RemoteControl();

        //Light Commands
        remote.setCommand(new TurnOnLightCommand(light));
        remote.pressButton();

        remote.setCommand(new DimLightCommand(light));
        remote.pressButton();

        remote.setCommand(new ChangeColorCommand(light, "Blue"));
        remote.pressButton();

        //Fan Commands
        remote.setCommand(new TurnOnFanCommand(fan));
        remote.pressButton();

        remote.setCommand(new SetFanSpeedCommand(fan, 3));
        remote.pressButton();
    }
}
