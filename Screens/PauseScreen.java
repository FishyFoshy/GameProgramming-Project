package Screens;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PauseScreen {

	private int panelWidth, panelHeight;
	private int buttonWidth = 200;
	private int buttonHeight = 50;
	private int buttonX;
	private int resumeButtonY, exitButtonY;

	// pause button in top left
	private int pauseButtonX = 10;
	private int pauseButtonY = 10;
	private int pauseButtonSize = 40;

	public PauseScreen(int width, int height) {
		panelWidth = width;
		panelHeight = height;
		buttonX = (width - buttonWidth) / 2;
		resumeButtonY = height / 2 - 60;
		exitButtonY = height / 2 + 10;
	}

	public void drawPauseButton(Graphics2D g) {
		Color green = new Color(0, 150, 0, 150);
		g.setColor(green);
		g.fillRoundRect(pauseButtonX, pauseButtonY, pauseButtonSize, pauseButtonSize, 10, 10);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 22));
		g.drawString("I I", pauseButtonX + 12, pauseButtonY + 28);
	}

	public void drawScore(Graphics2D g, int score) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 18));
		g.drawString("Score: " + score, pauseButtonX, pauseButtonY + pauseButtonSize + 20);
	}

	public void drawPauseMenu(Graphics2D g) {
		// dim overlay
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, panelWidth, panelHeight);

		// paused text
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		g.drawString("PAUSED", panelWidth / 2 - 90, panelHeight / 2 - 100);

		Color green = new Color(0, 150, 0, 150);
		g.setFont(new Font("Arial", Font.BOLD, 22));

		// resume button
		g.setColor(green);
		g.fillRoundRect(buttonX, resumeButtonY, buttonWidth, buttonHeight, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString("Resume", buttonX + 58, resumeButtonY + 33);

		// exit button
		g.setColor(green);
		g.fillRoundRect(buttonX, exitButtonY, buttonWidth, buttonHeight, 20, 20);
		g.setColor(Color.WHITE);
		g.drawString("Exit", buttonX + 78, exitButtonY + 33);
	}

	public String getButtonClicked(int x, int y, boolean isPaused) {
		if (!isPaused) {
			// check pause button
			if (x >= pauseButtonX && x <= pauseButtonX + pauseButtonSize &&
				y >= pauseButtonY && y <= pauseButtonY + pauseButtonSize) {
				return "pause";
			}
			return null;
		}

		// check resume and exit buttons
		if (x >= buttonX && x <= buttonX + buttonWidth) {
			if (y >= resumeButtonY && y <= resumeButtonY + buttonHeight)
				return "resume";
			if (y >= exitButtonY && y <= exitButtonY + buttonHeight)
				return "exit";
		}
		return null;
	}
}
