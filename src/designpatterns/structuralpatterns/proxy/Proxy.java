package designpatterns.structuralpatterns.proxy;

/*
 - The Proxy Pattern provides a placeholder (or surrogate) for another object to control access to it.
 - The client communicates with the proxy, which forwards requests to the real object. The proxy can also 
   provide extra functionality such as access control, lazy initialization, logging, and caching.
*/

//Subject interface
interface Document {
    void view();
}

//Real object
class RealDocument implements Document {
    private String fileName;

    public RealDocument(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void view() {
        System.out.println("Viewing document: " + fileName);
    }
}

//Proxy class, controls access to RealSubject
class DocumentProxy implements Document {
    //Composition
    private RealDocument realDocument;
    private String fileName;
    private String userRole;

    public DocumentProxy(String fileName, String userRole) {
        this.fileName = fileName;
        this.userRole = userRole;
    }

    @Override
    public void view() {
        //access Control Logic
        if (userRole.equalsIgnoreCase("ADMIN")) {
            // Lazy initialization + access granted
            if (realDocument == null) {
                realDocument = new RealDocument(fileName);
            }
            realDocument.view();
        } else {
            System.out.println("Access Denied: You are not authorized to view this document.");
        }
    }
}

//Client code
class ProxyMain {
    public static void main(String[] args) {
        //Unauthorized user
        Document doc1 = new DocumentProxy("secret.pdf", "USER");
        doc1.view();

        System.out.println("------");

        //Authorized user
        Document doc2 = new DocumentProxy("secret.pdf", "ADMIN");
        doc2.view();
    }
}
