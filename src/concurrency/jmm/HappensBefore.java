package concurrency.jmm;

/**
 Demonstrates the major "happens-before" relationships defined by the Java Memory Model (JMM).
 
 Happens-before guarantees:
 
 1. Program Order
      Earlier action in a thread
          ↓
      Later action in the same thread
 
 2. Volatile Rule
      volatile write
          ↓
      subsequent volatile read
 
 3. Monitor Lock Rule
      unlock
          ↓
      subsequent lock on the same monitor
 
 4. Thread Start Rule
      actions before start()
          ↓
      actions inside started thread
 
 5. Thread Join Rule
      actions inside thread
          ↓
      successful return from join()
 
 Important:
 Happens-before is NOT necessarily the same as "happens earlier in wall-clock time". 
 It is a guarantee about visibility and ordering.
 */
class HappensBefore {

    /*
     ---------------------------------------------------------
     1. PROGRAM ORDER
     ---------------------------------------------------------
     Actions within the same thread are ordered according to program order.
     The write to x happens-before the read of x.
     */
    static void programOrderExample() {

        int x = 10;

        //Action 1
        x = 20;

        //Action 2
        System.out.println(x);

        /*
         Within this thread:
         
         x = 20
             ↓
         println(x)
         
         The write happens-before the read.
         */
    }


    /*
     ---------------------------------------------------------
     2. VOLATILE WRITE -> VOLATILE READ
     ---------------------------------------------------------
     A write to a volatile variable happens-before every subsequent read of that same volatile variable.
     This provides visibility between threads.
     */
    private static int data;
    private static volatile boolean ready;

    static void volatileExample() throws InterruptedException {

        Thread writer = new Thread(() -> {

            //Normal write
            data = 42;

            //Volatile write
            ready = true;

        });

        Thread reader = new Thread(() -> {

            while (!ready) {
                //Wait until volatile write becomes visible
            }
           
            System.out.println("data = " + data);
        });

        writer.start();
        reader.start();

        writer.join();
        reader.join();
    }


    /*
     ---------------------------------------------------------
     3. UNLOCK -> SUBSEQUENT LOCK
     ---------------------------------------------------------
     An unlock on a monitor happens-before every subsequent lock on that same monitor.
     synchronized uses the monitor lock.
     */
    private static int counter;

    static void lockExample() throws InterruptedException {

        Object lock = new Object();

        Thread writer = new Thread(() -> {

            synchronized (lock) {
                counter = 100;
            }

            //Exiting synchronized block performs an unlock.
             
        });

        Thread reader = new Thread(() -> {

            synchronized (lock) {
                /*
                 Acquiring the same lock performs a lock.
                 
                 writer's unlock
                        ↓
                 reader's subsequent lock
                 
                 Therefore the reader sees counter = 100.
                 */
                System.out.println("counter = " + counter);
            }
        });

        writer.start();
        writer.join();

        reader.start();
        reader.join();
    }


    /*
     ---------------------------------------------------------
     4. THREAD.START()
     ---------------------------------------------------------
     A call to Thread.start() happens-before any actions performed by the started thread.
     */
    private static int value;

    static void startExample() throws InterruptedException {

        //Action performed by main thread
        value = 50;

        Thread worker = new Thread(() -> {

            /*
             main thread:
             
                 value = 50
                       ↓
                 worker starts
                       ↓
                 read value
             
             The worker is guaranteed to see the write performed before start().
             */
            System.out.println("value = " + value);
        });

        worker.start();

        worker.join();
    }


    /*
     ---------------------------------------------------------
     5. THREAD.JOIN()
     ---------------------------------------------------------
     All actions performed by a thread happen-before another thread successfully returns from join() on that thread.
     */
    private static int result;

    static void joinExample() throws InterruptedException {

        Thread worker = new Thread(() -> {

            //Work performed by worker
            result = 200;
        });

        worker.start();

        //Wait until worker terminates.
        worker.join();

        /*
         worker:
         
             result = 200
                   ↓
             worker terminates
                   ↓
             join() returns
                   ↓
             main reads result
         
         Therefore main is guaranteed to see result = 200.
         */
        System.out.println("result = " + result);
    }


    /*
     ---------------------------------------------------------
     6. COMBINING HAPPENS-BEFORE RELATIONSHIPS
     ---------------------------------------------------------
     
     Happens-before relationships can be chained.
     
     Example:
     
         main thread
              |
              | data = 100
              ↓
           start()
              |
              ↓
         worker thread
              |
              | result = data
              ↓
           worker ends
              |
              ↓
           join()
              |
              ↓
         main thread
     */
    static void combinedExample() throws InterruptedException {

        int[] data = new int[1];
        int[] result = new int[1];

        data[0] = 100;

        Thread worker = new Thread(() -> {

            result[0] = data[0];

        });

        worker.start();

        worker.join();

        System.out.println("result = " + result[0]);
    }


    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== Program Order ===");
        programOrderExample();

        System.out.println("\n=== Volatile ===");
        volatileExample();

        System.out.println("\n=== Lock / Unlock ===");
        lockExample();

        System.out.println("\n=== Thread Start ===");
        startExample();

        System.out.println("\n=== Thread Join ===");
        joinExample();

        System.out.println("\n=== Combined ===");
        combinedExample();
    }
}