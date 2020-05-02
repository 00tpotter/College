/*****************************************************
 * Dijkstra's Algorithm implementation for Project 4
 * Dependencies: IndexMinPQ.java, NetworkEdge.java,
 *               NetworkGraph.java, Stack.java
 * Author: Teddy Potter, tep32@pitt.edu
 ****************************************************/

public class Dijkstras
{
    private double[] distTo;
    private NetworkEdge[] edgeTo;
    private IndexMinPQ<Double> pq;

    public Dijkstras(NetworkGraph G, int s)
    {
        distTo = new double[G.getV()];
        edgeTo = new NetworkEdge[G.getV()];

        for (int v = 0; v < G.getV(); v++)
        {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        pq = new IndexMinPQ<Double>(G.getV());
        pq.insert(s, distTo[s]);

        while (!pq.isEmpty()) 
        {
            int v = pq.delMin();
            for (NetworkEdge e : G.adj(v))
            {
                relax(e, v);
            }
        }
    }     

    // relax edge e and update pq if changed
    private void relax(NetworkEdge e, int v) 
    {
        int w = e.other(v);
        if (distTo[w] > distTo[v] + e.weight()) 
        {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) 
            {
                pq.decreaseKey(w, distTo[w]);
            }
            else                
            {
                pq.insert(w, distTo[w]);
            }
        }
    }

    public double distTo(int v)
    {
        return distTo[v];
    }

    public boolean hasPathTo(int v)
    {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<NetworkEdge> pathTo(int v) 
    {
        if (!hasPathTo(v)) 
        {
            return null;
        }

        Stack<NetworkEdge> path = new Stack<NetworkEdge>();
        int x = v;

        for (NetworkEdge e = edgeTo[v]; e != null; e = edgeTo[x]) 
        {
            path.push(e);
            x = e.other(x);
        }
        return path;
    }
}