package utils;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;

public class ExceptionDuringTestError extends Error {

    /**
     * 
     */
    private static final long serialVersionUID = 3858360040183718143L;
    
    private Throwable rootCause;

    public ExceptionDuringTestError(String message, Throwable rootCause) {
        super(message);
        this.rootCause = rootCause;
    }

    public void printStackTrace() {
        super.printStackTrace();
        System.out.println("------------");
        System.out.println("Root Cause:");
        System.out.println("------------");
        rootCause.printStackTrace();
        if (rootCause instanceof ServletException) {
            Throwable root2 = ((ServletException) rootCause).getRootCause();
            if (root2 != null) {
                System.out.println("------------");
                System.out.println("Root Cause:");
                System.out.println("------------");
                root2.printStackTrace();
            }
        }
    }

    public void printStackTrace(PrintStream stream) {
        super.printStackTrace(stream);
        stream.println("------------");
        stream.println("Root Cause:");
        stream.println("------------");
        rootCause.printStackTrace(stream);
        if (rootCause instanceof ServletException) {
            Throwable root2 = ((ServletException) rootCause).getRootCause();
            if (root2 != null) {
                stream.println("------------");
                stream.println("Root Cause:");
                stream.println("------------");
                root2.printStackTrace(stream);
            }
        }
    }

    public void printStackTrace(PrintWriter stream) {
        super.printStackTrace(stream);
        stream.println("------------");
        stream.println("Root Cause:");
        stream.println("------------");
        rootCause.printStackTrace(stream);
        if (rootCause instanceof ServletException) {
            Throwable root2 = ((ServletException) rootCause).getRootCause();
            if (root2 != null) {
                stream.println("------------");
                stream.println("Root Cause:");
                stream.println("------------");
                root2.printStackTrace(stream);
            }
        }
    }
}
