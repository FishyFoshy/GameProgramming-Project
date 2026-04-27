package Screens;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class WinScreen {

    private int panelWidth, panelHeight;
    private int buttonWidth = 200;
    private int buttonHeight = 50;
    private int buttonX;
    private int restartButtonY, exitButtonY;

    public WinScreen(int width, int height) {
        panelWidth = width;
        panelHeight = height;
        buttonX = (width - buttonWidth) / 2;
        restartButtonY = height / 2 + 20;
        exitButtonY = height / 2 + 90;
    }

    public void draw(Graphics2D g, int score, int highScore) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, panelWidth, panelHeight);

        // title
        g.setColor(new Color(255, 215, 0)); // gold
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String title = "YOU WIN!";
        int tw = g.getFontMetrics().stringWidth(title);
        g.drawString(title, (panelWidth - tw) / 2, panelHeight / 2 - 120);

        // high score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        String hsText = "High Score: " + highScore;
        int hw = g.getFontMetrics().stringWidth(hsText);
        g.drawString(hsText, (panelWidth - hw) / 2, panelHeight / 2 - 60);

        // current score
        String scoreText = "Score: " + score;
        int sw = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (panelWidth - sw) / 2, panelHeight / 2 - 20);

        Color green = new Color(0, 150, 0, 150);
        g.setFont(new Font("Arial", Font.BOLD, 22));

        // play again button
        g.setColor(green);
        g.fillRoundRect(buttonX, restartButtonY, buttonWidth, buttonHeight, 20, 20);
        g.setColor(Color.WHITE);
        g.drawString("Play Again", buttonX + 43, restartButtonY + 33);

        // exit button
        g.setColor(green);
        g.fillRoundRect(buttonX, exitButtonY, buttonWidth, buttonHeight, 20, 20);
        g.setColor(Color.WHITE);
        g.drawString("Exit", buttonX + 78, exitButtonY + 33);
    }

    public String getButtonClicked(int x, int y) {
        if (x >= buttonX && x <= buttonX + buttonWidth) {
            if (y >= restartButtonY && y <= restartButtonY + buttonHeight)
                return "restart";
            if (y >= exitButtonY && y <= exitButtonY + buttonHeight)
                return "exit";
        }
        return null;
    }
}
