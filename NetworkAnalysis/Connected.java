/*****************************************************
 * Connected graph class for Project 4 to determine
 * if a graph is connected or if it remains connected
 * when two vertices are removed
 * Dependencies: NetworkGraph.java, NetworkEdge.java
 * Author: Teddy Potter, tep32@pitt.edu
 ****************************************************/

public class Connected
{
    private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int[] id;           // id[v] = id of connected component containing v
    private int[] size;         // size[id] = number of vertices in given component
    private int count;          // number of connected components
    private int v1;
    private int v2;

    // For a normal connected graph
    public Connected(NetworkGraph G)
    {
        marked = new boolean[G.getV()];
        id = new int[G.getV()];
        size = new int[G.getV()];
        for (int v = 0; v < G.getV(); v++) 
        {
            if (!marked[v])
            {
                dfs(G, v);
                count++;
            }
        }
    }

    // For testing if a graph is connected with two vertices removed
    public Connected(NetworkGraph G, int v1, int v2)
    {
        marked = new boolean[G.getV()];
        id = new int[G.getV()];
        size = new int[G.getV()];
        this.v1 = v1;
        this.v2 = v2;
        for (int v = 0; v < G.getV(); v++) 
        {
            if (!marked[v])
            {
                connectedWithout(G, v);
                count++;
            }
        }
    }

    // depth-first search for detecting if a normal graph is detected
    private void dfs(NetworkGraph G, int v)
    {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (NetworkEdge e : G.adj(v)) 
        {
            int w = e.other(v);
            if (!marked[w]) 
            {
                dfs(G, w);
            }
        }
    }

    // depth-first search for detecting if a graph with two vertices removed is connected
    public void connectedWithout(NetworkGraph G, int v) 
    {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (NetworkEdge e : G.adj(v)) 
        {
            int w = e.other(v);
            if (v != v1 && v != v2 && w != v1 && w != v2 && !marked[w]) 
            {
                connectedWithout(G, w);
            }
        }
    }

    public int id(int v) 
    {
        return id[v];
    }


    public int size(int v) 
    {
        return size[id[v]];
    }


    public int count() 
    {
        return count;
    }
}