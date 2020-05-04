// CS 0401 Fall 2018
// Assignment 3 List Test Program
// You should complete the RPList class such that this program will compile
// and execute correctly, with output similar to that shown in file 
// A3ListTest.txt.  See extensive comments about the various required methods
// and functionality of the RPList class.
//
// Important Note:  This program will modify the contents of file players.txt.
// The trace output of this program in file A3ListTest.txt shows two runs executed
// in sequence -- be sure to run yours twice when testing it.  Also be sure to copy
// the players.txt file to a temp file before running this program.  That way you 
// can restore it to the original before submitting.  You should submit this program
// with the original, provided version of players.txt.

import java.util.*;
public class A3ListTest
{
	public static void main(String [] args)
	{
		// Class RPList must be an array-based data structure which can store
		// RoulettePlayer objects.  You may not use any predefined Java collection
		// classes within your RPList class -- you must store your data in an array
		// of RoulettePlayer.  See more information on the required methods and
		// functionality below.
		
		RPList R = new RPList("players.txt");  // Create an RPList using the data
				// from file players.txt as input.  The underlying array in the
				// RPList should initially be exactly the correct size to fit the data
				// (i.e. logical size == physical size).  The format of the data in
				// players.txt is as follows:
				// 		First line is an int indicating the number of players in the file
				//		Each subsequent line represents one player, and is formatted as follows:
				// <Name>,<Password>,<Current Cash>,<Amount Borrowed>,<Q1 Question>,<Q1 Answer>,<Q2 Question>,<Q2 Answer>
				// 		The questions and answers are optional, but if they are present, there
				// 		must be two of each.  Also, no commas may appear in the questions or
				//		answers (since the file is a csv).
				// The file name should also be saved in the RPList object so it can be
				// re-written later.
		System.out.println(R.toString()); // Uses the toString() method for each
				// RoulettePlayer within the RPList.  Note the output does not include
				// the password or the questions.
		System.out.println("Number of players: " + R.getSize());  // logical size
		System.out.println("Size of array: " + R.getASize());	  // physical size
				// This method would not normally exist for this class since it
				// reveals the nature of the underlying data in the RPList.  However,
				// it is helpful for grading so it is included here.
		System.out.println();
		
		// Demonstrating getting a RoulettePlayer from the RPList using the id and
		// password.  See more details in the testIdPassword() method below.
		testIdPassword(R, "Westley", "DreadPirate"); // Should succeed
		testIdPassword(R, "Inigo", "Hello");	// Should fail (password does not match)
		testIdPassword(R, "Buttercup", "AsYouWish");	// Should file (id not found)
		
		// Demonstrating adding a new player to the RPList.  The add() method should
		// automatically double the size of the array within the RPList if necessary
		// (when the physical array is filled).
		// Note that a player that is a duplicate of an existing id will not be addded.
		RoulettePlayer P = new RoulettePlayer("Buttercup", "AsYouWish", 200.0, 0);
		boolean success = R.add(P);  // Should be succcessful.  Since the original
				// RPList was sized to exactly fit the data in the players.txt file,
				// this add() will require the array size to be doubled. 
		if (success)
			testIdPassword(R, "Buttercup", "AsYouWish");
		else
			System.out.println("Buttercup not added");
		success = R.add(P);  // Should NOT be successful since Buttercup is already
		if (success)         // in the list
			testIdPassword(R, "Buttercup", "AsYouWish");
		else
			System.out.println("Buttercup not added");
		System.out.println();
		
		System.out.println(R.toString());
		System.out.println("Number of players: " + R.getSize());
		System.out.println("Size of array: " + R.getASize());
		System.out.println();
		
		// Demonstrating getting a RoulettePlayer from the list by answering the
		// questions.  Note that first we must get the "question part" of the questions.
		// We then add the answers, creating an array of Question.  This is then passed
		// in to the getPlayerQuestions() method.
		Question [] QandA = new Question[2];
		String [] Qonly;
		Qonly = R.getQuestions("Westley");  // This method will return the "question part"
				// of the questions for a given player.  If the RoulettePlayer does not
				// exist or if the RoulettePlayer does not have any questions, the method
				// should return null.
		System.out.println("Questions for Westley:");
		for (String X: Qonly)
			System.out.println(X);
		System.out.println();
		// Now we will form some Q / A pairs that we will try in order to get
		// the RoulettePlayer Westley from the list.
		Question Q1 = new Question(Qonly[0], "They do not exist");
		Question Q2 = new Question(Qonly[1], "No");
		Question Q3 = new Question(Qonly[1], "Yes");
		QandA[0] = Q1; QandA[1] = Q2;
		// This should access Westley since the Q and A both match
		testQuestions(R, "Westley", QandA);
		
		// This should not access Fezzik since the Q and A do not match those of
		// Fezzik (in fact Fezzik does not even have any Questions)
		testQuestions(R, "Fezzik", QandA);
		
		// This should not access Westley since one of the answers is incorrect.
		QandA[1] = Q3;
		testQuestions(R, "Westley", QandA);
		
		// Save the RPList back to the file.  Note that this main method does
		// not "throws IOException" so your saveList() method cannot do this either.
		// This is not hard to handle.  See FileTest2.java.  Don't forget to write
		// the size of the list first to the file.
		R.saveList();
	}
	
	// Note the methods that are tested below
	public static void testIdPassword(RPList R, String id, String pass)
	{
		boolean foundId = R.checkId(id);  // checkId() should return true if the
						// id is found within the RPList and false otherwise
		if (foundId)
		{
			System.out.println("Id: " + id + " was found");
			RoulettePlayer P = R.getPlayerPassword(id, pass);  // this method should
				// return the RoulettePlayer within the RPList whose id and password
				// match the arguments.  If no such player exists it should return
				// null.
				// Important Note:  We discussed in lecture the difference between
				// returning a reference to an object within a type and returning
				// a copy of an object.  In the RPList class your getPlayerPassword()
				// and getPlayerQuestions() [see below] methods should both return a
				// reference to the original object within the RPList.  We are doing
				// it this way so that mutations to the RoulettePlayer (ex: adding or
				// subtracting money, changing password, etc) will be reflected in
				// the RPList.  Clearly there are some concerns with implementing it
				// in this way (as discussed) but this is how we will do it in this
				// assignment.
			if (P != null)
			{
				System.out.println("Legal credentials for: " + P.getName());
				System.out.println("Updating money...");
				// Mutating the RoulettePlayer
				P.updateMoney(100);
				P.showAllData(); // This is another method that is useful for grading.
						// It should show all of the data for the player.  This method
						// is also demonstrated in A3PlayerTest.java
			}
			else
				System.out.println("Password does not match for " + id);
		}
		else
			System.out.println("Id: " + id + " was not found");
		System.out.println();
	}
	
	// Note the methods tested below.  
	public static void testQuestions(RPList R, String id, Question [] quest)
	{
		boolean foundId = R.checkId(id);  // Once again checking id only
		if (foundId)
		{
			System.out.println("Id: " + id + " was found");
			RoulettePlayer P = R.getPlayerQuestions(id, quest);  // This method
				// will return a RoulettePlayer within the RPList whose Question
				// objects exactly match those in the argument Question array.
				// It should utilize the equals() method in the Question class.
			if (P != null)
			{
				System.out.println("Legal credentials for: " + P.getName());
				System.out.println("Updating password...");
				// Mutating the RoulettePlayer
				P.setPassword("ToBlave");
				P.showAllData();
			}
			else
				System.out.println("Questions do not match for " + id);
		}
		else
			System.out.println("Id: " + id + " was not found");
		System.out.println();
	}	
}
