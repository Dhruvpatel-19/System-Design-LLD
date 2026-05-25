package designpatterns.behavioralpatterns.observer;

import java.util.ArrayList;
import java.util.List;

/*
 - The Observer Design Pattern is a behavioral design pattern used when one object 
   changes state and multiple other objects need to be notified automatically.

Main Components:
 1. Subject (Publisher): The object being observed
 2. Observer: all subscribers

 - Provide better flexibility than sending data directly to the Concrete Observers.
 - Subject and observers are independent, also new observers can be added easily.  
*/


//Observer interface
interface Observer {
    //gets called when subject changes
    void update(int temperature);
}

//Subject interface
interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

//Concrete subject
class WeatherStation implements Subject {

    //Observer list
    private List<Observer> observers = new ArrayList<>();

    //State, data
    private int temperature;

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        //each observers gets update
        for (Observer observer : observers) {
            observer.update(temperature);
        }
    }

    public void setTemperature(int temperature) {
        //change in state, notify all observers
        this.temperature = temperature;
        notifyObservers();
    }
}

//Concrete observers
class MobileDisplay implements Observer {

    @Override
    public void update(int temperature) {
        System.out.println("Mobile Display Updated: " + temperature);
    }
}
class TVDisplay implements Observer {

    @Override
    public void update(int temperature) {
        System.out.println("TV Display Updated: " + temperature);
    }
}

//Client code
class ObserverMain {
    public static void main(String[] args) {
        //define subject and observers
        WeatherStation weatherStation = new WeatherStation();

        Observer mobile = new MobileDisplay();
        Observer tv = new TVDisplay();

        weatherStation.addObserver(mobile);
        weatherStation.addObserver(tv);

        //Change in state will notify all observers about it 
        weatherStation.setTemperature(30);

        weatherStation.setTemperature(35);

    }
}
