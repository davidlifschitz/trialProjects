package edu.yu.cs.com1320.project.stage4.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.yu.cs.com1320.project.impl.TrieImpl;

public class TrieImplTest {
    
    private class StringComparator<String> implements Comparator<String>
    {
        private StringComparator(){}

        @Override
        public int compare(String o1, String o2) {
            if(o1.toString().length() < o2.toString().length())
            {
                return -1;
            } else if(o1.toString().length() > o2.toString().length())
            {
                return 1;
            }
            else 
            {
                return 0;
            }
        }

    }
    TrieImpl<String> myTrie = new TrieImpl<>();
    
    private String testChar;
    private String testChar2;
    
    private String testString;
    private String testString2;

    private String testCharVal1;
    private String testCharVal2;
    
    private String testChar2Val1;

    private String testString1Val;
    private String testString2Val;

    private StringComparator sC = new StringComparator<>();
    @Before
    public void init() throws Exception {
        
        this.testChar = "a";
        this.testChar2 = "b";
        this.testCharVal1 = "this is the letter a.";
        this.testCharVal2 = "this is also the letter a.";
        this.testChar2Val1 = "this is the letter b";
        this.testString = "abc";
        this.testString2 = "def";
        this.testString1Val = "this is the string abc.";
        this.testString2Val = "this is the string def.";
        
    }

    private void putAllInOneNode()
    {
        myTrie.put(testChar, testCharVal1);
        myTrie.put(testChar, testChar2Val1);
        myTrie.put(testChar, testString1Val);
        myTrie.put(testChar,testString2Val);
    }
    private void putMultipleWordsValuesInDifferentWordsFromOnePrefix()
    {
        this.testChar = "to";
        this.testChar2 = "too";
        this.testString = "tooth";
        this.testString2 = "toothpaste";

        this.testCharVal1 = "this is the word to";
        this.testChar2Val1 = "this is the word too.";
        this.testString1Val = "this is the word tooth.";
        this.testString2Val = "this is the word toothpaste.";
        myTrie.put(testChar, testCharVal1);
        myTrie.put(testChar2, testChar2Val1);
        myTrie.put(testString, testString1Val);
        myTrie.put(testString2,testString2Val);

    }
    @Test
    public void putMethodSuccessTestWithOneLetter()
    {
        myTrie.put(this.testChar, this.testCharVal1);
    }
    @Test
    public void putMethodSuccessTestWithAWholeWord()
    {
        myTrie.put(testString, testString1Val);
    }
    @Test
    public void putMethodSuccessTestWithTwoWholeWords()
    {
        myTrie.put(testString,testString1Val);
        myTrie.put(testString2,testString2Val);
    }
    @Test
    public void putMethodSuccessTestWithTwoValuesAtOneLetter() {
        myTrie.put(this.testChar, this.testCharVal2);
        myTrie.put(this.testChar, this.testCharVal1);
    }
    @Test
    public void putMethodNulls()
    {
        myTrie.put(null, "this is the letter a");
        myTrie.put(testChar,null);
    }
    @Test
    public void getAllSortedTestSuccess1()
    {
        putMethodSuccessTestWithAWholeWord();
        putMethodSuccessTestWithTwoValuesAtOneLetter();
        List myList = myTrie.getAllSorted(testString, sC);
        List myList2 = myTrie.getAllSorted(testChar, sC);
        assertTrue(myList.size() == 1);
        assertTrue(myList2.size() == 2);
    }
    @Test
    public void getAllSortedTestSuccess2()
    {
        putMethodSuccessTestWithTwoWholeWords();
        List myList = myTrie.getAllSorted(testString, sC);
        List myList2 = myTrie.getAllSorted(testString2, sC);
        assertTrue(myList.size() == 1);
        assertTrue(myList2.size() == 1);
    }
    @Test
    public void getAllSortedTestFailure()
    {
        assertTrue(myTrie.getAllSorted(null, sC) == null);
        assertTrue(myTrie.getAllSorted(testChar, null) == null);
        assertTrue(myTrie.getAllSorted(testChar, sC) == null);
        assertTrue(myTrie.getAllSorted(testString, sC) == null);
        putMethodSuccessTestWithAWholeWord();
        putMethodSuccessTestWithTwoValuesAtOneLetter();
        assertFalse((myTrie.getAllSorted(testString, sC) == null));
        assertFalse((myTrie.getAllSorted(testChar, sC) == null));
    }
    @Test
    public void getAllSortedWithPrefixTestSuccess()
    {
        putMethodSuccessTestWithAWholeWord();
        putMethodSuccessTestWithTwoValuesAtOneLetter();
        putMethodSuccessTestWithTwoWholeWords();
        List myList1 = myTrie.getAllWithPrefixSorted(testChar, sC);
        List myList2 = myTrie.getAllWithPrefixSorted("d",sC);
    }
    @Test
    public void getAllSortedWithPrefixTestFailure()
    {
        assertTrue(myTrie.getAllWithPrefixSorted(null, sC) == null);
        assertTrue(myTrie.getAllWithPrefixSorted(testChar, null) == null);
        assertTrue(myTrie.getAllWithPrefixSorted(testChar, sC) == null);
        assertTrue(myTrie.getAllWithPrefixSorted(testString, sC) == null);
        putMethodSuccessTestWithAWholeWord();
        putMethodSuccessTestWithTwoValuesAtOneLetter();
        putMethodSuccessTestWithTwoWholeWords();
        assertFalse((myTrie.getAllSorted(testString, sC) == null));
        assertFalse((myTrie.getAllSorted(testChar, sC) == null));
    }
    @Test
    public void DeleteTestSuccess()
    {
        putMethodSuccessTestWithAWholeWord();
        String deletedString = myTrie.delete(testString, testString1Val);
        assertTrue(deletedString.equals(testString1Val));
    }
    @Test
    public void DeleteTestFailure()
    {
        String deletedString = myTrie.delete(testString, testString1Val);
        assertTrue(deletedString == null);
        String deletedString2 = myTrie.delete(null, testString1Val);
        assertTrue(deletedString2 == null);
        String deletedString3 = myTrie.delete(testString, null);
        assertTrue(deletedString3 == null);
    }
    @Test
    public void DeleteAllTestSuccess()
    {
        putAllInOneNode();
        Set<String> mySet = myTrie.deleteAll(testChar);
        assertTrue(mySet.size() == 4);
    }
    @Test
    public void DeleteAllTestFailure()
    {
        Set<String> mySet = myTrie.deleteAll("key");
        assertNull(mySet);
        myTrie.put("a", "a value.");
        mySet = myTrie.deleteAll("key");
        assertNull(mySet);

    }
    @Test
    public void DeleteAllWithPrefixTestSuccess()
    {
        putMultipleWordsValuesInDifferentWordsFromOnePrefix();
        Set<String> mySet = myTrie.deleteAllWithPrefix("to");
        assertTrue(mySet.size() == 4);


        putMultipleWordsValuesInDifferentWordsFromOnePrefix();
        mySet = myTrie.deleteAllWithPrefix("tooth");
        assertTrue(mySet.size() == 2);


    }
    @Test
    public void DeleteAllWithPrefixTestFailure()
    {
        Set<String> mySet = myTrie.deleteAllWithPrefix("to");
        assertTrue(mySet.size() == 0);
    }

    @Test
    public void testingWithQueues()
    {
        Queue<String> myQueue = new ArrayDeque<>();
        myQueue.add("e");
        myQueue.add("f");
        myQueue.add("g");
        myQueue.add("h");
        myQueue.remove();
        myQueue.remove();

    }    
}