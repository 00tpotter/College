/*****************************************************
 * Main driver class for Project 4
 * Dependencies: NetworkGraph.java, NetworkEdge.java, 
 *               Dijkstras.java, Connected.java
 * Author: Teddy Potter, tep32@pitt.edu
 ****************************************************/

import java.util.*;
import java.io.*;

public class NetworkAnalysis
{
    public static void main(String args[]) throws IOException
    {
        // Declaring variables used for input from a file
        Scanner input = new Scanner(System.in);
        String fileName = new String(args[0]);
        File inputFile = new File(fileName);
        Scanner fromFile = new Scanner(inputFile);
        String line = fromFile.nextLine();

        // Declaring main data structure variables
        int V = Integer.parseInt(line);
        NetworkGraph net = new NetworkGraph(V);
        NetworkGraph copper = new NetworkGraph(V);

        // Reading in graph data from the file
        while(fromFile.hasNext())
        {
            line = fromFile.nextLine();

            int v1 = Integer.parseInt(line.substring(0, line.indexOf(" ")));
            line = line.substring(line.indexOf(" ") + 1);

            int v2 = Integer.parseInt(line.substring(0, line.indexOf(" ")));
            line = line.substring(line.indexOf(" ") + 1);

            String type = line.substring(0, line.indexOf(" "));
            line = line.substring(line.indexOf(" ") + 1);

            int bandwidth = Integer.parseInt(line.substring(0, line.indexOf(" ")));
            line = line.substring(line.indexOf(" ") + 1);

            int length = Integer.parseInt(line);

            NetworkEdge newEdge = new NetworkEdge(v1, v2, type, bandwidth, length);
           
            net.addEdge(newEdge); // Graph with every vertex and edge

            if(newEdge.getType().equals("copper"))
            {
                copper.addEdge(newEdge);    // Graph with just copper links
            }
        }

        // Beginning of main program input/output
        System.out.println("Select an option:");
        System.out.println("\t1) Find the lowest latency path between two switches");
        System.out.println("\t2) Determine if the network is copper-only connected");
        System.out.println("\t3) Determine if the network would remain connected if two switches failed");
        System.out.println("\t4) Quit program");

        String response = input.nextLine();

        while(!response.equals("4"))
        {
            if(response.equals("1"))    // Find the lowest latency path
            {
                System.out.println(" - Finding lowest latency path - ");
                System.out.print("\tEnter the first switch: ");
                int v = Integer.parseInt(input.nextLine());

                System.out.print("\tEnter the second switch: ");
                int w = Integer.parseInt(input.nextLine());

                Dijkstras late = new Dijkstras(net, v);     // Create a new Dijkstras object to find lowest latency path

                double latency = 0.0;
                int bandwidth = Integer.MAX_VALUE;
                if(late.hasPathTo(w))   // If there is a path to that vertex
                {
                    System.out.println("\n\tEdges of lowest latency path: ");
                    for(NetworkEdge e : late.pathTo(w))     // Go through each vertex along that path and calculate total latency and available bandwidth
                    {
                        System.out.println("\t\t" + e);
                        latency += e.weight();
                        if(e.getBandwidth() < bandwidth)
                        {
                            bandwidth = e.getBandwidth();
                        }
                    }
                }
                System.out.println("\n\tTotal latency along path: " + latency + " seconds");
                System.out.println("\n\tAvailable bandwidth along path: " + bandwidth + " mbps");
            }
            else if(response.equals("2"))   // Determine if the graph is copper-only connected
            {
                Connected con = new Connected(copper);  // Create a new connected graph with the graph we already made out of copper links

                if(con.count() == 1)    // If there's just one connected component, then it's all connected
                {
                    System.out.println("This network is connected with considering only copper links.");
                }
                else
                {
                    System.out.println("This network is not connected with just copper links.");
                }
            }
            else if(response.equals("3"))   // Connected with any two vertices removed?
            {
                boolean stillCon = true;

                // Loop through every possible combination of vertex pairs
                for(int i = 0; i < V; i++)  
                {
                    for(int j = i + 1; j < V; j++)
                    {
                        Connected verts = new Connected(net, i, j); // Create a new connected graph without the two vertices

                        if(verts.count() > 3) // If there are more than 3 connected components, then the graph didn't remain connected
                        {
                            stillCon = false;
                            System.out.println("If switches " + i + " and " + j + " fail, the network will become unconnected.");
                        }
                    }
                }

                if(stillCon)
                {
                    System.out.println("This network would remain connected if two switches failed.");
                }
                else
                {
                    System.out.println("This network would not remain connected if two switches failed.");
                }
            }

            System.out.println("\nSelect an option:");
            System.out.println("\t1) Find the lowest latency path between two switches");
            System.out.println("\t2) Determine if the network is copper-only connected");
            System.out.println("\t3) Determine if the network would remain connected if two switches failed");
            System.out.println("\t4) Quit program");
    
            response = input.nextLine();
        }
    }
}