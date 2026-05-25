package designpatterns.structuralpatterns.flyweight;

import java.util.HashMap;
import java.util.Map;

/*
 - The Flyweight Pattern is a structural design pattern used to reduce memory usage 
   when you need to create a large number of similar objects.
 - It achieves this by sharing common (intrinsic) state between multiple objects instead of 
   storing it repeatedly in each object.

*/

//Flyweight Interface
interface IconFlyweight {
    void display(int x, int y); //extrinsic (unique) state, Changes per usage
}

//Concrete Flyweight
class Icon implements IconFlyweight {
    private char symbol; //intrinsic (shared) state, Does not change

    public Icon(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public void display(int x, int y) {
        System.out.println("Character: " + symbol + " at (" + x + ", " + y + ")");
    }
}

//Flyweight Factory to store similar objects
class IconFactory {

    
    //cache to store already created flyweight objects, composition
    private static Map<Character, IconFlyweight> cache = new HashMap<>();

    public static IconFlyweight getCharacter(char c) {

        //create object only if it doesn't exist in cache
        if (!cache.containsKey(c)) {
            cache.put(c, new Icon(c));
            System.out.println("Creating new object for: " + c);
        }

        //return available stored object
        return cache.get(c);
    }
}

//Client
class FlyweightMain {
    public static void main(String[] args) {
        //create symbol 'A' and use it at coordinates (1, 1)
        IconFlyweight c1 = IconFactory.getCharacter('A');
        c1.display(1, 1);
        //reuse 'A' from cache and put at (2, 2)
        IconFlyweight c2 = IconFactory.getCharacter('A');
        c2.display(2, 2);
        
        //create new symbol 'B' and use at coordinates (3, 3)
        IconFlyweight c3 = IconFactory.getCharacter('B');
        c3.display(3, 3);
    }
}
