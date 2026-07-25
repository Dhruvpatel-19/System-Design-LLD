package concurrency.multithreading;

/*
* Thread:
* - A Thread is the smallest unit of execution within a process.
* - A Java application starts with one thread called the Main Thread.
* - Threads allow multiple tasks to run concurrently, improving responsiveness
*   and resource utilization.
*
* Creating Threads:
* 1. Extending Thread class
* 2. Implementing Runnable interface (Preferred approach)
*
* start():
* - Creates a new thread and internally calls run().
* - The new thread executes independently.
*
* run():
* - Contains the task that the thread executes.
* - Calling run() directly does NOT create a new thread.
* - It behaves like a normal method call.
*/


/*
 Why Runnable is preferred:
 
 1. Java supports single inheritance.
    - If a class extends Thread, it cannot extend any other class.
    - Implementing Runnable allows the class to extend another class.

 2. Separation of responsibility:
    - Runnable represents the task.
    - Thread represents the execution mechanism.
    
    Example:
        Runnable -> What needs to be done
        Thread   -> How it runs


 3. Better resource sharing:
    - Multiple threads can execute the same Runnable object.

 4. Works better with Executor Framework:
    - Modern Java applications usually use ExecutorService
      instead of manually creating Thread objects.
*/


/*
 Thread Lifecycle:

 NEW
 ---
 Thread object is created but start() is not called.


 RUNNABLE
 --------
 Thread is ready to run or currently running.


 BLOCKED
 -------
 Waiting to acquire a lock.


 WAITING
 -------
 Waiting indefinitely for another thread.


 TIMED_WAITING
 -------------
 Waiting for a specific amount of time.
 Example: sleep(), join(timeout)


 TERMINATED
 ----------
 Thread execution is completed.
*/

/*
 Important Thread Methods:

 start()
 -------
 Creates a new thread and invokes run().
 
 Example:
 thread.start();


 run()
 -----
 Contains thread execution logic.
 Calling directly does not create a new thread.


 sleep(milliseconds)
 -------------------
 Pauses the currently executing thread temporarily.


 join()
 ------
 Makes one thread wait until another thread completes.


 interrupt()
 -----------
 Sends interruption request to a thread.
 Used for stopping/waking threads gracefully.


 currentThread()
 ---------------
 Returns reference to currently executing thread.


 getName()
 ----------
 Returns thread name.


 setName()
 ----------
 Assigns custom name to a thread.
*/

//Approach 1: Extending Thread class
class WorkerThread extends Thread {

    @Override
    public void run() {

        for (int i = 1; i <= 5; i++) {

            System.out.println(
                    Thread.currentThread().getName()
                            + " -> Working using Thread class: " + i);

            try {
                Thread.sleep(500); //Pause current thread for 500 ms
            } catch (InterruptedException e) {

                //Restore interrupted status
                Thread.currentThread().interrupt();
            }
        }
    }
}


//Approach 2: Implementing Runnable interface
class RunnableWorker implements Runnable {

    @Override
    public void run() {

        for (int i = 1; i <= 5; i++) {

            System.out.println(
                    Thread.currentThread().getName()
                            + " -> Working using Runnable: " + i);

            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }
    }
}


class MultithreadingMain {

    public static void main(String[] args) {


        System.out.println(
                "Main thread started: "
                        + Thread.currentThread().getName());
        
        //Creating thread by extending Thread class
        WorkerThread workerThread = new WorkerThread();
        workerThread.setName("Worker-Thread");
        workerThread.start();



        //Creating thread using Runnable interface
        //Runnable only represents the task.
        //Thread represents the actual execution unit.    
        RunnableWorker task = new RunnableWorker();
        Thread runnableThread = new Thread(task);
        runnableThread.setName("Runnable-Thread");
        runnableThread.start();


        //Main thread continues execution independently.
        for (int i = 1; i <= 5; i++) {

            System.out.println( Thread.currentThread().getName() + " -> Main Task: " + i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }


        //join(): Main thread waits until worker threads complete.
        try {
            workerThread.join();
            runnableThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Main thread finished.");
    }
}