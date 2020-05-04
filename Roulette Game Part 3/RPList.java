// RPList

import java.util.*;
import java.io.*;

public class RPList
{
    private RoulettePlayer [] rPlayers;
    private int logSize;
    private String fileName;

    public RPList(String fN)
    {
        fileName = fN;

        try
        {
            File newFile = new File(fN);
            Scanner fileInput = new Scanner(newFile);
            logSize = fileInput.nextInt();
            fileInput.nextLine();
            rPlayers = new RoulettePlayer[logSize];

            for(int i = 0; i < logSize; i++)
            {
                String player = fileInput.nextLine();
                String [] playerInfo = player.split(",");
                rPlayers[i] = new RoulettePlayer(playerInfo[0], playerInfo[1], Double.valueOf(playerInfo[2]), Double.valueOf(playerInfo[3]));
                if(playerInfo.length > 4)
                {
                    Question q1 = new Question(playerInfo[4], playerInfo[5]);
                    Question q2 = new Question(playerInfo[6], playerInfo[7]);
                    Question [] ques = new Question[2];
                    ques[0] = q1;
                    ques[1] = q2;
                    rPlayers[i].addQuestions(ques);
                }
            }
            fileInput.close();
        }   
        catch(FileNotFoundException e)
		{
		    System.out.println("Nope.");
		}
    }

    public int getSize()
    {
        return logSize;
    }

    public int getASize()
    {
        return rPlayers.length;
    }

    public boolean checkId(String name)
    {
        for(int i = 0; i < logSize; i++)
        {
            if(rPlayers[i].getName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    public RoulettePlayer getPlayerPassword(String name, String pass)
    {
        for(int i = 0; i < logSize; i++)
        {
            if(rPlayers[i].getName().equals(name) && rPlayers[i].getPassword().equals(pass))
            {
                return rPlayers[i];
            }
        }
        return null;
    }

    public RoulettePlayer getPlayerQuestions(String name, Question [] quest)
    {
        for(int i = 0; i < logSize; i++)
        {
            if(rPlayers[i].getName().equals(name) && rPlayers[i].hasQuestions())
            {
                if(rPlayers[i].matchQuestions(quest))
                {
                    return rPlayers[i];
                }
            }
        }
        return null;
    }

    public boolean add(RoulettePlayer newPlayer)
    {
        if(logSize == rPlayers.length)
        {
            resizeArray(rPlayers);
        }

        for(int i = 0; i < logSize; i++)
        {
            if(newPlayer.getName().equals(rPlayers[i].getName()))
            {
                return false;
            }
        }

        logSize++;
        rPlayers[logSize - 1] = newPlayer;
        return true;
    }

    public void resizeArray(RoulettePlayer [] oldRP)
    {
        int oldLength = rPlayers.length;
        int newLength = oldLength * 2;
        RoulettePlayer [] temp = new RoulettePlayer[newLength];

        for(int i = 0; i < oldRP.length; i++)
        {   
            temp[i] = oldRP[i];
        }

        rPlayers = temp;
    }

    public String [] getQuestions(String name)
    {
        for(int i = 0; i < logSize; i++)
        {
            if(rPlayers[i].getName().equals(name))
            {
                return rPlayers[i].getQuestions();
            }
        }
        return null;
    }

    public void saveList()
    {
        try
        {
            File newFile = new File(fileName);
            newFile.createNewFile();
            PrintWriter save = new PrintWriter(newFile);
            save.println(logSize);
            for(int i = 0; i < logSize; i++)
            {
                save.println(rPlayers[i].saveString());
            }
            save.close();
        }
        catch (IOException e)
		{
			System.out.println("Nah.");
		}
    }

    public String toString()
    {
        String s = "Players: \n\t";
        for(int i = 0; i < logSize; i++)
        {
            s += rPlayers[i].toString() + "\n\t";
        }
        return s;
    }
}