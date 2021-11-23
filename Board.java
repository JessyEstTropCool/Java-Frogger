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
    private final String MEDIA_PATH = "Images/";

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
    private int level = -1;
    private boolean inGame = false;
    private boolean spawnedGoal = false;

    private int[] levels = { 3, 7, 11 };

    private Timer timer;
    private Image head;

    private int score = 0;
    private int voidX = -1*B_WIDTH;
    private int voidY = -1*B_HEIGHT;

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.GREEN);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        collectibleImages = new HashMap<String, Image>();

        ImageIcon iic = new ImageIcon(MEDIA_PATH+Coin.getPathToImage());
        collectibleImages.put("Coin", iic.getImage());

        ImageIcon iib = new ImageIcon(MEDIA_PATH+Bug.getPathToImage());
        collectibleImages.put("Bug", iib.getImage());

        ImageIcon iig = new ImageIcon(MEDIA_PATH+Goal.getPathToImage());
        collectibleImages.put("Goal", iig.getImage());

        ImageIcon iih = new ImageIcon(MEDIA_PATH+"head.png");
        head = iih.getImage();
    }

    private void initGame() {

        level++;

        System.out.println("We initin' " + level);

        timer = new Timer(2000, loadNextLevel);
        timer.setRepeats(false);
        timer.start();

        repaint();

        //loadNextLevel.actionPerformed(null);
    }

    private ActionListener loadNextLevel = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            System.out.println("We loadin' " + level);

            spawnedGoal = false;
            inGame = true;

            posX = B_WIDTH / 2;
            posY = B_HEIGHT / 2;
            
            coinCount = levels[level];
            bugCount = levels[level] / 2 + 1;
            collectibleList = new ArrayList<Collectible>();

            for ( int compt = 0; compt < coinCount; compt++ )
                collectibleList.add(new Coin(GetRandomCoordinate(), GetRandomCoordinate()));

            for ( int compt = 0; compt < bugCount; compt++ )
                collectibleList.add(new Bug(GetRandomCoordinate(), GetRandomCoordinate()));

            startLevel();
        }
    };

    private void startLevel()
    {
        System.out.println("We startin' " + level);
        timer = new Timer(DELAY, this);
        timer.start();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            Font hudFont = new Font("Helvetica", Font.BOLD, DOT_SIZE);
            g.setFont(hudFont);
            g.setColor(Color.WHITE);
            g.drawString("Score : " + score, 0, DOT_SIZE);

            for ( Collectible app : collectibleList )
            {
                g.drawImage(collectibleImages.get(app.getType()), app.getPosX(), app.getPosY(), this);
            }
            
            g.drawImage(head, posX, posY, this);

            Toolkit.getDefaultToolkit().sync();

        } else {

            if ( level < levels.length ) nextLevel(g);
            else gameOver(g);

            Toolkit.getDefaultToolkit().sync();
        }        
    }

    private void nextLevel(Graphics g)
    {
        String msg = "Niveau " + (level + 1);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
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
                
                if ( coll.getType() != "Goal" )
                {
                    System.out.println("Pièces restantes : "+coinCount);
                    System.out.println("Score : "+score);
                }
            }
        }

        if ( !spawnedGoal && coinCount <= 0 ) 
        {
            collectibleList.add(new Goal(B_WIDTH / 2, 0));
            spawnedGoal = true;
        }
    }

    public void triggerGameOver()
    {
        System.out.println("We stoppin'");
        inGame = false;
        timer.stop();
        if ( level < levels.length ) initGame();
        else repaint();
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
            posY = B_HEIGHT - DOT_SIZE;
        }

        if (posY < 0) {
            posY = 0;
        }

        if (posX >= B_WIDTH) {
            posX = B_WIDTH - DOT_SIZE;
        }

        if (posX < 0) {
            posX = 0;
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
