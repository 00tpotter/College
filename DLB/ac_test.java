// Thomas (Teddy) Potter
// tep32
// Main file for running Project 1, but MyLinkedList
// has the main data structure to support this file
import java.util.*;
import java.io.*;

public class ac_test
{
    public static void main(String args[]) throws IOException
    {
        // Creating the input information from the dictionary.txt file
        // and the user input, and then reading in and creating the DLB
        Scanner input = new Scanner(System.in);
        String fileName = new String("dictionary.txt");
        File DLB = new File(fileName);
        Scanner fromFile = new Scanner(DLB);
        MyLinkedList tester = readInFile(fromFile);

        // Creating and maintaining the user history as a text file
        File userHistory = new File("user_history.txt");
        LinkedList<String> history = new LinkedList<String>();

        if(!userHistory.createNewFile()) // if the file already exists, read in its data
        {
            Scanner fromHistory = new Scanner(userHistory);
            while(fromHistory.hasNext())
            {
                history.add(fromHistory.nextLine());
            }
        }

        FileWriter writer = new FileWriter(userHistory);

        // Initial variables to keep time
        long startTime;
        long endTime;
        long elapsedTime;
        double seconds;
        double avgTime = 0;
        int count = 0;

        // User interface/main program
        System.out.println("Hello! Welcome to Teddy's Dictionary AdventureTM!\n");
        System.out.print("Enter the first letter of the word: ");
        String response = input.nextLine();
        String key = "";

        ArrayList<String> suggs = new ArrayList<String>();

        while(!response.equals("!")) // "!" to end the program
        {
            // Selection options that aren't letters
            if(response.equals("1"))
            {
                history.add(suggs.get(0));
                System.out.println("\tWORD SELECTED: " + suggs.get(0));
            }
            else if(response.equals("2"))
            {
                history.add(suggs.get(1));
                System.out.println("\tWORD SELECTED: " + suggs.get(1)); 
            }
            else if(response.equals("3"))
            {
                history.add(suggs.get(2));
                System.out.println("\tWORD SELECTED: " + suggs.get(2));
            }
            else if(response.equals("4"))
            {
                history.add(suggs.get(3));
                System.out.println("\tWORD SELECTED: " + suggs.get(3));
            }
            else if(response.equals("5"))
            {
                history.add(suggs.get(4));
                System.out.println("\tWORD SELECTED: " + suggs.get(4));
            }
            else if(response.equals("$"))
            {
                history.add(key);
                System.out.println("\tWORD SELECTED: " + key);
            }
            else // Normal behavior when a letter is input
            {    
                startTime = System.nanoTime();

                key += response;
                suggs = tester.search(key);     // searches the trie for the key(s)

                // Sees if the prefix is contained in the user history
                for(String item : history)
                { 
                    if(item.startsWith(key))
                    {
                        if(suggs.contains(item))
                        {
                            suggs.remove(item); 
                        }
                        else if(suggs.size() >= 5)
                        {
                            suggs.remove(suggs.size()-1); 
                        }
                        suggs.add(0, item);
                    }
                } 

                // Calculating elapsed time of the search
                endTime = System.nanoTime();
                elapsedTime = endTime - startTime;
                seconds = (double)elapsedTime / 1_000_000_000;
                avgTime += seconds;
                System.out.println("\n(" + seconds + " s)");

                // If there is no suggestion from the DLB or the user history
                if(suggs.isEmpty())
                {
                    System.out.println("No predictions found. Press '$' to save word or continue typing. ");
                    System.out.print("\n\nEnter the next letter: ");
                    response = input.nextLine(); 
                    count++;
                    continue;
                }

                System.out.println("Predictions: ");

                // Prints out predictions
                int i = 1;
                for(String item : suggs)
                {
                    System.out.print("(" + i + ") " + item + "\t");
                    i++;
                }

                // Next letter...
                System.out.print("\n\nEnter the next letter: ");
                response = input.nextLine(); 
                count++;
                continue;
            }

            // Only reaches here once you've selected a word and are going to start a new one
            response = "";
            key = "";
            System.out.print("\nEnter the first letter of the next word: ");
            response = input.nextLine();  
            count++;
        }

        // Goes through the history and writes it back to the file
        for(String item : history)
        {
            writer.write(item + "\n"); 
        }
        writer.flush();
        writer.close();

        // Printing out average time before exiting...
        avgTime = avgTime / count;
        System.out.println("\nAverage time: " + avgTime + " s");
        System.out.println("Bye!");
    }

    // Reads in the dictionary words from dictionary.txt and inserts them into the DLB
    public static MyLinkedList readInFile(Scanner fromFile) throws IOException
    {
        MyLinkedList temp = new MyLinkedList();
        while(fromFile.hasNext())
        {
            String word = fromFile.nextLine();
            word = word + '^';
            temp.insert(word);
        }
        return temp;
    }
}