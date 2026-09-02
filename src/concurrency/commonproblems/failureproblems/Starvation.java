package concurrency.commonproblems.failureproblems;

import java.util.concurrent.locks.ReentrantLock;

/**
============================================================
Starvation
============================================================
Starvation occurs when a thread repeatedly fails to get the CPU time, lock, 
or other resource it needs to make progress.

A thread can remain runnable/alive while making little or no progress 
because other threads repeatedly get the resources it needs.

------------------------------------------------------------
WHAT IS THREAD STARVATION?
------------------------------------------------------------
Thread starvation occurs when a thread repeatedly loses access to a required resource.

Common examples:
1. A thread repeatedly fails to acquire a lock.
2. A low-priority thread receives very little CPU time.
3. A thread waits behind a long-running synchronized section.
4. An unfair locking policy repeatedly favors other threads.

Starvation is different from deadlock because other threads can continue making progress.


------------------------------------------------------------
STARVATION vs DEADLOCK
------------------------------------------------------------
STARVATION:
Thread A -> repeatedly gets resource
Thread B -> repeatedly waits for resource
Some threads continue making progress.

DEADLOCK:
Thread A -> owns Lock 1 -> waits for Lock 2
Thread B -> owns Lock 2 -> waits for Lock 1
Neither thread can make progress.


------------------------------------------------------------
COMMON CAUSES OF STARVATION
------------------------------------------------------------
1. Unfair lock acquisition
2. Thread priority differences
3. Long-running synchronized sections
4. A thread repeatedly reacquiring a lock
5. Poor resource allocation policies

------------------------------------------------------------
UNFAIR vs FAIR LOCK
------------------------------------------------------------
Unfair lock:
ReentrantLock lock = new ReentrantLock();
A thread that has just released a lock may be able to acquire it again before another waiting thread.

Fair lock:
ReentrantLock lock = new ReentrantLock(true);
The fairness policy attempts to grant the lock to waiting threads in approximately first-come-first-served order.
Fair locks can reduce starvation but may have lower throughput than unfair locks.

------------------------------------------------------------
IMPORTANT
------------------------------------------------------------
Fairness applies to lock acquisition.
It does NOT guarantee that every thread receives equal CPU time.
Thread scheduling is still controlled by the JVM and the underlying operating system.

------------------------------------------------------------
HOW TO REDUCE STARVATION
------------------------------------------------------------
1. Use fair locks when fairness is important.
2. Keep synchronized/locked sections short.
3. Avoid performing slow I/O while holding a lock.
4. Avoid repeatedly reacquiring the same lock.
5. Avoid unnecessary thread-priority manipulation.
6. Use appropriate java.util.concurrent utilities.
7. Design resource allocation with fairness in mind.

============================================================
*/

public class Starvation {

    //Demonstrates the difference between an unfair and fair ReentrantLock.
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Unfair lock:");
        demonstrateLock(false);

        System.out.println("\nFair lock:");
        demonstrateLock(true);
    }

   
    //ReentrantLock() creates an unfair lock by default.
    //ReentrantLock(true) creates a fair lock.
    private static void demonstrateLock(boolean fair) throws InterruptedException {

        ReentrantLock lock = new ReentrantLock(fair);

        Thread t1 = createThread("Thread-1", lock);
        Thread t2 = createThread("Thread-2", lock);
        Thread t3 = createThread("Thread-3", lock);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }

    //Each thread repeatedly acquires the lock.
    //With an unfair lock, acquisition order is not guaranteed.
    //With a fair lock, waiting threads are given a much stronger opportunity to acquire the lock 
    //according to their waiting order.
    private static Thread createThread(String name, ReentrantLock lock) {

        return new Thread(() -> {

            for (int i = 0; i < 5; i++){

                lock.lock();

                try{
                    System.out.println(Thread.currentThread().getName() + " acquired the lock");

                    //Simulate work while holding the lock.
                    sleep(50);

                } 
                finally{
                    lock.unlock();
                }
            }

        }, name);
    }

   
    //Utility method used only to make the example easier to observe.
    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}