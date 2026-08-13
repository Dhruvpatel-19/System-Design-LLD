package concurrency.concurrentcollections;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
============================================================
ConcurrentLinkedQueue
============================================================
ConcurrentLinkedQueue is a thread-safe, non-blocking FIFO queue provided by java.util.concurrent.

------------------------------------------------------------
WHAT IS ConcurrentLinkedQueue?
------------------------------------------------------------
ConcurrentLinkedQueue<E> is an unbounded, thread-safe queue
designed for concurrent access by multiple threads.

It follows FIFO (First-In-First-Out) ordering.

Example: A -> B -> C
poll() returns: A, then B, then C

Important characteristics:
     - Thread-safe
     - Non-blocking
     - FIFO
     - Unbounded
     - Does not allow null elements
     - Uses a lock-free algorithm internally
     - Based on CAS (Compare-And-Swap)

------------------------------------------------------------
WHY USE IT?
------------------------------------------------------------
Use ConcurrentLinkedQueue when multiple threads need to
safely add and remove elements from the same queue without
using explicit synchronization.

Common use cases:
     - Task/event queues
     - Message passing
     - Producer-consumer scenarios
     - Collecting events from multiple threads
     - Concurrent task processing


------------------------------------------------------------
THREAD-SAFETY
------------------------------------------------------------
ConcurrentLinkedQueue is thread-safe.
Multiple threads can safely perform operations such as:
     offer()
     poll()
     peek()
     add()
     remove()
at the same time.
We do not need to manually use synchronized for individual
queue operations.

However, thread-safe individual operations do NOT mean that
multiple operations automatically become one atomic operation.
For example:
     if (!queue.isEmpty()) {
         queue.poll();
     }
Another thread could modify the queue between isEmpty()
and poll().

Prefer:
     Integer value = queue.poll();
     if (value != null) {
         // process value
     }


------------------------------------------------------------
NON-BLOCKING OPERATIONS
------------------------------------------------------------
ConcurrentLinkedQueue does not block when the queue is empty.

For example: queue.poll();
If the queue is empty, poll() immediately returns null. It does NOT wait for another thread to add an element.

This is different from BlockingQueue:
     blockingQueue.take();
take() waits until an element becomes available.


------------------------------------------------------------
IMPORTANT METHODS
------------------------------------------------------------
offer(): Adds an element to the queue.
     queue.offer("A");
Returns true when the element is successfully added.


poll(): Removes and returns the head element.
     queue.poll();
If the queue is empty: returns null


peek(): Returns the head element without removing it.
     queue.peek();
If the queue is empty: returns null


add(): Adds an element to the queue.
     queue.add("A");
For ConcurrentLinkedQueue, add() behaves like offer().


remove(): Removes and returns the head element.
     queue.remove();
If the queue is empty, remove() throws NoSuchElementException.


------------------------------------------------------------
offer() vs add()
------------------------------------------------------------
Both are used to add elements.
     offer() -> returns false if insertion cannot be performed
     add()   -> throws exception if insertion cannot be performed
Since ConcurrentLinkedQueue is unbounded, both normally succeed.


------------------------------------------------------------
poll() vs remove()
------------------------------------------------------------
     poll()
         -> removes and returns head
         -> returns null if empty

     remove()
         -> removes and returns head
         -> throws NoSuchElementException if empty


------------------------------------------------------------
peek() vs poll()
------------------------------------------------------------
     peek()
         -> returns head
         -> does NOT remove it

     poll()
         -> returns head
         -> removes it


------------------------------------------------------------
WHEN TO USE
------------------------------------------------------------
Use ConcurrentLinkedQueue when:
     1. Multiple threads access the queue.
     2. FIFO ordering is required.
     3. Non-blocking operations are preferred.
     4. An unbounded queue is acceptable.
     5. You do not want threads to wait.


------------------------------------------------------------
WHEN NOT TO USE
------------------------------------------------------------
Do NOT use ConcurrentLinkedQueue when:
     1. You need blocking operations such as:
            put()
            take()
        Use BlockingQueue instead.

     2. You need a bounded queue.
        Consider:
            ArrayBlockingQueue
            LinkedBlockingQueue

     3. You need producer-consumer backpressure.
        A BlockingQueue is usually more appropriate.

     4. You frequently need the queue size.
        size() may require traversal and is not intended
        to be used as a cheap synchronization mechanism.

     5. You don't need concurrency.
        For simple single-threaded use, ArrayDeque is usually
        a better choice.

------------------------------------------------------------
Questions
------------------------------------------------------------
Q: Is ConcurrentLinkedQueue thread-safe?
Yes. Multiple threads can safely operate on it concurrently.

Q: Is it blocking?
No. It is a non-blocking queue.

Q: Is it bounded?
No. It is unbounded.

Q: Does it allow null?
No. null elements are not allowed.

Q: What ordering does it provide?
FIFO (First-In-First-Out).

Q: What does poll() return when the queue is empty?
null.

Q: What does remove() do when the queue is empty?
It throws NoSuchElementException.

Q: What does peek() do?
It returns the head without removing it.

Q: What is the difference between ConcurrentLinkedQueue and BlockingQueue?
ConcurrentLinkedQueue:
     - Non-blocking
     - Unbounded
     - poll() returns null when empty
BlockingQueue:
     - Supports blocking operations
     - Can be bounded
     - take() waits for an element
     - put() can wait for capacity

Q: What does ConcurrentLinkedQueue use internally?
It uses a lock-free algorithm based on CAS (Compare-And-Swap).


------------------------------------------------------------
Summary
------------------------------------------------------------
ConcurrentLinkedQueue
Thread-safe  -> YES
Blocking     -> NO
Lock-free    -> YES
FIFO         -> YES
Bounded      -> NO
Null allowed -> NO


Methods:
     offer() -> add
     add()   -> add
     poll()  -> remove + return, null if empty
     remove()-> remove + return, exception if empty
     peek()  -> return without removing


KEY IDEA: Thread-safe + FIFO + Non-blocking + Unbounded
============================================================
*/

class ConcurrentLinkedQueueDemo {

    public static void main(String[] args) {

        System.out.println("\n========== Basic Queue Operations ==========");

        ConcurrentLinkedQueue<String> queue =new ConcurrentLinkedQueue<>();

        //For simple single-threaded queue usage, a simpler implementation such as LinkedList or ArrayDeque can be used
        //Queue<String> queue = new LinkedList<>();

        queue.offer("A");
        queue.offer("B");
        queue.offer("C");

        System.out.println("Queue: " + queue);
        System.out.println("peek(): " + queue.peek());
        System.out.println("poll(): " + queue.poll());

        queue.add("D");
        System.out.println("After add(): " + queue);
        System.out.println("remove(): " + queue.remove());
        

        System.out.println("\n========== Multi-threaded usage ==========");

        //If we use a simple queue in multi-threaded operations, it can lead to problems
        //such as lost elements, unexpected results, race conditions, inconsistent internal state, and exceptions.
        //Therefore, ConcurrentLinkedQueue should be used when multiple threads need to access the same queue concurrently.
        ConcurrentLinkedQueue<Integer> concurrentQueue = new ConcurrentLinkedQueue<>();

        Thread producer = new Thread(() -> {

            for (int i = 1; i <= 5; i++) {
                concurrentQueue.offer(i);
                System.out.println("Produced: " + i);
            }
        });


        Thread consumer = new Thread(() -> {

            int consumed = 0;

            while (consumed < 5) {
                Integer value = concurrentQueue.poll();
                if (value != null) {
                    System.out.println("Consumed: " + value);
                    consumed++;
                }
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Remaining elements: " + concurrentQueue);
    }
}