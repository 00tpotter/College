// The individual squares that are to go in the RouletteWheel
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  

public class RouletteSquare extends JLabel
{
    private int squareValue;
    private final Font jawnt = new Font("Serif", Font.BOLD, 60);

    public RouletteSquare(int val) // initializes the instance variable
    {
        squareValue = val;
        setText(Integer.toString(squareValue));
        setFont(jawnt);
        setHorizontalAlignment(SwingConstants.CENTER);

        if(squareValue == 0) // SImply sets each square to its appropriate color
        {
            setForeground(Color.green);
        }
        else if(((squareValue >= 1 && squareValue <= 10) || (squareValue >= 19 && squareValue <= 28)) && squareValue % 2 != 0)
        {  
            setForeground(Color.red);
        }
        else if(((squareValue >= 11 && squareValue <= 18) || (squareValue >= 29 && squareValue <= 36)) && squareValue % 2 == 0)
        {
            setForeground(Color.red);
        }
        else
        {
            setForeground(Color.black);
        }
    }

    public int getValue() // returns the value of the square
    {
        return squareValue;
    }

    public RColors getColor() // returns its color
    {
        if(getForeground() == Color.green)
        {
            return RColors.Green;
        }
        if(getForeground() == Color.red)
        {
            return RColors.Red;
        }
        else
        {
            return RColors.Black;
        }
    }

    public RParities getParity() // returns its parity
    {
        if(squareValue == 0)
        {
            return RParities.None;
        }
        else if(squareValue % 2 == 0)
        {
            return RParities.Even;
        }
        else
        {
            return RParities.Odd;
        }
    }

    public RRanges getRange() // returns its range
    {
        if(squareValue == 0)
        {
            return RRanges.None;
        }
        else if(squareValue >= 1 && squareValue <= 18)
        {
            return RRanges.Low;
        }
        else
        {
            return RRanges.High;
        }
    }

    public boolean isChosen() // returns if it is selected or not
    {
        if(getBackground() == Color.blue)
        {
            return true;
        }
        return false;
    }

    public void choose() // selects it
    {
        setOpaque(true);
        setBackground(Color.blue);
    }

    public void unChoose() // unselects it
    {
        setOpaque(false);
        setBackground(Color.white);
    }
}