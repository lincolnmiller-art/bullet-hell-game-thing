import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UpgradeMenu extends JPanel {
    private Upgrade[] upgrades;
    private Button[] buttons;
    private UpgradeMenuListener listener;
    private static final Color BG_COLOR = new Color(0, 0, 0, 220);
    private static final Color BUTTON_COLOR = new Color(150, 100, 200);
    private static final Color BUTTON_HOVER_COLOR = new Color(200, 150, 255);
    private static final Color TEXT_COLOR = Color.WHITE;

    private class Button {
        int x, y, width, height;
        Upgrade upgrade;
        boolean hovered = false;

        Button(int x, int y, int width, int height, Upgrade upgrade) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.upgrade = upgrade;
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
            
            g.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g.getFontMetrics();
            int textX = x + (width - fm.stringWidth(upgrade.getType().getName())) / 2;
            int textY = y + 25;
            g.drawString(upgrade.getType().getName(), textX, textY);
            
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            fm = g.getFontMetrics();
            String desc = upgrade.getType().getDescription();
            int descX = x + (width - fm.stringWidth(desc)) / 2;
            int descY = y + 50;
            g.drawString(desc, descX, descY);
        }
    }

    public UpgradeMenu(Upgrade[] upgrades, int screenWidth, int screenHeight) {
        this.upgrades = upgrades;
        this.buttons = new Button[upgrades.length];
        
        int buttonWidth = 200;
        int buttonHeight = 80;
        int startX = (screenWidth - (buttonWidth * upgrades.length + 20 * (upgrades.length - 1))) / 2;
        int startY = (screenHeight - buttonHeight) / 2;
        
        for (int i = 0; i < upgrades.length; i++) {
            buttons[i] = new Button(startX + i * (buttonWidth + 20), startY, buttonWidth, buttonHeight, upgrades[i]);
        }
        
        setFocusable(true);
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
        for (Button button : buttons) {
            button.hovered = button.contains(x, y);
        }
        repaint();
    }

    private void handleClick(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].contains(x, y)) {
                if (listener != null) {
                    listener.onUpgradeSelected(upgrades[i]);
                }
                break;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(BG_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        String title = "CHOOSE AN UPGRADE";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 80);

        for (Button button : buttons) {
            button.draw(g2d);
        }
    }

    public void setListener(UpgradeMenuListener listener) {
        this.listener = listener;
    }

    public void updateScreenSize(int screenWidth, int screenHeight) {
        int buttonWidth = 200;
        int buttonHeight = 80;
        int startX = (screenWidth - (buttonWidth * upgrades.length + 20 * (upgrades.length - 1))) / 2;
        int startY = (screenHeight - buttonHeight) / 2;
        
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].x = startX + i * (buttonWidth + 20);
            buttons[i].y = startY;
        }
        repaint();
    }

    public interface UpgradeMenuListener {
        void onUpgradeSelected(Upgrade upgrade);
    }
}
