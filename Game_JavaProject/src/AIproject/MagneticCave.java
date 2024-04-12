//Amal Butmah 1200623
//Layan Shoukri 1201225

package AIproject;

import java.util.Scanner;

public class MagneticCave {
	private static int boardSize = 8; // define the board size to 8 which will be 8*8
	private static char blank = '_'; // board blanks, the initial state of each position
	private static char player1 = 'a'; // this brick defines the first player (if the character is invalid replace with any character you want)
	private static char player2 = 'b'; // this brick defines the second player  (if the character is invalid replace with any character you want)
	private static char[][] board; // 2-D array to store the game board

	public static void main(String[] args) { // the main function
		MagneticCave game = new MagneticCave(); // create an instance of the MagneticCave class
		Scanner scanner = new Scanner(System.in);
		int playMode; // define an integer to store the play mode on it

		System.out.println("Magnetic Cave Game");
		do {
			System.out.println("...................\nSelect play mode:");
			System.out.println("1. Manual vs Manual");
			System.out.println("2. Manual vs Automatic");
			System.out.println("3. Automatic vs Manual");

			playMode = scanner.nextInt(); // read the play mode choice from the user
			switch (playMode) {
			case 1:
				game.playManualManual(); // call the playManualManual function to start the game in manual vs manual
											// mode
				break;
			case 2:
				game.playManualAutomatic(); // call the playManualAutomatic method to start the game in manual vs
											// automatic mode
				break;
			case 3:
				game.playAutomaticManual(); // call the playAutomaticManual method to start the game in automatic vs
											// manual mode
				break;
			default:
				System.out.println("Invalid play mode! Enter a correct choice:");
			}
		} while (playMode != 1 && playMode != 2 && playMode != 3); // do while loop until a correct input entered

		scanner.close();
	}

	public MagneticCave() { // initialize the board
		board = new char[boardSize][boardSize]; // define the board to be 8*8 blanks
		// initialize every cell (row,column) the blank '_'
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				board[row][col] = blank;
			}
		}
	}

	private void printBoard() { // observe the board specifying the rows and columns numbers
		System.out.println();
		System.out.println("  0 1 2 3 4 5 6 7 ");
		// print the updated board with players moves
		for (int row = 0; row < board.length; row++) {
			System.out.print(row + " ");

			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] == player1) { // if the first player is playing then print in the specified cell &player1
					System.out.print(player1+" ");
				} else if (board[row][col] == player2) { // if the first player is playing then print in the specified
															// cell &player2
					System.out.print(player2+" ");
				} else {
					System.out.print(blank + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private boolean validation(int row, int col) { // check if the move of the player is valid
		// check if the entered move is within the board size
		if (row < 0 || row >= boardSize || col < 0 || col >= boardSize) {
			return false;
		}
		// check if the move is placed on blank of the cave provided that the brick is
		// stacked directly on the left or right wall
		// or is stacked to the left or the right of another brick
		return (board[row][col] == blank
				&& (col == 0 || board[row][col - 1] != blank || col == boardSize - 1 || board[row][col + 1] != blank));
	}

	private boolean isWin(char player) { // check if the current player has won

		// check if the player has 5 consecutive bricks in the same row
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col <= boardSize - 5; col++) {
				boolean win = true; // at first initiate the win variable to true
				for (int k = 0; k < 5; k++) {
					if (board[row][col + k] != player) {
						win = false; // set win to false if there is no 5 consecutive bricks in the same row
						break;
					}
				}
				if (win) {
					return true;
				}
			}
		}
		// check if the player has 5 consecutive bricks in the same column
		for (int col = 0; col < boardSize; col++) {
			for (int row = 0; row <= boardSize - 5; row++) {
				boolean win = true; // at first initiate the win variable to true
				for (int k = 0; k < 5; k++) {
					if (board[row + k][col] != player) {
						win = false; // set win to false if there is no 5 consecutive bricks in the same column
						break;
					}
				}
				if (win) {
					return true;
				}
			}
		}
		// check if the player has 5 consecutive bricks in the same increasing diagonal
		for (int row = 0; row <= boardSize - 5; row++) {
			for (int col = 0; col <= boardSize - 5; col++) {
				boolean win = true; // at first initiate the win variable to true
				for (int k = 0; k < 5; k++) {
					if (board[row + k][col + k] != player) {
						win = false; // set win to false if there is no 5 consecutive bricks in the same diagonal
						break;
					}
				}
				if (win) {
					return true;
				}
			}
		}
		// check if the player has 5 consecutive bricks in the same decreasing diagonal
		for (int row = 0; row <= boardSize - 5; row++) {
			for (int col = 4; col < boardSize; col++) {
				boolean win = true; // at first initiate the win variable to true
				for (int k = 0; k < 5; k++) {
					if (board[row + k][col - k] != player) {
						win = false; // set win to false if there is no 5 consecutive bricks in the same diagonal
						break;
					}
				}
				if (win) {
					return true;
				}
			}
		}

		return false;
	}

	private int minimax(int depth, char player) { // the miniMax algorithm for determining the best move for the
													// automatic player in the game.

		// check if the current player (player1) has won the game.
		if (isWin(player1)) {
			return 1;
		}
		// check if the opponent (player2) has won the game.
		if (isWin(player2)) {
			return -1;
		}
		// check if the maximum depth has been reached, this limits the depth of the
		// search tree and prevents infinite recursion.
		if (depth == 0) {
			return 0;
		}
		// if it's the current player's turn (player1), maximize the score.
		if (player == player1) {
			int maxScore = Integer.MIN_VALUE;
			for (int row = 0; row < boardSize; row++) {
				for (int col = 0; col < boardSize; col++) {
					if (board[row][col] == blank && validation(row, col)) {
						board[row][col] = player;
						int score = minimax(depth - 1, player2); // recursively call miniMax with the opponent's turn
																	// (player2) and decrease the depth.
						board[row][col] = blank;
						maxScore = Math.max(maxScore, score); // update the maximum score.
						if (maxScore == 1) { // if a winning move is found, no need to search further.
							return maxScore;
						}
					}
				}
			}
			return maxScore;
		}
		// if it's the opponent's turn (player2), minimize the score.
		else {
			int minScore = Integer.MAX_VALUE;
			for (int row = 0; row < boardSize; row++) {
				for (int col = 0; col < boardSize; col++) {
					if (board[row][col] == blank && validation(row, col)) {
						board[row][col] = player;
						int score = minimax(depth - 1, player1); //// recursively call miniMax with the current player's
																	//// turn (player1) and decrease the depth.
						board[row][col] = blank;
						minScore = Math.min(minScore, score); // update the minimum score.
						if (minScore == -1) { // if a losing move for the opponent is found, no need to search further.
							return minScore;
						}
					}
				}
			}
			return minScore;
		}
	}

	private void makeMove(char player) { // this function is for the automatic player moves

		// initialize variables to store the best row, column, and score
		int bestRow = -1;
		int bestCol = -1;
		int bestScore = Integer.MIN_VALUE;

		// go across all the cells in order to evaluate the score for them
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				if (board[row][col] == blank && validation(row, col)) {
					board[row][col] = player;
					// calculate the score using the miniMax algorithm with for example depth=3
					int score = minimax(3, player);
					score += evaluateMove(row, col, player);
					board[row][col] = blank;

					// update the best score and store the corresponding row and column
					if (score > bestScore) {
						bestScore = score;
						bestRow = row;
						bestCol = col;
					}
				}
			}
		}

		board[bestRow][bestCol] = player; // place the best move on the board
	}

	private int evaluateMove(int row, int col, char player) { // this function evaluates the score for the move
		int score = 0;
		char opponent;
		// check if the move creates a winning configuration for the player
		if (isWin(player)) {
			score += 100;
		}
		// check if the move ruins the opponent's potential winning configurations
		if (player == player1) {
			opponent = player2;
		} else {
			opponent = player1;
		}
		board[row][col] = opponent;
		if (isWin(opponent)) {
			score += 50;
		}
		board[row][col] = blank;

		// check the number of adjacent opponent's bricks in the neighborhood of the
		// move
		int adjacentOpponentBricks = adjacentbBricks(row, col, opponent);
		score -= adjacentOpponentBricks;

		return score;
	}

	private int adjacentbBricks(int row, int col, char piece) {
		int count = 0;

		// count the bricks that are adjacent in a specific row
		if (col > 0 && board[row][col - 1] == piece) {
			count++;
		}
		if (col < boardSize - 1 && board[row][col + 1] == piece) {
			count++;
		}

		// count the bricks that are adjacent in a specific column
		if (row > 0 && board[row - 1][col] == piece) {
			count++;
		}
		if (row < boardSize - 1 && board[row + 1][col] == piece) {
			count++;
		}

		// count the bricks that are adjacent in a specific diagonal
		if (row > 0 && col > 0 && board[row - 1][col - 1] == piece) {
			count++;
		}
		if (row < boardSize - 1 && col < boardSize - 1 && board[row + 1][col + 1] == piece) {
			count++;
		}
		if (row > 0 && col < boardSize - 1 && board[row - 1][col + 1] == piece) {
			count++;
		}
		if (row < boardSize - 1 && col > 0 && board[row + 1][col - 1] == piece) {
			count++;
		}

		return count;
	}

	private boolean isFull() { // this function checks if the board is full, so if yes the player will not be
								// able to add a move
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				if (board[row][col] == blank && validation(row, col)) { // here if a specific cell is empty and is valid
																		// then return false so the player can add his
																		// move
					return false;
				}
			}
		}
		return true;
	}

	public void playManualManual() { // Manual Manual Mode function
		Scanner scanner = new Scanner(System.in);
		int turn = 0;

		while (!isWin(player1) && !isWin(player2) && !isFull()) { // loop until one of the players wins or the board is
																	// full
			printBoard();
			if (turn % 2 == 0) { // specify which player is his turn
				System.out.println("Player 1 "+player1);
				System.out.print("enter row number: "); // let the first player enter his row #
				int row = scanner.nextInt(); // read the row number and store it in row
				System.out.print("enter column number: "); // let the first player enter his column #
				int col = scanner.nextInt(); // read the column number and store it in col

				if (validation(row, col)) { // check if the row and column that the player input are valid or not
					board[row][col] = player1;
					turn++;
				} else {
					System.out.println("Invalid move. Try again.");
				}
			} else {
				System.out.println("Player 2 "+player2);
				System.out.print("enter row number: "); // let the second player enter his row #
				int row = scanner.nextInt(); // read the row number and store it in row
				System.out.print("enter column number: "); // let the first player enter his column #
				int col = scanner.nextInt(); // read the column number and store it in col

				if (validation(row, col)) { // check if the row and column that the player input are valid or not
					board[row][col] = player2;
					turn++;
				} else {
					System.out.println("Invalid move. Try again.");
				}
			}
		}
		// determine the results, one of the players is win or the board is full?
		if (isWin(player1)) {
			printBoard();
			System.out.println("Player 1 "+player1+" wins!");
		} else if (isWin(player2)) {
			printBoard();
			System.out.println("Player 2 "+player2+ " wins!");
		} else {
			printBoard();
			System.out.println("It's a tie!");
		}

		scanner.close();
	}

	public void playManualAutomatic() { // Manual Automatic Mode function
		Scanner scanner = new Scanner(System.in);
		int turn = 0;

		while (!isWin(player1) && !isWin(player2) && !isFull()) { // loop until one of the players wins or the board is
																	// full
			printBoard();
			if (turn % 2 == 0) { // specify which player is his turn
				System.out.println("Player 1 "+player1);
				System.out.print("enter row number: "); // let the first player enter his row #
				int row = scanner.nextInt(); // read the row number and store it in row
				System.out.print("enter column number: "); // let the first player enter his column #
				int col = scanner.nextInt(); // read the column number and store it in col

				if (validation(row, col)) { // check if the row and column that the user inputs are valid or not
					board[row][col] = player1;
					turn++;
				} else {
					System.out.println("Invalid move. Try again.");
				}
				// if the turn is odd then the player will be the second one and in this mode it
				// is an automatic player
			} else {
				makeMove(player2); // call the make move function that will chose the automatic player moves based
									// on the miniMax function
				// and a specified evaluation function
				turn++;
				System.out.println("Player 2 "+player2+" made a move.");
			}
		}
		// determine the results, one of the players is win or the board is full?
		if (isWin(player1)) {
			printBoard();
			System.out.println("Player 1 "+player1+" wins!");
		} else if (isWin(player2)) {
			printBoard();
			System.out.println("Player 2 "+player2+" wins!");
		} else {
			printBoard();
			System.out.println("It's a tie!");
		}

		scanner.close();
	}

	public void playAutomaticManual() { // Automatic Manual Mode function
		Scanner scanner = new Scanner(System.in);
		int turn = 0;

		while (!isWin(player1) && !isWin(player2) && !isFull()) { // loop until one of the players wins or the board is
																	// full
			printBoard();
			if (turn % 2 == 0) { /// if the turn is even then the player will be the first one and in this mode
									/// it is an automatic player
				makeMove(player1); // call the make move function that will chose the automatic player moves based
									// on the miniMax function
				// and a specified evaluation function
				turn++;
				System.out.println("Player 1 "+player1+" made a move.");
			}
			// is the turn is odd then the player will be the second one and it is the
			// manual player
			else {
				System.out.println("Player 2 "+player2);
				System.out.print("enter row number: "); // let the second player enter his row #
				int row = scanner.nextInt(); // read the row number and store it in row
				System.out.print("enter column number: "); // let the second player enter his column #
				int col = scanner.nextInt(); // let the second player enter his column #

				if (validation(row, col)) { // check if the row and column that the user inputs are valid or not
					board[row][col] = player2;
					turn++;
				} else {
					System.out.println("Invalid move. Try again.");
				}
			}
		}
		// determine the results, one of the players is win or the board is full?
		if (isWin(player1)) {
			printBoard();
			System.out.println("Player 1 "+player1+" wins!");
		} else if (isWin(player2)) {
			printBoard();
			System.out.println("Player 2 "+player2+" wins!");
		} else {
			printBoard();
			System.out.println("It's a tie!");
		}

		scanner.close();
	}
}