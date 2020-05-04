public class Teller
{
    private Customer currCust;

    public Teller()
    {
        currCust = null;
    }

    public boolean isOpen()
    {
        if(currCust == null)
        {
            return true;
        }
        return false;
    }

    public void addCust(Customer c)
    {
        currCust = c;
    }

    public Customer getCust()
    {
        return currCust;
    }

    public void removeCust()
    {
        currCust = null;
    }
}