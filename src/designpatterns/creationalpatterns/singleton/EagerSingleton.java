package designpatterns.creationalpatterns.singleton;

class EagerSingleton {

    //Eage initialization, intance is created when class is loaded
    //thread-safe because class loading is handled by JVM
    //drawback: instance is created even if not used
    private static EagerSingleton instance = new EagerSingleton();

    private EagerSingleton(){}

    public static EagerSingleton getInstance(){
        return instance;
    }

    public void showMessage(){
        System.out.println("Hello World");
    }

}

class EagerSingletonMain{
    public static void main(String[] args) {
        
        EagerSingleton object = EagerSingleton.getInstance();
        object.showMessage();
        
    }
}