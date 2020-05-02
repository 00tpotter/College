import java.util.*;

public class CarPQ
{
    private int maxN;        // maximum number of elements on PQ; simply for the fact that the pq is stored as an array with a static size
    private int n;           // number of elements on PQ
    private int[] pq;        // binary heap using 1-based indexing
    private int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i; the array backing the normal pq
    private Car[] keys;      // keys[i] = priority of i; list of the actual keys for the pq
    private char mode;

    public CarPQ(int maxN, char mode)
    {
        this.maxN = maxN;
        this.mode = mode;
        n = 0;
        keys = new Car[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];
        for(int i = 0; i <= maxN; i++)
        {
            qp[i] = -1;
        }
    }

    public void resize()
    {
        maxN = maxN * 2;

        keys = Arrays.copyOf(keys, maxN + 1);
        pq = Arrays.copyOf(pq, maxN + 1);
        qp = Arrays.copyOf(qp, maxN + 1);

        for(int i = n; i <= maxN; i++)
        {
            qp[i] = -1;
        }
    }

    public boolean contains(int i)
    {
        return qp[i] != -1;
    }

    public void insert(int i, Car key)
    {
        if(contains(i))
        {
            return;
        }

        if(n >= maxN)
        {
            resize();
        }

        n++;
        qp[i] = n;
        pq[n] = i;
        keys[i] = key;
        upheap(n);    
    }

    public void update(int i, Car key)
    {
        keys[i] = key;
        upheap(qp[i]);
        downheap(qp[i]);
    }

    public void delete(int i)
    {
        int index = qp[i];
        swap(index, n--);
        upheap(index);
        downheap(index);
        keys[i] = null;
        qp[i] = -1;
    }

    public int delMin() {
        int min = pq[1];
        swap(1, n--);
        downheap(1);
        assert min == pq[n+1];
        qp[min] = -1;        // delete
        keys[min] = null;    // to help with garbage collection
        pq[n+1] = -1;        // not needed
        return min;
    }

    public Car minCar()
    {
        return keys[pq[1]];
    }

    private void swap(int i, int j)
    {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    // compareTo needs to establish how it should determine a priority based on price and mileage
    private boolean greater(int i, int j) 
    {
        if(keys[pq[i]].getPrice() > keys[pq[j]].getPrice() && mode == 'p')
        {
            return true;
        }
        else if(keys[pq[i]].getMileage() > keys[pq[j]].getMileage() && mode == 'm')
        {
            return true;
        }
        return false;

        //return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private void upheap(int k)  // k is the index of the current item selected to be moved up the heap
    {
        while(k > 1 && greater(k/2, k)) // while k is not the first item and k's parent is greater than k
        {
            swap(k, k/2);       // swap k and its parent
            k = k/2;            // the index of the current item selected is now where the parent was
        }
    }

    private void downheap(int k)    // k is the index of the current item selected to be moved up the heap
    {
        while(2*k <= n)     // while k's child is less than or equal to the number of items in the pq
        {
            int j = 2*k;    // j is k's child
            if(j < n && greater(j, j+1))    // if j is less than the number of items in the pq and greater than its sibling
            {
                j++;    // increase j so that the position of the current item being examined is the next sibling over
            }

            if(!greater(k, j))  // if k < j; if the parent is less than the child, then don't swap just break out of the loop 
            {
                break;
            }

            swap(k, j);     // swap the parent and child
            k = j;          // new current location of the parent is the previous child (as it is going down the heap)
        }
    }

    public int size() 
    {
        return n;
    }

    public Car carAt(int i)
    {
        return keys[i];
    }

    public void printPQ()
    {
        System.out.println("\n*****************************");
        for(int i = 1; i <= n; i++)
        {
            System.out.println(keys[pq[i]].toString());
        }
        System.out.println("\n");
        for(int i = 0; i < n; i++)
        {
            System.out.println(keys[i]);
        }
        System.out.println("\n");
        for(int i = 0; i < qp.length; i++)
        {
            //System.out.println(qp[i]);
        }
    }
}
