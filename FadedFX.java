import java.awt.image.BufferedImage;

public class FadedFX implements ImageFX {
	int alpha;

	public FadedFX () {
		alpha = 100;
	}

	public BufferedImage apply(BufferedImage image) {
		BufferedImage copyImage = ImageManager.copyImage(image);

		int imWidth = copyImage.getWidth();
		int imHeight = copyImage.getHeight();

		int a, red, green, blue, newValue;
    	int [] pixels = new int[imWidth * imHeight];
    	copyImage.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i=0; i<pixels.length; i++) {
			a = (pixels[i] >> 24);
			red = (pixels[i] >> 16) & 255;
			green = (pixels[i] >> 8) & 255;
			blue = pixels[i] & 255;

			if (a != 0) {
				newValue = blue | (green << 8) | (red << 16) | (alpha << 24);
				pixels[i] = newValue;
			}
		}

    	copyImage.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);	
		return copyImage;
	}

	public void update() {
	}

	public String getEffectName(){return "Faded";}
}