// CS 0401 Fall 2018
// Assignment 2 Player Test Program
// You should complete the RoulettePlayer class such that
// this program will compile and execute correctly, with output similar to
// that shown in file PlayerTest.txt.

public class PlayerTest
{
	public static void main(String [] args)
	{
		RoulettePlayer p = new RoulettePlayer("Herbert", 200.0);
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
	}
}