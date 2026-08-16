package concurrency.concurrentcollections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
============================================================
ConcurrentHashMap
============================================================
ConcurrentHashMap is a thread-safe Map implementation provided by java.util.concurrent.

It is designed for concurrent access by multiple threads and
provides better concurrency than synchronizing an entire Map.

------------------------------------------------------------
WHAT IS ConcurrentHashMap?
------------------------------------------------------------
ConcurrentHashMap<K, V> is a thread-safe hash-based Map that
allows multiple threads to read and modify the map concurrently.

It provides:
     - Thread-safety
     - High concurrency
     - Concurrent access
     - Atomic compound operations
     - Weakly consistent iteration
     - No null keys
     - No null values

Example:
HashMap
    ↓
Not thread-safe

ConcurrentHashMap
    ↓
Thread-safe
    ↓
High concurrency
    ↓
Atomic compound operations


------------------------------------------------------------
WHY HashMap IS NOT THREAD-SAFE
------------------------------------------------------------
HashMap is not designed for concurrent modification by multiple threads.

For example:
Thread 1:
    map.put("A", 1);

Thread 2:
    map.put("B", 2);

If multiple threads modify a normal HashMap concurrently, race conditions and inconsistent results can occur.
Therefore, HashMap should not be directly used when multiple threads are modifying the same map concurrently.

Possible alternatives:
     - Collections.synchronizedMap()
     - ConcurrentHashMap

ConcurrentHashMap is generally preferred when high concurrent access is required.


------------------------------------------------------------
THREAD-SAFETY
------------------------------------------------------------
ConcurrentHashMap is thread-safe.

Multiple threads can safely perform individual operations such as:
     put()
     get()
     remove()
     putIfAbsent()
     compute()
     computeIfAbsent()
     computeIfPresent()
     merge()
     replace()

at the same time. We do not need to manually synchronize individual Map operations.

However, thread-safe individual operations do NOT mean that multiple separate operations
automatically become one atomic operation.

For example:
     if (!map.containsKey(key)) {
         map.put(key, value);
     }

This is NOT an atomic compound operation.
Another thread could insert the same key between containsKey() and put().

Prefer:
     map.putIfAbsent(key, value);

when the operation means:
     "Insert this value only if the key does not already exist."


------------------------------------------------------------
CONCURRENT ACCESS
------------------------------------------------------------
ConcurrentHashMap allows multiple threads to access the map concurrently.

For example:
Thread 1:
     map.put("A", 1);

Thread 2:
     map.get("B");

Thread 3:
     map.remove("C");

These operations can safely happen concurrently.

ConcurrentHashMap is designed to provide high concurrency rather than simply locking the entire Map for every operation.

The exact internal implementation details have evolved between Java versions, but the important idea is:
     ConcurrentHashMap
          ↓
     Thread-safe
          ↓
     High concurrent access
          ↓
     Fine-grained coordination


------------------------------------------------------------
IMPORTANT METHODS
------------------------------------------------------------
put(): Adds or replaces a value for a key.
     map.put("apple", 3);
If "apple" already exists, its value is replaced.


get(): Returns the value associated with a key.
     map.get("apple");
If the key does not exist, get() returns null.
Since ConcurrentHashMap does not allow null values, null returned from get() means that the key is absent.


remove(): Removes the mapping associated with a key.
     map.remove("apple");
Returns the previous value associated with the key.
If the key does not exist, returns null.


putIfAbsent(): Adds the mapping only if the key does not already exist.
     map.putIfAbsent("apple", 3);
If "apple" does not exist: apple -> 3. If "apple" already exists: existing value remains unchanged.
This is an atomic operation.
Instead of:
     if (!map.containsKey(key)) {
         map.put(key, value);
     }
use:
     map.putIfAbsent(key, value);


compute(): Computes a new value for a key.
     map.compute("apple", (key, value) -> value + 1);
The remapping function is applied atomically.
Useful when the new value depends on the existing value.
Example: apple -> 3
After: map.compute("apple", (key, value) -> value + 1);
Result: apple -> 4


computeIfAbsent(): Computes and inserts a value only if the key is absent.
     map.computeIfAbsent(
         key,
         k -> createValue(k)
     );
If the key already exists, the function is not executed.

Example:
     map.computeIfAbsent(
         "apple",
         k -> 10
     );
If apple does not exist: apple -> 10. If apple already exists: existing value remains unchanged.

This is useful for lazily creating values.
Example:
     map.computeIfAbsent(
         "users",
         k -> new ArrayList<>()
     );


computeIfPresent():
Computes a new value only if the key already exists.
     map.computeIfPresent(
         "apple",
         (key, value) -> value + 1
     );
If apple exists: its value is updated. If apple does not exist: nothing happens.


merge(): merge() is one of the most useful ConcurrentHashMap methods.
Example:
     map.merge(word, 1, Integer::sum);

If the key does not exist: apple -> 1
If the key already exists: apple -> 2

then another:
     map.merge("apple", 1, Integer::sum);
produces:
     apple -> 3

The general idea is:
     merge(key, value, remappingFunction)

If key is absent: insert value
If key is present: combine existing value and new value using the function.

This makes merge() extremely useful for frequency counters.
Example:
     apple  -> 3
     banana -> 5
     orange -> 2
This can be built using: map.merge(word, 1, Integer::sum);


replace(): Replaces the value associated with an existing key.
     map.replace("apple", 10);
If apple exists: apple -> 10
If apple does not exist: nothing happens.
There is also a conditional version: map.replace("apple", 3, 10);
This replaces 3 with 10 only if the current value is 3.


------------------------------------------------------------
ATOMIC COMPOUND OPERATIONS
------------------------------------------------------------
ConcurrentHashMap provides several atomic compound operations. These are especially important in concurrent programming.

Example of a non-atomic approach:
     Integer count = map.get("apple");

     if (count == null) {
         map.put("apple", 1);
     } else {
         map.put("apple", count + 1);
     }

Multiple threads can interfere between get() and put().
Instead, use: map.merge("apple", 1, Integer::sum);

Similarly:
     map.putIfAbsent(key, value);
     map.compute(key, ...);
     map.computeIfAbsent(key, ...);
     map.computeIfPresent(key, ...);
     map.merge(key, value, ...);

These methods perform the corresponding compound operation atomically for the specified key.

IMPORTANT:
Thread-safe individual operations
        !=
Atomic sequence of separate operations


------------------------------------------------------------
putIfAbsent() vs containsKey() + put()
------------------------------------------------------------
NOT ATOMIC:
     if (!map.containsKey(key)) {
         map.put(key, value);
     }

ATOMIC:
     map.putIfAbsent(key, value);

This is one of the most common examples used to explain why ConcurrentHashMap provides atomic compound operations.


------------------------------------------------------------
computeIfAbsent() EXAMPLE
------------------------------------------------------------
Suppose we want to create a value only when a key does not exist.

Instead of:
     if (!map.containsKey(key)) {
         map.put(key, createValue(key));
     }

Use:
     map.computeIfAbsent(
         key,
         k -> createValue(k)
     );

This guarantees that the computation and insertion are performed atomically for the key.


------------------------------------------------------------
merge() FREQUENCY COUNTER
------------------------------------------------------------
A very common use case is counting frequencies.

Suppose we have:
     apple
     banana
     apple
     orange
     banana
     apple
     banana
     orange
     banana
     banana

We want:
     apple  -> 3
     banana -> 5
     orange -> 2

Use:
     map.merge(word, 1, Integer::sum);

For each word:
First occurrence: apple -> 1
Second occurrence: apple -> 2
Third occurrence: apple -> 3

The same approach works safely when multiple threads are processing words concurrently.


------------------------------------------------------------
ITERATION
------------------------------------------------------------
ConcurrentHashMap supports concurrent iteration. Its iterators are weakly consistent.

This means:
     - They do not throw ConcurrentModificationException merely because another thread modifies the map.
     - They may reflect modifications made during iteration.
     - They do not represent a completely fixed snapshot.

Example:
     for (Map.Entry<String, Integer> entry : map.entrySet()) {
         System.out.println(entry.getKey() + " -> " + entry.getValue());
     }

We can also iterate over:
     map.keySet()
     map.values()
     map.entrySet()


------------------------------------------------------------
WEAKLY CONSISTENT ITERATORS
------------------------------------------------------------
ConcurrentHashMap does NOT provide a fail-fast iterator like some ordinary collections.
For example, another thread may modify the map while we iterate. The iterator continues safely.
However, we should NOT assume that the iterator represents an exact snapshot of the Map at a particular point in time.

Think:
     Weakly consistent
          ↓
     Safe during concurrent modification
          ↓
     Not necessarily a fixed snapshot


------------------------------------------------------------
NULL RESTRICTIONS
------------------------------------------------------------
ConcurrentHashMap does NOT allow null keys or null values.

Examples:
     map.put(null, 1);
     map.put("apple", null);
Both result in NullPointerException.

Why?
ConcurrentHashMap uses null to represent the absence of a mapping
in operations such as get().

If null values were allowed, it would become ambiguous whether:
     map.get(key) == null

means: key does not exist
or: key exists with null value
Therefore, null keys and null values are prohibited.


------------------------------------------------------------
BASIC MULTI-THREADED EXAMPLE
------------------------------------------------------------
ConcurrentHashMap can safely be shared between multiple threads.

Example:
     ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

Multiple threads can perform:
     map.put(...)
     map.get(...)
     map.remove(...)
     map.merge(...)
concurrently.

For counters, prefer atomic operations such as merge():
     map.merge("count", 1, Integer::sum);
Instead of:
     map.put("count", map.get("count") + 1);
The second approach is not atomic because another thread can modify the value between get() and put().


------------------------------------------------------------
ConcurrentHashMap vs synchronizedMap
------------------------------------------------------------
Collections.synchronizedMap():
     Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());

This provides thread-safe access by synchronizing operations.
ConcurrentHashMap is specifically designed for high concurrency and generally provides better concurrent performance.

ConcurrentHashMap also provides useful atomic compound operations
such as:
     putIfAbsent()
     compute()
     computeIfAbsent()
     computeIfPresent()
     merge()
     replace()


------------------------------------------------------------
WHEN TO USE
------------------------------------------------------------
Use ConcurrentHashMap when:
     1. Multiple threads need to access the same Map.
     2. Multiple threads may read and modify the Map concurrently.
     3. You need thread-safe Map operations.
     4. High concurrency is important.
     5. You need atomic compound operations.
     6. You need operations such as:
            putIfAbsent()
            computeIfAbsent()
            merge()


------------------------------------------------------------
WHEN NOT TO USE
------------------------------------------------------------
Do NOT use ConcurrentHashMap when:
     1. You do not need concurrency. Use HashMap for simple single-threaded usage.
     2. You need null keys or null values. ConcurrentHashMap does not support them.
     3. You need a completely synchronized snapshot for iteration.
        ConcurrentHashMap iterators are weakly consistent.
     4. You need operations across the entire Map to behave as one atomic transaction.
        ConcurrentHashMap does not make arbitrary sequences of operations atomic.


------------------------------------------------------------
IMPORTANT INTERVIEW QUESTIONS
------------------------------------------------------------
Q: Is ConcurrentHashMap thread-safe?
Yes. It is designed for safe concurrent access by multiple threads.

Q: Is HashMap thread-safe?
No. HashMap is not thread-safe.

Q: Why use ConcurrentHashMap instead of HashMap?
ConcurrentHashMap supports concurrent access and provides thread-safe and atomic compound operations.

Q: Does ConcurrentHashMap allow null keys?
No.

Q: Does ConcurrentHashMap allow null values?
No.

Q: What happens if get() cannot find a key?
It returns null.

Q: Is get() thread-safe?
Yes, individual operations such as get() are thread-safe.

Q: Is this atomic?
     if (!map.containsKey(key)) {
         map.put(key, value);
     }
No. Another thread can modify the map between the two operations.

Q: What should we use instead?
Use: map.putIfAbsent(key, value);

Q: How do you implement a concurrent frequency counter?
Use: map.merge(word, 1, Integer::sum);

Q: What does computeIfAbsent() do?
It computes and inserts a value only when the key is absent.

Q: What does computeIfPresent() do?
It computes a new value only when the key is already present.

Q: What does compute() do?
It computes a new value for a key using the existing key/value mapping.

Q: What does merge() do?
It inserts a value when the key is absent or combines the existing value with the supplied value when the key is present.

Q: What does replace() do?
It replaces the value associated with an existing key.

Q: Are ConcurrentHashMap iterators fail-fast?
No. They are weakly consistent.

Q: Can another thread modify the map while iteration is happening?
Yes. The iterator is designed to tolerate concurrent modification.

Q: Does ConcurrentHashMap lock the entire Map?
The important conceptual point is that it is designed for high concurrency and does not simply synchronize the entire
Map for every operation.

Q: Are multiple operations automatically atomic?
No.
For example:
     map.get(key);
     map.put(key, value);
are two separate operations.
Use appropriate atomic methods such as:
     putIfAbsent()
     compute()
     computeIfAbsent()
     computeIfPresent()
     merge()


------------------------------------------------------------
SUMMARY
------------------------------------------------------------
ConcurrentHashMap
Thread-safe              -> YES
Concurrent access        -> YES
High concurrency         -> YES
Null keys                -> NO
Null values              -> NO
Atomic compound methods  -> YES
Weakly consistent        -> YES
Fail-fast iterator       -> NO

Important methods:
    put() -> add or replace mapping
    get() -> retrieve value
    remove() -> remove mapping
    putIfAbsent() -> insert only if key is absent
    compute() -> compute value using key/current value
    computeIfAbsent() -> compute value only if key is absent
    computeIfPresent() -> compute value only if key is present
    merge() -> insert or combine values
    replace() -> replace an existing value


KEY IDEA:
HashMap
    ↓
Not thread-safe

ConcurrentHashMap
    ↓
Thread-safe
    ↓
High concurrency
    ↓
Atomic compound operations
    ↓
No null keys / values
============================================================
*/

class ConcurrentHashMapDemo {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("\n========== Basic Map Operations ==========");
        
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        // put()
        map.put("apple", 3);
        map.put("banana", 5);
        map.put("orange", 2);

        System.out.println("Map: " + map);

        // get()
        System.out.println("get(apple): " + map.get("apple"));

        // remove()
        map.remove("orange");
        System.out.println("After remove(orange): " + map);


        System.out.println("\n========== Atomic Operations ==========");

        // putIfAbsent()
        map.putIfAbsent("orange", 2);
        map.putIfAbsent("apple", 100);

        System.out.println("After putIfAbsent(): " + map);

        // compute()
        map.compute("apple", (key, value) -> value + 1);

        System.out.println("After compute(): " + map);

        // computeIfAbsent()
        map.computeIfAbsent(
                "grape",
                key -> key.length()
        );

        System.out.println("After computeIfAbsent(): " + map);

        // computeIfPresent()
        map.computeIfPresent(
                "banana",
                (key, value) -> value + 1
        );

        System.out.println("After computeIfPresent(): " + map);

        // merge()
        map.merge("apple", 1, Integer::sum);

        System.out.println("After merge(): " + map);

        // replace()
        map.replace("orange", 10);

        System.out.println("After replace(): " + map);


        System.out.println("\n========== Frequency Counter ==========");

        ConcurrentHashMap<String, Integer> frequencyMap = new ConcurrentHashMap<>();

        String[] words = {
                "apple",
                "banana",
                "apple",
                "orange",
                "banana",
                "apple",
                "banana",
                "orange",
                "banana",
                "banana"
        };

        for (String word : words) {
            // Atomic frequency update
            frequencyMap.merge(word, 1, Integer::sum);
        }

        System.out.println("Frequency: " + frequencyMap);
        /*
        Expected:
        apple  -> 3
        banana -> 5
        orange -> 2
        */


        System.out.println("\n========== Multi-threaded Usage ==========");

        ConcurrentHashMap<String, Integer> concurrentMap = new ConcurrentHashMap<>();

        Runnable task = () -> {
            for (int i = 0; i < 1_000; i++) {
                // Atomic increment
                concurrentMap.merge( "count",1, Integer::sum);
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        Thread thread3 = new Thread(task);
        Thread thread4 = new Thread(task);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }

        System.out.println("Final count: " + concurrentMap.get("count"));
        /*
        Expected:
        4 threads × 1,000 increments = 4,000
        merge() performs the update atomically for the key.
        */

        System.out.println("\n========== Iteration ==========");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }


        System.out.println("\n========== Null Restrictions ==========");
        try {
            map.put(null, 1);

        } catch (NullPointerException e) {
            System.out.println("Null keys are not allowed.");
        }

        try {
            map.put("apple", null);

        } catch (NullPointerException e) {
            System.out.println("Null values are not allowed.");
        }
    }
}
