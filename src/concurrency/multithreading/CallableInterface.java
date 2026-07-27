package concurrency.multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/*
 * Callable:
 * - Callable represents a task that returns a result.
 * - It is similar to Runnable but with additional capabilities.
 *
 *
 * Runnable:
 * ---------
 * run() method
 * returns void
 * cannot throw checked exceptions
 *
 *
 * Callable:
 * ---------
 * call() method
 * returns a value
 * can throw checked exceptions
 *
 *
 * Callable is generally used with ExecutorService.
 *
 * Flow:
 *
 * Callable Task
 *       |
 *       v
 * ExecutorService.submit()
 *       |
 *       v
 * Future<V>
 *       |
 *       v
 * Result using future.get()
 */


/*
 * Callable uses Generic type.
 *
 * Callable<Integer>
 * means call() will return Integer.
 *
 * Callable<String>
 * means call() will return String.
 */
class CalculationTask implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + " is calculating...");
        Thread.sleep(2000);
        int result = 10 + 20;
        return result;
    }
}


class CallableExample {


    public static void main(String[] args) {

        /*
         * ExecutorService manages thread creation.
         *
         * Instead of manually creating Thread objects,
         * modern Java applications use Executor Framework.
         */
        ExecutorService executor = Executors.newSingleThreadExecutor();

        /*
         * submit():
         *
         * - Executes Callable task asynchronously.
         * - Returns Future object immediately.
         */
        Future<Integer> future = executor.submit(new CalculationTask());

        System.out.println("Main thread continues execution...");

        try {

            /*
             * get():
             *
             * - Retrieves result from Callable.
             * - If task is not completed,
             *   current thread waits.
             */
            Integer result = future.get();
            System.out.println( "Result: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Shutdown executor after completing tasks.
        executor.shutdown();

        System.out.println("Main thread finished.");
    }
}