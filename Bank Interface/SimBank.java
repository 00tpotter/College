import java.util.*;

public class SimBank
{
    private int ntell;  // number of tellers
    private boolean queueType;  // whether bank uses single queue (true) or multiple queus (false)
    private double hrs; // hours to run simulation
    private double arr_rate;    // arrivals per hours of customers
    private double t_min;   // average length of a transaction in minutes
    private int maxq;   // maximum number of customers allowed to wait (M, not counting those being served)
    private long seed;  // seed for initializing the RandDist object

    private Teller [] tellers;
    private ArrayList<Queue<Customer>> line;  // the ArrayList of queues to represent the lines in the bank
    private PriorityQueue<SimEvent> futureEventList;    // stores the events as they are generated

    private int numQueues;  // 1 or K, number of queues in the bank, dependent on queueType
    private int customersGenerated = 0;  // total number of customers generated
    private int customersServed;     // total number of customers served
    private int customersTurnt;      // total number of customers turned away due to a long line
    // do not count customers turned away for the below variables
    private int customersWaiting;    // number of customers who had to wait before being served
    private double avgWaitTime;      // average wait time for all customers in bank, in minutes
    private double maxWaitTime;      // max wait time for any customer in the bank, in minutes
    private double firstSTD;         // first standard deviation of the wait time for all customers
    private double avgServiceTime;   // average service time of customers, in minutes
    private double avgWaitTimeB;     // average wait time of customers who had to wait
    private double avgSysTime;       // average time in the system for all customers

    private double currTime = 0;
    private RandDist randy;
    private int customersInBank = 0;

    private ArrayList<Customer> oldCustomers;
    private ArrayList<Customer> turntCustomers;

    public SimBank(int ntell, boolean queueType, double hrs, double arr_rate, double t_min, int maxq, long seed)
    {
        this.ntell = ntell;
        this.queueType = queueType;
        this.hrs = hrs;
        this.arr_rate = arr_rate;
        this.t_min = t_min;
        this.maxq = maxq;
        this.seed = seed;
        randy = new RandDist(seed);
        tellers = new Teller[ntell];

        for(int i = 0; i < tellers.length; i++)
        {
            tellers[i] = new Teller();
        }

        line = new ArrayList<Queue<Customer>>();
        if(queueType)
        {
            line.add(new LinkedList<Customer>());
        }
        else
        {
            for(int i = 0; i < ntell; i++)
            {
                line.add(new LinkedList<Customer>());
            }
        }

        futureEventList = new PriorityQueue<SimEvent>();
        oldCustomers = new ArrayList<Customer>();
        turntCustomers = new ArrayList<Customer>();
    }

    public void runSimulation()
    {
        SimEvent currEvent;
        futureEventList.add(new ArrivalEvent(randy.exponential(arr_rate)*60)); 

        while(currTime < (hrs * 60))
        {
            currEvent = futureEventList.remove();
            currTime = currEvent.get_e_time();

            if(currEvent instanceof ArrivalEvent)
            {
                customersGenerated++;
                customersInBank++;
                Customer currCust = createCust(customersGenerated, currTime);
                if(customersInBank < maxq)
                {
                    add(currCust);
                    futureEventList.add(new ArrivalEvent(currTime + randy.exponential(arr_rate)*60)); 
                }
                else if(customersInBank >= maxq)
                {
                    turntCustomers.add(currCust);
                    customersTurnt++;
                }
            }
            else if(currEvent instanceof CompletionLocEvent)
            {
                remove(((CompletionLocEvent)currEvent).getLoc());
            }
        }
    }

    public void showResults()
    {
        System.out.println();
        System.out.println("Customer     Arrival     Service     Queue     Teller     Time Serv     Time Cust     Time Serv     Time Spent");
        System.out.println("   ID         Time        Time        Loc       Loc        Begins         Waits         Ends         in Bank  ");
        System.out.println("--------     -------     -------     -----     ------     ---------     ---------     ---------     ----------");
        for(int i = 0; i < oldCustomers.size(); i++)
        {
            System.out.println("   " + oldCustomers.get(i).getID() + "\t      " + oldCustomers.get(i).getATime() + "\t  " + 
            oldCustomers.get(i).getServTime() + "\t       " + oldCustomers.get(i).getQueueID() + "\t " + 
            oldCustomers.get(i).getTellerID() + "\t    " + oldCustomers.get(i).getBegServTime() + "   \t   " + 
            oldCustomers.get(i).getWaitServTime() + "\t \t" + oldCustomers.get(i).getEndServTime() + "\t  " + oldCustomers.get(i).getTotalTime());
        }

        calcValues();
    }

    public Customer createCust(int id, double time)
    {
        Customer c = new Customer(id, time);
        return c;
    }

    public void add(Customer cust) 
    {
        for(int i = 0; i < tellers.length; i++) // checks if there are any open tellers the customer can use
        {
            if(tellers[i].isOpen())
            {
                tellers[i].addCust(cust);
                double currServTime = randy.exponential(1/t_min);
                double endServTime = currTime + currServTime;
                cust.setServTime(currServTime);
                cust.setEndServTime(endServTime);
                futureEventList.add(new CompletionLocEvent(currTime + currServTime, i)); // generate event for when the service is completed
                cust.setQueueID(-1);
                cust.setTellerID(i);
                return;
            }
        }
        
        int shortestLine = 0;
        for(int i = 0; i < line.size(); i++) // adds the customer to the shortest line
        {
            if(line.get(i).size() < line.get(shortestLine).size())
            {
                shortestLine = i;
            }
        }
        line.get(shortestLine).add(cust);
        cust.setQueueID(shortestLine);
    }

    public void remove(int loc) // removes the customer from the teller and gets the next customer in line
    {
        Customer currCust;
        double currServTime = randy.exponential(1/t_min);
        double endServTime = currTime + currServTime;
    
        if(tellers[loc].getCust() != null)
        {
            oldCustomers.add(tellers[loc].getCust());
            currCust = tellers[loc].getCust();
            currCust.setServTime(currServTime);
            currCust.setTellerID(loc);
            currCust.setEndServTime(endServTime);
        }
        
        tellers[loc].removeCust();
        customersInBank--;

        if(line.size() == 1 && line.get(0).size() != 0)
        {
            tellers[0].addCust(line.get(0).remove());
        }
        else if(line.size() != 1 && line.get(loc).size() != 0)
        {
            tellers[loc].addCust(line.get(loc).remove());
        }

        futureEventList.add(new CompletionLocEvent(currTime + currServTime, loc));
    }

    public void calcValues()
    {
        System.out.println();
        System.out.println("Number of queues: " + line.size());
        System.out.println("Max number allowed to wait: " + maxq);
        System.out.println("Customer arrival rate (per hr): " + arr_rate);
        System.out.println("Customer service time (ave min): " + t_min);
        System.out.println("Number of customers arrived: " + customersGenerated);
        System.out.println("Number of customers served: " + (customersGenerated-customersTurnt));
        System.out.println("Number Turned Away: " + customersTurnt);
        System.out.println("Number Who Waited: " + (customersGenerated-customersTurnt));

        double totalWait = 0;
        maxWaitTime = oldCustomers.get(0).getWaitServTime();
        for(int i = 0; i < oldCustomers.size(); i++)
        {
            totalWait += oldCustomers.get(i).getWaitServTime();
            if(maxWaitTime < oldCustomers.get(i).getWaitServTime())
            {
                maxWaitTime = oldCustomers.get(i).getWaitServTime();
            }
        }
        avgWaitTime = totalWait / oldCustomers.size();
        System.out.println("Average Wait (in mins): " + avgWaitTime);
        System.out.println("Max Wait (in mins): " + maxWaitTime);

        double squares = 0;
        for(int i = 0; i < oldCustomers.size(); i++)
        {
            squares += Math.pow((oldCustomers.get(i).getWaitServTime() - avgWaitTime), 2);
        }
        firstSTD = Math.pow(squares/oldCustomers.size(), .5);

        System.out.println("Std. Dev. Wait: " + firstSTD);
        System.out.println();
    }
}