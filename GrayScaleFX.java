import java.awt.image.BufferedImage;

public class GrayScaleFX implements ImageFX {
	public GrayScaleFX () {
	}

	private int toGray (int pixel) {
  		int alpha, red, green, blue, gray;
		int newPixel;

		alpha = (pixel >> 24) & 255;
		red = (pixel >> 16) & 255;
		green = (pixel >> 8) & 255;
		blue = pixel & 255;
		gray = (red + green + blue) / 3;
		red = green = blue = gray;

		newPixel = blue | (green << 8) | (red << 16) | (alpha << 24);

		return newPixel;
	}

	public BufferedImage apply(BufferedImage image) {
		BufferedImage copyImage = ImageManager.copyImage(image);

		int imWidth = copyImage.getWidth();
		int imHeight = copyImage.getHeight();

		int [] pixels = new int[imWidth * imHeight];
		copyImage.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

		for (int i=0; i<pixels.length; i++) {
			pixels[i] = toGray(pixels[i]);
		}
  
		copyImage.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
		return copyImage;
	}

	public void update(){}

	public String getEffectName(){return "GrayScale";}
}