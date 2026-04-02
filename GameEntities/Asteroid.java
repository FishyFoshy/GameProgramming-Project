package GameEntities;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Asteroid {
    private int x, y;
    private int size;
    private int dy;
    private boolean alive;

    // hitbox rectangles (top, middle, bottom)
    private int topW, topH;
    private int midW, midH;
    private int botW, botH;

    public Asteroid(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.size = 50;
        this.dy = 2;
        this.alive = true;

        // small rectangle at top
        topW = (int)(size * 0.6);
        topH = size / 4;

        // longer rectangle in the middle
        midW = size;
        midH = size / 2;

        // small rectangle at bottom
        botW = (int)(size * 0.6);
        botH = size / 4;
    }

    public void update() {
        if (alive) {
            y += dy;
        }
    }

    public void draw(Graphics2D g2) {
        if (!alive) return;

        GradientPaint bodyGradient = new GradientPaint(
            x, y, new Color(160, 140, 120),
            x + size, y + size, new Color(90, 75, 60)
        );
        g2.setPaint(bodyGradient);
        Ellipse2D.Double body = new Ellipse2D.Double(x, y, size, size);
        g2.fill(body);

        // rocky surface texture
        g2.setColor(new Color(120, 100, 80, 100));
        g2.fillOval(x + 4, y + 3, 5, 4);
        g2.fillOval(x + 30, y + 5, 4, 3);
        g2.fillOval(x + 40, y + 15, 5, 4);
        g2.fillOval(x + 10, y + 38, 4, 3);
        g2.fillOval(x + 35, y + 38, 6, 4);
        g2.fillOval(x + 22, y + 2, 3, 3);
        g2.fillOval(x + 3, y + 20, 4, 5);
        g2.fillOval(x + 42, y + 30, 4, 3);

        // crater 1
        g2.setColor(new Color(100, 85, 70));
        g2.fillOval(x + 8, y + 7, 14, 11);
        g2.setColor(new Color(75, 62, 50));
        g2.fillOval(x + 10, y + 9, 10, 7);

        // crater 2
        g2.setColor(new Color(100, 85, 70));
        g2.fillOval(x + 28, y + 22, 15, 12);
        g2.setColor(new Color(70, 58, 45));
        g2.fillOval(x + 30, y + 24, 11, 8);

        // crater 3
        g2.setColor(new Color(105, 88, 72));
        g2.fillOval(x + 6, y + 30, 10, 9);
        g2.setColor(new Color(78, 65, 52));
        g2.fillOval(x + 8, y + 32, 6, 5);

        // crater 4
        g2.setColor(new Color(95, 80, 65));
        g2.fillOval(x + 24, y + 5, 8, 6);
        g2.setColor(new Color(72, 60, 48));
        g2.fillOval(x + 25, y + 6, 5, 4);

        // crater 5
        g2.setColor(new Color(95, 80, 65));
        g2.fillOval(x + 36, y + 36, 7, 6);
        g2.setColor(new Color(72, 60, 48));
        g2.fillOval(x + 37, y + 37, 5, 4);
    }

    public boolean collidesWith(Rectangle2D.Double other) {
        if (!alive) return false;

        int topX = x + (size - topW) / 2;
        int topY = y;
        Rectangle2D.Double topBox = new Rectangle2D.Double(topX, topY, topW, topH);

        int midX = x;
        int midY = y + topH;
        Rectangle2D.Double midBox = new Rectangle2D.Double(midX, midY, midW, midH);

        int botX = x + (size - botW) / 2;
        int botY = y + topH + midH;
        Rectangle2D.Double botBox = new Rectangle2D.Double(botX, botY, botW, botH);

        return topBox.intersects(other) || midBox.intersects(other) || botBox.intersects(other);
    }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }

    public void destroy() {
        alive = false;
    }

    public boolean isAlive() { return alive; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
}