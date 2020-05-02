import java.util.Arrays;

/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

public class MyLZW 
{
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W, originally 4096
    private static int W = 9;         // codeword width, originally 12
    private static boolean reset = false;
    private static boolean monitor = false;
    

    public static void compress() 
    { 
        // Simply reads in the file, input contains the contents of the entire file
        String input = BinaryStdIn.readString();    
        double uncompSize = 0;     // gives the size of the uncompressed data that has been processed
        double compSize = 0;
        double compRatio = 0;
        double oldRatio = 0;
        double thresholdRatio = 0;

        // creating a new Ternary Search Tree that will be used to store the symbol tree for the compression
        TST<Integer> st = new TST<Integer>();   
        
        // initializes the ASCII characters 0-255 in the codebook
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R = 256 is codeword for EOF; "code" variable is like the counter for which number codeword we're on

        // main loop for encoding prefixes into the codebook and then finding matches and saving space
        while (input.length() > 0)  // while !endOfFile
        {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.; match longest prefix in codebook

            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.; writes codeword to output

            int t = s.length();       // length of the current longest prefix of the input
            uncompSize += t * 8;    // times eight because 8 bits are used to store each character
            compSize += W;            
            
            // standard/nothing mode; simply implements variable-width codewords up to 16 bits
            if(code == L && W < 16)
            {
                W++;
                L = L << 1;
            }
            // reset mode; is the codebook is full then reset it
            else if(code >= L && W == 16 && reset == true)
            {
                st = new TST<Integer>();   
                W = 9;
                L = 512;
        
                // initializes the ASCII characters 0-255 in the codebook
                for (int i = 0; i < R; i++)
                    st.put("" + (char) i, i);
                code = R+1;
            }
            // monitor mode; if the codebook is full and the compression ratio of ratios is > 1.1
            else if(code >= L && W == 16 && monitor == true) 
            {
                compRatio = uncompSize / compSize;
                thresholdRatio = oldRatio / compRatio;
                
                if(thresholdRatio > 1.1)
                {                    
                    st = new TST<Integer>();   
                    W = 9;
                    L = 512;
            
                    // initializes the ASCII characters 0-255 in the codebook
                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i);
                    code = R+1;
                }
            }

            // Add s to symbol table.; if you can add another character and we haven't run out of space for codewords
            if (t < input.length() && code < L)    
            {
                // add, from a substring of the input, the prefix plus the next character 
                st.put(input.substring(0, t + 1), code++); // also increment code to keep track of codeword entries
                oldRatio = uncompSize / compSize;
            }

            // Scan past s in input.; new input is simply the previous input but the codeword that was just added is subtracted out
            input = input.substring(t);    
        }

        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() 
    {
        String[] st = new String[L];
        int i; // next available codeword value

        double uncompSize = 8;     // gives the size of the uncompressed data that has been processed
        double compSize = W;
        double compRatio = 0;
        double oldRatio = 0;
        double thresholdRatio = 0;

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
        {
            st[i] = "" + (char) i;
        }
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        
        // Simply reading in the first character to set the nothing, reset, or monitor variables
        if(codeword == 'n')
        {
            reset = false;
            monitor = false;
            codeword = BinaryStdIn.readInt(W);
        }
        else if(codeword == 'r')
        {
            reset = true;
            monitor = false;
            codeword = BinaryStdIn.readInt(W);
        }
        else if(codeword == 'm')
        {
            monitor = true;
            reset = false;
            codeword = BinaryStdIn.readInt(W);
        }

        if (codeword == R) return;           // expanded message is empty string; value 256 is endOfFile
        
        String val = st[codeword];

        while(true) 
        {
            if(i == L && W < 16)    // variable length codewords 
            {
                W++;
                L = L << 1;
                st = Arrays.copyOf(st, L);
            }
            else if(i >= L && W == 16 && reset == true)  // reset when the codebook fills up
            {
                W = 9;
                L = 512;
                st = new String[L];   
        
                // initializes the ASCII characters 0-255 in the codebook
                for (i = 0; i < R; i++)
                {
                    st[i] = "" + (char) i;
                }
                st[i++] = "";    
            }
            else if(i >= L && W == 16 && monitor == true)  // reset when the codebook fills up
            {
                compRatio = uncompSize / compSize;
                thresholdRatio = oldRatio / compRatio;
                
                if(thresholdRatio > 1.1)
                {
                    W = 9;
                    L = 512;
                    st = new String[L];   
            
                    // initializes the ASCII characters 0-255 in the codebook
                    for (i = 0; i < R; i++)
                    {
                        st[i] = "" + (char) i;
                    }
                    st[i++] = "";    
                }
            }

            BinaryStdOut.write(val);

            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];

            if (i == codeword) 
            {
                s = val + val.charAt(0);   // special case hack
                oldRatio = uncompSize / compSize;
            }

            if (i < L)
            {
                st[i++] = val + s.charAt(0);
                oldRatio = uncompSize / compSize;
            }
            
            val = s;

            uncompSize += val.length() * 8;    // times eight because 8 bits are used to store each character
            compSize += W;            
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) 
    {
        if(args[0].equals("-"))
        {
            if(args[1].equals("n"))
            {
                reset = false;
                monitor = false;
                BinaryStdOut.write("n", W);
            }
            else if(args[1].equals("r"))
            {
                reset = true;
                monitor = false;
                BinaryStdOut.write("r", W);
            }
            else if(args[1].equals("m"))
            {
                monitor = true;
                reset = false;
                BinaryStdOut.write("m", W);
            }
            compress();
        } 
        else if (args[0].equals("+"))
        {
            expand();
        }
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
