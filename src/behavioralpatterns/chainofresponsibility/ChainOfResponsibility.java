package behavioralpatterns.chainofresponsibility;

/*
- The Chain of Responsibility design pattern is a behavioral design pattern that allows
  an object to pass a request along a chain of handlers. Each handler in the chain decides 
  either to process the request or to pass it along the chain to the next handler.
- This pattern is used when multiple objects may handle a request and the handler is not known in advance.
*/

//Handler
abstract class Logger {

    //to set next handler
    protected Logger nextLogger;

    public void setNextLogger(Logger nextLogger) {
        this.nextLogger = nextLogger;
    }

    public void logMessage(String level, String message) {
        //if current one can handle request, process it
        if (canHandle(level)) {
            write(message);
        } else if (nextLogger != null) { //oherwise, pass request to the next handler if avaialble
            nextLogger.logMessage(level, message);
        }
        else {
        System.out.println("No handler found for level: " + level);
        }
    }

    //handlers will implement following methods
    //check if current one is valid to process the request
    protected abstract boolean canHandle(String level);
    //how it will be processed
    protected abstract void write(String message);
}


//Concrete Handlers
class InfoLogger extends Logger {

    @Override
    protected boolean canHandle(String level) {
        return "INFO".equalsIgnoreCase(level);
    }

    @Override
    protected void write(String message) {
        System.out.println("INFO: " + message);
    }
}

class DebugLogger extends Logger {
    
    @Override
    protected boolean canHandle(String level) {
        return "DEBUG".equalsIgnoreCase(level);
    }

    @Override
    protected void write(String message) {
        System.out.println("DEBUG: " + message);
    }
}

class ErrorLogger extends Logger {
    
    @Override
    protected boolean canHandle(String level) {
        return "ERROR".equalsIgnoreCase(level);
    }

    @Override
    protected void write(String message) {
        System.out.println("ERROR: " + message);
    }
}

//Client
class ChainOfResponsibilityMain {
    public static void main(String[] args) {
        
        //Define different handlers
        Logger info = new InfoLogger();
        Logger debug = new DebugLogger();
        Logger error = new ErrorLogger();

        //Build chain
        info.setNextLogger(debug);
        debug.setNextLogger(error);

        //Send requests
        info.logMessage("INFO", "This is an info message");
        info.logMessage("DEBUG", "Debugging issue");
        info.logMessage("ERROR", "Something went wrong");
        info.logMessage("XYZ", "No handler found");

    }
}
