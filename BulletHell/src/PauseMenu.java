import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PauseMenu extends JPanel {
    private Button resumeButton;
    private Button mainMenuButton;
    private PauseMenuListener listener;
    private static final Color BG_COLOR = new Color(0, 0, 0, 200);
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

    public PauseMenu(int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        resumeButton = new Button(centerX - 75, centerY - 40, 150, 50, "Resume");
        mainMenuButton = new Button(centerX - 75, centerY + 30, 150, 50, "Main Menu");

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
        resumeButton.hovered = resumeButton.contains(x, y);
        mainMenuButton.hovered = mainMenuButton.contains(x, y);
        repaint();
    }

    private void handleClick(int x, int y) {
        if (resumeButton.contains(x, y)) {
            if (listener != null) listener.onResumeClicked();
        } else if (mainMenuButton.contains(x, y)) {
            if (listener != null) listener.onMainMenuClicked();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Semi-transparent background
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw title
        g2d.setColor(TEXT_COLOR);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        String title = "PAUSED";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        g2d.drawString(title, titleX, 100);

        // Draw buttons
        resumeButton.draw(g2d);
        mainMenuButton.draw(g2d);
    }

    public void setListener(PauseMenuListener listener) {
        this.listener = listener;
    }

    public void updateScreenSize(int width, int height) {
        int centerX = width / 2;
        int centerY = height / 2;
        resumeButton.x = centerX - 75;
        resumeButton.y = centerY - 40;
        mainMenuButton.x = centerX - 75;
        mainMenuButton.y = centerY + 30;
        repaint();
    }

    public interface PauseMenuListener {
        void onResumeClicked();
        void onMainMenuClicked();
    }
}
