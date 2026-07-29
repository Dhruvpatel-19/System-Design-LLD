package concurrency.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
================================================================================
                            EXECUTOR BASICS
================================================================================
Definition
----------
The Executor Framework is a high-level API introduced in Java 5 to simplify
thread management. Instead of creating and managing threads manually, developers
submit tasks to an Executor, which decides how and when to execute them.

Without Executor:
    Thread thread = new Thread(task);
    thread.start();

Problems:
- Thread creation is expensive.
- No thread reuse.
- Difficult lifecycle management.
- Poor scalability with many tasks.

The Executor Framework solves these problems using thread pools.


--------------------------------------------------------------------------------
1. Executor
--------------------------------------------------------------------------------
Definition
----------
Executor is the root interface of the Executor Framework.

Method:
    void execute(Runnable task);

Responsibilities:
- Accepts Runnable tasks.
- Decides how the task is executed.
- Does not manage lifecycle.

Example:
Executor executor = Executors.newSingleThreadExecutor();
executor.execute(task);


--------------------------------------------------------------------------------
2. ExecutorService
--------------------------------------------------------------------------------
Definition
----------
ExecutorService extends Executor and provides lifecycle management.

Additional Features:
- submit()
- shutdown()
- shutdownNow()
- invokeAll()
- invokeAny()

Unlike Executor, it can return task results using Future.

Example:
ExecutorService service = Executors.newFixedThreadPool(2);


--------------------------------------------------------------------------------
3. Callable
--------------------------------------------------------------------------------
Definition
----------
Callable represents a task that returns a value.

Method:
    V call() throws Exception;

Difference from Runnable:

Runnable
- returns void
- cannot throw checked exceptions

Callable
- returns value
- can throw checked exceptions

Callable must be submitted using submit().


--------------------------------------------------------------------------------
4. Future
--------------------------------------------------------------------------------
Definition
----------
Future represents the result of an asynchronous computation.

Important Methods:
get(): Waits for completion and returns result.
isDone(): Returns true if completed.
cancel(): Attempts to cancel execution.
isCancelled(): Checks whether task was cancelled.
Future is returned by:
Future<Integer> future = service.submit(task);


--------------------------------------------------------------------------------
Execution Flow
--------------------------------------------------------------------------------
Main Thread
     |
Create ExecutorService
     |
Submit Callable
     |
Returns Future Immediately
     |
Main Thread Continues
     |
Worker Thread Executes Task
     |
Task Completes
     |
Future Stores Result
     |
future.get()
     |
Result Returned


--------------------------------------------------------------------------------
Executor vs ExecutorService
--------------------------------------------------------------------------------
Executor
---------
- execute()
- Fire-and-forget
- No shutdown
- No Future

ExecutorService
---------------
- execute()
- submit()
- shutdown()
- invokeAll()
- invokeAny()
- Future support


--------------------------------------------------------------------------------
Runnable vs Callable
--------------------------------------------------------------------------------
Runnable
---------
- run()
- Returns void
- Cannot throw checked exceptions

Callable
---------
- call()
- Returns value
- Can throw checked exceptions


--------------------------------------------------------------------------------
Future Lifecycle
--------------------------------------------------------------------------------
Created
   |
Running
   |
Completed
   |
Result Available
   |
future.get()


--------------------------------------------------------------------------------
Questions
--------------------------------------------------------------------------------
Q1. Why use Executor instead of creating Threads manually?
Because thread creation is expensive and Executors reuse threads using thread
pools.

----------------------------------------------------------
Q2. Difference between Executor and ExecutorService?
Executor only executes tasks.
ExecutorService additionally manages lifecycle and supports Future.

----------------------------------------------------------
Q3. When should Callable be used?
Whenever a task needs to return a value or throw checked exceptions.

----------------------------------------------------------
Q4. What does Future.get() do?
Blocks until the computation completes and returns the result.

----------------------------------------------------------
Q5. Why call shutdown()?
Without shutdown(), worker threads remain alive and the JVM may not terminate.

--------------------------------------------------------------------------------
Real-world Uses
--------------------------------------------------------------------------------
✔ Web Servers
✔ Database Query Execution
✔ Background Email Sending
✔ Payment Processing
✔ Batch Processing
✔ File Upload Services
✔ REST API Request Handling
================================================================================
*/

public class ExecutorBasics {

    /*
     * =========================================================================
     * 1. Executor
     * =========================================================================
     *
     * Executor is the simplest interface.
     * It only executes Runnable tasks.
     */
    private static void executorExample() {

        System.out.println("\n========== Executor Example ==========");

        //Executor executor = Executors.newSingleThreadExecutor(); //same but need service interface use shutdown

        ExecutorService service = Executors.newSingleThreadExecutor();
        //Reference type is Executor,
        //but actual object is ExecutorService.
        Executor executor = service;

        executor.execute(() ->  System.out.println("Task executed by: " + Thread.currentThread().getName()));

        service.shutdown();
    }

    /*
     * =========================================================================
     * 2. ExecutorService
     * =========================================================================
     *
     * ExecutorService extends Executor.
     * It provides lifecycle management.
     */
    private static void executorServiceExample() {

        System.out.println("\n========== ExecutorService Example ==========");

        ExecutorService service = Executors.newFixedThreadPool(2);

        service.execute(() -> System.out.println("Task A"));
        service.execute(() -> System.out.println("Task B"));

        service.shutdown();
    }

    /*
     * =========================================================================
     * 3. Callable + Future
     * =========================================================================
     *
     * Callable returns a value.
     * submit() returns a Future representing the result.
     */
    private static void callableFutureExample() throws Exception {

        System.out.println("\n========== Callable & Future Example ==========");

        ExecutorService service = Executors.newSingleThreadExecutor();

        Callable<Integer> task = () -> {

            Thread.sleep(2000);
            return 42;
        };

        // Future<Integer> future = service.submit(task);
        // System.out.println("Doing other work...");
        // Integer result = future.get();
        // System.out.println("Result = " + result);
        
        Future<Integer> future = service.submit(task);
        System.out.println("Main thread continues...");
        System.out.println("Task completed? " + future.isDone());
        Integer result = future.get();
        System.out.println("Result = " + result);
        System.out.println("Task completed? " + future.isDone());

        service.shutdown();
    }

    public static void main(String[] args) throws Exception {
        
        executorExample();
        executorServiceExample();
        callableFutureExample();
    }
}