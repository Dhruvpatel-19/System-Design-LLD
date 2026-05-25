package designpatterns.structuralpatterns.composite;

import java.util.ArrayList;
import java.util.List;

/*
- The Composite Pattern is a structural design pattern that organizes objects into tree structures, 
  enabling clients to treat individual and composite objects uniformly through a common interface.

Participants:
  1. Component: Common interface for both Leaf and Composite
  2. Leaf: Represents individual object
  3. Composite: Contains children (Leaf or Composite)
*/


//Component, common interface
interface FileSystem {
    void showDetails();
}

//Leaf, primitive  objects
class File implements FileSystem {
    private String name;

    public File(String name) {
        this.name = name;
    }

    @Override
    public void showDetails() {
        System.out.println("File: " + name);
    }
}

//Composite, stores child components
class Folder implements FileSystem {
    private String name;

    //Children
    private List<FileSystem> items = new ArrayList<>();

    public Folder(String name) {
        this.name = name;
    }

    public void add(FileSystem item) {
        items.add(item);
    }

    public void remove(FileSystem item) {
        items.remove(item);
    }

    //Recursive call, enables tree-traversal
    @Override
    public void showDetails() {
        System.out.println("Folder: " + name);
        for (FileSystem item : items) {
            item.showDetails();
        }
    }
}

//Client
class CompositeMain {
    public static void main(String[] args) {

        File file1 = new File("Resume.pdf");
        File file2 = new File("Photo.png");

        //adding file1 into folder1
        Folder folder1 = new Folder("Documents");
        folder1.add(file1);

        //adding file2 into folder2
        Folder folder2 = new Folder("Images");
        folder2.add(file2);

        //adding both folders into parent folder
        Folder root = new Folder("Root");
        root.add(folder1);
        root.add(folder2);

        //invoking method through common interface (polymorphism)
        root.showDetails();

    }
}