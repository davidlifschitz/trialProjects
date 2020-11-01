package code;

import java.util.HashMap;
import java.util.Map;
/**
 * This is a bounded cache that maintains only the most recently accessed IP Addresses
 * and their routes.  Only the least recently accessed route will be purged from the
 * cache when the cache exceeds capacity.  There are 2 closely coupled data structures:
 *   -  a Map keyed to IP Address, used for quick lookup
 *   -  a Queue of the N most recently accessed IP Addresses
 * All operations must be O(1).  A big hint how to make that happen is contained
 * in the type signature of the Map on line 38.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RouteCache
{
    // instance variables - add others if you need them
    // do not change the names of any fields as the test code depends on them
    
    // Cache total capacity and current fill count.
    private final int capacity;
    private int nodeCount;
    
    // private class for nodes in a doubly-linked queue
    // used to keep most-recently-used data
    private class Node {
        private Node prev, next;
        private final IPAddress elem; 
        private final int route;

        Node(IPAddress elem, int routerNumber) {
            prev = next = null;
            this.elem = elem;
            this.route = routerNumber;
        }  
    }
    private Node head = null;
    private Node tail = null;
    private Map<IPAddress, Node> nodeMap; // the cache itself

    protected Map<IPAddress,Node> getNodeMap()
    {
        return this.nodeMap;
    }


    /**
     * Constructor for objects of class RouteCache
     */
    public RouteCache(int cacheCapacity)
    {
        this.capacity = cacheCapacity;
        this.nodeMap = new HashMap<IPAddress,Node>();
    }

    /**
     * Lookup the output port for an IP Address in the cache, adding it if not already there
     * 
     * @param  addr   a possibly cached IP Address
     * @return     the cached route for this address, or null if not found 
     */
    public Integer lookupRoute(IPAddress address)
    {
        Node lookupNode = this.nodeMap.get(address);
        if(lookupNode != null) 
        {   
            Integer returnInt = lookupNode.route;
            return returnInt;
        }
        return null; 
     }
     
    /**
     * Update the cache each time an element's route is looked up.
     * Make sure the element and its route is in the Map.
     * Enqueue the element at the tail of the queue if it is not already in the queue.  
     * Otherwise, move it from its current position to the tail of the queue.  If the queue
     * was already at capacity, remove and return the element at the head of the queue.
     * 
     * @param  elem  an element to be added to the queue, which may already be in the queue. 
     *               If it is, don't add it redundantly, but move it to the back of the queue
     * @return       the expired least recently used element, if any, or null
     */
    public IPAddress updateCache(IPAddress elem, int route)
    {
        IPAddress returnAddress = null;
        // if the element is not in the queue.
        Node insertNode = new Node(elem, route);
        if (nodeCount == 0) {
            this.head = this.tail = insertNode;
            head.next = tail;
            tail.prev = head;
            this.nodeCount++;
        } else {
            Integer integer = this.lookupRoute(elem);
            if (integer == null) {
                
                if (head == tail) {
                    tail = insertNode;
                } else {
                    if (this.nodeCount == this.capacity) {
                        Node returnNode = head;
                        head = head.next;
                        returnAddress = returnNode.elem;
                        this.nodeCount--;
                    }
                    Node temp = tail;
                    temp.prev.next = temp;
                    tail = insertNode;
                    temp.next = tail;
                    tail.prev = temp;
                }
                head.next = tail;
                tail.prev = head;
                this.nodeCount++;
                return returnAddress;
            } else {
                // Remove the node and make it a tempNode called getNode.
                Node getNode = this.nodeMap.get(elem);
                getNode.prev.next = getNode.next;
                getNode.next.prev = getNode.prev;
                // Put it back in at the tail.
                Node temp = tail;
                temp.prev.next = temp;
                tail = getNode;
                temp.next = tail;
                tail.prev = temp;
            }
        }
        this.nodeMap.put(elem, tail);
        return returnAddress;
    }

    
    /**
     * For testing and debugging, return the contents of the LRU queue in most-recent-first order,
     * as an array of IP Addresses in CIDR format. Return a zero length array if the cache is empty
     * 
     */
    String[] dumpQueue()
    {
        String[] returnStringArray = new String[this.nodeCount];
        Node temp = tail;
        for (int i = 0; temp.next != tail;i++) {
            returnStringArray[i] = temp.elem.toCIDR();
            temp = temp.next;
        }
    	return returnStringArray; //placeholder
    }
    
    
}
