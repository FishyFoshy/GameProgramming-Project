package ImageManip;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GlowFX implements ImageFX {
    int glowR, glowG, glowB;
    int intensity;
    double contrast;

    public GlowFX(int r, int g, int b, int intensity) {
        glowR = r;
        glowG = g;
        glowB = b;
        this.intensity = intensity;
        contrast = 1.2;
    }

    private int truncate(int colourValue) {
        if (colourValue > 255)
            return 255;

        if (colourValue < 0)
            return 0;

        return colourValue;
    }

    private int applyGlow(int pixel) {

        int alpha, red, green, blue;
        int newPixel;

        alpha = (pixel >> 24) & 255;
        red = (pixel >> 16) & 255;
        green = (pixel >> 8) & 255;
        blue =  pixel & 255;

        if (alpha == 0) return 0;

        red = truncate((int)(glowR * contrast));
        green = truncate((int)(glowG * contrast));
        blue = truncate((int)(glowB * contrast));

        alpha = (alpha * intensity) / 255;
        newPixel = blue | (green << 8) | (red << 16) | (alpha << 24);

        return newPixel;
    }


    public BufferedImage apply(BufferedImage image) {
        BufferedImage copy = ImageManager.copyImage(image);

        int imWidth  = copy.getWidth();
        int imHeight = copy.getHeight();

        int[] pixels = new int[imWidth * imHeight];
        copy.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = applyGlow(pixels[i]);
        }

        copy.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
        return copy;
    }

    public BufferedImage apply(Image image) {
        BufferedImage bi = new BufferedImage(
                image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return apply(bi);
    }

    public void update() {}

    public String getEffectName() { return "Glow"; }
}
