package creationalpatterns;


/*
 * Creational Design Patterns (Java)
 *
 * These patterns deal with object creation mechanisms.
 *
 * ================================
 * 1. Singleton Pattern
 * ================================
 * Ensures only one instance exists.
 *
 * Follow this learning sequence:
 *
 * 1. SimpleSingleton
 *    - Basic lazy initialization
 *    - Not thread-safe
 *
 * 2. ThreadSafeSingleton
 *    - Uses synchronized method
 *    - Thread-safe but slower
 *
 * 3. DoubleCheckSingleton
 *    - Optimized with volatile + double-check
 *
 * 4. EagerSingleton
 *    - Instance created at class loading
 *
 * 5. StaticInnerClassSingleton (Bill Pugh)
 *    - Lazy + thread-safe without synchronization
 *
 * 6. EnumSingleton
 *    - Most robust (prevents reflection & serialization)
 *
 * Recommended:
 * → Use EnumSingleton in production
 * → Use Bill Pugh when flexibility is needed
 *
 * 
 * 
 * ================================
 * 2. Builder Pattern
 * ================================
 * Used to construct complex objects step-by-step.
 * 
 * Follow this learning sequence:
 *
 * 1. Simple Builder
 *    - Basic step-by-step object creation
 *
 * 2. Builder with Director (Classic GoF)
 *    - Director controls construction process
 *
 * 3. Static Inner Builder (Most Common)
 *    - Builder inside product class
 *    - Cleaner and widely used in real-world Java
 *
 * 
 * 
 * ================================
 * 3. Prototype Pattern
 * ================================
 * Creates new objects by cloning existing ones instead of creating from scratch.
 * Useful when object creation is expensive and many similar objects are needed.
 * 
 * 
 * 
 * ================================
 * 4. Factory Method Pattern
 * ================================
 * Creates objects through a factory method instead of using new directly.
 * Encapsulates object creation logic and promotes loose coupling between client and concrete classes.
 * 
 * 
 * 
 * ================================
 * (Future)
 * ================================
 * - Abstract Factory Pattern
 */
public class CreationalPatterns {

    public static void main(String[] args) {
        System.out.println("Refer comments for Creational Design Patterns guide.");
    }
}
