package edu.yu.cs.com1320.project.impl;


import java.util.HashMap;

import edu.yu.cs.com1320.project.MinHeap;

public class MinHeapImpl<E extends Comparable> extends MinHeap {

    public MinHeapImpl(){
        this.count = 0;
        this.elements = (E[]) new Comparable[10];
        this.elementsToArrayIndex = (HashMap<E, Integer>) new HashMap<Comparable, Integer>();
    }

    private Comparable getLeftChild(Comparable parentNode)
    {
        Comparable temp = null;
        int leftChildIndex = 0;
        for (int i = 0; i < elements.length; i++) {
            if(parentNode.equals(elements[i]))
            {
                leftChildIndex = i*2;
                if(leftChildIndex < elements.length)
                {
                    temp = elements[leftChildIndex];
                }
                break;
            }
        }
        return temp;
    }
    private Comparable getRightChild(Comparable parentNode)
    {
        Comparable temp = null;
        int rightChildIndex = 0;
        for (int i = 0; i < elements.length; i++) {
            if(parentNode.equals(elements[i]))
            {
                rightChildIndex = ((i*2)+1);
                if(rightChildIndex < elements.length)
                {
                    temp = elements[rightChildIndex];
                }
                break;
            }
        }
        return temp;
    }
    private boolean isLeaf(int pos) 
    {
        Comparable node = elements[pos];
        if (getLeftChild(node) == null && getRightChild(node) == null) { 
            return true; 
        } 
        return false; 
    } 
    @Override
    public void reHeapify(Comparable node) {

        int pos = getArrayIndex(node);
        // If the node is a non-leaf node and greater
        // than any of its child
        Comparable leftChild = getLeftChild(node);
        Comparable rightChild = getRightChild(node);
        if (!isLeaf(pos)) {
            // Make sure both right and left nodes aren't null
            if (hasBothRightAndLeft(node)) {
                if (node.compareTo(leftChild) > 0 || node.compareTo(rightChild) > 0) {
                    if (leftChild.compareTo(rightChild) < 0) {
                        swap(pos, getArrayIndex(leftChild));
                        reHeapify(node);
                    } else if (leftChild.compareTo(rightChild) > 0) {
                        swap(pos, getArrayIndex(rightChild));
                        reHeapify(node);
                    } else {
                        swap(pos, getArrayIndex(rightChild));
                        reHeapify(node);
                    }
                }
            } else {
                if (getRightChild(node) != null) {
                    swap(pos, getArrayIndex(rightChild));
                    reHeapify(node);
                } else {
                    swap(pos, getArrayIndex(leftChild));
                    reHeapify(node);
                }
            }
        }

    }

    private boolean hasBothRightAndLeft(Comparable node) {
        if(getLeftChild(node) != null && getRightChild(node) != null) return true;
        return false;
    }

    @Override
    protected int getArrayIndex(Comparable node) {
        int returnIndex = -1;
        for (int i = 0; i < elements.length; i++) {
            if(elements[i] == null) continue;
            if (node.equals(elements[i]))
            {
                returnIndex = i;
                break;
            }
        }
        return returnIndex;
    }

    @Override
    protected void doubleArraySize() {
        Comparable[] doubledArray = new Comparable[this.elements.length*2];
        for (int i = 0; i < this.elements.length; i++) {
            doubledArray[i] = this.elements[i];
        }
        this.elements = doubledArray;
    }
    



}