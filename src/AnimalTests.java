import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class AnimalTests {
	
	@Test	
	public void testPlaySimpleGame() {
		// Checks to see whether the game can be played without crashing
		Guess game= new Guess();
		BufferedReader in= null;
		try {
			in = new BufferedReader ( // We take input from the text file "offlinePlayer1" rather than standard input
					new FileReader("offlinePlayer1"));
		} catch(Exception ee) { 
			System.out.println("File not found");	
		}	
		String loadFileName= "animalData";
		game.makeTree(loadFileName); // Calls the makeTree method in Guess

		while (true) {
			int i= game.anl.getAnswer("Is it animal, vegetable or mineral?", "animal,vegetable,mineral,save,quit", in);
			if (i == 3) {
				game.save("checkFile", game.qNS);
				continue;
			}
			if (i == 4) {
				System.out.println("Quitting: ");
				break;
			}  
			QuestionNode qN= game.qNS[i]; // game tree qN is now the one we're using for this round.
	
			if (qN == null) { // Has this branch been defined?
				System.out.println("What is it? ");
				String firstThing= "";
				try{
					firstThing= in.readLine().trim();
				}  catch(IOException e) { e.printStackTrace();
				}
				qN= new QuestionNode("Is it "+firstThing+"?", null, null);
				System.out.println("It's "+firstThing+", eh? Now that's settled, let's try again.");
			}
			else  
				game.Play(qN, in); // It has been defined, so play it.
		}
		try{
			assertEquals(in.readLine().trim(),"check"); // Check played the game to the end
			}  catch(IOException e) { e.printStackTrace();
		}	
	}
	
	@Test	
	public void testPlayandAddNodeSimple() { 
		// Can you play the game and add a new node without crashing		
		Guess game= new Guess();
		BufferedReader in= null;
		try {
			in = new BufferedReader ( // We take input from the file rather than standard input
					new FileReader("offlinePlayer2"));
		} catch(Exception ee) { 
			System.out.println("File not found");	
		}	
			
		String loadFileName= "animalData";  // Default file names.
		game.makeTree(loadFileName); // Calls the makeTree method in Guess

		while (true) {
			int i= game.anl.getAnswer("Is it animal, vegetable or mineral?", "animal,vegetable,mineral,save,quit", in);
			if (i == 3) {
				game.save("checkfile", game.qNS);
				continue;
			}
			if (i == 4) {
				System.out.println("Quitting: ");
				break;
			}  
			QuestionNode qN= game.qNS[i]; // qN now the one we're using for this round.
		
			if (qN == null) { // Has this branch been defined?
				System.out.println("What is it? ");
				String firstThing= "";
				try{
					firstThing= in.readLine().trim();
				}  catch(IOException e) { e.printStackTrace();
				}
				qN= new QuestionNode("Is it "+firstThing+"?", null, null);
				System.out.println("It's "+firstThing+", eh? Now that's settled, let's try again.");
			}
			else game.Play(qN, in); // It has been defined, so play it.
		}
		try{
			assertEquals(in.readLine().trim(),"check"); // Check played the game to the end.
			}  catch(IOException e) { e.printStackTrace();
		}
	}
	
	@Test	
	public void testPlayandAddNode() {
		// Same as before but check that the node is properly installed in the tree
		Guess game= new Guess();
		BufferedReader in= null;
		//boolean checkInputFile= false;
		try {
			in = new BufferedReader ( // We take input from the text file rather than standard input
					new FileReader("offlinePlayer3"));
		} catch(Exception ee) { 
			System.out.println("File not found");	
		}	
	
		
		System.out.println("loaded the tree");
		String loadFileName= "animalData";  // Default file names.
		game.makeTree(loadFileName); // Calls the makeTree method in Guess

		while (true) {
			int i= game.anl.getAnswer("Is it animal, vegetable or mineral?", "animal,vegetable,mineral,save,quit,check", in);
			if (i==5) {
				// Go into the tree and check that the new node has been properly installed.
				QuestionNode temp= game.qNS[0];
				temp= temp.no;
				temp= temp.no;
				temp= temp.yes;
				assertEquals(temp.text, "does it say moo?"); // Make sure this matches the testdata
				temp= temp.yes;
				assertEquals(temp.text, "Is it a cow?");
				break;
			}
			
			if (i == 3) {
				game.save("checkfile", game.qNS);
				continue;
			}
			if (i == 4) {
				System.out.println("Quitting: ");
				break;
			}  
			QuestionNode qN= game.qNS[i]; // qN now the one we're using for this round.

		
			if (qN == null) { // Has this branch been defined?
				System.out.println("What is it? ");
				String firstThing= "";
				try{
					firstThing= in.readLine().trim();
				}  catch(IOException e) { e.printStackTrace();
				}
				qN= new QuestionNode("Is it "+firstThing+"?", null, null);
				System.out.println("It's "+firstThing+", eh? Now that's settled, let's try again.");
			}
			else game.Play(qN, in); // It has been defined, so play it.
		}	 
	}
	
	@Test
	public void testSaveMaybe() {
		Guess game= new Guess();
		String loadFileName= "animalSave";  // Default file names.
		game.makeTree(loadFileName); // Calls the makeTree method in Guess
		

		String testOut= "checkfile";
		game.save(testOut, game.qNS); // Save back using the saveMaybe method
		
		// read it back an check line-by-line		
		BufferedReader in1= null;
		BufferedReader in2= null;
		//boolean checkInputFile= false;
		try {
			in1 = new BufferedReader ( // original file
					new FileReader(loadFileName));
			in2 = new BufferedReader ( // saved file
					new FileReader(testOut));
		} catch(Exception ee) { 
			System.out.println("File not found");	
		}
		
		String line1=null;
		String line2=null;
		for(int i= 1; i < 25; i++){ // This is the number of lines in the file
			try{
				line1= in1.readLine().trim();
				line2= in2.readLine().trim();
			} catch (Exception ee) {
				System.out.println("read error");
			}		
			assertEquals(line1, line2);
		}		
	}
	
	@Test
	public void testMerge() { 
		
		// Build the first tree
		Guess game1= new Guess();
		String loadFileName1= "animalMerge";  // Default file names.
		game1.makeTree(loadFileName1); // Calls the makeTree method in Guess
		
		// Build the second tree
		Guess game2= new Guess();
		String loadFileName2= "animalData2";  // Default file names.
		game2.makeTree(loadFileName2); // Calls the makeTree method in Guess
		
		// merge the two trees
		
		QuestionNode merged= game2.merge(game1.qNS[0], game2.qNS[0]);		
		// Check that the tree is the correct merge				
		assertEquals("does it eat honey?", merged.text);
		assertEquals("does it snort?", merged.yes.text);
		assertEquals("Is it a pig?", merged.yes.yes.text);
		assertEquals("Is it a person?", merged.yes.no.text);
		assertEquals("does it swim?", merged.no.text); // and so on
		assertEquals("Is it a fish?", merged.no.yes.text);
		assertEquals("Is it a worm?", merged.no.no.text);
		assertEquals("Is it a bear?", merged.yes.yes.no.text);
		assertEquals("Is it a bear?", merged.yes.no.no.text);
	}
	
	@Test
	public void testClean() {
		// test that clean works on the merged file
		
		System.out.println("Loading mergedDataC");
		Guess game1= new Guess();
		String loadFileName1= "mergedDataC"; // This is  saved game after merging two user games
		game1.makeTree(loadFileName1); // Calls the makeTree method in Guess		
		String answers= "";
		game1.qNS[0]= game1.clean(game1.qNS[0], answers); // clean it up
		// check to see that repeated questions removed
		
		assertEquals(game1.qNS[0].yes.no.text, "Is it a lion?");// more checks	
		assertEquals(game1.qNS[0].no.no.text, "Does it have a trunk?");
	}
}


