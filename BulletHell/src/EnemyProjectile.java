import java.awt.Color;

public class EnemyProjectile {
    private int x, y;
    private double vx, vy;
    private int size;
    private Color color;
    private int damage;
    private static final int SPEED = 4;

    public EnemyProjectile(int x, int y, double angle) {
        this(x, y, angle, 4, Color.YELLOW, 1);
    }

    public EnemyProjectile(int x, int y, double angle, int size, Color color) {
        this(x, y, angle, size, color, 1);
    }

    public EnemyProjectile(int x, int y, double angle, int size, Color color, int damage) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.damage = damage;
        this.vx = Math.cos(angle) * SPEED;
        this.vy = Math.sin(angle) * SPEED;
    }

    public void update() {
        x += vx;
        y += vy;
    }

    public boolean isOutOfBounds(int width, int height) {
        return x < -10 || x > width + 10 || y < -10 || y > height + 10;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSize() { return size; }
    public Color getColor() { return color; }
    public int getDamage() { return damage; }
}
