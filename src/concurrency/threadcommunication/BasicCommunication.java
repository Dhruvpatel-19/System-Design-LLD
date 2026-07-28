package concurrency.threadcommunication;

/*
================================================================================
                        BASIC THREAD COMMUNICATION
================================================================================

Definition
----------
Thread Communication is a mechanism that allows multiple threads to coordinate
their execution by communicating with each other instead of continuously
checking shared data (busy waiting).

It enables one thread to wait until another thread performs a specific action.

Java provides three basic methods for thread communication:

1. wait()
2. notify()
3. notifyAll()

These methods are defined in the Object class because every object in Java can
act as a monitor (lock).

--------------------------------------------------------------------------------
Why do we need Thread Communication?
--------------------------------------------------------------------------------

Suppose there are two threads:

        Producer                  Consumer
            |                         |
      Produce Data              Consume Data
            |                         |
            +----------- Buffer ------+

Case 1:
If the buffer is empty, the Consumer should wait.

Case 2:
If the buffer is full, the Producer should wait.

Without thread communication:

    while(buffer.isEmpty()){
        // Keep checking...
    }

This is called Busy Waiting (or Polling).

Problems:
- Wastes CPU cycles
- Poor performance
- High CPU utilization

Instead, Java allows a thread to sleep until another thread wakes it.

--------------------------------------------------------------------------------
Monitor (Object Lock)
--------------------------------------------------------------------------------

wait(), notify(), and notifyAll() can only be called by a thread that currently
owns the monitor (lock) of the object.

Therefore these methods must always be called inside: synchronized method OR synchronized block
Otherwise Java throws: IllegalMonitorStateException

Example:
synchronized(lock){
    lock.wait();
}

--------------------------------------------------------------------------------
1. wait()
--------------------------------------------------------------------------------
Definition:
Causes the current thread to:
1. Release the monitor lock
2. Enter WAITING state
3. Stay blocked until another thread calls notify() or notifyAll()

Syntax: wait();

Behavior: 
Consumer Thread

Acquire Lock
      |
Buffer Empty?
      |
     Yes
      |
    wait()
      |
  Release Lock
      |
    WAITING
      |
   (notify())
      |
    Wake Up
      |
Acquire Lock Again
      |
Continue Execution

Important:
------------
• Releases the lock.
• Must be inside synchronized.
• Always use inside a while loop instead of if.

Example:
while(!available){
    wait();
}

Reason:
After waking up, the condition may no longer be true because another thread
might have consumed the resource.

--------------------------------------------------------------------------------
2. notify()
--------------------------------------------------------------------------------
Definition:
Wakes up ONE thread waiting on the same monitor.

Syntax: notify();

Example:
Waiting Threads

Thread-1
Thread-2
Thread-3

notify()

Only ONE thread wakes up.
Which thread wakes up is decided by the JVM scheduler.

Important:
-----------
notify() does NOT release the lock immediately.

The awakened thread can continue only after the current thread exits the
synchronized block.

--------------------------------------------------------------------------------
3. notifyAll()
--------------------------------------------------------------------------------
Definition:
Wakes ALL waiting threads.

Syntax: notifyAll();

Example:
Thread-1 WAITING
Thread-2 WAITING
Thread-3 WAITING

notifyAll()
    ↓
All become BLOCKED (Ready to compete)
    ↓
One acquires lock
    ↓
Others wait for the lock.

Useful when multiple waiting threads may be interested in the state change.

--------------------------------------------------------------------------------
wait() vs notify() vs notifyAll()
--------------------------------------------------------------------------------

wait()
-------
• Releases lock
• Thread goes into WAITING state
• Must reacquire lock before continuing

notify()
---------
• Wakes one waiting thread
• Does NOT release current lock immediately

notifyAll()
------------
• Wakes all waiting threads
• All compete again for the monitor


--------------------------------------------------------------------------------
Thread Lifecycle Example
--------------------------------------------------------------------------------

Consumer Thread

Acquire Lock
      |
Buffer Empty
      |
wait()
      |
WAITING ----------------------------+
                                    |
                                    |
Producer acquires lock              |
      |                             |
Produce Data                        |
      |                             |
notify() ---------------------------+
      |
Release Lock
      |
Consumer wakes
      |
Acquire Lock Again
      |
Consume Data


--------------------------------------------------------------------------------
Questions
--------------------------------------------------------------------------------

Q1. Why are wait(), notify(), and notifyAll() defined in Object instead of Thread?
Because every object in Java can act as a monitor (lock). Thread communication
is associated with the object's monitor, not the thread itself.

----------------------------------------------------------

Q2. Can wait() be called outside synchronized?
No. It throws: IllegalMonitorStateException

----------------------------------------------------------

Q3. Does notify() immediately wake another thread?
No. The waiting thread wakes only after the notifying thread releases the monitor.

----------------------------------------------------------

Q4. Why use while instead of if before wait()?
Because of:
• Spurious wakeups
• Another thread may consume the resource before this thread reacquires the lock.

--------------------------------------------------------------------------------
Real-world Examples
--------------------------------------------------------------------------------

✔ Producer-Consumer
✔ Print Queue
✔ Chat Application
✔ Order Processing
✔ Inventory Management
✔ Job Scheduling
✔ Banking Transactions

================================================================================
*/

class SharedResource {

    private boolean available = false;

    
    //Consumer waits until a resource becomes available.
    public synchronized void consume() {

        while (!available) {
            try {
                System.out.println(Thread.currentThread().getName()+ " is waiting...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println(Thread.currentThread().getName() + " consumed the resource.");
        available = false;
    }

    
    //Producer creates the resource and notifies one waiting thread.
    public synchronized void produce() {

        System.out.println(Thread.currentThread().getName() + " produced the resource.");
        available = true;
        notify();        // notify one waiting thread
        // notifyAll();  // Uncomment to wake all waiting threads
    }
}

class BasicCommunication {

    public static void main(String[] args) {

        SharedResource resource = new SharedResource();

        Thread consumer = new Thread(() -> resource.consume(), "Consumer");

        Thread producer = new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate some work
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            resource.produce();

        }, "Producer");

        consumer.start();
        producer.start();
    }
}