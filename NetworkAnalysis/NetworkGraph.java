/*****************************************************
 * Graph class for Project 4
 * Dependencies: Bag.java. NetworkEdge.java
 * Author: Teddy Potter, tep32@pitt.edu
 ****************************************************/

import java.util.*;

public class NetworkGraph
{
    private int V;
    private int E;
    private Bag<NetworkEdge>[] adj;

    public NetworkGraph(int V)
    {
        this.V = V;
        E = 0;
        adj = (Bag<NetworkEdge>[]) new Bag[V];
        for(int v = 0; v < V; v++)
        {
            adj[v] = new Bag<NetworkEdge>();
        }
    }

    public int getV()
    {
        return V;
    }

    public int getE()
    {
        return E;
    }

    public void addEdge(NetworkEdge e)
    {
        int v = e.getv();
        int w = e.getw();
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    public Iterable<NetworkEdge> adj(int v)
    {
        return adj[v];
    }

    public int degree(int v)
    {
        return adj[v].size();
    }

    public Iterable<NetworkEdge> edges() 
    {
        Bag<NetworkEdge> list = new Bag<NetworkEdge>();
        for(int v = 0; v < V; v++) 
        {
            int selfLoops = 0;
            for(NetworkEdge e : adj(v)) 
            {
                if(e.getw() > v) 
                {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                else if(e.getw() == v) 
                {
                    if(selfLoops % 2 == 0) 
                    {
                        list.add(e);
                    }
                    selfLoops++;
                }
            }
        }
        return list;
    }

    public String toString() 
    {
        StringBuilder s = new StringBuilder();
        s.append("\nV = " + V + " E = " + E + "\n");
        for (int v = 0; v < V; v++) 
        {
            s.append("\n-- Row " + v + " -- \n");
            for (NetworkEdge e : adj[v]) 
            {
                s.append(e + "  \n");
            }
            s.append("\n");
        }
        return s.toString();
    }
}