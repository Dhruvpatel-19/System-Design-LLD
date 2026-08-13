package concurrency.concurrentcollections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
============================================================================
BLOCKING QUEUE
============================================================================
A BlockingQueue is a thread-safe Queue that provides operations which can
BLOCK a thread when the requested operation cannot be completed immediately.

The two most important blocking scenarios are:
1. Queue is EMPTY:  Consumer calling take() waits until an element becomes available.
2. Queue is FULL: Producer calling put() waits until space becomes available.


============================================================================
WHY BLOCKINGQUEUE?
============================================================================
A normal thread-safe queue such as ConcurrentLinkedQueue provides
thread-safe, non-blocking operations.

Example:
     ConcurrentLinkedQueue
             |
             | poll()
             v
     Queue empty?
         |
         +---- YES ---> return null immediately


BlockingQueue provides an additional thread-coordination mechanism:
     BlockingQueue
             |
             | take()
             v
     Queue empty?
         |
         +---- YES ---> WAIT until an element is available


Similarly, with a bounded BlockingQueue:
     put()
       |
       v
     Queue full?
       |
       +---- YES ---> WAIT until space is available


Therefore:
     ConcurrentLinkedQueue
         -> Thread-safe queue
         -> Non-blocking
         -> Useful when threads should not wait

     BlockingQueue
         -> Thread-safe queue
         -> Can block
         -> Useful for producer-consumer coordination
         -> Can provide backpressure with bounded queues


============================================================================
WHEN TO USE WHICH?
============================================================================
ConcurrentLinkedQueue:
     Use when:
         - You only need a thread-safe queue
         - Operations should return immediately
         - Consumer should not wait for an element
         - You don't need producer-consumer blocking coordination

BlockingQueue:
     Use when:
         - Consumer should wait when queue is empty
         - Producer should wait when queue is full
         - You need producer-consumer coordination
         - You need timeout-based operations
         - You need bounded capacity / backpressure

IMPORTANT:
BlockingQueue is NOT always better than ConcurrentLinkedQueue. They solve different problems.


============================================================================
IMPLEMENTATIONS
============================================================================
1. ArrayBlockingQueue
     - Backed by an array
     - Bounded
     - Fixed capacity
     - Cannot grow beyond its capacity
     Example:
         new ArrayBlockingQueue<>(10);


2. LinkedBlockingQueue
     - Backed by linked nodes
     - Can be bounded or effectively unbounded
     - Capacity can be specified
     Example:
         new LinkedBlockingQueue<>();
         new LinkedBlockingQueue<>(10);


3. PriorityBlockingQueue
     - Thread-safe priority queue
     - Elements are ordered according to their priority
     - Unbounded
     - Does NOT block because of capacity
     - take() can still block when the queue is empty


4. DelayQueue
     - Unbounded
     - Elements become available only after their delay expires
     - Elements must implement Delayed
     - take() waits until an element's delay has expired


============================================================================
IMPORTANT OPERATIONS
============================================================================
INSERTION:
add(e)
     - Adds element
     - Throws IllegalStateException if queue is full

offer(e)
     - Adds element if possible
     - Returns false if queue is full
     - Does NOT block

put(e)
     - Adds element
     - BLOCKS if queue is full
     - Throws InterruptedException if waiting thread is interrupted


REMOVAL:
remove()
     - Removes and returns head
     - Throws NoSuchElementException if queue is empty

poll()
     - Removes and returns head
     - Returns null if queue is empty
     - Does NOT block

take()
     - Removes and returns head
     - BLOCKS if queue is empty
     - Throws InterruptedException if waiting thread is interrupted


TIMEOUT OPERATIONS:
offer(e, timeout, unit)
     - Waits for space for at most the specified time
     - Returns false if space does not become available

poll(timeout, unit)
     - Waits for an element for at most the specified time
     - Returns null if no element becomes available


QUICK OPERATION TABLE:
                 Throws Exception    Returns Special Value    Blocks       Times Out
Add                add(e)             offer(e)                put(e)       offer(e, time)
Remove             remove()           poll()                  take()       poll(time)
Examine            element()          peek()                  -            -


============================================================================
BOUNDED VS UNBOUNDED
============================================================================
BOUNDED:
     Queue has a fixed maximum capacity.
     Example:
         BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(2);

     Once the queue contains 2 elements:
         offer() -> false
         put()   -> blocks

UNBOUNDED:
     Queue does not have a user-defined fixed capacity.
     Examples:
         PriorityBlockingQueue
         DelayQueue
         new LinkedBlockingQueue<>()

IMPORTANT:
"Unbounded" does NOT mean infinite.
The queue can still consume available memory if producers keep adding
elements faster than consumers remove them.


============================================================================
PRODUCER-CONSUMER
============================================================================
One of the most important uses of BlockingQueue is the Producer-Consumer
pattern.

         PRODUCER
             |
             | put()
             v
      +----------------+
      | BlockingQueue  |
      +----------------+
             |
             | take()
             v
         CONSUMER


If the queue is EMPTY: Consumer -> take() -> waits
If the queue is FULL: Producer -> put() -> waits

This gives us thread coordination without manually implementing:
     wait()
     notify()
     notifyAll()


============================================================================
INTERRUPTED EXCEPTION
============================================================================
put() and take() are interruptible blocking operations.

Example:
     queue.take();

If the thread is waiting and another thread interrupts it:
     thread.interrupt();
take() throws InterruptedException.

A common pattern is:
     try {
         queue.take();
     } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
     }


============================================================================
THREAD COORDINATION
============================================================================
BlockingQueue handles the waiting/coordination required by producer-consumer
scenarios internally.

Without BlockingQueue, we might need to coordinate threads using mechanisms
such as:
     synchronized
     wait()
     notify()
     notifyAll()

With BlockingQueue:
     producer -> put()
     consumer -> take()

The queue handles the blocking and coordination.


============================================================================
Questions
============================================================================
1. Is BlockingQueue thread-safe?
Yes.

2. Is BlockingQueue always better than ConcurrentLinkedQueue?
No. BlockingQueue is useful when blocking/thread coordination is required.
ConcurrentLinkedQueue is useful when non-blocking queue operations are preferred.

3. Which BlockingQueue is bounded?
ArrayBlockingQueue is always bounded.
LinkedBlockingQueue can be bounded if capacity is specified.

4. Which BlockingQueues are unbounded?
PriorityBlockingQueue and DelayQueue

5. Does PriorityBlockingQueue block?
It can block when empty because take() waits.
However, it does not block because of capacity because it is unbounded.

6. Does put() always block?
No. It only blocks when the queue is full.
For an unbounded queue, there is normally no capacity-related blocking.

7. Does take() always block?
No. It only blocks when the queue is empty.

8. What is the main use case of BlockingQueue?
Producer-Consumer pattern.


9. Why is BlockingQueue useful for backpressure?
A bounded queue can prevent producers from continuously producing unlimited amounts of work.
When the queue becomes full, producers must wait.

============================================================================
*/

class BlockingQueueDemo {

    // ========================================================================
    // 1. BASIC OPERATIONS
    // ========================================================================
    private static void basicOperations() throws InterruptedException {

        System.out.println("\n===== BASIC OPERATIONS =====");

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);

        // --------------------------------------------------------------------
        // add()
        // --------------------------------------------------------------------
        // Adds the element.
        // If queue is full: add() -> IllegalStateException
        queue.add(10);
        queue.add(20);

        System.out.println("After add(): " + queue);

        // --------------------------------------------------------------------
        // offer()
        // --------------------------------------------------------------------
        // Adds element without blocking.
        // If queue is full: offer() -> false
        boolean added = queue.offer(30);
        System.out.println("offer(30): " + added);
        System.out.println("Queue: " + queue);
        boolean rejected = queue.offer(40);
        System.out.println("offer(40): " + rejected);
       

        // --------------------------------------------------------------------
        // remove()
        // --------------------------------------------------------------------
        // Removes the head.
        // If queue is empty: remove() -> NoSuchElementException
        Integer value = queue.remove();
        System.out.println("remove(): " + value);

        // --------------------------------------------------------------------
        // poll()
        // --------------------------------------------------------------------
        // Removes the head without blocking.
        // If queue is empty: poll() -> null
        value = queue.poll();
        System.out.println("poll(): " + value);

        // --------------------------------------------------------------------
        // put()
        // --------------------------------------------------------------------
        // Adds element.
        // If queue is full: put() -> BLOCKS
        queue.put(40);
        System.out.println("After put(40): " + queue);

        // --------------------------------------------------------------------
        // take()
        // --------------------------------------------------------------------
        // Removes the head.
        // If queue is empty: take() -> BLOCKS
        value = queue.take();
        System.out.println("take(): " + value);
    }

    // ========================================================================
    // 2. BOUNDED VS UNBOUNDED
    // ========================================================================
    private static void boundedVsUnbounded() {

        System.out.println("\n===== BOUNDED VS UNBOUNDED =====");

        // --------------------------------------------------------------------
        // BOUNDED
        // --------------------------------------------------------------------
        BlockingQueue<Integer> boundedQueue = new ArrayBlockingQueue<>(2);

        boundedQueue.offer(1);
        boundedQueue.offer(2);
        System.out.println("Bounded queue: " + boundedQueue);

        //Queue is full, therefore offer() returns false.
        System.out.println(
                "offer(3): " + boundedQueue.offer(3)
        );

        // --------------------------------------------------------------------
        // UNBOUNDED
        // --------------------------------------------------------------------
        BlockingQueue<Integer> unboundedQueue = new LinkedBlockingQueue<>();
        unboundedQueue.add(1);
        unboundedQueue.add(2);
        unboundedQueue.add(3);

        System.out.println("Unbounded queue: " + unboundedQueue);
    }

    // ========================================================================
    // 3. PRODUCER-CONSUMER
    // ========================================================================
    private static void producerConsumer() throws InterruptedException {

        System.out.println("\n===== PRODUCER-CONSUMER =====");
        /*
         * Queue capacity = 3.
         *
         * Producer:
         *      put()
         *        |
         *        v
         *   +----------+
         *   |  Queue   |
         *   +----------+
         *        |
         *        v
         * Consumer:
         *      take()
         *
         * If queue becomes FULL: Producer -> put() -> waits
         * If queue becomes EMPTY: Consumer -> take() -> waits
         */
        BlockingQueue<Integer> queue =  new ArrayBlockingQueue<>(3);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Producer produced: " + i);
                    queue.put(i);
                    System.out.println("Producer inserted: " + i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                /*
                 * The producer was interrupted.
                 * Restore the interrupt status so higher-level code can
                 * detect that interruption occurred.
                 */
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted");
            }

        });

        Thread consumer = new Thread(() -> {
            try{
                for (int i = 1; i <= 5; i++) {
                    int value = queue.take();
                    System.out.println("Consumer consumed: " + value);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                //Restore interruption status.
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted");
            }

        });

        producer.start();
        consumer.start();

        //join() makes the main thread wait until these threads finish 
        //join() can throw InterruptedException, so declared with throws as we are not handling in catch block     
        producer.join();    
        consumer.join();
    }

    // ========================================================================
    // 4. PRIORITY BLOCKING QUEUE
    // ========================================================================
    private static void priorityBlockingQueue()  throws InterruptedException {

        System.out.println("\n===== PRIORITY BLOCKING QUEUE =====");

        /*
         * PriorityBlockingQueue:
         *      - Thread-safe
         *      - Unbounded
         *      - Elements are ordered by priority
         *
         * take() returns the highest-priority element according to the
         * queue's ordering.
         */
        BlockingQueue<Integer> queue = new PriorityBlockingQueue<>();

        queue.put(30);
        queue.put(10);
        queue.put(20);

        System.out.println(queue.take()); // 10
        System.out.println(queue.take()); // 20
        System.out.println(queue.take()); // 30
    }

    // ========================================================================
    // 5. DELAY QUEUE
    // ========================================================================
    private static void delayQueue() throws InterruptedException {

        System.out.println("\n===== DELAY QUEUE =====");
        /*
         * DelayQueue:
         *      - Unbounded
         *      - Thread-safe
         *      - Elements become available only after their delay expires
         *      - Elements must implement Delayed
         *
         * take() blocks until an element's delay has expired.
         */

        BlockingQueue<DelayedTask> queue = new DelayQueue<>();

        queue.put(new DelayedTask("Task 1",2, TimeUnit.SECONDS));
        queue.put(new DelayedTask("Task 2",4, TimeUnit.SECONDS));

        System.out.println("Waiting for delayed task..." );
        System.out.println(queue.take());
        System.out.println("Waiting for next delayed task...");
        System.out.println(queue.take());
    }

    // ========================================================================
    // DELAYED TASK
    // ========================================================================
    private static class DelayedTask implements Delayed {

        private final String name;
        private final long executeAt;

        DelayedTask(String name,long delay,TimeUnit unit){
            this.name = name;
            this.executeAt = System.nanoTime() + unit.toNanos(delay);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long remaining = executeAt - System.nanoTime();
            return unit.convert(remaining, TimeUnit.NANOSECONDS);
        }

        @Override
        public int compareTo(Delayed other) {
            return Long.compare(this.executeAt, ((DelayedTask) other).executeAt);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        basicOperations();

        boundedVsUnbounded();

        producerConsumer();

        priorityBlockingQueue();

        delayQueue();
    }

}