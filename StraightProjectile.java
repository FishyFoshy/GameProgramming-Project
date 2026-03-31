import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class StraightProjectile implements Projectile {
    private int x, y;
    private int size;
    private int dy;
    private boolean active;
    private boolean isShip;

    public StraightProjectile(int startX, int startY, boolean isShip) {
        this.x = startX;
        this.y = startY;
        this.size = 8;
        this.dy = 10;
        this.active = true;
        this.isShip = isShip;
    }

    public void update() {
        if (active) {
            if (isShip) {
                y -= dy;
                if (y + size < 0) {
                    active = false;
                }
            } else {
                y += dy;
                if (y > 800) {
                    active = false;
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        if (!active) return;
        if (isShip) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(Color.RED);
        }
        g2.fillRect(x, y, size, size);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, size, size);
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }
}
