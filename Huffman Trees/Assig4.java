import java.util.*;
import java.io.*;

public class Assig4
{
    public static void main(String [] args) throws IOException
    {
        Scanner input = new Scanner(System.in);
        // Reading in the file
        String fileName = new String(args[0]);
        File newHuff = new File(fileName);
        Scanner fromFile = new Scanner(newHuff);
        BinaryNode<Character> root = readInFile(newHuff, fromFile);
        int leaves = numberOfLeaves(root, 0);
        
        // Making the table
        String [] codeTable = new String[leaves]; 
        char [] letterTable = new char[leaves];   
        
        makeTable(root, new StringBuilder(), codeTable, letterTable);
        System.out.println();

        // Interactive session
        System.out.println("The Huffman Tree has been restored.");
        System.out.println("Please choose from the following: ");
        System.out.println("1) Encode a text string");
        System.out.println("2) Decode a Huffman string");
        System.out.println("3) Quit");
        String response = input.nextLine();
        while(!response.equals("3"))
        {
            if(response.equals("1"))
            {
                System.out.println("Enter a String from the following characters: ");
                for(int i = 0; i < letterTable.length; i++)
                {
                    System.out.print(letterTable[i]);
                }  
                System.out.println();
                response = input.nextLine();
                encodeString(response, codeTable, letterTable);
            }
            else if(response.equals("2"))
            {
                System.out.println("Here is the encoding table: ");
                for(int i = 0; i < leaves; i++)
                {
                    System.out.print(letterTable[i] + ": ");
                    System.out.print(codeTable[i]);
                    System.out.println();
                }
                System.out.println("Enter a Huffman string: ");
                response = input.nextLine();
                System.out.println(decodeString(new StringBuilder(response), root, new StringBuilder(), root));
            }

            System.out.println("Please choose from the following: ");
            System.out.println("1) Encode a text string");
            System.out.println("2) Decode a Huffman string");
            System.out.println("3) Quit");
            response = input.nextLine();
        }

        System.out.println("See ya next time");
    }

    public static StringBuilder decodeString(StringBuilder input, BinaryNode<Character> node, StringBuilder output, BinaryNode<Character> root)
    {
        try
        {
            if(node != null)
            {
                if(node.isLeaf()) // If it reaches a leaf, it appends the result and sets the current node to the root
                {
                    output.append(node.getData());
                    node = root;
                }
                else if(input.charAt(0) == '0') // if it is 0, resurse left, remove the first character fromt the input
                {
                    input.deleteCharAt(0);
                    decodeString(input, node.getLeftChild(), output, root);
                }
                else if(input.charAt(0) == '1') // if it is 1, recurse right, remove the first character fromt the input
                {
                    input.deleteCharAt(0);
                    decodeString(input, node.getRightChild(), output, root);
                }
                if(input.length() > 0) // if there is still characters in the input, then keep going
                {
                    decodeString(input, node, output, root);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("*Invalid Huffman string*");
            output.delete(0, output.length());
        }

        return output;
    }

    // Simply uses the encoding table created previously and checks if the letters match, then prints them
    public static void encodeString(String input, String [] codeTable, char [] letterTable)
    {
        System.out.println("Huffman String:");
        String output = new String();
        for(int i = 0; i < input.length(); i++)
        {
            for(int j = 0; j < letterTable.length; j++)
            {
                if(input.charAt(i) == letterTable[j])
                {
                    output += codeTable[j];
                    System.out.println(codeTable[j]);
                }
            }
        }
        if(output.length() == 0)
        {
            System.out.println("Invalid input");
        }
        System.out.println();
    }

    // Makes a table by traversing the tree inorder, uses two arrays for the table
    public static StringBuilder makeTable(BinaryNode<Character> node, StringBuilder code, String [] codeTable, char [] letterTable)
	{
		if(node != null)
		{
            // Uses 0's and 1's to keep track of where it goes, then deletes those when it backtracks
            code.append('0');
            makeTable(node.getLeftChild(), code, codeTable, letterTable);
            code.delete(code.length()-1, code.length());

            if(node.isLeaf())
            {            
                // uses the character's ASCII value to determine its position in the arrays
                codeTable[(int)node.getData()-65] = code.toString();
                letterTable[(int)node.getData()-65] = node.getData();      
                return code;
            }

            code.append('1');
            makeTable(node.getRightChild(), code, codeTable, letterTable);
            code.delete(code.length()-1, code.length());
        }
        return code;
	}

    public static BinaryNode<Character> readInFile(File newHuff, Scanner fromFile) throws IOException
    {
        if(fromFile.hasNext()) // checks for the next line in the file
        {
            String line = fromFile.nextLine();
            if(line.charAt(0) == 'L') // base case, if it's an L then create a leaf
            {
                BinaryNode<Character> leaf = new BinaryNode<>(line.charAt(2)); // the letter that goes into the leaf
                return leaf;
            }
            else if(line.charAt(0) == 'I') // recursive case, if it's an I then create an interior node with two children
            {
                BinaryNode<Character> interior = new BinaryNode<>('3'); // arbitrary character 3
                interior.setLeftChild(readInFile(newHuff, fromFile));
                interior.setRightChild(readInFile(newHuff, fromFile));
                return interior;
            }
        }  
        return null; 
    }

    // Simple recursive method for calculating the number of leaves in a given tree, used for the array sizes in the table
    public static int numberOfLeaves(BinaryNode<Character> node, int count)
    {
        if (node != null)
		{
            if(node.isLeaf())
            {
                count++;
            }
			count = numberOfLeaves(node.getLeftChild(), count);
            count = numberOfLeaves(node.getRightChild(), count);
        }
        return count;
    }
}