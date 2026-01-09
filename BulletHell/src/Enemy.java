import java.util.ArrayList;

public class Enemy {
    protected int x, y;
    protected int hp = 1;
    protected int shootCooldown;
    protected int verticalDirection;
    protected static final int MOVE_SPEED = 1;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.shootCooldown = 20 + (int)(Math.random() * 30);
        this.verticalDirection = Math.random() < 0.5 ? -1 : 1;
    }

    public static Enemy spawnRandom(int width, int height, int wave) {
        int[] edges = {0, 1, 2, 3};
        int edge = edges[(int)(Math.random() * 4)];
        int x, y;

        switch (edge) {
            case 0: x = (int)(Math.random() * width); y = -20; break;
            case 1: x = (int)(Math.random() * width); y = height + 20; break;
            case 2: x = 50 + (int)(Math.random() * 100); y = (int)(Math.random() * height); break;
            default: x = width - 50 - (int)(Math.random() * 100); y = (int)(Math.random() * height); break;
        }

        Enemy enemy = new Enemy(x, y);
        if (wave > 2 && Math.random() < 0.3) {
            enemy.hp = 2;
        }
        return enemy;
    }

    public void update(int width, int height, int wave, boolean isSlowed) {
        // Move up and down (slower if affected by slow field)
        int moveAmount = isSlowed ? 0 : MOVE_SPEED;
        y += moveAmount * verticalDirection;
        
        // Change direction at boundaries
        if (y < 50) verticalDirection = 1;
        else if (y > height - 50) verticalDirection = -1;
        
        if (shootCooldown > 0) shootCooldown--;
    }
    
    public void update(int width, int height, int wave) {
        update(width, height, wave, false);
    }

    public void spawnProjectiles(ArrayList<EnemyProjectile> projectiles, int playerX, int playerY, int wave) {
        if (shootCooldown <= 0) {
            double angle = Math.atan2(playerY - y, playerX - x);
            projectiles.add(new EnemyProjectile(x, y, angle));
            shootCooldown = 20 + (int)(Math.random() * 30);
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean collidesWith(int px, int py) {
        return Math.abs(x - px) < 18 && Math.abs(y - py) < 18;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
