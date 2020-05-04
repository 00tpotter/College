// CS 0445 Spring 2019
// Read this class and its comments very carefully to make sure you implement
// the class properly.  Note the items that are required and that cannot be
// altered!  Generally speaking you will implement your MyStringBuilder using
// a singly linked list of nodes.  See more comments below on the specific
// requirements for the class.

// For more details on the general functionality of most of these methods, 
// see the specifications of the similar method in the StringBuilder class.  
public class MyStringBuilder
{
	// These are the only three instance variables you are allowed to have.
	// See details of CNode class below.  In other words, you MAY NOT add
	// any additional instance variables to this class.  However, you may
	// use any method variables that you need within individual methods.
	// But remember that you may NOT use any variables of any other
	// linked list class or of the predefined StringBuilder or 
	// or StringBuffer class in any place in your code.  You may only use the
	// String class where it is an argument or return type in a method.
	private CNode firstC;	// reference to front of list.  This reference is necessary
							// to keep track of the list
	private CNode lastC; 	// reference to last node of list.  This reference is
							// necessary to improve the efficiency of the append()
							// method
	private int length;  	// number of characters in the list

	// You may also add any additional private methods that you need to
	// help with your implementation of the public methods.

	// Create a new MyStringBuilder initialized with the chars in String s
	public MyStringBuilder(String s)
	{
		if(s == null || s.length() == 0) // checks validity of parameter, sets everything to null if it's not valid
		{
			firstC = null;
			lastC = null;
			length = 0;
		}
		else	// creates a new node for each character in the string and connects them
		{
			firstC = new CNode(s.charAt(0));
			length = 1;
			CNode curr = firstC;

			for(int i = 1; i < s.length(); i++)
			{
				CNode newNode = new CNode(s.charAt(i));
				curr.next = newNode;
				curr = newNode;
				length++;
			}
			lastC = curr;
		}
	}

	// Create a new MyStringBuilder initialized with the chars in array s
    public MyStringBuilder(char [] s)
	{
		if(s == null || s.length == 0)	// checks validity of parameter
		{
			firstC = null;
			lastC = null;
			length = 0;
		}
		else // creates a new node for each character in the array and connects them
		{
			firstC = new CNode(s[0]);
			length = 1;
			CNode curr = firstC;

			for(int i = 1; i < s.length; i++)
			{
				CNode newNode = new CNode(s[i]);
				curr.next = newNode;
				curr = newNode;
				length++;
			}
			lastC = curr;
		}
	}

	// Create a new empty MyStringBuilder
    public MyStringBuilder()
	{
        firstC = null;
		lastC = null;
		length = 0;
	}

	// Append MyStringBuilder b to the end of the current MyStringBuilder, and
	// return the current MyStringBuilder.  Be careful for special cases!
	public MyStringBuilder append(MyStringBuilder b)
	{
		if(b != null && b.length != 0)	// checks validity of parameter
		{
			// simply adds each node from b and puts them at the end of the current MyStringBuilder
			CNode curr = lastC;
			CNode currBNode = b.firstC;
			for(int i = 0; i < b.length(); i++)
			{
				CNode newNode = new CNode(currBNode.data);
				curr.next = newNode;
				currBNode = currBNode.next;
				curr = newNode;
				length++;
			}
			lastC = curr;
		}		
		else if(length == 0)
		{
			firstC = b.firstC;
			lastC = b.lastC;
			length = b.length;
		}

		return this;
	}


	// Append String s to the end of the current MyStringBuilder, and return
	// the current MyStringBuilder.  Be careful for special cases!
	public MyStringBuilder append(String s)
	{
		CNode curr = lastC;
		if(s != null && s.length() != 0 && curr != null)	// checks validity of parameter
		{
			// creates a new node for each character in the string and connects them to the end of the current MyStrinBuilder
			for(int i = 0; i < s.length(); i++)
			{
				CNode newNode = new CNode(s.charAt(i));
				curr.next = newNode;
				curr = newNode;
				length++;
			}
			lastC = curr;
		}		
		else if(length == 0)	// if the current object is empty, it makes a new one (essentially like the constructor)
		{
			if(s == null || s.length() == 0)
			{
				firstC = null;
				lastC = null;
				length = 0;
			}
			else
			{
				firstC = new CNode(s.charAt(0));
				length = 1;
				CNode otherCurr = firstC;

				for(int i = 1; i < s.length(); i++)
				{
					CNode newNode = new CNode(s.charAt(i));
					otherCurr.next = newNode;
					otherCurr = newNode;
					length++;
				}
				lastC = curr;
			}		
		}

		return this;
	}

	// Append char array c to the end of the current MyStringBuilder, and
	// return the current MyStringBuilder.  Be careful for special cases!
	public MyStringBuilder append(char [] c)
	{
		if(c != null && c.length != 0) // checks validity of parameter
		{
			// creates a new node for each character in the array and adds it to the end of the current MyStringBuilder
			CNode curr = lastC;
			for(int i = 0; i < c.length; i++)
			{
				CNode newNode = new CNode(c[i]);
				curr.next = newNode;
				curr = newNode;
				length++;
			}
			lastC = curr;
		}
		else if(length == 0) // if the current object is empty, it makes a new one (essentially like the constructor)
		{
			if(c == null || c.length == 0)
			{
				firstC = null;
				lastC = null;
				length = 0;
			}
			else
			{
				firstC = new CNode(c[0]);
				length = 1;
				CNode curr = firstC;

				for(int i = 1; i < c.length; i++)
				{
					CNode newNode = new CNode(c[i]);
					curr.next = newNode;
					curr = newNode;
					length++;
				}
				lastC = curr;
			}	
		}

		return this;
	}

	// Append char c to the end of the current MyStringBuilder, and
	// return the current MyStringBuilder.  Be careful for special cases!
	public MyStringBuilder append(char c)
	{
		if(length == 0)
		{
			char [] newThing = {c};
			if(newThing == null || newThing.length == 0)
			{
				firstC = null;
				lastC = null;
				length = 0;
			}
			else
			{
				firstC = new CNode(newThing[0]);
				length = 1;
				CNode curr = firstC;

				for(int i = 1; i < newThing.length; i++)
				{
					CNode newNode = new CNode(newThing[i]);
					curr.next = newNode;
					curr = newNode;
					length++;
				}
				lastC = curr;
			}
		}
		else
		{
			CNode newNode = new CNode(c);	
			lastC.next = newNode;
			lastC = newNode;
			length++;
		}		
		
		return this;
	}

	// Return the character at location "index" in the current MyStringBuilder.
	// If index is invalid, throw an IndexOutOfBoundsException.
	public char charAt(int index)
	{
		CNode curr = firstC;
		if(index >= 0 && index < length)
		{
			for(int i = 0; i < index; i++)
			{
				curr = curr.next;
			}
			return curr.data;
		}
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}

	// Delete the characters from index "start" to index "end" - 1 in the
	// current MyStringBuilder, and return the current MyStringBuilder.
	// If "start" is invalid or "end" <= "start" do nothing (just return the
	// MyStringBuilder as is).  If "end" is past the end of the MyStringBuilder, 
	// only remove up until the end of the MyStringBuilder. Be careful for 
	// special cases!
	public MyStringBuilder delete(int start, int end)
	{
		CNode curr = firstC;
		CNode nodeBeforeStart = null;
		CNode nodeAfterEnd = null;

		if(start < 0 || end <= start) // checks that start and end are valid arguments
		{
			return this;
		}
		else if(end >= length) // if the end is past the length of the MyStringBuilder, just make the end the length
		{
			end = length;
		}

		if(start == 0)	// special case for if you're deleting from the front
		{
			for(int i = start; i < end; i++)
			{
				curr = curr.next;
			}
			nodeAfterEnd = curr;
			firstC = nodeAfterEnd;
			length -= end - start;
		}
		else // normal case deleting from somewhere in the middle
		{
			for(int i = 0; i < start - 1; i++)
			{
				curr = curr.next;
			}
			nodeBeforeStart = curr;
			for(int i = start; i <= end; i++)
			{
				curr = curr.next;
			}
			nodeAfterEnd = curr;
			nodeBeforeStart.next = nodeAfterEnd;
			length -= end - start;
		}
		
		return this;
	}

	// Delete the character at location "index" from the current
	// MyStringBuilder and return the current MyStringBuilder.  If "index" is
	// invalid, do nothing (just return the MyStringBuilder as is).
	// Be careful for special cases!
	public MyStringBuilder deleteCharAt(int index)
	{
		CNode curr = firstC;
		CNode nodeBeforeIndex = null;
		CNode nodeAfterIndex = null;

		if(index < 0 || index >= length) // checks validity of index
		{
			return this;
		}
		else if(index == 0) // special case deleting from first node
		{
			firstC = firstC.next;
			length--;
		}
		else // normal case deleting from middle of linked list
		{
			for(int i = 0; i < index-1; i++)
			{
				curr = curr.next;
			}
			nodeBeforeIndex = curr;
			curr = curr.next;
			nodeAfterIndex = curr.next;
			nodeBeforeIndex.next = nodeAfterIndex;
			length--;
		}
		
		return this;
	}

	// Find and return the index within the current MyStringBuilder where
	// String str first matches a sequence of characters within the current
	// MyStringBuilder.  If str does not match any sequence of characters
	// within the current MyStringBuilder, return -1.  Think carefully about
	// what you need to do for this method before implementing it.
	public int indexOf(String str)
	{
		int loc = 0;
		int index = -1;
		int tries = 0;
		CNode curr = firstC;
		while(curr != null && loc < length) 
		{
			// if consecutive characters in the linked list match the str, then return that index
			if(str.charAt(tries) == curr.data) 
			{
				tries++;
			}
			else
			{
				tries = 0;
			}
		
			if(tries == str.length())
			{
				index = loc - str.length() + 1;
				return index;
			}
			curr = curr.next;
			loc++;
		}
		return index;
	}

	// Insert String str into the current MyStringBuilder starting at index
	// "offset" and return the current MyStringBuilder.  if "offset" == 
	// length, this is the same as append.  If "offset" is invalid
	// do nothing.
	public MyStringBuilder insert(int offset, String str)
	{
		CNode curr = firstC;
		int loc = 0;

		while(curr != null && loc < length && offset > 0 && offset < length) // checking validity of offset
		{
			if(loc == offset-1)	// normal case (must insert from node before offset)
			{
				CNode temp = curr.next;
				for(int i = 0; i < str.length(); i++)
				{
					CNode newNode = new CNode(str.charAt(i));
					curr.next = newNode;
					curr = newNode;
					length++;
				}
				curr.next = temp;
			}

			curr = curr.next;
			loc++;
		}

		if(offset == 0) // special case if inserting at the first node
		{
			CNode temp = firstC;
			firstC = new CNode(str.charAt(0));
			curr = firstC;
			for(int i = 1; i < str.length(); i++)
			{
				CNode newNode = new CNode(str.charAt(i));
				curr.next = newNode;
				curr = newNode;
				length++;
			}
			curr.next = temp;
			length++;
		}

		if(offset == length) // special case if inserting at the end, same as the append method
		{
			if(str == null || str.length() == 0)
			{
				firstC = null;
				lastC = null;
				length = 0;
			}
			else
			{
				firstC = new CNode(str.charAt(0));
				length = 1;
				curr = firstC;
	
				for(int i = 1; i < str.length(); i++)
				{
					CNode newNode = new CNode(str.charAt(i));
					curr.next = newNode;
					curr = newNode;
					length++;
				}
				lastC = curr;
			}
		}

		return this;
	}

	// Insert character c into the current MyStringBuilder at index
	// "offset" and return the current MyStringBuilder.  If "offset" ==
	// length, this is the same as append.  If "offset" is invalid, 
	// do nothing.
	public MyStringBuilder insert(int offset, char c)
	{
		CNode curr = firstC;

		if(offset < 0 || offset >= length) // checks validity of offset
		{
			return this;
		}
		else if(offset == length)	// special case if inserting at the end
		{
			CNode newNode = new CNode(c);	
			lastC.next = newNode;
			lastC = newNode;
			length++;
		}
		else if(offset == 0) // special case inserting at first node
		{
			CNode temp = firstC;
			firstC = new CNode(c);
			firstC.next = temp;
			length++;
		}
		else // normal case inserting in the middle somewhere
		{
			for(int i = 0; i < offset; i++)
			{
				curr = curr.next;
			}
			CNode newNode = new CNode(c);
			CNode temp = curr.next;
			curr.next = newNode;
			curr = newNode;
			curr.next = temp;
			length++;
		}

		return this;
	}

	// Insert char array c into the current MyStringBuilder starting at index
	// index "offset" and return the current MyStringBuilder.  If "offset" is
	// invalid, do nothing.
	public MyStringBuilder insert(int offset, char [] c)
	{
		CNode curr = firstC;
		int loc = 0;

		while(curr != null && loc < length && offset > 0 && offset < length) // checks validity
		{
			if(loc == offset-1) // normal case for inserting
			{
				CNode temp = curr.next;
				for(int i = 0; i < c.length; i++)
				{
					CNode newNode = new CNode(c[i]);
					curr.next = newNode;
					curr = newNode;
					length++;
				}
				curr.next = temp;
			}

			curr = curr.next;
			loc++;
		}

		if(offset == 0) // special case inserting at first node
		{
			CNode temp = firstC;
			firstC = new CNode(c[0]);
			curr = firstC;
			for(int i = 1; i < c.length; i++)
			{
				CNode newNode = new CNode(c[i]);
				curr.next = newNode;
				curr = newNode;
				length++;
			}
			curr.next = temp;
			length++;
		}

		if(offset == length)	// inserting at the end, the same as the append method
		{
			if(c == null || c.length == 0)
			{
				firstC = null;
				lastC = null;
				length = 0;
			}
			else
			{
				firstC = new CNode(c[0]);
				length = 1;
				curr = firstC;
	
				for(int i = 1; i < c.length; i++)
				{
					CNode newNode = new CNode(c[i]);
					curr.next = newNode;
					curr = newNode;
					length++;
				}
				lastC = curr;
			}
		}

		return this;
	}

	// Return the length of the current MyStringBuilder
	public int length()
	{
		return length;
	}

	// Delete the substring from "start" to "end" - 1 in the current
	// MyStringBuilder, then insert String "str" into the current
	// MyStringBuilder starting at index "start", then return the current
	// MyStringBuilder.  If "start" is invalid or "end" <= "start", do nothing.
	// If "end" is past the end of the MyStringBuilder, only delete until the
	// end of the MyStringBuilder, then insert.  This method should be done
	// as efficiently as possible.  In particular, you may NOT simply call
	// the delete() method followed by the insert() method, since that will
	// require an extra traversal of the linked list.
	public MyStringBuilder replace(int start, int end, String str)
	{
		CNode curr = firstC;
		CNode nodeBeforeStart = null;
		CNode nodeAfterEnd = null;

		if(start < 0 || end <= start) // checks validity of start and end
		{
			return this;
		}
		else if(end >= length) // once again, if it goes past the length of the object, just make the end the length
		{
			end = length;
		}

		if(start == 0) // special case for replacing from the first node
		{
			for(int i = 0; i < end; i++)
			{
				curr = curr.next;
			}
			nodeAfterEnd = curr;
			firstC = new CNode(str.charAt(0));
			curr = firstC;
			for(int i = 1; i < str.length(); i++)
			{
				CNode newNode = new CNode(str.charAt(i));
				curr.next = newNode;
				curr = newNode;
			}
			curr.next = nodeAfterEnd;
			length -= end - start;
			length += str.length();
		}
		else // normal case replacing in the middle
		{
			for(int i = 0; i < start - 1; i++)
			{
				curr = curr.next;
			}
			nodeBeforeStart = curr;
			for(int i = start; i <= end; i++)
			{
				curr = curr.next;
			}
			nodeAfterEnd = curr;
			nodeBeforeStart.next = new CNode(str.charAt(0));
			curr = nodeBeforeStart.next;
        	for(int i = 1; i < str.length(); i++)
			{
				CNode newNode = new CNode(str.charAt(i));
				curr.next = newNode;
				curr = newNode;
			}
			curr.next = nodeAfterEnd;
			length -= end - start;
			length += str.length();
		}
		
		return this;
	}

	// Reverse the characters in the current MyStringBuilder and then
	// return the current MyStringBuilder.
	public MyStringBuilder reverse()
	{
		CNode previous = null;
		CNode curr = firstC;
		CNode next = null;
		int loc = 0;

		while(curr != null && loc < length)
		{
			next = curr.next;
			curr.next = previous;
			previous = curr;

			curr = next;
			loc++;
		}
		firstC = previous;

		return this;
	}
	
	// Return as a String the substring of characters from index "start" to
	// index "end" - 1 within the current MyStringBuilder
	public String substring(int start, int end)
	{
		CNode curr = firstC;
		String newSub = "";
		int loc = 0;

		if(start < 0 || end <= start)
		{
			return this.toString();
		}
		else if(end >= length)
		{
			end = length;
		}

		while(curr != null && loc < length)
		{
			if(loc >= start && loc < end)
			{
				newSub += this.charAt(loc);
			}

			curr = curr.next;
			loc++;
		}
		
		return newSub;
	}

	// Return the entire contents of the current MyStringBuilder as a String
	public String toString()
	{
		char [] c = new char[length];
		CNode curr = firstC;
		for(int i = 0; i < length; i++)
		{
			c[i] += curr.data;
			curr = curr.next;
		}
		
		return new String(c);
	}

	// You must use this inner class exactly as specified below.  Note that
	// since it is an inner class, the MyStringBuilder class MAY access the
	// data and next fields directly.
	private class CNode
	{
		private char data;
		private CNode next;

		public CNode(char c)
		{
			data = c;
			next = null;
		}

		public CNode(char c, CNode n)
		{
			data = c;
			next = n;
		}
	}
}