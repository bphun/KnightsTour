import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

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

	private boolean randomMode;
	private boolean shouldPlay;

	private int currentRow;
	private int currentCol;

	public static void main(String[] args) {
		new KnightsTour().start();
	}

	public void start() {
		grid = new int[ROWS][COLS];
		randomMode = false;
		frame = new JFrame("Knights Tour");
		panel = new KnightsTourPanel(this, grid);
		frame.add(panel);	
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		startTimer();
		startPlayTimer();
	}

	private void startTimer() {
		t = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
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

	public void step() {
		if (randomMode) {
			randomMove();
		} else {
			algorithmMove();
		}
	}

	public void clear() {
		this.grid = new int[ROWS][COLS];
	}

	public void updateSpeed(int newSpeed) {
		playTimer.setDelay(newSpeed);
	}

	public void updateMode() {
		randomMode = !randomMode;
	}

	public void play() {
		if (shouldPlay) {
			step();	
		}
	}

	private void randomMove() {
		
	}

	private void algorithmMove() {

	}

}
