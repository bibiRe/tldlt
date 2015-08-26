package utils;

import org.g4studio.common.dao.Dao;
import org.junit.Before;
import org.mockito.Mockito;

import com.sysware.tldlt.app.service.common.BaseAppServiceImpl;

public abstract class BaseAppServiceImplTest {
    /**
     * App Dao对象.
     */
    protected Dao appDao;
    /**
     * g4 Dao对象.
     */
    protected Dao g4Dao;
    
    /**
     * service实现对象.
     */
    protected BaseAppServiceImpl serviceImpl;
    /**
     * 创建service实现类对象.
     * @return service实现类对象.
     */
    protected abstract  BaseAppServiceImpl createService();
    
    @Before
    public void setUp() {
        appDao = Mockito.mock(Dao.class);
        g4Dao = Mockito.mock(Dao.class);
        serviceImpl = createService();
        serviceImpl.setAppDao(appDao);
        serviceImpl.setG4Dao(g4Dao);
    }
}
