import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PlayerSelectScreen {

	private int panelWidth, panelHeight;
	private int buttonWidth = 200;
	private int buttonHeight = 50;
	private int buttonX;
	private int onePlayerButtonY, twoPlayerButtonY;

	public PlayerSelectScreen(int width, int height) {
		panelWidth = width;
		panelHeight = height;
		buttonX = (width - buttonWidth) / 2;
		onePlayerButtonY = height / 2 - 40;
		twoPlayerButtonY = height / 2 + 40;
	}

	public void draw(Graphics2D g) {
		BackgroundManager bg = new BackgroundManager();
		bg.draw(g, false);

		// title
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 28));
		g.drawString("Select Number of Players", panelWidth / 2 - 180, panelHeight / 2 - 100);

		Color green = new Color(0, 150, 0, 150);
		g.setFont(new Font("Arial", Font.BOLD, 22));

		// 1 player button
		g.setColor(green);
		g.fillRoundRect(buttonX, onePlayerButtonY, buttonWidth, buttonHeight, 18, 18);
		g.setColor(Color.WHITE);
		g.drawString("1 Player", buttonX + 60, onePlayerButtonY + 33);

		// 2 players button
		g.setColor(green);
		g.fillRoundRect(buttonX, twoPlayerButtonY, buttonWidth, buttonHeight, 18, 18);
		g.setColor(Color.WHITE);
		g.drawString("2 Players", buttonX + 53, twoPlayerButtonY + 33);
	}

	public String getButtonClicked(int x, int y) {
		if (x >= buttonX && x <= buttonX + buttonWidth) {
			if (y >= onePlayerButtonY && y <= onePlayerButtonY + buttonHeight)
				return "1player";
			if (y >= twoPlayerButtonY && y <= twoPlayerButtonY + buttonHeight)
				return "2players";
		}
		return null;
	}
}
