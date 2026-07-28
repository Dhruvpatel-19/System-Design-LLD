package concurrency.threadcommunication;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
================================================================================
                    ADVANCED THREAD SYNCHRONIZATION
================================================================================

Definition
----------
Java introduced the java.util.concurrent package in Java 5 to provide more
powerful synchronization utilities than the traditional synchronized,
wait(), notify(), and notifyAll() methods.

Two important synchronization mechanisms are:
1. Condition
2. Semaphore

Both solve different synchronization problems while improving readability,
flexibility, and scalability.

--------------------------------------------------------------------------------
Traditional Synchronization vs Modern Synchronization
--------------------------------------------------------------------------------

Traditional

synchronized
      |
      +---- wait()
      |
      +---- notify()
      |
      +---- notifyAll()

Limitations:
-------------
• Only one waiting queue per object.
• Less flexible.
• No fairness support.
• Lock is tied to synchronized.

Modern Synchronization

Lock
   |
   +---- Condition
   |
   +---- Semaphore

Advantages:
-------------
• Multiple waiting queues.
• Better control.
• Fair locking support.
• More readable APIs.
• Better suited for complex concurrent applications.


================================================================================
1. CONDITION
================================================================================
Definition
----------
Condition is an interface that works together with a Lock object to provide
thread communication similar to wait(), notify(), and notifyAll().

It is an advanced replacement for Object monitor methods.
Package: java.util.concurrent.locks

Relationship

Lock
   |
   +---- Condition

A Lock may create multiple Condition objects.

--------------------------------------------------------------------------------
Why Condition?
--------------------------------------------------------------------------------
Suppose a Bank Account has two independent situations:

Account Lock
        |
        +------ Balance Available
        |
        +------ Transaction Complete

Using wait()/notify():

There is only ONE waiting queue.

Every waiting thread is mixed together.

Using Condition:

Lock
 |
 +---- balanceCondition
 |
 +---- transactionCondition

Different threads can wait for different conditions.

Much cleaner.

--------------------------------------------------------------------------------
Methods
--------------------------------------------------------------------------------
wait() → await()
notify() → signal()
notifyAll() → signalAll()

--------------------------------------------------------------------------------
Important Methods
--------------------------------------------------------------------------------
lock.lock()
Acquires the lock.

------------------------------------------------
condition.await()
Releases the lock and waits.

------------------------------------------------
condition.signal()
Wakes one waiting thread.

------------------------------------------------
condition.signalAll()
Wakes all waiting threads.

------------------------------------------------
lock.unlock()
Releases the lock.


--------------------------------------------------------------------------------
Condition Workflow
--------------------------------------------------------------------------------
Consumer Thread

Acquire Lock
      |
Resource Available?
      |
     No
      |
    await()
      |
    Release Lock
      |
    WAITING



Producer Thread

Acquire Lock
      |
Produce Resource
      |
    signal()
      |
    Unlock


Consumer
Wake Up
Acquire Lock
Continue

================================================================================
2. SEMAPHORE
================================================================================
Definition
----------
Semaphore is a synchronization utility that controls how many threads can
access a shared resource simultaneously.

Instead of a single lock, Semaphore maintains a fixed number of permits.
Package: java.util.concurrent

--------------------------------------------------------------------------------
Why Semaphore?
--------------------------------------------------------------------------------
Suppose there are only 3 database connections.
Database Connections [1] [2] [3]

Thread-1 -> Connection 1
Thread-2 -> Connection 2
Thread-3 -> Connection 3
Thread-4 -> Must Wait

Semaphore ensures that only three threads can access the database at once.

--------------------------------------------------------------------------------
Important Methods
--------------------------------------------------------------------------------
acquire()
Obtains one permit.
If none available, thread waits.

------------------------------------------------
release()
Returns the permit.
Waiting thread may continue.

------------------------------------------------
availablePermits()
Returns remaining permits.

--------------------------------------------------------------------------------
Semaphore Workflow
--------------------------------------------------------------------------------
Initial Permits = 2

Thread-A acquire()
Permits = 1

Thread-B acquire()
Permits = 0

Thread-C acquire()
    ↓
WAITING

Thread-A release()
    ↓
Thread-C continues


--------------------------------------------------------------------------------
Types of Semaphore
--------------------------------------------------------------------------------
1. Binary Semaphore
Only one permit.
Acts similarly to a lock.
Semaphore semaphore = new Semaphore(1);

------------------------------------------------
2. Counting Semaphore
Multiple permits.
Semaphore semaphore = new Semaphore(5);
Allows five concurrent threads.

--------------------------------------------------------------------------------
Real-world Uses
--------------------------------------------------------------------------------
Condition
✔ Producer Consumer
✔ Banking System
✔ Order Processing
✔ Blocking Queue

Semaphore
✔ Database Connection Pool
✔ Rate Limiting
✔ Printer Pool
✔ Parking Lot
✔ API Request Limiting


--------------------------------------------------------------------------------
Condition vs Semaphore
--------------------------------------------------------------------------------
Condition
• Works with Lock
• Used for thread communication
• Threads wait for a condition
• Supports multiple waiting queues

Semaphore
• Independent synchronization utility
• Controls concurrent access
• Uses permits
• Does not require Lock

--------------------------------------------------------------------------------
Questions
--------------------------------------------------------------------------------
Q1. Why use Condition instead of wait()/notify()?
Condition allows multiple waiting queues for a single lock, resulting in
better organization and flexibility.

----------------------------------------------------------
Q2. Can Condition be used without Lock?
No. A Condition object is always created from a Lock.
Example:
Lock lock = new ReentrantLock();
Condition condition = lock.newCondition();

----------------------------------------------------------
Q3. Does Semaphore provide mutual exclusion?
Yes, when initialized with one permit.
Semaphore semaphore = new Semaphore(1);
This is called a Binary Semaphore.

----------------------------------------------------------
Q4. Does Semaphore release automatically?
No. Every acquire() should have a matching release().
Usually release() is placed inside finally.

----------------------------------------------------------
Q5. Difference between Lock and Semaphore?
Lock protects one critical section.
Semaphore limits the number of threads accessing a resource.

================================================================================
*/



/*
===============================================================================
                    CONDITION EXAMPLE
===============================================================================
*/
class SharedBuffer {

    private boolean available = false;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void consume() {
        lock.lock();
        try {
            while (!available) {
                System.out.println(Thread.currentThread().getName() + " waiting...");
                condition.await();
            }

            System.out.println(Thread.currentThread().getName() + " consumed resource.");

            available = false;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    public void produce() {
        lock.lock();
        try {
            available = true;
            System.out.println(Thread.currentThread().getName() + " produced resource.");
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}


/*
===============================================================================
                    SEMAPHORE EXAMPLE
===============================================================================
*/
class Printer {

    private final Semaphore semaphore = new Semaphore(2);

    public void printDocument() {

        try {

            semaphore.acquire();

            System.out.println(Thread.currentThread().getName() + " acquired printer.");
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " finished printing.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {

            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " released printer.");
        }
    }
}


 class AdvancedSynchronization {

    public static void main(String[] args) {

        System.out.println("========== CONDITION EXAMPLE ==========");

        SharedBuffer buffer = new SharedBuffer();
        Thread consumer = new Thread(buffer::consume, "Consumer");
        Thread producer = new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            buffer.produce();

        }, "Producer");

        consumer.start();
        producer.start();

        try{
            consumer.join();
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        System.out.println("\n========== SEMAPHORE EXAMPLE ==========");
        Printer printer = new Printer();
        for (int i = 1; i <= 5; i++) {
            Thread thread = new Thread(printer::printDocument, "Employee-" + i);
            thread.start();
        }
    }
}
