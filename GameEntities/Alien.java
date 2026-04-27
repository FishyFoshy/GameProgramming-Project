package GameEntities;
import ImageManip.ImageFX;
import ImageManip.ImageManager;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class Alien {
   private int x;
   private int y, dy, dx;
   
   private int width;
   private int height;

   private Image alienImage;
   private ImageFX effect;
   private final Random rand;

   private boolean dead;

   private ArrayList<Projectile> projectiles;


   public Alien (int xPos, int yPos, ImageFX effect) {
      width = 50;
      height = 50;

      this.effect = effect;
      dy = 2;
      dx = 0;
      x = xPos;
      y = yPos;
      rand = new Random();
      dead = true;

      alienImage = ImageManager.loadBufferedImage("images/Alien.png");
   }
   
   public void draw (Graphics2D g2) {
      Image drawnImage = ImageManager.copyImage((BufferedImage) alienImage);
      if(effect != null)
         drawnImage = effect.apply((BufferedImage) drawnImage);

      if(alienImage != null){
         g2.drawImage(drawnImage, x, y, width, height, null);
      }

      for (Projectile p : projectiles) {
         p.draw(g2);
      }
   }

   public void move(){
      // 5% chance per frame to change horizontal direction
      if (rand.nextInt(100) < 5) {
         dx = rand.nextInt(5) - 2;
      }
      x += dx;
      if (x < 0) { x = 0; dx = 0; }
      if (x > 550) { x = 550; dx = 0; }

      y += dy;
      if(y > 100){
         y = 100;
      }
   }

   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x, y, width, height);
   }

   public void fire() {
         int bulletX = x + width / 2 - 4;
         int bulletY = y + 50;
         if(effect == null)
            projectiles.add(new StraightProjectile(bulletX, bulletY, false, 1, 0));
         else if(effect.getEffectName() == "blue")
            projectiles.add(new SineProjectile(bulletX, bulletY, false, 1));
         else if(effect.getEffectName() == "red")
            projectiles.add(new CircularProjectile(bulletX, bulletY, false, 2));
   }

   public ArrayList<Projectile> getProjectiles() {
      return projectiles;
   }

   public boolean isDead() { return dead; }
   public void setDead(boolean d) { dead = d; }

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