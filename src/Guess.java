// Bao-lim Smith
// 43277047

import java.io.*;
import java.util.ArrayList;

class Guess
{
	Animal anl = new Animal();
	QuestionNode[] qNS = new QuestionNode[3];

	static String DEFAULT_DATA = "animalData"; // Use this for setting up a game.
	
	void makeTree(String _fileName)
	{
		// This takes the data from a saved game in the file _fileName and builds a game tree ready for playing.
		boolean checkInputFile = false;
		String loadFileName= _fileName; // saveFileName= _fileName; 
		// At the game level.
		BufferedReader inputTree = null;
		try 
		{
			inputTree =	new BufferedReader (new FileReader(loadFileName));
			checkInputFile = true;
		} catch(Exception ee) { System.out.println("File not found"); }
		
		QuestionNode temp = new QuestionNode("", null, null);
		if (checkInputFile) 
		{
			System.out.println("Loading tree from textfile '"+loadFileName+ "'");
			for (int i = 0; i < 3; i++) 
				qNS[i] = temp.Load(inputTree);
			for (int i = 0; i < 3; i++) 
			{
				try {
					Thread.sleep(1000);  // For dramatic effect ...
					System.out.print('.');
				} catch(InterruptedException e) { e.printStackTrace(); }
			}
			System.out.println("done.");
		} 
		else
			System.out.println("No file '"+loadFileName+"' found: starting from scratch.");
	}
	
	
	// Recursive function that plays the game. 

	// Takes a QuestionNode qN and a BufferedReader in (normally defined to be standard input)
	// and plays the game. The BufferedReader takes responses from the player (yes/no) and
	// data for a new node if necessary.
	void Play(QuestionNode qN, BufferedReader in) 
	{		
		String objectName = null;
		String objectQuestion = null;
		String answer = null;
		String append = " (yes, no)";

		if (qN != null) // Could probably remove this?
		{
			System.out.println(qN.text + append);

			while (true)
			{
				// Gets the users answer.
				answer = takeAnswer(answer, in);
				
				if (answer.equals("yes"))
				{
					if (qN.yes != null)
					{
						// Prints the successive question.
						System.out.println(qN.yes.text + append);
						// Moves you down the tree.
						qN = qN.yes; 
					}
					else
					{
						System.out.println("Great! Let's try again.");
						// Exit game.
						break; 
					}
				}
				else if (answer.equals("no"))
				{
					if(qN.no != null)
					{
						// Prints the successive question.
						System.out.println(qN.no.text + append);
						// Traverses you down the tree.
						qN = qN.no;
					}
					else
					{
						// Object not in tree.
						System.out.println("Then, what is it?");

						// Determines the object's name.
						try
						{ 
							objectName = in.readLine().trim(); 
						} catch(IOException e) { e.printStackTrace(); }

						System.out.println("so it's a " + objectName + " then.");
						System.out.println("What is a 'yes' question for " + objectName + "?");

						// Takes the question for the new object.
						try
						{ 
							objectQuestion = in.readLine().trim(); 
						} catch(IOException e) { e.printStackTrace(); }

						// Defines a new node for the question.	
						String temp = qN.text;
						qN.text = objectQuestion;
						qN.yes = new QuestionNode("Is it " + objectName + "?", null, null);
						qN.no = new QuestionNode(temp, null, null);

						System.out.println("Ok, let me have another go.");
						// Exit session.
						break; 
					}
				}
			}		
		}	
	}
	
	// Takes user input to determine what the answer to the question is.
	String takeAnswer(String answer, BufferedReader in)
	{
		while (true)
		{
			try
			{ 
				answer = in.readLine().trim(); 
			} catch(IOException e) { e.printStackTrace(); }

			if ("yes".compareToIgnoreCase(answer) == 0 || "no".compareToIgnoreCase(answer) == 0)
				return answer;
			else
				System.out.println("Please answer with a yes or a no.");
		}

	}

	// This takes the name of an output file sFN and the Animal-Vegetable-Mineral
	// game trees stored in qNS[] and saves it serially into sFN so that
	// the Load and makeTree methods together can restore it exactly to qNS[]	
	void save(String sFN, QuestionNode qNS[])
	{ 		
		String fileName = sFN;
		String lineBreak = "\n";
		String T = "T: ";
		String N = "N: ";
		String indent = "";
		sFN = "";
		
		// Formats the string into sFN.
		for (int i = 0; i < qNS.length; i++)
		{
			if (qNS[i] != null)
			{			
				QuestionNode qN = qNS[i];

				// Creates a String to be written into the file. 
				indent = "";
				sFN = saveNodes(sFN, qN, lineBreak, T, N, indent);
				sFN += N + lineBreak + N + lineBreak;			
			}	
		}
				
		// Writes sFN to a text file. 
		BufferedWriter writer = null;
		try{
			File file = new File(fileName);
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(sFN);
		} catch ( IOException e){ e.printStackTrace(); }
		finally
		{
			try
			{
				if (writer != null)
					writer.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	// Properly creates sFN to a specified format from the tree. 
	String saveNodes(String sFN, QuestionNode qN, String lineBreak, String T, String N, String indent)
	{		
		// Adds the value of the node to the String.
		sFN += indent + T + qN.text + lineBreak;
		
		// Increment indent correctly. 
		indent += "  ";
		
		// Traverses the tree.
		// Go down left node.
		if (qN.yes != null)
			sFN = saveNodes(sFN, qN.yes, lineBreak, T, N, indent);
		else // If we're at a leaf. 
			sFN += indent + N + lineBreak;
		
		// Go down right node.
		if (qN.no != null)
			sFN = saveNodes(sFN, qN.no, lineBreak, T, N, indent);
		else 
			sFN += indent + N + lineBreak;		
		
		return sFN;
	}

 	void mergeGames(Guess q1, Guess q2) 
 	{
 		// Assume that these two nodes are not null.
 		if (q1 != null && q2 != null) 
 		{
 			qNS[0] = merge(q1.qNS[0], q2.qNS[0]);
 			qNS[1] = merge(q1.qNS[1], q2.qNS[1]);
 			qNS[2] = merge(q1.qNS[2], q2.qNS[2]);
 		}
 	}
	   
	// Take two separate databases (outputs from the game "main") and merge them
	// into one. The idea is that several groups can play separately (initially
	// individual people) and then by merging again and again make bigger
	// and bigger databases that will be interesting/amusing to a larger
	// constituency.
	QuestionNode merge(QuestionNode qN1, QuestionNode qN2)
	{		
		// Checks for nullity of the nodes.
		if (qN1 == null && qN2 == null)
			return null;
		else if (qN1 == null && qN2 != null)
			return qN2;
		else if (qN1 != null && qN2 == null)
			return qN1;
		else if (qN1.text.equals(qN2.text))
			qN1 = new QuestionNode(qN1.text, merge(qN1.yes, qN2.yes), merge(qN1.no, qN2.no));
		// Both guess nodes.
		else if (qN1.yes == null && qN2.yes == null) 
			qN1 = new QuestionNode(qN1.text, null, qN2);
		// qN1 is a guess node.
		else if (qN1.yes == null && qN1.no == null) 
			qN1 = new QuestionNode(qN2.text, merge(qN1, qN2.yes), merge(qN1, qN2.no));
		// qN2 is a guess node.
		else if (qN2.yes == null && qN2.no == null) 
			qN1 = new QuestionNode(qN1.text, merge(qN1.yes, qN2), merge(qN1.no, qN2));
		// Both question nodes.
		else 
			qN1 = new QuestionNode(qN1.text, merge(qN2, qN1.yes), merge(qN2, qN1.no));	
		
		return qN1;
	}
		
	// For use after a merge, to remove duplicate answers.
	QuestionNode clean(QuestionNode qN, String answersSoFar) 
	{		
		// Quick-checking. 
		if (qN == null)
			return null;
		
		// For checking what questions have been found in the array. 
		ArrayList<String> questions = new ArrayList<String>();
		// What the previous node that you may change was. 
		String whichNode = "";
		
		// Adds the base question nodes.
		questions.add(qN.text);
		if (qN.yes.text != qN.text)
			if (qN.yes.text.subSequence(0, 4).equals("does"))
				questions.add(qN.yes.text);
		if (qN.no.text != qN.text && qN.no.text != qN.yes.text)
			if (qN.no.text.subSequence(0, 4).equals("does"))
				questions.add(qN.no.text);
		
		// Since we don't want to remove the first yes/no question. 
		// Cleans the left side of the tree.
		whichNode = "yes";
		qN.yes.yes = cleanNodes(qN.yes.yes, questions, whichNode);
		whichNode = "no";
		qN.yes.no = cleanNodes(qN.yes.no, questions, whichNode);

		// Cleans the right side of the tree. 
		whichNode = "yes";
		qN.no.yes = cleanNodes(qN.no.yes, questions, whichNode);
		whichNode = "no";
		qN.no.no = cleanNodes(qN.no.no, questions, whichNode);
		
		// Returns the cleaned tree. 
		return qN;
	}
	
	// Recursive method that Creates an ArrayList of questions from the tree, and then 
	// checks to see if another node contains the same question, and if so removes that 
	// node. Then returns the section of that tree once cleaned. 
	QuestionNode cleanNodes(QuestionNode qN, ArrayList<String> key, String whichNode)
	{
		// Makes things look nicer. 
		String temp = qN.text.trim();

		// Determines if the question is already in key or not. 
		if (temp.subSequence(0, 4).equals("does"))
		{
			for (int i = 0; i < key.size(); i++)
			{
				// Duplicate question node found.
				if (key.get(i).equals(temp))
				{
					if (qN.yes == null && qN.no == null)
						qN = null;
					else if (qN.yes == null && qN.no != null) 
						qN = new QuestionNode(qN.no.text, null, qN.no.no);
					else if (qN.yes != null && qN.no == null)
						qN = new QuestionNode(qN.yes.text, qN.yes.yes, null);
					// Neither node is null, and the replacing node should be replaced
					// with a "yes" node.
					else if (whichNode.equals("yes"))
						qN = new QuestionNode(qN.yes.text, qN.yes.yes, qN.yes.no);
					// Node should be updated with next "no" nodes values. 
					else if (whichNode.equals("no"))
						qN = new QuestionNode(qN.no.text, qN.no.yes, qN.no.no);

					// QuestionNode properly created, exit loop. 
					break; 
				}	
				// New question found. 
				else if (i == key.size() - 1)
				{
					key.add(temp);
					break;
				}
			}
		}

		// Go down "yes" node if not null.
		if (qN.yes != null)
		{
			whichNode = "yes";
			cleanNodes(qN.yes, key, whichNode);
		}
		// Go down "no" node if not null.
		if (qN.no != null)
		{
			whichNode = "no";
			cleanNodes(qN.no, key, whichNode);
		}

		return qN;
	}
	
	
}
