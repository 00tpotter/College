// The Panel which will hold the RouletteWheel and buttons to play the game
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoulettePanel extends JPanel implements Activatable
{
    private ButtonPanelType buttonPanel; // left panel
    private SpinPanelType spinPanel; // right panel

    private RoulettePlayer player1;
    private RoulettePanelTest rpTest;
    private RouletteWheel newWheel;

    private Activatable activia;
    private boolean canContinue;

    private final Font jawnt = new Font("Serif", Font.BOLD, 60);

    public RoulettePanel(RoulettePlayer P, RoulettePanelTest R) // initializes all instance variables and adds the panels to the RoulettePanel
    {
        player1 = P;
        rpTest = R;
        canContinue = false;

        buttonPanel = new ButtonPanelType();
        spinPanel = new SpinPanelType();

        add(buttonPanel, BorderLayout.WEST);
        add(spinPanel, BorderLayout.EAST);
    }

    public void activate()
    {
        canContinue = true;
    }

    private class ButtonPanelType extends JPanel // Panel that holds all the buttons on the left
    {
        private JLabel text;
        private JButton makeBet;
        private JButton spinWheel;
        private JButton showMyInfo;
        private JButton quit;

        public ButtonPanelType()
        {
            setLayout(new GridLayout(5, 1));

            text = new JLabel("ARE YOU READY TO PLAY, HERBERT???");
            makeBet = new JButton("Make Bet");
            makeBet.setFont(jawnt);
            makeBet.addActionListener(new thisListener());
            spinWheel = new JButton("Spin Wheel");
            spinWheel.setFont(jawnt);
            spinWheel.addActionListener(new thisListener());
            showMyInfo = new JButton("Show My Info");
            showMyInfo.setFont(jawnt);
            showMyInfo.addActionListener(new thisListener());
            quit = new JButton("Quit");
            quit.setFont(jawnt);
            quit.addActionListener(new thisListener());

            add(text);
            add(makeBet);
            add(spinWheel);
            add(showMyInfo);
            add(quit);

            spinWheel.setEnabled(false);
        }

        private class thisListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == makeBet) // listens for the button to be clicked and allows the user to enter their bet information like normal
                {
                    Double betAmount = Double.valueOf(JOptionPane.showInputDialog("Enter the amount you would like to bet (Cannot exceed 250.0): "));
                    
                    while(betAmount > player1.getMoney() || betAmount < 0)
                    {
                        betAmount = Double.valueOf(JOptionPane.showInputDialog("Enter the amount you would like to bet (Cannot exceed 250.0): "));
                    }

                    String betTypeString = JOptionPane.showInputDialog("Enter the type of bet you would like (Value, Color, Range, Parity): ");
                    RBets betType = null;
                    String betValue = "";
                    
                    if(betTypeString.equals("Value"))
                    {
                        betType = RBets.Value;
                        betValue = JOptionPane.showInputDialog("Enter the value you'd like to bet on: ");
                    }
                    else if(betTypeString.equals("Color"))
                    {
                        betType = RBets.Color;
                        betValue = JOptionPane.showInputDialog("Enter the color you'd like to bet on: ");
                    }
                    else if(betTypeString.equals("Range"))
                    {
                        betType = RBets.Range;
                        betValue = JOptionPane.showInputDialog("Enter the range you'd like to bet on: ");
                    }
                    else if(betTypeString.equals("Parity"))
                    {
                        betType = RBets.Parity;
                        betValue = JOptionPane.showInputDialog("Enter the parity you'd like to bet on: ");
                    }                 
                    spinWheel.setEnabled(true);   
                }
                else if(e.getSource() == spinWheel) // spins the wheel visually and blocks out other buttons
                {
                    makeBet.setEnabled(false);
                    spinWheel.setEnabled(false);
                    quit.setEnabled(false);
                    newWheel.spin();
                    //newWheel.getResult();
                }
                else if(e.getSource() == showMyInfo) // shows the player info
                {
                    JOptionPane.showMessageDialog(null, "Here's your info: \n" + player1.toString());
                }
                else if(e.getSource() == quit) // quits the program
                {
                    System.exit(0);
                }
            }
        }
    }

    private class SpinPanelType extends JPanel
    {
        public SpinPanelType() // creates the RouleteeWheel that is to be spun
        {
            newWheel = new RouletteWheel(activia);
            add(newWheel);
        }

        /*private class thisListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {

            }
        }*/
    }
}