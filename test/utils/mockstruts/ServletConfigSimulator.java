package utils.mockstruts;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class ServletConfigSimulator implements ServletConfig {

    @SuppressWarnings("rawtypes")
    private Hashtable parameters;
    private ServletContext context;

    @SuppressWarnings("rawtypes")
    public ServletConfigSimulator() {
        parameters = new Hashtable();
        context = new ServletContextSimulator();
    }

    /**
     * Returns a <code>String</code> containing the value of the
     * named initialization parameter, or <code>null</code> if
     * the parameter does not exist.
     * @param name a <code>String</code> specifying the name
     *            of the initialization parameter
     * @return a <code>String</code> containing the value
     *         of the initialization parameter
     */
    public String getInitParameter(String name) {
        return (String) parameters.get(name);
    }

    /**
     * Returns the names of the servlet's initialization parameters
     * as an <code>Enumeration</code> of <code>String</code> objects,
     * or an empty <code>Enumeration</code> if the servlet has
     * no initialization parameters.
     * @return an <code>Enumeration</code> of <code>String</code> objects
     *         containing the names of the servlet's
     *         initialization parameters
     */
    @SuppressWarnings("rawtypes")
    public Enumeration getInitParameterNames() {
        return parameters.keys();
    }

    /**
     * Returns a reference to the {@link ServletContext} in which the caller
     * is executing.
     * @return a {@link ServletContext} object, used
     *         by the caller to interact with its servlet
     *         container
     * @see ServletContext
     */
    public ServletContext getServletContext() {
        return context;
    }

    /**
     * Returns the name of this servlet instance.
     * The name may be provided via server administration, assigned in the
     * web application deployment descriptor, or for an unregistered (and thus
     * unnamed) servlet instance it will be the servlet's class name.
     * @return the String "ActionServlet"
     */
    public String getServletName() {
        return "ActionServlet";
    }

    /**
     * Sets a named initialization parameter with the supplied
     * <code>String</code> value.
     * @param key a <code>String</code> specifying the name
     *            of the initialization parameter
     * @param value a <code>String</code> value for this initialization
     *            parameter
     */
    @SuppressWarnings("unchecked")
    public void setInitParameter(String key, String value) {
        parameters.put(key, value);
    }

}