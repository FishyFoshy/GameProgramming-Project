import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface Item {
    void update();
    void draw(Graphics2D g2);
    Rectangle2D.Double getBoundingRectangle();
    boolean isActive();
    int getX();
    int getY();
}
