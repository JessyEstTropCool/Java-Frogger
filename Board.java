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

public class Board extends JPanel implements ActionListener, Idirectional 
{
    //Paramêtres
    private final String MEDIA_PATH = "20x20/";
    private final int DOT_SIZE = 20;
    private final int GRID_WIDTH = 30;
    private final int GRID_HEIGHT = 30;
    private final int HUD_HEIGHT = 2;
    private final int PROMPT_TIME = 2;
    private final int START_LIVES = 3;
    private final int LIVES_POINTS = 50;

    private final int GOAL_BAND_HEIGHT = 3;
    private final int GOAL_BAND_POS = 8;
    private final int GOAL_BAND_DOWN_HEIGHT = 2;
    private final int GOAL_BAND_DOWN_POS = 2;

    private final int WATER_EDGE_HEIGHT = 5;
    private final int FOAM_LINE_HEIGHT = 2;

    //Constantes pour le code
    private final char ROAD = 'R', GRASS = 'G', WATER = 'W';

    private final int B_WIDTH = GRID_WIDTH * DOT_SIZE;
    private final int B_HEIGHT = GRID_HEIGHT * DOT_SIZE;
    private final int VERT_OFFSET = HUD_HEIGHT * DOT_SIZE;
    private final int W_HEIGHT = B_HEIGHT + VERT_OFFSET;
    private final int DELAY = 100;
    private final int ROAD_SEPARATOR_OFFSET = DOT_SIZE / (2 - GRID_WIDTH % 2);
    private final double VERT_CENTER_TEXT = 0.75;

    private final Color BACKCOLOR = new ColorUIResource(32, 128, 16);
    private final Color FORECOLOR = Color.WHITE;

    //TODO skip ligne de goal et start
    private final Level[] LEVELS = new Level[]{
        new Level("GGGWWWWWWWWGGRRRRGGWWWWWWW", new int[]{ 4, 0, 2, 2, 2 }, 3, 1, 0, BACKCOLOR),
        new Level("GGGRRRRGGGRRRGGGRRRGGGRRRR", new int[]{ 0, 1, 0, 0, 0 }, 2, 1, 1, new ColorUIResource(32, 128, 128)),
        new Level("GGGRRRRRGRWWWWWRGRRRGGGRR", new int[]{ 6, 1, 1, 1, 1 }, 4, 2, 2, BACKCOLOR),
        new Level("GGGRRRRRRGGGGRRRRRRGGGGRRR", new int[]{ 4, 1, 2, 2, 1 }, 5, 2, 3, new ColorUIResource(128, 128, 128))
    };

    private final String[] IMAGE_FILENAMES = { 
        "headUp.png", 
        "headDown.png", 
        "headLeft.png", 
        "headRight.png", 
        "headUpInv.png", 
        "headDownInv.png", 
        "headLeftInv.png", 
        "headRightInv.png", 
        "coin.png", 
        "insectCommon.png", 
        "insectRare.png", 
        "insectUnique.png", 
        "pill.png", 
        "pillPierre.png",
        "goal.png", 
        "goalDown.png", 
        "coeur.png",
        "coeurVide.png",
        "tronc.png",
        "troncEau.png",
        "bush.png",
        "carL.png",
        "carR.png",
        "redCarL.png",
        "redCarR.png",
        "purpleCarL.png",
        "purpleCarR.png",
        "blueCarL.png",
        "blueCarR.png",
        "orangeCarL.png",
        "orangeCarR.png"
    };

    private final String[] IMAGE_KEYS = { 
        "Frogger"+UP+false,
        "Frogger"+DOWN+false, 
        "Frogger"+LEFT+false, 
        "Frogger"+RIGHT+false, 
        "Frogger"+UP+true,
        "Frogger"+DOWN+true, 
        "Frogger"+LEFT+true, 
        "Frogger"+RIGHT+true, 
        "Coin", 
        "BugCommon", 
        "BugRare", 
        "BugUnique", 
        "Pill"+false,
        "Pill"+true,
        "Goal", 
        "GoalDown",
        "Coeur",
        "CoeurVide",
        "Tronc",
        "TroncEau",
        "Buisson",
        "Voiture"+LEFT,
        "Voiture"+RIGHT,
        "Blinky"+LEFT,
        "Blinky"+RIGHT,
        "Pinky"+LEFT,
        "Pinky"+RIGHT,
        "Inky"+LEFT,
        "Inky"+RIGHT,
        "Clyde"+LEFT,
        "Clyde"+RIGHT
    };

    private final Frog frogger = new Frog(0, 0, DOT_SIZE, DOT_SIZE, UP, 2);

    private int[][] collision = new int[GRID_WIDTH][GRID_HEIGHT];

    private int coinCount;
    //private int bugCount;
    private ArrayList<Entity> collectibleList;
    private ArrayList<Voiture> voitureList;
    private ArrayList<Tronc> troncList;
    private Goal goal;

    private int level = 0;
    private int lives = START_LIVES;
    private boolean inGame = false;
    private boolean lost = false;

    private Timer gameTimer;
    private Timer promptTimer;
    private HashMap<String, Image> spritesMap;
    private Font hudFont;
    private FontMetrics hudMetrics;

    private int highScore = 0;
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

        frogger.addListener(repaint);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private ActionListener repaint = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            repaint();
        }
    };
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            if ( level < LEVELS.length )
            {
                for ( int compt = 0; compt < GRID_HEIGHT; compt++ )
                {
                    if ( compt < LEVELS[level].LAYOUT.length() && LEVELS[level].LAYOUT.charAt(compt) != GRASS)
                    {
                        switch (LEVELS[level].LAYOUT.charAt(compt))
                        {
                            case ROAD:
                                g.setColor(Color.BLACK);
                                g.fillRect(0, VERT_OFFSET + compt * DOT_SIZE, B_WIDTH, DOT_SIZE);
                
                                g.setColor(Color.YELLOW);

                                if ( compt == 0 || LEVELS[level].LAYOUT.charAt(compt-1) != ROAD )
                                    g.fillRect(0, VERT_OFFSET + compt * DOT_SIZE - 1, B_WIDTH, 2);
                                else
                                {
                                    g.setColor(Color.WHITE);

                                    for ( int compt2 = 0; compt2 < GRID_WIDTH / 2; compt2++)
                                    {
                                        g.fillRect(ROAD_SEPARATOR_OFFSET + compt2 * DOT_SIZE * 2, VERT_OFFSET + compt * DOT_SIZE - 1, DOT_SIZE, 2);
                                    }
                                }

                                g.setColor(Color.YELLOW);
                                
                                if ( compt == LEVELS[level].LAYOUT.length() - 1 || LEVELS[level].LAYOUT.charAt(compt+1) != ROAD)
                                    g.fillRect(0, VERT_OFFSET + (compt+1) * DOT_SIZE - 1, B_WIDTH, 2);
                                break;

                            case WATER:
                                g.setColor(Color.CYAN);
                                g.fillRect(0, VERT_OFFSET + compt * DOT_SIZE, B_WIDTH, DOT_SIZE);

                                if ( compt == 0 || LEVELS[level].LAYOUT.charAt(compt-1) != WATER )
                                {
                                    g.setColor(Color.decode("#7C4D26"));
                                    g.fillRect(0, VERT_OFFSET + compt * DOT_SIZE, B_WIDTH, WATER_EDGE_HEIGHT);
                                    g.setColor(Color.WHITE);
                                    g.fillRect(0, VERT_OFFSET + compt * DOT_SIZE + WATER_EDGE_HEIGHT, B_WIDTH, FOAM_LINE_HEIGHT);
                                }
                                break;
                        }
                    }
                    else
                    {
                        for ( int comptx = 0; comptx < GRID_WIDTH; comptx++ )
                        {
                            if (collision[comptx][compt] == 1) g.drawImage(spritesMap.get("Buisson"), comptx * DOT_SIZE, VERT_OFFSET + compt * DOT_SIZE, this);
                        }
                    }
                }
            }

            for ( Tronc tron : troncList )
            {
                if (tron.Collides(frogger)) g.drawImage(spritesMap.get(tron.getType()+"Eau"), tron.getPosX(), tron.getPosY(), this);
                else g.drawImage(spritesMap.get(tron.getType()), tron.getPosX(), tron.getPosY(), this);
            }

            for ( Voiture voit : voitureList )
            {
                g.drawImage(spritesMap.get(voit.getType()), voit.getPosX(), voit.getPosY(), this);
            }

            for ( Entity coll : collectibleList )
            {
                g.drawImage(spritesMap.get(coll.getType()), coll.getPosX(), coll.getPosY(), this);
            }

            if (goal.getReady())
            {
                g.setColor(Color.RED);
                g.fillRect(0, VERT_OFFSET + DOT_SIZE - GOAL_BAND_POS, B_WIDTH, GOAL_BAND_HEIGHT);
            }
            else
            {
                g.setColor(Color.GRAY);
                g.fillRect(0, VERT_OFFSET + DOT_SIZE - GOAL_BAND_DOWN_POS, B_WIDTH, GOAL_BAND_DOWN_HEIGHT);
            }

            g.drawImage(spritesMap.get(goal.getType()), 0, VERT_OFFSET, this);
            g.drawImage(spritesMap.get(goal.getType()), B_WIDTH - DOT_SIZE, VERT_OFFSET, this);
            
            g.drawImage(spritesMap.get( frogger.getType() ), frogger.getPosX(), frogger.getPosY(), this);

            //HUD

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, B_WIDTH, VERT_OFFSET);

            for ( int compt = 0 ; compt < START_LIVES ; compt++ )
            {
                if ( compt >= lives ) g.drawImage(spritesMap.get("CoeurVide"), B_WIDTH - DOT_SIZE - compt * DOT_SIZE, 0, this);
                else g.drawImage(spritesMap.get("Coeur"), B_WIDTH - DOT_SIZE - compt * DOT_SIZE, 0, this);
            }

            for ( int compt = 0 ; compt < frogger.getInvincibleTime() ; compt++ )
            {
                g.drawImage(spritesMap.get("Pill"+false), B_WIDTH - DOT_SIZE - compt * DOT_SIZE, DOT_SIZE, this);
            }

            g.setFont(hudFont);
            g.setColor(FORECOLOR);
            g.drawString("Score : " + score, 0, (int)(hudFont.getSize() * VERT_CENTER_TEXT));
            g.drawString("Meilleur score : " + highScore, 0, DOT_SIZE + (int)(hudFont.getSize() * VERT_CENTER_TEXT));
            g.drawString("Niveau " + (level + 1), (B_WIDTH - getFontMetrics(hudFont).stringWidth("Niveau X")) / 2, (int)(hudFont.getSize() * VERT_CENTER_TEXT));
            g.drawString("    x "+coinCount, (B_WIDTH - getFontMetrics(hudFont).stringWidth("    x "+coinCount)) / 2, DOT_SIZE + (int)(hudFont.getSize() * VERT_CENTER_TEXT));
            g.drawImage(spritesMap.get("Coin"), (B_WIDTH - getFontMetrics(hudFont).stringWidth("    x "+coinCount)) / 2, DOT_SIZE, this);
        } 
        else 
        {
            if ( lost ) prompt(g, "Game Over", new String[]{"Score : "+score, "Niveau : " + (level + 1)});
            else if ( level < LEVELS.length ) prompt(g, "Niveau " + (level + 1), "Vies restantes : "+lives);
            else prompt(g, "Fin de jeu", new String[]{"Vies : " + lives, "Score : "+score});
        }    
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    /*private void prompt(Graphics g, String prompt)
    {
        prompt(g, prompt, new String[0]);
    }*/

    private void prompt(Graphics g, String prompt, String subPrompt)
    {
        prompt(g, prompt, new String[]{subPrompt});
    }

    private void prompt(Graphics g, String prompt, String[] subPrompts)
    {
        Font promptFont = new Font("Helvetica", Font.BOLD, 2*DOT_SIZE);
        FontMetrics metr = getFontMetrics(promptFont);

        int totalSize = promptFont.getSize() + subPrompts.length * hudFont.getSize();
        if (subPrompts.length > 0) totalSize += hudFont.getSize();

        int strY = (W_HEIGHT - totalSize) / 2;
        
        setBackground(BACKCOLOR);

        g.setColor(Color.BLACK);
        g.fillRect(0, strY - DOT_SIZE, B_WIDTH, totalSize + DOT_SIZE * 2);

        g.setColor(FORECOLOR);
        g.setFont(promptFont);
        g.drawString(prompt, (B_WIDTH - metr.stringWidth(prompt)) / 2, (int)(strY + VERT_CENTER_TEXT * promptFont.getSize()) );

        strY += promptFont.getSize();
        if (subPrompts.length > 0) strY += hudFont.getSize();

        g.setFont(hudFont);

        for ( String line : subPrompts )
        {
            g.drawString(line, (B_WIDTH - hudMetrics.stringWidth(line)) / 2, (int)(strY + VERT_CENTER_TEXT * hudFont.getSize()) );
            strY += hudFont.getSize();
        }
    }

    private ActionListener restartGame = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            if ( score > highScore ) highScore = score;

            score = 0;
            level = 0;
            lives = START_LIVES;
            lost = false;
            
            repaint();
            
            initGame();
        }
    };
    
    private void initGame() 
    {
        System.out.println("We loadin' " + level);

        //start the prompt

        promptTimer = new Timer(PROMPT_TIME * 1000, startLevel);
        promptTimer.setRepeats(false); 

        promptTimer.start();

        repaint();

        //load the level

        goal = new Goal(0, VERT_OFFSET, B_WIDTH, DOT_SIZE, false);

        frogger.setPosX(B_WIDTH / 2);
        frogger.setPosY(W_HEIGHT - DOT_SIZE);
        frogger.setDirection(UP);
        
        coinCount = LEVELS[level].COINS;
        collectibleList = new ArrayList<Entity>();
        voitureList = new ArrayList<Voiture>();
        troncList = new ArrayList<Tronc>();
        collision = new int[GRID_WIDTH][GRID_HEIGHT];

        if ( level < LEVELS.length )
        {
            for ( int compt = 0; compt < GRID_HEIGHT; compt++ )
            {
                if ( compt < LEVELS[level].LAYOUT.length() && LEVELS[level].LAYOUT.charAt(compt) != GRASS)
                {
                    switch (LEVELS[level].LAYOUT.charAt(compt))
                    {    
                        case ROAD:
                            for ( int comptVoit = 0; comptVoit < LEVELS[level].CARS_PER_LANE; comptVoit++ )
                                voitureList.add(LEVELS[level].GetRandomCar(GetRandomXCoordinate(), VERT_OFFSET + compt * DOT_SIZE, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.25));
                            break;
    
                        case WATER:
                            int comptY = (int)(Math.random() * 3) * DOT_SIZE;
                            while ( comptY + 3 * DOT_SIZE <= B_WIDTH )
                            {
                                troncList.add(new Tronc(comptY, VERT_OFFSET + compt * DOT_SIZE, 3 * DOT_SIZE, DOT_SIZE));
                                comptY += 3 * DOT_SIZE;
                                comptY += (int)(Math.random() * 3) * DOT_SIZE;
                            }
                            break;
                    }
                }
                else
                {
                    if ( compt != 0 && compt != GRID_HEIGHT - 1 )
                    {
                        for ( int comptBush = 0; comptBush < LEVELS[level].BUSHES; comptBush++ )
                        {
                            collision[(int)(Math.random()*GRID_WIDTH)][compt] = 1;
                        }
                    }
                }
            }
        }

        /*if ( LEVELS[level].LAYOUT.indexOf(ROAD) != -1)
        {
            Voiture[] voits = new Voiture[4];
            voits[0] = new Blinky(0, 0, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.25);
            voits[1] = new Inky(0, 0, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.5);
            voits[2] = new Pinky(0, 0, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.5);
            voits[3] = new Clyde(0, 0, 2 * DOT_SIZE, DOT_SIZE, (int)(Math.random()*2) > 0 ? RIGHT : LEFT, Math.random()/2 + 0.25);

            for ( Voiture voit : voits)
            {
                placeOnRoad(voit);
                voitureList.add(voit);
            }
        }*/

        Entity ent;

        for ( int compt = 0; compt < coinCount; compt++ )
        {
            ent = new Coin(0, 0, DOT_SIZE);
            placeEntity(ent);
            collectibleList.add(ent);
        }

        for ( int compt = 0; compt < coinCount / BugCommon.getFrenquency(); compt++ )
        {
            ent = new BugCommon(0, 0, DOT_SIZE);
            placeEntity(ent);
            collectibleList.add(ent);
        }

        for ( int compt = 0; compt < coinCount / BugRare.getFrenquency(); compt++ )
        {
            ent = new BugRare(0, 0, DOT_SIZE);
            placeEntity(ent);
            collectibleList.add(ent);
        }

        for ( int compt = 0; compt < coinCount / BugUnique.getFrenquency(); compt++ )
        {
            ent = new BugUnique(0, 0, DOT_SIZE);
            placeEntity(ent);
            collectibleList.add(ent);
        }

        /*ent = new BugRare(0, 0, DOT_SIZE);
        placeEntity(ent);
        collectibleList.add(ent);

        ent = new BugUnique(0, 0, DOT_SIZE);
        placeEntity(ent);
        collectibleList.add(ent);*/

        ent = new Pill(0, 0, DOT_SIZE);
        placeEntity(ent);
        collectibleList.add(ent);

        for ( int[] col : collision )
        {
            for ( int i : col )
            {
                System.out.print(i+" ");
            }

            System.out.println();
        }
    }

    private ActionListener startLevel = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            inGame = true;
            System.out.println("We startin' " + level);
            promptTimer.stop();

            setBackground(LEVELS[level].BACKCOLOR);

            gameTimer.start();
            repaint();
        }
    };

    public void triggerLevelEnd()
    {
        System.out.println("We stoppin' "+level);

        inGame = false;
        gameTimer.stop();

        frogger.resetInvincible();

        level++;

        if ( level < LEVELS.length ) initGame();
        else 
        {
            score += lives * LIVES_POINTS;

            repaint();

            promptTimer = new Timer(PROMPT_TIME * 1000, restartGame);
            promptTimer.setRepeats(false); 
            promptTimer.start();
        }
    }

    public void loseLife()
    {
        inGame = false;
        gameTimer.stop();

        frogger.resetInvincible();

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

            promptTimer = new Timer(PROMPT_TIME * 1000, restartGame);
            promptTimer.setRepeats(false); 
            promptTimer.start();
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

                ((Itriggerable)coll).triggerAction(this);
            }
        }

        if ( coinCount <= 0 && !goal.getReady() ) goal.setReady(true);
            
        if ( frogger.Collides(goal) && goal.getReady() )
        {
            SendToVoid(goal);
            goal.triggerAction(this);
        }

        for ( Voiture voit : voitureList )
        {
            if ( frogger.Collides(voit) )
            {
                ((Itriggerable)voit).triggerAction(this);
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

    public boolean isRoad(int posY)
    {
        return terrainTest(posY, ROAD);
    }

    public boolean isWater(int posY)
    {
        return terrainTest(posY, WATER);
    }

    private boolean terrainTest(int posY, char material)
    {
        int gridY = ((posY - VERT_OFFSET) / DOT_SIZE);
        return gridY >= 0 && gridY < LEVELS[level].LAYOUT.length() && LEVELS[level].LAYOUT.charAt(gridY) == material;
    }

    public void alignY(Entity ent)
    {
        ent.setPosY(ent.getPosY() - ent.getPosY() % DOT_SIZE);
    }

    public void alignX(Entity ent)
    {
        ent.setPosX(ent.getPosX() - ent.getPosX() % DOT_SIZE);
    }

    public Frog getFrogger()
    {
        return frogger;
    }

    public void incScore(int amount)
    {
        if ( frogger.isInvincible() ) amount *= 2;
        score += amount;
    }

    public void decCoinCount()
    {
        coinCount--;
    }

    public double getCompletion()
    {
        return (LEVELS[level].COINS - coinCount) * 1.0 / LEVELS[level].COINS;
    }

    public boolean onTronc(Entity ent)
    {
        for ( Tronc tron : troncList )
        {
            if ( ent.Collides(tron) ) return true;
        }

        return false;
    }

    public boolean isWall(int posX, int posY)
    {
        posX /= DOT_SIZE;
        posY = (posY - VERT_OFFSET) / DOT_SIZE;

        if ( posX < 0 || posX >= GRID_WIDTH || posY < 0 || posY >= GRID_HEIGHT ) return true;

        if ( collision[posX][posY] != 0 ) return true;

        return false;
    }

    private void move() {

        //si frogger n'est pas aligné alors qu'il a une vitesse normale, on tente de le réaligner en donnant une vitesse moindre
        if ( ( (frogger.getPosY() % DOT_SIZE != 0) || (frogger.getPosX() % DOT_SIZE != 0 ) )
        && !frogger.isInvincible() )
        {
            switch ( frogger.getDirection() )
            {
                case LEFT:
                    if ( frogger.getPosX() % (DOT_SIZE * frogger.getSpeed()) != 0 )
                    {
                        alignX(frogger);
                    }
                    break;

                case RIGHT:
                    if ( frogger.getPosX() % (DOT_SIZE * frogger.getSpeed()) != 0 )
                    {
                        frogger.setPosX(frogger.getPosX() + DOT_SIZE);
                        alignX(frogger);
                    }
                    break;

                case UP:
                    if ( frogger.getPosY() % (DOT_SIZE * frogger.getSpeed()) != 0 )
                    {
                        alignY(frogger);
                    }
                    break;

                case DOWN:
                    if ( frogger.getPosY() % (DOT_SIZE * frogger.getSpeed()) != 0 )
                    {
                        frogger.setPosY(frogger.getPosY() + DOT_SIZE);
                        alignY(frogger);
                    }
                    break;
            }
        }
        else frogger.Move(DOT_SIZE, this);

        if (frogger.getPosY() + frogger.getHeight() >= W_HEIGHT) {
            frogger.setPosY(W_HEIGHT - DOT_SIZE);
        }

        if (frogger.getPosY() < VERT_OFFSET) {
            frogger.setPosY(VERT_OFFSET);
        }

        if (frogger.getPosX() + frogger.getWidth() >= B_WIDTH) {
            frogger.setPosX(B_WIDTH - DOT_SIZE);
        }

        if (frogger.getPosX() < 0) {
            frogger.setPosX(0);
        }

        if ( isWater(frogger.getPosY()) && isWater(frogger.getPosY() + frogger.getHeight() - 1) && !onTronc(frogger) ) loseLife();
    }

    private int GetRandomXCoordinate() {

        int r = (int) (Math.random() * (GRID_WIDTH - 1));
        return (r * DOT_SIZE);
    }

    private int GetRandomYCoordinate() {

        int r = (int) (Math.random() * (GRID_HEIGHT - 1)) * DOT_SIZE;
        return VERT_OFFSET + r;
    }

    private void placeEntity(Entity ent)
    {
        do
        {
            ent.setPosX(GetRandomXCoordinate());
            ent.setPosY(GetRandomYCoordinate());
        } 
        while ( (!onTronc(ent) && isWater(ent.getPosY())) || ent.Collides(goal) );
    }

    /*private void placeOnRoad(Entity ent)
    {
        ent.setPosX(GetRandomXCoordinate());

        do
        {
            ent.setPosY(GetRandomYCoordinate());
        } 
        while ( !isRoad(ent.getPosY()) );
    }*/

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (inGame)
            {
                int key = e.getKeyCode();
                boolean actionDone = false;

                if (key == KeyEvent.VK_LEFT) {
                    frogger.setDirection(LEFT);
                    actionDone = true;
                }
    
                if (key == KeyEvent.VK_RIGHT) {
                    frogger.setDirection(RIGHT);
                    actionDone = true;
                }
    
                if (key == KeyEvent.VK_UP) {
                    frogger.setDirection(UP);
                    actionDone = true;
                }
    
                if (key == KeyEvent.VK_DOWN) {
                    frogger.setDirection(DOWN);
                    actionDone = true;
                }

                if (actionDone)
                {
                    move();
                    checkCollectibles();
                    repaint();
                }
            }
        }
    }
}
