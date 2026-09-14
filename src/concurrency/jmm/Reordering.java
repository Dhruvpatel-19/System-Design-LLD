package concurrency.jmm;

/**
============================================================
Instruction Reordering
============================================================
Instruction reordering means that the order in which Java statements are written is not always the same
as the order in which operations are actually executed or seen by other threads.

The compiler, JVM, and CPU can reorder operations for better performance.

For a single-threaded program, these changes normally do not affect the result that the program is allowed to produce.

The problem becomes important when multiple threads share data.

Example:
    data = 42;
    ready = true;

We may think:
    1. data becomes 42
    2. ready becomes true
    3. another thread sees data = 42

Without proper synchronization, Java does not guarantee that another thread will observe these operations 
in the order we expect.

============================================================
COMPILER / JIT REORDERING
============================================================
The compiler and JIT can change the order of instructions when doing so does not break the guarantees provided 
by the Java Memory Model.

For example:
    int x = 10;
    int y = 20;

The compiler may internally arrange these operations differently if the result that the program is allowed to 
observe remains the same.

This is an optimization, not random execution.

============================================================
CPU REORDERING
============================================================
Modern CPUs also optimize how instructions are executed.

A CPU may execute some instructions out of order internally.

For example:
    Write A
    Write B

The CPU may process these operations internally in a different order.

The CPU still follows its architecture's rules, but without proper synchronization, multiple threads may not 
observe memory operations in the simple order we expect from the source code.

============================================================
SOURCE CODE ORDER VS EXECUTION ORDER
============================================================
Consider:
    x = 1;
    y = 2;

The source code clearly says:
    x = 1
        then
    y = 2

But this does not mean that every layer of the system must physically execute these operations in exactly that order.

There are several layers:
    Java Source Code
          |
          v
    Compiler / JIT
          |
          v
    Machine Instructions
          |
          v
         CPU
          |
          v
        Memory

The Java Memory Model defines what behavior Java programs can rely on.

============================================================
VISIBILITY VS ORDERING
============================================================
Visibility means: Can one thread see a value written by another thread?

Ordering means: Is one operation guaranteed to be observed before another operation?

Example:
    data = 42;
    ready = true;

Another thread:
    if (ready) {
        System.out.println(data);
    }

There are two questions here:
    1. Will the second thread see ready == true?
    2. If it sees ready == true, will it also see data == 42?

Without synchronization, the JMM does not give us the guarantees we need for this communication.

============================================================
HAPPENS-BEFORE AND REORDERING
============================================================
Happens-before is the JMM rule that tells us when one thread's actions are guaranteed to be visible 
and ordered before another thread's actions.

The detailed happens-before rules are covered in:
    HappensBeforeDemo.java

For reordering, remember the main idea:
    Happens-before
          |
          v
    Ordering + Visibility guarantees

When a happens-before relationship exists, the compiler and CPU cannot reorder operations in a way 
that breaks that relationship.

============================================================
VOLATILE AND REORDERING
============================================================
volatile provides visibility and ordering guarantees.

Example:
    data = 42;
    ready = true;       // ready is volatile

Another thread:
    if (ready) {        // volatile read
        System.out.println(data);
    }

The volatile write to ready acts as an important ordering point.

The write to data happens before the volatile write to ready.

When another thread sees the volatile write to ready, the previous write to data is guaranteed to be visible to that thread.

So volatile helps prevent problematic reordering between the normal
memory operation and the volatile operation.

============================================================
SYNCHRONIZED AND REORDERING
============================================================
synchronized also provides ordering and visibility guarantees.

Example:
    synchronized (LOCK) {
        data = 42;
    }

Another thread:
    synchronized (LOCK) {
        System.out.println(data);
    }

The synchronization mechanism prevents the compiler and CPU from reordering operations in a way 
that breaks the guarantees provided between these critical sections.

synchronized therefore provides:
    Mutual exclusion
    Visibility
    Ordering

============================================================
IMPORTANT IDEA
============================================================
Do not think: "Java executes statements randomly."

Instead, think: "The compiler, JVM, and CPU are allowed to optimize execution, 
but they must still follow the guarantees defined by the JMM."

Without synchronization:
    Source order
         !=
    Guaranteed order seen by other threads

With mechanisms such as volatile and synchronized:
    Required ordering
         +
    Required visibility
         =
    Safe communication between threads

============================================================
*/

class ReorderingDemo {

    /*
    ============================================================
    CLASSIC REORDERING EXAMPLE
    ============================================================    
    Two threads perform these operations:

    Thread 1:
        x = 1;
        r1 = y;

    Thread 2:
        y = 1;
        r2 = x;

    A surprising result can be:
        r1 = 0
        r2 = 0

    This does not mean Java simply executed both methods backwards.

    It demonstrates that without proper synchronization, we cannot assume a single global execution order between threads.
    */
    private static int x = 0;
    private static int y = 0;

    private static int r1 = 0;
    private static int r2 = 0;
    //Thread 1 writes x and then reads y.
    private static void threadOne() {
        x = 1;
        r1 = y;
    }


    //Thread 2 writes y and then reads x.
    private static void threadTwo() {
        y = 1;
        r2 = x;
    }


    /*
    ============================================================
    VOLATILE EXAMPLE
    ============================================================
    volatile provides visibility and ordering guarantees.
    The volatile variable acts as a synchronization point between the writer and reader.
    */
    private static int volatileData = 0;
    private static volatile boolean volatileReady = false;
    private static void volatileWriter() {

        volatileData = 42;
        //Volatile write provides an ordering boundary.
        volatileReady = true;
    }


    private static void volatileReader() {

        if (volatileReady) {
            //The previous write to volatileData is guaranteed to be visible.
            System.out.println("volatileData = " + volatileData);
        }
    }


    /*
    ============================================================
    SYNCHRONIZED EXAMPLE
    ============================================================
    synchronized provides mutual exclusion, visibility, and ordering.
    */
    private static int synchronizedData = 0;
    private static final Object LOCK = new Object();
    private static void synchronizedWriter() {

        synchronized (LOCK) {
            synchronizedData = 100;
        }
    }


    private static void synchronizedReader() {
        synchronized (LOCK) {
            //The write inside the previous critical section is visible.
            System.out.println("synchronizedData = " + synchronizedData);
        }
    }


    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n========== Reordering Example ==========");
        x = 0;
        y = 0;
        r1 = 0;
        r2 = 0;

        Thread thread1 = new Thread(ReorderingDemo::threadOne);
        Thread thread2 = new Thread(ReorderingDemo::threadTwo);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("r1 = " + r1);
        System.out.println("r2 = " + r2);


        System.out.println("\n========== Volatile Ordering ==========");
        volatileWriter();
        volatileReader();


        System.out.println("\n========== Synchronized Ordering ==========");
        synchronizedWriter();
        synchronizedReader();
    }
}