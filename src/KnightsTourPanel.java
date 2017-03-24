import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

public class KnightsTourPanel extends JPanel {

	private static final int SQUARE_SIZE = 80;
	private static final int ROWS = 8;
	private static final int COLS = 8;
	private static final Dimension PANEL_DIMENSIONS = new Dimension(ROWS * SQUARE_SIZE, COLS * SQUARE_SIZE + 80);
	private final static int LINE_THICKNESS = 1;

	private int currX;
	private int currY;

	private	KnightsTour knightsTour;
	private KnightsTourControlPanel controlPanel;

	private int[][] grid;

	private boolean canSelectSquare;

	public KnightsTourPanel(KnightsTour knightsTour, int[][] grid) {
		this.setLayout(new BorderLayout());
		controlPanel = new KnightsTourControlPanel(PANEL_DIMENSIONS.width, 80, knightsTour);
		controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(controlPanel, BorderLayout.SOUTH);
		this.knightsTour = knightsTour;
		this.grid = grid;
		canSelectSquare = true;
		setPreferredSize(PANEL_DIMENSIONS);
		setUpClickListener();
	}

		private void setUpClickListener() {
		this.requestFocusInWindow();
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}
			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent click) {

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				clicked(arg0);
			}

		});
	}

	private void clicked(MouseEvent e) {
		int row = e.getY() / SQUARE_SIZE;
		int col = e.getX() / SQUARE_SIZE;
		if (grid == null) { return; }

		if (canSelectSquare) {
			grid[row][col] = 1;
			canSelectSquare = false;
			knightsTour.setStartPosition(row, col);
		}

		repaint();
	}

	/* add the mouse listener.  This will only work for the 
	 * first click, and then after the first click, there should
	 * be no more mouse listening!
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		fillSquares(g2);
		drawGrid(g2);
	}

	private void fillSquares(Graphics2D g2) {
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				if (grid[r][c] == 1) {
					g2.setColor(new Color(0,0,0));
				} else {
					if ((c % 2) == (r % 2)) {
						g2.setColor(new Color(117, 117, 117));
					} else {
						g2.setColor(new Color(189, 189, 189));
					}
				}
				g2.fillRect(c * SQUARE_SIZE + LINE_THICKNESS, r * SQUARE_SIZE + LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS, SQUARE_SIZE - LINE_THICKNESS);		
			}
		}
		g2.setColor(Color.BLACK);
	}
	private void drawGrid(Graphics2D g2) {
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				//	Draws the vertical line
				g2.drawLine(SQUARE_SIZE * c, 0, SQUARE_SIZE * c, (PANEL_DIMENSIONS.height) - 80);
			}
			//	Draws the horizontal line
			g2.drawLine(0, (SQUARE_SIZE * r), PANEL_DIMENSIONS.width, (SQUARE_SIZE * r)); 
		}

	}

}
