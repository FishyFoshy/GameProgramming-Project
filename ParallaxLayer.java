import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ParallaxLayer {

   private int y, startY;
   private int width;
   private int height;

   private BufferedImage image;	// background image
    private float factor; 

   public ParallaxLayer (BufferedImage img, float factor, int startY) {
      width = 600;
      height = 2400;
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

   public void setY(int newY){ y = newY;}
   public int getWidth(){ return width;}
   public int getHeight(){ return height;}
   public int getY(){ return y;}
   public Image getImage(){ return image;}
}