package designprinciples.solid.dependencyinversion;

/*
 - Dependency Inversion Principle states that high-level modules should not 
   directly depend on low-level modules. Both should depend on abstractions 
   such as interfaces. This reduces coupling, improves flexibility, makes testing 
   easier, and allows implementations to be changed without affecting business logic.
*/

//Bad design
//NotificationManager(high-level module) depends EmailService(low-level module)
//If tomorrow we want SMS, WhatsApp, Push Notifications. We need to modify NotificationManager. This violates DIP.
class EmailServiceExample {
    public void sendEmail(String message) {
        System.out.println("Sending Email: " + message);
    }
}

class NotificationManagerExample {
    //concrete class is used
    private EmailServiceExample emailService;

    public NotificationManagerExample() {
        this.emailService = new EmailServiceExample();
    }

    public void notifyUser(String message) {
        emailService.sendEmail(message);
    }
}

//Good example following DIP
//Create an abstraction
interface MessageService {
    void send(String message);
}

//Low-level modules implement the abstraction
class EmailService implements MessageService {

    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}
class SMSService implements MessageService {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

//Now high-level module depends on the abstraction
class NotificationManager {

    private MessageService service;

    public NotificationManager(MessageService service) {
        this.service = service;
    }

    public void notifyUser(String message) {
        service.send(message);
    }
}

/* 
    Without DIP: High-level depends on low-level
            NotificationManager
                ↓
            EmailService
    
    After DIP: Both depend on the abstraction. The dependency direction is "inverted".
            NotificationManager
                ↓
            MessageService
                ↑
        EmailService   SMSService
*/

//Usage
class DependencyInversionMain {
    public static void main(String[] args) {

        MessageService email = new EmailService();
        NotificationManager manager1 =
                new NotificationManager(email);

        manager1.notifyUser("Welcome!");

        MessageService sms = new SMSService();
        NotificationManager manager2 =
                new NotificationManager(sms);

        manager2.notifyUser("OTP: 1234");
    }
}
