import java.awt.*;			// need this for GUI objects
import javax.swing.*;		// need this to respond to GUI events
	
public class GameWindow extends JFrame
{
	private final Container c;
	private final GamePanel gamePanel;

	public GameWindow() {
 
		setTitle ("Spacewar");
		setSize (600, 800);
		setLocationRelativeTo(null);

		// create the gamePanel for game entities
		gamePanel = new GamePanel(this);
		gamePanel.setPreferredSize(new Dimension(600, 800));

		c = getContentPane();
		c.add(gamePanel);

		// set properties of window
		pack();
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);

		gamePanel.requestFocus();
		gamePanel.startThread();
	}
}