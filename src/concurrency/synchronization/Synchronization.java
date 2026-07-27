package concurrency.synchronization;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/*
===============================================================================
Synchronization
===============================================================================

Why Synchronization?
--------------------
When multiple threads access shared data simultaneously, inconsistent results
can occur. This is called a Race Condition.

Synchronization ensures:
- Thread Safety
- Mutual Exclusion (Only one thread enters critical section)
- Data Consistency


===============================================================================
1. synchronized
===============================================================================
- Built-in synchronization mechanism in Java.
- Acquires an object's Monitor (Intrinsic Lock).
- Only one thread can execute synchronized code at a time.

Example:
    synchronized(this) {
        // critical section
    }



===============================================================================
2. Monitor / Intrinsic Lock
===============================================================================
- Every Java object has an internal Monitor (Intrinsic Lock).
- synchronized automatically acquires/releases this lock.

You never create the monitor yourself.

Object
   |
 Monitor
   |
 synchronized



===============================================================================
3. Object Lock vs Class Lock
===============================================================================
Object Lock
-----------
public synchronized void method()

Equivalent:
synchronized(this)

Each object has its own lock.


Class Lock
----------
public static synchronized void method()

Equivalent:
synchronized(ClassName.class)

Entire class shares one lock.



===============================================================================
4. volatile
===============================================================================
Guarantees: ✔ Visibility
Does NOT guarantee: ✘ Atomicity

Good Example:
volatile boolean running = true;
Bad Example:
volatile int count;
count++;     // NOT thread-safe



===============================================================================
5. AtomicInteger
===============================================================================
Thread-safe integer.
Uses CAS internally.

Methods:
incrementAndGet()
decrementAndGet()
addAndGet()



===============================================================================
6. AtomicReference
===============================================================================
Thread-safe object reference.
Useful for immutable objects.



===============================================================================
7. Compare And Swap (CAS)
===============================================================================
Algorithm used by Atomic classes.

Pseudo:
Current = 10
Expected = 10
New = 11

If(Current == Expected)
    Replace
Else
    Retry

No locking required.



===============================================================================
8. ReentrantLock
===============================================================================
More powerful than synchronized.
Advantages
✔ tryLock()
✔ lockInterruptibly()
✔ Fair Lock
✔ Multiple Conditions

Always unlock inside finally block.



===============================================================================
9. ReadWriteLock
===============================================================================
Allows Multiple Readers OR One Writer
Ideal for read-heavy applications.



===============================================================================
10. StampedLock
===============================================================================
Improved ReadWriteLock.

Supports
✔ Read Lock
✔ Write Lock
✔ Optimistic Read
Optimistic Read is faster when writes are rare.



===============================================================================
Summary
===============================================================================

synchronized -> Simplest locking mechanism

Monitor -> Internal lock every object owns

volatile -> Visibility only

AtomicInteger -> Lock-free integer updates

AtomicReference -> Lock-free object updates

CAS -> Compare-And-Swap

ReentrantLock -> Advanced lock

ReadWriteLock -> Many readers, one writer

StampedLock -> Optimistic reading
===============================================================================
*/

class SynchronizationDemo {
    
    //Shared Counter
    private int count = 0;

    //Atomic Counter    
    private final AtomicInteger atomicCounter = new AtomicInteger(0);

    //Atomic Reference
    private final AtomicReference<String> status = new AtomicReference<>("Idle");

    //volatile variable
    private volatile boolean running = true;

    //Reentrant Lock
    private final Lock lock = new ReentrantLock();

    //Read Write Lock
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    //Stamped Lock
    private final StampedLock stampedLock = new StampedLock();


    //==========================================================================
    // synchronized (Object Lock)
    //==========================================================================
    public synchronized void increment() {
        count++;
        System.out.println(Thread.currentThread().getName()
                + " synchronized count = "
                + count);
    }

    //==========================================================================
    // Class Lock
    //==========================================================================
    public static synchronized void classLevelMethod() {
        System.out.println(Thread.currentThread().getName() + " acquired CLASS lock");
    }

    //==========================================================================
    // volatile
    //==========================================================================
    public void stopThread() {
        running = false;
    }
    public void checkRunning() {
        System.out.println("Running : " + running);
    }

    //==========================================================================
    // AtomicInteger
    //==========================================================================
    public void incrementAtomic() {
        int value = atomicCounter.incrementAndGet();
        System.out.println(Thread.currentThread().getName()+ " Atomic Count = "+ value);
    }

    //==========================================================================
    // AtomicReference
    //==========================================================================
    public void updateStatus(String newStatus) {
        status.set(newStatus);
        System.out.println("Status : " + status.get());
    }

    //==========================================================================
    // CAS
    //==========================================================================
    public void compareAndSwapDemo() {
        boolean success = atomicCounter.compareAndSet(5, 100);
        System.out.println("CAS Success : "+ success+ " Current Value : "+ atomicCounter.get());
    }

    //==========================================================================
    // ReentrantLock
    //==========================================================================
    public void reentrantLockExample() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+ " acquired ReentrantLock");
        } finally {
            lock.unlock();
        }
    }

    //==========================================================================
    // ReadWriteLock
    //==========================================================================
    public void readData() {
        rwLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " reading count = "+ count);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void writeData() {
        rwLock.writeLock().lock();
        try {
            count++;
            System.out.println(Thread.currentThread().getName() + " writing count = " + count);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    //==========================================================================
    // StampedLock
    //==========================================================================
    public void optimisticRead() {

        long stamp = stampedLock.tryOptimisticRead();
        int localCount = count;

        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {
                localCount = count;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }

        System.out.println("Optimistic Read : "+ localCount);
    }

    public void stampedWrite() {

        long stamp = stampedLock.writeLock();

        try {
            count++;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }


    //==========================================================================
    // Main
    //==========================================================================
    public static void main(String[] args) {

        SynchronizationDemo demo =
                new SynchronizationDemo();

        System.out.println("\n========== synchronized ==========");
        demo.increment();

        System.out.println("\n========== Class Lock ==========");
        SynchronizationDemo.classLevelMethod();

        System.out.println("\n========== volatile ==========");
        demo.checkRunning();
        demo.stopThread();
        demo.checkRunning();

        System.out.println("\n========== AtomicInteger ==========");
        demo.incrementAtomic();
        demo.incrementAtomic();

        System.out.println("\n========== AtomicReference ==========");
        demo.updateStatus("Running");

        System.out.println("\n========== CAS ==========");
        demo.compareAndSwapDemo();

        System.out.println("\n========== ReentrantLock ==========");
        demo.reentrantLockExample();

        System.out.println("\n========== ReadWriteLock ==========");
        demo.writeData();
        demo.readData();

        System.out.println("\n========== StampedLock ==========");
        demo.stampedWrite();
        demo.optimisticRead();
    }
}