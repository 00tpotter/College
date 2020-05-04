// CS 0401 Fall 2018
// Demonstration of Enum types in Java.  This should help you with Assignment 2.
// For the (very simple) enum type definitions see files:
// RColors.java, RRanges.java, RParities.java and RBets.java
// You will need these types in your various other class files.

import java.util.*;
public class EnumDemo
{
	public static void main(String [] args)
	{
			
		Scanner inScan = new Scanner(System.in);
		System.out.println("Please enter the type of your bet: ");
		String bType = inScan.next();
		// We can convert a String into an enum value using the valueOf() method
		RBets betType = RBets.valueOf(bType);
		System.out.println("Your bet type is " + betType);
		
		// We can access a specific value within an enum type using the dot notation
		if (betType == RBets.Color)
			System.out.println("You chose to bet on a color");
		else
			System.out.println("You did NOT choose to be on a color");
			
		// We can iterate through the values of an enum type
		System.out.print("Here are the Colors: ");
		for (RColors c: RColors.values())
			System.out.print(c + " ");
		System.out.println();

		System.out.print("Here are the Ranges: ");
		for (RRanges r: RRanges.values())
			System.out.print(r + " ");
		System.out.println();
		
		System.out.print("Here are the Parities: ");
		for (RParities p: RParities.values())
			System.out.print(p + " ");
		System.out.println();
		
		// Note that the the user is just being prompted for an arbitrary value
		// In your program you should show the correct possible values to the user
		// based on the type of the bet
		System.out.println("Please enter your value: ");
		String betVal = inScan.next();
		
		// Note:  Since the bet value is a String, the user can really enter anything
		// for the bet value and the RouletteBet object will still be created without
		// error.  However, if the value does not match the possible values for the
		// type of the bet, the checkBet() method in the RouletteWheel class should
		// crash with an exception.  To see this see the end of the RouletteTest.java
		// program.
		RouletteBet bet = new RouletteBet(betType, betVal);
		System.out.println("Your bet is " + bet);
	}
}