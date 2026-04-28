package GameEntities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class StraightProjectile implements Projectile {
    private final int x;
    private int y;
    private final int size;
    private final int dy;
    private boolean active;
    private final boolean isShip;
    private final int damage;
    private Color colour;

    public StraightProjectile(int startX, int startY, boolean isShip, int damage, int colour) {
        this.x = startX;
        this.y = startY;
        this.size = 8;
        this.dy = 10;
        this.active = true;
        this.isShip = isShip;
        this.damage = damage;

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
            case 3:
                this.colour = Color.MAGENTA;
                break;
            case 4:
                this.colour = Color.ORANGE;
                break;
            default:
                break;
        }
    }

    public int getDamage() {
        return damage;
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
