/**
 * Entrypoint of the program, loads an instance of the Hangman game.
 * @author Will Brown
 * @version 1.0
 */
public class Application {

	/**
	 * Main method, loads the main game loop.
	 * @param args
	 */
	public static void main(String[] args) {
		new Hangman().playGame();
	} // end main

} // end class
