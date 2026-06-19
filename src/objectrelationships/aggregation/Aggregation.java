package objectrelationships.aggregation;

import java.util.Arrays;
import java.util.List;

/*
 - Aggregation is a special form of Association that represents a "Has-A" relationship with weak ownership. 
   The contained object can exist independently of the container object.
 - Real-Life Example: 
   A Department has Professors.
  (i) A professor can exist even if a department is deleted
  (ii) The professor may even be transferred to another department
 - UML class diagram: It is often represented by a hollow diamond (◇) on the side of the owner (the "whole").
 - Since Aggregation is a specialized form of Association, it can also have the same relationship types.
 - For given example it can be presented by Department 1 ◇──── * Professor
 - If a Professor can be associated with multiple Departments, the multiplicity could become: Department * ◇──── * Professor
*/

//Professor is the part
class Professor {
    private String name;

    public Professor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

//Department is the owner/whole
class Department {
    private String name;
    private List<Professor> professors;

    public Department(String name, List<Professor> professors) {
        this.name = name;
        this.professors = professors;
    }

    public void displayProfessors() {
        System.out.println("Department: " + name);

        for (Professor professor : professors) {
            System.out.println(professor.getName());
        }
    }
}

class AggregationMain {
    public static void main(String[] args) {
        Professor p1 = new Professor("John");
        Professor p2 = new Professor("Alice");

        Department department =
                new Department("Computer Science",
                               Arrays.asList(p1, p2));

        department.displayProfessors();

        //Department object removed
        department = null;

        System.out.println("\nAfter deleting department");

        //Professors still exist after deleting department
        System.out.println(p1.getName());
        System.out.println(p2.getName());
    }
}
