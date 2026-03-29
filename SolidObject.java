import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class SolidObject {
	private int x;
	private int y;
	private int width, height, worldX, worldY;
	private Background background;

	public SolidObject (JPanel p, int xPos, int yPos, int width, int height, Background bg) { 
		worldX = xPos;
		worldY = yPos;
		this.width = width;
		this.height = height;
		background = bg;
	}

	public void draw (Graphics2D g2) {
		x = worldX + background.getX();
		y = worldY + background.getY();

		Rectangle2D.Double solid = new Rectangle2D.Double(x, y, width, height);
		g2.setColor(new Color(111, 78, 55));
		g2.fill(solid);
	}

	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (worldX, worldY, width, height);
	}

   public int getX() { return x; }
	public int getY() { return y; }
	public int getWorldX() { return worldX; }
	public int getWorldY() { return worldY; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
}