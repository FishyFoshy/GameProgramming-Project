import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Ship {
   private JPanel panel;
   private int centreX;
   private int centreY;
   private int width;
   private int height;
   private int worldX, x;
   private int worldY, y;
   private boolean left, right, isP2;

   private int dx, dy;

   private Dimension dimension;

   private SoundManager soundManager;
   private Background background;
   private Animation movementAnimation, rightAnimation, leftAnimation, nextAnimation, idleAnimation;

   private ArrayList<Alien> aliens;
   private ArrayList<SolidObject> objects;
   private ImageFX gray;

   public Ship (JPanel p, Background bg, ArrayList<SolidObject> s, ArrayList<Alien> a, boolean isP2) {
      panel = p;
      dimension = panel.getSize();
      background = bg;

      this.isP2 = isP2;

      gray = new GrayScaleFX();

      width = 80;
      height = 65;

      objects = s;
      aliens = a;

      centreX = dimension.width/2 - width/2;
      centreY = dimension.height/2 - height/2;

      x = dimension.width/2 - width/2;
      y = dimension.height - height - 20;

      worldX = 0;
      worldY = 0;

      left = right = false;

      dx = 10;
      dy = 10;

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


   public void draw (Graphics2D g2) {
      if (idleAnimation != null) {
         g2.drawImage(idleAnimation.getImage(), x, y, width, height, null);
      }
      if(movementAnimation != null){
         g2.drawImage(movementAnimation.getImage(), x, y, width, height, null);
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
	}

   public Rectangle2D.Double getBoundingRectangle() {
      // Convert current screen position (x,y) into world coordinates
      int bgx = 0;
      int bgy = 0;
      if (background != null) {
         bgx = background.getX();
         bgy = background.getY();
      }
      int wx = x - bgx + 15;
      int wy = y - bgy;
      return new Rectangle2D.Double (wx+15, wy, width-30, height);
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
   public int getWorldX(){
      int bgx = 0;
      if (background != null) {
         bgx = background.getX();
      }
      return x - bgx;
   }
   public int getWorldY(){
      int bgy = 0;
      if (background != null) {
         bgy = background.getY();
      }
      return y - bgy;
   }
}