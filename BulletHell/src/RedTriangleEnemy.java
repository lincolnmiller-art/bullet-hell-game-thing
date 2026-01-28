import java.awt.*;
import java.util.ArrayList;

public class RedTriangleEnemy {
    private int x, y;
    private double posX, posY;
    private int hp = 2;
    private int state = 0; // 0=spinning,1=vanished+warning,2=dashing
    private double spinAngle = 0;
    private int spinTimer = 0;
    private int spinDuration = 60; // spin before vanish
    private int vanishTimer = 0;
    private int vanishDuration = 40; // warning before reappear
    private int respawnX, respawnY;
    private double dashAngle = 0;
    private static final int DASH_SPEED = 18; // very fast
    private double currentSpeed = DASH_SPEED;
    private int bounceCount = 0;
    private static final int MAX_BOUNCES = 2;
    private ArrayList<int[]> trailPositions;
    private static final int TRAIL_MAX = 30;
    private int trailFade = 0;
    // vanish-circle visual
    private int vanishCircleX = -1000, vanishCircleY = -1000;
    private int vanishCircleDuration = 20;
    // death/dying state after dash ends
    private int deathTimer = 0;
    private static final int DEATH_CIRCLE_DURATION = 24;

    public RedTriangleEnemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.posX = x;
        this.posY = y;
        this.trailPositions = new ArrayList<>();
        randomizeTimings();
    }

    private void randomizeTimings() {
        // spinDuration: base 50-90 frames
        this.spinDuration = 50 + (int)(Math.random() * 40);
        // vanishDuration: base 30-60 frames
        this.vanishDuration = 30 + (int)(Math.random() * 30);
    }

    // update with player coordinates to pick respawn near player
    public void update(int width, int height, int playerX, int playerY, boolean isSlowed) {
        // if killed by damage, switch to death state to play circle
        if (hp <= 0 && state != 3) {
            state = 3;
            deathTimer = DEATH_CIRCLE_DURATION;
            vanishCircleX = x;
            vanishCircleY = y;
        }
        if (state == 0) {
            spinTimer++;
            spinAngle += 0.4;
            if (spinTimer >= spinDuration) {
                // vanish and choose respawn near player
                state = 1;
                vanishTimer = 0;
                // respawn location near player
                respawnX = playerX + (int)(Math.random() * 160 - 80);
                respawnY = playerY + (int)(Math.random() * 120 - 60);
                // record vanish location for a circle visual
                vanishCircleX = x;
                vanishCircleY = y;
            }
        } else if (state == 1) {
            vanishTimer++;
            // warning pulse grows at respawn location
            if (vanishTimer >= vanishDuration) {
                // reappear and dash toward player
                x = respawnX;
                y = respawnY;
                // initialize precise position used by dash physics
                posX = x;
                posY = y;
                // reset dash motion state
                currentSpeed = DASH_SPEED;
                bounceCount = 0;
                trailFade = 0;
                dashAngle = Math.atan2(playerY - y, playerX - x);
                state = 2;
                trailPositions.clear();
                // clear vanish-circle so it doesn't persist
                vanishCircleX = -1000;
                vanishCircleY = -1000;
            }
        } else if (state == 2) {
            // dash quickly toward dashAngle using double positions for smooth movement
            double nx = posX + Math.cos(dashAngle) * currentSpeed;
            double ny = posY + Math.sin(dashAngle) * currentSpeed;

            // detect wall collisions and bounce back toward player if hit
            boolean hitWall = false;
            if (nx < 10 || nx > width - 10) {
                hitWall = true;
            }
            if (ny < 10 || ny > height - 10) {
                hitWall = true;
            }

            // update and clamp positions so dash starts/stays onscreen
            posX = Math.max(10, Math.min(width - 10, nx));
            posY = Math.max(10, Math.min(height - 10, ny));
            x = (int) Math.round(posX);
            y = (int) Math.round(posY);

            trailPositions.add(new int[]{x, y});
            if (trailPositions.size() > TRAIL_MAX) trailPositions.remove(0);
            // fade timer to eventually remove enemy if offscreen or after trail
            trailFade++;

            if (hitWall) {
                bounceCount++;
                // on hitting a wall, redirect toward player and slow down a bit
                dashAngle = Math.atan2(playerY - y, playerX - x);
                currentSpeed *= 0.65; // lose momentum on hit
                if (bounceCount >= MAX_BOUNCES) {
                    // stop dashing further — transition to death state so circle can show
                    state = 3;
                    deathTimer = DEATH_CIRCLE_DURATION;
                    vanishCircleX = x;
                    vanishCircleY = y;
                }
            }
        }
        else if (state == 3) {
            // dying: countdown death timer so GamePanel removes this enemy after effect
            if (deathTimer > 0) deathTimer--;
        }
        // keep on screen
        x = Math.max(10, Math.min(width - 10, x));
        y = Math.max(10, Math.min(height - 10, y));
    }

    // Spawn local projectiles/particles to global list
    public void spawnProjectiles(ArrayList<EnemyProjectile> projectiles, int playerX, int playerY) {
        // Red triangle no longer spawns flying particles; visual circle is drawn directly in draw().
    }

    public boolean collidesWith(int px, int py) {
        return Math.abs(x - px) < 18 && Math.abs(y - py) < 18;
    }

    public void takeDamage(int d) { hp -= d; }
    public boolean isDead() { return hp <= 0 && state == 3 ? (deathTimer <= 0) : (state == 3 && deathTimer <= 0); }
    public int getX() { return x; }
    public int getY() { return y; }

    public void draw(Graphics2D g) {
        if (state == 0) {
            // spinning sprite (use triangle sprite if available)
            // use the red triangle sprite for visibility
            var sprite = SpriteLoader.getSprite("red_triangle");
            if (sprite != null) {
                java.awt.geom.AffineTransform t = g.getTransform();
                g.translate(x, y);
                g.rotate(spinAngle);
                g.drawImage(sprite, -20, -20, 40, 40, null);
                g.setTransform(t);
            } else {
                // fallback: draw a red triangle so enemy is always visible
                java.awt.geom.AffineTransform t = g.getTransform();
                g.translate(x, y);
                g.rotate(spinAngle);
                int[] xp = {0, -12, 12};
                int[] yp = {-14, 12, 12};
                g.setColor(new Color(200, 40, 40));
                g.fillPolygon(xp, yp, 3);
                g.setColor(new Color(120, 20, 20));
                g.setStroke(new BasicStroke(2));
                g.drawPolygon(xp, yp, 3);
                g.setTransform(t);
            }
        } else if (state == 1) {
            // draw nothing at original spot; draw warning at respawn
            float alpha = (float)(vanishTimer) / vanishDuration;
            g.setColor(new Color(1f, 0.3f, 0.3f, Math.min(0.9f, alpha)));
            int size = 20 + (int)(alpha * 30);
            g.fillOval(respawnX - size/2, respawnY - size/2, size, size);
            // draw a short vanish-circle at the original vanish location
            if (vanishTimer <= vanishCircleDuration) {
                float vprog = (float)vanishTimer / (float)Math.max(1, vanishCircleDuration);
                float valpha = Math.max(0f, 1f - vprog);
                int vr = 12 + (int)(vprog * 36);
                g.setColor(new Color(1f, 0.2f, 0.2f, valpha * 0.9f));
                g.fillOval(vanishCircleX - vr/2, vanishCircleY - vr/2, vr, vr);
                g.setColor(new Color(0.6f, 0.1f, 0.1f, valpha));
                g.setStroke(new BasicStroke(2));
                g.drawOval(vanishCircleX - vr/2, vanishCircleY - vr/2, vr, vr);
            }
        } else if (state == 3) {
            // draw death vanish circle at last position
            int v = (DEATH_CIRCLE_DURATION - deathTimer);
            float vprog = (float)v / (float)Math.max(1, DEATH_CIRCLE_DURATION);
            float valpha = Math.max(0f, 1f - vprog);
            int vr = 12 + (int)(vprog * 36);
            g.setColor(new Color(1f, 0.2f, 0.2f, valpha * 0.95f));
            g.fillOval(vanishCircleX - vr/2, vanishCircleY - vr/2, vr, vr);
            g.setColor(new Color(0.6f, 0.1f, 0.1f, valpha));
            g.setStroke(new BasicStroke(2));
            g.drawOval(vanishCircleX - vr/2, vanishCircleY - vr/2, vr, vr);
        } else if (state == 2) {
            // draw trail
            for (int i = 0; i < trailPositions.size(); i++) {
                int[] pos = trailPositions.get(i);
                float a = (float)i / trailPositions.size();
                g.setColor(new Color(1f, 0.2f, 0.2f, a));
                int s = 12 - (i * 10 / Math.max(1, trailPositions.size()));
                g.fillRect(pos[0] - s/2, pos[1] - s/2, s, s);
            }
            var sprite = SpriteLoader.getSprite("red_triangle");
            if (sprite != null) {
                java.awt.geom.AffineTransform t = g.getTransform();
                // draw using rounded pos for smoother rotation when dashing
                g.translate((int)Math.round(posX), (int)Math.round(posY));
                g.rotate(dashAngle + Math.PI/2);
                g.drawImage(sprite, -20, -20, 40, 40, null);
                g.setTransform(t);
            } else {
                // fallback red triangle when no sprite loaded
                java.awt.geom.AffineTransform t = g.getTransform();
                g.translate((int)Math.round(posX), (int)Math.round(posY));
                g.rotate(dashAngle + Math.PI/2);
                int[] xp = {0, -12, 12};
                int[] yp = {-14, 12, 12};
                g.setColor(new Color(220, 60, 60));
                g.fillPolygon(xp, yp, 3);
                g.setColor(new Color(140, 30, 30));
                g.setStroke(new BasicStroke(2));
                g.drawPolygon(xp, yp, 3);
                g.setTransform(t);
            }
        }
    }
}
