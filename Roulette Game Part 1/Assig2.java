// main game program

import java.util.*;
import java.io.*;

public class Assig2
{
    public static void main(String [] args) throws IOException
    {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String playerName = input.nextLine();
        File newPlayer = new File(playerName + ".txt");
        if(newPlayer.exists())
        {
            Scanner playerFile = new Scanner(newPlayer);
            //PrintWriter playerFile2 = new PrintWriter(playerName);
            double playerAmount = playerFile.nextDouble();
            RoulettePlayer player1 = new RoulettePlayer(playerName, playerAmount);
            if(player1.hasMoney())
            {
                System.out.println("Welcome back, " + player1.getName() + "!");
                System.out.print("Enter the amount of money you would like to bet (You have " + player1.getMoney() + " dollars) or 0 to quit: ");
                int rounds = 0;
                double startingMoney = player1.getMoney();
                double betAmount = input.nextDouble();
                while(betAmount != 0)
                {
                    while(betAmount > player1.getMoney() || betAmount < 0)
                    {
                        System.out.print("Enter a valid bet: ");
                        betAmount = input.nextDouble();
                    }

                    System.out.print("Enter the type of bet you would like (Value, Color, Range, Parity): ");
                    String betTypeString = input.next();
                    RBets betType = null;
                    String betValue = "";
                    
                    if(betTypeString.equals("Value"))
                    {
                        betType = RBets.Value;
                        System.out.print("Enter the value you'd like to bet on: ");
                        betValue = input.next();
                    }
                    else if(betTypeString.equals("Color"))
                    {
                        betType = RBets.Color;
                        System.out.print("Enter the color you'd like to bet on: ");
                        betValue = input.next();
                    }
                    else if(betTypeString.equals("Range"))
                    {
                        betType = RBets.Range;
                        System.out.print("Enter the range you'd like to bet on: ");
                        betValue = input.next();
                    }
                    else if(betTypeString.equals("Parity"))
                    {
                        betType = RBets.Parity;
                        System.out.print("Enter the parity you'd like to bet on: ");
                        betValue = input.next();
                    }
                    RouletteBet newBet = new RouletteBet(betType, betValue);
                    RouletteWheel newWheel = new RouletteWheel();
                    RouletteResult result = newWheel.spinWheel();
                    System.out.println("And the wheel landed on... " + result.toString());
                    if(newWheel.checkBet(newBet) == 35)
                    {
                        player1.updateMoney(35.0 * betAmount);
                        System.out.println("Congrats! You won " + (betAmount * 35.0) + " dollars!");
                    }
                    else if(newWheel.checkBet(newBet) == 1)
                    {
                        player1.updateMoney(1.0 * betAmount);
                        System.out.println("Congrats! You won " + (betAmount * 1.0) + " dollars!");
                    }
                    else
                    {
                        player1.updateMoney(-betAmount);
                        System.out.println("Whoops! You lost your bet of " + betAmount + " dollars! Better luck next time!");
                    }

                    if(player1.hasMoney())
                    {
                        System.out.print("Enter the amount of money you would like to bet (You have " + player1.getMoney() + " dollars) or 0 to quit: ");
                        betAmount = input.nextDouble();
                    }
                    else
                    {
                        betAmount = 0;
                    }
                    
                    rounds++;
                }
                System.out.println("Looks like your game is over!");
                System.out.println("Here are your final results: ");
                System.out.println("\tRounds played: " + rounds);
                System.out.println("\tStarting money: " + startingMoney);
                System.out.println("\tFinal money: " + player1.getMoney());
                System.out.println("\tNet Winnings: " + (player1.getMoney() - startingMoney));    
            }
            else
            {
                System.out.println("Come back when you have money, " + player1.getName() + ".");
            }
            playerFile2.print("");
            playerFile2.println(player1.getMoney());
            playerFile2.close();
            playerFile.close();
        }     
        else
        {
            newPlayer.createNewFile();
            System.out.print("Enter the amount of money you have: ");
            double playerAmount = input.nextDouble();
            PrintWriter playerFile = new PrintWriter(newPlayer);
            playerFile.println(playerAmount);
            RoulettePlayer player1 = new RoulettePlayer(playerName, playerAmount);
            if(player1.hasMoney())
            {
                System.out.println("Welcome, " + player1.getName() + "!");
                System.out.print("Enter the amount of money you would like to bet (You have " + player1.getMoney() + " dollars) or 0 to quit: ");
                int rounds = 0;
                double startingMoney = player1.getMoney();
                double betAmount = input.nextDouble();
                while(betAmount != 0)
                {
                    while(betAmount > player1.getMoney() || betAmount < 0)
                    {
                        System.out.print("Enter a valid bet: ");
                        betAmount = input.nextDouble();
                    }

                    System.out.print("Enter the type of bet you would like (Value, Color, Range, Parity): ");
                    String betTypeString = input.next();
                    RBets betType = null;
                    String betValue = "";
                    
                    if(betTypeString.equals("Value"))
                    {
                        betType = RBets.Value;
                        System.out.print("Enter the value you'd like to bet on: ");
                        betValue = input.next();
                    }
                    else if(betTypeString.equals("Color"))
                    {
                        betType = RBets.Color;
                        System.out.print("Enter the color you'd like to bet on: ");
                        betValue = input.next();
                    }
                    else if(betTypeString.equals("Range"))
                    {
                        betType = RBets.Range;
                        System.out.print("Enter the range you'd like to bet on: ");
                        betValue = input.next();
                    }
                    else if(betTypeString.equals("Parity"))
                    {
                        betType = RBets.Parity;
                        System.out.print("Enter the parity you'd like to bet on: ");
                        betValue = input.next();
                    }
                    RouletteBet newBet = new RouletteBet(betType, betValue);
                    RouletteWheel newWheel = new RouletteWheel();
                    RouletteResult result = newWheel.spinWheel();
                    System.out.println("And the wheel landed on... " + result.toString());
                    if(newWheel.checkBet(newBet) == 35)
                    {
                        player1.updateMoney(35.0 * betAmount);
                        System.out.println("Congrats! You won " + (betAmount * 35.0) + " dollars!");
                    }
                    else if(newWheel.checkBet(newBet) == 1)
                    {
                        player1.updateMoney(1.0 * betAmount);
                        System.out.println("Congrats! You won " + (betAmount * 1.0) + " dollars!");
                    }
                    else
                    {
                        player1.updateMoney(-betAmount);
                        System.out.println("Whoops! You lost your bet of " + betAmount + " dollars! Better luck next time!");
                    }

                    if(player1.hasMoney())
                    {
                        System.out.print("Enter the amount of money you would like to bet (You have " + player1.getMoney() + " dollars) or 0 to quit: ");
                        betAmount = input.nextDouble();
                    }
                    else
                    {
                        betAmount = 0;
                    }
                    
                    rounds++;
                }
                System.out.println("Looks like your game is over!");
                System.out.println("Here are your final results: ");
                System.out.println("\tRounds played: " + rounds);
                System.out.println("\tStarting money: " + startingMoney);
                System.out.println("\tFinal money: " + player1.getMoney());
                System.out.println("\tNet Winnings: " + (player1.getMoney() - startingMoney));   
                playerFile.println(player1.getMoney());
            }
            else
            {
                System.out.println("Come back when you have money, " + player1.getName() + ".");
            }
            //playerFile.flush();
            //playerFile.println(player1.getMoney());
            playerFile.close();
        }
    }
}