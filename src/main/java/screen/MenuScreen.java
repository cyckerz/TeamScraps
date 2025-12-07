package screen;

import engine.Core;
import engine.GameStates;
import engine.StateMachine;
import engine.FileManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import engine.SpriteLoader;
import java.awt.image.BufferedImage;

import java.awt.Font;
import java.io.FileInputStream;
import java.io.InputStream;


// menu implementation
public class MenuScreen implements Screen, MouseListener {

    private final Core core;
    private final StateMachine states;
    private final FileManager files; // not used yet, but available if needed later

    // background + fonts
    private BufferedImage menuBackground;
    private Font titleFont;
    private Font buttonFont;

    // simple button hitboxes
    private BufferedImage stoneTile;
    private Rectangle startBtn;
    private Rectangle highScoreBtn;
    private Rectangle quitBtn;

    public MenuScreen(Core core, StateMachine states, FileManager files) { // constructor
        this.core = core;
        this.states = states;
        this.files = files;

        loadFonts();
        initButtons();

        menuBackground = SpriteLoader.load("resources/backgrounds/front.jpg");
        stoneTile      = SpriteLoader.load("resources/sprites/button.png");
    }

    private void loadFonts() {
        try (InputStream in = new FileInputStream("resources/fonts/alagard.ttf")) {
            Font base = Font.createFont(Font.TRUETYPE_FONT, in);
            titleFont = base.deriveFont(Font.BOLD, 140f);   // big title
            buttonFont = base.deriveFont(Font.PLAIN, 26f); // menu buttons
        } catch (Exception e) {
            titleFont = new Font("Arial", Font.BOLD, 80);
            buttonFont = new Font("Arial", Font.PLAIN, 26);
        }
    }

    // layout buttons
    private void initButtons() {
        int btnWidth = 220;
        int btnHeight = 50;
        int centerX = Core.WIDTH / 2 - btnWidth / 2;
        int firstY = 260;

        startBtn      = new Rectangle(centerX, firstY, btnWidth, btnHeight);
        highScoreBtn  = new Rectangle(centerX, firstY + 70, btnWidth, btnHeight);
        quitBtn       = new Rectangle(centerX, firstY + 140, btnWidth, btnHeight);
    }

    @Override

    public void onEnter() {
        core.addMouseListener(this); // start listening for clicks
    }

    @Override
    public void onExit() {
        core.removeMouseListener(this); // stop listening when leaving menu
    }

    @Override
    public void update(double dt) {
        // no per-frame logic needed for static menu right now
    }

    @Override
    public void render(Graphics2D g) {
        g.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );

        // background
        if (menuBackground != null) {
            g.drawImage(menuBackground, 0, 0, Core.WIDTH, Core.HEIGHT, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, Core.WIDTH, Core.HEIGHT);
        }

        // title + shadow using BIG titleFont
        g.setFont(titleFont);

        // shadow
        g.setColor(new Color(0, 0, 0, 180));
        drawCentered(g, "SNAKE", Core.WIDTH + 3, 190 + 3);

        // main text
        g.setColor(new Color(255, 140, 0));
        drawCentered(g, "SNAKE", Core.WIDTH, 190);

        // buttons (unchanged, they use buttonFont inside drawButton)
        drawButton(g, startBtn, "Start Game");
        drawButton(g, highScoreBtn, "High Scores");
        drawButton(g, quitBtn, "Quit");
    }

    private void drawButton(Graphics2D g, Rectangle rect, String text) {
        // --- stone background ---
        if (stoneTile != null) {
            // draw the button.png scaled to the button rect
            g.drawImage(stoneTile,
                    rect.x, rect.y,
                    rect.x + rect.width, rect.y + rect.height,
                    0, 0, stoneTile.getWidth(), stoneTile.getHeight(),
                    null);
        } else {
            // fallback flat fill if image missing
            g.setColor(new Color(40, 40, 40, 220));
            g.fill(rect);
        }

        // --- chunky pixel-style border ---
        g.setColor(new Color(10, 10, 10));                 // dark outer edge
        g.drawRect(rect.x, rect.y, rect.width, rect.height);

        g.setColor(new Color(130, 130, 130));              // lighter inner edge
        g.drawRect(rect.x + 2, rect.y + 2,
                rect.width - 4, rect.height - 4);

        // --- label ---
        g.setFont(buttonFont);
        g.setColor(new Color(235, 235, 235));              // slightly off-white
        FontMetrics fm = g.getFontMetrics();

        int tx = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int ty = rect.y + (rect.height + fm.getAscent()) / 2 - 4;

        g.drawString(text, tx, ty);
    }


    private void drawCentered(Graphics2D g, String text, int width, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    // --- MouseListener implementation ---

    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();

        if (startBtn.contains(p)) {
            core.requestFocusInWindow();
            core.toPlaying();  // start the game
        } else if (highScoreBtn.contains(p)) {
            core.toHighScores();  // go to high score screen
        } else if (quitBtn.contains(p)) {
            System.exit(0); // exit game
        }
    }

    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}
