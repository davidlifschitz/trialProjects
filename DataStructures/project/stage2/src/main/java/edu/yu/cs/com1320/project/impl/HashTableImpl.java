package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.MyLinkedList.MyLinkedListIterator;

public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {
    // When the array needs to change size: instantiate an int "n" to be the desired
    // size
    // and then change the size of the array to be "n"
    // This will move to the constructor (this constructor will have to take a size,
    // or be able to double sizes in further implementation)
    MyLinkedList[] entryArray;
    int counter;

    public HashTableImpl() {
        this.entryArray = new MyLinkedList[5];
        for (int i = 0; i < entryArray.length; i++) {
            entryArray[i] = new MyLinkedList();
        }
    }

    private int HashFunction(Key myKey) {
        int hashCode = myKey.hashCode();
        int returnInt = (hashCode % this.entryArray.length) & 0x7fffffff;
        return returnInt;
    }

    /**
     * @param k the key at which to store the value
     * @param v the value to store
     * @return if the key was already present in the HashTable, return the previous
     *         value stored for the key. If the key was not already present, return
     *         null.
     */
    public Value put(Key k, Value v) {
        // Setting up variables to play with
        int id = HashFunction(k);
        Node<Key, Value> newNode = new Node<>(k, v);
        Node previousNode = null;
        Node currentNode = entryArray[id].header;
        // Dealing with load factor and arrayDoubling
        int loadFactor = counter / this.entryArray.length;
        if (loadFactor >= 4) {
            this.doubleArray();
        }
        // Logic for adding/deleting a node
        for (; currentNode != null; currentNode = currentNode.next) {
            if (currentNode.hashCode.equals((int) (k.hashCode()))) {
                Value oldDoc = (Value) (currentNode.value);
                if (v != null) {
                    currentNode.value = v;
                    counter++;
                } else {
                    if (previousNode == null) {
                        this.entryArray[id].header = null;
                    } else {
                        previousNode.next = currentNode.next;
                    }
                    counter--;
                }
                return oldDoc;
            }
            previousNode = currentNode;
        }
        if (v != null) {
            this.entryArray[id].setNextNode(newNode);
            counter++;
        }
        return null;
    }

    /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is
     *         no such key in the table
     */
    public Value get(Key k) {
        int id = HashFunction(k);
        if (this.entryArray[id].header == null) {
            return null;
        } else {
            Node cNode = entryArray[id].header;
            for (; cNode != null; cNode = cNode.next) {
                int hs = (int) (cNode.hashCode);
                if (hs == (int) (k.hashCode())) {
                    return (Value) (cNode.value);
                }
            }
            return null;
        }
    }

    /*
     * Doubling Array Method This should only occur when the load factor > 4
     * //meaning that the (total number of nodes that exist)/length of array is > 4
     */
    private void doubleArray() {
        MyLinkedList[] tempArray = this.entryArray;
        this.entryArray = new MyLinkedList[this.entryArray.length * 2];
        for (int j = 0; j < this.entryArray.length; j++) {
            this.entryArray[j] = new MyLinkedList<>();
        }
        for (int i = 0; i < tempArray.length; i++) {
            MyLinkedListIterator newLinkedListIterator = tempArray[i].new MyLinkedListIterator();
            for (; newLinkedListIterator.hasNext();) {
                Node newNode = newLinkedListIterator.next();
                Key key = (Key) (Integer) (newNode.hashCode());
                int index = HashFunction(key);
                this.put((Key) (Integer) (index), (Value) (newNode.value));
                counter--;
            }
        }
        this.entryArray = tempArray;
    }

}