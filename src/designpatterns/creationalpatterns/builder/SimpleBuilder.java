package designpatterns.creationalpatterns.builder;

//Builder class to construct phone object step-by-step 
//which gives better control over object creation
//and improves readability by avoiding constructors with too many parameters  
class PhoneBuilder{
    private String os;
    private String processor;
    private double screenSize;
    private int battery;
    private int camera;

    //retunr builder class object after setting any particular feild for method chaining
    public PhoneBuilder setOs(String os){
        this.os = os;
        return this;
    }

    public PhoneBuilder setProcessor(String processor){
        this.processor = processor;
        return this;
    }

    public PhoneBuilder setScreenSize(double screenSize){
        this.screenSize = screenSize;
        return this;
    }

    public PhoneBuilder setBattery(int battery){
        this.battery = battery;
        return this;
    }

    public PhoneBuilder setCamera(int camera){
        this.camera = camera;
        return this;
    }

    //return final contructed object
    public Phone getPhone(){
        return new Phone(os, processor, screenSize, battery, camera);
    }
}

//Product class
class Phone{
    private String os;
    private String processor;
    private double screenSize;
    private int battery;
    private int camera;

    //constructor used by builder
    public Phone(String os, String processor, double screenSize, int battery, int camera){
        this.os = os;
        this.processor = processor;
        this.screenSize = screenSize;
        this.battery = battery;
        this.camera = camera;
    }

    @Override
    public String toString(){
        return "Phone [os = "+os+", processor = "+processor+", screenSize = "+screenSize+", battery = "+battery+", camera = "+camera+"]";
    }
}
class SimpleBuilderMain {
    public static void main(String[] args) {

        //set feilds according to requirements 
        PhoneBuilder builder = new PhoneBuilder().setOs("Android").setBattery(5000);
        //get the object specified
        Phone p = builder.getPhone();

        System.out.println(p);
    }
}
