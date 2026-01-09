import java.awt.*;
import java.util.ArrayList;

public class TriangleEnemy {
    private int x, y;
    private int hp = 1;
    private int shootCooldown = 30;
    private int velocityX;
    private int velocityY;
    private static final int MOVE_SPEED = 1;

    public TriangleEnemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.velocityX = (int)((Math.random() - 0.5) * 2);
        this.velocityY = 1;
    }

    public void update(int width, int height) {
        update(width, height, false);
    }

    public void update(int width, int height, boolean isSlowed) {
        // Move with velocity
        if (!isSlowed) {
            x += velocityX;
            y += velocityY;
        }
        
        // Bounce off walls
        if (x < 20) {
            x = 20;
            velocityX = Math.abs(velocityX);
        }
        if (x > width - 20) {
            x = width - 20;
            velocityX = -Math.abs(velocityX);
        }
        if (y < 20) {
            y = 20;
            velocityY = Math.abs(velocityY);
        }
        if (y > height - 20) {
            y = height - 20;
            velocityY = -Math.abs(velocityY);
        }

        if (shootCooldown > 0) shootCooldown--;
    }

    public void spawnProjectiles(ArrayList<EnemyProjectile> projectiles, int playerX, int playerY) {
        if (shootCooldown <= 0) {
            // Shotgun pattern: 3 bullets spread
            double baseAngle = Math.atan2(playerY - y, playerX - x);
            
            for (int i = -1; i <= 1; i++) {
                double angle = baseAngle + (i * 0.35);
                projectiles.add(new EnemyProjectile(x, y, angle));
            }
            
            shootCooldown = 60;
        }
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public boolean collidesWith(int px, int py) {
        return Math.abs(x - px) < 15 && Math.abs(y - py) < 15;
    }

    public void draw(Graphics2D g) {
        var sprite = SpriteLoader.getSprite("triangle_enemy");
        if (sprite != null) {
            g.drawImage(sprite, x - 20, y - 20, 40, 40, null);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
