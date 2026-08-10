package concurrency.executors;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/*
================================================================================
                              FORK/JOIN FRAMEWORK
================================================================================
The Fork/Join Framework is a part of java.util.concurrent that is designed
for executing CPU-intensive tasks that can be broken down into smaller
independent subtasks.

Instead of manually creating and managing threads, Fork/Join allows us to:
    1. Split a large task into smaller subtasks
    2. Execute those subtasks in parallel
    3. Combine their results

This is called a divide-and-conquer approach.

================================================================================
ForkJoinPool
================================================================================
ForkJoinPool is a specialized ExecutorService designed to execute
Fork/Join tasks.

It maintains a pool of worker threads and distributes tasks among them.

Example:
    ForkJoinPool pool = new ForkJoinPool();
    pool.invoke(task);

Unlike a normal ExecutorService, ForkJoinPool is specifically optimized
for tasks that recursively split themselves into smaller tasks.

--------------------------------------------------------------------------------
Parallelism
--------------------------------------------------------------------------------
ForkJoinPool can be created with a specific level of parallelism:
    ForkJoinPool pool = new ForkJoinPool(4);

This creates a pool designed to use 4 worker threads.

We can also use:
    ForkJoinPool.commonPool();

which returns Java's shared common ForkJoinPool.


================================================================================
1. RecursiveTask<V>
================================================================================
RecursiveTask is used when a task needs to RETURN a result.

Syntax:
    class MyTask extends RecursiveTask<Integer> {

        @Override
        protected Integer compute() {
            ...
        }
    }

The generic type represents the result returned by the task.

For example:
    RecursiveTask<Integer>
means the task returns an Integer.

A RecursiveTask normally follows this structure:
    if (task is small enough) {
        calculate directly;
    } else {
        split task into smaller tasks;
        leftTask.fork();
        rightResult = rightTask.compute();
        leftResult = leftTask.join();

        return leftResult + rightResult;
    }

Important methods:
compute(): Contains the actual task logic.

fork(): Schedules the task for asynchronous execution.

join(): Waits for a forked task to finish and returns its result.

invoke(): Executes a task and waits for its completion/result.


================================================================================
2. RecursiveAction
================================================================================
RecursiveAction is similar to RecursiveTask, but it DOES NOT return a result.

Use RecursiveAction when the task performs an operation but does not
need to produce a value.

Example use cases:
    - Modify array elements
    - Process files
    - Transform data
    - Perform calculations where the result is stored elsewhere

Example:
    class MyTask extends RecursiveAction {

        @Override
        protected void compute() {
            ...
        }
    }

RecursiveAction Pattern:
    if (task is small enough) {
        process directly;
    } else {
        split task;
        invokeAll(leftTask, rightTask);
    }


================================================================================
Work Stealing
================================================================================
Work stealing is one of the most important features of ForkJoinPool.

Each worker thread maintains its own queue/deque of tasks.

Conceptually:
        Worker 1              Worker 2
        --------              --------
        Task A                Task D
        Task B                Task E
        Task C

If Worker 1 finishes all its tasks while Worker 2 still has work:
        Worker 1              Worker 2
        --------              --------
        IDLE                  Task D
                              Task E
                              Task F

Worker 1 can STEAL a task from Worker 2:
        Worker 1              Worker 2
        --------              --------
        Task F                Task D
                              Task E

This helps keep worker threads busy and improves CPU utilization.

Important:
We normally do NOT implement work stealing ourselves.
ForkJoinPool handles work stealing internally.


================================================================================
fork() + compute() + join() Pattern
================================================================================
A very common Fork/Join pattern is:
    leftTask.fork();

    rightResult = rightTask.compute();

    leftResult = leftTask.join();


Why not simply do:
    leftTask.fork();
    rightTask.fork();

    leftTask.join();
    rightTask.join();


The first pattern is often preferred because the current worker immediately
starts working on the right task instead of simply waiting.

This allows the ForkJoinPool to make better use of worker threads.


================================================================================
RecursiveTask vs RecursiveAction
================================================================================
RecursiveTask<V>: Returns a result.
Example:
    RecursiveTask<Integer> task;
    int result = pool.invoke(task);

RecursiveAction: Does not return a result.
Example:
    RecursiveAction task;
    pool.invoke(task);

Simple rule:
    Need a result? -> RecursiveTask<V>
    Don't need a result? -> RecursiveAction


================================================================================
invokeAll() with RecursiveAction
================================================================================
RecursiveAction does not return a result, so there is no need to call
rightTask.compute() and collect a return value.

invokeAll() can be used to execute multiple RecursiveAction subtasks and
wait until all of them complete.

Example:
    invokeAll(leftTask, rightTask);

Simple rule:
    RecursiveTask<V>  -> compute one + join the other when results are needed.
    RecursiveAction   -> invokeAll() when multiple actions must complete.


================================================================================
Fork/Join vs ExecutorService
================================================================================
ExecutorService: Best suited for submitting independent tasks:
    executor.submit(task);

ForkJoinPool: Best suited for:
    Large task
        |
        +---- Smaller task
        |       |
        |       +---- Smaller task
        |
        +---- Smaller task
                |
                +---- Smaller task

In other words, ForkJoinPool is particularly useful for
divide-and-conquer problems.


================================================================================
Common Use Cases
================================================================================
Fork/Join is useful for:
    - Large array processing
    - Recursive algorithms
    - Merge sort
    - Quick sort variants
    - Searching large datasets
    - Image processing
    - CPU-intensive calculations
    - Divide-and-conquer algorithms

It is generally NOT ideal for:
    - Database calls
    - Network requests
    - Long blocking I/O operations
    - Tasks that spend most of their time waiting


================================================================================
Important Points
================================================================================
Q1. What is ForkJoinPool?
A specialized ExecutorService designed for executing recursively
splittable tasks using worker threads and work stealing.

Q2. What is RecursiveTask?
A Fork/Join task that returns a result.

Q3. What is RecursiveAction?
A Fork/Join task that does not return a result.

Q4. What is work stealing?
When a worker thread becomes idle, it can take/steal tasks from another
worker's queue instead of remaining idle.

Q5. What is fork()?
Schedules a task for asynchronous execution.

Q6. What is join()?
Waits for a forked task to complete and obtains its result.

Q7. What is invoke()?
Executes a task and waits for its completion/result.

Q8. What is the commonPool()?
A shared ForkJoinPool provided by Java that can be used for parallel tasks.
================================================================================
*/


class ForkJoinPoolDemo {

    /*
    ================================================================================
                            RecursiveTask Example
    ================================================================================
    Calculates the sum of an integer array.
    The task recursively divides the array into smaller portions until the
    portion becomes small enough to calculate directly.
    */
    static class SumTask extends RecursiveTask<Integer> {

        private static final int THRESHOLD = 2;

        private final int[] numbers;
        private final int start;
        private final int end;

        SumTask(int[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {

            int size = end - start;

            // Base case
            if (size <= THRESHOLD) {

                int sum = 0;

                for (int i = start; i < end; i++) {
                    sum += numbers[i];
                }

                return sum;
            }

            // Divide the task
            int middle = start + size / 2;
            SumTask leftTask = new SumTask(numbers, start, middle);
            SumTask rightTask = new SumTask(numbers, middle, end);

            /*
             * Fork the left task.
             *
             * The left task can now be executed asynchronously by
             * a ForkJoinPool worker.
             */
            leftTask.fork();

            /*
             * Instead of immediately forking the right task,
             * the current worker computes it.
             */
            int rightResult = rightTask.compute();

            /*
             * Wait for the left task and obtain its result.
             */
            int leftResult = leftTask.join();

            return leftResult + rightResult;
        }
    }


    /*
    ================================================================================
                            RecursiveAction Example
    ================================================================================
    RecursiveAction does not return a result.
    This example simply processes each element of an array.
    */
    static class PrintTask extends RecursiveAction {

        private static final int THRESHOLD = 2;

        private final int[] numbers;
        private final int start;
        private final int end;

        PrintTask(int[] numbers, int start, int end) {
            this.numbers = numbers;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {

            int size = end - start;

            // Base case
            if (size <= THRESHOLD) {
                for (int i = start; i < end; i++) {
                    System.out.println(  "Processing " + numbers[i] + " -> " + Thread.currentThread().getName());
                }
                return;
            }

            // Divide the task
            int middle = start + size / 2;
            PrintTask leftTask = new PrintTask(numbers, start, middle);
            PrintTask rightTask = new PrintTask(numbers, middle, end);

            /*
             * invokeAll() forks both tasks and waits for both
             * tasks to complete.
             */
            invokeAll(leftTask, rightTask);
        }
    }


    /*
    ================================================================================
                            Work Stealing Example
    ================================================================================
    The following task creates many smaller tasks.

    Different worker threads can process these tasks.

    If one worker becomes idle while another worker still has pending tasks,
    the idle worker can steal work from another worker.

    Work stealing is automatically handled by ForkJoinPool.
    */
    static class WorkStealingTask extends RecursiveAction {

        private static final int THRESHOLD = 2;

        private final int start;
        private final int end;

        WorkStealingTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {

            int size = end - start;

            // Base case
            if (size <= THRESHOLD) {
                System.out.println( "Processing range [" + start + ", "+ end + ") by " + Thread.currentThread().getName());
                return;
            }

            int middle = start + size / 2;

            WorkStealingTask leftTask = new WorkStealingTask(start, middle);

            WorkStealingTask rightTask = new WorkStealingTask(middle, end);

            /*
             * Both tasks are made available to the ForkJoinPool.
             *
             * The pool decides which worker executes them.
             */
            invokeAll(leftTask, rightTask);
        }
    }


    public static void main(String[] args) {

        /*
         * =========================================================================
         * 1. ForkJoinPool
         * =========================================================================
         */
        @SuppressWarnings("resource")
        ForkJoinPool pool = new ForkJoinPool();
        System.out.println("ForkJoinPool parallelism: " + pool.getParallelism());


        /*
         * =========================================================================
         * 2. RecursiveTask
         * =========================================================================
         */
        System.out.println("\n========== RecursiveTask Example ==========");

        int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        SumTask sumTask = new SumTask(numbers, 0, numbers.length);
        int sum = pool.invoke(sumTask);
        System.out.println("Sum: " + sum);


        /*
         * =========================================================================
         * 3. RecursiveAction
         * =========================================================================
         */
        System.out.println("\n========== RecursiveAction Example ==========");
        PrintTask printTask = new PrintTask(numbers, 0, numbers.length);
        pool.invoke(printTask);


        /*
         * =========================================================================
         * 4. Work Stealing
         * =========================================================================
         */
        System.out.println("\n========== WorkStealing Demo ==========");
        WorkStealingTask workStealingTask = new WorkStealingTask(0, 20);
        pool.invoke(workStealingTask);


        /*
         * =========================================================================
         * 5. Shutdown
         * =========================================================================
         */
        pool.shutdown();
    }
}