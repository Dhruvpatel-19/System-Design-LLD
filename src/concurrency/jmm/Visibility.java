package concurrency.jmm;

/**
Demonstrates the visibility problem described by the Java Memory Model (JMM).

Two threads share a variable:

    main thread ---> stop = true
         |
         | should become visible
         v
    worker thread ---> while (!stop)

Without a happens-before relationship, the worker thread is NOT guaranteed
to see the updated value of stop.

Using volatile establishes the required visibility guarantee.
*/

public class Visibility {

    //Try removing 'volatile' to observe the visibility issue.
    private static volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {

        Thread worker = new Thread(() -> {
            System.out.println("Worker thread started.");

            while (running) {
                //Simulating some work.
            }

            System.out.println("Worker thread stopped.");
        });

        worker.start();

        //Give the worker thread time to start.
        Thread.sleep(1000);

        System.out.println("Main thread setting running = false.");

        running = false;

        worker.join();

        System.out.println("Main thread finished.");
    }
}