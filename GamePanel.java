import GameEntities.*;
import ImageManip.*;
import Misc.SoundManager;
import Screens.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener {
   
	private final SoundManager soundManager;

	private ArrayList<Alien> aliens;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Item> items;
	private ArrayList<Explosion> explosions;
	private Boss boss;
	private boolean bossSpawnedOnce;
	private int level;
	private final int asteroidChance, alienChance, straightAlien, circularAlien, sineAlien;
	private boolean isRunning;
	private boolean isPaused;

	private Thread gameThread;

	private BufferedImage image;
 	private BackgroundManager backgroundImage;
	private Ship ship;
	private Ship ship2;
	private boolean twoPlayer;

	private ImageFX blueTintFx;
	private ImageFX redTintFx;
	private boolean gameOver;
	private int collected, fps, frames;
	private long lastFrameTime;
	private long lastAsteroidSpawnTime, lastAlienSpawnTime;
	private long gameTime;
	private long lastUpdateTime;
	private long levelTimer;
	private long level2StartTime;
	private static final long LEVEL1_DURATION = 120000; // 2 minutes in ms
	private static final long CANTIME = 10000; // 10 seconds in ms
	private final Random random;
	private GameWindow gameWindow;

	private final StartScreen startScreen;
	private boolean showStartScreen;

	private final PlayerSelectScreen playerSelectScreen;
	private boolean showPlayerSelect;

	private final PauseScreen pauseScreen;
	public static int score = 0;


	public GamePanel (GameWindow gW) {
		gameWindow = gW;
		bossSpawnedOnce = false;
		collected = 0;
		blueTintFx = new TintFX("blue");
		redTintFx = new TintFX("red");
		random = new Random();
		isRunning = false;
		isPaused = gameOver = false;
		soundManager = SoundManager.getInstance();
		backgroundImage = null;
		ship = null;
		ship2 = null;
		twoPlayer = false;
		level = 1;
		asteroidChance = 100;
		alienChance = 50;
		straightAlien = 10;
		sineAlien = 5;
		circularAlien = 2;
		gameTime = 0;
		lastUpdateTime = System.currentTimeMillis();
		lastAsteroidSpawnTime = 0;
		lastAlienSpawnTime = 0;
		levelTimer = LEVEL1_DURATION;

		image = new BufferedImage (600, 800, BufferedImage.TYPE_INT_RGB);

		startScreen = new StartScreen(600, 800);
		showStartScreen = true;

		playerSelectScreen = new PlayerSelectScreen(600, 800);
		showPlayerSelect = false;

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
		backgroundImage = new BackgroundManager(); 
		asteroids = new ArrayList<>();
		items = new ArrayList<>();
		explosions = new ArrayList<>();
		// This is done so that the time actually starts when the game entities are created
		// and not while the player is still on the start screen or player select screen
		lastUpdateTime = System.currentTimeMillis();
		boss = null;
		bossSpawnedOnce = false;
		ship = new Ship(this, 210, 715, aliens, false);
		if (twoPlayer) {
			ship2 = new Ship(this, 310, 715, aliens, true);
		}
	}

	public void run () {
		long targetPeriod = 25;
		try {
			isRunning = true;
			soundManager.playClip("mainmenu", true);

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
				Thread.sleep(16);
			}

			// player select screen loop
			while (isRunning && showPlayerSelect) {
				Graphics2D imageContext = (Graphics2D) image.getGraphics();
				playerSelectScreen.draw(imageContext);
				imageContext.dispose();

				Graphics2D g2 = (Graphics2D) getGraphics();
				if (g2 != null) {
					g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
					g2.dispose();
				}
				Thread.sleep(16);
			}

			// initialize game after start screen
			isPaused = gameOver = false;
			soundManager.playClip("bgm", true);
			createGameEntities();

			// game loop
			while (isRunning) {
				long beginTime = System.currentTimeMillis();

				if (!isPaused)
					gameUpdate();
				gameRender();

				long timeTaken = System.currentTimeMillis() - beginTime;
	            long timeLeft = targetPeriod - timeTaken;

	            // 3. Sleep only for the remaining time in the 50ms window
	            if (timeLeft > 0) {
	                Thread.sleep(timeLeft);
	            }	
			}
		}
		catch(InterruptedException e) {}
	}

	public void gameUpdate() {
		for (Alien alien : aliens) {
			alien.move();
		}

		// update game clock
		long now = System.currentTimeMillis();
		long delta = now - lastUpdateTime;
		gameTime += delta;
		lastUpdateTime = now;

		// countdown timer for level 1
		if (level == 1) {
			levelTimer -= delta;
			if (levelTimer <= 0) {
				levelTimer = 0;
				level = 2;
				level2StartTime = gameTime;
				aliens.clear();
				soundManager.stopClip("bgm");
				soundManager.playAfter("incoming-boss", "boss", true);
			}
		}

		if (level == 2 && boss == null && !bossSpawnedOnce && gameTime - level2StartTime >= 8000) {
			boss = new Boss(600, twoPlayer);
			bossSpawnedOnce = true;
		}

		if (boss != null) {
			boss.update();
			// if boss just died, spawn multiple explosions over its sprite and remove it
			if (!boss.isAlive()) {
				int explCount = 10;
				for (int i = 0; i < explCount; i++) {
					int size = random.nextInt(40, 121);
					int exX = boss.getX() + random.nextInt(boss.getWidth());
					int exY = boss.getY() + random.nextInt(boss.getHeight());
					explosions.add(new Explosion(exX - size/2, exY - size/2, size));
				}
				// stop boss music/loop and play explosion sounds
				soundManager.stopClip("boss");
				soundManager.playClip("explosion", false);
				score += 10000;
				boss = null;
			}
		}

		ship.update();
		ship.move();
		if (ship2 != null) {
			ship2.update();
			ship2.move();
		}

		// only allow firing in level 1, or in level 2 after the boss health bar fills up
		boolean canFire = (level == 1) || (boss != null && boss.isHealthBarFull());
		if (canFire) {
			ship.fire();
			if (ship2 != null) {
				ship2.fire();
			}
		}
		if(level == 1 && gameTime - lastAlienSpawnTime >= random.nextInt(2000, 5001)){
			int roll = random.nextInt(100);
			if(roll < alienChance){
				int spawnX = random.nextInt(550);
				roll = random.nextInt(10);
				if(roll < circularAlien){
					aliens.add(new Alien(spawnX, 0, redTintFx));
				}
				else if(roll < sineAlien){
					aliens.add(new Alien(spawnX, 0, blueTintFx));
				}
				else if(roll < straightAlien){
					aliens.add(new Alien(spawnX, 0, null));
				}
			}
			lastAlienSpawnTime = gameTime;
		}

		// asteroid spawn logic: every 5 seconds
		if (gameTime - lastAsteroidSpawnTime >= 5000) {
			int roll = random.nextInt(100);
			if (roll < asteroidChance) {
				int spawnX = random.nextInt(550);
				asteroids.add(new Asteroid(spawnX, -50));
			}
			lastAsteroidSpawnTime = gameTime;
		}

		// update asteroids and remove dead/offscreen ones
		for (int a = asteroids.size() - 1; a >= 0; a--) {
			Asteroid ast = asteroids.get(a);
			ast.update();
			if (!ast.isAlive() || ast.isOffScreen(800)) {
				asteroids.remove(a);
			}
		}

		// check projectile-asteroid collisions
		checkProjectileCollisions(ship.getProjectiles());
		if (ship2 != null) {
			checkProjectileCollisions(ship2.getProjectiles());
		}

		// check projectile-boss collisions
		if (boss != null && boss.isAlive() && !boss.isSlidingIn()) {
			checkBossCollisions(ship.getProjectiles());
			if (ship2 != null) {
				checkBossCollisions(ship2.getProjectiles());
			}
		}

		// update explosions
		for (int e = explosions.size() - 1; e >= 0; e--) {
			explosions.get(e).update();
			if (!explosions.get(e).isActive()) {
				explosions.remove(e);
			}
		}

		// update items and check collection
		for (int it = items.size() - 1; it >= 0; it--) {
			Item item = items.get(it);
			item.update();
			if (!item.isActive()) {
				items.remove(it);
				continue;
			}
			if (item.getBoundingRectangle().intersects(ship.getBoundingRectangle())) {
				collectItem(item, ship);
				items.remove(it);
				continue;
			}
			if (ship2 != null && item.getBoundingRectangle().intersects(ship2.getBoundingRectangle())) {
				collectItem(item, ship2);
				items.remove(it);
			}
		}
	}

	private void checkProjectileCollisions(ArrayList<Projectile> projectiles) {
		for (int p = projectiles.size() - 1; p >= 0; p--) {
			Projectile proj = projectiles.get(p);
			for (int a = asteroids.size() - 1; a >= 0; a--) {
				Asteroid ast = asteroids.get(a);
				if (ast.isAlive() && ast.collidesWith(proj.getBoundingRectangle())) {
					explosions.add(new Explosion(ast.getX() - 25, ast.getY() - 25, ast.getSize() + 50));
					soundManager.playClip("explosion", false);
					ast.destroy();
					projectiles.remove(p);
					score += 10;
					spawnItemDrop(ast.getX() + ast.getSize() / 2, ast.getY());
					break;
				}
			}
		}
	}

	private void spawnItemDrop(int x, int y) {
		if (level == 1) {
			items.add(new EnergyCanister(x - 15, y));
		} else if (level == 2) {
			items.add(new PowerGem(x - 12, y));
		}
	}

	private void checkBossCollisions(ArrayList<Projectile> projectiles) {
		for (int p = projectiles.size() - 1; p >= 0; p--) {
			Projectile proj = projectiles.get(p);
			if (boss.getBoundingRectangle().intersects(proj.getBoundingRectangle())) {
				int dmg = 1;
				if (proj instanceof StraightProjectile sp) {
					dmg = sp.getDamage();
				}
				boss.takeDamage(dmg);
				projectiles.remove(p);
			}
		}
	}

	private void collectItem(Item item, Ship collector) {
		if (item instanceof EnergyCanister energyCanister) {
			energyCanister.collect();
			soundManager.playClip("canister", false);
			levelTimer += CANTIME;
		} else if (item instanceof PowerGem powerGem) {
			powerGem.collect();
			soundManager.playClip("powergem", false);
			collector.increaseDamage(1);
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
			backgroundImage.draw(imageContext, isPaused);
		}

		if(aliens != null){
			for(Alien alien : aliens){
				alien.draw(imageContext);
			}
		}

		if (boss != null) {
			boss.draw(imageContext);
			boss.drawHealthBar(imageContext);
		}

		if (ship != null) {
			ship.draw(imageContext);
		}

		if (ship2 != null) {
			ship2.draw(imageContext);
		}

		if (asteroids != null) {
			for (Asteroid ast : asteroids) {
				ast.draw(imageContext);
			}
		}

		if (explosions != null) {
			for (Explosion exp : explosions) {
				exp.draw(imageContext);
			}
		}

		if (items != null) {
			for (Item item : items) {
				item.draw(imageContext);
			}
		}

		// draw timer
		if (level == 1) {
			long seconds = Math.max(levelTimer / 1000, 0);
			long mins = seconds / 60;
			long secs = seconds % 60;
			String timerStr = String.format("%d:%02d", mins, secs);

			int boxX = 460;
			int boxY = 8;
			int boxW = 130;
			int boxH = 50;

			// background box
			imageContext.setColor(new Color(0, 0, 0, 150));
			imageContext.setStroke(new BasicStroke(3f));
			imageContext.fillRoundRect(boxX, boxY, boxW, boxH, 12, 12);
			imageContext.setColor(new Color(100, 200, 255));
			imageContext.drawRoundRect(boxX, boxY, boxW, boxH, 12, 12);

			// label
			imageContext.setColor(new Color(180, 180, 180));
			imageContext.setFont(new Font("Arial", Font.BOLD, 12));
			imageContext.drawString("TIME REMAINING", boxX + 18, boxY + 15);

			// timer value
			imageContext.setColor(Color.WHITE);
			imageContext.setFont(new Font("Arial", Font.BOLD, 24));
			imageContext.drawString(timerStr, boxX + 38, boxY + 42);
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
		showPlayerSelect = true;
		requestFocus();
	}

	public void selectPlayers(boolean two) {
		twoPlayer = two;
		showPlayerSelect = false;
		soundManager.stopClip("mainmenu");
		soundManager.playClip("start", false);
		requestFocus();
	}

	public void pauseGame() {
		if (isRunning) {
			if (isPaused) {
				isPaused = false;
				lastUpdateTime = System.currentTimeMillis();
				soundManager.resumeAll();
			} else {
				isPaused = true;
				soundManager.pauseAll();
				// stop ship movement so it doesn't keep going after unpause
				if (ship != null) {
					ship.setMoveDirection(-1);
					ship.setMoveDirection(-2);
					ship.setMoveDirection(-3);
					ship.setMoveDirection(-4);
				}
				if (ship2 != null) {
					ship2.setMoveDirection(-1);
					ship2.setMoveDirection(-2);
					ship2.setMoveDirection(-3);
					ship2.setMoveDirection(-4);
				}
			}
		}
	}

	public void endGame() {
		isRunning = false;
		gameOver();
		soundManager.pauseAll();
		soundManager.playClip("game-over", false);
	}

	public void gameOver(){
		Graphics2D g2 = (Graphics2D) getGraphics();
		g2.drawImage(image, 0, 0, 600, 800, null);
		g2.setColor(Color.white);
		g2.setFont(new Font("Arial", Font.BOLD, 40));
		g2.drawString("GAME OVER", 184, 304);
		g2.dispose();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_ESCAPE && isRunning && !gameOver)
			pauseGame();
		if (keyCode == KeyEvent.VK_A)
			updateShip(1);
		if (keyCode == KeyEvent.VK_D)
			updateShip(2);
		if (keyCode == KeyEvent.VK_W && ship != null && !isPaused)
			ship.setFiring(true);
		if (ship2 != null && !isPaused) {
			if (keyCode == KeyEvent.VK_LEFT)
				ship2.setMoveDirection(1);
			if (keyCode == KeyEvent.VK_RIGHT)
				ship2.setMoveDirection(2);
			if (keyCode == KeyEvent.VK_UP)
				ship2.setFiring(true);
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_A)
			updateShip(-1);
		if (keyCode == KeyEvent.VK_D)
			updateShip(-2);
		if (keyCode == KeyEvent.VK_W && ship != null)
			ship.setFiring(false);
		if (ship2 != null) {
			if (keyCode == KeyEvent.VK_LEFT)
				ship2.setMoveDirection(-1);
			if (keyCode == KeyEvent.VK_RIGHT)
				ship2.setMoveDirection(-2);
			if (keyCode == KeyEvent.VK_UP)
				ship2.setFiring(false);
		}
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
		} else if (showPlayerSelect) {
			int mx = e.getX() * 600 / getWidth();
			int my = e.getY() * 800 / getHeight();
			String clicked = playerSelectScreen.getButtonClicked(mx, my);
			if (clicked != null) {
				if (clicked.equals("1player")) {
					selectPlayers(false);
				} else if (clicked.equals("2players")) {
					selectPlayers(true);
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