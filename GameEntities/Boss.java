package GameEntities;
import Misc.Animation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Boss {
    public static final int BOSS_HEALTH = 500;

    private final int maxHealth;
    private final int x;
    private int y;
    private final int width, height;
    private final int targetY;
    private final int dy;
    private boolean slidingIn;

    private int health;
    private double displayedHealthRatio; // for the fill-up animation

    private int damageFlashTimer; // counts down frames for the red glow

    private final Animation animation;

    public Boss(int screenWidth, boolean twoPlayer) {
        if (twoPlayer) {
            maxHealth = BOSS_HEALTH * 2;
        } else {
            maxHealth = BOSS_HEALTH;
        }
        width = 250;
        height = 180;
        x = (screenWidth - width) / 2;
        y = -height;
        targetY = 80;
        dy = 1;
        slidingIn = true;

        health = maxHealth;
        displayedHealthRatio = 0.0;
        damageFlashTimer = 0;

        animation = new Animation(true, "images/BossAlien.png", 6, 3, 100);
        animation.start();
    }

    public void update() {
        if (slidingIn) {
            y += dy;
            if (y >= targetY) {
                y = targetY;
                slidingIn = false;
            }
        }

        // slowly fill up the displayed health bar once sliding is done
        if (!slidingIn) {
            double actualRatio = (double) health / maxHealth;
            if (displayedHealthRatio < actualRatio) {
                displayedHealthRatio += 0.003; // animate fill
            }
            displayedHealthRatio = Math.min(displayedHealthRatio, actualRatio);
        }

        if (damageFlashTimer > 0) {
            damageFlashTimer--;
        }

        animation.update();
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
        damageFlashTimer = 15;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isHealthBarFull() {
        double actualRatio = (double) health / maxHealth;
        return displayedHealthRatio >= actualRatio;
    }

    public void draw(Graphics2D g2) {
        if (animation.getImage() != null) {
            g2.drawImage(animation.getImage(), x, y, width, height, null);
        }
    }

    public void drawHealthBar(Graphics2D g2) {
        if (slidingIn) return;

        int barX = 60;
        int barY = 12;
        int barW = 526;
        int barH = 32;

        // damage flash
        if (damageFlashTimer > 0) {
                // fade out quickly
                int alpha = Math.min(180, damageFlashTimer * 12);
            g2.setColor(new Color(255, 0, 0, alpha));
            g2.setStroke(new BasicStroke(6f));
            g2.drawRoundRect(barX - 3, barY - 3, barW + 6, barH + 6, 14, 14);
        }

        // background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(barX, barY, barW, barH, 10, 10);

        // gradient fill that changes color based on health %
        int fillW = (int) (displayedHealthRatio * (barW - 4));
        if (fillW > 0) {
            double healthPct = displayedHealthRatio * 100;
            Color barColor;
            if (healthPct > 50) {
                barColor = new Color(200, 30, 30);       // red
            } else if (healthPct > 25) {
                barColor = new Color(230, 140, 20);      // orange
            } else {
                barColor = new Color(230, 220, 30);      // yellow
            }

            Color lighter = barColor.brighter();
            GradientPaint gp = new GradientPaint(
                barX + 2, barY + 2, lighter,
                barX + 2, barY + barH - 2, barColor
            );
            g2.setPaint(gp);
            g2.fillRoundRect(barX + 2, barY + 2, fillW, barH - 4, 8, 8);
        }

        // border
        g2.setColor(new Color(100, 200, 255));
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(barX, barY, barW, barH, 10, 10);

        // health text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String healthStr = health + " / " + maxHealth;
        int textW = g2.getFontMetrics().stringWidth(healthStr);
        g2.drawString(healthStr, barX + (barW - textW) / 2, barY + 22);
    }

    public boolean isSlidingIn() {
        return slidingIn;
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public int getHealth() { return health; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
