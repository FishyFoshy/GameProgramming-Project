import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import java.awt.Image;

public class Alien {
   private int x;
   private int y;
   
   private int width;
   private int height;

   private Image alienImage;
   private ImageFX effect;


   public Alien (int xPos, int yPos, ImageFX effect) {
      width = 50;
      height = 50;

      this.effect = effect;

      x = xPos;
      y = yPos;

      alienImage = ImageManager.loadBufferedImage("images/Alien.png");
   }
   
   public void draw (Graphics2D g2) {
      Image drawnImage = ImageManager.copyImage((BufferedImage) alienImage);
      if(effect != null)
         drawnImage = effect.apply((BufferedImage) drawnImage);

      if(alienImage != null){
         g2.drawImage(drawnImage, x, y, width, height, null);
      }
   }

   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x, y, width, height);
   }

   public void setEffect(ImageFX newFx) { effect = newFx; }
   public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
   public ImageFX getEffect() { 
      if(effect != null)
         return effect;
      return null;
   }
}