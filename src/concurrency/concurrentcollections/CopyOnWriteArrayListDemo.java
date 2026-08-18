package concurrency.concurrentcollections;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
============================================================
CopyOnWriteArrayList
============================================================
CopyOnWriteArrayList is a thread-safe implementation of List provided by java.util.concurrent.

It is designed primarily for scenarios where reads and
iterations happen much more frequently than modifications.

------------------------------------------------------------
WHAT IS CopyOnWriteArrayList?
------------------------------------------------------------
CopyOnWriteArrayList<E> is a thread-safe List implementation
that creates a new copy of the underlying array whenever
the list is modified.

The existing array is never modified directly.

Example:
Initial array:
    [A, B, C]

When add("D") is performed:

New array:
    [A, B, C, D]

The list then starts referring to the new array.
Readers that were already using the old array can continue reading it safely.
This is the core idea behind Copy-On-Write.

Important characteristics:
     - Thread-safe
     - Copy-on-write
     - Good for read-heavy workloads
     - Poor for write-heavy workloads
     - Allows concurrent reads and writes
     - Iterators provide snapshot semantics
     - Does not throw ConcurrentModificationException during normal concurrent modification
     - Allows null elements
     - Maintains insertion order
     - Implements the List interface


------------------------------------------------------------
WHY COPY-ON-WRITE?
------------------------------------------------------------
The main problem with concurrent collections is balancing
safe access with good performance.

If many threads are reading a collection, continuously
locking readers can create unnecessary contention.

CopyOnWriteArrayList solves this by allowing readers to
access the current array without requiring a lock.

When a write occurs, the entire underlying array is copied
and the modification is performed on the new array.

Therefore:
     READ
         -> Read existing array
         -> No copying

     WRITE
         -> Create a new array
         -> Copy existing elements
         -> Apply modification
         -> Replace the underlying array

This means writes are expensive, but reads are very efficient.
The design intentionally favors reads over writes.


------------------------------------------------------------
THREAD SAFETY
------------------------------------------------------------
CopyOnWriteArrayList is thread-safe.

Multiple threads can safely perform operations such as:
     add()
     remove()
     set()
     get()
     contains()
     iterator()
at the same time.

Individual operations are thread-safe and do not require
external synchronization.

However, thread-safe individual operations do NOT mean that
a sequence of multiple operations automatically becomes
atomic.

For example:
     if (!list.isEmpty()) {
         list.remove(0);
     }

Another thread could modify the list between isEmpty()
and remove(0).
Therefore, thread safety of the collection does not
automatically make compound operations atomic.


------------------------------------------------------------
READ VS WRITE BEHAVIOR
------------------------------------------------------------
The most important characteristic of CopyOnWriteArrayList
is the difference between reads and writes.

READ OPERATIONS
Examples:
     get()
     contains()
     iterator()
     forEach()
These operations can read the existing array directly.
No new array needs to be created.

Therefore, reads are relatively efficient and do not
normally block other readers.

WRITE OPERATIONS
Examples:
     add()
     addAll()
     remove()
     removeAll()
     set()
     clear()
These operations create a new copy of the underlying array.
Therefore, writes are relatively expensive.

If the list contains N elements, a modification may require
copying N elements into a new array.

This becomes especially expensive when:
     - The list is large
     - Writes are frequent


------------------------------------------------------------
WHY IS IT GOOD FOR READ-HEAVY WORKLOADS?
------------------------------------------------------------
CopyOnWriteArrayList is most appropriate when:
     Reads >>> Writes

For example:
     10,000 reads
     10 writes
can be a good workload.

Readers can continue reading while a writer creates a new
version of the underlying array.

Typical examples include:
     - Event listeners
     - Observer lists
     - Registered callbacks
     - Application configuration
     - Plugin registrations
     - Routing information
     - Listener/handler collections

For example, an application may notify thousands of event
listeners while listeners are added or removed only rarely.
That is a good use case for CopyOnWriteArrayList.


------------------------------------------------------------
WHY IS IT BAD FOR WRITE-HEAVY WORKLOADS?
------------------------------------------------------------
Every modification may require the entire underlying array
to be copied.

For example, imagine a list containing: 1,000,000 elements

If many threads repeatedly add and remove elements, a large
amount of copying can occur.

This can result in:
     - High CPU usage
     - Additional memory allocation
     - Garbage collection pressure
     - Poor performance

Therefore, CopyOnWriteArrayList should generally NOT be used
for write-heavy workloads.

The key trade-off is:
     Expensive writes
           +
     Efficient reads
           =
     Good read-heavy collection


------------------------------------------------------------
ITERATOR BEHAVIOR
------------------------------------------------------------
CopyOnWriteArrayList iterators behave differently from
iterators of ArrayList.

An iterator works with the array that existed when the
iterator was created.

If the list is modified after the iterator is created,
the iterator continues using the previous array.

For example:
     List:
         [A, B, C]

     Create iterator
     Add D

     Current list:
         [A, B, C, D]

     Iterator:
         [A, B, C]

The iterator does not see D.

This means that concurrent modification does not cause
ConcurrentModificationException during normal iteration.


------------------------------------------------------------
SNAPSHOT SEMANTICS
------------------------------------------------------------
The iterator of CopyOnWriteArrayList provides snapshot
semantics.

The iterator effectively sees a stable snapshot of the list
at the time the iterator was created.

Example:
     Initial list:
         [A, B, C]

     Create iterator
     Add D
     Remove A

     Current list:
         [B, C, D]

     Existing iterator:
         [A, B, C]

The iterator continues seeing:
     [A, B, C]

while a newly created iterator would see:
     [B, C, D]

This provides stable iteration without requiring the caller
to manually synchronize the list.

The trade-off is that an iterator may see stale data.


------------------------------------------------------------
IMPORTANT METHODS
------------------------------------------------------------
CopyOnWriteArrayList provides the normal List API.

Common methods include:
add(): Adds an element.
addAll(): Adds multiple elements.
get(): Returns an element at an index.
set(): Replaces an element.
remove(): Removes an element.
contains(): Checks whether an element exists.
size(): Returns the number of elements.
clear(): Removes all elements.
iterator(): Returns an iterator with snapshot semantics.
forEach(): Iterates over the elements.


------------------------------------------------------------
MULTI-THREADED USAGE
------------------------------------------------------------
CopyOnWriteArrayList can safely be accessed by multiple
threads.

For example:
     Thread 1 -> reads the list
     Thread 2 -> reads the list
     Thread 3 -> modifies the list

The readers can continue using their current view while
the writer creates a new copy.

This makes it useful when many threads frequently read
shared data and modifications happen occasionally.


------------------------------------------------------------
WHEN TO USE
------------------------------------------------------------
Use CopyOnWriteArrayList when:
     1. Multiple threads access the same List.
     2. Reads are much more frequent than writes.
     3. Iteration happens frequently.
     4. You want safe iteration without ConcurrentModificationException.
     5. Snapshot-style iteration is acceptable.
     6. The collection is relatively small or moderate in size.
     7. Writes are relatively rare.

Common examples:
     - Event listeners
     - Observer collections
     - Callback registrations
     - Plugin lists
     - Configuration data
     - Listener/handler registrations


------------------------------------------------------------
WHEN NOT TO USE
------------------------------------------------------------
Do NOT use CopyOnWriteArrayList when:
     1. Writes happen frequently. Every modification may copy the entire array.
     2. The list is very large and frequently modified. Copying large arrays can become expensive.
     3. You need the iterator to see the latest changes. Iterators use snapshot semantics.
     4. You need low memory allocation overhead. Writes create new arrays.
     5. You don't need concurrency. For simple single-threaded use, ArrayList is usually a better choice.
     6. You need blocking behavior. CopyOnWriteArrayList does not provide blocking operations.


------------------------------------------------------------
COPYONWRITEARRAYLIST VS ARRAYLIST
------------------------------------------------------------
ArrayList:
     - Not thread-safe
     - Fast for normal single-threaded usage
     - Directly modifies its backing array
     - Iterator is fail-fast
     - Concurrent modification can cause
       ConcurrentModificationException

CopyOnWriteArrayList:
     - Thread-safe
     - Designed for concurrent access
     - Copies the backing array on modification
     - Iterator uses snapshot semantics
     - Safe concurrent iteration
     - Better for read-heavy workloads


------------------------------------------------------------
COPYONWRITEARRAYLIST VS SYNCHRONIZEDLIST
------------------------------------------------------------
Collections.synchronizedList() protects operations using
synchronization.

CopyOnWriteArrayList uses a different strategy.

SynchronizedList:
     - Uses synchronization
     - Operations can involve locking
     - Iteration requires external synchronization

CopyOnWriteArrayList:
     - Copies on modification
     - Readers can access the current array
     - Iterator provides snapshot semantics
     - No external synchronization is required for normal
       iteration

The better choice depends on the workload.


------------------------------------------------------------
IMPORTANT NOTES
------------------------------------------------------------
Q: What is CopyOnWriteArrayList?
It is a thread-safe List implementation where modifications
create a new copy of the underlying array.

Q: Why is it called Copy-On-Write?
Because the underlying array is copied whenever the list
is modified.

Q: Is CopyOnWriteArrayList thread-safe?
Yes.

Q: Is CopyOnWriteArrayList good for read-heavy workloads?
Yes. This is its primary use case.

Q: Is CopyOnWriteArrayList good for write-heavy workloads?
No. Every modification can require copying the entire
underlying array.

Q: Can we iterate while another thread modifies the list?
Yes.

Q: Does its iterator throw ConcurrentModificationException?
No, not because of concurrent modifications to the list.
The iterator works with a snapshot of the underlying array.


Q: Does the iterator see modifications made after it
was created?
No. The iterator sees the snapshot that existed when it was
created.

Q: What is the biggest advantage?
Safe and efficient concurrent reads and stable iteration.

Q: What is the biggest disadvantage?
Writes can be expensive because the underlying array is
copied.

Q: What is the key rule for choosing CopyOnWriteArrayList?
Use it when: READS >>> WRITES
Avoid it when: WRITES are frequent


------------------------------------------------------------
SUMMARY
------------------------------------------------------------
CopyOnWriteArrayList

Thread-safe       -> YES
Copy-on-write     -> YES
Read-heavy        -> YES
Write-heavy       -> NO
Snapshot iterator -> YES
Blocking          -> NO
Allows null       -> YES
Maintains order   -> YES

Read:
     Efficient
     No array copy

Write:
     New array created
     Existing elements copied
     Modification applied

Iterator:
     Snapshot semantics
     Does not see later modifications
     No ConcurrentModificationException

Best use cases:
     Event listeners
     Observer lists
     Callbacks
     Plugin registrations
     Rarely changing configuration


KEY IDEA:
     CopyOnWriteArrayList trades expensive writes for safe, efficient reads and snapshot-style iteration.

     READ-HEAVY + WRITE-RARE -> CopyOnWriteArrayList
============================================================
*/

class CopyOnWriteArrayListDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n========== Basic List Operations ==========");
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");

        System.out.println("List: " + list);
        System.out.println("get(0): " + list.get(0));
        System.out.println("contains(B): " + list.contains("B"));
        System.out.println("size(): " + list.size());

        list.set(0, "X");
        System.out.println("After set(): " + list);

        list.remove("B");
        System.out.println("After remove(): " + list);


        System.out.println("\n========== Iterator / Snapshot Behavior ==========");
        CopyOnWriteArrayList<String> snapshotList = new CopyOnWriteArrayList<>(List.of("A", "B", "C"));

        var iterator = snapshotList.iterator();

        // Modification happens after the iterator is created.
        snapshotList.add("D");
        snapshotList.remove("A");

        System.out.println("Current list: " + snapshotList);

        System.out.print("Iterator sees: ");

        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }

        System.out.println();

        // The iterator sees the snapshot [A, B, C],
        // while the current list is [B, C, D].


        System.out.println("\n========== Multi-threaded Usage ==========");

        CopyOnWriteArrayList<String> users = new CopyOnWriteArrayList<>(List.of("Alice", "Bob", "Charlie"));

        Thread reader = new Thread(() -> {

            System.out.println("Reader started");

            for (String user : users) {
                System.out.println("Reader sees: " + user);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            System.out.println("Reader finished");
        });


        Thread writer = new Thread(() -> {

            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            users.add("David");

            System.out.println("Writer added: David");
        });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println("Final list: " + users);


        System.out.println("\n========== Read-heavy Use Case ==========");

        CopyOnWriteArrayList<String> listeners = new CopyOnWriteArrayList<>();

        listeners.add("LoggingListener");
        listeners.add("MetricsListener");
        listeners.add("AuditListener");

        // Imagine this happens thousands of times,
        // while listeners are added or removed only rarely.
        for (String listener : listeners) {
            System.out.println("Notifying: " + listener);
        }
    }
}