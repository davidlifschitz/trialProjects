package edu.yu.cs.com1320.project.impl;

import java.util.Iterator;

public class MyLinkedList<Key, Value> implements Iterable
    {
        //Header Node
        Node<Key,Value> header = null;
        public MyLinkedList()
        {}
        
        public void setNextNode(Node newNode)
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


        public class MyLinkedListIterator implements Iterator
        {
            Node current = null;

            @Override
            public boolean hasNext() {
                if(current == null && header != null)
                {
                    return true;
                }
                else if(current != null) 
                {
                    return current.getNext() != null;
                }
                return false;
            }

            @Override
            public Node next() {
                if(current == null && header != null)
                {
                    current = header;
                    return header;
                } else if(current != null)
                {
                    current = current.next;
                    return current;
                }
                else 
                {
                    throw new IllegalArgumentException();
                }
                
            }
            
        }

        @Override
        public Iterator iterator() {
            return (Iterator) new MyLinkedListIterator();
        }

        
    }
    
    