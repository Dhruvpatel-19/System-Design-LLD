package dependencymanagement;

/*
 - Inversion of Control is a design principle where the control of creating and managing 
   objects is transferred from the application code to an external container or framework.
 - Normally, your code controls object creation. With IoC, someone else (container/framework) controls object creation.

 - Problem with Composition that the class creates that object itself using new, it becomes tightly coupled to a 
   specific implementation. IoC removes this tight coupling by moving object creation outside the class.
*/

//Without IoC
//NotificationService creates EmailService. So the control remains inside NotificationService. This is NOT IoC.
//Suppose tomorrow you want SMSService, EmailService. Again modify NotificationService. High coupling.
class EmailServiceWithouIoC {

    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
class NotificationServiceWithouIoC {
    
    private EmailServiceWithouIoC emailService = new EmailServiceWithouIoC();

    public void notifyUser() {
        emailService.send("Hello User");
    }
}

//With IoC
//First create an interface
interface MessageService {

    void send(String message);
}
//Implementations
class EmailService implements MessageService {

    @Override
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
class SmsService implements MessageService {

    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
//NotificationService
class NotificationService {

    private MessageService service;

    public NotificationService(MessageService service) {
        this.service = service;
    }

    public void notifyUser() {
        service.send("Hello User");
    }
}

class InversionofControlMain {
    public static void main(String[] args) {
        //Main acts as a simple IoC container
        //It creates the dependency and injects it into NotificationService
        //Can use SmsService without changing NotificationService
        MessageService service = new EmailService();

        NotificationService notification =
                new NotificationService(service);

        notification.notifyUser();
    }
}
