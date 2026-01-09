import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteGenerator {
    private static final String SPRITES_DIR = "sprites";

    public static void generateAllSprites() {
        new File(SPRITES_DIR).mkdirs();
        
        try {
            generatePlayerSprite();
            generateEnemySprite();
            generateTriangleEnemySprite();
            generateGreenTriangleEnemySprite();
            generatePurpleTriangleEnemySprite();
            generateBossSprite();
            generatePlayerProjectileSprite();
            generateEnemyProjectileSprite();
            generateHealingItemSprite();
            System.out.println("Sprites generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatePlayerSprite() throws IOException {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Blue square with white border
        g.setColor(Color.BLUE);
        g.fillRect(5, 5, 30, 30);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawRect(5, 5, 30, 30);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/player.png"));
    }

    private static void generateEnemySprite() throws IOException {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        
        // Red square
        g.setColor(Color.RED);
        g.fillRect(5, 5, 30, 30);
        g.setColor(new Color(139, 0, 0));
        g.setStroke(new BasicStroke(1));
        g.drawRect(5, 5, 30, 30);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/enemy.png"));
    }

    private static void generateTriangleEnemySprite() throws IOException {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Cyan triangle pointing down
        int[] xPoints = {20, 8, 32};
        int[] yPoints = {8, 32, 32};
        g.setColor(Color.CYAN);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(1));
        g.drawPolygon(xPoints, yPoints, 3);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/triangle_enemy.png"));
    }

    private static void generateGreenTriangleEnemySprite() throws IOException {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Green triangle pointing down
        int[] xPoints = {20, 8, 32};
        int[] yPoints = {8, 32, 32};
        g.setColor(Color.GREEN);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(new Color(0, 100, 0));
        g.setStroke(new BasicStroke(1));
        g.drawPolygon(xPoints, yPoints, 3);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/green_triangle_enemy.png"));
    }

    private static void generatePurpleTriangleEnemySprite() throws IOException {
        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Purple triangle pointing down
        int[] xPoints = {20, 8, 32};
        int[] yPoints = {8, 32, 32};
        g.setColor(new Color(180, 100, 200));
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(new Color(100, 50, 150));
        g.setStroke(new BasicStroke(1));
        g.drawPolygon(xPoints, yPoints, 3);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/purple_triangle_enemy.png"));
    }

    private static void generateBossSprite() throws IOException {
        BufferedImage img = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        
        // Red square
        g.setColor(Color.RED);
        g.fillRect(10, 10, 100, 100);
        g.setColor(new Color(139, 0, 0));
        g.setStroke(new BasicStroke(2));
        g.drawRect(10, 10, 100, 100);
        
        // Add some pattern
        g.setColor(new Color(200, 0, 0));
        g.fillRect(25, 25, 20, 20);
        g.fillRect(75, 25, 20, 20);
        g.fillRect(50, 70, 20, 20);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/boss.png"));
    }

    private static void generatePlayerProjectileSprite() throws IOException {
        BufferedImage img = new BufferedImage(12, 12, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Cyan circle
        g.setColor(Color.CYAN);
        g.fillOval(2, 2, 8, 8);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1));
        g.drawOval(2, 2, 8, 8);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/player_projectile.png"));
    }

    private static void generateEnemyProjectileSprite() throws IOException {
        BufferedImage img = new BufferedImage(12, 12, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Red circle
        g.setColor(Color.RED);
        g.fillOval(2, 2, 8, 8);
        g.setColor(new Color(139, 0, 0));
        g.setStroke(new BasicStroke(1));
        g.drawOval(2, 2, 8, 8);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/enemy_projectile.png"));
    }

    private static void generateHealingItemSprite() throws IOException {
        BufferedImage img = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Green circle with white cross
        g.setColor(Color.GREEN);
        g.fillOval(4, 4, 16, 16);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));
        g.drawOval(4, 4, 16, 16);
        g.drawLine(12, 8, 12, 16);
        g.drawLine(8, 12, 16, 12);
        
        g.dispose();
        ImageIO.write(img, "PNG", new File(SPRITES_DIR + "/healing_item.png"));
    }

    public static void main(String[] args) {
        generateAllSprites();
    }
}
