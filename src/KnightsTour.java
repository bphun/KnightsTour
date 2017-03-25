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

public class KnightsTour {

	private static final int ROWS =  8;
	private static final int COLS = 8;
	private static final int SQUARE_SIZE = 80;
	private static final Dimension PANEL_DIMENSIONS = new Dimension(ROWS * SQUARE_SIZE, COLS * SQUARE_SIZE + 80);

	private JFrame frame;
	private KnightsTourPanel panel;

	private Timer t;
	private Timer playTimer;

	private int[][] grid;
	private int[][] iterations;

	private boolean randomMode;
	private boolean shouldPlay;

	private int currentRow;
	private int currentCol;
	private int currIteration;

	private HashMap<Integer, Location> moves = new HashMap<>();

	public static void main(String[] args) {
		new KnightsTour().start();
	}

	public void start() {
		grid = new int[ROWS][COLS];
		initializeIterationGrid();
		randomMode = true;
		moves = new HashMap<>();
		frame = new JFrame("Knights Tour");
		panel = new KnightsTourPanel(this, grid, iterations);
		frame.add(panel);	
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		startTimer();
		startPlayTimer();
	}

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

	public void setStartPosition(int row, int col) {
		currentRow = row;
		currentCol = col;
	}

	public void shouldPlay() {
		shouldPlay = !shouldPlay;
	}

	public boolean step() {
		printGrid();
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

	public void clear() {
		this.grid = new int[ROWS][COLS];
	}

	public void updateSpeed(int newSpeed) {
		playTimer.stop();
		playTimer.setDelay(newSpeed);
		playTimer.start();
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

	private boolean randomMove() {
		int newRow;
		int newCol;

		while (true) {
			if (numMovesAvailable() <= 0) { 
				JOptionPane.showMessageDialog(null, "There are no more available moves");
				return false;
			}
			newRow = (int)(Math.random() * ROWS);
			newCol = (int)(Math.random() * COLS);
			if (canMakeMove(newRow, newCol)) {
				updateLocation(newRow, newCol);

				panel.setGrid(grid);

				updateIterations(newRow, newCol);
				
				break;
			}
		}
		return true;
	}

	private boolean algorithmMove() {
		int numMovesAvailable = numMovesAvailable();

		while (true) {
			if (numMovesAvailable() <= 0) { 
				JOptionPane.showMessageDialog(null, "There are no more available moves");
				return false; 
			}

			for (int r = 0; r < ROWS; r++) {
				for (int c = 0; c < COLS; c++) {
					if (((r != currentRow) && (c != currentCol)) && canMakeMove(r, c)) {
						moves.put(new Integer(moves.size()), new Location(r, c));
					}
				}
			}

			if (moves.size() == numMovesAvailable) {
				// Location optimalMove = moves.get(new Integer(0));
				// double distance = optimalMove.distanceTo(currentCol, currentRow);
				// for (Location location : moves.values()) {
				// 	double d = location.distanceTo(currentCol, currentRow);
				// 	if ((location.distanceTo(currentCol, currentRow) < distance)) {
				// 		optimalMove = location;
				// 		distance = d;
				// 	}
				// }
				Location optimalMove = Collections.min(moves.values());

				updateLocation(optimalMove.y(), optimalMove.x());

				break;
			}
		}
		return true;
	}

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

	private void pause() {
		shouldPlay = false;
		panel.pause();
	}

	private void printGrid() {
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

}
