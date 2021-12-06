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
    private final int HUD_HEIGHT = 2;
    private final int INVINCIBLE_TIME = 10;
    private final int PROMPT_TIME = 2;
    private final int START_LIVES = 3;

    private final String[] LEVEL_LAYOUTS = {
        "GGGGRRRGGGRRRGGGRRRGGGRRR",
        "GGGGRRRRRGRRRGRRRGRRRGGGRR",
        "GGGGRRRRRRGGGGRRRRRRGGGGRRR"
    };

    //Constantes pour le code
    private final int LEFT = 0, UP = 1, RIGHT = 2, DOWN = 3;
    private final char ROAD = 'R', GRASS = 'G', WATER = 'W';

    private final int B_WIDTH = GRID_WIDTH * DOT_SIZE;
    private final int B_HEIGHT = GRID_HEIGHT * DOT_SIZE;
    private final int VERT_OFFSET = HUD_HEIGHT * DOT_SIZE;
    private final int W_HEIGHT = B_HEIGHT + VERT_OFFSET;
    //private final int RAND_POS = GRID_WIDTH - 1;
    private final int DELAY = 100;
    private final double VERT_CENTER_TEXT = 0.75;

    private final Color BACKCOLOR = new ColorUIResource(32, 128, 16);
    private final Color FORECOLOR = Color.WHITE;

    private final String[] IMAGE_FILENAMES = { 
        Coin.getPathToImage(), 
        Bug.getPathToImage(), 
        Pill.getPathToImage(), 
        Goal.getPathToImage(), 
        "goalDown.png", 
        "headUp.png", 
        "headDown.png", 
        "headLeft.png", 
        "headRight.png", 
        "coeur.png",
        "car.png",
        "redCar.png",
        "purpleCar.png",
        "blueCar.png",
        "orangeCar.png",
    };

    private final String[] IMAGE_KEYS = { 
        "Coin", 
        "Bug", 
        "Pill",
        "Goal", 
        "GoalDown",
        Integer.toString(UP),
        Integer.toString(DOWN), 
        Integer.toString(LEFT), 
        Integer.toString(RIGHT), 
        "Coeur",
        "Voiture",
        "Blinky",
        "Pinky",
        "Inky",
        "Clyde"
    };

    private final Frog frogger = new Frog(0, 0, DOT_SIZE, DOT_SIZE, UP, 1);

    private int coinCount;
    private int bugCount;
    private ArrayList<Entity> collectibleList;
    private ArrayList<Voiture> voitureList;
    private Goal goal;

    private int level = 0;
    private int lives = START_LIVES;
    private boolean inGame = false;
    private boolean spawnedGoal = false;
    private boolean lost = false;

    private final int[] levels = { 1, 2, 3 };

    private Timer gameTimer;
    private Timer introTimer;
    private Timer invincTimer;
    private HashMap<String, Image> spritesMap;
    private Font hudFont;
    private FontMetrics hudMetrics;

    private int highScore = 0;
    private int score = 0;
    private int voidX = -1*B_WIDTH;
    private int voidY = -1*B_HEIGHT;
    private int invincSeconds = 0;

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

        introTimer = new Timer(PROMPT_TIME * 1000, loadNextLevel);
        introTimer.setRepeats(false); 

        invincTimer = new Timer(1000, invincibleAction);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            if ( level < LEVEL_LAYOUTS.length )
            {
                for ( int compt = 0; compt < LEVEL_LAYOUTS[level].length(); compt++ )
                {
                    switch (LEVEL_LAYOUTS[level].charAt(compt))
                    {
                        case GRASS:
                            break;
    
                        case ROAD:
                            g.setColor(Color.BLACK);
                            g.fillRect(0, VERT_OFFSET + compt * DOT_SIZE, B_WIDTH, DOT_SIZE);
            
                            g.setColor(Color.YELLOW);
                            g.fillRect(0, VERT_OFFSET - 1 + compt * DOT_SIZE, B_WIDTH, 2);
                            g.fillRect(0, VERT_OFFSET + (compt+1) * DOT_SIZE, B_WIDTH, 2);
                            break;

                        case WATER:
                            g.setColor(Color.CYAN);
                            g.fillRect(0, VERT_OFFSET + compt * DOT_SIZE, B_WIDTH, DOT_SIZE);
                            break;
                    }
                }
            }

            for ( Voiture voit : voitureList )
            {
                g.drawImage(spritesMap.get(voit.getType()), voit.getPosX(), voit.getPosY(), this);
            }

            for ( Entity coll : collectibleList )
            {
                g.drawImage(spritesMap.get(coll.getType()), coll.getPosX(), coll.getPosY(), this);
            }

            if (spawnedGoal)
            {
                g.setColor(Color.RED);
                g.fillRect(0, VERT_OFFSET + DOT_SIZE - 3, B_WIDTH, 2);

                g.drawImage(spritesMap.get("Goal"), 0, VERT_OFFSET, this);
                g.drawImage(spritesMap.get("Goal"), B_WIDTH - DOT_SIZE, VERT_OFFSET, this);
            }
            else
            {
                g.setColor(Color.GRAY);
                g.fillRect(0, VERT_OFFSET + DOT_SIZE - 1, B_WIDTH, 1);

                g.drawImage(spritesMap.get("GoalDown"), 0, VERT_OFFSET, this);
                g.drawImage(spritesMap.get("GoalDown"), B_WIDTH - DOT_SIZE, VERT_OFFSET, this);
            }
            
            g.drawImage(spritesMap.get( Integer.toString( frogger.getDirection() ) ), frogger.getPosX(), frogger.getPosY(), this);

            //HUD

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, B_WIDTH, VERT_OFFSET);

            g.setFont(hudFont);
            g.setColor(FORECOLOR);
            g.drawString("Score : " + score, 0, (int)(DOT_SIZE * VERT_CENTER_TEXT));
            g.drawString("Meilleur score : " + highScore, 0, DOT_SIZE + (int)(DOT_SIZE * VERT_CENTER_TEXT));
            g.drawString("Niveau " + (level + 1), (B_WIDTH - getFontMetrics(hudFont).stringWidth("Niveau X")) / 2, (int)(DOT_SIZE * VERT_CENTER_TEXT));

            for ( int compt = 0 ; compt < lives ; compt++ )
            {
                g.drawImage(spritesMap.get("Coeur"), B_WIDTH - DOT_SIZE - compt * DOT_SIZE, 0, this);
            }

            for ( int compt = 0 ; compt < invincSeconds ; compt++ )
            {
                g.drawImage(spritesMap.get("Pill"), B_WIDTH - DOT_SIZE - compt * DOT_SIZE, DOT_SIZE, this);
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            if ( lost ) prompt(g, "Game Over", new String[]{"", "Score : "+score, "Niveau : " + (level + 1)});
            else if ( level < levels.length ) prompt(g, "Niveau " + (level + 1), "Vies restantes : "+lives);
            else prompt(g, "Fin de jeu", "Score : "+score);

            Toolkit.getDefaultToolkit().sync();
        }        
    }
    
    private void prompt(Graphics g, String prompt)
    {
        prompt(g, prompt, new String[0]);
    }

    private void prompt(Graphics g, String prompt, String subPrompt)
    {
        prompt(g, prompt, new String[]{subPrompt});
    }

    private void prompt(Graphics g, String prompt, String[] subPrompts)
    {
        Font promptFont = new Font("Helvetica", Font.BOLD, 2*DOT_SIZE);
        FontMetrics metr = getFontMetrics(promptFont);
        int totalSize = promptFont.getSize() + subPrompts.length * hudFont.getSize();
        int strY = (W_HEIGHT - totalSize) / 2;

        g.setColor(Color.BLACK);
        g.fillRect(0, strY - DOT_SIZE, B_WIDTH, totalSize + DOT_SIZE * 2);

        g.setColor(FORECOLOR);
        g.setFont(promptFont);
        g.drawString(prompt, (B_WIDTH - metr.stringWidth(prompt)) / 2, (int)(strY + VERT_CENTER_TEXT * promptFont.getSize()) );

        strY += promptFont.getSize();
        g.setFont(hudFont);

        for ( String line : subPrompts )
        {
            g.drawString(line, (B_WIDTH - hudMetrics.stringWidth(line)) / 2, (int)(strY + VERT_CENTER_TEXT * hudFont.getSize()) );
            strY += hudFont.getSize();
        }
    }

    private void restartGame()
    {
        if ( score > highScore ) highScore = score;

        score = 0;
        level = 0;
        lives = START_LIVES;
        lost = false;

        initGame();
    }
    
    private void initGame() 
    {
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

            goal = null;

            frogger.setPosX(B_WIDTH / 2);
            frogger.setPosY(W_HEIGHT - DOT_SIZE);
            
            coinCount = levels[level];
            bugCount = levels[level] / 2 + 1;
            collectibleList = new ArrayList<Entity>();
            voitureList = new ArrayList<Voiture>();

            if ( level < LEVEL_LAYOUTS.length )
            {
                for ( int compt = 0; compt < LEVEL_LAYOUTS[level].length(); compt++ )
                {
                    switch (LEVEL_LAYOUTS[level].charAt(compt))
                    {
                        case GRASS:
                            break;
    
                        case ROAD:
                            voitureList.add(new Voiture(GetRandomXCoordinate(), VERT_OFFSET + compt * DOT_SIZE, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.25));
                            if ( level == 2 ) voitureList.add(new Voiture(GetRandomXCoordinate(), VERT_OFFSET + compt * DOT_SIZE, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.25));
                            break;
                    }
                }
            }

            voitureList.add(new Blinky(GetRandomXCoordinate(), VERT_OFFSET + 10 * DOT_SIZE, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, 0.6));

            for ( int compt = 0; compt < coinCount; compt++ )
                collectibleList.add(new Coin(GetRandomXCoordinate(), VERT_OFFSET + GetRandomYCoordinate(), DOT_SIZE));

            for ( int compt = 0; compt < bugCount; compt++ )
                collectibleList.add(new Bug(GetRandomXCoordinate(), VERT_OFFSET + GetRandomYCoordinate(), DOT_SIZE));

            collectibleList.add(new Pill(GetRandomXCoordinate(), VERT_OFFSET + GetRandomYCoordinate(), DOT_SIZE));

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

    public void triggerLevelEnd()
    {
        System.out.println("We stoppin' "+level);

        inGame = false;
        gameTimer.stop();

        if ( invincTimer.isRunning() )
        {
            invincTimer.stop();
            invincSeconds = 0;
        }

        level++;

        if ( level < levels.length ) initGame();
        else 
        {
            repaint();
            restartGame();
        }
    }

    public void loseLife()
    {
        inGame = false;
        gameTimer.stop();

        if ( invincTimer.isRunning() )
        {
            invincTimer.stop();
            invincSeconds = 0;
        }

        if ( lives > 0 ) 
        {
            lives--;
            initGame();

            System.out.println("We losin' ");
        }
        else 
        {
            lost = true;
            repaint();

            System.out.println("We actually losin' ");

            restartGame();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) 
        {
            checkCollectibles();
            moveVoiture();
        }

        repaint();
    }

    private void checkCollectibles() {

        for (Entity coll : collectibleList)
        {
            if (frogger.Collides(coll)) {
    
                SendToVoid(coll);

                coll.triggerAction(this);
            }
        }

        if ( coinCount <= 0 )
        {
            if ( !spawnedGoal ) 
            {
                goal = new Goal(0, VERT_OFFSET, B_WIDTH, DOT_SIZE);
                spawnedGoal = true;
            }
            
            if ( frogger.Collides(goal) )
            {
                SendToVoid(goal);
                goal.triggerAction(this);
            }
        }

        for ( Voiture voit : voitureList )
        {
            if ( frogger.Collides(voit) )
            {
                if ( invincSeconds > 0 )
                {
                    voit.setDirection(-1);
                    voit.setPosX(voidX);
                    voit.setPosY(voidY);

                    incScore(2);
                }
                else loseLife();
            }
        }
    }

    private void moveVoiture()
    {
        for ( Voiture voit : voitureList )
        {
            voit.Move(DOT_SIZE, this);

            switch (voit.getDirection())
            {
                case LEFT:
                    if (voit.getPosX() < 0 - voit.getWidth() ) voit.setPosX(B_WIDTH);
                    break;
    
                case RIGHT:
                    if (voit.getPosX() > B_WIDTH) voit.setPosX(0 - voit.getWidth());
                    break;
            }
        }
    }

    public void SendToVoid(Entity victim)
    {
        victim.setPosX(voidX);
        victim.setPosY(voidY);
    }

    public void triggerInvincible()
    {
        invincSeconds = INVINCIBLE_TIME;
        invincTimer.start();
        frogger.setSpeed(0.5);
    }

    public boolean isRoad(int posY)
    {
        int gridY = ((posY - VERT_OFFSET) / DOT_SIZE);
        return gridY >= 0 && gridY < LEVEL_LAYOUTS[level].length() && LEVEL_LAYOUTS[level].charAt(gridY) == ROAD;
    }

    public void alignY(Entity ent)
    {
        ent.setPosY(ent.getPosY() - ent.getPosY() % DOT_SIZE);
    }
    
    private ActionListener invincibleAction = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            invincSeconds--;
            
            if ( invincSeconds <= 0 )
            {
                frogger.setSpeed(1);
                invincTimer.stop();
            }

            repaint();
        }
    };

    public boolean isInvincible()
    {
        return invincSeconds > 0;
    }

    public MovingEntity getFrogger()
    {
        return frogger;
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

        if ( ( (frogger.getPosY() % DOT_SIZE != 0 && (frogger.getDirection() == UP || frogger.getDirection() == DOWN)) 
        || (frogger.getPosX() % DOT_SIZE != 0 && (frogger.getDirection() == LEFT || frogger.getDirection() == RIGHT)) )
        && invincSeconds <= 0 )
        {
            frogger.Move(DOT_SIZE / 2, this);
        }
        else frogger.Move(DOT_SIZE, this);

        if (frogger.getPosY() >= W_HEIGHT) {
            frogger.setPosY(W_HEIGHT - DOT_SIZE);
        }

        if (frogger.getPosY() < VERT_OFFSET) {
            frogger.setPosY(VERT_OFFSET);
        }

        if (frogger.getPosX() >= B_WIDTH) {
            frogger.setPosX(B_WIDTH - DOT_SIZE);
        }

        if (frogger.getPosX() < 0) {
            frogger.setPosX(0);
        }
    }

    private int GetRandomXCoordinate() {

        int r = (int) (Math.random() * (GRID_WIDTH - 1));
        return (r * DOT_SIZE);
    }

    private int GetRandomYCoordinate() {

        int r = (int) (Math.random() * (GRID_HEIGHT - 1));
        return (VERT_OFFSET + r * DOT_SIZE);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (inGame)
            {
                int key = e.getKeyCode();

                if (key == KeyEvent.VK_LEFT) {
                    frogger.setDirection(LEFT);
                }
    
                if (key == KeyEvent.VK_RIGHT) {
                    frogger.setDirection(RIGHT);
                }
    
                if (key == KeyEvent.VK_UP) {
                    frogger.setDirection(UP);
                }
    
                if (key == KeyEvent.VK_DOWN) {
                    frogger.setDirection(DOWN);
                }
    
                move();
                checkCollectibles();
                repaint();
            }
        }
    }
}
