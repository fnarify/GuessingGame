import java.io.*;
public class QuestionNode 
{
	// This is the basic binary tree node. Its data field is a string
	// to store questions and guesses.
	
	String text;
	QuestionNode yes, no;
	
	QuestionNode(String t, QuestionNode y, QuestionNode n) 
	{
		text = t;
		yes = y;
		no = n;
	}

	//Read in serialised form of the tree from a file myIn.
	QuestionNode Load(BufferedReader myIn) 
	{ 
		String line = null;

		try 
		{ 
			line= myIn.readLine(); 
		} catch(IOException e) { e.printStackTrace(); }  

		line = line.trim(); // Skip leading and following blanks
		
		if (line.substring(0,2).compareTo("N:") == 0) 
			return null;
		else if (line.substring(0,2).compareTo("T:") == 0) {
			return new QuestionNode(line.substring(2,line.length()).trim(), Load(myIn), Load(myIn));
		} 
		else 
			System.out.println("Load error.");
		
		return null; // To supress compile-time warning; it can "never" get here.
	}
}	   

