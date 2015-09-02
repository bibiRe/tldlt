package utils.mockstruts;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Simulates a <code>javax.servlet.RequestDispatcher</code> object.
 */
public class RequestDispatcherSimulator implements RequestDispatcher
{
    private Object dispatchedResource;
    /**
     *@param    dispatchedResource      The <code>dispatchedResource</code> object represents the resource that
     *                                  <code>this</code> <code>javax.servlet.RequestDispatcher</code> is tied to.
     *                                  Currently this class only supports <code>javax.servlet.Servlet</code> objects
     *                                  and <code>java.lang.String</code> objects.  If the parameter passed in is not
     *                                  a <code>javax.servlet.Servlet</code> object when forward or include is called
     *                                  the parameter's toString method is called and sent to <code>System.out</code>.
     *                                  Otherwise, the appropriate service method is called.
     */
    public RequestDispatcherSimulator( Object dispatchedResource )
    {
        this.dispatchedResource = dispatchedResource;
    }
    /**
     * Simulates the forward method of the <code>javax.servlet.RequestDispatcher</code> interface
     */
    public void forward( ServletRequest request, ServletResponse response ) throws ServletException, IOException
    {
        if( dispatchedResource instanceof Servlet )
            ((Servlet)dispatchedResource).service( request, response );
    }
    public void include( ServletRequest request, ServletResponse response ) throws ServletException, IOException
    {
        System.out.println( dispatchedResource.toString() );
    }

    public String getForward() {
        if (dispatchedResource instanceof String)
            return (String) dispatchedResource;
        else
            return dispatchedResource.getClass().toString();
    }
}

