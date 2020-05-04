// RouletteWheel Class

import java.util.*;

public class RouletteWheel
{
    private Random randy = new Random();
    private int spin;
    private RColors color;
    private RRanges range;
    private RParities evensOdds;

    public RouletteWheel()
    {
        spin = 0;
        color = RColors.None;
        range = RRanges.None;
        evensOdds = RParities.None;
    }

    public RouletteResult spinWheel()
    {
        spin = randy.nextInt(36);

        // If it's 0, set everything to the correct special case
        if(spin == 0)
        {
            color = RColors.Green;
            range = RRanges.None;
            evensOdds = RParities.None;
        }

        // Set the correct range of the spin
        if(spin >= 1 && spin <= 18)
        {
            range = RRanges.Low;
        }
        else if(spin >= 19 && spin <= 36)
        {
            range = RRanges.High;
        }

        // Set the correct colors and ranges for the spin
        if((spin >= 1 && spin <= 10) || (spin >= 19 && spin <= 28))
        {
            if(spin % 2 == 0)
            {
                color = RColors.Black;
                evensOdds = RParities.Even;
            }
            else
            {
                color = RColors.Red;
                evensOdds = RParities.Odd;
            }
        }
        else if((spin >= 11 && spin <= 18) || (spin >= 29 && spin <= 36))
        {

            if(spin % 2 == 0)
            {
                color = RColors.Red;
                evensOdds = RParities.Even;
            }
            else
            {
                color = RColors.Black;
                evensOdds = RParities.Odd;
            }
        }

        RouletteResult newResult = new RouletteResult(color, range, evensOdds, spin);
        return newResult;
    }

    public int checkBet(RouletteBet b)
    {
        if(b.getBetType() == RBets.Value && Integer.parseInt(b.getBetValue()) == spin)
        {
            return 35;
        }
        else if((b.getBetType() == RBets.Color && RColors.valueOf(b.getBetValue()) == color) ||
                (b.getBetType() == RBets.Range && RRanges.valueOf(b.getBetValue()) == range) ||
                (b.getBetType() == RBets.Parity && RParities.valueOf(b.getBetValue()) == evensOdds))
        {
            return 1;
        }
        else    
        {
            return 0;
        }
    }
}