package designpatterns.behavioralpatterns.mediator;

import java.util.ArrayList;
import java.util.List;

/* 
 - The Mediator Design Pattern is a behavioral pattern that focuses on reducing direct communication between objects.

Main Components:
 1. Mediator (interface) → defines communication methods
 2. ConcreteMediator → implements coordination logic
 3. Colleague (abstract/class) → objects that communicate via mediator
 4. ConcreteColleague → actual objects
*/

//Mediator interface
interface ChatMediator {
    //define coordination logic
    //here in this code, one user sends message and others are notified about it
    void sendMessage(String message, User user);

    //to add new users in chatRoom
    void addUser(User user);
}

//Concrete mediator
class ChatRoom implements ChatMediator {
    //create a list to manage and maintain users
    private List<User> users = new ArrayList<>();

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    
    @Override
    public void sendMessage(String message, User sender) {
        //coordination logic implementation
        for (User user : users) {
            if (user != sender) {
                user.receive(message);
            }
        }
    }
}

//Colleague (Abstract User)
abstract class User {
    protected ChatMediator mediator;
    protected String name;

    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    //methods for user to send and receive messages
    abstract void send(String message);
    abstract void receive(String message);
}


//Concrete User
class ConcreteUser extends User {

    public ConcreteUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }

    @Override
    void send(String message) {
        System.out.println(this.name + " sends: " + message);
        //call mediator method to notify other users
        mediator.sendMessage(message, this);
    }

    @Override
    void receive(String message) {
        System.out.println(this.name + " receives: " + message);
    }
}

//Client
class MediatorMain {
    public static void main(String[] args) {

        //intialize chatroom
        ChatMediator chatRoom = new ChatRoom();

        //create and add users in chatRoom
        User user1 = new ConcreteUser(chatRoom, "Alice");
        User user2 = new ConcreteUser(chatRoom, "Bob");
        User user3 = new ConcreteUser(chatRoom, "Charlie");
        
        chatRoom.addUser(user1);
        chatRoom.addUser(user2);
        chatRoom.addUser(user3);

        //message from user1, notifies others
        user1.send("Hello everyone!");
    }
}
