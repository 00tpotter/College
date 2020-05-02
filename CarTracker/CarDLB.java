import java.util.*;

public class CarDLB
{
    private Node head;
    private int size;

    public CarDLB()
    {
        head = new Node();
        size = 0;
    }

    // For reading in string from the file and creating a DLB out of them
    public void insert(String s, int index)
    {
        Node newNode;  
        Node curr = head;
        s = s + '^';

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

        if(curr.value == '^' && curr.next == null)
        {
            curr.next = new Node(index);
        }

        size++;
    }   

    // Inserting into the DLB, only to be used by the set of all Make and Model
    public void insertMM(String s, Car key, char mode)
    {
        Node newNode;  
        Node curr = head;
        s = s + '^';

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
            if(curr.value == c && curr.next != null && curr.value != '^')
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

        if(curr.value == '^' && curr.next == null)
        {
            curr.next = new Node();
            curr = curr.next;
            
            curr.makeModel = new CarPQ(15, mode);
            curr.makeModel.insert(curr.makeModel.size(), key);
        }
        else if(curr.value == '^' && curr.next != null)
        {
            curr = curr.next;
            curr.makeModel.insert(curr.makeModel.size(), key);
        }

        size++;
    }   

    // Searching a DLB
    public int search(String key)
    {
        Node curr = head;
        int index = -1;
        
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
        }
        
        if(curr.value == '^')
        {
            curr = curr.next;
            index = curr.key;
        }    

        return index;
    }

    // Searching a DLB, only to be used by the set of all Make and Model
    public CarPQ searchMM(String key)
    {
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
        }
        
        if(curr.value == '^')
        {
            curr = curr.next;
        }    
        return curr.makeModel;
    }

    public int size()
    {
        return size;
    }
    
    private class Node
    {
        char value;
        private Node next;
        private Node nextRight;
        private int key;
        private CarPQ makeModel;

        public Node()
        {
            value = ' ';
            next = null;
            nextRight = null;
            key = -1;
            makeModel = null;
        }

        public Node(char v)
        {
            value = v;
            next = null;
            nextRight = null;
            key = -1;
            makeModel = null;
        }

        public Node(int i)
        {
            value = ' ';
            next = null;
            nextRight = null;
            key = i;
            makeModel = null;
        }
    }
}