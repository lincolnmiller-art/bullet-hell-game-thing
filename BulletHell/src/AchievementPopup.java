import java.awt.*;

public class AchievementPopup {
    private String achievement;
    private int x, y;
    private int lifetime = 180; // 3 seconds at 60 FPS
    private int timer = 0;
    private static final int WIDTH = 250;
    private static final int HEIGHT = 60;

    public AchievementPopup(String achievement, int screenWidth) {
        this.achievement = achievement;
        this.x = screenWidth - WIDTH - 20;
        this.y = 20;
    }

    public void update() {
        timer++;
    }

    public boolean isFinished() {
        return timer >= lifetime;
    }

    public void draw(Graphics2D g) {
        float alpha = 1.0f;
        if (timer > lifetime - 60) {
            // Fade out in last second
            alpha = (lifetime - timer) / 60.0f;
        }

        // Draw semi-transparent background
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.9f));
        g.setColor(new Color(50, 150, 100));
        g.fillRoundRect(x, y, WIDTH, HEIGHT, 10, 10);

        // Draw border
        g.setColor(new Color(100, 255, 150));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(x, y, WIDTH, HEIGHT, 10, 10);

        // Draw text
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();

        String text1 = "Achievement Unlocked!";
        String text2 = achievement;

        int x1 = x + (WIDTH - fm.stringWidth(text1)) / 2;
        g.drawString(text1, x1, y + 20);

        g.setFont(new Font("Arial", Font.PLAIN, 11));
        fm = g.getFontMetrics();
        int x2 = x + (WIDTH - fm.stringWidth(text2)) / 2;
        g.drawString(text2, x2, y + 40);

        // Reset composite
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}
