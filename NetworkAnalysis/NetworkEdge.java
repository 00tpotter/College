/*****************************************************
 * Edge class for Project 4
 * 
 * Author: Teddy Potter, tep32@pitt.edu
 ****************************************************/

public class NetworkEdge
{
    private int v;
    private int w;
    private String type;
    private int bandwidth;
    private int length;

    public NetworkEdge(int v, int w, String t, int b, int l)
    {
        this.v = v;
        this.w = w;
        type = t;
        bandwidth = b;
        length = l;
    }

    public int getv()
    {
        return v;
    }

    public int getw()
    {
        return w;
    }

    public String getType()
    {
        return type;
    }

    public int other(int vertex)
    {
        if(vertex == v)
        {
            return w;
        }
        else if(vertex == w)
        {
            return v;
        }
        return -1;
    }
    
    public double weight()
    {
        double weight = 0.0;
        if(type.equals("copper"))
        {
            weight = length / 230000000.0;
        }
        else if(type.equals("optical"))
        {
            weight = length / 200000000.0;
        }
        return weight;
    }

    public int getBandwidth()
    {
        return bandwidth;
    }

    public String toString() 
    {
        return "v: " + v + "\tw: " + w + "\ttype: " + type + "\tbandwidth: " + bandwidth + "  \tlength: " + length + "\tweight: " + weight();
    }
}