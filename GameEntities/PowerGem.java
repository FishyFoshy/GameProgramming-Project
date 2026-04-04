package GameEntities;
import ImageManip.GlowFX;
import ImageManip.ImageManager;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class PowerGem implements Item {
    private int x, y;
    private int width, height;
    private int dy;
    private boolean active;
    private BufferedImage image;
    private GlowFX glowFX;
    private static final int GLOW_PAD = 6;

    public PowerGem(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 25;
        this.height = 25;
        this.dy = 4;
        this.active = true;

        image = ImageManager.loadBufferedImage("images/powerGem.png");
        glowFX = new GlowFX(255, 215, 0, 180);
    }

    public void update() {
        if (!active) return;
        y += dy;
    }

    public void draw(Graphics2D g2) {
        if (!active) return;

        BufferedImage glowImage = glowFX.apply(image);
        g2.drawImage(glowImage, x - GLOW_PAD, y - GLOW_PAD,
                     width + GLOW_PAD * 2, height + GLOW_PAD * 2, null);
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
