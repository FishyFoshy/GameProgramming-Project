package Screens;
import java.awt.Graphics2D;

import Misc.Animation;

public class Explosion {
    private int x, y;
    private int size;
    private Animation animation;

    public Explosion(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        animation = new Animation(false, "images/explosion.png", 5, 4, 60);
        animation.start();
    }

    public void update() {
        animation.update();
    }

    public void draw(Graphics2D g2) {
        if (animation.isStillActive()) {
            g2.drawImage(animation.getImage(), x, y, size, size, null);
        }
    }

    public boolean isActive() {
        return animation.isStillActive();
    }
}
