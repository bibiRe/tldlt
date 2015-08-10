package utils;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class ServletOutputStreamSimulator extends ServletOutputStream {
    private OutputStream outStream;

    /**
     * Default constructor that sends all output to <code>System.out</code>.
     */
    public ServletOutputStreamSimulator() {
        this.outStream = System.out;
    }

    /**
     * Constructor that sends all output to given OutputStream.
     * @param out OutputStream to which all output will be sent.
     */
    public ServletOutputStreamSimulator(OutputStream out) {
        this.outStream = out;
    }

    public void write(int b) {
        try {
            outStream.write(b);
        } catch (IOException io) {
            System.err.println("IOException: " + io.getMessage());
            io.printStackTrace();
        }
    }
}
