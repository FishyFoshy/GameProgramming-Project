package Screens;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ParallaxLayer {

   private final int startY;
   private int y;
   private final BufferedImage image;	// background image
   private final float factor; 

   public ParallaxLayer (BufferedImage img, float factor, int startY) {
      this.image = img;
      this.factor = factor;
      y = startY;
      this.startY = startY;
   }

   public void draw(Graphics2D g2, int dy) {
      y = y + Math.round(dy * factor);

      if(y >= 0){
         y = startY;
      }

      g2.drawImage(image, 0, y, null);

   }
}