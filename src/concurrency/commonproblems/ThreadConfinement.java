package concurrency.commonproblems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
============================================================
Thread Confinement
============================================================
Thread confinement means keeping mutable data accessible to only one thread.

Instead of allowing multiple threads to access shared mutable state and using synchronization to protect it, 
thread confinement prevents the state from being shared in the first place.

The basic idea is:
    Shared Mutable State
             +
    Multiple Threads
             =
    Synchronization may be required

Whereas:
    Thread-Confined Mutable State
             +
    Single Thread Access
             =
    Synchronization may not be required


------------------------------------------------------------
WHY THREAD CONFINEMENT IS USEFUL
------------------------------------------------------------
Synchronization is often required because multiple threads can access and modify the same mutable data concurrently.
If mutable data is confined to a single thread, other threads cannot access that data concurrently.
Therefore, certain synchronization requirements can be eliminated.

For example:

    Shared:
        Thread 1 ----\
                      \
                       ---> Shared List
                      /
        Thread 2 ----/

        Synchronization may be required.


    Confined:
        Thread 1 ---> List 1
        Thread 2 ---> List 2

        Each thread has its own mutable state.


------------------------------------------------------------
STACK / THREAD-LOCAL CONFINEMENT
------------------------------------------------------------
Each thread has its own stack.

Method-local variables are stored as part of a method invocation and are normally accessible
only by the thread executing that method invocation.

Example:
    public void process() {
        int count = 0;
        List<String> names = new ArrayList<>();
    }

If two threads call process(), each method invocation gets its own count and names variables.
Therefore, the local variables are confined to their respective thread.


------------------------------------------------------------
METHOD-LOCAL VARIABLES
------------------------------------------------------------
Method-local variables are one of the simplest examples of thread confinement.

Example:

    public void calculate() {
        int sum = 0;

        List<Integer> numbers = new ArrayList<>();

        numbers.add(10);
        numbers.add(20);
    }

The numbers list is mutable, but it is created inside the method.
If the method is executed by multiple threads, each invocation creates a separate List object.
Therefore, the lists are not shared between the threads.


------------------------------------------------------------
LOCAL REFERENCE DOES NOT GUARANTEE CONFINEMENT
------------------------------------------------------------
A local reference does not automatically mean that the referenced object is thread-confined.

Example:
    private final List<String> sharedList = new ArrayList<>();

    public void process() {
        List<String> localList = sharedList;
    }

The localList reference is local, but the actual List object is shared.
Therefore, the object is not thread-confined.
The object itself must not be accessible by multiple threads.


------------------------------------------------------------
INSTANCE CONFINEMENT
------------------------------------------------------------
Instance confinement means keeping mutable state inside an object and controlling access to that state.

Example:
    class Counter {
        private int count;

        public void increment() {
            count++;
        }
    }

The count field is encapsulated inside the Counter object.
However, private alone does not make the state thread-safe.
If the same Counter object is shared by multiple threads, its methods may still require synchronization.
Instance confinement is useful when the entire object is confine to a single thread.


------------------------------------------------------------
THREADLOCAL
------------------------------------------------------------
ThreadLocal provides each thread with its own independent value.

Example:
    ThreadLocal<String> user = new ThreadLocal<>();

Thread 1 can have:
    user = "Alice"

Thread 2 can have:
    user = "Bob"

The threads access different values even though they use the same ThreadLocal object.
ThreadLocal is useful when each thread needs its own independent copy of some data.


------------------------------------------------------------
THREADLOCAL AND THREAD POOLS
------------------------------------------------------------
ThreadLocal requires special care when used with thread pools.

ExecutorService and other thread pools reuse worker threads.

If a ThreadLocal value is not removed after a task finishes, a later
task running on the same worker thread may see the previous value.

Therefore, ThreadLocal values should generally be removed when they are no longer needed.

Example:

    try {
        threadLocal.set(value);

        // Task logic

    } finally {
        threadLocal.remove();
    }


------------------------------------------------------------
EXECUTOR / TASK CONFINEMENT
------------------------------------------------------------
Task confinement means arranging for mutable state to be accessed only by tasks running on a particular executor.

A common example is a single-thread executor.

Example:
    ExecutorService executor = Executors.newSingleThreadExecutor();

If all access to a mutable object happens through this executor,
the state can effectively be confined to its single worker thread.

Conceptually:

    Caller Thread 1 ----\
    Caller Thread 2 -----\
    Caller Thread 3 -------> Executor
                              |
                              v
                         Worker Thread
                              |
                              v
                         Mutable State

Only the worker thread directly accesses the mutable state.


------------------------------------------------------------
IMPORTANT RULE
------------------------------------------------------------
The safest shared mutable state is state that is not shared.

Before using synchronization, consider whether the mutable state can
instead be confined to a single thread.

Thread confinement can make concurrent programs simpler because
there is no need to coordinate access to state that is never shared.


------------------------------------------------------------
Questions
------------------------------------------------------------
Q: What is thread confinement?
Thread confinement means keeping mutable data accessible to only one thread.

Q: Why does thread confinement reduce synchronization?
Synchronization is needed primarily when multiple threads access shared mutable state. 
If the state is confined to one thread, other threads cannot concurrently modify it.

Q: Are method-local variables thread-confined?
Normally yes. Each method invocation has its own local variables.

Q: Does a local reference guarantee thread confinement?
No. A local reference can point to an object that is shared by multiple threads.

Q: Does private make an instance field thread-safe?
No. private provides encapsulation but does not prevent multiple threads from accessing the object through its methods.

Q: What is ThreadLocal?
ThreadLocal provides each thread with its own independent value.

Q: Why should ThreadLocal.remove() be used with thread pools?
Thread pools reuse worker threads. Removing the value prevents a later task from accidentally seeing stale data from an earlier task.

Q: What is task confinement?
Task confinement means restricting access to mutable state so that only tasks 
executed by a particular executor or thread can access it.

Q: Does thread confinement mean synchronization is never required?
No. If confined state becomes accessible to multiple threads, synchronization or another thread-safety mechanism may be required.

============================================================
*/

class ThreadConfinementDemo {

    //Stack / thread-local confinement
    //Method-local variables belong to a particular method invocation.
    private void demonstrateStackConfinement() {

        int count = 0;

        List<String> names = new ArrayList<>();

        names.add("Alice");
        names.add("Bob");

        count = names.size();

        System.out.println(Thread.currentThread().getName() + " -> count = " + count + ", names = " + names);
    }

    //Demonstrates that each thread gets its own method-local variables.
    private static void demonstrateMethodLocalVariables() throws InterruptedException {

        Thread thread1 = new Thread(() -> {

            List<Integer> numbers = new ArrayList<>();

            numbers.add(10);
            numbers.add(20);

            System.out.println(Thread.currentThread().getName() + " -> " + numbers);

        }, "Thread-1");

        Thread thread2 = new Thread(() -> {

            List<Integer> numbers = new ArrayList<>();

            numbers.add(30);
            numbers.add(40);

            System.out.println(Thread.currentThread().getName() + " -> " + numbers);

        }, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    //Demonstrates that a local reference does not guarantee confinement.
    private final List<String> sharedList = new ArrayList<>();

    private void demonstrateNonConfinedLocalReference() {

        //The reference is local, but the object is shared.
        List<String> localReference = sharedList;

        localReference.add("Shared Object");

        System.out.println("Shared list: " + sharedList);
    }

    //Instance confinement example.
    //The mutable state is encapsulated inside the Counter object.
    static class ConfinedCounter {

        private int count;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }

    //Demonstrates an object that is created and used by one thread.
    private static void demonstrateInstanceConfinement() {

        ConfinedCounter counter = new ConfinedCounter();

        counter.increment();
        counter.increment();
        counter.increment();

        System.out.println("Confined counter: " + counter.getCount());
    }

    //ThreadLocal gives each thread its own independent value.
    private static final ThreadLocal<String> CURRENT_USER = new ThreadLocal<>();

    // Demonstrates ThreadLocal.
    private static void demonstrateThreadLocal() throws InterruptedException {

        Thread thread1 = new Thread(() -> {

            try {

                CURRENT_USER.set("Alice");
                System.out.println(Thread.currentThread().getName() + " -> " + CURRENT_USER.get());

            } finally {

                CURRENT_USER.remove();
            }

        }, "Thread-1");

        Thread thread2 = new Thread(() -> {

            try {

                CURRENT_USER.set("Bob");

                System.out.println(Thread.currentThread().getName() + " -> " + CURRENT_USER.get());

            } finally {

                CURRENT_USER.remove();
            }

        }, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    //Demonstrates ThreadLocal cleanup when using a thread pool.
    private static void demonstrateThreadLocalWithThreadPool()
            throws InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(1);

        executor.submit(() -> {

            try {

                CURRENT_USER.set("Alice");

                System.out.println(Thread.currentThread().getName()  + " -> " + CURRENT_USER.get()
                );

            } finally {

                //Remove the value because the worker thread is reused.
                CURRENT_USER.remove();
            }
        });

        executor.submit(() -> {

            try {

                CURRENT_USER.set("Bob");

                System.out.println(Thread.currentThread().getName() + " -> " + CURRENT_USER.get());

            } finally {

                CURRENT_USER.remove();
            }
        });

        executor.shutdown();
    }

    //Executor / task confinement example.
    static class SingleThreadTaskProcessor {

        private final ExecutorService executor = Executors.newSingleThreadExecutor();

        private final List<String> tasks = new ArrayList<>();

        public void submitTask(String task) {

            executor.submit(() -> {

                //This code always executes on the single worker thread.
                tasks.add(task);

                System.out.println(Thread.currentThread().getName() + " -> Added: " + task);
            });
        }

        public void shutdown() {
            executor.shutdown();
        }
    }

    //Demonstrates executor / task confinement.
    private static void demonstrateTaskConfinement() throws InterruptedException {

        SingleThreadTaskProcessor processor = new SingleThreadTaskProcessor();

        processor.submitTask("Task 1");
        processor.submitTask("Task 2");
        processor.submitTask("Task 3");

        processor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {

        ThreadConfinementDemo example = new ThreadConfinementDemo();

        System.out.println("\n========== Stack Confinement ==========");
        example.demonstrateStackConfinement();

        System.out.println("\n========== Method-Local Variables ==========");
        demonstrateMethodLocalVariables();

        System.out.println("\n========== Non-Confined Local Reference ==========");
        example.demonstrateNonConfinedLocalReference();

        System.out.println("\n========== Instance Confinement ==========");
        demonstrateInstanceConfinement();

        System.out.println("\n========== ThreadLocal ==========");
        demonstrateThreadLocal();

        System.out.println("\n========== ThreadLocal With Thread Pool ==========");
        demonstrateThreadLocalWithThreadPool();

        System.out.println("\n========== Task Confinement ==========");
        demonstrateTaskConfinement();
    }
}