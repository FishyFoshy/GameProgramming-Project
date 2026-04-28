package Screens;
import ImageManip.ImageFX;
import ImageManip.ImageManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BackgroundManager {
    private final List<ParallaxLayer> layers = new ArrayList<>();

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public BackgroundManager() {
        BufferedImage layer1Image, layer2Image, layer3Image;

        layer1Image = ImageManager.loadBufferedImage ("images/Space.png");
        layer2Image = ImageManager.loadBufferedImage ("images/Planets.png");
        layer3Image = ImageManager.loadBufferedImage ("images/Stars.png");

	    addLayer(new ParallaxLayer(layer1Image, 0.20f, -1600));
        addLayer(new ParallaxLayer(layer2Image, 0.50f, -4000));
	    addLayer(new ParallaxLayer(layer3Image, 0.80f, -1600));
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
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRect(0, 0, 600, 800);
            }
        }
    }
}