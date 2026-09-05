package concurrency.jmm;

/**
============================================================
Stack vs Heap
============================================================
In Java, each thread has its own stack, while objects are generally created on the shared heap.

The stack contains method execution data such as local variables and object references.
The heap contains objects created during program execution.

The important concurrency concept is that local variables are normally thread-confined, 
while objects on the heap can be shared between multiple threads.

Example:
    int count = 10;

Here, count is a local variable belonging to the current method invocation.

Example:
    Counter counter = new Counter();

Here:
    counter -> local object reference
    Counter -> object stored on the heap

If multiple threads have access to the same Counter object, its mutable state becomes shared mutable state.

    Thread 1
        |
        v
    Counter object
        ^
        |
    Thread 2

Shared heap state matters in concurrency because multiple threads can read and modify the same object concurrently.

============================================================
STACK
============================================================
Each thread has its own stack.

The stack contains method frames created when methods are called.

Local variables belong to the method invocation executing on that thread.

Example:
    int value = 10;

If two threads execute the same method, each thread has its own local value.

    Thread 1 Stack          Thread 2 Stack
    -------------           -------------
    value = 10              value = 10

Changing one local variable does not directly change the other.

============================================================
HEAP
============================================================
Objects created using new are generally allocated on the heap.

Example:
    Person person = new Person();

Conceptually:

    Stack                       Heap
    --------                    --------
    person  -----------------> Person object

The variable person stores a reference to the object.

The reference may be local to a thread, while the object itself can potentially be accessed by multiple threads.

============================================================
OBJECT REFERENCES
============================================================
A reference variable does not contain the object itself.

Example:
    Person person = new Person();

Conceptually:

    person
       |
       v
    Person object

If another thread receives the same reference, both threads can access the same heap object.

============================================================
SHARED OBJECTS BETWEEN THREADS
============================================================
An object becomes shared when multiple threads can access the ssame object.

Example:
    Counter counter = new Counter();

    Thread 1 -> counter
    Thread 2 -> counter

Both threads can access the same Counter object.

If the object contains mutable state, that state becomes shared mutable state.

============================================================
WHY SHARED HEAP STATE MATTERS
============================================================
Shared heap state can cause concurrency problems when multiple threads modify the same data without proper synchronization.

Example:
    counter.increment();

If increment() performs:
    count++;

multiple threads may execute the read-modify-write operation concurrently.

This can result in:
    Race conditions
    Lost updates
    Visibility problems
    Inconsistent state

The problem is not simply that the object is on the heap.

The important condition is:

    Shared Object
          +
    Mutable State
          +
    Concurrent Access
          +
    Improper Synchronization
          =
    Concurrency Problem

============================================================
IMPORTANT DISTINCTION
============================================================
Stack does not automatically mean thread-safe.

Heap does not automatically mean unsafe.

The important question is:
    Can multiple threads access the same mutable state?

If state is thread-confined, it generally does not require synchronization.

If mutable state is shared between threads, appropriate concurrency mechanisms may be required.

============================================================
*/

class StackVsHeapDemo {

    //Local variables belong to the current method invocation.
    private static void demonstrateLocalVariables() {

        int value = 10;

        System.out.println("Local value: " + value);
    }


    //An object reference can be local while the object is on the heap.
    private static void demonstrateObjectReference() {

        Person person = new Person("Dhruv");

        System.out.println("Person name: " + person.getName());
    }


    //Multiple threads can share the same heap object.
    private static void demonstrateSharedObject() throws InterruptedException {

        Counter counter = new Counter();

        Thread thread1 = new Thread(() -> {

            for (int i = 0; i < 100_000; i++) {
                counter.increment();
            }

        });

        Thread thread2 = new Thread(() -> {

            for (int i = 0; i < 100_000; i++) {
                counter.increment();
            }

        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Expected count: 200000");
        System.out.println("Actual count:   " + counter.getCount());
    }


    //Demonstrates that each thread has its own local variables.
    private static void demonstrateThreadLocalState() throws InterruptedException {

        Runnable task = () -> {
            int localValue = 100;
            System.out.println(Thread.currentThread().getName() + " local value: " + localValue);
        };

        Thread thread1 = new Thread(task, "Thread-1");
        Thread thread2 = new Thread(task, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }


    //Simple object whose instance exists on the heap.
    private static class Person {

        private final String name;

        Person(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }


    //Mutable object that can be shared between multiple threads.
    private static class Counter {

        private int count = 0;

        void increment() {
            count++;
        }

        int getCount() {
            return count;
        }
    }


    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n========== Local Variables ==========");
        demonstrateLocalVariables();

        System.out.println("\n========== Object Reference ==========");
        demonstrateObjectReference();

        System.out.println("\n========== Thread Local State ==========");
        demonstrateThreadLocalState();

        System.out.println("\n========== Shared Heap Object ==========");
        demonstrateSharedObject();
    }
}

