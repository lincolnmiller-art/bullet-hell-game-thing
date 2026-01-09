import java.awt.*;

public class RotatingBeam {
    private int centerX, centerY;
    private double baseAngle; // starting angle
    private double rotationOffset = 0;
    private int screenWidth, screenHeight;
    private static final int BEAM_WIDTH = 80;

    public RotatingBeam(int bossX, int bossY, double angle, int screenWidth, int screenHeight) {
        this.centerX = bossX;
        this.centerY = bossY;
        this.baseAngle = angle;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void setCenter(int x, int y) {
        this.centerX = x;
        this.centerY = y;
    }

    public void setRotation(double offset) {
        this.rotationOffset = offset;
    }

    public void update(int bossX, int bossY, double rotationOffset) {
        setCenter(bossX, bossY);
        setRotation(rotationOffset);
    }

    public void setScreenSize(int w, int h) {
        this.screenWidth = w;
        this.screenHeight = h;
    }

    public void draw(Graphics2D g) {
        double finalAngle = baseAngle + rotationOffset;
        java.awt.geom.AffineTransform old = g.getTransform();
        g.translate(centerX, centerY);
        g.rotate(finalAngle);

        int beamLength = (int) Math.sqrt(screenWidth * screenWidth + screenHeight * screenHeight);

        // Glow
        g.setColor(new Color(1f, 1f, 0.6f, 0.6f));
        g.fillRect(0, -BEAM_WIDTH / 2, beamLength, BEAM_WIDTH);

        // Core
        g.setColor(new Color(1f, 1f, 0.2f, 1f));
        g.fillRect(0, -BEAM_WIDTH / 4, beamLength, BEAM_WIDTH / 2);

        g.setTransform(old);
    }

    public boolean checkCollision(int px, int py) {
        double dx = px - centerX;
        double dy = py - centerY;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double angle = Math.atan2(dy, dx);
        double finalAngle = baseAngle + rotationOffset;

        double angleDiff = angle - finalAngle;
        while (angleDiff > Math.PI) angleDiff -= 2 * Math.PI;
        while (angleDiff < -Math.PI) angleDiff += 2 * Math.PI;

        int beamLength = (int) Math.sqrt(screenWidth * screenWidth + screenHeight * screenHeight);
        return Math.abs(angleDiff) < 0.5 && dist > 20 && dist < beamLength;
    }
}
