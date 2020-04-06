package edu.yu.cs.com1320.project.impl;

public class Node<Key, Value> {
    Key hashCode;
    Value value;
    Node<Key, Value> next;

    protected Node() {
    }

    protected Node(Key mKey, Value mValue) {
        this.hashCode = (Key) (Integer) (mKey.hashCode());
        this.value = mValue;
        this.next = null;
    }

    protected Node getNext() {
        return next;
    }
}