package concurrency.commonproblems;

import java.util.concurrent.atomic.AtomicInteger;

/**
============================================================
Race Condition
============================================================
A race condition occurs when multiple threads concurrently access shared mutable data
and the correctness of the result depends on the timing or interleaving of those threads.

Race conditions commonly occur when a compound operation is performed without proper synchronization.

Example:
    count++;

Although this looks like a single operation, 
conceptually it consists of:
    1. Read count
    2. Add 1
    3. Write count

If multiple threads perform these operations concurrently,
their operations can interleave and cause lost updates.


------------------------------------------------------------
BASIC EXAMPLE
------------------------------------------------------------
Suppose count = 0 and two threads execute:
    count++;

Possible execution:
    Thread 1                  Thread 2
    --------                  --------
    Read count = 0
                              Read count = 0
    Add 1
                              Add 1
    Write count = 1
                              Write count = 1

Expected result:
    count = 2

Actual result:
    count = 1

The second increment is lost.
This is called a "lost update".


------------------------------------------------------------
WHY DOES THIS HAPPEN?
------------------------------------------------------------
The problem is that count++ is not an atomic operation.

It is a read-modify-write operation:
    Read -> Modify -> Write

Another thread can execute between any of these steps.


------------------------------------------------------------
SHARED MUTABLE STATE
------------------------------------------------------------
Race conditions commonly occur when multiple threads share mutable data.

Example:
    private int count;

If multiple threads can modify count concurrently, the operations performed on count must be properly synchronized.

The general pattern is:
    Shared Mutable State
             +
    Concurrent Access
             +
    Unsynchronized Mutation
             =
       Race Condition


------------------------------------------------------------
Questions
------------------------------------------------------------
Q: What is a race condition?
A race condition occurs when multiple threads access shared mutable data concurrently and the result depends on the
timing/interleaving of those threads.

Q: Is count++ atomic?
No. It is a read-modify-write operation.

Q: What is a lost update?
A lost update occurs when one thread's update is overwritten by another thread because both threads operate on the same
shared value concurrently.

Q: Does every concurrent access cause a race condition?
No. Race conditions generally require shared mutable state and an operation whose correctness depends on synchronization.

Q: How can a race condition be prevented?
Common approaches include synchronized, Lock, atomic variables, concurrent collections, immutability, and
thread confinement.

Q: Can a race condition produce different results?
Yes. Thread scheduling is unpredictable, so the result can vary between executions.

Q: Is synchronized the only solution?
No. Atomic variables, Lock, concurrent collections, immutability, and other concurrency techniques can also
prevent race conditions depending on the problem.

============================================================
*/

class RaceConditionDemo {

    private int count = 0;

    //Increment without synchronization  
    public void incrementWithoutSynchronization() {
        count++;
    }

    //Returns the current counter value
    public int getCount() {
        return count;
    }

    //Demonstrates the race condition
    private static void demonstrateRaceCondition() {

        RaceConditionDemo counter = new RaceConditionDemo();

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
    //synchronized ensures that only one thread at a time can execute this method for the same object.
    public synchronized void incrementWithSynchronization() {
        count++;
    }
    

    //Demonstrates the synchronized solution
    private static void demonstrateSynchronized() {

        RaceConditionDemo counter = new RaceConditionDemo();

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


    //FIX 2: AtomicInteger
    //AtomicInteger provides atomic operations on an integer.
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

    public static void main(String[] args) {
        System.out.println("\n========== Race Condition ==========");
        demonstrateRaceCondition();

        System.out.println("\n========== synchronized Solution ==========");
        demonstrateSynchronized();

        System.out.println("\n========== AtomicInteger Solution ==========");
        demonstrateAtomicInteger();
    }
}