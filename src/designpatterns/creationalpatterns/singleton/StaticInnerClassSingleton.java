package designpatterns.creationalpatterns.singleton;

//Bill Pugh Singleton, Best Java-Specific Way
//Lazy Initialization, Thread-safe, no snychronization overhead, cleaner-code
class StaticInnerClassSingleton {
    
    private StaticInnerClassSingleton(){}

    //Inner class is loaded only when getInstance() is called
    //JVM ensures thread-safe class loading
    private static class SingletonInner{
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance(){
        return SingletonInner.INSTANCE;
    }

    public void showMessage(){
        System.out.println("Hello World");
    }
}
class StaticInnerClassSingletonMain{
    public static void main(String[] args) {
        StaticInnerClassSingleton object = StaticInnerClassSingleton.getInstance();
        object.showMessage();
    }
}
