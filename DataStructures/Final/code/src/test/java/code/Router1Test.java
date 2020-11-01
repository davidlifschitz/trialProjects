package code;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import code.IPRouter;

import java.io.*;

/**
 * The test class TestRouter.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class Router1Test
{

    //private TrieST<Integer> router;
    private IPRouter router;
    /**
     * Default constructor for test class TestRouter
     */
    public Router1Test()
    {}

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
       this.router = new IPRouter(8,4); 
        try {
            router.loadRoutes("routes1.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
    }

    @Test(expected = RuntimeException.class) 
    public void testLoadRoutesFailure()
    {
        try{
            router.loadRoutes("badRoutes.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
    }

    /**
     * Handle an unroutable address
     */
    @Test
    public void testBadRoute()
    {
        IPAddress address = new IPAddress("73.73.0.1");
        assertEquals(-1, this.router.getRoute(address));
    }

    /**
     * Handle an address that only matches one prefix
     */
    @Test
    public void port2Test()
    {
        IPAddress address = new IPAddress("85.2.0.1");
        String addressString = address.toString();
        int res = this.router.getRoute(address);
        assertEquals(2, res);
    }

    /**
     * Handle an address that only matches multiple prefixes. Only the longest one counts
     */
    @Test
    public void port1Test()
    {
        IPAddress address = new IPAddress("85.85.85.85");
        int res = this.router.getRoute(address);
        assertEquals(1, res);
    }

   @Test
   public void Test1FAILURE()
   {
        IPAddress address = new IPAddress("84.0.0.0");
        int res = this.router.getRoute(address);
        assertEquals(-1, res);
   }
   @Test 
   public void port2Test2()
   {
        IPAddress address = new IPAddress("85.86.85.85");
        int res = this.router.getRoute(address);
        assertEquals(2,res);
   }
   @Test
   public void port1Test2()
   {
        IPAddress address = new IPAddress("85.85.255.255");
        int res = this.router.getRoute(address);
        assertEquals(1,res);
   }
   @Test
   public void deleteTest1()
   {
       IPAddress address = new IPAddress("85.85.0.0");
       int res = this.router.getRoute(address);
       assertEquals(1,res);
       
       this.router.deleteRule("85.85.0.0/15");
       res = this.router.getRoute(address);
       assertEquals(2, res);
   }
   @Test
   public void deleteTest2()
   {
       IPAddress address = new IPAddress("85.85.0.0");
       int res = this.router.getRoute(address);
       assertEquals(1,res);
       
       this.router.deleteRule("85.0.0.0/8");
       res = this.router.getRoute(address);
       assertEquals(1, res);
   }
   @Test
   public void deleteTest3DeletingBoth()
   {
        IPAddress address = new IPAddress("85.85.0.0");
        int res = this.router.getRoute(address);
        assertEquals(1,res);

        this.router.deleteRule("85.0.0.0/8");
        res = this.router.getRoute(address);
        assertEquals(1, res);
        
        this.router.deleteRule("85.85.0.0/15");
        res = this.router.getRoute(address);
        assertEquals(-1, res);
   }




   @Test
   public void testRouteCache1()
   {
        IPAddress address = new IPAddress("85.85.0.0");
        int res = this.router.getRoute(address);
        
   }
}
