import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PowerGem implements Item {
    private int x, y;
    private int width, height;
    private int dy;
    private boolean active;
    private BufferedImage image;

    public PowerGem(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 25;
        this.height = 25;
        this.dy = 2;
        this.active = true;

        image = ImageManager.loadBufferedImage("images/powerGem.png");
    }

    public void update() {
        if (!active) return;
        y += dy;
    }

    public void draw(Graphics2D g2) {
        if (!active) return;
        g2.drawImage(image, x, y, width, height, null);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    public boolean isActive() { return active; }

    public void collect() { active = false; }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
