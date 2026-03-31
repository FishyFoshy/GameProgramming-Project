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
   private boolean left, right, down, up;

   private int dx, dy;

   private Dimension dimension;

   private SoundManager soundManager;
   private Background background;
   private Animation movementAnimation, rightAnimation, leftAnimation, nextAnimation, idleAnimation;

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

      x = dimension.width/2 - width/2;
      y = dimension.height - height - 20;

      worldX = 0;
      worldY = 0;

      left = right = down = up = false;

      dx = 16;
      dy = 16;

      soundManager = SoundManager.getInstance();

      idleAnimation = new Animation(true, "images/shipIdleP1.png",3, 2, 100);
      rightAnimation = new Animation(true, "images/rightP1.png", 3, 2, 100);
      leftAnimation = new Animation(true, "images/leftP1.png", 3, 2, 100);

      movementAnimation = null;
      idleAnimation.start();
   }

   // constructor for player 2
   public Ship (JPanel p, Background bg, ArrayList<SolidObject> s, ArrayList<Alien> a, boolean isP2) {
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

      x = dimension.width/2 - width/2 + 80;
      y = dimension.height - height - 20;

      worldX = 0;
      worldY = 0;

      left = right = down = up = false;

      dx = 16;
      dy = 16;

      soundManager = SoundManager.getInstance();

      idleAnimation = new Animation(true, "images/shipIdleP2.png", 3, 2, 100);
      rightAnimation = new Animation(true, "images/rightP2.png", 3, 2, 100);
      leftAnimation = new Animation(true, "images/leftP2.png", 3, 2, 100);

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
      if(!left && ! right){
         movementAnimation = null;
      }
      if (movementAnimation != null) {
         if(nextAnimation != movementAnimation && nextAnimation != null){
            movementAnimation = nextAnimation;
            movementAnimation.stop();
            movementAnimation.start();
         }
         movementAnimation.update();
      }
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
      int wx = x - bgx;
      int wy = y - bgy;
      return new Rectangle2D.Double (wx, wy, width, height);
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