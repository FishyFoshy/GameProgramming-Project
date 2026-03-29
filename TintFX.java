import java.awt.image.BufferedImage;

public class TintFX implements ImageFX {
	int tint, tintChange;		// to alter the brightness of the image
	String color;

	public TintFX (String color) {
		this.color = color;
		tint = 255;
	}


	private int truncate (int colourValue) {	// keeps colourValue within [0..255] range
		if (colourValue > 255)
			return 255;

		if (colourValue < 0)
			return 0;

		return colourValue;
	}


	private int applyTint (int pixel) {

    	int alpha, red, green, blue;
		int newPixel;
		
		alpha = (pixel >> 24) & 255;
		red = (pixel >> 16) & 255;
		green = (pixel >> 8) & 255;
		blue = pixel & 255;

		if(color == "red")
			red = truncate (red + tint);
		else if(color == "blue")
			blue = truncate (blue + tint);
		else if(color == "green")
			green = truncate (green + tint);
		
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
			pixels[i] = applyTint(pixels[i]);
		}

    	copyImage.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);	
		return copyImage;
	}

	public void update() {
	}

	public String getEffectName(){return "Tint";}
}