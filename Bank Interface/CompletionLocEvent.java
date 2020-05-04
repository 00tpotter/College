// CS 0445 Spring 2019
// This subclass of CompletionEvent allows the event to store
// an index -- which will correspond to the teller in which
// the event occurs.  You will use this for Assignment 1.  The
// idea is that a CompletionLocEvent will be generated for each
// customer when the customer is "waited on" at a specific teller.

public class CompletionLocEvent extends CompletionEvent
{
	private int location;
	
	public CompletionLocEvent(double new_time, int loc)
	{
		super(new_time);
		location = loc;
	}
	
	public int getLoc()
	{
		return location;
	}
	
	public String toString()
	{
		return super.toString() + " at location " + location;
	}
}