import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Projectile {
    void update();
    void draw(Graphics2D g2);
    Rectangle2D.Double getBoundingRectangle();
    boolean isActive();
}
