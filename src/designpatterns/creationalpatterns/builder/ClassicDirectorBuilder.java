package designpatterns.creationalpatterns.builder;

//Director controls the object creation, client doesn't need to know order of steps and required fields
//complex workflow, limits client-side flexibility

//Product class
/* //created in SimpleBuilder
class Phone{
    private String os;
    private String processor;
    private double screenSize;
    private int battery;
    private int camera;

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
}*/

//Builder interface
interface Builder {
    Builder setOs(String os);
    Builder setProcessor(String processor);
    Builder setScreenSize(double screenSize);
    Builder setBattery(int battery);
    Builder setCamera(int camera);

    //return the product class
    Phone build();
}

//Concrete Builder class
class Phone_Builder implements Builder{ //change name as same name is used in SimpleBuilder
    private String os;
    private String processor;
    private double screenSize;
    private int battery;
    private int camera;

    @Override
    public Phone_Builder setOs(String os){
        this.os = os;
        return this;
    }

    @Override
    public Phone_Builder setProcessor(String processor){
        this.processor = processor;
        return this;
    }

    @Override
    public Phone_Builder setScreenSize(double screenSize){
        this.screenSize = screenSize;
        return this;
    }

    @Override
    public Phone_Builder setBattery(int battery){
        this.battery = battery;
        return this;
    }

    @Override
    public Phone_Builder setCamera(int camera){
        this.camera = camera;
        return this;
    }

    @Override
    public Phone build(){
        return new Phone(os, processor, screenSize, battery, camera);
    }
}

//Director
class PhoneDirector {

    public Phone buildBasicPhone(Builder builder){
        return builder
                .setOs("Android")
                .setBattery(4000)
                .build();
    }

    public Phone buildGamingPhone(Builder builder){
        return builder
                .setOs("Android")
                .setProcessor("Snapdragon 8 Gen")
                .setBattery(6000)
                .setCamera(64)
                .build();
    }
}

//Client
class ClassicDirectorBuilderMain {
    public static void main(String[] args) {
        PhoneDirector director = new PhoneDirector();

        // build basic phone
        Phone basic = director.buildBasicPhone(new Phone_Builder());
        System.out.println(basic);

        // build gaming phone
        Phone gaming = director.buildGamingPhone(new Phone_Builder());
        System.out.println(gaming);

    }
}
