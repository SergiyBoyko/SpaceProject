package com.company;

import com.company.controller.Controller;
import com.company.controller.Space;
import com.company.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serhii Boiko on 04.08.2017.
 */
public class View extends JPanel {
    private static final Color BG_COLOR = new Color(0xbbada0);
    private static final String FONT_NAME = "Arial";
    private static final int PEACE_SIZE = 500 / 20 - 7;
    private static final int PEACE_MARGIN = 5;
    private static final Color FILL_PEACE = new Color(0x00AA0A0);
    private static final Color EMPTY_PEACE = new Color(0xcdc1b4);

    private Controller controller;
    //    public Color pause;
    private List<Image> backgroundImages = new ArrayList<Image>();
    private Image ship;
    private List<Image> heavyShip = new ArrayList<Image>();
    private List<Image> liteShip = new ArrayList<Image>();
    private Image ufo;
    private Image boss;
    private Image rocket;
    private Image bomb;
    private List<Image> expl = new ArrayList<Image>();

    boolean isGameWon = false;
    boolean isGameLost = false;

    public void setGameWon(boolean gameWon) {
        isGameWon = gameWon;
    }

    public boolean isGameWon() {
        return isGameWon;
    }

    public boolean isGameLost() {
        return isGameLost;
    }

    public void setGameLost(boolean gameLost) {
        isGameLost = gameLost;
    }

    public View(Controller controller) {
        setFocusable(true);
        this.controller = controller;
        addKeyListener(controller);
        try {
            loadSources();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    /*
    * ship size 125 123
    * ufo size 125 83
    *
     */

    private void loadSources() throws IOException {
        backgroundImages.add(ImageIO.read(new File("sources/images/space_bg1.jpg")));
        backgroundImages.add(ImageIO.read(new File("sources/images/space_bg2.jpg")));
        backgroundImages.add(ImageIO.read(new File("sources/images/space_bg3.jpg")));

//            lightShip = ImageIO.read(new File("sources/images/ship_light.png"));
        ship = ImageIO.read(new File("sources/images/ship.png"));
        heavyShip.add(ImageIO.read(new File("sources/images/heavy/l_ship.png")));
        heavyShip.add(ImageIO.read(new File("sources/images/heavy/m_ship.png")));
        heavyShip.add(ImageIO.read(new File("sources/images/heavy/r_ship.png")));

        liteShip.add(ImageIO.read(new File("sources/images/lite/l_ship.png")));
        liteShip.add(ImageIO.read(new File("sources/images/lite/m_ship.png")));
        liteShip.add(ImageIO.read(new File("sources/images/lite/r_ship.png")));

        boss = ImageIO.read(new File("sources/images/boss.png"));
        ufo = ImageIO.read(new File("sources/images/ufo.png"));
        rocket = ImageIO.read(new File("sources/images/rocket.png"));
        bomb = ImageIO.read(new File("sources/images/bomb.png"));

        expl.add(ImageIO.read(new File("sources/images/explosion/expl.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expl.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expl.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expld.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expld.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expl.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expl.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expld.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expld.png")));
        expl.add(ImageIO.read(new File("sources/images/explosion/expl.png")));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        fillGameField(g);
        fillGamePanel(g);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        Font[] allFonts = ge.getAllFonts();

//        for (int i = 0; i < allFonts.length; i++) {
//            Font f = allFonts[i].deriveFont(10.0f);
//            g.setFont(f);
//
//            g.setColor(Color.white);
//            g.drawString("Hello! " + allFonts[26].getName() + " " + allFonts.length + " " , 10, 200);
//        System.out.println(allFonts[26].getName());
//
//        }
    }

    private void fillGameField(Graphics g) {
        Image bg = (int) (Space.game.getLevel() / 10) <= 2 ? backgroundImages.get(Space.game.getLevel() / 10)
                : backgroundImages.get(2);
        g.drawImage(bg, 0, 0, this);

        if (Space.game.getExplosions().size() > 0) {
//            List<Explosion> explosions = new ArrayList<Explosion>(Space.game.getExplosions());
            for (Explosion explosion : Space.game.getExplosions()) {
                if (explosion.getFrame() != 0) {
                    drawElement(g, expl.get(explosion.getFrame()), (int) explosion.getX() - 1, (int) explosion.getY(), 200, 86, true);
                    explosion.removeOneFrame();
                } else explosion.die();
            }
        }

        SpaceShip spaceShip = Space.game.getShip();
        if (Space.game.getShip().getLevel() == 0) {
            drawElement(g, liteShip.get((int) (spaceShip.getDx() + 1)), (int) spaceShip.getX(), (int) spaceShip.getY(), 85, 136, true);
        } else if (Space.game.getShip().getLevel() == 1) {
            drawElement(g, ship, (int) spaceShip.getX(), (int) spaceShip.getY(), 100, 99, true);
        } else {
            drawElement(g, heavyShip.get((int) (spaceShip.getDx() + 1)), (int) spaceShip.getX(), (int) spaceShip.getY(), 125, 123, true);
        }
        if (!spaceShip.isAlive()) {
            drawElement(g, expl.get(0), (int) spaceShip.getX() - 1, (int) spaceShip.getY(), 175, 75, true);
//            Space.game.getExplosions().add(new Explosion(spaceShip.getX(), spaceShip.getY()));
        }

        if (Space.game.isBossAval() && Space.game.getBosses().size() > 0) {
            List<Boss> bosses = new ArrayList<Boss>(Space.game.getBosses());
            for (Boss b : bosses) {
                drawElement(g, boss, (int) b.getX(), (int) b.getY(), 150, 157, true);
                if (!b.isAlive()) {
                    drawElement(g, expl.get(0), (int) b.getX() - 1, (int) b.getY() + 1, 200, 86, true);
//                    Space.game.getExplosions().add(new Explosion(b.getX(), b.getY()));
                }
            }
        }

        List<Ufo> ufos = new ArrayList<>(Space.game.getUfos());
        for (Ufo u : ufos) {
            drawElement(g, ufo, (int) u.getX(), (int) u.getY(), 125, 83, true);
            if (!u.isAlive()) {
                drawElement(g, expl.get(0), (int) u.getX() - 1, (int) u.getY(), 200, 86, true);
                Space.game.getExplosions().add(new Explosion(u.getX(), u.getY()));
            }
        }


        List<Rocket> rockets = new ArrayList<>(Space.game.getRockets());
        for (Rocket r : rockets) {
            drawElement(g, rocket, (int) r.getX(), (int) r.getY(), 25, 35, false);
        }


        List<Bomb> bombs = new ArrayList<>(Space.game.getBombs());
        for (Bomb b : bombs) {
            drawElement(g, bomb, (int) b.getX(), (int) b.getY(), 12, 25, false);
        }
    }

    private void fillGamePanel(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(offsetCoors(Space.game.getWidth()), 0, this.getSize().width, this.getSize().height);
//        for (int i = 0; i < Space.game.getHeight(); i++) {
//            for (int j = 0; j < Space.game.getWidth(); j++) {
////                if (i == 0 || j == 0 || i == Space.game.getHeight()-1 || j == Space.game.getWidth()-1)
//                drawTile(g, String.valueOf(i + "" + j), j, i);
//            }
//        }

        // - my costume edit begin
        g.setColor(new Color(0xe0e0e0));
        g.fillRoundRect(710, 20, 210, 290, 80, 8);
        g.setFont(new Font(FONT_NAME, Font.ITALIC, 26));
        g.setColor(Color.BLACK);
        g.drawString("Instruction:", 750, 50);
        g.drawString("arrows - control", 720, 85);
        g.drawString("down - stop", 720, 120);
        g.drawString("space - fire", 720, 155);
        g.drawString("p - pause", 720, 190);
        g.drawString("q - exit", 720, 225);
        g.drawString("esc - restart", 720, 270);
        g.setColor(Color.red);
        if (Space.game.isBossAval() && !Space.game.getBosses().isEmpty()) {
            g.drawString("Boss:", 720, 365);
            g.fillRect(780, 345, (Space.game.getBosses().get(0).getHp()) * 2, 20);
        }
        g.fillRect(780, 375, Space.game.getShip().getHp() * 20, 20);
        g.drawString("HP: ", 720, 395);// + (Space.game.getShip().getHp())
        g.setColor(Color.CYAN);
        g.drawString("Level: " + (Space.game.getLevel() + 1), 720, 415);
        g.drawString("Score: " + Space.game.getScore(), 720, 435);
        if (Space.game.isPause()) {
            g.setColor(new Color(0xfff6bc));
            g.setFont(new Font("Garamond", Font.BOLD | Font.ITALIC, 70));
            g.drawString("Paused", getWidth() / 2 - 150, getHeight() / 2);
        }
//        side(g);
        // - end
        if (isGameWon) {
            JOptionPane.showMessageDialog(this, "You've won!");
        } else if (isGameLost) {
            JOptionPane.showMessageDialog(this, "You've lost :(");
        }
    }

    private void drawElement(Graphics g2, Image value, int x, int y, int width, int height, boolean off) {
        Graphics2D g = ((Graphics2D) g2);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int xOffset;
        int yOffset;
        if (off) {
            xOffset = offsetCoors(x) - (int) (PEACE_SIZE * 3.5) - (int) (Space.game.getShip().getLevel() == 2 ? (PEACE_SIZE * 0.5) : 0);
            yOffset = offsetCoors(y) - PEACE_SIZE * 4;
        } else {
            xOffset = offsetCoors(x) - (int) (PEACE_SIZE * 1.5);
            yOffset = offsetCoors(y) - (int) (PEACE_SIZE);
        }
        g.drawImage(value, xOffset, yOffset, width, height, this);

    }

//    private void drawTile(Graphics g2, String s, int x, int y) {
//        Graphics2D g = ((Graphics2D) g2);
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
////        int value = tile.value;
//        int xOffset = offsetCoors(x);
//        int yOffset = offsetCoors(y);
//        g.setColor(EMPTY_PEACE);
//        g.fillRoundRect(xOffset, yOffset, PEACE_SIZE, PEACE_SIZE, 80, 80);
////        g.setColor(tile.getFontColor());
////        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
//        final Font font = new Font(FONT_NAME, Font.BOLD, 6);
////        g.setFont(font);
////
////        String s = String.valueOf(value);
//        final FontMetrics fm = getFontMetrics(font);
////
//        final int w = fm.stringWidth(s);
//        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];
////
////        if (value != 0)
//        g.setColor(Color.BLACK);
//        g.drawString(s, xOffset + (PEACE_SIZE - w) / 2, yOffset + PEACE_SIZE - (PEACE_SIZE - h) / 2 - 2);
//    }

    private static int offsetCoors(int arg) {
        return arg * (PEACE_MARGIN + PEACE_SIZE) + PEACE_MARGIN;
    }

}
