package designpatterns.creationalpatterns.singleton;

class SimpleSingleton {

    //static memble to hold single shared instance
    private static SimpleSingleton instance;

    //make constructor private
    private SimpleSingleton(){
    }

    //Lazy initialization, object created when needed
    public static SimpleSingleton getInstance(){
        //check if instance exists or not
        if(instance == null)
            //create one, if it does not exist
            instance = new SimpleSingleton();
        return instance;
    }

    public void showMessage(){
        System.out.println("Hello World");
    }

}

class SimpleSingletonMain{
    public static void main(String[] args) {
        //error
        //SimpleSingleton object = new SimpleSingleton();

        //get the only object available
        SimpleSingleton object = SimpleSingleton.getInstance();
        object.showMessage();
    }
}

