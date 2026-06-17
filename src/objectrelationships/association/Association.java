package objectrelationships.association;

/*
 - Association is a relationship between two independent classes where one object uses or interacts with another object.
 - It represents a "Uses-A" or "Knows-A" relationship and both objects can exist independently.
 - Aggregation and Composition are specialized forms of Association.
 - Real-life Example:
    1. A Teacher teaches a Student.
    2. A Customer places an Order.
    3. A Driver drives a Car.
 - Even if the teacher is removed, the student can still exist, and vice versa.
 - UML notation: Presented by simple line( ——————). * placed on the multiple objects side.
 - Types of Association:
    1. Based on Navigability (Direction)
      (i) Unidirectional Association: Only one class knows about the other. Navigation is possible in one direction.
      UML: Teacher ——————> Student
      (ii)Bidirectional Association: Both classes know about each other. Navigation is possible in both directions.
      UML: Teacher <——————> Student

    2. Based on Multiplicity (Cardinality)
      (i) One-to-One (1:1) Person 1 —————— 1 Passport
      (ii) One-to-Many (1:*)Teacher 1 —————— * Student
      (iii) Many-to-One (*:1) Employee * —————— 1 Department
      (iv) Many-to-Many (*:*) Student * —————— * Course
*/

//Two independent classes, Student and Teacher
class Student {
    private String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Teacher {
    private String name;

    public Teacher(String name) {
        this.name = name;
    }

    //one object merely interacts with another
    //teacher doesn't own student
    public void teach(Student student) {
        System.out.println(name + " is teaching " + student.getName());
    }
}


class AssociationMain {
    public static void main(String[] args) {
        Student student = new Student("ABC");
        Teacher teacher = new Teacher("XYZ");

        //teacher just interacts with student
        teacher.teach(student);
    }
}
