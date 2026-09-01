package concurrency.commonproblems;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
============================================================
Immutability
============================================================
An immutable object is an object whose state cannot be changed after it has been created.

Once an immutable object is constructed, its fields cannot be modified. 
If a different value is required, a new object is created.

Immutable objects are especially useful in concurrency because multiple threads 
can safely share them without synchronization for their immutable state.


------------------------------------------------------------
IMMUTABLE VS MUTABLE OBJECTS
------------------------------------------------------------
Mutable objects can change their internal state after creation.

Example:
    StringBuilder builder = new StringBuilder("Hello");
    builder.append(" World");

The existing StringBuilder object is modified.

Immutable objects cannot change their state.

Example:
    String name = "Hello";
    String newName = name.concat(" World");

The original String remains unchanged.
A new String object is created instead.


------------------------------------------------------------
WHY IMMUTABLE OBJECTS ARE THREAD-SAFE
------------------------------------------------------------
Race conditions require shared mutable state.

Immutable objects do not have mutable state that threads can concurrently modify.

Therefore, multiple threads can safely read and share the same immutable object.

Example:

    Immutable Object
          |
    +-----+-----+
    |           |
 Thread 1    Thread 2
    |           |
    +-----+-----+
          |
        Read

No thread can modify the object's state.


------------------------------------------------------------
RULES FOR CREATING AN IMMUTABLE CLASS
------------------------------------------------------------
A typical immutable class follows these rules:

1. Make the class final
   Prevents subclasses from adding behavior that could break
   the immutability contract.

2. Make fields private and final
   Fields cannot be reassigned after construction.

3. Initialize all state in the constructor
   The object should be completely initialized when created.

4. Do not provide setters
   No method should modify the object's state.

5. Use defensive copying
   Mutable objects passed into the constructor should be copied.

6. Protect mutable objects returned by getters
   Return immutable copies or immutable views.

7. Prefer immutable types
   Use String, Integer, LocalDate, LocalDateTime, List.of(), List.copyOf(), etc. whenever possible.


------------------------------------------------------------
FINAL DOES NOT MEAN IMMUTABLE
------------------------------------------------------------
The final keyword prevents reassignment of a reference.

Example:
    private final List<String> skills;

The reference cannot point to another List, but the List itself can still be modified.

Therefore:
    final != immutable

For example:
    skills.add("Java");

can still modify the List.

Defensive copying or immutable collections are required.


------------------------------------------------------------
DEFENSIVE COPYING
------------------------------------------------------------
Defensive copying protects an immutable object from mutable objects supplied by the caller.

Example:
    this.skills = List.copyOf(skills);

List.copyOf() creates an unmodifiable copy of the supplied list.

This prevents the caller from modifying the immutable object's internal state through the original list.


------------------------------------------------------------
IMMUTABLE COLLECTIONS
------------------------------------------------------------
Java provides several ways to create immutable collections:
    List.of(...)
    Set.of(...)
    Map.of(...)
    List.copyOf(...)
    Set.copyOf(...)
    Map.copyOf(...)

Example:
    List<String> skills = List.of("Java", "Python");

Attempting to modify the list results in UnsupportedOperationException.


------------------------------------------------------------
COMMON IMMUTABLE JAVA CLASSES
------------------------------------------------------------
String:
    String is immutable.

Wrapper classes:
    Integer
    Long
    Double
    Boolean
    Character
    Byte
    Short
    Float

Date and Time API:
    LocalDate
    LocalDateTime
    LocalTime
    Instant
    ZonedDateTime

These classes return new objects when an operation appears to modify their value.


------------------------------------------------------------
SAFE SHARING
------------------------------------------------------------
Immutable objects can be safely shared between multiple threads.

Example:

    User user = new User(...);

    Thread 1 ---> user
    Thread 2 ---> user
    Thread 3 ---> user

All threads can safely read the same object because none of them
can modify its state.


------------------------------------------------------------
Questions
------------------------------------------------------------
Q: What is immutability?
An object is immutable when its state cannot be changed after it has been created.

Q: Why are immutable objects thread-safe?
Because their state cannot be modified after construction, so multiple threads can safely share the same object.

Q: Does final make an object immutable?
No. final prevents reassignment of a reference, but the referenced object can still be mutable.

Q: Why are setters avoided in immutable classes?
Setters modify object state, which violates immutability.

Q: What is defensive copying?
Creating a copy of a mutable object so external code cannot modify the immutable object's internal state.

Q: Why should an immutable class be final?
To prevent subclasses from introducing mutable state or behavior that could violate the immutability contract.

Q: Can immutable objects be shared between threads?
Yes. Safe sharing is one of the major benefits of immutability.

Q: Are String and Integer immutable?
Yes.

Q: Are LocalDate and LocalDateTime immutable?
Yes. Operations such as plusDays() and plusHours() return new objects instead of modifying the original object.

============================================================
*/

class ImmutabilityDemo {

    //Demonstrates that String is immutable
    private static void demonstrateString() {

        String original = "Hello";

        String modified = original.concat(" World");

        System.out.println("Original: " + original);
        System.out.println("Modified: " + modified);
    }


    //Demonstrates immutable wrapper classes
    private static void demonstrateWrapperClass() {

        Integer original = 10;

        Integer modified = original + 5;

        System.out.println("Original: " + original);
        System.out.println("Modified: " + modified);
    }


    //Demonstrates that LocalDate is immutable
    private static void demonstrateLocalDate() {

        LocalDate original = LocalDate.of(2026, 8, 31);

        LocalDate tomorrow = original.plusDays(1);

        System.out.println("Original date: " + original);
        System.out.println("Tomorrow:      " + tomorrow);
    }


    //Demonstrates that LocalDateTime is immutable
    private static void demonstrateLocalDateTime() {

        LocalDateTime original = LocalDateTime.of(2026, 8, 31, 10, 0);

        LocalDateTime later = original.plusHours(2);

        System.out.println("Original: " + original);
        System.out.println("Later:    " + later);
    }


    //Demonstrates defensive copying and immutable collections
    private static void demonstrateDefensiveCopying() {

        List<String> skills = List.of("Java", "Python");

        User user = new User("Dhruv", 25, skills);

        System.out.println("Skills: " + user.getSkills());

        //The returned list cannot be modified
        //user.getSkills().add("Spring");
    }


    //Demonstrates safe sharing of an immutable object
    private static void demonstrateSafeSharing() {

        User user = new User("Dhruv", 25, List.of("Java", "Python"));

        Thread thread1 = new Thread(() -> System.out.println("Thread 1: " + user.getName()));

        Thread thread2 = new Thread(() -> System.out.println("Thread 2: " + user.getName()));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //Demonstrates a custom immutable class
    private static void demonstrateCustomImmutableClass() {

        User user = new User("Dhruv",  25, List.of("Java", "Spring Boot"));

        System.out.println("Name:   " + user.getName());
        System.out.println("Age:    " + user.getAge());
        System.out.println("Skills: " + user.getSkills());
    }


    //Immutable class
    //final prevents subclass mutation
    //private final fields prevent reassignment
    //No setters are provided
    //Constructor initializes all state
    //List.copyOf() provides defensive copying
    public static final class User {

        private final String name;
        private final int age;
        private final List<String> skills;

        public User(String name, int age, List<String> skills){
            this.name = name;
            this.age = age;

            //Defensive copy
            this.skills = List.copyOf(skills);
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public List<String> getSkills() {
            return skills;
        }
    }


    public static void main(String[] args) {

        System.out.println("\n========== String ==========");
        demonstrateString();

        System.out.println("\n========== Wrapper Class ==========");
        demonstrateWrapperClass();

        System.out.println("\n========== LocalDate ==========");
        demonstrateLocalDate();

        System.out.println("\n========== LocalDateTime ==========");
        demonstrateLocalDateTime();

        System.out.println("\n========== Defensive Copying ==========");
        demonstrateDefensiveCopying();

        System.out.println("\n========== Custom Immutable Class ==========");
        demonstrateCustomImmutableClass();

        System.out.println("\n========== Safe Sharing ==========");
        demonstrateSafeSharing();
    }
}