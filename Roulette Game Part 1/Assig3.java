// Assig 3

import java.util.*;

import javax.lang.model.util.ElementScanner6;

import java.io.*;

public class Assig3
{
    public static void main(String args[]) throws IOException
    {
        RPList jawn = new RPList(args[0]);
        
        Scanner input = new Scanner(System.in);

        System.out.println("Hello! Welcome to Some Roulette Game!");
        System.out.print("Are you a returning player or nah? ('Yes/No' or 'Quit' to Quit)");

        while((!input.nextLine().equals("Quit")))
        {
            if(input.nextLine().equals("Yes"))
            {
                System.out.print("Enter your name: ");
                String name = input.nextLine();
                System.out.print("Enter your password: ")
                String password = input.nextLine();
                if(returnee(jawn, name, password, input))
                {
                    // Play the game as the returnee
                    playGame(jawn.getPlayerPassword(name, password), input);
                }
                else
                {
                    // create a new user and play the game from there
                    RoulettePlayer newbie = newUser(input);
                    jawn.add(newbie);
                    playGame(newbie, input);
                }
            }
            else
            {
                // create a new user and play game from there
                RoulettePlayer newbie = newUser(input);
                jawn.add(newbie);
                playGame(newbie, input);
            }
            System.out.println("Hello! Welcome to Some Roulette Game!");
            System.out.print("Are you a returning player or nah? ('Yes/No' or 'Quit' to Quit)");
        }        
    }

    public static void playGame(RoulettePlayer player1, Scanner input) throws IOException
    {
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
            
            // Ask if they would like to pay back debt if they have money remaining
            if(player1.hasMoney() && player1.getDebt() > 0)
            {
                System.out.print("Would you like to pay off some or all of your debts? (Yes/No)");
                if(input.nextLine().equals("Yes"))
                {
                    System.out.print("Enter the amount you would like to pay back: ");
                    player1.payBack(input.nextDouble());
                }
            }
        }
        else
        {
            System.out.println("Would you like to borrow some money, " + player1.getName() + "? (Yes/No)");
            if(input.nextLine().equals("Yes"))
            {
                if(player1.getDebt() >= 500)
                {
                    System.out.println("You cannot borrow anymore and you're out of money! Have a good day!");
                }
                else
                {
                    System.out.print("Enter the amount you would like to borrow (cannot exceed $500.00): ");
                    double borrowAmount = input.nextDouble();
                    while(borrowAmount > 500)
                    {
                        System.out.print("Enter enter a valid amount (cannot exceed $500.00): ");
                        borrowAmount = input.nextDouble();
                    }
                    player1.borrow(borrowAmount);
                }
            }
        }
    }

    public static RoulettePlayer newUser(Scanner input)
    {
        System.out.print("Enter your name: ");
        String name = input.nextLine();
        System.out.print("Create a password: ");
        String password = input.nextLine();
        System.out.print("Reenter your password: ");
        String matchPass = input.nextLine();
        while(!(matchPass.equals(password)))
        {
            System.out.print("Create a password: ");
            String password = input.nextLine();
            System.out.print("Reenter your password: ");
            String matchPass = input.nextLine();
        }

        System.out.print("Enter your initial money: ");
        double money = input.nextDouble();

        RoulettePlayer newPlayer = new RoulettePlayer(name, password, money, 0.0);
        System.out.print("Would you like to add security questions? (Yes/No)");
        if(input.nextLine().equals("Yes"))
        {
            System.out.print("Enter your first question: ");
            String q1 = input.nextLine();
            System.out.print("Enter your first answer: ");
            String a1 = input.nextLine();
            System.out.print("Enter your second question: ");
            String q2 = input.nextLine();
            System.out.print("Enter your second answer: ");
            String a2 = input.nextLine();

            Question quest1 = new Question(q1, a1);
            Question quest2 = new Question(q2, a2);
            Question [] quests = new Question[2];
            quests[0] = quest1;
            quests[1] = quest2;
            newPlayer.addQuestions(quests);
        }
        return newPlayer;        
    }

    public static boolean returnee(RPList rList, String name, String pass, Scanner input)
    {
        if(rList.getPlayerPassword(name, pass) == null)
        {
            System.out.print("Enter your password: ")
            String p = input.nextLine();
            if(rList.getPlayerPassword(name, p) == null && rList.getQuestions() != null)
            {
                Question [] QandA = new Question[2];
                String [] qOnly;
                qOnly = rList.getQuestions(name);
                System.out.println("Questions for " + name + ": ");
                for (String q: qOnly)
                {
                    System.out.println(q);
                }
                System.out.print("Answer for question 1: ");
                String answer1 = input.nextLine();
                System.out.print("Answer for question 1: ");
                String answer2 = input.nextLine();

                Question q1 = new Question(qOnly[0], answer1);
                Question q2 = new Question(qOnly[1], answer2);
                QandA[0] = q1;
                QandA[1] = q2;

                if(rList.getPlayerQuestions(name, QandA) != null)
                {
                    RoulettePlayer P = R.getPlayerQuestions(name, QandA)
                    System.out.print("Please update your password: ");
                    String newPass = input.nextLine();

                    P.setPassword(newPass);
                    return true;
                }
                else
                {
                    System.out.println("Cannot retrieve your account. You must create a new account to proceed.");
                }
            }
        }
        return false;
    }
}









        /*String playerName = input.nextLine();
        File playerFile = new File(playerName + ".txt");    // a new File object to store the text file stuffs

        if(playerFile.exists())     // Case if the file for the player already exists or not
        {
            RoulettePlayer player1 = new RoulettePlayer(playerName, existingPlayer(playerFile));
            playGame(player1, input);
            updateFile(playerFile, player1);
        }
        else
        {
            RoulettePlayer player1 = new RoulettePlayer(playerName, newPlayer(playerFile, playerName, input));
            playGame(player1, input);
            updateFile(playerFile, player1);
        }
    }

    public static double existingPlayer(File playerFile) throws IOException
    {
        Scanner fileInput = new Scanner(playerFile);
        double playerAmount = fileInput.nextDouble();
        return playerAmount;    
    }

    public static double newPlayer(File playerFile, String playerName, Scanner input) throws IOException
    {
        playerFile.createNewFile();
        System.out.print("Enter the amount of money you have: ");
        double playerAmount = input.nextDouble();
        PrintWriter newFileInfo = new PrintWriter(playerFile);
        newFileInfo.println(playerAmount);
        newFileInfo.close();
        return playerAmount;
    }

    public static void updateFile(File playerFile, RoulettePlayer player1) throws IOException
    {
        playerFile.createNewFile();
        PrintWriter updateFileInfo = new PrintWriter(playerFile);
        updateFileInfo.println(player1.getMoney());
        updateFileInfo.close();
    }

    public static void playGame(RoulettePlayer player1, Scanner input) throws IOException
    {
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
    }
}*/