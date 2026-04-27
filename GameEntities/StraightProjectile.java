package GameEntities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class StraightProjectile implements Projectile {
    private int x, y;
    private int size;
    private int dy;
    private boolean active;
    private boolean isShip;
    private int damage;
    private Color colour;

    public StraightProjectile(int startX, int startY, boolean isShip, int damage, int colour) {
        this.x = startX;
        this.y = startY;
        this.size = 8;
        this.dy = 10;
        this.active = true;
        this.isShip = isShip;
        this.damage = damage;

        if(colour == 0)
            this.colour = Color.GREEN;
        else if(colour == 1)
            this.colour = Color.CYAN;
        else if(colour == 2)
            this.colour = Color.YELLOW;
        else if(colour == 3)
            this.colour = new Color(228, 66, 243);
        else if(colour == 4)
            this.colour = Color.RED;
    }

    public int getDamage() {
        return damage;
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
            g2.setColor(colour);
        } else {
            g2.setColor(colour);
        }
        g2.fillRect(x, y, size, size);

        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, size, size);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        if(isActive())
            return new Rectangle2D.Double(x, y, size, size);
        return new Rectangle2D.Double(0,0,0,0);
    }

    public boolean isActive() {
        return active;
    }

    public boolean isShip() {
        return isShip;
    }
}
