package designprinciples.solid.openclosed;

/*
 - It states that software entities (classes, modules, functions, etc.) should be open for extension but closed for modification.
*/

//Bad desing
//Violates OCP because new notification type requires modifying given service class
class NotificationServiceExample {

    public void send(String type, String message) {

        if(type.equals("EMAIL")) {
            System.out.println("Sending email");
        }
        else if(type.equals("SMS")) {
            System.out.println("Sending SMS");
        }
    }
}

//Follows OCP
interface NotificationSender {
    void send(String message);
}

//types
class EmailSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}
class SmsSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

//Service class
class NotificationService {

    //type or strategy to be used and message
    public void sendNotification(NotificationSender sender, String message) {
        sender.send(message);
    }

}

//Adding WhatsApp, No existing classes are modified
class WhatsAppSender implements NotificationSender {

    @Override
    public void send(String message) {
        System.out.println("Sending WhatsApp: " + message);
    }
}


class OpenClosedMain {
    public static void main(String[] args) {
        NotificationService service = new NotificationService();

        //type of notification and message 
        service.sendNotification(new EmailSender(), "Email message");
        service.sendNotification(new SmsSender(), "SMS message");
        service.sendNotification(new WhatsAppSender(), "WhatsApp message");
    }
}
