package behavioralpatterns.iterator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/* 
 - The Iterator design pattern is a behavioral design pattern that provides a way to access 
   the elements of an aggregate object (like a list) sequentially without exposing its underlying representation.
 - When you press next or previous on a remote, you don’t know how channels are stored internally. 
   you just iterate through them one by one.
*/

//Iterator interface
interface Iterator<T> {
    boolean hasNext();
    T next();
}

//Aggregate interface
interface Aggregate<T> {
    Iterator<T> createIterator();
}

//Employee class
class Employee {
    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }
}

//Concrete collection
class Company implements Aggregate<Employee>{

    List<Employee> employees;

    public Company(List<Employee> employees) {
        this.employees = employees;
    }
 
    @Override
    public Iterator<Employee> createIterator(){
        return new EmployeeIterator();
    }

    //better to define iterator class inside
    //encapsulations, direct access to class data
    private class EmployeeIterator implements Iterator<Employee>{

        int index = 0;

        @Override
        public boolean hasNext(){
            return index < employees.size();
        }
        
        @Override
        public Employee next(){
            //if exist, return object and increment index
            if(this.hasNext())
                return employees.get(index++);
            throw new NoSuchElementException();
        }
    }
}


class IteratorMain {
    public static void main(String[] args) {

        //create list
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Alice", 50000));
        employees.add(new Employee("Bob", 60000));
        employees.add(new Employee("Charlie", 70000));

        //set up the list in the concrete collection class
        Company company = new Company(employees);

        //use iterator
        Iterator<Employee> iterator = company.createIterator();

        //traverse the list and access single object using hasNext() and next() 
        double totalSalary = 0;
        while (iterator.hasNext()) {
            totalSalary += iterator.next().getSalary();
        }

        System.out.println("Total salary: " + totalSalary);
    }
}
