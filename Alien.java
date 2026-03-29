import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import java.awt.Image;

public class Alien {
   private int x, worldX;
   private int y, worldY;
   
   private int width;
   private int height;

   private Animation alienAnimation;
   private Background background;
   private ImageFX effect;

   private boolean isCollected;

   public Alien (JPanel p, int xPos, int yPos, Background bg, ImageFX effect) {
      isCollected = false;

      width = 48;
      height = 48;

      background = bg;
      this.effect = effect;

      this.worldX = xPos;
      this.worldY = yPos;

      alienAnimation = new Animation(true, "images/alien.png", 1, 6, 200);

      alienAnimation.start();
   }

   public void draw (Graphics2D g2) {
      x = worldX + background.getX();
      y = worldY + background.getY();

      Image drawnImage = alienAnimation.getImage();
      if(effect != null)
         drawnImage = effect.apply((BufferedImage) drawnImage);

      if(alienAnimation != null){
         g2.drawImage(drawnImage, x, y, width, height, null);
      }
   }

   public void update(){
      if(alienAnimation != null){
         alienAnimation.update();
      }
   }

   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (worldX, worldY, width, height);
   }

   public void collected(){ isCollected = true;}

   public void setEffect(ImageFX newFx) { effect = newFx; }
   public boolean isCollected(){return isCollected;}
   public int getX() { return x; }
	public int getY() { return y; }
	public int getWorldX() { return worldX; }
	public int getWorldY() { return worldY; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
   public ImageFX getEffect() { 
      if(effect != null)
         return effect;
      return null;
   }
}