// CS 0445 Spring 2019
// Read this class and its comments very carefully to make sure you implement
// the class properly.  The data and public methods in this class are identical
// to those MyStringBuilder, with the exception of the two additional methods
// shown at the end.  You cannot change the data or add any instance
// variables.  However, you may (and will need to) add some private methods.
// No iteration is allowed in this implementation. 

// For more details on the general functionality of most of these methods, 
// see the specifications of the similar method in the StringBuilder class.  
public class MyStringBuilder2
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

    // Create a new MyStringBuilder2 initialized with the chars in String s
    // Constructor to make a new MyStringBuilder2 from a String.  The constructor
    // itself is NOT recursive – however, it calls a recursive method to do the
    // actual set up work.  This should be your general approach for all of the
    // methods, since the recursive methods typically need extra parameters that
    // are not given in the specification.
	public MyStringBuilder2(String s)
	{
        if (s != null && s.length() > 0)
            makeBuilderStr(s, 0);
        else  // no String so initialize empty MyStringBuilder2
        {
            length = 0;
            firstC = null;
            lastC = null;
        }
    }
    
    // Recursive method to set up a new MyStringBuilder2 from a String
    private void makeBuilderStr(String s, int pos)
    {
        // Recursive case – we have not finished going through the String
        if(pos < s.length() - 1)
        {
            // Note how this is done – we make the recursive call FIRST, then
            // add the node before it.  In this way the LAST node we add is
            // the front node, and it enables us to avoid having to make a
            // special test for the front node.  However, many of your
            // methods will proceed in the normal front to back way.
            makeBuilderStr(s, pos+1);
            firstC = new CNode(s.charAt(pos), firstC);
            length++;
        }
        else if(pos == s.length() - 1)  // Special case for last char in String
        {                               // This is needed since lastC must be
                                        // set to point to this node
            firstC = new CNode(s.charAt(pos));
            lastC = firstC;
            length = 1;
        }
        else    // This case should never be reached, due to the way the
                // constructor is set up.  However, I included it as a
        {       // safeguard (in case some other method calls this one)
            length = 0;
            firstC = null;
            lastC = null;
        }
    }

	// Create a new MyStringBuilder2 initialized with the chars in array s
	public MyStringBuilder2(char [] s)
	{
        if(s != null && s.length > 0)
        {
            makeBuilderChar(s, 0);
        }
        else
        {
            length = 0;
            firstC = null;
            lastC = null;
        }
    }
    
    private void makeBuilderChar(char [] s, int pos)
    {
        // Recursive case – we have not finished going through the String
        if(pos < s.length - 1)
        {
            // Note how this is done – we make the recursive call FIRST, then
            // add the node before it.  In this way the LAST node we add is
            // the front node, and it enables us to avoid having to make a
            // special test for the front node.  However, many of your
            // methods will proceed in the normal front to back way.
            makeBuilderChar(s, pos+1);
            firstC = new CNode(s[pos], firstC);
            length++;
        }
        else if(pos == s.length - 1)  // Special case for last char in String
        {                               // This is needed since lastC must be
                                        // set to point to this node
            firstC = new CNode(s[pos]);
            lastC = firstC;
            length = 1;
        }
        else    // This case should never be reached, due to the way the
                // constructor is set up.  However, I included it as a
        {       // safeguard (in case some other method calls this one)
            length = 0;
            firstC = null;
            lastC = null;
        }
    }

	// Create a new empty MyStringBuilder2
	public MyStringBuilder2()
	{
        firstC = null;
        lastC = null;
        length = 0;
	}

	// Append MyStringBuilder2 b to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(MyStringBuilder2 b)
	{
        if(length == 0) // special case
        {
            firstC = b.firstC;
            lastC = b.lastC;
            length = b.length;
        }
        else if(b != null && b.length != 0) // normal case
        {
            CNode curr = lastC;
            CNode currBNode = b.firstC;
            appendMSB(b, curr, currBNode);
        }
        return this;
    }
    
    private void appendMSB(MyStringBuilder2 b, CNode curr, CNode currBNode)
    {
        if(currBNode.next == null) // base case, when it reaches the end of the append
        {
            CNode newNode = new CNode(currBNode.data);
            curr.next = newNode;
            curr = newNode;
            length++;
            lastC = curr;
        }
        else if(curr != null) // recursive case
        {
            CNode newNode = new CNode(currBNode.data);
            curr.next = newNode;
            length++;
            appendMSB(b, curr.next, currBNode.next);
        }
    }


	// Append String s to the end of the current MyStringBuilder2, and return
	// the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(String s)
	{
        if(length == 0) // special case
        {
            makeBuilderStr(s, 0);
        }
        else if(s != null && s.length() != 0) // normal case
        {
            CNode curr = lastC;
            appendStr(s, curr, 0);
        }
        
        return this;
    }
    
    private void appendStr(String s, CNode curr, int pos)
    {
        if(pos == s.length()-1) // base case, when it reaches the end of the append
        {
            CNode newNode = new CNode(s.charAt(pos));
            curr.next = newNode;
            length++;
            lastC = curr.next;
        }
        else if(curr != null) // recursive case
        {
            CNode newNode = new CNode(s.charAt(pos));
            curr.next = newNode;
            length++;
            appendStr(s, curr.next, pos+1);
        }
    }

	// Append char array c to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(char [] c)
	{
        if(length == 0) // special case
        {
            makeBuilderChar(c, 0);
        }
        else if(c != null && c.length != 0) // normal case
        {
            CNode curr = lastC;
            appendChar(c, curr, 0);
        }
        return this;
    }
    
    private void appendChar(char [] c, CNode curr, int pos)
    {
        if(pos == c.length-1) // base case, when it reaches the end of the append
        {
            CNode newNode = new CNode(c[pos]);
            curr.next = newNode;
            length++;
            lastC = curr.next;
        }
        else if(curr != null) // recursive case
        {
            CNode newNode = new CNode(c[pos]);
            curr.next = newNode;
            length++;
            appendChar(c, curr.next, pos+1);
        }
    }

	// Append char c to the end of the current MyStringBuilder2, and
	// return the current MyStringBuilder2.  Be careful for special cases!
	public MyStringBuilder2 append(char c)
	{
        if(length == 0)
        {
            char [] newThing = {c};
            makeBuilderChar(newThing, 0);
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

	// Return the character at location "index" in the current MyStringBuilder2.
	// If index is invalid, throw an IndexOutOfBoundsException.
	public char charAt(int index)
	{
        CNode curr = firstC;
		if(index >= 0 && index < length)
		{
			return charAtRec(index, curr, 0);
		}
        throw new IndexOutOfBoundsException();
    }
    
    private char charAtRec(int index, CNode curr, int pos)
    {
        if(index == pos)
        {
            return curr.data;
        }
        return charAtRec(index, curr.next, pos+1);// potentially return the method call
    }

	// Delete the characters from index "start" to index "end" - 1 in the
	// current MyStringBuilder2, and return the current MyStringBuilder2.
	// If "start" is invalid or "end" <= "start" do nothing (just return the
	// MyStringBuilder2 as is).  If "end" is past the end of the MyStringBuilder2, 
	// only remove up until the end of the MyStringBuilder2. Be careful for 
	// special cases!
	public MyStringBuilder2 delete(int start, int end)
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
            nodeAfterEnd = traverseToIndex(curr, end+1, 0);
            firstC = nodeAfterEnd;
		}
		else // normal case deleting from somewhere in the middle
		{
            nodeBeforeStart = traverseToIndex(curr, start, 0);
            nodeAfterEnd = traverseToIndex(nodeBeforeStart.next, end+2, start+1);
            nodeBeforeStart.next = nodeAfterEnd;
        }
        length -= end - start;
        return this;
    }

    private CNode traverseToIndex(CNode curr, int index, int pos)
    {
        if((pos == index-1 && pos < length) || curr == null) 
        {
            return curr;
        }
        return traverseToIndex(curr.next, index, pos+1);
    }

	// Delete the character at location "index" from the current
	// MyStringBuilder2 and return the current MyStringBuilder2.  If "index" is
	// invalid, do nothing (just return the MyStringBuilder2 as is).
	// Be careful for special cases!
	public MyStringBuilder2 deleteCharAt(int index)
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
		}
		else // normal case deleting from middle of linked list
		{
			nodeBeforeIndex = traverseToIndex(curr, index, 0);
            curr = nodeBeforeIndex.next;
            nodeAfterIndex = curr.next;
			nodeBeforeIndex.next = nodeAfterIndex;
        }
        length--;
		
		return this;
    }

	// Find and return the index within the current MyStringBuilder2 where
	// String str first matches a sequence of characters within the current
	// MyStringBuilder2.  If str does not match any sequence of characters
	// within the current MyStringBuilder2, return -1.  Think carefully about
	// what you need to do for this method before implementing it.
	public int indexOf(String str)
	{
        int loc = 0;
		int index = -1;
		int tries = 0;
        CNode curr = firstC;
		if(curr != null && loc < length) 
		{
			index = indexOfRec(str, loc, index, tries, curr);
		}
		return index;
    }
    
    private int indexOfRec(String str, int loc, int index, int tries, CNode curr)
    {
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
    
        if(curr.next != null)
        {
            index = indexOfRec(str, loc+1, index, tries, curr.next);
        }
        return index;
    }

	// Insert String str into the current MyStringBuilder2 starting at index
	// "offset" and return the current MyStringBuilder2.  if "offset" == 
	// length, this is the same as append.  If "offset" is invalid
	// do nothing.
	public MyStringBuilder2 insert(int offset, String str)
	{
        CNode curr = firstC;
        CNode temp;
        
        if(curr != null && offset > 0 && offset < length) // change later
        {
            curr = traverseToIndex(curr, offset, 0);
            temp = curr.next;
            curr = insertStrRec(offset, str, curr, 0);
            curr.next = temp;
        }
        else if(offset == 0)
        {
            temp = firstC;
            firstC = new CNode(str.charAt(0));
            curr = firstC;
            curr = insertStrRec(offset, str, curr, 1);
            curr.next = temp;
        }
        else if(offset == length)
        {
            curr = lastC;
            appendStr(str, curr, 0);
        }
        return this;
    }
    
    private CNode insertStrRec(int offset, String str, CNode curr, int pos)
    {
        if(pos == str.length())
        {
            length += str.length();
        }
        else
        {
            CNode newNode = new CNode(str.charAt(pos));
            curr.next = newNode;
            return insertStrRec(offset, str, curr.next, pos+1);
        }
        return curr;
    }

	// Insert character c into the current MyStringBuilder2 at index
	// "offset" and return the current MyStringBuilder2.  If "offset" ==
	// length, this is the same as append.  If "offset" is invalid, 
	// do nothing.
	public MyStringBuilder2 insert(int offset, char c)
	{
        CNode curr = firstC;
        CNode temp;
        
        if(curr != null && offset > 0 && offset < length) // change later
        {
            curr = traverseToIndex(curr, offset, 0);
            temp = curr.next;
            CNode newNode = new CNode(c);
			temp = curr.next;
			curr.next = newNode;
			curr = newNode;
			curr.next = temp;
			length++;
        }
        else if(offset == 0)
        {
            temp = firstC;
			firstC = new CNode(c);
			firstC.next = temp;
			length++;
        }
        else if(offset == length)
        {
            CNode newNode = new CNode(c);
            lastC.next = newNode;
            lastC = newNode;
            length++;
        }
        return this;
    }

	// Insert char array c into the current MyStringBuilder2 starting at index
	// index "offset" and return the current MyStringBuilder2.  If "offset" is
	// invalid, do nothing.
	public MyStringBuilder2 insert(int offset, char [] c)
	{
        CNode curr = firstC;
        CNode temp;
        
        if(curr != null && offset > 0 && offset < length) // change later
        {
            curr = traverseToIndex(curr, offset, 0);
            temp = curr.next;
            curr = insertCharArrRec(offset, c, curr, 0);
            curr.next = temp;
        }
        else if(offset == 0)
        {
            temp = firstC;
            firstC = new CNode(c[0]);
            curr = firstC;
            curr = insertCharArrRec(offset, c, curr, 1);
            curr.next = temp;
        }
        else if(offset == length)
        {
            curr = lastC;
            appendChar(c, curr, 0);
        }
        return this;
    }
    
    private CNode insertCharArrRec(int offset, char [] c, CNode curr, int pos)
    {
        if(pos == c.length)
        {
            length += c.length;
        }
        else
        {
            CNode newNode = new CNode(c[pos]);
            curr.next = newNode;
            return insertCharArrRec(offset, c, curr.next, pos+1);
        }
        return curr;
    }

	// Return the length of the current MyStringBuilder2
	public int length()
	{
        return length;
	}

	// Delete the substring from "start" to "end" - 1 in the current
	// MyStringBuilder2, then insert String "str" into the current
	// MyStringBuilder2 starting at index "start", then return the current
	// MyStringBuilder2.  If "start" is invalid or "end" <= "start", do nothing.
	// If "end" is past the end of the MyStringBuilder2, only delete until the
	// end of the MyStringBuilder2, then insert.  This method should be done
	// as efficiently as possible.  In particular, you may NOT simply call
	// the delete() method followed by the insert() method, since that will
	// require an extra traversal of the linked list.
	public MyStringBuilder2 replace(int start, int end, String str)
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
        
        if(start == 0)
        {
            nodeAfterEnd = traverseToIndex(curr, end, 0);
            firstC = new CNode(str.charAt(0));
            curr = firstC;
            curr = insertStrRec(start, str, curr, 1);
            curr.next = nodeAfterEnd;
			length -= end - start;
			length += str.length();
        }
        else
        {
            nodeBeforeStart = traverseToIndex(curr, start, 0);
            nodeAfterEnd = traverseToIndex(nodeBeforeStart, end+2, start);
            nodeBeforeStart.next = new CNode(str.charAt(0));
            curr = nodeBeforeStart.next;
            curr = insertStrRec(start, str, curr, 1);
            curr.next = nodeAfterEnd;
            length -= end - start;
			length += str.length();
        }
        
        return this;
    }

	// Reverse the characters in the current MyStringBuilder2 and then
	// return the current MyStringBuilder2.
	public MyStringBuilder2 reverse()
	{
        CNode previous = null;
		CNode curr = firstC;
		CNode next = null;
        int loc = 0;
        
        reverseRec(previous, curr, next, loc);
        return this;
    }
    
    private MyStringBuilder2 reverseRec(CNode previous, CNode curr, CNode next, int pos)
    {
        if(curr != null && pos < length)
        {
            next = curr.next;
            curr.next = previous;
            previous = curr;
            curr = next;
            firstC = previous;
            reverseRec(previous, curr, next, pos+1);
       }        
       
       return this;
    }
	
	// Return as a String the substring of characters from index "start" to
	// index "end" - 1 within the current MyStringBuilder2
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
        
        newSub = substringRec(start, end, curr, loc, newSub);
        return newSub;
    }
    
    private String substringRec(int start, int end, CNode curr, int pos, String newSub)
    {
        if(curr != null && pos < length)
        {
            if(pos >= start && pos < end)
            {
                newSub += this.charAt(pos);
            }
            
            return substringRec(start, end, curr.next, pos+1, newSub);
        }
        return newSub;
    }

    // Again note that the specified method is not actually recursive – rather it
    // calls a recursive method to do the work.  Note that in this case we also
    // create a char array before the recursive call, then pass it as a
    // parameter, then construct and return a new string from the char array.
    // Carefully think about the parameters you will be passing to your recursive
    // methods.  Through them you must be able to move through the list and
    // reduce the "problem size" with each call.
	// Return the entire contents of the current MyStringBuilder2 as a String
	public String toString()
	{
        char [] c = new char[length];
        getString(c, 0, firstC);
        return (new String(c));
    }
    
    // Here we need the char array to store the characters, the pos value to
    // indicate the current index in the array and the curr node to access
    // the data in the actual MyStringBuilder2.  Note that these rec methods
    // are private – the user of the class should not be able to call them.
    private void getString(char [] c, int pos, CNode curr)
    {
        if(curr != null)
        {
            c[pos] = curr.data;
            getString(c, pos+1, curr.next);
        }
    }

	// Find and return the index within the current MyStringBuilder2 where
	// String str LAST matches a sequence of characters within the current
	// MyStringBuilder2.  If str does not match any sequence of characters
	// within the current MyStringBuilder2, return -1.  Think carefully about
	// what you need to do for this method before implementing it.  For some
	// help with this see the Assignment 3 specifications.
	public int lastIndexOf(String str)
	{
        int loc = 0;
        int index = -1;
        int tries = 0;
        CNode curr = firstC;
        if(curr != null && loc < length) 
        {
            index = lastIndexOfRec(str, loc, index, tries, curr);
        }
        return index;
    }
    
    private int lastIndexOfRec(String str, int loc, int index, int tries, CNode curr)
    {
        if(curr == null)
        {
            return index;
        }
        
        int someIndex = lastIndexOfRec(str, loc+1, index, tries, curr.next);
        
        if(someIndex == -1)
        {
            index = indexOfRec(str, loc, index, tries, curr);
            return index;
        }
        else
        {
            index = someIndex;
        }
        
        return index;
    }

	
	// Find and return an array of MyStringBuilder2, where each MyStringBuilder2
	// in the return array corresponds to a part of the match of the array of
	// patterns in the argument.  If the overall match does not succeed, return
	// null.  For much more detail on the requirements of this method, see the
	// Assignment 3 specifications.
	public MyStringBuilder2 [] regMatch(String [] pats)
	{
        MyStringBuilder2 [] matches = new MyStringBuilder2[pats.length];
        initMatches(matches, 0);
        CNode curr = firstC;
        if(regMatchRec(pats, curr, 0, matches))
        {
            System.out.println();
            return matches;
        }
        return null;
    }

    private void initMatches(MyStringBuilder2 [] matches, int pos)
    {
        if(pos < matches.length)
        {
            matches[pos] = new MyStringBuilder2();
            initMatches(matches, pos+1);
        }
    }
    
    private boolean regMatchRec(String [] pats, CNode curr, int pos, MyStringBuilder2 [] matches)
    {
        boolean result = false;
        // State 1 - no characters in pattern 0 match
        if(matches[0].length() == 0)
        {
            // case a.
            if(curr == null)
            {
                result = false;
            }
            // case c.
            else if(pats[0].indexOf(curr.data) != -1)
            {
                matches[0].append(curr.data);
                result = regMatchRec(pats, curr.next, pos, matches);

                if(!result && curr.next != null)
                {
                    matches[0].deleteCharAt(matches[0].length()-1);
                    result = backtrackReg(pats, curr, pos, matches);
                }                
            }
            // case b.
            else
            {
                result = regMatchRec(pats, curr.next, pos, matches);
            }
        }
        // State 2 - at least one character in pattern pos matches
        else if(matches[pos].length() > 0)
        {
            // case a.
            if(curr != null && pats[pos].indexOf(curr.data) != -1)
            {
                matches[pos].append(curr.data);
                result = regMatchRec(pats, curr.next, pos, matches);

                if(!result)
                {
                    matches[pos].deleteCharAt(matches[pos].length()-1);
                    result = regMatchRec(pats, curr, pos+1, matches);
                }
            }
            // case b.
            else if(curr != null && pats[pos].indexOf(curr.data) == -1 && pos == matches.length - 1)
            {
                result = true;
            }
            // case c. 
            else if(curr != null && pats[pos].indexOf(curr.data) == -1 && pos != matches.length - 1)
            {
                result = regMatchRec(pats, curr, pos+1, matches);
            }
            else
            {
                if(pos == matches.length-1)
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
            }
        }
        // State 3 - no characters in pattern pos matches where pos != 0
        else if(matches[pos].length() == 0)
        {
            // case a.
            if(pats[pos].indexOf(curr.data) != -1)
            {
                matches[pos].append(curr.data);
                result = regMatchRec(pats, curr.next, pos, matches);

                if(curr.next != null) // backtrack and recurse
                {
                    if(!result)
                    {
                        matches[pos].firstC = null;
                        matches[pos].lastC = null;
                        matches[pos].length = 0;
                    }

                }
            }
            // case b.
            else
            {
                result = false;
            }
        }
        return result;
    }

    private boolean backtrackReg(String [] pats, CNode curr, int pos, MyStringBuilder2 [] matches)
    {
        boolean result = false;
        matches[pos].firstC = null;
        matches[pos].lastC = null;
        matches[pos].length = 0;

        if(!result && curr.next != null) // backtrack and recurse
        {
            if(pats[0].indexOf(curr.data) != -1)
            {
                matches[0].append(curr.data);
                result = regMatchRec(pats, curr.next, pos, matches);
            }
            
            result = backtrackReg(pats, curr.next, pos, matches);
        }
        return result;
    }
	
	// You must use this inner class exactly as specified below.  Note that
	// since it is an inner class, the MyStringBuilder2 class MAY access the
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
