import java.awt.image.BufferedImage;

public interface ImageFX {
	public BufferedImage apply(BufferedImage image);
	public void update();
	public String getEffectName();
}