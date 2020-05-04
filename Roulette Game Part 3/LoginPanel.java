// JPanel that allows a user to log in with the correct name and password

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel
{
    // List and interface that will be initialized by constructor
    private RPList theList;
    private LoginInterface theLog;

    // All the objects necessary to log in
    private JLabel jayTitle;
    private JLabel userLabel;
    private JLabel passLabel;
    private JTextField userText;
    private JTextField passText;
    private JButton submitButton;

    private final Font jawnt = new Font("Serif", Font.BOLD, 60);

    public LoginPanel (RPList listOfRP, LoginInterface L) // initializes all instance variables
    {
        theList = listOfRP;
        theLog = L;

        setLayout(new GridLayout(4, 2));
        jayTitle = new JLabel("Please log into the site");
        jayTitle.setFont(jawnt);
        userLabel = new JLabel("User ID: ");
        userLabel.setFont(jawnt);
        passLabel = new JLabel("Password: ");
        passLabel.setFont(jawnt);
        userText = new JTextField(15);
        passText = new JPasswordField(15);
        submitButton = new JButton("Submit");
        submitButton.setFont(jawnt);
        submitButton.addActionListener(new theListener()); // the only thing needed to be listened to is the button

        add(jayTitle);
        add(new JLabel()); // filler space for formatting
        add(userLabel);
        add(userText);
        add(passLabel);
        
        add(passText);
        add(submitButton);
    }

    class theListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == submitButton)
            {
                String name = userText.getText();
                String pass = passText.getText();
                if(!(theList.checkId(name))) // checks name to see if it works
                {
                    JOptionPane.showMessageDialog(jayFlannel, "Enter a valid username.");
                }
                else if(theList.getPlayerPassword(name, pass) == null) // checks password
                {
                    JOptionPane.showMessageDialog(jayFlannel, "Enter a valid password.");
                }
                else
                {
                    theLog.setPlayer(theList.getPlayerPassword(name, pass)); // sets the player when it is correctly entered
                }
            }
        }
    }
}