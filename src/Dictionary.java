import java.io.File;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Code for loading the dictionary into memory and picking a random word.
 * @author Will Brown
 * @version 1.0
 */

public class Dictionary {
	
	private String[] wordList = new String[200];
	private int currentCard; // Currently chosen word from the array
	private SecureRandom randomNumbers = new SecureRandom();
	
	/**
	 * Sends the text file containing the dictionary to the readFile method and catches any errors. If there is an error here, the game can't even start.
	 * @param fileName Path to the text file containing the dictionary
	 */
	public Dictionary(String fileName) {
		try {
			readFile(fileName);
		} catch (FileNotFoundException e) {
			System.out.println("There is no dictionary file at \"" + fileName + "\". The game will not start.");
			System.exit(1);
		} catch (NoSuchElementException e) {
			System.out.println("There are less than 200 lines in the dictionary file at \"" + fileName + "\". The game will not start.");
			System.exit(1);
		}
	}
	
	/**
	 * Reads 200 lines of the specified text file and inserts them into an array.
	 * @param fileName Path to the text file containing the dictionary
	 * @throws FileNotFoundException if the specified text file does not exist
	 * @throws NoSuchElementException if there are less than 200 lines in the text file
	 */
	private void readFile(String fileName) throws FileNotFoundException, NoSuchElementException {
		Scanner fileScanner = new Scanner(new File(fileName));
		
		for (int wordCount = 0; wordCount < 200; wordCount++) {
			wordList[wordCount] = fileScanner.nextLine().toUpperCase();
		}
	}
	
	/**
	 * Picks a random word from the dictionary and returns it to the Hangman game.
	 * @return a random word from the dictionary (in uppercase so the game doesn't need to manipulate it at all)
	 */
	public String chooseWord() {
		currentCard = randomNumbers.nextInt(200);
		return wordList[currentCard].toUpperCase();
	}
	
}
