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
import java.util.HashMap;

public class Board extends JPanel implements ActionListener {

    private final int LEFT = 0, UP = 1, RIGHT = 2, DOWN = 3;

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private int posX;
    private int posY;

    private int coinCount;
    private int bugCount;
    private ArrayList<Collectible> collectibleList;
    private HashMap<String, Image> collectibleImages;

    private int Direction;
    private boolean inGame = true;

    private Timer timer;
    private Image head;

    private int score;
    private int voidX = -1*B_WIDTH;
    private int voidY = -1*B_HEIGHT;

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

        collectibleImages = new HashMap<String, Image>();

        ImageIcon iic = new ImageIcon(Coin.getPathToImage());
        collectibleImages.put("Coin", iic.getImage());

        ImageIcon iib = new ImageIcon(Bug.getPathToImage());
        collectibleImages.put("Bug", iib.getImage());

        ImageIcon iih = new ImageIcon("head.png");
        head = iih.getImage();
    }

    private void initGame() {

        score = 0;

        posX = B_WIDTH / 2;
        posY = B_HEIGHT / 2;
        
        coinCount = 3;
        bugCount = 2;
        collectibleList = new ArrayList<Collectible>();

        for ( int compt = 0; compt < coinCount; compt++ )
            collectibleList.add(new Coin(GetRandomCoordinate(), GetRandomCoordinate()));

        for ( int compt = 0; compt < bugCount; compt++ )
            collectibleList.add(new Bug(GetRandomCoordinate(), GetRandomCoordinate()));

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

            for ( Collectible app : collectibleList )
            {
                g.drawImage(collectibleImages.get(app.getType()), app.getPosX(), app.getPosY(), this);
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

    private void checkCollectibles() {

        for (Collectible coll : collectibleList)
        {
            if ((posX == coll.getPosX()) && (posY == coll.getPosY())) {
    
                coll.setPosX(voidX);
                coll.setPosY(voidY);

                coll.triggerAction(this);
                
                System.out.println("Pièces restantes : "+coinCount);
                System.out.println("Score : "+score);
            }
        }
    }

    public void incScore(int amount)
    {
        score += amount;
    }

    public void decCoinCount()
    {
        coinCount--;
    }

    private void move() {

        if (Direction == LEFT) {
            posX -= DOT_SIZE;
        }

        if (Direction == RIGHT) {
            posX += DOT_SIZE;
        }

        if (Direction == UP) {
            posY -= DOT_SIZE;
        }

        if (Direction == DOWN) {
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

            checkCollectibles();
            checkCollision();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                Direction = LEFT;
            }

            if (key == KeyEvent.VK_RIGHT) {
                Direction = RIGHT;
            }

            if (key == KeyEvent.VK_UP) {
                Direction = UP;
            }

            if (key == KeyEvent.VK_DOWN) {
                Direction = DOWN;
            }

            move();
        }
    }
}
