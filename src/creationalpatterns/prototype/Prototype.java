package creationalpatterns.prototype;

//Prototype pattern is used when creating object is expensive like database calls, complex computations or initialization
//and you need many similar objects with small differences

//Prototype interface
/*interface Prototype{
     Prototype clone();
}*/

//using generics, so no typecasting needed after cloning
interface Prototype<T>{
    T clone();
}

class Address{
    String city;
}

class Employee implements Prototype<Employee>{
    String name;
    Address address;

    Employee(String name, Address address){
        this.name = name;
        this.address = address;
    }
    
    //Shallow copy
    /*@Override
    public Prototype clone(){
        return new Employee(this.name, this.address); //same reference for Address(mutable object)
    }*/

    //Deep copy
    //only needed for mutable objects
    @Override
    public Employee clone(){
        Address newAddress = new Address(); //new instance created for Address
        newAddress.city = this.address.city;
        return new Employee(this.name, newAddress); //new reference for Address
    }

    public void show(){
        System.out.println(name+", "+address.city);
    }
}


class PrototypeMain {
    public static void main(String[] args) {
        Address address = new Address();
        address.city = "Mumbai";

        Employee e1 = new Employee("ABC", address);
        //creating same object using clone method
        Employee e2 = e1.clone();
        //changing required fields
        e2.name = "XYZ";

        e1.show();
        e2.show();
    }
}
