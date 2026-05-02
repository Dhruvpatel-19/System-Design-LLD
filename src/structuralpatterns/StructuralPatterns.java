package structuralpatterns;

/*
 * Sturctural Design Patterns (Java)
 *
 * Sturctural patterns deal with deal with object composition.
 * They help with how classes and objects are combined to form larger, flexible, and efficient structures.
 * 
 * ================================
 * 1. Adapter Pattern
 * ================================
 * Converts the interface of one class into another interface expected by the client.
 * Allows incompatible interfaces to work together without modifying existing code.
 * Useful when integrating legacy systems or third-party libraries.
 * 
 *
 *
 * ================================
 * 2. Bridge Pattern
 * ================================
 * Decouples abstraction from implementation so that both can vary independently.
 * Instead of creating many class combinations, it splits hierarchy into:
 * - Abstraction (high-level control)
 * - Implementation (low-level details)
 * Useful when both abstractions and implementations are expected to change frequently.
 *
 * 
 *
 * ================================
 * 3. Composite Pattern
 * ================================
 * Composes objects into tree-like structures to represent part-whole hierarchies.
 * Allows clients to treat individual objects and compositions uniformly through a common interface.
 * Useful for file systems, UI components, and hierarchical structures.
 *
 *
 *
 * ================================
 * 4. Decorator Pattern
 * ================================
 * Attaches additional responsibilities/behaviors to an object dynamically at runtime 
 * without modifying its original code.
 * It provides a flexible alternative to subclassing for extending functionality.
 * Useful when you want to extend functionality in a flexible and reusable way instead of using inheritance.
 *
 *
 *
 * ================================
 * 5. Facade Pattern
 * ================================
 * Provides a simplified, unified interface to a complex subsystem.
 * It hides the complexity of multiple classes and provides an easy-to-use entry point.
 * Useful when a system has many interdependent classes and you want to reduce
 * client-side complexity and coupling.
 * 
 * 
 * 
 * ================================
 * 6. Flyweight Pattern
 * ================================
 * Reduces memory usage by sharing common (intrinsic) data between multiple objects.
 * Instead of creating new objects, it reuses existing ones when possible.
 * Useful when dealing with a large number of similar objects.
 * 
 * 
 * 
 * ================================
 * 7. Proxy Pattern
 * ================================
 * Provides a placeholder or surrogate object to control access to another object.
 * The proxy forwards requests to the real object while adding extra behavior (e.g., logging, security, caching).
 * Useful when you want to add control without modifying the original class.
 * 
 */
public class StructuralPatterns {
    public static void main(String[] args) {
        System.out.println("Refer comments for Structural Design Patterns guide.");
    }
}
