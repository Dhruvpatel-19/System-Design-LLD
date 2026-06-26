package objectrelationships.composition;


/*
 - Composition is a strong "has-a" relationship where one object owns another object, and the owned object cannot 
   exist independently of the owner.
 - If the parent object is destroyed, the child object is also destroyed.
 - Real life example: Consider a House and its Rooms.
    • A House has Rooms.
    • Rooms belong to a specific House.
    • If the House is demolished, its Rooms no longer exist independently.
 - UML class notiation: the filled diamond (◆) on the owner side represents Composition.
 - For given example it can be presented by House 1 ◆──── 1 Room. If there are multiple rooms, then House 1 ◆──── * Room.
*/

class Room {
    private String roomName;

    public Room(String roomName) {
        this.roomName = roomName;
    }

    public void display() {
        System.out.println("Room: " + roomName);
    }
}

class House {
    private Room room; // Composition

    public House(String roomName) {
        // House creates and owns Room
        this.room = new Room(roomName);
    }

    public void displayHouse() {
        room.display();
    }
}

class CompositionMain {
    public static void main(String[] args) {
        House house = new House("Bedroom");

        house.displayHouse();
    }
}
