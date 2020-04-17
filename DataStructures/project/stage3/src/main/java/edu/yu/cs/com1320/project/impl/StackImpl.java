package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack<T> {
    Node root = null;
    public StackImpl() {
    }

    /**
     * @param element object to add to the Stack
     */
    @Override
    public void push(T element) {
        Node topNode = new Node<>();
        topNode.value = element;
        Node tempNode = root;
        if (root != null) {
            while (root.next != null) {
                root = root.next;
            }
        }
        topNode.next = tempNode;
        root = topNode;
    }

    /**
     * removes and returns element at the top of the stack
     * 
     * @return element at the top of the stack, null if the stack is empty
     */
    @Override
    public T pop() {
        if (root == null) {
            return null;
        } else {
            Node tempNode = root;
            root = root.next;
            return (T) tempNode.value;
        }
    }

    /**
     *
     * @return the element at the top of the stack without removing it
     */
    @Override
    public T peek() {
        if (root == null) {
            return null;
        } else {
            return (T) root.value;
        }
    }

    /**
     *
     * @return how many elements are currently in the stack
     */
    @Override
    public int size() {
        int counter = 0;
        Node tempNode = root.next;
        while (tempNode != null) {
            counter++;
            tempNode = tempNode.next;
        }
        return counter;
    }

}