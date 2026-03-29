import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {
   
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

		image = new BufferedImage (608, 608, BufferedImage.TYPE_INT_RGB);
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

		gameWindow.setFPS("" + fps);
		gameWindow.setCoords("X: " + ship.getWorldX() + ", Y: " + ship.getWorldY());
		gameWindow.setEffects(getEffectsString());
		gameWindow.setAliens(collected + "/" + 5 + " Aliens");
	}

	public void updateShip (int direction) {
		if (backgroundImage != null && !isPaused) {
			ship.move(direction);
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

		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, 608, 608, null);

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

		if (isRunning)
			return;

		isPaused = gameOver = false;

		soundManager.playClip("bgm", true);
		createGameEntities();
		gameThread = new Thread (this);			
		gameThread.start();
	}

	public void pauseGame() {
		if (isRunning) {
			if (isPaused)
				isPaused = false;
			else
				isPaused = true;
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
		g2.drawImage(image, 0, 0, 608, 608, null);
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
}