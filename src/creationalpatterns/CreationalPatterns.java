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
 * ================================
 * (Future)
 * ================================
 * - Builder Pattern
 * - Prototype Pattern
 * - Factory Pattern
 */
public class CreationalPatterns {

    public static void main(String[] args) {
        System.out.println("Refer comments for Creational Design Patterns guide.");
    }
}
