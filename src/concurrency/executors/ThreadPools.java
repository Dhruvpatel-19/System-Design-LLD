package concurrency.executors;

import java.util.concurrent.*;

/*
================================================================================
                                THREAD POOLS
================================================================================

Definition
----------
A Thread Pool is a collection of reusable worker threads used to execute tasks.

Instead of creating a new thread for every task, tasks are submitted to a pool
where existing threads execute them.

Benefits
--------
✔ Reuses threads
✔ Improves performance
✔ Reduces thread creation overhead
✔ Controls maximum concurrent threads
✔ Better resource utilization

Without Thread Pool
Task 1 ---> Thread-1 (Destroyed)

Task 2 ---> Thread-2 (Destroyed)

Task 3 ---> Thread-3 (Destroyed)

Lots of thread creation and destruction.


With Thread Pool
             +----------------------+
Task 1 ----->|                      |
Task 2 ----->|     Thread Pool      |
Task 3 ----->|                      |
             +----------------------+
                  |      |      |
              Worker1 Worker2 Worker3

Threads are reused.


--------------------------------------------------------------------------------
1. ThreadPoolExecutor
--------------------------------------------------------------------------------
Definition: ThreadPoolExecutor is the core implementation of ExecutorService.

Almost every thread pool created using the Executors utility class internally
uses ThreadPoolExecutor.

Constructor:
ThreadPoolExecutor(
    corePoolSize,
    maximumPoolSize,
    keepAliveTime,
    unit,
    workQueue
)

Important Parameters
corePoolSize: Minimum number of worker threads kept alive.
maximumPoolSize: Maximum number of threads allowed.
keepAliveTime: Idle time before extra threads are destroyed.
workQueue: Stores waiting tasks.

--------------------------------------------------------------------------------
Execution Flow
--------------------------------------------------------------------------------
New Task
    |
Core thread available?
    |
   Yes -----------------> Execute
    |
   No
    |
Queue Task
    |
Queue Full?
    |
   Yes
    |
Create Extra Thread
    |
Maximum Reached?
    |
Reject Task

--------------------------------------------------------------------------------
2. Fixed Thread Pool
--------------------------------------------------------------------------------
Definition: Creates a pool with a fixed number of threads.

ExecutorService service = Executors.newFixedThreadPool(3);

Behavior:
Tasks <= Threads -> Run immediately.

Tasks > Threads -> Extra tasks wait in the queue.

Best For:
✔ Web servers
✔ Database requests
✔ REST APIs
✔ Batch processing

--------------------------------------------------------------------------------
3. Cached Thread Pool
--------------------------------------------------------------------------------
Definition: Creates threads as needed.

ExecutorService service = Executors.newCachedThreadPool();

Behavior:
No idle thread? -> Create one

Idle thread exists? -> Reuse it

Idle threads are removed after 60 seconds.

Advantages:
✔ Fast
✔ No waiting queue

Disadvantages:
Can create a very large number of threads.

Best For:
✔ Short-lived asynchronous tasks

--------------------------------------------------------------------------------
4. Single Thread Executor
--------------------------------------------------------------------------------
Definition: Creates exactly one worker thread.

ExecutorService service = Executors.newSingleThreadExecutor();

Behavior:
Task A
Task B
Task C
   ↓
Executed sequentially.

Guarantees:
✔ One thread only
✔ Preserves task order

Best For:
✔ Logging
✔ File writing
✔ Sequential processing


                    ThreadPoolExecutor
                           ↑
        -----------------------------------------
        |                  |                    |
Fixed Thread Pool   Cached Thread Pool   Single Thread Executor
(preconfigured)      (preconfigured)      (preconfigured)


--------------------------------------------------------------------------------
Fixed vs Cached vs Single
--------------------------------------------------------------------------------
Fixed Thread Pool:
Threads: Fixed
Queue: Yes
Reuse: Yes

Cached Thread Pool:
Threads: Unlimited (as needed)
Queue: No
Reuse: Yes

Single Thread Executor:
Threads: One
Queue: Yes
Reuse: Same thread

--------------------------------------------------------------------------------
Questions
--------------------------------------------------------------------------------
Q1. Why use a thread pool?
To avoid creating and destroying threads repeatedly.

----------------------------------------------------------
Q2. Which thread pool is safest?
Fixed Thread Pool.

----------------------------------------------------------
Q3. Why can Cached Thread Pool be dangerous?
It may create a very large number of threads.

----------------------------------------------------------
Q4. When should Single Thread Executor be used?
When tasks must execute sequentially in submission order.

----------------------------------------------------------
Q5. Which class powers most thread pools?
ThreadPoolExecutor.

----------------------------------------------------------
Q6. Does keepAliveTime affect core threads?
By default, No. It only affects threads created beyond corePoolSize.

----------------------------------------------------------
Q7. Why use ThreadPoolExecutor if Executors already provides thread pools?
Use ThreadPoolExecutor when you need full control over parameters such as corePoolSize, 
maximumPoolSize, keepAliveTime, the work queue, thread factory, or rejection policy.
Use Executors when the predefined configurations (Fixed, Cached, Single, Scheduled) meet 
your needs as they are simple and convenient.

----------------------------------------------------------
Q8. What is the purpose of awaitTermination()?
awaitTermination() blocks the current thread until all tasks complete after a shutdown request, 
or until the specified timeout occurs. It is commonly used for graceful executor shutdown and cleanup.


--------------------------------------------------------------------------------
Real-world Uses
--------------------------------------------------------------------------------
✔ Web Servers
✔ Spring Boot Request Processing
✔ Email Sending
✔ Payment Processing
✔ Background Jobs
✔ Image Processing
✔ Report Generation
================================================================================
*/

public class ThreadPools {

    /*
     * =========================================================================
     * 1. ThreadPoolExecutor
     * =========================================================================
     *
     * ThreadPoolExecutor gives complete control over the thread pool.
     */
    private static void threadPoolExecutorExample() throws InterruptedException {

        System.out.println("\n========== ThreadPoolExecutor Example ==========");

        @SuppressWarnings("resource")
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,                              // Core threads
                4,                              // Maximum threads
                60,                             // Keep alive time
                TimeUnit.SECONDS,                              // KeepAliveTime unit
                new LinkedBlockingQueue<>()                 // where extra tasks are queued, if limited size then threads are created as per maximumPoolSize
        );

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            executor.execute(() -> System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName()));
        }

        //shutdown() doesn't terminate all task immediately
        //it just says stop accepting new tasks and finish the tasks that are already submitted
        executor.shutdown();

        //awaitTermination() makes the current thread wait until
        //All tasks are completed OR The timeout expires OR The thread is interrupted
        executor.awaitTermination(5, TimeUnit.SECONDS); //must be called after shutdown()
    }

    /*
     * =========================================================================
     * 2. Fixed Thread Pool
     * =========================================================================
     *
     * Uses a fixed number of reusable worker threads.
     */
    private static void fixedThreadPoolExample() throws InterruptedException {

        System.out.println("\n========== Fixed Thread Pool ==========");

        ExecutorService service = Executors.newFixedThreadPool(2);
        
        //It's approximately equivalent to:
        /*
            ExecutorService service = new ThreadPoolExecutor(
                2,                      // corePoolSize
                2,                      // maximumPoolSize
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
            );
        */

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            service.execute(() -> System.out.println("Task " + taskId +" executed by " + Thread.currentThread().getName()));
        }

        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);
    }

    /*
     * =========================================================================
     * 3. Cached Thread Pool
     * =========================================================================
     *
     * Creates new threads when required and reuses idle threads.
     */
    private static void cachedThreadPoolExample() throws InterruptedException {

        System.out.println("\n========== Cached Thread Pool ==========");

        ExecutorService service = Executors.newCachedThreadPool();

        //Equivalent to:
        /*
            ExecutorService service = new ThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>()
            );
        */

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            service.execute(() -> System.out.println("Task " + taskId +" executed by " + Thread.currentThread().getName()));
        }

        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);
    }

    /*
     * =========================================================================
     * 4. Single Thread Executor
     * =========================================================================
     *
     * Executes every task on the same worker thread.
     */
    private static void singleThreadExecutorExample() throws InterruptedException {

        System.out.println("\n========== Single Thread Executor ==========");

        ExecutorService service = Executors.newSingleThreadExecutor();

        //Equivalent to:
        /*
            ExecutorService service = new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
            );
        */

        for (int i = 1; i <= 5; i++) {
            int taskId = i;
            service.execute(() -> System.out.println("Task " + taskId + " executed by " + Thread.currentThread().getName()));
        }

        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {

        threadPoolExecutorExample();

        fixedThreadPoolExample();

        cachedThreadPoolExample();

        singleThreadExecutorExample();
    }
}