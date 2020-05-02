// My implementation of Linked List data structure made from scratch
// Will be used to create a DLB
import java.util.*;

public class MyLinkedList
{
    private Node head;

    public MyLinkedList()
    {
        head = new Node();
    }

    // For reading in string from the file and creating a DLB out of them
    public void insert(String s)
    {
        Node newNode;  
        Node curr = head;

        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            // For starting at the head when there is no character, 
            // so it only builds down from the head on the first entry 
            // which should just be "A"
            if(curr.value == ' ' && curr.next != null)
            {
                curr = curr.next;
            }  

            // Check if a node needs to be added going down
            if(curr.next == null && curr.value != '^')
            {
                newNode = new Node(c);
                curr.next = newNode;
                curr = curr.next;
                continue;
            }
            // Check if a node needs to be added going right
            else if(curr.value != c && curr.nextRight == null)
            {
                newNode = new Node(c);
                curr.nextRight = newNode;
                curr = curr.nextRight;
                continue;
            }

            // Continue traversing the DLB going down
            if(curr.value == c && curr.next != null)
            {
                curr = curr.next;
                continue;
            }
            // Continue traversing the DLB going right
            else if(curr.value != c && curr.nextRight != null)
            {
                curr = curr.nextRight;
                i--; // crucial for connecting the prefixes with the rest of the word
            }
        }
    }   

    public ArrayList<String> search(String key)
    {
        ArrayList<String> q = new ArrayList<String>();
        Node curr = head;
        
        for(int i = 0; i < key.length(); i++)
        {
            char c = key.charAt(i);

            // For starting at the head when there is no character, 
            // so it only builds down from the head on the first entry 
            // which should just be "A"
            if(curr.value == ' ' && curr.next != null)
            {
                curr = curr.next;
            }  
            
            // Continue traversing the DLB going down
            if(curr.value == c && curr.next != null)
            {
                curr = curr.next;
                continue;
            }
            // Continue traversing the DLB going right
            else if(curr.value != c && curr.nextRight != null)
            {
                curr = curr.nextRight;
                i--; // crucial for connecting the prefixes with the rest of the word
            }
            else // if the character of the key doesn't exist, don't add anything to the list and just return it
            {
                return q;
            }
        }
        // Call recursive function to search the list
        searchRec(curr, key, q);
    
        return q;
    }

    // Recursive function to find 5 words in the DLB that match the prefix 
    private void searchRec(Node curr, String prefix, ArrayList<String> q)
    {
        // Base case
        if(curr == null || q.size() >= 5)
        {
            return;
        }

        // Reach the end of the word? Add it to the list!
        if(curr.value == '^')
        {
            q.add(prefix);
        }

        // Recursive cases
        searchRec(curr.next, prefix + curr.value, q);
        searchRec(curr.nextRight, prefix, q);
    }


    
    private class Node
    {
        char value;
        private Node next;
        private Node nextRight;

        public Node()
        {
            value = ' ';
            next = null;
            nextRight = null;
        }

        public Node(char v)
        {
            value = v;
            next = null;
            nextRight = null;
        }
    }
}