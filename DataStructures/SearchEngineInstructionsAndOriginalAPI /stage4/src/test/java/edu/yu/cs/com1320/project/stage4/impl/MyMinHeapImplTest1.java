package edu.yu.cs.com1320.project.stage4.impl;

import static org.junit.Assert.assertTrue;



import org.junit.Before;
import org.junit.Test;

import edu.yu.cs.com1320.project.impl.MinHeapImpl;

public class MyMinHeapImplTest1 {

    private String testString;
    private String testString2;
    private String testString3;
    private String testString4;
    private String testString5;
    private String testString6;
    private String testString7;
    private String testString8;
    private String testString9;
    private String testString10;
    private String testString11;
    private String testString12;
    private String testString13;
    MinHeapImpl mhi = new MinHeapImpl<String>();
    
    @Before 
    public void init() throws Exception
    {
        
        this.testString = "a"; 
        this.testString2 = "b"; 
        this.testString3 = "c";  
        this.testString4 = "d";  
        this.testString5 = "e";  
        this.testString6 = "f";  
        this.testString7 = "g";  
        this.testString8 = "h";  
        this.testString9 = "i";  
        this.testString10 = "j"; 
        this.testString11 = "k"; 
        this.testString12 = "l"; 
        this.testString13 = "m"; 
    }

    private void insertOneTestString()
    {
        mhi.insert(testString);
    }
    private void insertAllTestString()
    {
        mhi.insert(testString);
        mhi.insert(testString2);
        mhi.insert(testString3);
        mhi.insert(testString4);
        mhi.insert(testString5);
        mhi.insert(testString6);
        mhi.insert(testString7);
        mhi.insert(testString8);
        mhi.insert(testString9);
        mhi.insert(testString10);
        mhi.insert(testString11);
        mhi.insert(testString12);
        mhi.insert(testString13);
    }
    private void insertAllTestStringReverse()
    {
        mhi.insert(testString13);
        mhi.insert(testString12);
        mhi.insert(testString11);
        mhi.insert(testString10);
        mhi.insert(testString9);
        mhi.insert(testString8);
        mhi.insert(testString7);
        mhi.insert(testString6);
        mhi.insert(testString5);
        mhi.insert(testString4);
        mhi.insert(testString3);
        mhi.insert(testString2);
        mhi.insert(testString);
    }
    @Test
    public void putOneTest1()
    {
        insertOneTestString();
        String removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString));
    }
    
    @Test
    public void putAllTestInOrder1() {
        insertAllTestString();
        String removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString2));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString3));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString4));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString5));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString6));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString7));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString8));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString9));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString10));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString11));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString12));
    }
    @Test
    public void putAllTestInReverse1() {
        insertAllTestStringReverse();
        String removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString2));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString3));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString4));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString5));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString6));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString7));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString8));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString9));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString10));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString11));
        removed = (String) mhi.removeMin();
        assertTrue(removed.equals(testString12));
    }

    @Test
    public void reheapifyTest1()
    {
        insertAllTestString();
        testString2 = "z";
        mhi.insert(testString2);
        mhi.reHeapify(testString2);
        
    }
}