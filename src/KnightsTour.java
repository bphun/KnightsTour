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

	private int[][] grid;

	public static void main(String[] args) {
		new KnightsTour().start();
	}

	public void start() {
		grid = new int[ROWS][COLS];
		frame = new JFrame("Knights Tour");
		panel = new KnightsTourPanel(this, grid);
		frame.add(panel);	
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		startTimer();
	}

	private void startTimer() {
		t = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		t.start();	
	}

}
