package creationalpatterns.singleton;

class DoubleCheckSingleton {

    //volatile for any thread accesing variable to see the most recent value
    //it doesn't stop multiple thread from accessing the variable at the exact same time
    private static volatile DoubleCheckSingleton obj = null;
    
    private DoubleCheckSingleton() {}

    public static DoubleCheckSingleton getInstance()
    {
        //first check, no locking
        //most will skip this once object is created
        if (obj == null) { 

            //lock on class object so only one thread enters this block
            synchronized (DoubleCheckSingleton.class)
            {
                //second check as multiple threads can pass first check simultaneously
                //create object if null
                if (obj == null)
                    obj = new DoubleCheckSingleton();
            }
        }

        return obj;
    }
}

class DoubleCheckSingletonMain{

    public static void main(String[] args) {

        //Thread-safe and efficient than EagerSingleton
        
        /* Flow-chart
        1. Thread A:
            Enters if (obj == null)
            Acquires lock on DoubleCheckSingleton.class
            Creates object

        2. Thread B:
            Also reaches if (obj == null)
            Tries to enter synchronized block (blocked)
            Waits until Thread A finishes, and Second Check will prevent another object creation
        */
    
        DoubleCheckSingleton thread1 = DoubleCheckSingleton.getInstance();
        DoubleCheckSingleton thread2 = DoubleCheckSingleton.getInstance();

        System.out.println("thread1 == thread2 : "+(thread1 == thread2));
    }
}
