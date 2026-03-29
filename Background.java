import java.awt.Graphics2D;

import java.awt.Image;

public class Background {

   private int x;
   private int y;
   private int width;
   private int height;

   private Image backgroundImage;

   public Background () {
      x = 0;
      y = 0;

      width = 2560;
      height = 1440;

      backgroundImage = ImageManager.loadImage("images/background.jpg");
   }


   public void draw (Graphics2D g2) {
      g2.drawImage(backgroundImage, x, y, null);
   }

   public void setX(int newX){ x = newX;}
   public void setY(int newY){ y = newY;}
   public int getWidth(){ return width;}
   public int getHeight(){ return height;}
   public int getX(){ return x;}
   public int getY(){ return y;}
   public Image getImage(){ return backgroundImage;}
}