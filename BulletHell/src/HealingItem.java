import java.awt.*;

public class HealingItem {
    private int x, y;

    public HealingItem(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g) {
        var sprite = SpriteLoader.getSprite("healing_item");
        if (sprite != null) {
            g.drawImage(sprite, x - 12, y - 12, 24, 24, null);
        }
    }

    public boolean collidesWith(int px, int py) {
        return Math.abs(x - px) < 15 && Math.abs(y - py) < 15;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
