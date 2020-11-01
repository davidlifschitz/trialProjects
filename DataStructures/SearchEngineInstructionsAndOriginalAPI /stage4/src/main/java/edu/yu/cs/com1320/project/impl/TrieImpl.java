package edu.yu.cs.com1320.project.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import java.util.Stack;

import edu.yu.cs.com1320.project.Trie;





public class TrieImpl<Value> implements Trie<Value> {

    private static final int characterSize = 91;
    private TrieNode root;
    private Value valueFromDelete = null; 
    private Set<Value> bigSetForDeleteAll = new HashSet<Value>();

    public TrieImpl()
    {
    }


    public static class TrieNode<Value> {

        protected Set<Value> val; //Will be a set of Documents when the time comes
        protected TrieNode[] children;
        protected Boolean isTheEndOfAWord; 
        public TrieNode()
        {
            this.val = new HashSet<Value>();
            this.children = new TrieNode[TrieImpl.characterSize];
            this.isTheEndOfAWord = false;

        }
        private boolean containsChildren()
        {
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null)
                {
                    return true;
                } 
            }
            return false;
        }
    }

    
    /**
     * add the given value at the given key
     * @param key
     * @param val
     */
    public void put(String key, Value val) {
        if (key == null || val == null) {
            return;
        }
        if(root == null)
        {
            this.root = new TrieNode<Value>();
        }
        TrieNode tempNode = root;
        String stringToCaps = key.toUpperCase();

        for(int depth = 0; depth < key.length(); depth++)
        {
            int index = stringToCaps.charAt(depth);
            if(tempNode.children[index] == null)
            {
                tempNode.children[index] = new TrieNode();
            }
            tempNode = tempNode.children[index];
            
        }
        tempNode.val.add(val);
        tempNode.isTheEndOfAWord = true;
    }


    /**
     * get all exact matches for the given key, sorted in descending order.
     * Search is CASE INSENSITIVE.
     * @param key
     * @param comparator used to sort values
     * @return a List of matching Values, in descending order
     */
    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
        if (key == null || root == null) {
            return null;
        }
        TrieNode tempNode = root; 
        String stringToCaps = key.toUpperCase();
        int index = 0;
        for (int depth = 0; depth < stringToCaps.length(); depth++) 
        { 
            index = stringToCaps.charAt(depth); 
            
            if (tempNode.children[index] == null) 
                {
                    break;
                }
                
            tempNode = tempNode.children[index]; 
        }
        Set nSet = tempNode.val;
        List<Value> listOfValues = new ArrayList<Value>(nSet);
        listOfValues.sort(comparator.reversed());        
        return listOfValues;
    }

    

    /**
     * get all matches which contain a String with the given prefix, sorted in descending order.
     * For example, if the key is "Too", you would return any value that contains "Tool", "Too", "Tooth", "Toodle", etc.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @param comparator used to sort values
     * @return a List of all matching Values containing the given prefix, in descending order
     */
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        if (prefix == null || root == null) {
            return null;
        }
        String prefixToCaps = prefix.toUpperCase();
        //Now we need to get to the node at the end of the prefix
        TrieNode endOfPrefixNode = get(root, prefixToCaps, 0);
        //Now we need to get to every node in its children
        Set<Value> bigSet = new HashSet<Value>();
        collectAllChildren(endOfPrefixNode, prefix, bigSet);
        List<Value> listOfValues = new ArrayList<Value>(bigSet);
        bigSet = null;
        listOfValues.sort(comparator);        
        return listOfValues;
    }
    
    private TrieNode get(TrieNode x, String key, int d)
    {
        //link was null - return null, indicating a miss
        if (x == null)
        {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (key.length() == d)
        {
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        return this.get(x.children[c], key, d + 1);
    }

    private void collectAllChildren(TrieNode node, String prefix, Set<Value> bigSet) 
    {
        if(node == null) return;
        if(node.val != null) bigSet.addAll(node.val);
        for(char c = 0; c < 91; c++)
        {
            collectAllChildren(node.children[c], prefix, bigSet);
        }
    }

    /**
     * delete ONLY the given value from the given key. Leave all other values.
     * @param key
     * @param val
     * @return if there was a Value already at that key, return that previous Value. Otherwise, return null.
     */
    public Value delete(String key, Value val) {
        if (key == null || val == null)
            return null;
        String stringToCaps = key.toUpperCase();
        TrieNode node = delete(root, stringToCaps, 0, val);
        Value deletedValue = valueFromDelete;
        return deletedValue;
    }

    private TrieNode delete(TrieNode x, String key, int d, Value value)
        {
        if (x == null){
            return null;
            }
        //we're at the node to delete - set the val to null
        if (d == key.length())
        {
            Object[] vA = x.val.toArray();
            for (Object object : vA) {
                if (object.equals(value))
                {
                    valueFromDelete = (Value) object;
                }
            }
            x.val.remove(value);
        }
        //continue down the trie to the target node
        else
        {
        int c = key.toUpperCase().charAt(d);
        TrieNode node = x.children[c];
        x.children[c] = this.delete(node, key, d + 1,value);
        }

        //this node has a val – do nothing, return the node
        if (x.val.size() != 0)
        {
        return x;
        } 
        //otherwise, check if subtrie rooted at x is completely empty
        for (int q = 0; q <this.characterSize; q++)
        {
        if (x.children[q] != null)
        {
        return x; //not empty
        }
        }
        //empty - set this link to null in the parent
        return null;
        }

        /**
     * delete ALL exact matches for the given key
     * 
     * @param key
     * @return a Set of all Values that were deleted.
     */
    public Set deleteAll(String key) {
        if(key == null || root == null) return null;
        String allCaps = key.toUpperCase();
        TrieNode node = delete(root, allCaps, 0);

        Set<Value> deleteAllSet = bigSetForDeleteAll;
        if(deleteAllSet.size() == 0) return null;
        return deleteAllSet;
    }    
    private TrieNode delete(TrieNode temp, String key, int d)
    {
        if (temp == null) {
            return null;
        }
        // we're at the node to delete - set the val to null
        if (d == key.length()) {
            bigSetForDeleteAll.addAll(temp.val);
            temp.val.removeAll(temp.val);
        }
        // continue down the trie to the target node
        else {
            char c = key.charAt(d);
            temp.children[c] = this.delete(temp.children[c], key, d + 1);
        }

        // this node has a val – do nothing, return the node
        if (temp.val.size() > 0) {
            return temp;
        }
        // otherwise, check if subtrie rooted at x is completely empty
        for (int c = 0; c < characterSize; c++) {
            if (temp.children[c] != null) {
                return temp; // not empty
            }
        }
        // empty - set this link to null in the parent
        return null;
    }

    //STILL NEED TO DO
    /**
     * Delete all matches that contain a String with the given prefix. Search is
     * CASE INSENSITIVE.
     * 
     * @param prefix
     * @return a Set of all Values that were deleted.
     */

     //VERY CLOSE TO FINISHING HAD TO STOP BEFORE CHAG HOPEFULLY WILL CONTINUE AFTER
    public Set deleteAllWithPrefix(String prefix) {
        if(prefix == null || root == null)
        {
            Set emptySet = new HashSet<>();
            return emptySet;
        }
        String allCaps = prefix.toUpperCase();
        TrieNode<Value> endOfPrefixNode = this.get(root, allCaps, 0);
        
        deleteAllWithPrefixNode(endOfPrefixNode, allCaps, 0);
        //collectAllChildren(endOfPrefix, allCaps, bigSet);
        //Going to create a new method which is similar to collectAllChildren except it deletes bottom up and collects.
        Set returnSet = new HashSet<>(bigSetForDeleteAll); 
        bigSetForDeleteAll.clear();
        return returnSet;
    }

    private void deleteAllWithPrefixNode(TrieNode<Value> temp, String key, int d) {
        Queue<String> tempList = keysWithPrefix(key);
        int size = tempList.size();
        Stack<String> tempStack = new Stack<String>();
        for (String string : tempList)
        {
            tempStack.push(tempList.remove());
        }
        for(int i = 0; i < size; i ++)
        {
            tempList.add(tempStack.pop());
        }
        for (String string : tempList) {
            if(doesThisWordHaveAValue(string) == true)
            {
                deleteAll(string);
            }
        }
    }

    private boolean doesThisWordHaveAValue(String string)
    {
        TrieNode tempNode = root; 
        String stringToCaps = string.toUpperCase();
        int index = 0;
        for (int depth = 0; depth < stringToCaps.length(); depth++) 
        { 
            index = stringToCaps.charAt(depth); 
            
            if (tempNode.children[index] == null) 
                {
                    break;
                }
                
            tempNode = tempNode.children[index]; 
        }
        Set nSet = tempNode.val;
        if(nSet.size() == 0) return false;
        return true;
    }

    protected Queue<String> keysWithPrefix(String prefix) {
        //use queue so matches closer to trie root get displayed first
        Queue<String> results = new ArrayDeque<String>();
        //find node which represents the prefix
        TrieNode x = this.get(this.root, prefix, 0);
        //collect keys under it
        if(x!= null) 
        {
            this.collect(x, new StringBuilder(prefix), results);
        }
        return results;
    }

    private void collect(TrieNode x, StringBuilder prefix, Queue<String> results) {
        // if this node has a value, add it’s key to the queue
        if (x.val.size() > 0) {
            // add a string made up of the chars from
            // root to this node to the result set
            results.add(prefix.toString());
        }
        // visit each non-null child/link
        for (char c = 0; c < characterSize; c++) {
            if (x.children[c] != null) {
                // add child's char to the string
                prefix.append(c);
                this.collect(x.children[c], prefix, results);
                // remove the child's char to prepare for next iteration
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

} 

    
    
    


