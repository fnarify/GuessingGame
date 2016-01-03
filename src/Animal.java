import java.io.*;

//Question/answer utility routine.
public class Animal 
{ 
	// Needs to have a reader passed to it
	int getAnswer(String question, String answers, BufferedReader in) 
	{ 
		String[] answersList = answers.split(",");
		String ans = null;
		for (;;) 
		{
			System.out.println(question + " (" + answers + ") ");
			try 
			{
				ans= in.readLine().trim();
			} catch(IOException e) { e.printStackTrace(); }
			if (ans.length() != 0) 
				break;
		}
		
		int ansIndex= 0;  
		for (int i = 0 ; i < answersList.length; i++) 
			if (answersList[i].compareTo(ans) == 0)  
				ansIndex = i;  // Count the ','s.
		
		return ansIndex;
	}
}