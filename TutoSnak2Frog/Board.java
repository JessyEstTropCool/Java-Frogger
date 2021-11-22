import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private int posX;
    private int posY;

    private int appleCount;
    private ArrayList<Apple> appleList;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    //private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("head.png");
        head = iih.getImage();
    }

    private void initGame() {

        posX = B_WIDTH / 2;
        posY = B_HEIGHT / 2;
        
        appleCount = 3;
        appleList = new ArrayList<Apple>(3);

        int appleX, appleY;

        for ( Apple apple : appleList )
        {
            appleX = GetRandomCoordinate();
            appleY = GetRandomCoordinate();
            appleList.add(new Apple(appleX, appleY));
        }

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            for ( Apple app : appleList )
            {
                g.drawImage(apple, app.getPosX(), app.getPosY(), this);
            }
            
            g.drawImage(head, posX, posY, this);

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        for (Apple app : appleList)
        {
            if ((posX == app.getPosX()) && (posY == app.getPosY())) {
    
                app.setPosX(GetRandomCoordinate());
                app.setPosY(GetRandomCoordinate());
            }
        }
    }

    private void move() {

        if (leftDirection) {
            posX -= DOT_SIZE;
        }

        if (rightDirection) {
            posX += DOT_SIZE;
        }

        if (upDirection) {
            posY -= DOT_SIZE;
        }

        if (downDirection) {
            posY += DOT_SIZE;
        }
    }

    private void checkCollision() {

        if (posY >= B_HEIGHT) {
            inGame = false;
        }

        if (posY < 0) {
            inGame = false;
        }

        if (posX >= B_WIDTH) {
            inGame = false;
        }

        if (posX < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }

    private int GetRandomCoordinate() {

        int r = (int) (Math.random() * RAND_POS);
        return (r * DOT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            move();
        }
    }
}
