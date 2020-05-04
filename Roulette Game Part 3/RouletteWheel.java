// CS 0401 Fall 2018
// Assignment 4 RouletteWheel skeleton
// You must complete this class so that it runs as specified.
// See WheelTest.java and WheelTest.htm.

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class RouletteWheel extends JPanel implements Runnable
{
	// Need instance data.  Note that fundamentally your
	// RouletteWheel should contain an array of RouletteSquare,
	// initilized to each of the possible values on the wheel.
	private RouletteSquare [] tiles;
	private RouletteSquare tile;
	Activatable thing;
	private final Font jawnt = new Font("Serif", Font.BOLD, 60);

    public RouletteWheel(Activatable X)
    {
       // Create a RouletteWheel.  The argument here is an Activatable
	   // that allows for the method call activate().  This reference
	   // should be stored and the activate() method should be called at
	   // the end of a wheel spin.
	   thing = X;

	   setLayout(new GridLayout(5,8));
	   tiles = new RouletteSquare[37];

	   for(int i = 0; i < tiles.length; i++) // initializes the array of RouletteSquare and adds it to the Panel
	   {
		   tiles[i] = new RouletteSquare(i);
		   tiles[i].setFont(jawnt);
		   add(tiles[i]);
	   }
    }

	public void set()
	{
		// Reinitialize the wheel so that it is ready to spin.
		for(int i = 0; i < tiles.length; i++)
		{
			tiles[i] = new RouletteSquare(i);
			tiles[i].setFont(jawnt);
			add(tiles[i]);
		}
	}
	
	public void spin()
	{
		// Spin the wheel using a Thread and the fact that RouletteWheel
		// implements the Runnable interface.  For an example of using the
		// Runnable interface see RunnableTest.java.  After the run() method
		// completes, the activate() method should be called (using the reference
		// passed in from the constructor).
		new Thread(this).start();
		//thing.activate();
	}
	
	public RouletteResult getResult()
	{
		// Get the selected square (after a completed spin) and return its
		// data as a new RouletteResult object.  If a spin() has not yet been
		// performed this method should return null.  The RouletteResult class
		// was provided in previous assignments.
		RouletteResult result = new RouletteResult(tile.getColor(), tile.getRange(), tile.getParity(), tile.getValue());
		return result;
	}
	
	public void run()
	{
		// implementation of the Runnable interface.  This should select 
		// RouletteSquare objects in a random way for a certain amount of time,
		// so that visually the user sees the wheel "moving".  This is done in
		// the run() method rather than the spin() method directly so that it
		// can be run in a Thread asynchronously.
		long delay = 500;
		long duration = 5000;
		long start = System.nanoTime();
		long end = System.nanoTime();
		long delta = end - start;  // this is the elapsed time
		long durNano = duration * 1000000;  // convert to nanoTime
		// Loop until the elapsed time is more than the requested duration
		while (delta <= durNano)  // keep iterating as long as the elapsed
		{						  // time is less than the duration
			int randy = (int)(Math.random() * 37 + 0);
			tile = tiles[randy];

			tile.choose();
			add(tile);
			
			try 
			{
				Thread.sleep(delay);  // sleep between changes
			}
			catch (InterruptedException e)
			{  
				System.out.println("Problem with Thread!");
			}
			tile.unChoose();

			end = System.nanoTime();  // recalculate elapsed time
			delta = end - start;
		}
		tile.choose();
		//thing.activate();
	}
	
	public int checkBet(RouletteBet b)
	{
		// Same functionality as in Assignment 2.  However, now the result is
		// obtained from whichever RouletteSquare is selected at the end of the
		// spin.
		if(b.getBetType() == RBets.Value && Integer.parseInt(b.getBetValue()) == tile.getValue())
        {
            return 35;
        }
        else if((b.getBetType() == RBets.Color && RColors.valueOf(b.getBetValue()) == tile.getColor()) ||
                (b.getBetType() == RBets.Range && RRanges.valueOf(b.getBetValue()) == tile.getRange()) ||
                (b.getBetType() == RBets.Parity && RParities.valueOf(b.getBetValue()) == tile.getParity()))
        {
            return 1;
        }
        else    
        {
            return 0;
        }
	}						
}