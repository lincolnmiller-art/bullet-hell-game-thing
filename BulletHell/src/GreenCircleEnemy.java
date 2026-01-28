import java.awt.*;
import java.util.ArrayList;

public class GreenCircleEnemy {
    private int x, y;
    private int hp = 6;
    private int screenWidth, screenHeight;
    private int reviveTimer = 0;
    private int reviveInterval = 600; // every 10 seconds
    private int warningDuration = 90; // 1.5 seconds warning
    private int pendingReviveCount = 0;
    private int pulseTimer = 0;

    public GreenCircleEnemy(int x, int y, int screenWidth, int screenHeight) {
        this.x = x;
        this.y = y;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.pendingReviveCount = 0;
        this.reviveTimer = (int)(Math.random() * reviveInterval / 2);
    }

    public void update(int width, int height) {
        update(width, height, false);
    }

    public void update(int width, int height, boolean isSlowed) {
        reviveTimer++;
        pulseTimer++;
        if (pulseTimer > 120) pulseTimer = 0;

        if (reviveTimer >= reviveInterval - warningDuration && reviveTimer < reviveInterval) {
            // warning pulse visual only
        }

        if (reviveTimer >= reviveInterval) {
            // Time to request revives: request a count (GamePanel will revive exact recent deaths)
            int count = 1 + (int)(Math.random() * 3);
            pendingReviveCount = count;
            reviveTimer = 0;
        }
    }

    // GamePanel will call this each tick to collect and clear requests
    // GamePanel will call this each tick to get how many revives are requested
    public int collectReviveRequests() {
        int out = pendingReviveCount;
        pendingReviveCount = 0;
        return out;
    }

    public void takeDamage(int d) { hp -= d; }
    public boolean isDead() { return hp <= 0; }
    public boolean collidesWith(int px, int py) { return Math.abs(x - px) < 28 && Math.abs(y - py) < 28; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void draw(Graphics2D g) {
        // Draw green circle
        g.setColor(new Color(60, 180, 80));
        g.fillOval(x - 22, y - 22, 44, 44);
        g.setColor(new Color(160, 255, 160));
        g.setStroke(new BasicStroke(2));
        g.drawOval(x - 22, y - 22, 44, 44);

        // Revive warning pulsing ring
        if (reviveTimer >= reviveInterval - warningDuration) {
            float t = (float)(reviveInterval - reviveTimer) / (float)warningDuration;
            float prog = 1f - Math.max(0f, Math.min(1f, t));
            int size = 60 + (int)(prog * 40);
            float alpha = 0.6f * prog;
            g.setColor(new Color(0f, 1f, 0f, alpha));
            g.fillOval(x - size/2, y - size/2, size, size);
            g.setColor(new Color(0f, 1f, 0f, Math.min(0.9f, alpha + 0.2f)));
            g.drawOval(x - size/2, y - size/2, size, size);
        }
    }
}
