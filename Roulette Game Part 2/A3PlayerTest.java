// CS 0401 Fall 2018
// Assignment 3 Player Test Program
// You should complete the RoulettePlayer class (which is a modfication
// of RoulettePlayer from Assignment 2) such that this program will compile
// and execute correctly, with output similar to that shown in file 
// A3PlayerTest.txt.  This file also tests the Question class, which is also
// required in Assignment 3.

public class A3PlayerTest
{
	public static void main(String [] args)
	{
		RoulettePlayer p = new RoulettePlayer("Herbert", "Weasel5", 200.0,0.0);
			// Create a new RoulettePlayer with (name, password, cash, debt).  The
			// last field is the amount of money the player has borrowed from the
			// bank (i.e. how much the player owes)
		System.out.println("Player: " + p.toString());
		String name = p.getName();
		double money = p.getMoney();
		System.out.println("Player name: " + name);
		System.out.println("Player money: " + money);
		double change = -50.0;
		while (p.hasMoney())
		{
			p.updateMoney(change);
			System.out.println("Player money is now: " + p.getMoney());
		}
		System.out.println("Player: " + p.toString());
		
		// These statements demonstrate the borrow() and payBack() methods.
		// Note that the amount that is paid back cannot exceed either the
		// amount borrowed or the amount of cash held by the player.  Both of
		// these cases are demonstrated below.  See the output in A3PlayerTest.txt
		p.borrow(100.0);
		System.out.println("Player: " + p.toString());
		p.updateMoney(50.0);
		p.payBack(75.00);
		System.out.println("Player: " + p.toString());
		p.payBack(50.00);
		System.out.println("Player: " + p.toString());
		p.borrow(100.00);
		p.updateMoney(-100.0);
		System.out.println("Player: " + p.toString());
		p.payBack(100.00);
		System.out.println("Player: " + p.toString());
		
		// This shows how Questions can be added to a RoulettePlayer.  These
		// are used to identify a player if he / she forgets his / her password.
		// A RoulettePlayer does not need to have any questions but if they
		// are added there MUST be two of them in a Question array.
		System.out.println();
		p.showAllData();
		Question Q1 = new Question("What is your name?", "Lancelot of Camelot");
		Question Q2 = new Question("What is your quest?", "To seek the Holy Grail");
		Question [] quest = new Question[2];
		quest[0] = Q1;
		quest[1] = Q2;
		p.addQuestions(quest);
		p.showAllData(); System.out.println();
		
		// Testing equals method.  This should return true if both the name and the
		// password match exactly.  Note that p2 here does not even have any values for
		// the money or debt fields.  The equals() method does not care about those fields.
		RoulettePlayer p2 = new RoulettePlayer("Herbert", "Wacky10");
		testEquals(p, p2);
		p2.setPassword("Weasel5");
		testEquals(p, p2);
		System.out.println();

		// The getQuestions() method should return and array of String containing
		// the question part of the questions for the RoulettePlayer.  If the
		// RoulettePlayer has no questions it should return null.
		String [] questOnly = p.getQuestions();
		System.out.println("Here are the questions for " + p.getName());
		for (String X: questOnly)
			System.out.println(X);
		System.out.println();
		
		Question myQ1 = new Question(questOnly[0], "Lancelot of Camelot");
		Question myQ2 = new Question(questOnly[1], "To seek the Holy Grail");
		Question myQ3 = new Question(questOnly[0], "Bedivere the Wise");
		
		quest = new Question[1]; // This won't match since there aren't enough questions
		quest[0] = myQ1;
		checkQuestions(p, quest);
		
		quest = new Question[2]; // This should match since the Q and A for both questions
								 // are the same (test using the equals() method for the
								 // Question class)
		quest[0] = myQ1; quest[1] = myQ2;
		checkQuestions(p, quest);
		
		quest = new Question[3]; 			// This won't match because the answer to the
		quest[0] = myQ3; quest[1] = myQ2;	// first question does not match
		checkQuestions(p, quest);
		System.out.println();
		
		// The toString() method is for the user to see a (somewhat) formatted
		// version of the viewable fields.  The saveString() method simply outputs
		// all of the data in a comma separated form.  The saveString() method
		// should be used when saving the data back to the file.
		System.out.println("Difference between toString() and saveString():");
		System.out.println(p.toString());
		System.out.println(p.saveString());
	}
	
	public static void testEquals(RoulettePlayer first, RoulettePlayer second)
	{
		// Note the call to the equals() method.  This should return true only if the
		// id and password of the objects are equal. Any other data in either object
		// is ignored.
		if (first.equals(second))
			System.out.println("First: " + first.toString() + "  EQUALS  Second: " + second.toString());
		else
			System.out.println("First: " + first.toString() + "  NOT EQ  Second: " + second.toString());
	}
	
	public static void checkQuestions(RoulettePlayer first, Question [] quest)
	{
		// Note the call to the matchQuestions() method in the RoulettePlayer class
		// This method should take an array of Questions as an argument and return
		// true only if the number of questions matches that in the RoulettePlayer and
		// if the questions and answers for each Question match exactly.
		if (first.matchQuestions(quest))
			System.out.println("Questions MATCH");
		else
			System.out.println("Questions DO NOT MATCH");
	}
}
