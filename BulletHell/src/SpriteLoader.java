import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class SpriteLoader {
    private static final Map<String, BufferedImage> sprites = new HashMap<>();
    private static boolean loaded = false;

    public static void loadSprites() {
        if (loaded) return;
        
        try {
            sprites.put("player", ImageIO.read(new File("sprites/player.png")));
            sprites.put("enemy", ImageIO.read(new File("sprites/enemy.png")));
            sprites.put("triangle_enemy", ImageIO.read(new File("sprites/triangle_enemy.png")));
            sprites.put("green_triangle_enemy", ImageIO.read(new File("sprites/green_triangle_enemy.png")));
            sprites.put("purple_triangle", ImageIO.read(new File("sprites/purple_triangle_enemy.png")));
            sprites.put("boss", ImageIO.read(new File("sprites/boss.png")));
            sprites.put("player_projectile", ImageIO.read(new File("sprites/player_projectile.png")));
            sprites.put("enemy_projectile", ImageIO.read(new File("sprites/enemy_projectile.png")));
            sprites.put("healing_item", ImageIO.read(new File("sprites/healing_item.png")));
            loaded = true;
        } catch (IOException e) {
            System.err.println("Error loading sprites: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static BufferedImage getSprite(String name) {
        return sprites.get(name);
    }

    public static boolean isLoaded() {
        return loaded;
    }
}
