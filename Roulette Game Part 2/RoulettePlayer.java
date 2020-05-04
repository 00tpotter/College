// RoulettePlayer Class

public class RoulettePlayer
{
    private String name;
    private String password;
    private double money;
    private double debt;
    
    private Question [] questions;

    // Default Constructor
    public RoulettePlayer()
    {
        name = "";
        password = "";
        money = 0.0;
        debt = 0.0;
        questions = new Question[2];
    }

    // Regular Constructor for normal use
    public RoulettePlayer(String n, String p, double m, double d)
    {
        name = n;
        password = p;
        money = m;
        debt = d;
        questions = new Question[2];
    }

    // Constructor for if someone just puts in name and password
    public RoulettePlayer(String n, String p)
    {
        name = n;
        password = p;
        money = 0.0;
        debt = 0.0;
        questions = new Question[2];
    }

    // Updates the money instance variable with the change in money from betting
    public void updateMoney(double delta)
    {
        money += delta;
    }

    // Simple getMoney function to return the current amount of money 
    public double getMoney()
    {
        return money;
    }

    public double getDebt()
    {
        return debt;
    }

    // Simple getName function to return the name of the player
    public String getName()
    {
        return name;
    }

    // Adds the questions, essentially instantiates the questions[] array
    public void addQuestions(Question [] q)
    {
        for(int i = 0; i < q.length; i++)
        {
            questions[i] = q[i];
        }
    }

    public boolean hasQuestions()
    {
        if(questions[0] == null)
        {
            return false;
        }
        return true;
    }

    // Returns the individual questions stored in Question objects in the array questions[] as an array of Strings 
    public String [] getQuestions()
    {
        String [] q = new String[2];
        for(int i = 0; i < q.length; i++)
        {
            q[i] = questions[i].getQ();
        }
        return q;
    }

    // Returns true if the questions and answers and amount of Questions match
    public boolean matchQuestions(Question [] q)
    {
        if(q.length != questions.length)
        {
            return false;
        }
        for(int i = 0; i < q.length; i++)
        {
            if(!questions[i].equals(q[i]))//(q[i].getQ().equals(questions[i].getQ()) && q[i].getA().equals(questions[i].getA())))
            {
                return false;
            }
        }
        return true;
    }

    // Simply prints out all the data
    public void showAllData()
    {
        String data = "";
        data += "Name: " + name;
        data += "\nPassword: " + password;
        data += "\nMoney: " + money;
        data += "\nDebt: " + debt;
        if(questions[0] != null)
        {
            data += "\nQ: " + questions[0].getQ();
            data += " A: " + questions[0].getA();
            data += "\nQ: " + questions[1].getQ();
            data += " A: " + questions[1].getA();
        }
        else
        {
            data += "\nQuestions: None";
        }
        
        System.out.println(data);
    }

    // Returns all the data in the form of a String
    public String saveString()
    {
        String s;
        if(questions[0] != null)
        {
            s = name + "," + password + "," + money + "," + debt + "," + questions[0].getQ() + "," + questions[0].getA() + "," + questions[1].getQ() + "," + questions[1].getA();
        }
        else
        {
            s = name + "," + password + "," + money + "," + debt;        
        }
        return s;
    }

    // Simple setPassword method to set the password instance variable to the input String
    public void setPassword(String p)
    {
        password = p;
    }

    // Simple getPassword method that returns the password since you cannot directly access it
    public String getPassword()
    {
        return password;
    }

    // Allows the user to pay back debts they have accumulating
    public void payBack(double b)
    {
        // Checks if you're paying back over what you owe
        if(b > debt)
        {
            System.out.println("The amount you are trying to pay back (" + b + ") is more than your debt (" + debt + ").");
            System.out.println("You can only pay back " + debt);
            money -= debt;
            debt = 0;
        }
        // Checks if you're paying back more than you actually have
        else if(b > money)
        {
            System.out.println("The amount you are trying to pay back (" + b + ") is more than the money you have (" + money + ").");
            System.out.println("You can only pay back " + money + " right now.");
            debt -= money;
            money = 0;            
        }
        // Normal condition for paying back moneyz
        else
        {
            debt -= b;
            money -= b;
        }
    }

    // Allows the user to borrow money by adding to the debt and adding to the money
    public void borrow(double b)
    {
        debt += b;
        updateMoney(b);
    }

    // Makes equals() method to test if the name and password of a RoulettePlayer object
    public boolean equals(RoulettePlayer r)
    {
        if(r.name.equals(this.name) && r.password.equals(this.password))
        {
            return true;
        }
        return false;
    }

    // A simple toString method
    public String toString()
    {
        String player = "";
        player += "Name: " + name;
        player += "\t Money: " + money;
        player += "\t Debt: " + debt;
        return player;
    }

    // Simply checks if the player has money
    public boolean hasMoney()
    {
        if(money > 0)
        {
            return true;
        }
        return false;
    }
}