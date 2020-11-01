package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;


public class HashTableImpl<Key,Value> implements HashTable<Key, Value> 
{
    //When the array needs to change size: instantiate an int "n" to be the desired size 
    //and then change the size of the array to be "n"
    //This will move to the constructor (this constructor will have to take a size, or be able to double sizes in further implementation)
    MyLinkedList[] entryArray;
    
    public HashTableImpl()
    {
        this.entryArray = new MyLinkedList[5];
        for (int i = 0; i < entryArray.length; i++)
        {
            entryArray[i] = new MyLinkedList();
        }
        
    }
    
    private int HashFunction(Key myKey)
    {
        return (myKey.hashCode() % this.entryArray.length) & 0x7fffffff;
    }
    
    private class MyLinkedList<Key,Value>
    {
        //Header Node
        Node<Key,Value> header = null;

        public MyLinkedList(){}
        private void setNextNode(Node newNode)
        {
            if(this.header == null)
            {
                this.header = newNode;
            } else 
            {
                Node pNode = null;
                Node cNode = this.header;
                while(cNode != null)
                {
                    pNode = cNode;
                    cNode = cNode.next;
                }
                pNode.next = newNode;
            }
        }

        
    }
    
    private class Node<Key,Value>
    { 
		Key hashCode; 
        Value value;
        Node<Key,Value> next;
        
        public Node(Key mKey, Value mValue) {
                this.hashCode = (Key)(Integer)(mKey.hashCode());
                this.value = mValue;
                this.next = null;  
        }
	} 
    
    /**
     * @param k the key at which to store the value
     * @param v the value to store
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     */
    public Value put(Key k, Value v)
    {
        int id = HashFunction(k);
        Node<Key,Value> newNode = new Node<>(k, v);
        Node previousNode = null;
        Node currentNode = entryArray[id].header;
        
        for(;currentNode != null;currentNode = currentNode.next)
        {
            if(currentNode.hashCode.equals((int)(k.hashCode()))){
                Value oldDoc = (Value)(currentNode.value);
                if(v != null){
                    currentNode.value = v;
                } else{
                    if(previousNode == null)
                    {
                        this.entryArray[id].header = null;
                    } else 
                    {
                        previousNode.next = currentNode.next;
                    }
                }
                return oldDoc;
            }
            previousNode = currentNode;
        }
        if(v != null){
            this.entryArray[id].setNextNode(newNode); 
        } 
        return null;
    }
    

    /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
     */
    public Value get(Key k) {
        int id = HashFunction(k);
        if(this.entryArray[id].header == null){return null;}
        else 
        {
            Node cNode = entryArray[id].header;
            for(;cNode != null;cNode = cNode.next)
            {
                int hs = (int)(cNode.hashCode);
                if(hs == (int)(k.hashCode()))
                {
                    return (Value)(cNode.value);
                }
            }
            return null;
        }
    }

    
    

    
    
    
    
    
}