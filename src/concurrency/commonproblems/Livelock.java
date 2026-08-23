package concurrency.commonproblems;

import java.util.concurrent.locks.ReentrantLock;

/**
============================================================
Livelock
============================================================
A livelock occurs when two or more threads remain active and keep changing their state, 
but no useful progress is made.

Unlike deadlock, threads in a livelock are not permanently blocked. 
They continue executing, acquiring, releasing, and retrying resources.

The problem is that their actions continuously interfere with each other, 
preventing any thread from completing its work.

Example:
    Thread 1 acquires LOCK_1.
    Thread 2 acquires LOCK_2.

    Thread 1 tries to acquire LOCK_2 but fails.
    Thread 1 releases LOCK_1.

    Thread 2 tries to acquire LOCK_1 but fails.
    Thread 2 releases LOCK_2.

    Both threads retry at the same time.
    This process can continue indefinitely.


------------------------------------------------------------
DEADLOCK VS LIVELOCK
------------------------------------------------------------
Deadlock: Threads are blocked and waiting for each other.
Livelock: Threads are active and repeatedly responding to each other.
Deadlock: "Nobody moves."
Livelock: "Everyone keeps moving, but nobody makes progress."


------------------------------------------------------------
BASIC EXAMPLE
------------------------------------------------------------
Thread 1                  Thread 2
--------                  --------
Acquire LOCK_1            Acquire LOCK_2
Try LOCK_2                Try LOCK_1
Fail                      Fail
Release LOCK_1            Release LOCK_2
Retry                     Retry

Both threads continue running, but neither completes its work.
This is different from deadlock because the threads are not blocked while waiting for each other.


------------------------------------------------------------
RETRY LOOPS
------------------------------------------------------------
Retry loops are a common cause of livelock.

A thread may repeatedly try an operation:
    while (!success) {
        retry();
    }

If multiple threads retry at the same time using the same
strategy, they may continuously interfere with each other.

For example:
    Thread 1 retries
    Thread 2 retries
    Thread 1 releases
    Thread 2 releases
    Thread 1 retries
    Thread 2 retries

The threads remain active but make no useful progress.


------------------------------------------------------------
OVERLY POLITE THREADS
------------------------------------------------------------
Livelock can also occur when threads are "too polite".

Suppose two threads need two locks.

Instead of holding the first lock while waiting for the second, 
each thread releases its first lock when the second lock is unavailable.

This seems like good behavior because the thread is giving another thread a chance to proceed.

However, if both threads do this at the same time, they can repeatedly release their resources for each other.

Example:
    Thread 1 holds LOCK_1.
    Thread 2 holds LOCK_2.

    Thread 1 releases LOCK_1 for Thread 2.
    Thread 2 releases LOCK_2 for Thread 1.

    Both retry.

    Thread 1 releases LOCK_1 again.
    Thread 2 releases LOCK_2 again.

This repeated cooperation can result in livelock.


------------------------------------------------------------
HOW TO PREVENT LIVELOCK
------------------------------------------------------------
1. Randomized Backoff: Wait for a random amount of time before retrying.
    This makes it less likely that multiple threads will retry at exactly the same time.

2. Retry Limits: Do not retry forever.
    Stop after a fixed number of attempts and either fail, return an error, or use another strategy.

3. Timeouts: Give an operation a maximum amount of time to complete.
    If the timeout expires, stop retrying and handle the failure.

4. Change the Retry Strategy: Avoid having every thread follow exactly the same retry pattern.
    Possible strategies:
        - Exponential backoff
        - Randomized delays
        - Different retry intervals
        - Retry priorities
        - Abandoning and restarting the operation


------------------------------------------------------------
Questions
------------------------------------------------------------
Q: What is a livelock?
A livelock occurs when threads remain active and repeatedly
change their state but fail to make useful progress.

Q: How is livelock different from deadlock?
In deadlock, threads are blocked and waiting for each other.
In livelock, threads are active but repeatedly interfere with each other.

Q: What is a common cause of livelock?
Repeated retries using the same strategy, especially when multiple threads retry at the same time.

Q: What are overly polite threads?
Threads that repeatedly release resources to give other threads a chance to proceed, 
but end up preventing each other from making progress.

Q: How can livelock be prevented?
Use randomized backoff, retry limits, timeouts, and different retry strategies.

Q: Does livelock consume CPU?
It can. Since threads remain active and repeatedly execute instructions, 
a livelock can consume significant CPU.


============================================================
*/

class LivelockDemo {

    private static final ReentrantLock LOCK_1 = new ReentrantLock();
    private static final ReentrantLock LOCK_2 = new ReentrantLock();         

    //Creates a livelock-like situation where both threads repeatedly acquire and release resources.
    //There might be some situaion where one thread acquires both locks and completes work, so liveLock might not happen.
    private static void demonstrateLivelock() {

        Thread thread1 = new Thread(() -> {

            while(true){
                
                if(LOCK_1.tryLock()){

                    try{
                        System.out.println("Thread 1 acquired LOCK_1");

                        if(LOCK_2.tryLock()){
                            try{
                                System.out.println("Thread 1 acquired LOCK_2");
                                System.out.println("Thread 1 completed work");
                                return;
                            }
                            finally{
                                LOCK_2.unlock();
                            }
                        } 
                        else{
                            System.out.println("Thread 1 could not acquire LOCK_2");
                            System.out.println("Thread 1 releasing LOCK_1 and retrying");
                        }
                    } 
                    finally{
                        LOCK_1.unlock();
                    }
                }
            }
        });


        Thread thread2 = new Thread(() -> {

            while(true){

                if(LOCK_2.tryLock()){

                    try{
                        System.out.println("Thread 2 acquired LOCK_2");
                        if(LOCK_1.tryLock()){

                            try{
                                System.out.println("Thread 2 acquired LOCK_1");
                                System.out.println("Thread 2 completed work");
                                return;
                            } 
                            finally{
                                LOCK_1.unlock();
                            }
                        }
                        else{
                            System.out.println("Thread 2 could not acquire LOCK_1");
                            System.out.println("Thread 2 releasing LOCK_2 and retrying");
                        }
                    }
                    finally{
                        LOCK_2.unlock();
                    }
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


    //Demonstrates the retry-loop pattern that can cause livelock.
    //Threads repeatedly retry the same operation without changing their strategy.
    private static void demonstrateRetryLoop() {

        int maxRetries = 5;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            System.out.println("Retry attempt: " + attempt);
            System.out.println("Operation failed, retrying...");
        }

        System.out.println("Retry limit reached");
    }


    //FIX: Randomized backoff.
    //Each retry waits for a different amount of time.
    private static void demonstrateRandomizedBackoff() {

        for (int attempt = 1; attempt <= 5; attempt++) {

            System.out.println("Attempt " + attempt + " failed");
            int delay = 50 + (int) (Math.random() * 100);
            System.out.println("Waiting " + delay + " ms before retry");

            sleep(delay);
        }
    }


    //FIX: Retry limits.
    //The thread stops retrying after a fixed number of attempts.
    private static void demonstrateRetryLimit() {

        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            System.out.println("Attempt " + attempt);
            boolean success = false;

            if (success) {
                System.out.println("Operation succeeded");
                return;
            }
        }

        System.out.println("Operation failed after "+ maxRetries + " attempts");
    }


    //FIX: Timeout.
    //The operation stops retrying after a maximum amount of time.
    private static void demonstrateTimeout() {

        long timeout = 500;
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeout) {
            System.out.println("Attempting operation...");
            sleep(100);
        }

        System.out.println("Timeout reached, stopping retries");
    }


    //FIX: Change the retry strategy.
    //Instead of retrying at the same interval, use exponential
    //backoff so that the delay increases after every failure.
    private static void demonstrateChangingRetryStrategy() {

        long delay = 50;

        for (int attempt = 1; attempt <= 5; attempt++) {
            System.out.println("Attempt " + attempt);
            System.out.println("Waiting " + delay + " ms before next retry");
            sleep(delay);
            delay *= 2;
        }
    }


    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public static void main(String[] args) {

        System.out.println("\n========== Livelock Example ==========");
        //demonstrateLivelock();

        System.out.println("\n========== Retry Loop ==========");
        demonstrateRetryLoop();

        System.out.println("\n========== Randomized Backoff ==========");
        demonstrateRandomizedBackoff();

        System.out.println("\n========== Retry Limit ==========");
        demonstrateRetryLimit();

        System.out.println("\n========== Timeout ==========");
        demonstrateTimeout();

        System.out.println("\n========== Changing Retry Strategy ==========");
        demonstrateChangingRetryStrategy();
    }
}