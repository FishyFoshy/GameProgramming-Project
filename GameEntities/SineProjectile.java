package GameEntities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class SineProjectile implements Projectile {
    private int x, y;
    private int size;
    private int dy;
    private boolean active;
    private boolean isShip;

    private double amplitudeFactor;
    private double frequencyFactor;
    private int yAxis;
    private Color colour;

    public SineProjectile(int startX, int startY, boolean isShip, int colour) {
        this.x = startX;
        this.y = startY;
        this.yAxis = startX;
        this.size = 8;
        this.dy = 4;
        this.active = true;
        this.isShip = isShip;
        amplitudeFactor = 75;
        frequencyFactor = 1;

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
            double radians = (2*y / 180.0) * Math.PI * frequencyFactor;

            x = (int) (Math.sin (radians) * amplitudeFactor);

            x = yAxis - x;
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
