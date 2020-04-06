package edu.yu.cs.com1320.project.stage1;

public class MyLinkedList {
    public class Node { 
		int data; 
		Node next; 

        Node(int data) 
        { 
            this.data = data; 
        } 
	} 
    Node head; // head of list 

    protected Node getNextNode()
    {
        return this.head.next;
    }
    //When you're awake write the code so that it should keep moving 
    //to the next node until there is a null and place the new Node there.
    //Look at the slides
    protected void setNextNode(Node next)
    {
        Node currentNode = this.head;
        if(this.head == null)
        {
            //currentNode = next;
        } else 
        {
            //currentNode.next = 
            //this.head.next 
        }
    }
	/* Linked list Node*/
	
}
