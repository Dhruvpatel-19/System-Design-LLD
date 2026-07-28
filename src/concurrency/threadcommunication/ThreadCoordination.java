package concurrency.threadcommunication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

/*
================================================================================
                          THREAD COORDINATION
================================================================================
Definition
----------
Thread Coordination is the process of controlling when multiple threads should
start, wait, or continue execution based on the progress of other threads.

Unlike synchronization (which protects shared resources), thread coordination
focuses on coordinating execution among multiple threads.

Java provides several utilities for thread coordination:
1. CountDownLatch
2. CyclicBarrier
3. Phaser

Package: java.util.concurrent

--------------------------------------------------------------------------------
Why Thread Coordination?
--------------------------------------------------------------------------------
Suppose an application performs the following tasks during startup.

Main Thread
        |
        |
        +------ Load Database
        |
        +------ Load Cache
        |
        +------ Load Configuration
        |
        +------ Start Server

The server should start only after every initialization task completes.
Thread coordination utilities make this easy.

================================================================================
1. COUNTDOWNLATCH
================================================================================
Definition
----------
CountDownLatch allows one or more threads to wait until a fixed number of
operations complete.

A latch is initialized with a count.
Every completed task decreases the count.
When the count reaches zero, waiting threads continue.

--------------------------------------------------------------------------------
Workflow
--------------------------------------------------------------------------------

Initial Count = 3

Database Thread
countDown()

Count = 2

Cache Thread
countDown()

Count = 1

Config Thread
countDown()

Count = 0
    ↓
Main Thread continues

--------------------------------------------------------------------------------
Important Methods
--------------------------------------------------------------------------------
await()
Wait until count reaches zero.

------------------------------------------------
countDown()
Decrease count by one.

------------------------------------------------
getCount()
Returns remaining count.

--------------------------------------------------------------------------------
Important Characteristics
--------------------------------------------------------------------------------
✔ One-time use
Once count becomes zero,
the latch cannot be reused.
Need a new CountDownLatch instance.

--------------------------------------------------------------------------------
Real-world Uses
--------------------------------------------------------------------------------
✔ Application Startup
✔ Waiting for Multiple APIs
✔ Parallel Data Loading
✔ Unit Testing


================================================================================
2. CYCLICBARRIER
================================================================================
Definition
----------
CyclicBarrier allows multiple threads to wait for each other before proceeding.
Every participating thread must reach the barrier.
Once all threads arrive, they continue together.

--------------------------------------------------------------------------------
Workflow
--------------------------------------------------------------------------------
Worker-1
Worker-2
Worker-3

All arrive at barrier
    ↓
Barrier opens
    ↓
All continue

--------------------------------------------------------------------------------
Important Methods
--------------------------------------------------------------------------------
await()
Wait at barrier.

------------------------------------------------
reset()
Reuse barrier.

------------------------------------------------
getNumberWaiting()
Returns waiting threads.

--------------------------------------------------------------------------------
Important Characteristics
--------------------------------------------------------------------------------
✔ Reusable
After all threads cross,
the barrier automatically resets.

--------------------------------------------------------------------------------
Real-world Uses
--------------------------------------------------------------------------------
✔ Multiplayer Games
✔ Scientific Simulations
✔ Parallel Matrix Processing
✔ Batch Processing

================================================================================
3. PHASER
===============================================================================
Definition
----------
Phaser is a more flexible version of CyclicBarrier.

It supports:
✔ Multiple phases
✔ Dynamic thread registration
✔ Dynamic thread removal

--------------------------------------------------------------------------------
Workflow
--------------------------------------------------------------------------------

Phase 1
Thread-1
Thread-2
Thread-3
  ↓
Advance
  ↓
Phase 2
Thread-1
Thread-2
  ↓
Advance
  ↓
Phase 3

New Thread joins

--------------------------------------------------------------------------------
Important Methods
--------------------------------------------------------------------------------
register()
Register new participant.

------------------------------------------------
arriveAndAwaitAdvance()
Wait until all participants arrive.

------------------------------------------------
arriveAndDeregister()
Leave phaser.

------------------------------------------------
getPhase()
Returns current phase.

--------------------------------------------------------------------------------
Real-world Uses
--------------------------------------------------------------------------------
✔ Multi-stage Pipelines
✔ Build Systems
✔ Game Engines
✔ Workflow Processing



================================================================================
Comparison
================================================================================
CountDownLatch
• One-time synchronization
• One thread waits for others
• Cannot reset

------------------------------------------------
CyclicBarrier
• All threads wait for each other
• Reusable
• Fixed participants

------------------------------------------------
Phaser
• Multi-phase synchronization
• Dynamic participants
• Reusable


================================================================================
Questions
================================================================================
Q1. Difference between CountDownLatch and CyclicBarrier?
CountDownLatch: One or more threads wait until tasks complete.
CyclicBarrier: All participating threads wait for each other.

------------------------------------------------
Q2. Can CountDownLatch be reused?
No. Need to create a new object.

------------------------------------------------
Q3. Why Phaser instead of CyclicBarrier?
Because Phaser supports:
• Multiple phases
• Dynamic registration
• Dynamic deregistration

------------------------------------------------
Q4. Which is best for application startup?
CountDownLatch

------------------------------------------------
Q5. Which is best for iterative algorithms?
CyclicBarrier or Phaser

================================================================================
*/



/*
===============================================================================
                    COUNTDOWNLATCH EXAMPLE
===============================================================================
*/
class StartupTask implements Runnable {

    private final CountDownLatch latch;
    private final String taskName;

    public StartupTask(CountDownLatch latch, String taskName) {
        this.latch = latch;
        this.taskName = taskName;
    }

    @Override
    public void run() {

        System.out.println(taskName + " loading...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println(taskName + " completed.");

        latch.countDown();
    }
}


/*
===============================================================================
                    CYCLICBARRIER EXAMPLE
===============================================================================
*/
class RaceRunner implements Runnable {

    private final CyclicBarrier barrier;

    public RaceRunner(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " reached barrier.");
        try {
            barrier.await();
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + " started next phase.");
    }
}


/*
===============================================================================
                        PHASER EXAMPLE
===============================================================================
*/
class EmployeeTask implements Runnable {

    private final Phaser phaser;

    public EmployeeTask(Phaser phaser) {
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName() + " completed Phase 1");
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName() + " completed Phase 2");
        phaser.arriveAndAwaitAdvance();
        System.out.println(Thread.currentThread().getName() + " completed Phase 3");
        phaser.arriveAndDeregister();
    }
}


public class ThreadCoordination {

    public static void main(String[] args) throws Exception {

        System.out.println("========== COUNTDOWNLATCH ==========");

        CountDownLatch latch = new CountDownLatch(3);

        new Thread(new StartupTask(latch, "Database")).start();
        new Thread(new StartupTask(latch, "Cache")).start();
        new Thread(new StartupTask(latch, "Configuration")).start();
        latch.await();
        System.out.println("Application Started.\n");


        System.out.println("========== CYCLICBARRIER ==========");
        CyclicBarrier barrier = new CyclicBarrier(
                3,
                () -> System.out.println("All runners are ready.\n")
        );

        for (int i = 1; i <= 3; i++) {
            new Thread( new RaceRunner(barrier), "Runner-" + i).start();
        }
        Thread.sleep(3000);


        System.out.println("\n========== PHASER ==========");
        Phaser phaser = new Phaser();
        new Thread(new EmployeeTask(phaser), "Employee-1").start();
        new Thread(new EmployeeTask(phaser), "Employee-2").start();
        new Thread(new EmployeeTask(phaser), "Employee-3").start();
    }
}