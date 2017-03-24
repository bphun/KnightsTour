import java.awt.*;
import java.util.Hashtable;
import javax.swing.*;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
/* One of the tricky things you have to figure out is how to have
 * the controls in the control panel talk to the KnightsTourPanel.
 * I know you'll figure out a way.  DON'T USE STATIC METHODS!!!!!
 */

public class KnightsTourControlPanel extends JPanel {
	
	private JButton startButton;
	private JButton nextButton;
	private JButton clearButton;
	private JSlider speedSlider;
	private JRadioButton randomModeRadioButton;
	private JRadioButton algorithmModeRadioButton;

	private KnightsTour knightsTour;

	// Minimum speed at which the simulation will play
	final static int MIN_SPEED = 1200;

	//	Maximum speed at which te simulation plays
	final static int MAX_SPEED = 300;

	public KnightsTourControlPanel(int w, int h, KnightsTour knightsTour) {		
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.orange);
		this.knightsTour = knightsTour;
		this.setLayout(null);
		setUpButtonsAndSliders();
	}

	/* Add all the buttons and sliders used to control this Knight's tour.
	 * It is best if you allow the placement of the components to be handled
	 * by a layout manager.  You can find out lots about layouts if you google
	 * it!  You can also bind key events to the buttons and sliders, as well
	 */
	private void setUpButtonsAndSliders() {
		startButton = new JButton("Start");
		nextButton = new JButton("Next");
		clearButton = new JButton("Clear");
		speedSlider = new JSlider(JSlider.HORIZONTAL, MIN_SPEED, MAX_SPEED);
		randomModeRadioButton = new JRadioButton("Random Mode");
		algorithmModeRadioButton = new JRadioButton("Algorithm Mode");

		speedSlider.setInverted(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(MAX_SPEED), new JLabel("Fast"));
		labelTable.put(new Integer(MIN_SPEED), new JLabel("Slow"));
		speedSlider.setLabelTable(labelTable);
		speedSlider.setPaintLabels(true);

		// algorithmModeRadioButton.setBounds(500,30,100,100);
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startButtonAction();
			}	
		});

		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextButtonAction();
			}	
		});

		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearButtonAction();
			}	
		});

		speedSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				speedSliderAction();
			}
		});

		randomModeRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				randomModeRadioButtonAction();
			}	
		});

		algorithmModeRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algorithmModeRadioButtonAction();
			}	
		});

		this.add(startButton);
		this.add(nextButton);
		this.add(clearButton);
		this.add(speedSlider);
		this.add(randomModeRadioButton);
		this.add(algorithmModeRadioButton);
	}

	boolean currentlyPlaying = false;
	private void startButtonAction() {
		if (currentlyPlaying) {
			startButton.setText("Start");
			currentlyPlaying = false;
		} else {
			startButton.setText("Stop");
			currentlyPlaying = true;
		}
		knightsTour.shouldPlay();
		knightsTour.play();
	}

	private void nextButtonAction() {
		knightsTour.step();
	}

	private void clearButtonAction() {
		knightsTour.clear();
	}

	private void speedSliderAction() {
		knightsTour.updateSpeed(speedSlider.getValue());
	}

	private void randomModeRadioButtonAction() {
		knightsTour.updateMode();
	}

	private void algorithmModeRadioButtonAction() {
		knightsTour.updateMode();
	}
}
