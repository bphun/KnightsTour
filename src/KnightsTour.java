import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

public class KnightsTour {

	//	Number of rows in the game's grid, and number of rows drawn on the panel
	private static final int ROWS =  8;

	//	Number of columns in the game's grid, and number of columns drawn on the panel
	private static final int COLS = 8;

	//	The width/height of each square drawn on the panel
	private static final int SQUARE_SIZE = 80;

	private static final Dimension PANEL_DIMENSIONS = new Dimension(ROWS * SQUARE_SIZE, COLS * SQUARE_SIZE + 80);

	private JFrame frame;

	//	The panel containing the grid that the knight traverses
	private KnightsTourPanel panel;

	//	Timer that is used to refresh the panel every 500 ms
	private Timer t;

	//	Timer used to step the game at the interval the user sets using the slider in the control panel
	private Timer playTimer;

	//	2D array with the status of every square in the grid, 1 if selected, 0 otherwise
	private int[][] grid;

	//	Intialized with every element starting at -1, a element be set to the current iteration when it is moved on to by the knight
	private int[][] iterations;

	private boolean randomMode;
	private boolean shouldPlay;

	// The current row/column of the knight
	private int currentRow;
	private int currentCol;

	//	The number of steps that have been made
	private int currIteration;

	public static void main(String[] args) {
		new KnightsTour().start();
	}

	public void start() {
		grid = new int[ROWS][COLS];
		initializeIterationGrid();
		randomMode = true;
		frame = new JFrame("Knights Tour");
		panel = new KnightsTourPanel(this, grid, iterations);
		frame.add(panel);	
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		startTimer();
		startPlayTimer();
	}

	/**
	 * @param row that the knight will start at
	 * @param col that the knight will start at
	 * Sets the knights row and column to the inputed
	 * row and column
	 */
	public void setStartPosition(int row, int col) {
		this.currentRow = row;
		this.currentCol = col;
	}

	/**
	 * Tells the panel if it should begin playing
	 * the game by stepping at the interval that the user
	 * has set using the slider
	 */
	public void shouldPlay() {
		shouldPlay = !shouldPlay;
	}

	/**
	 * Moves the knight to next location. The move type
	 * depends on wether or not the user has decided to use
	 * a random move or an algorithm based move
	 * @return a boolean, true if the knight moved with no errors, 
	 * false if an error occured
	 */
	public boolean step() {
		printGrid(this.grid);
		if (!containsSelectedSquare()) {
			JOptionPane.showMessageDialog(null, "You must select a square on the grid before continuing.");
			return false;
		}
		if (randomMode) {
			return randomMove();
		} else {
			return algorithmMove();
		} 
	}

	/**
	 * Called when the user clicks the clear button on the control panel
	 * Clears the grid, reintializes the iteration grid
	 * and updates the panels version of grid and iteration grid
	 */
	public void clear() {
		this.grid = new int[ROWS][COLS];
		initializeIterationGrid();
		panel.setIterations(this.iterations);
		panel.canSelectSquare();
	}

	/**
	 * updates the interval at which the play timer
	 * updates
	 */
	public void updateSpeed(int newSpeed) {
		playTimer.setDelay(newSpeed);
	}

	public void updateMode(boolean randomMode) {
		this.randomMode = randomMode;
	}

	public void play() {
		if (shouldPlay) {
			if (!step()) {
				pause();
			}	
		}
	}

	/**
	 * Intializes the iteration grid with all values 
	 * at -1
	 */
	private void initializeIterationGrid() {
		iterations = new int[ROWS][COLS];
		for (int r = 0; r < iterations.length; r++) {
			for (int c = 0; c < iterations[0].length; c++) {
				iterations[r][c] = -1;
			}
		}
	}

	private void startTimer() {
		t = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.refresh();
			}
		});
		t.start();	
	}

	private void startPlayTimer() {
		playTimer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play();
			}
		});
		playTimer.start();
	}

	/*
	 * Moves the knight to any of the possible new locations that it is 
	 * permitted to move to according to the rules of a Knight chess piece 
	 */
	private boolean randomMove() {
		int newRow;
		int newCol;

		while (true) {
			if (numMovesAvailable(currentRow, currentCol) <= 0) { 
				JOptionPane.showMessageDialog(null, "There are no more available moves");
				return false;
			}

			newRow = (int)(Math.random() * ROWS);
			newCol = (int)(Math.random() * COLS);

			if (canMakeMove(newRow, newCol)) {
				updateLocation(newRow, newCol);
				panel.setGrid(grid);
				break;
			}
		}
		return true;
	}

	/**
	 * Moves the knights to the best possible location on the grid
	 * so that the knight will always visit every spot on the grid.
	 */
	private boolean algorithmMove() {
		// Map containing possible moves that the knight can make, value = the number of moves that the knight can make from the location stored in the key
		HashMap<Location, Integer> moves = new HashMap<>();

		//	The maximum number of moves the knight can make from its current location
		int numMovesAvailable = numMovesAvailable(currentRow, currentCol);

		while (true) {
			//	No point in doing all the stuff below if there are no moves remaining
			if (numMovesAvailable(currentRow, currentCol) == 0) { 
				JOptionPane.showMessageDialog(null, "There are no more available moves");
				return false; 
			}

			// Populate the 'moves' map with possible locations that the knight can move to  
			for (int r = 0; r < ROWS; r++) {
				for (int c = 0; c < COLS; c++) {
					Location l = new Location(c, r);
					if ((r != currentRow && c != currentCol) && canMakeMove(currentRow, r, currentCol, c) && (!moves.containsKey(l))) {
						/* Have to put the numMovesAvailable as the value because there is a possibility that that 
						 * value will be repeated, and in a map a key will never be repeated
						 */
						moves.put(l, numMovesAvailable(r, c));
					}
				}
			}

			//	Determine the best move for the knight to make
			if (moves.size() == numMovesAvailable) {
				Integer value = Collections.min(moves.values());
				Location bestLocation = null;
				for (Map.Entry<Location, Integer> map : moves.entrySet()) {
					if (map.getValue().equals(value)) {
						bestLocation = map.getKey();
						break; // Need this or else the algorithm won't work
					}
				}
				updateLocation(bestLocation.y(), bestLocation.x());
				break;
			}
		}
		return true;
	}

	/**
	* @param newRow is the new row of the knight on the grid
	* @param newCol is the new column of the knight on the grid
	* Moves the knight the the desired row/col and updates the iteration
	* grid
	*/
	private void updateLocation(int newRow, int newCol) {
		grid[currentRow][currentCol] = 0;
		grid[newRow][newCol] = 1;
		currentRow = newRow;
		currentCol = newCol;
		updateIterations(newRow, newCol);
	}

	private void updateIterations(int row, int col) {
		iterations[row][col] = ++currIteration;
		panel.setIteration(row, col, currIteration);
	}

	// Pauses the game if it is currently playing, called when there has been an error in executing a step
	private void pause() {
		shouldPlay = false;
		panel.pause();
	}

	private void printGrid(int[][] grid) {
		for (int[] r : grid) {
			for (int c : r) {
				switch (c) {
					case 0:
						System.out.print(".");
						break;
					case 1:
						System.out.print("*");
						break;
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	private boolean containsSelectedSquare() {
		for (int[] r : grid) {
			for (int c : r) {
				if (c == 1) {
					return true;
				}
			}
		}
		return false;
	}

	private int numMovesAvailable() {
		int numMoves = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				if (canMakeMove(r, c)) {
					numMoves++;
				}
			}
		}
		return numMoves;
	}

	private boolean canMakeMove(int newRow, int newCol) {
		if (iterations[newRow][newCol] > -1) { return false; }
		int row = Math.abs(newRow - currentRow);
		int col = Math.abs(newCol - currentCol);
		return ((row == 2 && col == 1) || (row == 1 && col == 2));
	}

	private int numMovesAvailable(int row, int col) {
		int numMoves = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				if (canMakeMove(row, r, col, c)) {
					numMoves++;
				}
			}
		}
		return numMoves;
	}

	/**
	 * Checks if the knight can move the knight can move 2 rows up or 
	 * down and 1 column left or right or 1 row up or down and 2 columns up or down
	 */
	private boolean canMakeMove(int startRow, int newRow, int startCol, int newCol) {
		if (iterations[newRow][newCol] > -1) { return false; }
		int row = Math.abs(newRow - startRow);
		int col = Math.abs(newCol - startCol);
		return ((row == 2 && col == 1) || (row == 1 && col == 2));
	}

}
