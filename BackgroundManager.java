import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class BackgroundManager {
    private JPanel panel;
    private List<ParallaxLayer> layers = new ArrayList<>();
    private int screenW, screenH;
    private int worldW, worldH;

    public BackgroundManager(JPanel panel, int screenW, int screenH) {
	this.panel = panel;
        this.screenW = screenW;
        this.screenH = screenH;

	BufferedImage layer1Image, layer2Image, layer3Image;

	layer1Image = ImageManager.loadBufferedImage ("images/layer1-sky.png");
	layer2Image = ImageManager.loadBufferedImage ("images/layer2-mountain.png");
	layer3Image = ImageManager.loadBufferedImage ("images/layer3-ground.png");

	worldW = layer1Image.getWidth();
	worldH = layer1Image.getHeight();

	addLayer(new ParallaxLayer(layer1Image, 0.00f)); // fixed sky
        addLayer(new ParallaxLayer(layer2Image, 0.50f));
	addLayer(new ParallaxLayer(layer3Image, 1.00f));
    }

    public void addLayer(ParallaxLayer layer) {
        layers.add(layer);
    }

    // Draw back-to-front using the camera position in WORLD space.

    public void draw(Graphics2D g2, int cameraX, int cameraY) {
 
        for (ParallaxLayer layer : layers) {
            layer.draw (g2, cameraX, cameraY);
        }
    }

/*
    private void drawLayer(Graphics2D g2, ParallaxLayer layer, int cameraX, int cameraY) {
        // Compute parallaxed origin on screen
        float p = layer.factor;
        int originX = Math.round(-cameraX * p);
        int originY = Math.round(-cameraY * p);

        if (!layer.repeatX && !layer.repeatY) {
            // Single blit
            g2.drawImage(layer.image, originX, originY, null);
            return;
        }

        // Tiled rendering to fill the viewport
        int iw = layer.image.getWidth();
        int ih = layer.image.getHeight();

        // Align to a seamless tile origin using modulo
        int startX = originX % iw; if (startX > 0) startX -= iw;
        int startY = originY % ih; if (startY > 0) startY -= ih;

        for (int x = startX; x < screenW; x += iw) {
            for (int y = startY; y < screenH; y += ih) {
                // If repeating only in one axis, draw a minimal set
                if (!layer.repeatX && x != startX) continue;
                if (!layer.repeatY && y != startY) continue;

                g2.drawImage(layer.image, x, y, null);
            }
        }
    }
*/
	public int getWidth() {
		return worldW;
	}

	public int getHeight() {
		return worldH;
	}

}