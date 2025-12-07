package screen;

import engine.Core;
import engine.FileManager;
import engine.StateMachine;
import entity.Score;
import engine.SpriteLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Screen to display high scores
 * (refactored from original JPanel-based version to fit the game loop)
 */
public class HighScoreScreen implements Screen, MouseListener {

    private final Core core;  // used for mouse listener and navigation
    private final StateMachine states;
    private final FileManager fileManager;

    // graphics
    private BufferedImage bgTile;     // tiled brick background (same as GameScreen wall2)
    private BufferedImage stoneTile;  // button texture
    private Font titleFont;
    private Font listFont;
    private Font buttonFont;

    // "Back to Menu" control - now a drawn button instead of JButton
    private Rectangle backBtn;

    // list of scores
    private List<Score> scores;

    public HighScoreScreen(Core core, StateMachine states, FileManager fileManager) {
        this.core = core;
        this.states = states;
        this.fileManager = fileManager;

        loadFonts();
        loadAssets();
        initLayout(); // similar role to original initializeUI()
    }

    private void loadFonts() {
        try (InputStream in = new FileInputStream("resources/fonts/alagard.ttf")) {
            Font base = Font.createFont(Font.TRUETYPE_FONT, in);
            titleFont  = base.deriveFont(Font.BOLD, 72f);
            listFont   = base.deriveFont(Font.PLAIN, 28f);
            buttonFont = base.deriveFont(Font.PLAIN, 26f);
        } catch (Exception e) {
            titleFont  = new Font("Arial", Font.BOLD, 72);
            listFont   = new Font("Arial", Font.PLAIN, 24);
            buttonFont = new Font("Arial", Font.PLAIN, 26);
        }
    }

    private void loadAssets() {
        // reuse menu background
        bgTile    = SpriteLoader.load("resources/sprites/wall2.png");
        stoneTile  = SpriteLoader.load("resources/sprites/button.png");
    }
    // sets up button position (instead of Swing layout)
    private void initLayout() {
        int btnWidth = 260;
        int btnHeight = 45;
        int x = Core.WIDTH / 2 - btnWidth / 2;
        int y = Core.HEIGHT - 100;
        backBtn = new Rectangle(x, y, btnWidth, btnHeight);
    }

    @Override
    public void onEnter() {
        // original behaviour: load scores when screen is shown
        scores = fileManager.getHighScores();
        core.addMouseListener(this);
        core.requestFocusInWindow();
    }

    @Override
    public void onExit() {
        core.removeMouseListener(this);
    }

    @Override
    public void update(double dt) {
        // no per-frame logic for now (static screen)
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );

        // tiled brick background
        if (bgTile != null) {
            int tw = bgTile.getWidth();
            int th = bgTile.getHeight();
            for (int x = 0; x < Core.WIDTH; x += tw) {
                for (int y = 0; y < Core.HEIGHT; y += th) {
                    g.drawImage(bgTile, x, y, tw, th, null);
                }
            }
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Core.WIDTH, Core.HEIGHT);
        }

        // dark panel behind the scores so they’re readable
        int panelX = 60;
        int panelY = 90;
        int panelW = Core.WIDTH - 120;
        int panelH = Core.HEIGHT - 220;

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        // border of the panel
        g.setColor(new Color(80, 80, 80));
        g.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        // title
        String title = "HIGH SCORES";
        g.setFont(titleFont);
        FontMetrics tfm = g.getFontMetrics();
        int titleX = (Core.WIDTH - tfm.stringWidth(title)) / 2;
        int titleY = panelY + 60;

        g.setColor(Color.BLACK);
        g.drawString(title, titleX - 2, titleY + 2);
        g.setColor(new Color(122, 10, 35));
        g.drawString(title, titleX, titleY);

        // centered scores list
        g.setFont(listFont);
        FontMetrics lfm = g.getFontMetrics();
        int centerX = Core.WIDTH / 2;
        int startY = titleY + 40;
        int lineH  = lfm.getHeight() + 4;

        if (scores == null || scores.isEmpty()) {
            String msg = "No scores yet!";
            int msgX = centerX - lfm.stringWidth(msg) / 2;
            int msgY = startY + lineH;
            g.setColor(Color.WHITE);
            g.drawString(msg, msgX, msgY);
        } else {
            int rank = 1;
            for (Score s : scores) {
                if (rank > 10) break;

                String line = String.format("%2d.  %-10s  %d foods",
                        rank, s.getPlayerName(), s.getFoodsEaten());

                int lineWidth = lfm.stringWidth(line);
                int x = centerX - lineWidth / 2;
                int y = startY + (rank - 1) * lineH;

                g.setColor(Color.BLACK);
                g.drawString(line, x + 2, y + 2);
                g.setColor(Color.WHITE);
                g.drawString(line, x, y);

                rank++;
            }
        }

        // "Back to Menu" button (replaces JButton backButton)
        drawButton(g, backBtn, "Back to Menu");
    }

    private void drawButton(Graphics2D g, Rectangle rect, String text) {
        if (stoneTile != null) {
            g.drawImage(
                    stoneTile,
                    rect.x, rect.y,
                    rect.x + rect.width, rect.y + rect.height,
                    0, 0, stoneTile.getWidth(), stoneTile.getHeight(),
                    null
            );
        } else {
            g.setColor(new Color(40, 40, 40, 220));
            g.fill(rect);
        }

        g.setColor(new Color(20, 20, 20));
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(new Color(130, 130, 130));
        g.drawRect(rect.x + 2, rect.y + 2, rect.width - 4, rect.height - 4);

        g.setFont(buttonFont);
        g.setColor(new Color(235, 235, 235));
        FontMetrics fm = g.getFontMetrics();
        int tx = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int ty = rect.y + (rect.height + fm.getAscent()) / 2 - 3;
        g.drawString(text, tx, ty);
    }

    // MouseListener – replaces original JButton's ActionListener / backButton.getBackButton()
    @Override
    public void mouseClicked(MouseEvent e) {
        if (backBtn.contains(e.getPoint())) { core.toMenu(); }
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }

    /**
     * Refresh the high scores display
     * (preserves original semantic: reload scores data)
     */
    public void refreshScores() {
        scores = fileManager.getHighScores();
    }
}
