package GameEntities;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import Misc.Animation;

public class EnergyCanister implements Item {
    private int x, y;
    private int width, height;
    private int dy;
    private boolean active;
    private Animation animation;

    public EnergyCanister(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.width = 30;
        this.height = 40;
        this.dy = 4;
        this.active = true;

        animation = new Animation(true, "images/sodaCan.png", 4, 3, 100);
        animation.start();
    }

    public void update() {
        if (!active) return;
        y += dy;
        animation.update();
    }

    public void draw(Graphics2D g2) {
        if (!active) return;
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
