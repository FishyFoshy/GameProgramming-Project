import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ParallaxLayer {
    private BufferedImage image;	// background image
    private float factor;      		// parallax factor p

    public ParallaxLayer(BufferedImage img, float factor) {
        this.image = img;
        this.factor = factor;
    }

    public void draw(Graphics2D g2, int cameraX, int cameraY) {

        // Compute parallaxed origin on screen

        int originX = Math.round(cameraX * factor);
        int originY = Math.round(cameraY * factor);

        g2.drawImage(image, originX, originY, null);

    }

}