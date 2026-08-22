package concurrency.commonproblems;

/**
============================================================
Deadlock
============================================================
A deadlock occurs when two or more threads are permanently blocked because 
each thread is waiting for a resource held by another thread.

Example:
    Thread 1 holds LOCK_1 and waits for LOCK_2.
    Thread 2 holds LOCK_2 and waits for LOCK_1.

Neither thread can continue, so the program gets stuck.


------------------------------------------------------------
BASIC EXAMPLE
------------------------------------------------------------
Thread 1                  Thread 2
--------                  --------
Acquire LOCK_1            Acquire LOCK_2
Wait for LOCK_2           Wait for LOCK_1
Blocked                   Blocked

Thread 1 cannot continue because Thread 2 holds LOCK_2.
Thread 2 cannot continue because Thread 1 holds LOCK_1.


------------------------------------------------------------
FOUR CONDITIONS FOR DEADLOCK
------------------------------------------------------------
Deadlock can occur when all four conditions exist:

1. Mutual Exclusion: A resource can be held by only one thread at a time.
2. Hold and Wait: A thread holds one resource while waiting for another.
3. No Preemption: A resource cannot be forcibly taken from a thread.
4. Circular Wait: Threads form a circular dependency while waiting for resources.

If any one of these four conditions is prevented, deadlock can be prevented.


------------------------------------------------------------
HOW TO PREVENT DEADLOCK
------------------------------------------------------------
1. Always acquire multiple locks in the same order.
   Example:
   Thread 1: lock1 -> lock2
   Thread 2: lock1 -> lock2
   This prevents circular wait.

2. Avoid holding locks longer than necessary.
3. Avoid nested locks when possible.
4. Use tryLock() with a timeout when appropriate.
5. Keep lock ordering consistent throughout the application.


------------------------------------------------------------
Questions
------------------------------------------------------------
Q: What is a deadlock?
A deadlock occurs when threads are permanently blocked because
each thread is waiting for a resource held by another thread.

Q: What are the four conditions for deadlock?
Mutual exclusion, hold and wait, no preemption, and circular wait.

Q: How can deadlock be prevented?
One common approach is to always acquire multiple locks in
the same order.

Q: What is circular wait?
Circular wait occurs when Thread A waits for a resource held
by Thread B, while Thread B waits for a resource held by Thread A.

Q: Is deadlock the same as starvation?
No. In deadlock, threads wait for each other indefinitely.
In starvation, a thread keeps getting denied access to resources
because other threads are continuously given priority.

============================================================
*/

class DeadlockDemo {

    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    //Creates a deadlock by acquiring locks in different orders.
    private static void demonstrateDeadlock() {

        Thread thread1 = new Thread(() -> {

            synchronized (LOCK_1) {

                System.out.println("Thread 1 acquired LOCK_1");
                sleep();
                System.out.println("Thread 1 waiting for LOCK_2");
                synchronized (LOCK_2) {
                    System.out.println("Thread 1 acquired LOCK_2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {

            synchronized (LOCK_2) {

                System.out.println("Thread 2 acquired LOCK_2");
                sleep();
                System.out.println("Thread 2 waiting for LOCK_1");
                synchronized (LOCK_1) {
                    System.out.println("Thread 2 acquired LOCK_1");
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //FIX: Acquire locks in the same order.
    //Both threads acquire LOCK_1 before LOCK_2.
    private static void demonstrateDeadlockPrevention() {

        Thread thread1 = new Thread(() -> {

            synchronized (LOCK_1) {

                System.out.println("Thread 1 acquired LOCK_1");

                synchronized (LOCK_2) {
                    System.out.println("Thread 1 acquired LOCK_2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {

            synchronized (LOCK_1) {

                System.out.println("Thread 2 acquired LOCK_1");

                synchronized (LOCK_2) {
                    System.out.println("Thread 2 acquired LOCK_2");
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private static void sleep() {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public static void main(String[] args) {

        System.out.println("\n========== Deadlock Prevention ==========");
        //demonstrateDeadlockPrevention();

        //Do not call demonstrateDeadlock() here.
        //It intentionally creates a deadlock, so the program will never terminate.
        demonstrateDeadlock();
    }
}