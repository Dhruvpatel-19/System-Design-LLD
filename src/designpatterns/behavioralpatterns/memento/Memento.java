package designpatterns.behavioralpatterns.memento;

import java.util.Stack;

/* 
 - The Memento Design Pattern is a behavioral pattern used when you want to save and restore 
   an object’s previous state without exposing its internal details.

Main Components:
 1. Originator -> main object whose state we want to save
 2. Memento -> stores the internal state of the Originator
 3. Caretaker -> Keeps track of saved states (history)
*/

//Memento
class Memento {
    private String state;

    public Memento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}

//Originator
class TextEditor {
    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    //Save state
    public Memento save() {
        return new Memento(content);
    }

    // Restore previous state
    public void restore(Memento memento) {
        this.content = memento.getState();
    }
}

//Caretaker
class History {

    //stack or list can be used based on implementation and preferability
    //here stack is used to keep track of latest modifications
    private Stack<Memento> history = new Stack<>();

    //save new state at the top of the stack
    public void save(Memento memento) {
        history.push(memento);
    }

    //undo operations to return previous states
    public Memento undo() {
        if(history.size() <= 1) {
            return null; // nothing to undo
        }

        history.pop(); // remove current state
        return history.peek(); // return previous state
    }
}

//Client
class MementoMain {
    public static void main(String[] args) {
        //initialize originator and caretaker
        TextEditor editor = new TextEditor();
        History history = new History();

        //set and save current state
        editor.setContent("Version 1");
        history.save(editor.save());

        editor.setContent("Version 2");
        history.save(editor.save());

        editor.setContent("Version 3");
        history.save(editor.save());


        System.out.println(editor.getContent()); // Version 3

        //undo to go back to previous version
        editor.restore(history.undo());
        System.out.println(editor.getContent()); // Version 2

        editor.restore(history.undo());
        System.out.println(editor.getContent()); // Version 1
    }
}
