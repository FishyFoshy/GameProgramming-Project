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

   public void move (int direction) {
      int bgx = background.getX();
      int bgy = background.getY();

      if (!panel.isVisible ()) return;
      
      if (direction == 1) {	// move left
         left = true;
         right = down = up = false;
         
         worldX = worldX - dx;
         if (worldX < 0)
            worldX = 0;
         if(x < centreX || x > centreX){
            x -= dx;
         } else if (x == centreX){
            bgx += dx;
            if(bgx > 0){
               x -= dx;
               bgx = 0;
            }
            
         }
         if(x < 0)
            x = 0;
         
         SolidObject solid = collidesWithSolid();
         if(solid != null){
            worldX = solid.getWorldX() + solid.getWidth();
            if(x > solid.getX() && x < solid.getX() + solid.getWidth())
               x = solid.getX() + solid.getWidth();
            if(x == centreX)
               bgx -= dx;
            x = solid.getX() + solid.getWidth();
         }
         
         background.setX(bgx);
      }
      
      if (direction == 2) {  	// move right
         right = true;
         left = down = up = false;
         
         
         worldX = worldX + dx;
         if (worldX > background.getWidth() - width)
            worldX = background.getWidth() - width;
         if(x > centreX || x < centreX){
            x += dx;
         } else if(x == centreX){
            bgx -= dx;
            if(bgx + background.getWidth() < dimension.width){
               x += dx;
               bgx = dimension.width - background.getWidth();
            }
         }
         
         if(x > dimension.width - width)
            x = dimension.width - width;
         
         SolidObject solid = collidesWithSolid();
         if(solid != null){
            worldX = solid.getWorldX() - width;
            if(x > solid.getX() && x < solid.getX() + solid.getWidth())
               x = solid.getX() - width;
            if(x == centreX)
               bgx += dx;
            x = solid.getX() - width;
         }

         background.setX(bgx);
      }
      
      if (direction == 3) {	// move up
         up = true;
         right = down = left = false;
         
         worldY = worldY - dy;
         if (worldY < 0)
            worldY = 0;

         if(y < centreY || y > centreY){
            y -= dy;
         } else if(y == centreY){
            bgy += dy;
            if(bgy > 0){
               y -= dy;
               bgy = 0;
            }
         }
         
         if(y < 0)
            y = 0;

         SolidObject solid = collidesWithSolid();
         if(solid != null){
            worldY = solid.getWorldY() + solid.getHeight();
            if(y > solid.getY() && y < solid.getY() + solid.getHeight())
               y = solid.getY() + solid.getHeight();
            if(y == centreY)
               bgy -= dy;
            y = solid.getY() + solid.getHeight();
         }

         background.setY(bgy);
      }
      
      if (direction == 4) {  	// move down
         down = true;
         right = left = up = false;
         
         worldY = worldY + dy;
         if (worldY > background.getHeight() - height)
            worldY = background.getHeight() - height;

         if(y < centreY || y > centreY){
            y += dy;
         } else if(y == centreY){
            bgy -= dy;
            if(bgy + background.getHeight() < dimension.height){
               y += dy;
               bgy = dimension.height - background.getHeight();
            }
         }
         
         if(y > dimension.height - height)
            y = dimension.height - height;

         SolidObject solid = collidesWithSolid();
         if(solid != null){
            worldY = solid.getWorldY() - height;
            if(y > solid.getY() && y < solid.getY() + solid.getHeight())
               y = solid.getY() - height;
            if(y == centreY)
               bgy += dy;
            y = solid.getY() - height;
         }

         background.setY(bgy);
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