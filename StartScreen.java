import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class StartScreen {

	private int panelWidth, panelHeight;
	private int buttonWidth = 200;
	private int buttonHeight = 50;
	private int buttonX;
	private int startButtonY, instructionsButtonY, exitButtonY;
	private boolean showingInstructions = false;
	private int xButtonX, xButtonY, xButtonSize = 30;

	private String instructionsText = 
		"""
                Control your spaceship to destroy incoming Aliens and avoid
                falling Asteroids. Shoot Aliens to earn points, and collect
                Energy Canisters (Level 1) to refill fuel or Power Gems
                (Level 2) to increase your damage, from destroyed Asteroids.
                Watch each Alien's color to predict their attack pattern.
                
                Controls:
                Player 1: A = left, D = right, W = fire
                Player 2: Left/Right arrows = move, Up arrow = fire
                
                Survive Level 1, fight for your life in Level 2,
                and rack up the highest score!
                
                Good luck out there space ranger!""";

	public StartScreen(int width, int height) {
		panelWidth = width;
		panelHeight = height;
		buttonX = (width - buttonWidth) / 2;
		startButtonY = height / 2 - 100;
		instructionsButtonY = height / 2 - 25;
		exitButtonY = height / 2 + 50;
		xButtonX = width - xButtonSize - 50;
		xButtonY = 50;
	}

	public void draw(Graphics2D g) {
		if (showingInstructions) {
			drawInstructions(g);
		} else {
			drawMainMenu(g);
		}
	}

	private void drawMainMenu(Graphics2D g) {
		BackgroundManager bg = new BackgroundManager();
		bg.draw(g, false);

		Color green = new Color(0, 150, 0, 150);
		g.setFont(new Font("Arial", Font.BOLD, 22));

		// start button
		g.setColor(green);
		g.fillRoundRect(buttonX, startButtonY, buttonWidth, buttonHeight, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString("Start", buttonX + 70, startButtonY + 33);

		// instructions button
		g.setColor(green);
		g.fillRoundRect(buttonX, instructionsButtonY, buttonWidth, buttonHeight, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString("Instructions", buttonX + 35, instructionsButtonY + 33);

		// exit button
		g.setColor(green);
		g.fillRoundRect(buttonX, exitButtonY, buttonWidth, buttonHeight, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString("Exit", buttonX + 78, exitButtonY + 33);
	}

	private void drawInstructions(Graphics2D g) {
		BackgroundManager bg = new BackgroundManager();
		bg.draw(g, false);

		int padding = 40;
		Color green = new Color(0, 150, 0, 150);
		g.setColor(green);
		g.fillRoundRect(padding, padding, panelWidth - padding * 2, panelHeight - padding * 2, 24, 24);

		// title
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 28));
		g.drawString("Instructions", padding + 20, padding + 45);

		// instructions text
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		FontMetrics fm = g.getFontMetrics();
		int lineHeight = fm.getHeight();
		int textX = padding + 20;
		int textY = padding + 90;

		String[] lines = instructionsText.split("\n");
		for (String line : lines) {
			g.drawString(line, textX, textY);
			textY += lineHeight;
		}

		// draw X button in top right
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.BOLD, 22));
		g.drawRoundRect(xButtonX, xButtonY, xButtonSize, xButtonSize, 8, 8);
		g.drawString("X", xButtonX + 7, xButtonY + 23);
	}

	public boolean isShowingInstructions() {
		return showingInstructions;
	}

	public void setShowingInstructions(boolean b) {
		showingInstructions = b;
	}

	public String getButtonClicked(int x, int y) {
		if (showingInstructions) {
			// check X button
			if (x >= xButtonX && x <= xButtonX + xButtonSize &&
				y >= xButtonY && y <= xButtonY + xButtonSize) {
				return "closeInstructions";
			}
			return null;
		}

		if (x >= buttonX && x <= buttonX + buttonWidth) {
			if (y >= startButtonY && y <= startButtonY + buttonHeight)
				return "start";
			if (y >= instructionsButtonY && y <= instructionsButtonY + buttonHeight)
				return "instructions";
			if (y >= exitButtonY && y <= exitButtonY + buttonHeight)
				return "exit";
		}
		return null;
	}
}
