package GameEntities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class CircularProjectile implements Projectile {
    private int x, y;
    private final int size;
    private final int dy;
    private boolean active;
    private final boolean isShip;
    private final int centreX, radius;
    private int centreY, degree;

    private Color colour;

    public CircularProjectile(int startX, int startY, boolean isShip, int colour) {
        this.x = startX;
        this.y = startY;
        this.centreX = startX;
        this.centreY = startY;
        this.size = 8;
        this.dy = 3;
        this.active = true;
        this.isShip = isShip;
        this.radius = 75;
        this.degree = 75;

        switch (colour) {
            case 0:
                this.colour = Color.GREEN;
                break;
            case 1:
                this.colour = Color.CYAN;
                break;
            case 2:
                this.colour = Color.YELLOW;
                break;
            default:
                break;
        }
    }

    @Override
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

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;
        if (isShip) {
            g2.setColor(colour);
        } else {
            g2.setColor(colour);
        }
        g2.fillRect(x, y, size, size);

        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, size, size);
    }

    @Override
    public Rectangle2D.Double getBoundingRectangle() {
        if(isActive())
            return new Rectangle2D.Double(x, y, size, size);
        return new Rectangle2D.Double(0,0,0,0);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isShip() {
        return isShip;
    }
}
