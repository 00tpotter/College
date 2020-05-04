// CS 0401 Fall 2018
// Assignment 2 Test Program
// You should complete the RouletteWheel class such that
// this program will compile and execute correctly, with output similar to
// that shown in file RouletteTest.txt.  Note, however, that since the
// spinWheel() method uses random numbers, your output will not match mine
// exactly.  See some more comments below.
// See also RouletteBet.java and RouletteResult.java

public class RouletteTest
{
	public static void main(String [] args)
	{
		RouletteWheel wheel = new RouletteWheel();
		
		// Note that the values for all of the bets are Strings
		RouletteBet bet1 = new RouletteBet(RBets.Value, "13");
		RouletteBet bet2 = new RouletteBet(RBets.Value, "24");
		RouletteBet bet3 = new RouletteBet(RBets.Color, "Red");
		RouletteBet bet4 = new RouletteBet(RBets.Color, "Black");
		RouletteBet bet5 = new RouletteBet(RBets.Parity, "Even");
		RouletteBet bet6 = new RouletteBet(RBets.Parity, "Odd");
		RouletteBet bet7 = new RouletteBet(RBets.Range, "Low");
		RouletteBet bet8 = new RouletteBet(RBets.Range, "High");
		
		for (int i = 0; i < 50; i++)
		{
			RouletteResult X = wheel.spinWheel();
			System.out.println("Spin result: " + X);
			testBet(wheel, bet1);
			testBet(wheel, bet2);
			testBet(wheel, bet3);
			testBet(wheel, bet4);
			testBet(wheel, bet5);
			testBet(wheel, bet6);
			testBet(wheel, bet7);
			testBet(wheel, bet8);
			System.out.println("\n");
		}
		
		// This demonstrates the error that will occur if your bet value is not
		// appropriate for the type.  Note below that the type of the bet is
		// Color but the value is "Even", which is not a valid RColor.  The
		// RouletteBet is created and output but the checkBet() method throws
		// an exception.
		System.out.println("About to crash with bogus bet");
		bet1 = new RouletteBet(RBets.Color, "Even");
		System.out.println("Bogus bet is " + bet1);
		int ans = wheel.checkBet(bet1);
	}

	public static void testBet(RouletteWheel w, RouletteBet b)
	{
		System.out.println("Bet: " + b);
		// The checkBet() method will compare the bet type and value with the
		// result of the wheel spin.  It will return one of three values:
		// 0 -- the bet was a loser.
		// 1 -- the bet was an "even money" winner -- this is the case if the
		//      bet was a Color winner, a Parity winner or a Range winner
		// 35 - the bet was a big winner (35x the bet) -- this is the case if
		//      the bet was a Value winner
		
		int res = w.checkBet(b);
		if (res == 0)
			System.out.println("Losing bet!  Sorry");
		else if (res == 1)
			System.out.println("Even money winner!");
		else if (res == 35)
			System.out.println("Big winner!");
	}
}