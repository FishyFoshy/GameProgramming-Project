import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Ship {
   private JPanel panel;
   private static int centreX;
   private static int centreY;
   private int width;
   private int height;
   private int worldX, x;
   private int worldY, y;
   private boolean left, right, down, up;

   private int dx, dy;

   private Dimension dimension;

   private SoundManager soundManager;
   private Background background;
   private Animation currentAnimation, upAnimation, downAnimation, rightAnimation, leftAnimation, nextAnimation;

   private ArrayList<Alien> aliens;
   private ArrayList<SolidObject> objects;
   private ImageFX gray;

   public Ship (JPanel p, Background bg, ArrayList<SolidObject> s, ArrayList<Alien> a) {
      panel = p;
      dimension = panel.getSize();
      background = bg;

      gray = new GrayScaleFX();

      width = 64;
      height = 64;

      objects = s;
      aliens = a;

      centreX = dimension.width/2 - width/2;
      centreY = dimension.height/2 - height/2;
      worldX = x = 0;
      worldY = y = 0;

      left = right = down = up = false;

      dx = 16;
      dy = 16;

      soundManager = SoundManager.getInstance();

      upAnimation = new Animation(true, "images/shipUp.png", 1, 6, 100);
      downAnimation = new Animation(true, "images/shipDown.png", 1, 6, 100);
      rightAnimation = new Animation(true, "images/shipRight.png", 1, 6, 100);
      leftAnimation = new Animation(true, "images/shipLeft.png", 1, 6, 100);

      currentAnimation = rightAnimation;
      currentAnimation.start();
   }


   public void draw (Graphics2D g2) {
      if (currentAnimation != null) {
         g2.drawImage(currentAnimation.getImage(), x, y, width, height, null);
      } 
   }

   public void setMoveDirection(int direction){
      if(direction == 1) left = true;
      if(direction == 2) right = true;
      if(direction == 3) up = true;
      if(direction == 4) down = true;

      if(direction == -1) left = false;
      if(direction == -2) right = false;
      if(direction == -3) up = false;
      if(direction == -4) down = false;
    }

   public void move () {
      if (!panel.isVisible ()) return;
      
      dimension = panel.getSize();

      if (left) {	// move left
         x = x - dx;
         if (x < 0)
            x = 0;
      }

      if (right) {  	// move right
         x = x + dx;
         if (x + width > dimension.width)
            x = dimension.width - width;
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
      if(down){
         nextAnimation = downAnimation;
      }
      if(up){
         nextAnimation = upAnimation;
      }
      if (currentAnimation != null) {
         if(nextAnimation != currentAnimation && nextAnimation != null){
            currentAnimation = nextAnimation;
            currentAnimation.stop();
            currentAnimation.start();
         }
         currentAnimation.update();
      }
	}

   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (worldX, worldY, width, height);
   }

   public SolidObject collidesWithSolid(){
      for(SolidObject solidObject : objects){
         if(getBoundingRectangle().intersects(solidObject.getBoundingRectangle())){
            return solidObject;
         }
      }
      return null;
   }
   
   public void collidesWithAlien(){
      for(Alien alien : aliens){
         if(!alien.isCollected()){
            if(getBoundingRectangle().intersects(alien.getBoundingRectangle())){
               alien.collected();
               alien.setEffect(gray);
               soundManager.playClip("collect", false);
            }
         }
      }
   }
   public int getWorldX(){return worldX;}
   public int getWorldY(){return worldY;}
}