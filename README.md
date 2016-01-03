# GuessingGame

Simple guessing game in the vein of Ask Jeeves. The structure of the questions asked are as a binary tree, and example files 
are provided to show how you would write your own default questions. Each time a question is asked and the program cannot guess 
what the object is, then it is added to the current tree and the user inputs a question that would help specify what it is. When 
playing a game you can choose to save the current game which will save all current choices so they can be loaded again at a later date.

Currently, the file being saved to and the file being loaded from at default are different. To make it so that they are the same, that 
is any saved choices are loaded by default when a new game is run elsewhere, what needs to be changed is in *PlayGame.java*, 
both saveFileName and loadFileName should be set to the same String to have that functionality.
