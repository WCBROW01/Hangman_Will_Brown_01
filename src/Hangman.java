import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Contains the game loop and methods for saving and loading the total wins and losses. 
 * @author Will Brown
 * @version 1.0
 */
public class Hangman {
	
	private int wins = 0, losses = 0; // Stores the wins and losses for the current session, also used to store the total once the user quits.
	private String currentWord; // The current word the user is trying to guess, taken from the dictionary.
	private Dictionary dictionary; // Instance of the dictionary.
	
	/**
	 * Creates a Dictionary object with the specified text file.
	 */
	public Hangman() {
		dictionary = new Dictionary("dictionary.txt");
	} // end constructor
	
	/**
	 * Loads all previously saved wins and losses from wl.txt and prints them. If the file doesn't exist yet, it is created.
	 * @throws IOException if there is an error reading wl.txt
	 * @throws NoSuchElementException if wl.txt is in a state where it cannot be read properly
	 */
	private void loadWL() throws IOException, NoSuchElementException {
		
		File wlFile = new File("wl.txt");
		
		// If the file doesn't exist yet or it is empty, skip reading and just use the stats from the current session.
		if (!wlFile.createNewFile() && wlFile.length() != 0) {
			Scanner wlScanner = new Scanner(wlFile);

			wins += wlScanner.nextInt();
			losses += wlScanner.nextInt();

			wlScanner.close();
		}
		
		System.out.print(wins == 1 ? "\nYou have a total of 1 win and " : "\nYou have a total of " + wins + " wins and ");
		System.out.println(losses == 1 ? "1 loss." : losses + " losses.");
		
	} // end loadWL
	
	/**
	 * Writes the new total of wins and losses to wl.txt
	 * @throws IOException if there is an error writing to wl.txt
	 */
	private void writeWL() throws IOException {
		
		FileWriter wlFileWriter = new FileWriter("wl.txt");
		
		wlFileWriter.write(wins + " " + losses);
		wlFileWriter.close();
		
	} // end writeWL
	
	/**
	 * Main game loop.
	 */
	public void playGame() {
		
		Scanner input = new Scanner(System.in);
		char playing; // Whether the user is currently playing.
		char guess; // The letter that the user guessed.
		int guessesRemaining; // The amount of remaining incorrect guesses.
		int letter; // An iterator through each character of the current word.
		boolean gameWon; // Whether the user has won the game.
		boolean letterCorrect; // Whether the user's guess was correct.
		boolean[] guessedLetters; // An array holding the state of which letters have been guessed.
		
		System.out.println("<<START>>");
		System.out.print("Would you like to play Y/N? ");
		playing = input.nextLine().strip().toUpperCase().charAt(0);
		
		while (playing == 'Y') {
			currentWord = dictionary.chooseWord();
			gameWon = false;
			guessesRemaining = 5;
			guessedLetters = new boolean[currentWord.length()];
			
			while (true) {
				if (!gameWon) {
					System.out.println("\nYou have " + guessesRemaining + " incorrect guesses left.");
				}
				
				for (letter = 0; letter < currentWord.length(); letter++) {
					System.out.print(guessedLetters[letter] ? currentWord.charAt(letter) + " " : "_ ");
				}
				
				System.out.println();
				
				if (gameWon) {
					wins++;
					System.out.println("\nYou won!");
					break;
				}
				
				System.out.print("\nWhat is your guess? ");
				guess = input.nextLine().strip().toUpperCase().charAt(0);
				
				letterCorrect = false; // This will be flipped if the user's guess was correct.
				gameWon = true; // This will be flipped if any letters still haven't been guessed.
				
				for (letter = 0; letter < currentWord.length(); letter++) {
					if (guess == currentWord.charAt(letter)) {
						letterCorrect = true;
						guessedLetters[letter] = true;
					} else if (!guessedLetters[letter]) {
						gameWon = false;
					}
				}
				
				/*
				 *  This exploits how Java checks conditional statements. Since this is an AND statement, if the first condition is false, then it won't even check the second one.
				 *  Because of this, the guessesRemaining variable won't even be checked or decremented unless letterCorrect is false.
				 */
				if (!letterCorrect && guessesRemaining-- == 0) {
					losses++;
					System.out.println("\nYou are out of guesses! You lost!");
					break;
				}

			}
			
			System.out.print("\nWould you like to play again Y/N? ");
			playing = input.nextLine().strip().toUpperCase().charAt(0);
			
		}
		
		System.out.print(wins == 1 ? "\nYou had 1 win and " : "\nYou had " + wins + " wins and ");
		System.out.println(losses == 1 ? "1 loss this round." : losses + " losses this round.");
		
		try {
			loadWL();
			writeWL();
		} catch (IOException | NoSuchElementException e) {
			System.out.println("\nThere was a problem accessing saved wins and losses. This round will not be saved.");
		}
		
		System.out.println("<<END>>");
		
	} // end playGame
	
}
