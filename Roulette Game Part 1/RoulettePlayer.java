// RoulettePlayer Class

public class RoulettePlayer
{
    private String name;
    private String password;
    private double money;
    private double debt;
    
    private Question [] questions;

    public RoulettePlayer()
    {
        name = "";
        password = "";
        money = 0.0;
        debt = 0.0;
        questions = new Question[2];
    }
    public RoulettePlayer(String n, String p, double m, double d)
    {
        name = n;
        password = p;
        money = m;
        debt = d;
        questions = new Question[2];
    }

    public void updateMoney(double delta)
    {
        money += delta;
    }

    public double getMoney()
    {
        return money;
    }

    public String getName()
    {
        return name;
    }

    public void addQuestions(Questions [] q)
    {
        for(int i = 0; i < q.length; i++)
        {
            questions[i] = q[i];
        }
    }

    public String [] getQuestions()
    {
        return questions;
    }

    /*public void showAllData()
    {

    }*/

    public void setPassword(String p)
    {
        password = p;
    }

    public void payBack(double b)
    {
        if(b > debt)
        {
            System.out.println("The amount you are trying to pay back (" + b + ") is more than your debt (" + debt + ").");
            System.out.println("You can only pay back " + debt);
            debt = 0;
        }
        else if(b > money)
        {
            System.out.println("The amount you are trying to pay back (" + b + ") is more than the money you have (" + money + ").");
            System.out.println("You can only pay back " + money + " right now.");
            debt -= money;
            money = 0;            
        }
        else
        {
            debt -= b;
            money -= b;
        }
    }

    public void borrow(double b)
    {
        debt += b;
        updateMoney(b);
    }

    public String toString()
    {
        String player = "";
        player += "Name: " + name;
        player += "\t Money: " + money;
        return player;
    }

    public boolean hasMoney()
    {
        if(money > 0)
        {
            return true;
        }
        return false;
    }
}