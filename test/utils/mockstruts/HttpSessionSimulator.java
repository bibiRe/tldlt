package utils.mockstruts;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class HttpSessionSimulator implements HttpSession
{
    @SuppressWarnings("rawtypes")
    private Hashtable values;
    private boolean valid = true;
    private ServletContext context;

    @SuppressWarnings("rawtypes")
    public HttpSessionSimulator(ServletContext context)
    {
        this.context = context;
        values = new Hashtable();
    }

    public Object getAttribute(String s) throws IllegalStateException
    {
        checkValid();
        return values.get(s);
    }

    @SuppressWarnings("rawtypes")
    public Enumeration getAttributeNames() throws IllegalStateException
    {
        checkValid();
        return values.keys();
    }

    public long getCreationTime() throws IllegalStateException
    {
        checkValid();
        return -1;
    }

    public String getId()
    {
        return "-9999";
    }

    public long getLastAccessedTime()
    {
        return -1;
    }

    public int getMaxInactiveInterval() throws IllegalStateException
    {
        checkValid();
        return -1;
    }

    /**
     * This method is not supported.
     */
    public HttpSessionContext getSessionContext()
    {
        throw new UnsupportedOperationException("getSessionContext not supported!");
    }

    public Object getValue(String s) throws IllegalStateException
    {
        checkValid();
        return values.get(s);
    }

    public String[] getValueNames() throws IllegalStateException
    {
        checkValid();
        return (String[]) values.keySet().toArray();
    }

    public void invalidate() throws IllegalStateException
    {
        checkValid();
        this.valid = false;
    }

    public boolean isNew() throws IllegalStateException
    {
        checkValid();
        return false;
    }

    @SuppressWarnings("unchecked")
    public void putValue(String s, Object obj) throws IllegalStateException
    {
        checkValid();
        values.put(s,obj);
    }

    public void removeAttribute(String s) throws IllegalStateException
    {
        checkValid();
        values.remove(s);
    }

    public void removeValue(String s) throws IllegalStateException
    {
        checkValid();
        values.remove(s);
    }

    @SuppressWarnings("unchecked")
    public void setAttribute(String s, Object obj) throws IllegalStateException
    {
        checkValid();
        if (obj == null)
            removeValue(s);
        else
            values.put(s,obj);
    }

    public void setMaxInactiveInterval(int i)
    {
    }

    public ServletContext getServletContext() {
        return this.context;
    }

    private void checkValid() throws IllegalStateException {
        if (!valid)
            throw new IllegalStateException("session has been invalidated!");
    }

    protected boolean isValid() {
        return valid;
    }
}
