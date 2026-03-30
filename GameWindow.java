import javax.swing.*;			// need this for GUI objects
import java.awt.*;			// need this for Layout Managers
import java.awt.event.*;		// need this to respond to GUI events
	
public class GameWindow extends JFrame implements ActionListener, KeyListener, MouseListener
{
	// declare instance variables for user interface objects

	// declare labels 

	private JLabel fpsL;
	private JLabel coordsL;
	private JLabel effectsL;
	private JLabel aliensCollectedL;

	// declare text fields

	private JTextField fpsTF;
	private JTextField coordsTF;
	private JTextField effectsTF;
	private JTextField aliensCollectedTF;

	// declare buttons

	private JButton startB;
	private JButton pauseB;
	private JButton endB;
	private JButton exitB;

	private Container c;

	private JPanel mainPanel;
	private GamePanel gamePanel;

	@SuppressWarnings({"unchecked"})
	public GameWindow() {
 
		setTitle ("Alien Collector");
		setSize (600, 800);
		setLocationRelativeTo(null);

		// create user interface objects

		// create labels

		fpsL = new JLabel ("FPS: ");
		coordsL = new JLabel("Coordinates: ");
		effectsL = new JLabel("Applied Effects: ");
		aliensCollectedL = new JLabel("Aliens Collected: ");

		// create text fields and set their colour, etc.

		fpsTF = new JTextField (25);
		coordsTF = new JTextField (25);
		effectsTF = new JTextField (25);
		aliensCollectedTF = new JTextField (25);

		fpsTF.setEditable(false);
		coordsTF.setEditable(false);
		effectsTF.setEditable(false);
		aliensCollectedTF.setEditable(false);

		fpsTF.setBackground(Color.PINK);
		coordsTF.setBackground(Color.CYAN);
		effectsTF.setBackground(Color.YELLOW);
		aliensCollectedTF.setBackground(Color.GREEN);

		// create buttons

		startB = new JButton ("Start Game");
		pauseB = new JButton ("Pause Game");
		endB = new JButton ("End Game");
		exitB = new JButton ("Exit");

		// add listener to each button (same as the current object)

		startB.addActionListener(this);
		pauseB.addActionListener(this);
		endB.addActionListener(this);
		exitB.addActionListener(this);
		
		// create mainPanel

		mainPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		mainPanel.setLayout(flowLayout);

		GridLayout gridLayout;

		// create the gamePanel for game entities

		gamePanel = new GamePanel(this);
		gamePanel.setPreferredSize(new Dimension(600, 800));

		// create infoPanel

		JPanel infoPanel = new JPanel();
		gridLayout = new GridLayout(4, 2);
		infoPanel.setLayout(gridLayout);
		infoPanel.setBackground(Color.ORANGE);

		// add user interface objects to infoPanel
	
		infoPanel.add (fpsL);
		infoPanel.add (fpsTF);

		infoPanel.add (coordsL);
		infoPanel.add (coordsTF);		

		infoPanel.add (effectsL);
		infoPanel.add (effectsTF);
		
		infoPanel.add (aliensCollectedL);
		infoPanel.add (aliensCollectedTF);
		
		// create buttonPanel

		JPanel buttonPanel = new JPanel();
		gridLayout = new GridLayout(1, 4);
		buttonPanel.setLayout(gridLayout);

		// add buttons to buttonPanel

		buttonPanel.add (startB);
		buttonPanel.add (pauseB);
		buttonPanel.add (endB);
		buttonPanel.add (exitB);

		// add sub-panels with GUI objects to mainPanel and set its colour

		//mainPanel.add(infoPanel);
		mainPanel.add(gamePanel);
		//mainPanel.add(buttonPanel);
		mainPanel.setBackground(Color.BLACK);

		// set up mainPanel to respond to keyboard and mouse

		gamePanel.addMouseListener(this);
		mainPanel.addKeyListener(this);

		// add mainPanel to window surface

		c = getContentPane();
		c.add(mainPanel);

		// set properties of window

		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		if (command.equals(startB.getText())) {
			gamePanel.startGame();
		}

		if (command.equals(pauseB.getText())) {
			gamePanel.pauseGame();
			if (command.equals("Pause Game"))
				pauseB.setText ("Resume");
			else
				pauseB.setText ("Pause Game");

		}
		
		if (command.equals(endB.getText())) {
			gamePanel.endGame();
		}

		if (command.equals(exitB.getText()))
			System.exit(0);

		mainPanel.requestFocus();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			gamePanel.updateShip(1);
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			gamePanel.updateShip(2);
		}

		if (keyCode == KeyEvent.VK_UP) {
			gamePanel.updateShip(3);
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			gamePanel.updateShip(4);
		}
	}

	public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_LEFT){
            gamePanel.updateShip(-1);
        }

        if (keyCode == KeyEvent.VK_RIGHT) {
			gamePanel.updateShip(-2);
		}

        if (keyCode == KeyEvent.VK_UP) {
			gamePanel.updateShip(-3);
		}

        if (keyCode == KeyEvent.VK_DOWN) {
			gamePanel.updateShip(-4);
		}
    }

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void setFPS(String s){fpsTF.setText(s);}
	public void setCoords(String s){coordsTF.setText(s);}
	public void setEffects(String s){effectsTF.setText(s);}
	public void setAliens(String s){aliensCollectedTF.setText(s);}
}