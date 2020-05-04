// Simulation class for MyStringBuilder
//  simply runs a for loop N times for each method of each class
//  and calculates the times it takes for these operations

public class Assig2B
{
    public static void main(String args[])
    {
        int n = Integer.parseInt(args[0]);
        long startTime;
        long endTime;
        long elapsedTime;
        double avgTime;

        // StringBuilder Test
        System.out.println("StringBuilder Test");
        StringBuilder newSB = new StringBuilder();
        
        System.out.println("\tAppend Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newSB.append('A');
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);

        System.out.println("\tDelete Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newSB.delete(0, 1);
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);

        System.out.println("\tInsert Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newSB.insert(newSB.length()/2, 'A');
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);
        

        // MyStringBuilder Test
        System.out.println("MyStringBuilder Test");
        MyStringBuilder newMSB = new MyStringBuilder();
        
        System.out.println("\tAppend Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newMSB.append('A');
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);

        System.out.println("\tDelete Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newMSB.delete(0, 1);
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);

        System.out.println("\tInsert Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newMSB.insert(newMSB.length()/2, 'A');
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);

        // String Test
        System.out.println("String Test");
        String newS = new String();
        
        System.out.println("\tAppend Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newS +='A';
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);

        System.out.println("\tDelete Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newS.substring(1);
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);

        System.out.println("\tInsert Test");
        startTime = System.nanoTime();
        for(int i = 0; i < n; i++)
        {
            newS = newS.substring(0, newS.length()/2) + 'A' + newS.substring(newS.length()/2, newS.length());
        }
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
        avgTime = (double)elapsedTime / n;
        printStats(elapsedTime, avgTime, n);
    }

    public static void printStats(long time, double avg, int n)
    {
        System.out.println("\t\tTotal time: " + time + " ns for " + n + " operations");
        System.out.println("\t\tAverage time per operation: " + avg + " ns");
    }
}