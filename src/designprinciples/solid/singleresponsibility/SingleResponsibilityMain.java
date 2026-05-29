package designprinciples.solid.singleresponsibility;

/* 
 - Single responsibility principle states that "A class should have only one reason to change" 
   which means every class should have a single responsibility or single job or single purpose
 - Its about Grouping related behavior meaningfully (cohesion) rather than spliting functions.
*/


//Violation of SRP
//This class has 3 responsibilities. User management, Email sending, and Database handling.
class UserServiceExample {
    public void createUser(String name) {
        System.out.println("User created: " + name);
    }

    public void sendEmail(String email) {
        System.out.println("Email sent to: " + email);
    }

    public void saveToDatabase(String user) {
        System.out.println("Saved user to DB: " + user);
    }
}


//Correct Design, Following SRP
//User model
class User {
    String name;
    String email;

    User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

//Business logic layer
class UserService {
    private EmailService emailService;
    private UserRepository userRepository;

    public UserService(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        System.out.println("User created: " + user.name);

        //delegate responsibilities
        //UserService does not implement email or DB logic
        //It has one reason to change: user creation flow
        emailService.sendEmail(user.email);
        userRepository.saveToDatabase(user);
    }
}

//Email responsibility
class EmailService {
    public void sendEmail(String email) {
        System.out.println("Email sent to: " + email);
    }
}

//Database responsibility
class UserRepository {
    public void saveToDatabase(User user) {
        System.out.println("Saved user to DB: " + user.name);
    }
}

class SingleResponsibilityMain {
    public static void main(String[] args) {

        EmailService emailService = new EmailService();
        UserRepository userRepository = new UserRepository();

        UserService userService = new UserService(emailService, userRepository);

        User user = new User("Dhruv", "dhruv@example.com");

        userService.createUser(user);
    }
}
