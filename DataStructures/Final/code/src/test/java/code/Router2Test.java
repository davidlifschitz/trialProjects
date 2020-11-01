package code;

import static org.junit.Assert.assertEquals;


import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestRouter.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class Router2Test
{

    //private TrieST<Integer> router;
    private IPRouter router;
    /**
     * Default constructor for test class TestRouter
     */
    public Router2Test()
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
            router.loadRoutes("routes2.txt");
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
   public void Router2Test1()
   {
        //Checking for the actual rule itself
        IPAddress address = new IPAddress("24.0.0.0");
        int res = this.router.getRoute(address);
        assertEquals(4, res);
        
        //Checking for a "leased" route
        address = new IPAddress("24.16.24.0");
        res = this.router.getRoute(address);
        assertEquals(4, res);

        //Checking for the actual rule itself
        address = new IPAddress("24.16.0.0");
        res = this.router.getRoute(address);
        assertEquals(4, res);
       
        //Checking for a "leased" route
        address = new IPAddress("24.16.24.0");
        res = this.router.getRoute(address);
        assertEquals(4, res);
   }
   @Test
   public void Router2Test2()
   {
        //Checking for the actual rule itself
        IPAddress address = new IPAddress("24.128.0.0");
        int res = this.router.getRoute(address);
        assertEquals(3, res);

        //Checking for a "leased" route
        address = new IPAddress("24.128.255.0");
        res = this.router.getRoute(address);
        assertEquals(3, res);

        //Checking for the actual rule itself
        address = new IPAddress("24.64.0.0");
        res = this.router.getRoute(address);
        assertEquals(4, res);
        
        //Checking for a "leased" route
        address = new IPAddress("24.64.1.1");
        res = this.router.getRoute(address);
        assertEquals(4, res);
        
        //Checking for the actual rule itself
        address = new IPAddress("24.91.0.0");
        res = this.router.getRoute(address);
        assertEquals(7, res);
        
        //Checking for the actual rule itself
        address = new IPAddress("24.98.0.0");
        res = this.router.getRoute(address);
        assertEquals(6, res);
   }

   @Test
   public void Router2TestDelete1()
   {
        //Checking for the actual rule itself
        IPAddress address = new IPAddress("24.0.0.0");
        int res = this.router.getRoute(address);
        assertEquals(4, res);
        //Check that when one overlap is deleted it reverts back from port 4 to port 2
        this.router.deleteRule("24.0.0.0/12");
        res = this.router.getRoute(address);
        assertEquals(2, res);
        //Check that when the second overlap is deleted it reverts back from port 2 to port 1
        this.router.deleteRule("24.0.0.0/9");
        res = this.router.getRoute(address);
        assertEquals(1, res);
   }



   
}
