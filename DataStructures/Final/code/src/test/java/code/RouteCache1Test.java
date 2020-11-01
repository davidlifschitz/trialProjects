package code;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import code.IPRouter;

import java.io.*;

public class RouteCache1Test {
    
    private RouteCache routeCache;
    private IPRouter router;


    @Before
    public void setUp()
    {
        this.router = new IPRouter(8,2); 
        try {
            router.loadRoutes("routes1.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        this.routeCache = this.router.getRouteCache();
    }

    @Test
    public void testingUpdateChache1()
    {
        IPAddress address = new IPAddress("85.0.0.0");
        IPAddress shouldBeNull = this.routeCache.updateCache(address, 1);
        assertNull(shouldBeNull);
    }
    @Test
    public void testingUpdateChache2()
    {
        //This tests removing the least-recent IPaddress when adding goes over capacity
        IPAddress address1 = new IPAddress("85.0.0.0");
        IPAddress shouldBeNull = this.routeCache.updateCache(address1, 1);
        assertNull(shouldBeNull);

        IPAddress address2 = new IPAddress("85.0.0.1");
        shouldBeNull = this.routeCache.updateCache(address2, 1);
        assertNull(shouldBeNull);

        IPAddress address3 = new IPAddress("85.0.0.2");
        IPAddress shouldNotBeNull = this.routeCache.updateCache(address3, 1);
        assertEquals(address1, shouldNotBeNull);
    }

}