import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EggCatcherGame extends JPanel implements KeyListener {
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 400;
    private static final int EGG_WIDTH = 45;
    private static final int EGG_HEIGHT = 55;
    private static final int EGG_SCORE = 10;
    private static final int EGG_SPEED = 500;
    private static final int EGG_INTERVAL = 4000;
    private static final double DIFFICULTY = 0.95;
    private static final int CATCHER_WIDTH = 100;
    private static final int CATCHER_HEIGHT = 100;
    private static final int LIVES = 3;

    private List<Point> eggs = new ArrayList<>();
    private int score = 0;
    private int livesRemaining = LIVES;
    private int catcherX;
    private int catcherY;
    private int catcherX2;
    private int catcherY2;

    public EggCatcherGame() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setBackground(new Color(135, 206, 235)); // Deep Sky Blue
        setFocusable(true);
        addKeyListener(this);

        Timer eggTimer = new Timer();
        eggTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                createEgg();
            }
        }, 1000, EGG_INTERVAL);

        Timer moveEggsTimer = new Timer();
        moveEggsTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moveEggs();
            }
        }, 1000, EGG_SPEED);

        Timer checkCatcherTimer = new Timer();
        checkCatcherTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkCatcher();
            }
        }, 1000, 100);

        initializeCatcher();
    }

    // Rest of the code remains the same...

    private void initializeCatcher() {
        catcherX = CANVAS_WIDTH / 2 - CATCHER_WIDTH / 2;
        catcherY = CANVAS_HEIGHT - CATCHER_HEIGHT - 20;
        catcherX2 = catcherX + CATCHER_WIDTH;
        catcherY2 = catcherY + CATCHER_HEIGHT;
    }

    private void createEgg() {
        int x = new Random().nextInt(CANVAS_WIDTH - EGG_WIDTH);
        eggs.add(new Point(x, 40));
    }

    private void moveEggs() {
        Iterator<Point> eggIterator = eggs.iterator();
        while (eggIterator.hasNext()) {
            Point egg = eggIterator.next();
            egg.y += 10;
            if (egg.y > CANVAS_HEIGHT) {
                eggIterator.remove();
                loseALife();
            }
        }
        repaint();
    }

    private void checkCatcher() {
        for (Iterator<Point> eggIterator = eggs.iterator(); eggIterator.hasNext(); ) {
            Point egg = eggIterator.next();
            if (catcherX < egg.x && egg.x + EGG_WIDTH < catcherX2 && catcherY2 - egg.y - EGG_HEIGHT < 40) {
                eggIterator.remove();
                increaseScore(EGG_SCORE);
            }
        }
    }

    private void increaseScore(int points) {
        score += points;
        repaint();
    }

    private void loseALife() {
        livesRemaining--;
        if (livesRemaining <= 0) {
            JOptionPane.showMessageDialog(this, "Game Over!\nFinal Score: " + score);
            System.exit(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.fillRect(-5, CANVAS_HEIGHT - 100, CANVAS_WIDTH + 5, CANVAS_HEIGHT + 5);

        g.setColor(Color.ORANGE);
        g.fillOval(-80, -80, 120, 120);

        g.setColor(Color.BLUE);
        g.fillArc(catcherX, catcherY, CATCHER_WIDTH, CATCHER_HEIGHT, 200, 140);

        g.setFont(new Font("Monospaced", Font.PLAIN, 18));
        g.setColor(Color.BLUE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + livesRemaining, CANVAS_WIDTH - 100, 20);

        for (Point egg : eggs) {
            g.setColor(Color.BLUE);
            g.fillOval(egg.x, egg.y, EGG_WIDTH, EGG_HEIGHT);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT && catcherX > 0) {
            catcherX -= 20;
            catcherX2 -= 20;
            repaint();
        } else if (keyCode == KeyEvent.VK_RIGHT && catcherX2 < CANVAS_WIDTH) {
            catcherX += 20;
            catcherX2 += 20;
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Egg Catcher");
        EggCatcherGame game = new EggCatcherGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
