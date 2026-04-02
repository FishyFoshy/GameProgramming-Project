import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class CircularProjectile implements Projectile {
    private int x, y;
    private int size;
    private int dy;
    private boolean active;
    private boolean isShip;

    private int centreY, centreX, radius, degree;

    public CircularProjectile(int startX, int startY, boolean isShip) {
        this.x = startX;
        this.y = startY;
        this.centreX = startX;
        this.centreY = startY;
        this.size = 8;
        this.dy = 3;
        this.active = true;
        this.isShip = isShip;
        radius = 75;
        degree = 75;
    }

    public void update() {
        if (active) {
            degree = degree + 9;
            if (degree > 360)
                degree = 0;

            double radians = (degree / 180.0) * Math.PI;
            x = (int) (radius * Math.cos (radians)) + centreX + 5;
            if (isShip) {
                radians = radians * -1;
                y = (centreY - 55 - (int) (radius * Math.sin (radians) * 1));
                centreY -= dy;
                if (y + size < 0) {
                    active = false;
                }
            } else {
                y = (centreY + 55 - (int) (radius * Math.sin (radians) * 1));
                centreY += dy;
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
