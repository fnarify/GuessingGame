import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class playGame 
{
	
	// This is the main routine for playing the game: it handles the initialisation, saving and quitting.
	// It calls methods in the class Guess to perform the load from- and save to text files,
	// using the default filenames "animalData" for loading and "savedGame" for saving.
	//
	// If you want to change the input then you can change the data in the file "animalData",
	// but make sure it abides by the expected format: otherwise you will get an error.
	
	public static void main(String[] args) 
	{
		Guess game = new Guess();
		BufferedReader in = null;
		String saveFileName = "savedGame";
		String loadFileName = "animalData"; 
		
		try 
		{ 
			in = new BufferedReader(new InputStreamReader(System.in)); 
		} catch(Exception ee) { System.out.println("File not found"); }	
		
		// Calls the makeTree method in Guess
		game.makeTree(loadFileName); 

		while (true) 
		{
			int i = game.anl.getAnswer("Is it animal, vegetable or mineral?", "animal,vegetable,mineral,save,quit", in);
			if (i == 3) 
			{
				game.save(saveFileName, game.qNS);
				continue;
			}
			if (i == 4) 
			{
				System.out.println("Quitting: ");
				break;
			}  
			QuestionNode qN = game.qNS[i]; // qN now the node we're using for this round.
		
			// This branch not yet defined.
			if (qN == null) 
			{ 
				System.out.println("What is it? ");
				String firstThing = ""; 
				
				try
				{ 
					firstThing = in.readLine().trim(); 
				} catch(IOException e) { e.printStackTrace(); }
				
				game.qNS[i] = new QuestionNode("Is it " + firstThing + "?", null, null);
				System.out.println("It's " + firstThing + ", eh? Now that's settled, let's try again.");
			}
			else 
				game.Play(qN, in); // This branch already defined, so play it.
		}		
	}
}
