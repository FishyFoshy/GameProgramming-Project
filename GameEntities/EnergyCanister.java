package GameEntities;
import ImageManip.GlowFX;
import Misc.Animation;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class EnergyCanister implements Item {
    private int x, y;
    private int width, height;
    private int dy;
    private boolean active;
    private Animation animation;
    private GlowFX glowFX;
    private static final int GLOW_PAD = 6;

    public EnergyCanister(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 30;
        this.height = 40;
        this.dy = 4;
        this.active = true;

        animation = new Animation(true, "images/sodaCan.png", 4, 3, 100);
        animation.start();
        glowFX = new GlowFX(255, 215, 0, 160);
    }

    public void update() {
        if (!active) return;
        y += dy;
        animation.update();
    }

    public void draw(Graphics2D g2) {
        if (!active) return;

        BufferedImage glowImage = glowFX.apply(animation.getImage());
        g2.drawImage(glowImage, x - GLOW_PAD, y - GLOW_PAD,
                     width + GLOW_PAD * 2, height + GLOW_PAD * 2, null);
        g2.drawImage(animation.getImage(), x, y, width, height, null);
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
