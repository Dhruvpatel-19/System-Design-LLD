package concurrency.executors;

import java.util.concurrent.*;

/*
================================================================================
                        ASYNCHRONOUS PROGRAMMING
================================================================================

Definition
----------
Asynchronous programming allows tasks to execute independently without blocking
the main execution flow.

Instead of waiting for a task to complete:
Main Thread
    |
    |
    Task (takes time)
    |
    |
Continue execution


Asynchronous approach:
Main Thread
    |
    +----------------------+
                           |
                      Background Task
                           |
                           |
                     Result returned later


Java provides asynchronous programming mainly through:
1. ScheduledExecutorService
2. CompletableFuture


================================================================================
1. ScheduledExecutorService
================================================================================

Definition: ScheduledExecutorService is an ExecutorService that allows tasks to be executed:
1. After a specific delay
2. Periodically after fixed intervals

Creation:
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

Important Methods:

1. schedule(): Executes a task once after a delay.
Example:
scheduler.schedule(
    task,
    5,
    TimeUnit.SECONDS
);

Execution:
Submit Task
    |
    |
 Wait 5 seconds
    |
    |
 Execute Task


2. scheduleAtFixedRate(): Executes a task repeatedly after a fixed interval.
Example:
scheduler.scheduleAtFixedRate(
    task,
    1,
    5,
    TimeUnit.SECONDS
);

Execution:
Start
 |
 |
Task
 |
5 seconds
 |
Task
 |
5 seconds
 |
Task

The interval is calculated from the start time of execution.


3. scheduleWithFixedDelay(): Executes a task repeatedly with a delay after completion
Execution:
Task starts
 |
Task completes
 |
Wait 5 seconds
 |
Task starts again

Difference:
scheduleAtFixedRate(): Based on task start time
scheduleWithFixedDelay(): Based on task completion time

--------------------------------------------------------------------------------
Real World Uses
--------------------------------------------------------------------------------
✔ Scheduled email sending
✔ Cache refresh
✔ Database cleanup jobs
✔ Monitoring services
✔ Background maintenance tasks



================================================================================
2. CompletableFuture
================================================================================
Definition: CompletableFuture represents the result of an asynchronous computation.

It is an improvement over Future because it supports:
✔ Non-blocking execution
✔ Callbacks
✔ Task chaining
✔ Combining multiple async operations
✔ Exception handling

Creation:
CompletableFuture<String> future =
        CompletableFuture.supplyAsync(() -> {
            return "Result";
        });


--------------------------------------------------------------------------------
Future vs CompletableFuture
--------------------------------------------------------------------------------
Future:
Submit Task
    |
    |
future.get()
    |
Blocks until result available


CompletableFuture:
Submit Task
    |
    |
Continue execution
    |
    |
Callback executes when result arrives


Important Methods:
1. supplyAsync(): Used when asynchronous task returns a value.

Example:
CompletableFuture<String> future =
        CompletableFuture.supplyAsync(
            () -> "Data"
        );

    
2. runAsync(): Used when asynchronous task does not return a value.

Example:
CompletableFuture.runAsync(
    () -> saveData()
);



3. thenApply(): Transforms the result of previous task.

Example:
Fetch User
    |
    |
Convert User Object
    |
    |
Return Result


4. thenAccept(): Consumes the result.

Example:
.thenAccept(
    result -> print(result)
)


5. thenCombine(): Combines results from two independent tasks.

Example:
User Service
      |
      |
      +-------> Combine
      |
Payment Service


6. exceptionally(): Handles exceptions in async execution.

Example:
.exceptionally(
    error -> defaultValue
)



--------------------------------------------------------------------------------
Important  Questions/Points
--------------------------------------------------------------------------------
Q1. Which thread executes CompletableFuture tasks?
By default:
ForkJoinPool.commonPool()
Custom executor can also be provided.


----------------------------------------------------------
Q2. Difference between thenApply() and thenCompose()?
thenApply(): Used when result transformation is required.
thenCompose(): Used when chaining another asynchronous operation.

Example:

Future<User>
      |
      |
Fetch Orders
      |
      |
Future<List<Order>>



----------------------------------------------------------
Q3. Difference between Future and CompletableFuture?
Future:
- Blocking
- No chaining
- Limited exception handling

CompletableFuture:
- Asynchronous callbacks
- Task chaining
- Combining tasks
- Better exception handling


================================================================================
Real World Uses
================================================================================

✔ Calling multiple APIs concurrently
✔ Microservice communication
✔ Parallel database calls
✔ Asynchronous file processing
✔ Background computation
✔ Event-driven systems

================================================================================
*/


class AsyncProgramming {

    /*
     * =========================================================================
     * 1. ScheduledExecutorService - schedule()
     * =========================================================================
     *
     * Executes a task once after a delay.
     */
    private static void scheduledTaskExample() throws InterruptedException {

        System.out.println("\n========== Scheduled Task Example ==========");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(() -> {

            System.out.println("Task executed after delay by "+ Thread.currentThread().getName());

        }, 3, TimeUnit.SECONDS);

        Thread.sleep(4000);
        scheduler.shutdown();
    }


    /*
     * =========================================================================
     * 2. ScheduledExecutorService - scheduleAtFixedRate()
     * =========================================================================
     *
     * Executes task repeatedly after fixed intervals.
     */
    private static void scheduledFixedRateExample() throws InterruptedException {

        System.out.println("\n========== Fixed Rate Scheduling ==========");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Running periodic task "+ System.currentTimeMillis());
        },
        1,
        2,
        TimeUnit.SECONDS);
        Thread.sleep(7000);
        scheduler.shutdown();
    }


    /*
     * =========================================================================
     * 3. CompletableFuture - supplyAsync()
     * =========================================================================
     *
     * Executes task asynchronously and returns result.
     */
    private static void completableFutureExample() throws Exception {

        System.out.println("\n========== CompletableFuture Example ==========");

        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> {

                    System.out.println("Running async task on "+ Thread.currentThread().getName());
                    sleep(2000);
                    return "Data Loaded";
                });



        future.thenAccept(result -> {
            System.out.println("Result: " + result);
        });

        Thread.sleep(3000);
    }


    /*
     * =========================================================================
     * 4. CompletableFuture Chaining
     * =========================================================================
     *
     * Demonstrates:
     *
     * supplyAsync()
     * thenApply()
     * thenAccept()
     */
    private static void completableFutureChainingExample() throws Exception {

        System.out.println("\n========== CompletableFuture Chaining ==========");

        CompletableFuture
                .supplyAsync(() -> {
                    return "Dhruv";
                })
                .thenApply(name -> {
                    return "Hello " + name;
                })
                .thenAccept(message -> {
                    System.out.println(message);
                });

        Thread.sleep(1000);
    }


    /*
     * =========================================================================
     * 5. Combining Multiple Async Tasks
     * =========================================================================
     *
     * thenCombine() combines independent async results.
     */
    private static void combineAsyncTasks() throws Exception {

        System.out.println("\n========== Combine Async Tasks ==========");

        CompletableFuture<String> user =
                CompletableFuture.supplyAsync(() -> {
                    sleep(1000);
                    return "User Data";
                });

        CompletableFuture<String> payment =
                CompletableFuture.supplyAsync(() -> {

                    sleep(1000);
                    return "Payment Data";
                });

        user.thenCombine(
                payment,
                (u, p) -> u + " + " + p
        )
        .thenAccept(System.out::println);

        Thread.sleep(2000);
    }


    /*
     * =========================================================================
     * 6. Exception Handling
     * =========================================================================
     */
    private static void exceptionHandlingExample() throws Exception {

        System.out.println("\n========== Exception Handling ==========");

        CompletableFuture
                .supplyAsync(() -> {
                    throw new RuntimeException( "Something went wrong");
                })
                .exceptionally(error -> {
                    return "Default Result";
                })
                .thenAccept(System.out::println);

        Thread.sleep(1000);
    }


    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws Exception {

        scheduledTaskExample();

        scheduledFixedRateExample();

        completableFutureExample();

        completableFutureChainingExample();

        combineAsyncTasks();

        exceptionHandlingExample();
    }
}