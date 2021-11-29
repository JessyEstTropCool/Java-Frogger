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

    //Paramêtres
    private final String MEDIA_PATH = "Images/";
    private final int DOT_SIZE = 10;
    private final int GRID_WIDTH = 30;
    private final int GRID_HEIGHT = 30;
    private final int HUD_HEIGHT = 1;

    //Constantes pour le code
    private final int LEFT = 0, UP = 1, RIGHT = 2, DOWN = 3;
    private final int B_WIDTH = GRID_WIDTH * DOT_SIZE;
    private final int B_HEIGHT = GRID_HEIGHT * DOT_SIZE;
    private final int VERT_OFFSET = HUD_HEIGHT * DOT_SIZE;
    private final int W_HEIGHT = B_HEIGHT + VERT_OFFSET;
    private final int RAND_POS = GRID_WIDTH - 1;
    private final int DELAY = 100;

    private final Color BACKCOLOR = new ColorUIResource(32, 128, 16);
    private final Color FORECOLOR = Color.WHITE;

    private final int[] LANES = { 5, 6, 7, 8, 9, 10, 15, 16, 17, 22, 23 };

    private final String[] IMAGE_FILENAMES = { 
        Coin.getPathToImage(), 
        Bug.getPathToImage(), 
        Goal.getPathToImage(), 
        "goalDown.png", 
        "headUp.png", 
        "headDown.png", 
        "headLeft.png", 
        "headRight.png", 
        "car.png",
        "redCar.png",
        "purpleCar.png",
        "blueCar.png",
        "orangeCar.png"
    };
    private final String[] IMAGE_KEYS = { 
        "Coin", 
        "Bug", 
        "Goal", 
        "GoalDown",
        Integer.toString(UP),
        Integer.toString(DOWN), 
        Integer.toString(LEFT), 
        Integer.toString(RIGHT), 
        "Normal",
        "Blinky",
        "Pinky",
        "Inky",
        "Clyde"
    };

    private int posX;
    private int posY;

    private int coinCount;
    private int bugCount;
    private ArrayList<Collectible> collectibleList;
    private ArrayList<Voiture> voitureList;

    private int direction;
    private int level = 0;
    private boolean inGame = false;
    private boolean spawnedGoal = false;

    private int[] levels = { 1, 2, 3 };

    private Timer gameTimer;
    private Timer introTimer;
    private HashMap<String, Image> spritesMap;
    private Font hudFont;
    private FontMetrics hudMetrics;

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

        setPreferredSize(new Dimension(B_WIDTH, W_HEIGHT));
        loadImages();
        setVariables();
        initGame();
    }

    private void loadImages() {

        spritesMap = new HashMap<String, Image>();
        ImageIcon ii;
        int length = ( IMAGE_FILENAMES.length > IMAGE_KEYS.length )? IMAGE_KEYS.length : IMAGE_FILENAMES.length;

        for ( int compt = 0; compt < length; compt++ )
        {
            ii = new ImageIcon(MEDIA_PATH+IMAGE_FILENAMES[compt]);
            spritesMap.put(IMAGE_KEYS[compt], ii.getImage());
        }
    }

    private void setVariables()
    {
        hudFont = new Font("Helvetica", Font.BOLD, DOT_SIZE);
        hudMetrics = getFontMetrics(hudFont);

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
            posY = B_HEIGHT - DOT_SIZE;
            
            coinCount = levels[level];
            bugCount = levels[level] / 2 + 1;
            collectibleList = new ArrayList<Collectible>();
            voitureList = new ArrayList<Voiture>();

            for ( int i : LANES )
            {
                voitureList.add(new Voiture(GetRandomCoordinate(), VERT_OFFSET + i * DOT_SIZE, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.25));
                if ( level == 2 ) voitureList.add(new Voiture(GetRandomCoordinate(), VERT_OFFSET + i * DOT_SIZE, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.25));
            }

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

            for ( int i : LANES )
            {
                g.setColor(Color.BLACK);
                g.fillRect(0, VERT_OFFSET + i * DOT_SIZE, B_WIDTH, DOT_SIZE);

                g.setColor(Color.YELLOW);
                g.fillRect(0, VERT_OFFSET - 1 + i * DOT_SIZE, B_WIDTH, 2);
                g.fillRect(0, VERT_OFFSET + (i+1) * DOT_SIZE, B_WIDTH, 2);
            }

            for ( Voiture voit : voitureList )
            {
                g.drawImage(spritesMap.get(voit.getType()), voit.getPosX(), voit.getPosY(), this);
            }

            for ( Collectible coll : collectibleList )
            {
                g.drawImage(spritesMap.get(coll.getType()), coll.getPosX(), coll.getPosY(), this);
            }

            if (!spawnedGoal) g.drawImage(spritesMap.get("GoalDown"), B_WIDTH / 2, VERT_OFFSET, this);
            
            g.drawImage(spritesMap.get(Integer.toString(direction)), posX, posY, this);

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, B_WIDTH, VERT_OFFSET);

            g.setFont(hudFont);
            g.setColor(FORECOLOR);
            g.drawString("Score : " + score, 0, DOT_SIZE - 1);
            g.drawString("Niveau " + (level + 1), (B_WIDTH - getFontMetrics(hudFont).stringWidth("Niveau X")) / 2, DOT_SIZE - 1);

            Toolkit.getDefaultToolkit().sync();

        } else {

            if ( level == -1 ) prompt(g, "Game Over", "Score : "+score);
            else if ( level < levels.length ) prompt(g, "Niveau " + (level + 1), "");
            else prompt(g, "Fin de jeu", "Score : "+score);

            Toolkit.getDefaultToolkit().sync();
        }        
    }

    private void prompt(Graphics g, String prompt, String subPrompt)
    {
        Font small = new Font("Helvetica", Font.BOLD, 2*DOT_SIZE);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.BLACK);
        g.fillRect(0, B_HEIGHT / 2 - 3 * DOT_SIZE, B_WIDTH, 5 * DOT_SIZE);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(prompt, (B_WIDTH - metr.stringWidth(prompt)) / 2, B_HEIGHT / 2);

        if ( subPrompt != "" ) 
        {
            g.setFont(hudFont);
            g.drawString(subPrompt, (B_WIDTH - hudMetrics.stringWidth(subPrompt)) / 2, B_HEIGHT / 2 + (int)(DOT_SIZE * 1.5));
        }
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
            collectibleList.add(new Goal(B_WIDTH / 2, VERT_OFFSET));
            spawnedGoal = true;
        }

        for ( Voiture voit : voitureList )
        {
            if ( voit.inCar(posX, posY) || voit.inCar(posX + DOT_SIZE, posY))
            {
                triggerGameOver();
            }
        }
    }

    private void triggerGameOver()
    {
        System.out.println("We losin' ");

        inGame = false;
        gameTimer.stop();

        level = -1;

        repaint();
    }

    public void triggerLevelEnd()
    {
        System.out.println("We stoppin' "+level);

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
        for ( Voiture voit : voitureList )
        {
            switch (voit.getDirection())
            {
                case LEFT:
                    voit.setPosX(voit.getPosX() - (int)(DOT_SIZE * voit.getSpeed()));
                    if (voit.getPosX() < 0 - voit.getWidth() ) voit.setPosX(B_WIDTH);
                    break;
    
                case RIGHT:
                    voit.setPosX(voit.getPosX() + (int)(DOT_SIZE * voit.getSpeed()));
                    if (voit.getPosX() > B_WIDTH) voit.setPosX(0 - voit.getWidth());
                    break;
            }
        }
    }

    private void checkCollision() {

        if (posY >= W_HEIGHT) {
            posY = W_HEIGHT - DOT_SIZE;
        }

        if (posY < VERT_OFFSET) {
            posY = VERT_OFFSET;
        }

        if (posX >= B_WIDTH) {
            posX = B_WIDTH - DOT_SIZE;
        }

        if (posX < 0) {
            posX = 0;
        }
        
        if (!inGame) {
            triggerLevelEnd();
            System.out.println("yoooo");
        }
    }

    private int GetRandomCoordinate() {

        int r = (int) (Math.random() * RAND_POS);
        return (VERT_OFFSET + r * DOT_SIZE);
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
