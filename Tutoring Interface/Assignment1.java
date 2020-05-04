// CS 0401 T H 9:30AM-10:45AM CL G8
// Assignment #1
//      A simple program for students looking to purchase tutoring in Defense Against the Dark Arts.
//      Students can buy tutoring by the hour, by number of spells, and can get a discount if they win a game of Wizard Chess.
// Teddy Potter ID# 4242571
// September 21 2018

import java.util.*; // Import package that contains the Scanner class

public class Assignment1
{
    public static void main(String args[])
    {
        Scanner input = new Scanner(System.in);

        String name;

        System.out.println("Hello! Welcome to Dank Defense Against the Dark Arts Tutoring!");
        System.out.print("Please enter your name (or nothing to quit): ");
        name = input.nextLine();
        
        while(!name.equals("")) // Loop will run until an empty string is entered as the name
        {
            String inCode;
            String realCode = "Potter";
            int optionNum; // number in the option menu
            int rate = 0; // hour/spell rate
            int hours = 0; // hours purchased
            int spells = 0; // spells purchased
            int total; // total cost
            boolean discount; // if they have the discount or not
            int payment = 0; // total payment made
            int amount = 0; // iterative amount they entered until the total payment is made
            String currency; // type of currency being entered

            System.out.println("\nWelcome " + name + "!");
            System.out.print("Enter the discount code if you have it: ");
            inCode = input.nextLine();

            if(inCode.equals(realCode)) // if they enter the correct code (Potter) then show them the correct menu
            {
                System.out.println("Enjoy the Wizard Chess Dinner Discount!");
                discountMenu();
                discount = true;
            }
            else
            {
                System.out.println("Wrong code, enjoy our regular prices.");
                regularMenu();
                discount = false;
            }

            do // iterates through the purchasing process
            {
                System.out.println("\nChoose an option: ");
                System.out.println("\t1) Hourly Tutoring");
                System.out.println("\t2) Individual Spell Tutoring");
                System.out.println("\t3) Show Pricing Menu");
                System.out.println("\t4) Proceed to Checkout");

                optionNum = input.nextInt();
                if(optionNum == 1) // option for buying hours of tutoring
                {
                    spells = 0;
                    System.out.print("\nEnter the amount of hours you would like: ");
                    hours = input.nextInt();
                    if(hours > 0)
                    {
                        rate = calculateHours(hours);
                        System.out.print("\tThe per hour rate for " + hours + " hours is " + rate + " Knuts");
                        System.out.println();
                    }
                    else
                    {
                        System.out.println("Enter a positive value for hours, please.");
                    }
                }
                else if(optionNum == 2) // option for buying spells of tutoring
                {
                    hours = 0;
                    System.out.print("\nEnter the amount of spells you would like: ");
                    spells = input.nextInt();
                    if(spells > 0 && spells <= 5)
                    {
                        rate = calculateSpells(spells);
                        System.out.print("\tThe per spell rate for " + spells + " spells is " + rate + " Knuts");
                        System.out.println();
                    }
                    else
                    {
                        System.out.println("Spells must be between 1 and 5.");
                    }
                }
                else if(optionNum == 3) // option to bring up the menus again
                {
                    if(discount == true)
                    {
                        discountMenu();
                    }
                    else
                    {
                        regularMenu();
                    } 
                }
                else if(optionNum < 1 || optionNum > 4)
                {
                    System.out.print("Ha-ha, nice try! Enter a valid number: ");
                }
            }while(optionNum != 4); // ends when the customer decides to check out

            // Calculates the totals and displays the check out menu
            total = calculateTotal(spells, hours, rate, discount);

            
            if(total != 0) // doesn't show up if you didn't buy anything
            {
                System.out.println("\nPlease pay in the following format <amount><'space'><currency type>");
                System.out.println("You can keep adding to your payment until the total is reached or exceeded.");
            }
            
            // Displays the check out process and allows the user to pay with whatever coinage they desire
            while(total > payment && total != 0)
            {
                System.out.print("\nEnter your payment: ");
                amount = Integer.parseInt(input.next());
                currency = input.nextLine();
                System.out.println("amount - " + amount);
                
                if(currency.toLowerCase().equals(" knuts") || currency.toLowerCase().equals(" knut"))
                {
                    payment += amount;
                    System.out.println("You have paid " + payment + " out of " + total + " total Knuts due");
                }
                else if(currency.toLowerCase().equals(" sickles") || currency.toLowerCase().equals(" sickle"))
                {
                    int knutAmount = amount * 29;
                    payment += knutAmount;
                    System.out.println("You have paid " + payment + " out of " + total + " total Knuts due");
                }
                else if(currency.toLowerCase().equals(" galleons") || currency.toLowerCase().equals(" galleon"))
                {
                    int knutAmount = amount * 493;
                    payment += knutAmount;
                    System.out.println("You have paid " + payment + " out of " + total + " total Knuts due");
                }   
                else
                {
                    System.out.println("Ha-ha, nice try! Enter a real amount!");
                }     
            }

            // calculates the change left over for the customer
            if(payment > total && total != 0)
            {
                System.out.println("And your change is: ");
                int change = payment - total;
                int galleons = change / 493;
                change = change % 493;
                int sickles = change / 29;
                change = change % 29;
                int knuts = change;
                
                if(galleons > 0)
                {
                    System.out.println("\t" + galleons + " Galleons");
                }
                if(sickles > 0)
                {
                    System.out.println("\t" + sickles + " Sickles");
                }
                if(knuts > 0)
                {
                   System.out.println("\t" + knuts + " Knuts ");
                }
            }

            if(total == 0)
            {
                System.out.println("Sorry you didn't decide on anything. Come again next time!");
                input.nextLine();
            }

            System.out.println("Thank you for shopping with Dank Defense Against the Dark Arts Tutoring!");
            System.out.println();


            System.out.println("Hello! Welcome to Dank Defense Against the Dark Arts Tutoring!");
            System.out.print("Please enter your name (or nothing to quit): ");
            name = input.nextLine();
        }
    }

    // Calculates and displays the bill
    public static int calculateTotal(int spells, int hours, int rate, boolean discount)
    {
        int total;
        int discRate = 0;
        int subtotal = 0;
        int tax;

        if(hours == 0 && spells == 0)
        {
            return 0;
        }

        System.out.println("\n - Total Bill - ");
        // Calculate hours 
        if(spells == 0)
        {
            System.out.println("\n\t" + hours + " hours of tutoring ordered");
            System.out.println("\n\tRegular rate: " + rate + " Knuts per hour");
            if(discount)
            {
                discRate = (int)Math.round(rate * .9);
                System.out.println("\n\tDiscount rate: " + discRate + " Knuts per hour");
                subtotal = hours * discRate;
            }
            else
            {
                subtotal = hours * rate;
            }
        }
        // Calculate spells
        else if(hours == 0)
        {
            System.out.println("\n\t" + spells + " spells of tutoring ordered");
            System.out.println("\n\tRegular rate: " + rate + " Knuts per spell");
            if(discount)
            {
                discRate = rate - 116;
                System.out.println("\n\tDiscount rate: " + discRate + " Knuts per spell");
                subtotal = spells * discRate;
            }
            else
            {
                subtotal = spells * rate;
            }
        }

        // Calculates tax and subtotal
        System.out.println("\n\tSubtotal: " + subtotal + " Knuts");
        tax = (int)Math.round(subtotal * .05);
        System.out.println("\n\tMinistry of Magic Tax: " + tax + " Knuts");
        total = subtotal + tax;
        System.out.println("\n\tTotal: " + total + " Knuts");

        return total;
    }

    // calculates the price/rate for hours of tutoring
    public static int calculateHours(int hours)
    {
        int price = 0;
        if(hours <= 25 && hours >= 0)
        {
            price = 2465 - ((hours / 5) * 145);
        }
        else if(hours > 25)
        {
            price = 1740;
        }
        else
        {
            price = 0;
        }
        return price;
    }

    // calculates the price/rate for spells of tutoring
    public static int calculateSpells(int spells)
    {
        int price = 0;
        if(spells == 1 || spells == 2)
        {
            price = 2465;
        }
        else if(spells == 3 || spells == 4)
        {
            price = 1972;
        }
        else if(spells >= 5)
        {
            price = 1479;
        }
        else 
        {
            price = 0;
        }
        return price;
    }

    // simply displays the reglar menu
    public static void regularMenu()
    {
        System.out.println("\nHere are our prices: ");
        System.out.println("\n - Hourly Tutoring - ");
        System.out.println("\t0 to 4 hours - 2465 Knuts per hour");
        System.out.println("\t145 Knuts per hour less for every 5 hours");
        System.out.println("\t*Minimum of 1740 Knuts per hour");

        System.out.println("\n - Individual Spell Tutoring - ");
        System.out.println("\t1 to 2 spells - 2465 Knuts per spell");
        System.out.println("\t3 to 4 spells - 1972 Knuts per spell");
        System.out.println("\t5 spells - 1479 Knuts per spell");
    }

    // displays the discount menu
    public static void discountMenu()
    {
        System.out.println("\nHere are our prices: ");
        System.out.println("\n - Hourly Tutoring - ");
        System.out.println("\t0 to 4 hours - 2465 Knuts per hour");
        System.out.println("\t145 Knuts per hour less for every 5 hours");
        System.out.println("\t*Minimum of 1740 Knuts per hour");

        System.out.println("\n - Individual Spell Tutoring - ");
        System.out.println("\t1 to 2 spells - 2465 Knuts per spell");
        System.out.println("\t3 to 4 spells - 1972 Knuts per spell");
        System.out.println("\t5 spells - 1479 Knuts per spell");

        System.out.println("\n - Wizard Chess Winner Discount - ");
        System.out.println("\t10% off hourly rate");
        System.out.println("\t4 Sickles off per spell rate");
        System.out.println("\t*All discounts applied at checkout");
    }
}