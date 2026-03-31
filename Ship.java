import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Ship {
   private JPanel panel;
   private int width;
   private int height;
   private int x;
   private int y;
   private boolean left, right, isP2;
   private boolean firing;
   private long lastFireTime;
   private static final long FIRE_COOLDOWN = 300;

   private int dx;

   private Dimension dimension;

   private SoundManager soundManager;
   private Animation movementAnimation, rightAnimation, leftAnimation, nextAnimation, idleAnimation;

   private ArrayList<Alien> aliens;
   private ArrayList<Projectile> projectiles;
   private ImageFX gray;

   public Ship (JPanel p, int xPos, int yPos, ArrayList<Alien> a, boolean isP2) {
      panel = p;
      dimension = panel.getSize();

      this.isP2 = isP2;

      gray = new GrayScaleFX();

      width = 80;
      height = 65;

      aliens = a;
      projectiles = new ArrayList<>();

      x = xPos;
      y = yPos;

      left = right = firing = false;
      lastFireTime = 0;

      dx = 10;

      soundManager = SoundManager.getInstance();

      idleAnimation = new Animation(true, "images/shipIdleP1.png",3, 2, 100);
      rightAnimation = new Animation(true, "images/rightP1.png", 3, 2, 100);
      leftAnimation = new Animation(true, "images/leftP1.png", 3, 2, 100);

      if(isP2){
         idleAnimation = new Animation(true, "images/shipIdleP2.png", 3, 2, 100);
         rightAnimation = new Animation(true, "images/rightP2.png", 3, 2, 100);
         leftAnimation = new Animation(true, "images/leftP2.png", 3, 2, 100);
      }

      movementAnimation = null;
      idleAnimation.start();
   }

   public void setFiring(boolean f) {
      firing = f;
   }

   public void fire() {
      long now = System.currentTimeMillis();
      if (firing && now - lastFireTime >= FIRE_COOLDOWN) {
         int bulletX = x + width / 2 - 4;
         int bulletY = y;
         projectiles.add(new StraightProjectile(bulletX, bulletY, true));
         lastFireTime = now;
      }
   }

   public ArrayList<Projectile> getProjectiles() {
      return projectiles;
   }

   public void draw (Graphics2D g2) {
      if (idleAnimation != null) {
         g2.drawImage(idleAnimation.getImage(), x, y, width, height, null);
      }
      if(movementAnimation != null){
         g2.drawImage(movementAnimation.getImage(), x, y, width, height, null);
      }
      for (Projectile p : projectiles) {
         p.draw(g2);
      }
   }

   public void setMoveDirection(int direction){
      if(direction == 1) left = true;
      if(direction == 2) right = true;

      if(direction == -1) left = false;
      if(direction == -2) right = false;
    }

   public void move () {
      if (!panel.isVisible ()) return;
      
      dimension = panel.getSize();

      if (left) {	// move left
         x = x - dx;
         if (x < 0-15)
            x = 0-15;
      }

      if (right) {  	// move right
         x = x + dx;
         if (x + width > dimension.width+15)
            x = dimension.width - width+15;
      }
      collidesWithAlien();
   }
   
	public void update() {
      nextAnimation = null;
      if(left){
         nextAnimation = leftAnimation;
      }
      if(right){
         nextAnimation = rightAnimation;
      }
      if(!left && ! right){
         movementAnimation = null;
      }

      if(nextAnimation != movementAnimation && nextAnimation != null){
         movementAnimation = nextAnimation;
         movementAnimation.stop();
         movementAnimation.start();
      }
      if(movementAnimation != null)
         movementAnimation.update();

      idleAnimation.update();

      for (int i = projectiles.size() - 1; i >= 0; i--) {
         projectiles.get(i).update();
         if (!projectiles.get(i).isActive()) {
            projectiles.remove(i);
         }
      }
	}

   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x+15, y, width-30, height);
   }
   
   public void collidesWithAlien(){
      for(Alien alien : aliens){
      }
   }
}