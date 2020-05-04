public class Customer
{
    private int orderNum;   // customer #, order in which it was generated
    private double arrTime;    // arrival time at bank, time customer was generated
    private double transTime;  // time required for transaction, in minutes
    private boolean stays;  // if the customer stays in the bank or not, if false then below values are not calculated
    private int queueID;    // value from 0 to K-1 for the which line they are in, -1 if they do not have to wait
    private int tellerID;   // value from 0 to K-1 for which teller is serving them
    private double startServiceTime;   // the time the service begins for the customer
    private double waitServiceTime;    // the time the customer waits to be serviced, startServiceTime - arrTime 
    private double endServiceTime;     // the time the service ends for the customer
    private double totalTime;  // endServiceTime - arrTime, total time of the customer in the bank

    public Customer(int custNum, double aTime)
    {
        orderNum = custNum;
        arrTime = aTime;
        transTime = 0;
    }

    public int getID()
    {
        return orderNum;
    }

    public double getATime()
    {
        return (int)(arrTime * 100 + 0.5) / 100.0;
    }

    public void setServTime(double servTime)
    {
        transTime = servTime;
    }

    public double getServTime()
    {
        return (int)(transTime * 100 + 0.5) / 100.0;
    }

    public void setStays(boolean stays)
    {
        this.stays = stays;
    }

    public boolean getStays()
    {
        return stays;
    }

    public void setQueueID(int id)
    {
        queueID = id;
    }

    public int getQueueID()
    {
        return queueID;
    }

    public void setTellerID(int id)
    {
        tellerID = id;
    }

    public int getTellerID()
    {
        return tellerID;
    }

    public void setBegServTime(double t)
    {
        startServiceTime = t;
    }

    public double gBegServTime()
    {
        return (int)(startServiceTime * 100 + 0.5) / 100.0;
    }

    public double getBegServTime()
    {
        startServiceTime = endServiceTime - transTime;
        return (int)(startServiceTime * 100 + 0.5) / 100.0;
    }

    public void setEndServTime(double time)
    {
        endServiceTime = time;
    }

    public double getEndServTime()
    {
        return (int)(endServiceTime * 100 + 0.5) / 100.0;
    }

    public double getWaitServTime()
    {
        waitServiceTime = startServiceTime - arrTime;

        if(waitServiceTime < 0)
        {
            return 0.00;
        }
        return (int)(waitServiceTime * 100 + 0.5) / 100.0;
    }

    public double getTotalTime()
    {
        totalTime = transTime + waitServiceTime;
        return (int)(totalTime * 100 + 0.5) / 100.0;
    }
}