import java.awt.*;			// need this for GUI objects
import java.awt.event.*;			// need this for Layout Managers
import javax.swing.*;		// need this to respond to GUI events
	
public class GameWindow extends JFrame implements ActionListener, KeyListener, MouseListener
{
	private final Container c;
	private final GamePanel gamePanel;

	@SuppressWarnings({})
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

    @Override
	public void actionPerformed(ActionEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}