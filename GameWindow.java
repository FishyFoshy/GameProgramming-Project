import java.awt.*;			// need this for GUI objects
import java.awt.event.*;			// need this for Layout Managers
import javax.swing.*;		// need this to respond to GUI events
	
public class GameWindow extends JFrame implements ActionListener, KeyListener, MouseListener
{
	private final Container c;
	private final GamePanel gamePanel;

	@SuppressWarnings({})
	public GameWindow() {
 
		setTitle ("Alien Collector");
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

	public void actionPerformed(ActionEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
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
}