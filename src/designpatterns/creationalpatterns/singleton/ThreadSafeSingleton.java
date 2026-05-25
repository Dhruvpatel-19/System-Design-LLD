package designpatterns.creationalpatterns.singleton;

class SingletonObject {

    private static SingletonObject instance;

    private SingletonObject(){}
    
    //only one thread can access
    //slightly slower but threadsafe
    public static synchronized SingletonObject getInstance() {
        if (instance == null)
            instance = new SingletonObject();
        return instance;
    }

    public void showMessage(){
        System.out.println("Hello World");
    }
}

class SingletonObjectMain{
    public static void main(String[] args) {
      
        //without synchronization, both threads may get different objects if method called at the same time 
        //synchronization make sure only one instance is created
        SingletonObject thread1 = SingletonObject.getInstance();
        SingletonObject thread2 = SingletonObject.getInstance();

        thread1.showMessage();
        thread2.showMessage();
    }
}

