import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class MainMenu extends JPanel {
    private Achievements achievements;
    private MainMenuListener listener;
    private Button playButton;
    private Button achievementsButton;
    private Button quitButton;
    private boolean showingAchievements = false;
    private boolean showDebugMenu = false;
    private String debugInput = "";
    private static final Color BG_COLOR = new Color(20, 20, 40);
    private static final Color BUTTON_COLOR = new Color(100, 50, 150);
    private static final Color BUTTON_HOVER_COLOR = new Color(150, 80, 200);
    private static final Color TEXT_COLOR = Color.WHITE;

    private class Button {
        int x, y, width, height;
        String text;
        boolean hovered = false;

        Button(int x, int y, int width, int height, String text) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.text = text;
        }

        boolean contains(int px, int py) {
            return px >= x && px < x + width && py >= y && py < y + height;
        }

        void draw(Graphics2D g) {
            g.setColor(hovered ? BUTTON_HOVER_COLOR : BUTTON_COLOR);
            g.fillRect(x, y, width, height);
            g.setColor(TEXT_COLOR);
            g.setStroke(new BasicStroke(2));
            g.drawRect(x, y, width, height);
            
            g.setFont(new Font("Arial", Font.BOLD, 18));
            FontMetrics fm = g.getFontMetrics();
            int textX = x + (width - fm.stringWidth(text)) / 2;
            int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();
            g.drawString(text, textX, textY);
        }
    }

    public MainMenu(Achievements achievements) {
        this.achievements = achievements;
        setBackground(BG_COLOR);
        setFocusable(true);

        int centerX = 400;
        int centerY = 300;
        playButton = new Button(centerX - 75, centerY - 40, 150, 50, "Play Game");
        achievementsButton = new Button(centerX - 75, centerY + 30, 150, 50, "Achievements");
        quitButton = new Button(centerX - 75, centerY + 100, 150, 50, "Quit");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateHover(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateHover(e.getX(), e.getY());
            }
        });
    }

    private void updateHover(int x, int y) {
        playButton.hovered = playButton.contains(x, y);
        achievementsButton.hovered = achievementsButton.contains(x, y);
        quitButton.hovered = quitButton.contains(x, y);
        repaint();
    }

    private void handleClick(int x, int y) {
        if (showDebugMenu) {
            // Click outside debug menu to close it
            showDebugMenu = false;
            debugInput = "";
            repaint();
        } else if (showingAchievements) {
            // Back button or click anywhere to return
            showingAchievements = false;
            repaint();
        } else {
            if (playButton.contains(x, y)) {
                if (listener != null) listener.onPlayClicked();
            } else if (achievementsButton.contains(x, y)) {
                showingAchievements = true;
                repaint();
            } else if (quitButton.contains(x, y)) {
                System.exit(0);
            }
            // Check if clicked in debug menu area (lower right)
            if (x > getWidth() - 150 && y > getHeight() - 100) {
                showDebugMenu = true;
                debugInput = "";
                repaint();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (showingAchievements) {
            drawAchievementsScreen(g2d);
        } else {
            drawMainMenu(g2d);
        }

        // Always draw debug menu if active (overlay)
        if (showDebugMenu) {
            drawDebugMenu(g2d);
        }
    }

    private void drawDebugMenu(Graphics2D g) {
        // Semi-transparent background
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Debug panel in lower right
        int panelX = getWidth() - 300;
        int panelY = getHeight() - 150;
        int panelW = 280;
        int panelH = 130;

        g.setColor(new Color(50, 50, 100));
        g.fillRect(panelX, panelY, panelW, panelH);
        g.setColor(Color.CYAN);
        g.setStroke(new BasicStroke(2));
        g.drawRect(panelX, panelY, panelW, panelH);

        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("DEBUG MENU", panelX + 15, panelY + 30);

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("Enter wave number:", panelX + 15, panelY + 60);

        // Input field
        g.setColor(Color.WHITE);
        g.fillRect(panelX + 15, panelY + 70, 200, 30);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g.drawString(debugInput + (System.currentTimeMillis() % 1000 < 500 ? "|" : ""), panelX + 25, panelY + 93);

        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Press ENTER to go | ESC to cancel", panelX + 15, panelY + 120);
    }

    private void drawMainMenu(Graphics2D g) {
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw title
        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        String title = "BULLET HELL";
        FontMetrics fm = g.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, 100);

        // Draw buttons
        playButton.draw(g);
        achievementsButton.draw(g);
        quitButton.draw(g);

        // Draw achievement count
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        String achievementText = "Achievements: " + achievements.getUnlockedCount() + "/" + achievements.getTotalCount();
        g.drawString(achievementText, 20, getHeight() - 20);

        // Draw debug menu hint in lower right
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.setColor(new Color(100, 100, 150));
        g.drawString("[DEBUG]", getWidth() - 100, getHeight() - 20);
    }

    private void drawAchievementsScreen(Graphics2D g) {
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("ACHIEVEMENTS", 50, 50);

        g.setFont(new Font("Arial", Font.PLAIN, 14));
        int y = 100;
        int index = 0;

        for (Map.Entry<String, Boolean> entry : achievements.getAllAchievements().entrySet()) {
            String achievementName = entry.getKey();
            boolean unlocked = entry.getValue();

            if (unlocked) {
                g.setColor(new Color(200, 200, 100)); // Gold for unlocked
            } else {
                g.setColor(new Color(100, 100, 100)); // Gray for locked
            }

            String status = unlocked ? "✓ " : "✗ ";
            g.drawString(status + achievementName, 100, y);

            y += 30;
            if (y > getHeight() - 80) {
                y = 100;
                index++;
            }
        }

        // Draw back button
        g.setColor(BUTTON_COLOR);
        g.fillRect(20, getHeight() - 60, 100, 50);
        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Back", 45, getHeight() - 25);
    }

    public void setListener(MainMenuListener listener) {
        this.listener = listener;
    }

    public void handleKeyPress(int keyCode, char keyChar) {
        if (showDebugMenu) {
            if (keyCode == KeyEvent.VK_ENTER) {
                // Try to jump to wave
                try {
                    int wave = Integer.parseInt(debugInput);
                    if (wave > 0) {
                        if (listener != null) listener.onDebugWaveSelected(wave);
                        showDebugMenu = false;
                        debugInput = "";
                    }
                } catch (NumberFormatException e) {
                    // Invalid input, just clear it
                }
                repaint();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                // Cancel debug menu
                showDebugMenu = false;
                debugInput = "";
                repaint();
            } else if (keyCode == KeyEvent.VK_BACK_SPACE && debugInput.length() > 0) {
                debugInput = debugInput.substring(0, debugInput.length() - 1);
                repaint();
            } else if (Character.isDigit(keyChar)) {
                debugInput += keyChar;
                repaint();
            }
        }
    }

    public interface MainMenuListener {
        void onPlayClicked();
        void onDebugWaveSelected(int wave);
    }
}
