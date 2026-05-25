package designpatterns.creationalpatterns.singleton;

//Lazy initialization, Thread-safe
//Prevents Reflection and Serizalization
enum EnumSingleton {
    INSTANCE; //Single intance

    //method
    public void showMessage(){
        System.out.println("Hello World");
    }
}

class EnumSingletonMain{
    public static void main(String[] args) {
        //access the single instance
        EnumSingleton obj1 = EnumSingleton.INSTANCE;
        EnumSingleton obj2 = EnumSingleton.INSTANCE;

        obj1.showMessage();
        System.out.println("obj1 == obj2 : "+(obj1 == obj2));
    }
}
