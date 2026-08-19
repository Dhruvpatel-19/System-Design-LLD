package concurrency.concurrentcollections;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
============================================================
ConcurrentSkipListMap
============================================================
ConcurrentSkipListMap is a thread-safe, concurrent, sorted map provided by java.util.concurrent.

It is an implementation of ConcurrentNavigableMap and maintains keys in sorted order.

------------------------------------------------------------
WHAT IS ConcurrentSkipListMap?
------------------------------------------------------------
ConcurrentSkipListMap<K, V> is a thread-safe map designed for
concurrent access by multiple threads.

Unlike ConcurrentHashMap, it maintains its keys in sorted order.

Example:
     map.put(30, "C");
     map.put(10, "A");
     map.put(20, "B");

The map maintains:
     10 -> A
     20 -> B
     30 -> C

Important characteristics:
     - Thread-safe
     - Concurrent
     - Sorted by keys
     - Navigable
     - Does not allow null keys
     - Does not allow null values
     - Based on a Skip List
     - Uses CAS-based techniques internally
     - Expected O(log n) time for search/update operations


------------------------------------------------------------
SKIP LIST CONCEPT
------------------------------------------------------------
A Skip List is a probabilistic data structure used to maintain elements in sorted order.

A normal linked list may look like: 10 -> 20 -> 30 -> 40 -> 50 -> 60
Searching for 50 may require traversing many elements.

A Skip List adds multiple levels that allow elements to be skipped during traversal.

Conceptually:
     Level 2:     10 ---------------------- 50
                  |                         |
     Level 1:     10 -------- 30 ----------- 50
                  |           |              |
     Level 0:     10 -- 20 -- 30 -- 40 -- 50 -- 60

Higher levels allow the search to skip over multiple elements.
This provides expected O(log n) complexity for operations such
as get(), put(), and remove().
ConcurrentSkipListMap uses a concurrent Skip List internally.


------------------------------------------------------------
WHY USE ConcurrentSkipListMap?
------------------------------------------------------------
Use ConcurrentSkipListMap when multiple threads need to safely
access a map AND sorted/navigable operations are required.

For example:
     - Find the first key
     - Find the last key
     - Find the next higher key
     - Find the previous lower key
     - Find the nearest key
     - Retrieve a range of keys
     - Iterate over entries in sorted order

Common use cases:
     - Time-based data
     - Scheduling systems
     - Leaderboards
     - Range-based queries
     - Concurrent ordered indexes
     - Time-series data
     - Event processing


------------------------------------------------------------
THREAD-SAFETY
------------------------------------------------------------
ConcurrentSkipListMap is thread-safe.

Multiple threads can safely perform operations such as:
     put()
     get()
     remove()
     containsKey()
     higherKey()
     lowerKey()
     firstKey()
     lastKey()
at the same time.

We do not need to manually synchronize individual map operations.

However, thread-safe individual operations do NOT mean that
multiple operations automatically become one atomic operation.

For example:
     if (!map.containsKey(key)) {
         map.put(key, value);
     }

Another thread could modify the map between containsKey()
and put().

For atomic compound operations, prefer methods such as:
     putIfAbsent()
     compute()
     computeIfAbsent()
     computeIfPresent()
     merge()


------------------------------------------------------------
SORTED MAP BEHAVIOR
------------------------------------------------------------
ConcurrentSkipListMap always maintains its keys in sorted order.

Example:
     map.put(30, "C");
     map.put(10, "A");
     map.put(20, "B");

Iteration produces:
     10 -> A
     20 -> B
     30 -> C

The ordering can be based on:
     1. Natural ordering of keys
     2. A Comparator supplied during construction

Example:
     ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();

Keys are sorted according to their natural ordering.

A custom Comparator can also be supplied if a different ordering is required.


------------------------------------------------------------
NAVIGABLE MAP FEATURES
------------------------------------------------------------
ConcurrentSkipListMap implements ConcurrentNavigableMap.

This provides navigation operations that allow us to find keys relative to another key.

Important methods:
     firstKey()
     lastKey()
     higherKey()
     lowerKey()
     ceilingKey()
     floorKey()
     subMap()


------------------------------------------------------------
IMPORTANT METHODS
------------------------------------------------------------
firstKey(): Returns the smallest key in the map.
Example:
     map = {10=A, 20=B, 30=C}
     map.firstKey()
Returns: 10


lastKey(): Returns the largest key in the map.
Example:
     map = {10=A, 20=B, 30=C}
     map.lastKey()
Returns: 30


higherKey(): Returns the smallest key strictly greater than the specified key.
Example:
     map = {10=A, 20=B, 30=C, 40=D}
     map.higherKey(20)
Returns: 30
Important: higherKey() does NOT include the specified key.


lowerKey(): Returns the largest key strictly smaller than the specified key.
Example:
     map = {10=A, 20=B, 30=C, 40=D}
     map.lowerKey(30)
Returns: 20
Important: lowerKey() does NOT include the specified key.


ceilingKey(): Returns the smallest key greater than or equal to the specified key.
Example:
     map = {10=A, 20=B, 30=C, 40=D}
     map.ceilingKey(25)
Returns: 30
If the key exists: map.ceilingKey(30), then returns: 30
Important: ceilingKey() includes the specified key if it exists.


floorKey(): Returns the largest key less than or equal to the specified key.
Example:
     map = {10=A, 20=B, 30=C, 40=D}
     map.floorKey(25)
Returns: 20
If the key exists: map.floorKey(30), then returns: 30
Important: floorKey() includes the specified key if it exists.


subMap():
Returns a view of the portion of the map whose keys are within the specified range.
Example:
     map = {10=A, 20=B, 30=C, 40=D, 50=E}
     map.subMap(20, 50)
Returns:
     {20=B, 30=C, 40=D}

The default form:
     subMap(fromKey, toKey)
means:
     fromKey -> inclusive
     toKey   -> exclusive


------------------------------------------------------------
HIGHER vs CEILING
------------------------------------------------------------
higherKey(key): Returns a key strictly greater than key.
ceilingKey(key): Returns a key greater than or equal to key.
Example:
     map = {10, 20, 30}
     higherKey(20)  -> 30
     ceilingKey(20) -> 20


------------------------------------------------------------
LOWER vs FLOOR
------------------------------------------------------------
lowerKey(key): Returns a key strictly smaller than key.

floorKey(key): Returns a key smaller than or equal to key.

Example:
     map = {10, 20, 30}
     lowerKey(20) -> 10
     floorKey(20)  -> 20


------------------------------------------------------------
CONCURRENT OPERATIONS
------------------------------------------------------------
Multiple threads can access and modify a ConcurrentSkipListMap concurrently.

For example:
     Thread 1 -> put()
     Thread 2 -> get()
     Thread 3 -> remove()

These operations can safely happen concurrently.
The map does not require us to synchronize the entire map manually.
Its iterators are weakly consistent.

This means:
     - They do not throw ConcurrentModificationException simply because another thread modifies the map.
     - They may reflect some modifications made after the iterator was created.


------------------------------------------------------------
IMPORTANT METHODS
------------------------------------------------------------
put(): Adds or updates a key-value pair.
get(): Returns the value associated with a key.
remove(): Removes a key-value pair.
containsKey(): Checks whether a key exists.
putIfAbsent(): Adds the key only if it is not already present.
compute(): Computes a new value for a key.
computeIfAbsent(): Computes a value only when the key is absent.
computeIfPresent(): Computes a new value only when the key is present.
merge(): Combines an existing value with a new value.
firstKey(): Returns the smallest key.
lastKey(): Returns the largest key.
higherKey(): Returns the smallest key strictly greater than the given key.
lowerKey(): Returns the largest key strictly smaller than the given key.
ceilingKey(): Returns the smallest key greater than or equal to the given key.
floorKey(): Returns the largest key less than or equal to the given key.
subMap(): Returns a range view of the map.


------------------------------------------------------------
USE CASES
------------------------------------------------------------
ConcurrentSkipListMap is useful when we need:
     Thread safety
          +
     Sorted keys
          +
     Navigation or range queries

Common examples:
     1. Scheduling systems
        timestamp -> task

     2. Time-series data
        timestamp -> event

     3. Leaderboards
        score -> players

     4. Concurrent ordered indexes

     5. Range-based queries

     6. Event processing

Example:
     timestamp -> event
     1000 -> Event A
     2000 -> Event B
     3000 -> Event C
     4000 -> Event D

We can efficiently find:
     - First event
     - Last event
     - Event after a specific timestamp
     - Event before a specific timestamp
     - Events within a timestamp range


------------------------------------------------------------
ConcurrentHashMap vs ConcurrentSkipListMap
------------------------------------------------------------

ConcurrentHashMap:
     - Thread-safe
     - No ordering
     - Hash table based
     - Expected O(1) basic lookup/update
     - Good for fast key-value access
     - Does not provide NavigableMap operations

ConcurrentSkipListMap:
     - Thread-safe
     - Sorted keys
     - Skip List based
     - Expected O(log n) basic lookup/update
     - Supports navigation
     - Supports range queries
     - Implements ConcurrentNavigableMap

Use ConcurrentHashMap when:
     - You only need fast concurrent key-value access.
     - Ordering is not important.

Use ConcurrentSkipListMap when:
     - You need concurrent access.
     - You need sorted keys.
     - You need navigation operations.
     - You need range queries.


------------------------------------------------------------
ConcurrentSkipListMap vs TreeMap
------------------------------------------------------------
TreeMap:
     - Not thread-safe
     - Sorted
     - Based on Red-Black Tree
     - Expected O(log n) basic operations
     - Suitable for single-threaded usage or externally
       synchronized access

ConcurrentSkipListMap:
     - Thread-safe
     - Sorted
     - Based on Skip List
     - Expected O(log n) basic operations
     - Designed for concurrent access


------------------------------------------------------------
WHEN TO USE
------------------------------------------------------------
Use ConcurrentSkipListMap when:
     1. Multiple threads access the map.
     2. Keys need to remain sorted.
     3. You need navigation operations such as:
            higherKey()
            lowerKey()
            ceilingKey()
            floorKey()
     4. You need range operations such as:
            subMap()
            headMap()
            tailMap()
     5. You need weakly consistent iteration during concurrent modifications.


------------------------------------------------------------
WHEN NOT TO USE
------------------------------------------------------------
Do NOT use ConcurrentSkipListMap when:
     1. You do not need sorted keys. Use ConcurrentHashMap instead.
     2. You do not need concurrency. Use TreeMap instead if sorted ordering is required.
     3. You need blocking operations. Use an appropriate BlockingQueue instead.
     4. You need the fastest possible basic key-value lookup and ordering is not required.
        ConcurrentHashMap is generally more appropriate.


------------------------------------------------------------
INTERVIEW NOTES
------------------------------------------------------------
Q: What is ConcurrentSkipListMap?
It is a thread-safe, concurrent, sorted implementation of NavigableMap based on a Skip List.

Q: Is ConcurrentSkipListMap thread-safe?
Yes. It is designed for concurrent access by multiple threads.

Q: Is ConcurrentSkipListMap sorted?
Yes. Keys are maintained according to their natural ordering or the supplied Comparator.

Q: Does ConcurrentSkipListMap allow null keys?
No.

Q: Does ConcurrentSkipListMap allow null values?
No.

Q: What data structure does ConcurrentSkipListMap use?
A concurrent Skip List.

Q: What is the expected complexity of get(), put(), and remove()?
Expected O(log n)

Q: Why use ConcurrentSkipListMap instead of ConcurrentHashMap?
When sorted ordering, navigation operations, or range queries are required.

Q: What is the difference between higherKey() and ceilingKey()?
higherKey(20) -> strictly greater than 20
ceilingKey(20) -> greater than or equal to 20

Q: What is the difference between lowerKey() and floorKey()?
lowerKey(20) -> strictly less than 20
floorKey(20) -> less than or equal to 20

Q: Are ConcurrentSkipListMap iterators fail-fast?
No. Its iterators are weakly consistent.

Q: What interface does ConcurrentSkipListMap implement?
ConcurrentSkipListMap implements ConcurrentNavigableMap, which extends ConcurrentMap and NavigableMap.


Q: What is the difference between ConcurrentSkipListMap and TreeMap?
TreeMap
     -> Not thread-safe
     -> Red-Black Tree
     -> Sorted

ConcurrentSkipListMap
     -> Thread-safe
     -> Skip List
     -> Sorted
     -> Designed for concurrent access


------------------------------------------------------------
SUMMARY
------------------------------------------------------------
ConcurrentSkipListMap
Thread-safe      -> YES
Concurrent       -> YES
Sorted           -> YES
Navigable        -> YES
Blocking         -> NO
Null keys        -> NO
Null values      -> NO
Expected lookup  -> O(log n)
Underlying DS    -> Skip List


Important methods:
     firstKey()     -> smallest key
     lastKey()      -> largest key
     higherKey()    -> strictly greater
     lowerKey()     -> strictly smaller
     ceilingKey()   -> greater than or equal
     floorKey()     -> less than or equal
     subMap()       -> range of keys


KEY IDEA: Thread-safe + Sorted + Navigable + Concurrent
============================================================
*/

class ConcurrentSkipListMapDemo {

    public static void main(String[] args) {

        System.out.println("\n========== Basic Map Operations ==========");
        ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();

        map.put(30, "C");
        map.put(10, "A");
        map.put(20, "B");
        map.put(50, "E");
        map.put(40, "D");

        // Keys are automatically maintained in sorted order.
        System.out.println("Map: " + map);

        System.out.println("First key: " + map.firstKey());
        System.out.println("Last key: " + map.lastKey());

        System.out.println("Higher than 20: " + map.higherKey(20));
        System.out.println("Lower than 20: " + map.lowerKey(20));

        System.out.println("Ceiling of 25: " + map.ceilingKey(25));
        System.out.println("Floor of 25: " + map.floorKey(25));


        System.out.println("\n========== Range Operations ==========");
        Map<Integer, String> subMap = map.subMap(20, 50);
        System.out.println("subMap(20, 50): " + subMap);


        System.out.println("\n========== Multi-threaded Usage ==========");
        ConcurrentSkipListMap<Integer, String> concurrentMap = new ConcurrentSkipListMap<>();

        Thread writer1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                concurrentMap.put(i, "Thread-1-" + i);
                System.out.println("Thread-1 added: " + i);
            }
        });


        Thread writer2 = new Thread(() -> {
            for (int i = 6; i <= 10; i++) {
                concurrentMap.put(i, "Thread-2-" + i);
                System.out.println("Thread-2 added: " + i);
            }
        });


        Thread reader = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Reader sees: " + concurrentMap);
            }
        });


        writer1.start();
        writer2.start();
        reader.start();

        try {
            writer1.join();
            writer2.join();
            reader.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final map: " + concurrentMap);
    }
}