import java.util.List;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class BackgroundManager {
    private List<ParallaxLayer> layers = new ArrayList<>();

    public BackgroundManager() {
        BufferedImage layer1Image, layer2Image, layer3Image;

        layer1Image = ImageManager.loadBufferedImage ("images/Space.png");
        layer2Image = ImageManager.loadBufferedImage ("images/Planets.png");
        layer3Image = ImageManager.loadBufferedImage ("images/Stars.png");

	    addLayer(new ParallaxLayer(layer1Image, 0.20f));
        addLayer(new ParallaxLayer(layer2Image, 0.50f));
	    addLayer(new ParallaxLayer(layer3Image, 0.80f));
    }

    public void addLayer(ParallaxLayer layer) {
        layers.add(layer);
    }

    public void draw(Graphics2D g2, boolean isPaused) {
 
        if (isPaused) {
            for (ParallaxLayer layer : layers) {
                layer.draw (g2, 0);
            }
        } else{
            for (ParallaxLayer layer : layers) {
                layer.draw (g2, 4);
            }
        }
    }
}