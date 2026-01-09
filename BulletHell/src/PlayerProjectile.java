public class PlayerProjectile {
    private int x, y;
    private double vx, vy;
    private boolean isPiercing;
    private static final int SPEED = 8;

    public PlayerProjectile(int x, int y, double angle) {
        this(x, y, angle, false);
    }

    public PlayerProjectile(int x, int y, double angle, boolean isPiercing) {
        this.x = x;
        this.y = y;
        this.isPiercing = isPiercing;
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

    public boolean isPiercing() { return isPiercing; }
    public int getX() { return x; }
    public int getY() { return y; }
}
