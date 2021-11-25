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
import javax.swing.plaf.ColorUIResource;

import java.util.ArrayList;
import java.util.HashMap;

public class Board extends JPanel implements ActionListener {

    private final int LEFT = 0, UP = 1, RIGHT = 2, DOWN = 3;
    private final String MEDIA_PATH = "Images/";
    private final int DOT_SIZE = 10;
    private final int GRID_WIDTH = 30;
    private final int GRID_HEIGHT = 30;

    private final int B_WIDTH = GRID_WIDTH * DOT_SIZE;
    private final int B_HEIGHT = GRID_HEIGHT * DOT_SIZE;
    private final int RAND_POS = GRID_WIDTH - 1;
    private final int DELAY = 100;

    private final Color BACKCOLOR = new ColorUIResource(0, 128, 0);
    private final Color FORECOLOR = Color.WHITE;

    private int posX;
    private int posY;

    private int coinCount;
    private int bugCount;
    private ArrayList<Collectible> collectibleList;
    private Voiture testVoit = new Voiture(B_WIDTH, B_HEIGHT / 2, DOT_SIZE, DOT_SIZE, LEFT);

    private int direction;
    private int level = 0;
    private boolean inGame = false;
    private boolean spawnedGoal = false;

    private int[] levels = { 1, 2, 3 };

    private Timer gameTimer;
    private Timer introTimer;
    private HashMap<String, Image> spritesMap;
    private Font hudFont;

    private int score = 0;
    private int voidX = -1*B_WIDTH;
    private int voidY = -1*B_HEIGHT;

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(BACKCOLOR);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        setVariables();
        initGame();
    }

    private void loadImages() {

        spritesMap = new HashMap<String, Image>();
        ImageIcon ii;

        ii = new ImageIcon(MEDIA_PATH+Coin.getPathToImage());
        spritesMap.put("Coin", ii.getImage());

        ii = new ImageIcon(MEDIA_PATH+Bug.getPathToImage());
        spritesMap.put("Bug",ii.getImage());

        ii = new ImageIcon(MEDIA_PATH+Goal.getPathToImage());
        spritesMap.put("Goal", ii.getImage());

        ii = new ImageIcon(MEDIA_PATH+"headUp.png");
        spritesMap.put(Integer.toString(UP), ii.getImage());

        ii = new ImageIcon(MEDIA_PATH+"headDown.png");
        spritesMap.put(Integer.toString(DOWN), ii.getImage());

        ii = new ImageIcon(MEDIA_PATH+"headLeft.png");
        spritesMap.put(Integer.toString(LEFT), ii.getImage());

        ii = new ImageIcon(MEDIA_PATH+"headRight.png");
        spritesMap.put(Integer.toString(RIGHT), ii.getImage());

        ii = new ImageIcon(MEDIA_PATH+"head.png");
        spritesMap.put("testVoit", ii.getImage());
    }

    private void setVariables()
    {
        hudFont = new Font("Helvetica", Font.BOLD, DOT_SIZE);

        gameTimer = new Timer(DELAY, this);
        introTimer = new Timer(2000, loadNextLevel);
        introTimer.setRepeats(false); 
    }

    private void initGame() {

        System.out.println("We initin' " + level);

        introTimer.start();

        repaint();
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
        introTimer.stop();
        gameTimer.start();
        repaint();
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
                g.drawImage(spritesMap.get(app.getType()), app.getPosX(), app.getPosY(), this);
            }
            
            g.drawImage(spritesMap.get(Integer.toString(direction)), posX, posY, this);

            g.drawImage(spritesMap.get("testVoit"), testVoit.getPosX(), testVoit.getPosY(), this);

            g.setFont(hudFont);
            g.setColor(FORECOLOR);
            g.drawString("Score : " + score, 0, DOT_SIZE);

            g.drawString("Niveau " + (level + 1), B_WIDTH - getFontMetrics(hudFont).stringWidth("Niveau X"), DOT_SIZE);

            Toolkit.getDefaultToolkit().sync();

        } else {

            if ( level < levels.length ) prompt(g, "Niveau " + (level + 1));
            else prompt(g, "Game Over");

            Toolkit.getDefaultToolkit().sync();
        }        
    }

    private void prompt(Graphics g, String prompt)
    {
        Font small = new Font("Helvetica", Font.BOLD, 2*DOT_SIZE);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.BLACK);
        g.fillRect(0, B_HEIGHT / 2 - 3 * DOT_SIZE, B_WIDTH, 5 * DOT_SIZE);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(prompt, (B_WIDTH - metr.stringWidth(prompt)) / 2, B_HEIGHT / 2);
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

        if ( testVoit.inCar(posX, posY) ) System.out.println("Aïeeeeeuh");

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
        gameTimer.stop();

        level++;

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

        switch (direction)
        {
            case LEFT:
                posX -= DOT_SIZE;
                break;

            case RIGHT:
                posX += DOT_SIZE;
                break;

            case UP:
                posY -= DOT_SIZE;
                break;

            case DOWN:
                posY += DOT_SIZE;
            break;
        }
    }

    private void moveVoiture()
    {
        switch (testVoit.getDirection())
        {
            case LEFT:
                testVoit.setPosX(testVoit.getPosX() - DOT_SIZE);
                if (testVoit.getPosX() < 0) testVoit.setPosX(B_WIDTH);
                break;

            case RIGHT:
                posX += DOT_SIZE;
                break;

            case UP:
                posY -= DOT_SIZE;
                break;

            case DOWN:
                posY += DOT_SIZE;
            break;
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
            triggerGameOver();
            System.out.println("yoooo");
        }
    }

    private int GetRandomCoordinate() {

        int r = (int) (Math.random() * RAND_POS);
        return (r * DOT_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkCollision();
            checkCollectibles();
            moveVoiture();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                direction = LEFT;
            }

            if (key == KeyEvent.VK_RIGHT) {
                direction = RIGHT;
            }

            if (key == KeyEvent.VK_UP) {
                direction = UP;
            }

            if (key == KeyEvent.VK_DOWN) {
                direction = DOWN;
            }

            move();
        }
    }
}
