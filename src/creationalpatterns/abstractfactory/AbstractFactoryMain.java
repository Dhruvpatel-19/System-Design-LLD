package creationalpatterns.abstractfactory;

//Instead of creating one object (like Factory Pattern), it creates a group of related objects
//so, it ensures that products created by a factory are compatible
//Client code depends only on abstractions (GUIFactory, Button, Checkbox), not on concrete implementations
//Follows Open/Closed Principle - new UI themes (Linux, Android, etc.) can be added without modifying existing code
//Same as Factory pattern, it also requires small changes in factory selection



//abstract products
interface Button {
    void render();
}
interface Checkbox {
    void check();
}

//concrete prodcuts
//related to windwos os
class WindowsButton implements Button {
    public void render() {
        System.out.println("Rendering Windows Button");
    }
}
class WindowsCheckbox implements Checkbox {
    public void check() {
        System.out.println("Checking Windows Checkbox");
    }
}
//for mac os
class MacButton implements Button {
    public void render() {
        System.out.println("Rendering Mac Button");
    }
}
class MacCheckbox implements Checkbox {
    public void check() {
        System.out.println("Checking Mac Checkbox");
    }
}

//abstract factory
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
}

//concrete factories
class WindowsFactory implements GUIFactory {
    public Button createButton() {
        return new WindowsButton();
    }

    public Checkbox createCheckbox() {
        return new WindowsCheckbox();
    }
}
class MacFactory implements GUIFactory {
    public Button createButton() {
        return new MacButton();
    }

    public Checkbox createCheckbox() {
        return new MacCheckbox();
    }
}

//client
class AbstractFactoryMain {
    public static void main(String[] args) {
        GUIFactory factory;

        String os = "Windows";

        if (os.equals("Windows")) {
            factory = new WindowsFactory();
        } else {
            factory = new MacFactory();
        }

        Button button = factory.createButton();
        Checkbox checkbox = factory.createCheckbox();

        button.render();
        checkbox.check();
    }
}
