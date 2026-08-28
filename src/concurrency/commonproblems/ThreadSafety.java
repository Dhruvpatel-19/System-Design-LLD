package concurrency.commonproblems;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
============================================================
Thread Safety
============================================================
Thread safety means that a class or object behaves correctly when multiple threads access it concurrently.

Thread-safety problems commonly occur because of:
    - Shared mutable state
    - Race conditions
    - Non-atomic operations
    - Visibility problems
    - Incorrect ordering assumptions

Thread safety can be achieved using:
    - synchronized
    - Lock
    - Atomic classes
    - Thread-safe collections
    - Immutable objects
    - Stateless objects
    - Thread confinement
    - Safe publication


------------------------------------------------------------
SHARED MUTABLE STATE
------------------------------------------------------------
Shared mutable state exists when multiple threads can access and modify the same data.

Example:
    private int count;

Multiple threads modifying count concurrently can cause a race condition.

A common pattern is:
    Shared Mutable State
             +
    Concurrent Access
             +
    Unsafe Operation
             =
       Thread-Safety Problem


------------------------------------------------------------
ATOMICITY
------------------------------------------------------------
Atomicity means an operation happens as one indivisible unit.

Example:
    count++;

This is not atomic because it involves:
    1. Read count
    2. Add 1
    3. Write count

AtomicInteger can provide atomic operations:
    count.incrementAndGet();


------------------------------------------------------------
VISIBILITY
------------------------------------------------------------
Visibility means that when one thread changes shared data, other threads can see the updated value.

The volatile keyword can provide visibility guarantees.

Example:
    private volatile boolean running = true;

However, volatile does not make compound operations such as count++ atomic.


------------------------------------------------------------
ORDERING
------------------------------------------------------------
Ordering describes the order in which operations become visible to other threads.

Without proper synchronization, the compiler, JVM, and CPU may reorder operations 
while preserving single-threaded correctness.

Synchronization mechanisms establish happens-before relationships
that provide the visibility and ordering guarantees needed for
thread-safe communication.


------------------------------------------------------------
RACE CONDITIONS
------------------------------------------------------------
A race condition occurs when multiple threads access shared mutable data and 
the correctness of the result depends on the timing or interleaving of those threads.

Thread safety is one way to prevent race conditions.


------------------------------------------------------------
CRITICAL SECTION
------------------------------------------------------------
A critical section is a section of code that accesses shared state
and must be protected from conflicting concurrent execution.

Example:
    balance -= amount;

If checking and modifying the balance must happen together, the entire operation should be protected.


------------------------------------------------------------
MUTUAL EXCLUSION
------------------------------------------------------------
Mutual exclusion means only one thread can execute a protected critical section at a time.

Java provides mutual exclusion using:
    - synchronized
    - Lock


------------------------------------------------------------
synchronized
------------------------------------------------------------
synchronized provides:
    - Mutual exclusion
    - Atomicity for the protected operation
    - Visibility
    - Ordering through happens-before guarantees

Example:
    public synchronized void increment() {
        count++;
    }


------------------------------------------------------------
Lock
------------------------------------------------------------
Lock provides explicit locking control.

Example:
    lock.lock();

    try {
        count++;
    } finally {
        lock.unlock();
    }

If exception occurs, it might prevent unlock() and leave the lock permanently held.
So, the lock should normally be released inside finally. 


------------------------------------------------------------
ATOMIC CLASSES
------------------------------------------------------------
Atomic classes provide thread-safe atomic operations without explicit synchronization for supported operations.

Common classes:
    - AtomicInteger
    - AtomicLong
    - AtomicBoolean
    - AtomicReference

Example:
    count.incrementAndGet();


------------------------------------------------------------
THREAD-SAFE COLLECTIONS
------------------------------------------------------------
Java provides collections designed for concurrent access.

Examples:
    - ConcurrentHashMap
    - CopyOnWriteArrayList
    - BlockingQueue
    - ConcurrentLinkedQueue

A thread-safe collection does not necessarily make an entire multi-step operation atomic.

Prefer atomic collection operations when available.

Example:
    map.putIfAbsent(key, value);


------------------------------------------------------------
IMMUTABLE OBJECTS
------------------------------------------------------------
An immutable object cannot change after it has been created.

Immutable objects are naturally easier to share between threads 
because their state cannot be modified concurrently.

Common characteristics:
    - final class
    - private fields
    - final fields
    - no setters
    - defensive copies when necessary


------------------------------------------------------------
STATELESS OBJECTS
------------------------------------------------------------
A stateless object does not maintain mutable shared state.

Example:
    public int add(int a, int b) {
        return a + b;
    }
Multiple threads can use the same Calculator safely because each invocation works only with local variables.

Compare that with:

class Calculator {

    private int result;

    public int add(int a, int b) {
        result = a + b;
        return result;
    }
}
here, result is shared mutable state.


------------------------------------------------------------
SAFE PUBLICATION
------------------------------------------------------------
Safe publication means making an object visible to other threads
in a way that guarantees its properly constructed state is visible.

Common mechanisms include:
    - synchronized
    - volatile
    - final fields
    - static initialization
    - concurrent collections
    - locks


------------------------------------------------------------
COMMON THREAD-SAFETY MISTAKES
------------------------------------------------------------
1. Assuming count++ is atomic.
2. Using volatile when atomicity is required.
3. Protecting only part of a compound operation.
4. Synchronizing on the wrong object.
5. Forgetting to unlock a Lock.
6. Assuming a thread-safe collection makes every operation atomic.
7. Sharing mutable objects without synchronization.
8. Publishing partially constructed objects.
9. Assuming synchronized is the only way to achieve thread safety.


------------------------------------------------------------
Questions
------------------------------------------------------------
Q: What does thread-safe mean?
A thread-safe class behaves correctly when accessed concurrently by multiple threads.

Q: What is shared mutable state?
Data that is shared between threads and can be modified.

Q: What are the main properties involved in thread safety?
Atomicity, visibility, and ordering.

Q: Is count++ thread-safe?
No. It is a non-atomic read-modify-write operation.

Q: Is volatile enough for count++?
No. volatile provides visibility and ordering guarantees, but does not make compound operations atomic.

Q: How does synchronized provide thread safety?
It provides mutual exclusion and establishes visibility and ordering guarantees.

Q: When should AtomicInteger be used?
When you need atomic operations on a shared integer without protecting a larger critical section with a lock.

Q: Are immutable objects thread-safe?
An appropriately designed immutable object can safely be shared between threads.

Q: Are stateless objects thread-safe?
Stateless objects are generally thread-safe because they do not maintain shared mutable state.

Q: Does a thread-safe collection make every operation atomic?
No. Individual operations may be thread-safe while a sequence of operations can still have a race condition.

============================================================
*/

class ThreadSafetyDemo {

    private int count = 0;

    //Shared mutable state without synchronization
    public void incrementWithoutSynchronization() {
        count++;
    }

    //Returns the current counter value
    public int getCount() {
        return count;
    }


    //Demonstrates a thread-safety problem
    private static void demonstrateUnsafeCounter() {

        ThreadSafetyDemo counter = new ThreadSafetyDemo();

        int numberOfThreads = 10;
        int incrementsPerThread = 10_000;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.incrementWithoutSynchronization();
                }

            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        int expected = numberOfThreads * incrementsPerThread;

        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + counter.getCount());
    }


    //FIX 1: synchronized
    //Provides mutual exclusion for the critical section.
    public synchronized void incrementWithSynchronization() {
        count++;
    }


    //Demonstrates synchronized thread safety
    private static void demonstrateSynchronized() {

        ThreadSafetyDemo counter = new ThreadSafetyDemo();

        int numberOfThreads = 10;
        int incrementsPerThread = 10_000;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.incrementWithSynchronization();
                }

            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        int expected = numberOfThreads * incrementsPerThread;

        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + counter.getCount());
    }


    //FIX 2: Lock
    //Provides explicit mutual exclusion for the critical section.
    private static void demonstrateLock() {

        LockCounter counter = new LockCounter();

        int numberOfThreads = 10;
        int incrementsPerThread = 10_000;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }

            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        int expected = numberOfThreads * incrementsPerThread;

        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + counter.getCount());
    }


    //Counter protected using Lock
    static class LockCounter {

        private int count = 0;

        private final Lock lock = new ReentrantLock();

        public void increment() {

            lock.lock();

            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        public int getCount() {
            lock.lock();

            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }


    //FIX 3: AtomicInteger
    //Provides atomic operations on an integer.
    private static void demonstrateAtomicInteger() {

        AtomicInteger count = new AtomicInteger(0);

        int numberOfThreads = 10;
        int incrementsPerThread = 10_000;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {

            threads[i] = new Thread(() -> {

                for (int j = 0; j < incrementsPerThread; j++) {
                    count.incrementAndGet();
                }

            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        int expected = numberOfThreads * incrementsPerThread;

        System.out.println("Expected count: " + expected);
        System.out.println("Actual count:   " + count.get());
    }


    //FIX 4: Thread-safe collection
    //ConcurrentHashMap supports concurrent access.
    private static void demonstrateConcurrentCollection() {

        Map<String, Integer> scores = new ConcurrentHashMap<>();

        scores.put("Alice", 100);
        scores.put("Bob", 200);

        scores.putIfAbsent("Alice", 500);
        scores.putIfAbsent("Charlie", 300);

        System.out.println("Scores: " + scores);
    }


    //FIX 5: Immutable object
    //Immutable state can safely be shared between threads.
    static final class ImmutableUser {

        private final String name;
        private final int age;

        public ImmutableUser(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }


    //Demonstrates sharing an immutable object
    private static void demonstrateImmutableObject() {

        ImmutableUser user = new ImmutableUser("Alice", 25);

        System.out.println("Name: " + user.getName());
        System.out.println("Age:  " + user.getAge());
    }


    //FIX 6: Stateless object
    //No shared mutable state is maintained by the object.
    static class Calculator {

        public int add(int a, int b) {
            return a + b;
        }

        public int multiply(int a, int b) {
            return a * b;
        }
    }


    //Demonstrates a stateless object
    private static void demonstrateStatelessObject() {

        Calculator calculator = new Calculator();

        System.out.println("2 + 3 = " + calculator.add(2, 3));
        System.out.println("2 * 3 = " + calculator.multiply(2, 3));
    }


    //FIX 7: volatile
    //Provides visibility between threads.
    static class Worker {

        private volatile boolean running = true;

        public void stop() {
            running = false;
        }

        public void run() {

            while (running) {
                //Perform work
            }
        }
    }


    //Demonstrates visibility using volatile
    private static void demonstrateVisibility() {

        Worker worker = new Worker();

        Thread thread = new Thread(worker::run);

        thread.start();

        worker.stop();

        System.out.println("Worker stop signal sent.");
    }


    //Demonstrates a critical section
    //The check and modification must happen atomically.
    static class BankAccount {

        private int balance;

        public BankAccount(int balance) {
            this.balance = balance;
        }

        public synchronized boolean withdraw(int amount) {

            if (balance < amount) {
                return false;
            }

            balance -= amount;
            return true;
        }

        public synchronized int getBalance() {
            return balance;
        }
    }


    //Demonstrates mutual exclusion around a critical section
    private static void demonstrateCriticalSection() {

        BankAccount account = new BankAccount(1_000);

        Thread thread1 = new Thread(() -> {
            account.withdraw(700);
        });

        Thread thread2 = new Thread(() -> {
            account.withdraw(700);
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Remaining balance: " + account.getBalance());
    }


    public static void main(String[] args) {

        System.out.println("\n========== Unsafe Shared Mutable State ==========");
        demonstrateUnsafeCounter();

        System.out.println("\n========== synchronized Solution ==========");
        demonstrateSynchronized();

        System.out.println("\n========== Lock Solution ==========");
        demonstrateLock();

        System.out.println("\n========== AtomicInteger Solution ==========");
        demonstrateAtomicInteger();

        System.out.println("\n========== Concurrent Collection ==========");
        demonstrateConcurrentCollection();

        System.out.println("\n========== Immutable Object ==========");
        demonstrateImmutableObject();

        System.out.println("\n========== Stateless Object ==========");
        demonstrateStatelessObject();

        System.out.println("\n========== Visibility using volatile ==========");
        demonstrateVisibility();

        System.out.println("\n========== Critical Section ==========");
        demonstrateCriticalSection();
    }
}