import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame {
    private GamePanel gamePanel;
    private MainMenu mainMenu;
    private Achievements achievements;
    private boolean isFullscreen = false;
    private GraphicsDevice gd;
    private JPanel currentPanel;

    public Window() {
        setTitle("Bullet Hell");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        achievements = new Achievements();
        mainMenu = new MainMenu(achievements);
        mainMenu.setListener(new MainMenu.MainMenuListener() {
            @Override
            public void onPlayClicked() {
                startGame();
            }

            @Override
            public void onDebugWaveSelected(int wave) {
                startGameAtWave(wave);
            }
        });
        
        currentPanel = mainMenu;
        add(mainMenu);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11) {
                    toggleFullscreen();
                }
                if (currentPanel instanceof GamePanel) {
                    ((GamePanel) currentPanel).keyPressed(e);
                } else if (currentPanel instanceof MainMenu) {
                    mainMenu.handleKeyPress(e.getKeyCode(), e.getKeyChar());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (currentPanel instanceof GamePanel) {
                    ((GamePanel) currentPanel).keyReleased(e);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        setFocusable(true);
    }

    private void startGame() {
        remove(currentPanel);
        gamePanel = new GamePanel(achievements);
        gamePanel.setGamePanelListener(new GamePanel.GamePanelListener() {
            @Override
            public void onReturnToMenu() {
                returnToMenu();
            }
        });
        currentPanel = gamePanel;
        add(gamePanel);
        setVisible(true);
        gamePanel.requestFocus();
        toggleFullscreen();
    }

    private void startGameAtWave(int wave) {
        remove(currentPanel);
        gamePanel = new GamePanel(achievements, wave);
        gamePanel.setGamePanelListener(new GamePanel.GamePanelListener() {
            @Override
            public void onReturnToMenu() {
                returnToMenu();
            }
        });
        currentPanel = gamePanel;
        add(gamePanel);
        setVisible(true);
        gamePanel.requestFocus();
        toggleFullscreen();
    }

    private void returnToMenu() {
        gd.setFullScreenWindow(null);
        isFullscreen = false;
        remove(currentPanel);
        currentPanel = mainMenu;
        add(mainMenu);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        mainMenu.requestFocus();
    }

    private void toggleFullscreen() {
        if (!isFullscreen) {
            dispose();
            setUndecorated(true);
            gd.setFullScreenWindow(this);
            isFullscreen = true;
        } else {
            gd.setFullScreenWindow(null);
            dispose();
            setUndecorated(false);
            setVisible(true);
            isFullscreen = false;
        }
        currentPanel.requestFocus();
    }

    public static void main(String[] args) {
        // Generate sprites if they don't exist
        SpriteGenerator.generateAllSprites();
        SwingUtilities.invokeLater(() -> new Window());
    }
}
