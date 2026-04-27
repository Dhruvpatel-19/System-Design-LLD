package creationalpatterns.factorymethod;

import java.util.HashMap;
import java.util.Map;

//1. Encapsulation of creation logic
//2. loose coupling as client depends on interface
//3. follow Open/Closed principle : class PushNotification and class PushNotificationFactory can be added
//without changing existing code. However, factory selection logic still requires modification.


//abstract product, common interface
interface Notification {
    void notifyUser();
}

//concrete products
class EmailNotification implements Notification {
    //business logic...
    @Override
    public void notifyUser() {
        System.out.println("Sending Email Notification");
    }
}
class SMSNotification implements Notification {
    //business logic...

    @Override
    public void notifyUser() {
        System.out.println("Sending SMS Notification");
    }
}

//factory class
interface NotificationFactory {
    Notification createNotification();
}

//concrete factories
class EmailNotificationFactory implements NotificationFactory {
    //complex logic here
    @Override
    public Notification createNotification() {
        return new EmailNotification();
    }
}
class SMSNotificationFactory implements NotificationFactory {
    //complex logic
    @Override
    public Notification createNotification() {
        return new SMSNotification();
    }
}


class FactoryProvider {
    /*public static NotificationFactory getFactory(String type) {
        if("EMAIL".equals(type)) return new EmailNotificationFactory();
        if("SML".equals(type)) return new SMSNotificationFactory();
        throw new IllegalArgumentException("Invalid notification type: " + type);
    }*/

    //static for shared single registry, final prevent changing reference but allows modifications
    private static final Map<String, NotificationFactory> map = new HashMap<>();

    //initialized when class is loaded, not when object is created or method is called
    static {
        map.put("EMAIL", new EmailNotificationFactory());
        map.put("SMS", new SMSNotificationFactory());
    }

    public static NotificationFactory getFactory(String type) {
        NotificationFactory factory = map.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Invalid notification type: " + type);
        }
        return factory;
    }
}


//simpler version, returns object directly, not official GoF
/*class SimpleNotificationFactory {
    public static Notification createNotification(String type) {
        if(type.equals("EMAIL")) return new EmailNotification();
        if(type.equals("SMS")) return new SMSNotification();
        return null;
    }
}*/

//client
class FactoryMethodMain {
    public static void main(String[] args) {
        //client chooses factory which hides creational logic to client
        NotificationFactory factory = FactoryProvider.getFactory("EMAIL");
        //factory will give related object
        Notification notification = factory.createNotification();
        //client uses object via interace, never uses new EmailNotification() directly
        notification.notifyUser();
    }
}
