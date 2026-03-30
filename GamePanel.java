import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {
   
	private SoundManager soundManager;

	private ArrayList<SolidObject> solids;

	private ArrayList<Alien> aliens;
	private boolean isRunning;
	private boolean isPaused;

	private Thread gameThread;

	private BufferedImage image;
 	private Background backgroundImage;
	private Ship ship;

	private ImageFX fadeFx;
	private ImageFX blueTintFx;
	private ImageFX redTintFx;
	private ImageFX greenTintFx;
	private ImageFX grayFx;
	private boolean gameOver;
	private int collected, fps, frames;
	private long lastFrameTime;
	private GameWindow gameWindow;

	private StartScreen startScreen;
	private boolean showStartScreen;

	private PauseScreen pauseScreen;
	public static int score = 0;


	public GamePanel (GameWindow gW) {
		gameWindow = gW;
		collected = 0;
		fadeFx = new FadedFX();
		grayFx = new GrayScaleFX();
		blueTintFx = new TintFX("blue");
		redTintFx = new TintFX("red");
		greenTintFx = new TintFX("green");
		solids = null;
		isRunning = false;
		isPaused = gameOver = false;
		soundManager = SoundManager.getInstance();
		backgroundImage = null;
		ship = null;

		image = new BufferedImage (600, 800, BufferedImage.TYPE_INT_RGB);

		startScreen = new StartScreen(600, 800);
		showStartScreen = true;

		pauseScreen = new PauseScreen(600, 800);

		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);

	}

	public void startThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void createGameEntities() {
		aliens = new ArrayList<>();
		solids = new ArrayList<>();
		backgroundImage = new Background(); 
		aliens.add(new Alien(this, 16*76, 16*36, backgroundImage, fadeFx));
		aliens.add(new Alien(this, 16*147, 16*83, backgroundImage, greenTintFx));
		aliens.add(new Alien(this, 16*6, 16*76, backgroundImage, blueTintFx));
		aliens.add(new Alien(this, 16*108, 16*46, backgroundImage, null));
		aliens.add(new Alien(this, 16*118, 16*8, backgroundImage, redTintFx));
		solids.add(new SolidObject(this, 240, 80, 16*3, 16*3, backgroundImage));
		solids.add(new SolidObject(this, 576, 480, 16*9, 16*3, backgroundImage));
		solids.add(new SolidObject(this, 400, 816, 16*1, 16*17, backgroundImage));
		solids.add(new SolidObject(this, 1168, 368, 16*4, 16*7, backgroundImage));
		solids.add(new SolidObject(this, 1552, 688, 16*2, 16*13, backgroundImage));
		solids.add(new SolidObject(this, 1984, 112, 16*23, 16*6, backgroundImage));
		solids.add(new SolidObject(this, 1904, 864, 16*9, 16*9, backgroundImage));
		solids.add(new SolidObject(this, 2352, 1168, 16*18, 16*9, backgroundImage));
		solids.add(new SolidObject(this, 736, 736, 16*37, 16*3, backgroundImage));
		ship = new Ship(this, backgroundImage, solids, aliens);
	}

	public void run () {
		try {
			isRunning = true;

			// start screen loop
			while (isRunning && showStartScreen) {
				Graphics2D imageContext = (Graphics2D) image.getGraphics();
				startScreen.draw(imageContext);
				imageContext.dispose();

				Graphics2D g2 = (Graphics2D) getGraphics();
				if (g2 != null) {
					g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
					g2.dispose();
				}
				Thread.sleep(50);
			}

			// initialize game after start screen
			isPaused = gameOver = false;
			soundManager.playClip("bgm", true);
			createGameEntities();

			// game loop
			while (isRunning) {
				if (!isPaused)
					gameUpdate();
				gameRender();

				Thread.sleep (25);	
			}
		}
		catch(InterruptedException e) {}
	}

	public void gameUpdate() {
		int i = 0;
		ship.update();
		ship.move();
		for(Alien alien : aliens){
			alien.update();

			if(alien.isCollected()){
				i++;
				if(i > collected)
					collected = i;
				if(i == 5)
					gameOver = true;
			}
		}
	}

	public void updateShip (int direction) {
		if (backgroundImage != null && !isPaused) {
			ship.setMoveDirection(direction);
		}
	}

	public void gameRender() {
		Graphics2D imageContext = (Graphics2D) image.getGraphics();


		if (backgroundImage != null) {
			backgroundImage.draw(imageContext);
		}

		if(aliens != null){
			for(Alien alien : aliens){
				alien.draw(imageContext);
			}
		}

		if (ship != null) {
			ship.draw(imageContext);
		}
		
		if(solids != null){
			for(SolidObject solidObject : solids){
				solidObject.draw(imageContext);
			}
		}

		// draw pause button and score on the image
		pauseScreen.drawPauseButton(imageContext);
		pauseScreen.drawScore(imageContext, score);

		// if paused, draw the dim overlay and menu
		if (isPaused) {
			pauseScreen.drawPauseMenu(imageContext);
		}

		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, 600, 800, null);

		imageContext.dispose();
		g2.dispose();

		frames++;
		long currentTime = System.currentTimeMillis();
		if(currentTime - lastFrameTime >= 1000){
			fps = frames;
			frames = 0;
			lastFrameTime = currentTime;
		}

		if(gameOver)
			endGame();
	}

	public void startGame() {

		showStartScreen = false;
		requestFocus();
	}

	public void pauseGame() {
		if (isRunning) {
			if (isPaused) {
				isPaused = false;
				soundManager.resumeClip("bgm");
			} else {
				isPaused = true;
				soundManager.pauseClip("bgm");
				// stop ship movement so it doesn't keep going after unpause
				if (ship != null) {
					ship.setMoveDirection(-1);
					ship.setMoveDirection(-2);
					ship.setMoveDirection(-3);
					ship.setMoveDirection(-4);
				}
			}
		}
	}

	public void endGame() {
		isRunning = false;
		gameOver();
		soundManager.stopClip("bgm");
		soundManager.playClip("game-over", false);
	}

	public void gameOver(){
		image = grayFx.apply(image);
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, 600, 800, null);
		g2.setColor(Color.white);
		g2.setFont(new Font("Arial", Font.BOLD, 40));
		g2.drawString("GAME OVER", 184, 304);
		g2.dispose();
	}

	private String getEffectsString(){
		ImageFX effect;
		Set<String> set = new LinkedHashSet<>();
		String s = "";

		for (Alien alien : aliens) {
			effect = alien.getEffect();
			if(effect != null)
				set.add(effect.getEffectName());
		}

		s = String.join(", ", set);

		return s;
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_ESCAPE && isRunning && !gameOver)
			pauseGame();
		if (keyCode == KeyEvent.VK_A)
			updateShip(1);
		if (keyCode == KeyEvent.VK_D)
			updateShip(2);
		if (keyCode == KeyEvent.VK_W)
			updateShip(3);
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_A)
			updateShip(-1);
		if (keyCode == KeyEvent.VK_D)
			updateShip(-2);
		if (keyCode == KeyEvent.VK_W)
			updateShip(-3);
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		if (showStartScreen) {
			int mx = e.getX() * 600 / getWidth();
			int my = e.getY() * 800 / getHeight();
			String clicked = startScreen.getButtonClicked(mx, my);
			if (clicked != null) {
				if (clicked.equals("start")) {
					startGame();
				} else if (clicked.equals("instructions")) {
					startScreen.setShowingInstructions(true);
				} else if (clicked.equals("closeInstructions")) {
					startScreen.setShowingInstructions(false);
				} else if (clicked.equals("exit")) {
					System.exit(0);
				}
			}
		} else if (isRunning && !gameOver) {
			int mx = e.getX() * 600 / getWidth();
			int my = e.getY() * 800 / getHeight();
			String clicked = pauseScreen.getButtonClicked(mx, my, isPaused);
			if (clicked != null) {
				if (clicked.equals("pause")) {
					pauseGame();
				} else if (clicked.equals("resume")) {
					pauseGame();
				} else if (clicked.equals("exit")) {
					System.exit(0);
				}
			}
		}
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
}