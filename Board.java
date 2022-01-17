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

public class Board extends JPanel implements ActionListener, Idirectional 
{
    private final char ROAD = 'R', GRASS = 'G', WATER = 'W';

    private final String MEDIA_PATH = "20x20/";
    private final int DOT_SIZE = 20;
    private final int GRID_WIDTH = 30;
    private final int GRID_HEIGHT = 30;
    private final int PROMPT_TIME = 2;
    private final int START_LIVES = 3;
    private final int LIVES_POINTS = 50;
    private final int DELAY = 100;
    private final int INITIAL_TIME_BONUS = 60;
    private final int TIME_BONUS_PENALTY = 1;

    private final int GOAL_BAND_HEIGHT = 3;
    private final int GOAL_BAND_POS = 8;
    private final int GOAL_BAND_DOWN_HEIGHT = 2;
    private final int GOAL_BAND_DOWN_POS = 2;

    private final int TRONC_GRID_WIDTH = 3;

    private final int WATER_EDGE_HEIGHT = DOT_SIZE / 4;
    private final int FOAM_LINE_HEIGHT = DOT_SIZE / 10;
    private final int ROAD_SEPARATOR_OFFSET = DOT_SIZE / (2 - GRID_WIDTH % 2);
    private final int ROAD_SEPARATOR_HEIGHT = 2;
    private final double VERT_CENTER_TEXT = 0.75;

    private final int HUD_HEIGHT = 2*DOT_SIZE;
    private final int B_WIDTH = GRID_WIDTH * DOT_SIZE;
    private final int B_HEIGHT = GRID_HEIGHT * DOT_SIZE;
    private final int W_HEIGHT = B_HEIGHT + HUD_HEIGHT;
    private final int VOID_X = -1*B_WIDTH;
    private final int VOID_Y = -1*B_HEIGHT;

    private final Color BACKCOLOR = Color.decode("#208010");
    private final Color FORECOLOR = Color.WHITE;
    private final Color WATER_EDGE_COLOR = Color.decode("#7C4D26");

    private final Level[] LEVELS = new Level[]{
        new Level("Route de campagne", "GGGWWWWWWWWGGRRRRGGWWWWWWW", new int[]{ 40, 0, 20, 20, 20 }, 3, 1, 1, 0.1, 0.25, BACKCOLOR),
        new Level("Au milieu d'un champ de blé", "GGGRRRGGWWGGRRRRGGGRRR", new int[]{ 60, 10, 10, 10, 10 }, 4, 1, 2, 0.25, 0.25, Color.decode("#E5CA54")),
        new Level("Attention, sortie d'école", "GGGRRRRGGGGRRRRGGGGRRRR", new int[]{ 10, 0, 0, 90, 0 }, 5, 2, 3, 0.25, 0.25, Color.decode("#208080")),
        new Level("Salon de l'automobile particulière", "GGGRRRGGRRRGGRRRGGRRRGGRRR", new int[]{ 0, 25, 25, 25, 25 }, 5, 1, 2, 0.25, 0.25, Color.decode("#808080")),
        new Level("Autour de l'étang", "GGGRRRRRGRWWWWWRGRRRGGGRR", new int[]{ 60, 10, 10, 10, 10 }, 4, 2, 2, 0.25, 0.25, Color.decode("#208000")),
        new Level("Embouteillage sur l'autoroute", "GGGRRRRRRRRGGGRRRRRRRR", new int[]{ 20, 20, 20, 20, 20 }, 10, 3, 3, 0.1, 0.2, BACKCOLOR),
        new Level("Avenue Argent Argenté d'Argentine", "GGGRRRRGGGRRRGGGRRRGGGRRRR", new int[]{ 0, 0, 100, 0, 0 }, 9, 2, 1, 0.25, 0.5, Color.decode("#3D0072")),
        new Level("Petite pause à la plage", "WWWWWWWWGGGGGGGRRRRGRRRR", new int[]{ 30, 10, 10, 40, 10 }, 6, 2, 5, 0.5, 0.25, Color.decode("#C7C771")),
        new Level("Circuit de F1", "GGGRRRRGGRRRRGGRRRRGGRRRR", new int[]{ 0, 100, 0, 0, 0 }, 2, 2, 1, 0.5, 0.25, Color.decode("#7F2020")),
        new Level("Centre-ville", "GGGRRRRRRGGGGRRRRRRGGGGRRR", new int[]{ 40, 10, 20, 20, 10 }, 5, 2, 3, 0.25, 0.5, Color.decode("#808080")),
        new Level("Rue random", "GGGRRRRGGRRRRGGRRRRGGRRRR", new int[]{ 0, 0, 0, 0, 100 }, 2, 2, 1, 0.1, 0.9, Color.decode("#FF6A20")),
        new Level("Congestion urbaine", "GGGRRRRRGGRRRRRGGRRRRR", new int[]{ 20, 20, 20, 20, 20 }, 6, 3, 3, 0.25, 0.5, Color.decode("#808080")),
        new Level("Bonus, go go go !", "GGGWWWWWGGGRRGGGWWWWWGGG", new int[]{ 20, 20, 20, 20, 20 }, 30, 3, 5, 0.5, 0.5, Color.decode("#80FF80"))
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
        "bushFleur.png",
        "bushFruit.png",
        "carL.png",
        "carR.png",
        "redCarL.png",
        "redCarR.png",
        "purpleCarL.png",
        "purpleCarR.png",
        "blueCarL.png",
        "blueCarR.png",
        "orangeCarL.png",
        "orangeCarR.png",
        "OVNI.png"
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
        "Goal"+true, 
        "Goal"+false,
        "Coeur",
        "CoeurVide",
        "Tronc",
        "TroncEau",
        "Bush"+0,
        "Bush"+1,
        "Bush"+2,
        "Voiture"+LEFT,
        "Voiture"+RIGHT,
        "Blinky"+LEFT,
        "Blinky"+RIGHT,
        "Pinky"+LEFT,
        "Pinky"+RIGHT,
        "Inky"+LEFT,
        "Inky"+RIGHT,
        "Clyde"+LEFT,
        "Clyde"+RIGHT,
        "Ovni"
    };

    private final Frog frogger = new Frog(0, 0, DOT_SIZE, DOT_SIZE, UP, 1);

    private ArrayList<Entity> collectibleList;
    private ArrayList<MovingEntity> voitureList;
    private ArrayList<Tronc> troncList;
    private ArrayList<Entity> obstacleList;
    private Goal goal;

    private int level = 0;
    private int lives = START_LIVES;
    private boolean inGame = false;
    private boolean lost = false;
    private boolean noMoreTime = false;

    private Timer gameTimer = new Timer(DELAY, this);;
    private Timer promptTimer;
    private HashMap<String, Image> spritesMap;
    private Font hudFont = new Font("Helvetica", Font.BOLD, DOT_SIZE);
    private FontMetrics hudMetrics = getFontMetrics(hudFont);

    private int highScore = 0;
    private int score = 0;
    private int secondsCounter = 0;
    private int timeBonus;
    private int coinCount;

    public Board() 
    {        
        initBoard();
    }
    
    private void initBoard() 
    {
        addKeyListener(new TAdapter());
        setBackground(BACKCOLOR);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, W_HEIGHT));
        frogger.addListener(repaint);
        loadImages();
        initGame();
    }

    //charge les graphismes du jeu tel que IMAGE_KEYS -> IMAGE_FILENAMES dans spritesMap, Attention pour éviter les erreurs ils doivent être de même longueur et dans le même ordre
    private void loadImages() 
    {
        spritesMap = new HashMap<String, Image>();
        ImageIcon ii;

        for ( int compt = 0; compt < IMAGE_FILENAMES.length; compt++ )
        {
            ii = new ImageIcon(MEDIA_PATH+IMAGE_FILENAMES[compt]);
            spritesMap.put(IMAGE_KEYS[compt], ii.getImage());
        }
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        doDrawing(g);
    }

    //listener servant a repeindre le board avec un timer, utilisé avec le timer Frog, pour afficher la progression de l'invincibilité
    private ActionListener repaint = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            repaint();
        }
    };
    
    //fonction principale d'affichage graphique, affiche le jeu si il y a partie en cours, sinon affiche un prompt adéquoi
    private void doDrawing(Graphics g)
    {        
        //la variable inGame dicte si une partie est en cours
        if (inGame) 
        {
            //partie en cours

            if ( level < LEVELS.length ) //ne devrait jamais arriver mais on vérifie si on dépasse le nombre de niveaux préparé
            {
                for ( int compt = 0; compt < GRID_HEIGHT; compt++ )
                {
                    //on considère que l'herbe est la même chose que ce qui remplit ce qui n'est pas couvert par le layout du niveau (où ici on ne doit rien afficher de plus)
                    if ( compt < LEVELS[level].getLayout().length() && LEVELS[level].getLayout().charAt(compt) != GRASS) 
                    {
                        switch (LEVELS[level].getLayout().charAt(compt))
                        {
                            case ROAD:
                                g.setColor(Color.BLACK);
                                g.fillRect(0, HUD_HEIGHT + compt * DOT_SIZE, B_WIDTH, DOT_SIZE);
                
                                g.setColor(Color.YELLOW);

                                if ( compt == 0 || LEVELS[level].getLayout().charAt(compt-1) != ROAD ) //ligne jaune au dessus si on commence la route
                                    g.fillRect(0, HUD_HEIGHT + compt * DOT_SIZE - ROAD_SEPARATOR_HEIGHT / 2, B_WIDTH, ROAD_SEPARATOR_HEIGHT);
                                else                                                                    //sinon on fait un séparateur fait de plusieurs lignes blanches
                                {
                                    g.setColor(Color.WHITE);

                                    for ( int compt2 = 0; compt2 < GRID_WIDTH / 2; compt2++)
                                    {
                                        g.fillRect(ROAD_SEPARATOR_OFFSET + compt2 * DOT_SIZE * 2, HUD_HEIGHT + compt * DOT_SIZE - ROAD_SEPARATOR_HEIGHT / 2, DOT_SIZE, ROAD_SEPARATOR_HEIGHT);
                                    }
                                }

                                if ( compt == LEVELS[level].getLayout().length() - 1 || LEVELS[level].getLayout().charAt(compt+1) != ROAD) //ligne jaune en dessous, si on fini la route
                                {
                                    g.setColor(Color.YELLOW);
                                    g.fillRect(0, HUD_HEIGHT + (compt+1) * DOT_SIZE - ROAD_SEPARATOR_HEIGHT / 2, B_WIDTH, ROAD_SEPARATOR_HEIGHT);
                                }
                                break;

                            case WATER:
                                g.setColor(Color.CYAN);
                                g.fillRect(0, HUD_HEIGHT + compt * DOT_SIZE, B_WIDTH, DOT_SIZE);

                                if ( compt != 0 && LEVELS[level].getLayout().charAt(compt-1) != WATER ) //si on commence une rivière, on marque le bord
                                {
                                    g.setColor(WATER_EDGE_COLOR);
                                    g.fillRect(0, HUD_HEIGHT + compt * DOT_SIZE, B_WIDTH, WATER_EDGE_HEIGHT);
                                    g.setColor(Color.WHITE);
                                    g.fillRect(0, HUD_HEIGHT + compt * DOT_SIZE + WATER_EDGE_HEIGHT, B_WIDTH, FOAM_LINE_HEIGHT);
                                }
                                break;
                        }
                    }
                }
            }

            //On affiche toutes les entités
            for ( Entity obs : obstacleList )
            {
                g.drawImage(spritesMap.get(obs.getType()), obs.getDisplayX(), obs.getDisplayY(), this);
            }

            for ( Tronc tron : troncList )
            {
                if (tron.collides(frogger)) g.drawImage(spritesMap.get(tron.getType()+"Eau"), tron.getDisplayX(), tron.getDisplayY(), this);
                else g.drawImage(spritesMap.get(tron.getType()), tron.getDisplayX(), tron.getDisplayY(), this);
            }
            
            g.drawImage(spritesMap.get( frogger.getType() ), frogger.getDisplayX(), frogger.getDisplayY(), this);

            for ( MovingEntity voit : voitureList )
            {
                g.drawImage(spritesMap.get(voit.getType()), voit.getDisplayX(), voit.getDisplayY(), this);
            }

            for ( Entity coll : collectibleList )
            {
                g.drawImage(spritesMap.get(coll.getType()), coll.getDisplayX(), coll.getDisplayY(), this);
            }

            //ligne du goal
            if (goal.getReady())
            {
                //goal activé
                g.setColor(Color.RED);
                g.fillRect(0, HUD_HEIGHT + DOT_SIZE - GOAL_BAND_POS, B_WIDTH, GOAL_BAND_HEIGHT);
            }
            else
            {
                //goal déactivé
                g.setColor(Color.GRAY);
                g.fillRect(0, HUD_HEIGHT + DOT_SIZE - GOAL_BAND_DOWN_POS, B_WIDTH, GOAL_BAND_DOWN_HEIGHT);
            }

            g.drawImage(spritesMap.get(goal.getType()), 0, HUD_HEIGHT, this);
            g.drawImage(spritesMap.get(goal.getType()), B_WIDTH - DOT_SIZE, HUD_HEIGHT, this);

            //HUD, affiché en dernier pour qu'il soit toujours au dessus

            //background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, B_WIDTH, HUD_HEIGHT);

            //texte et icones
            g.setFont(hudFont);
            g.setColor(FORECOLOR);

            g.drawString("Score : " + score, 0, (int)(hudFont.getSize() * VERT_CENTER_TEXT));
            g.drawString("Niveau " + (level + 1) + " - Bonus : " + timeBonus, (B_WIDTH - getFontMetrics(hudFont).stringWidth("Niveau X - Bonus : " + INITIAL_TIME_BONUS)) / 2, (int)(hudFont.getSize() * VERT_CENTER_TEXT));

            for ( int compt = 0 ; compt < START_LIVES ; compt++ )
            {
                if ( compt >= lives ) g.drawImage(spritesMap.get("CoeurVide"), B_WIDTH - DOT_SIZE - compt * DOT_SIZE, 0, this);
                else g.drawImage(spritesMap.get("Coeur"), B_WIDTH - DOT_SIZE - compt * DOT_SIZE, 0, this);
            }

            g.drawString("Meilleur score : " + highScore, 0, DOT_SIZE + (int)(hudFont.getSize() * VERT_CENTER_TEXT));
            g.drawString("    x "+coinCount, (B_WIDTH - getFontMetrics(hudFont).stringWidth("    x "+coinCount)) / 2, DOT_SIZE + (int)(hudFont.getSize() * VERT_CENTER_TEXT));
            g.drawImage(spritesMap.get("Coin"), (B_WIDTH - getFontMetrics(hudFont).stringWidth("    x "+coinCount)) / 2, DOT_SIZE, this);

            for ( int compt = 0 ; compt < frogger.getInvincibleTime() ; compt++ )
            {
                g.drawImage(spritesMap.get("Pill"+false), B_WIDTH - DOT_SIZE - compt * DOT_SIZE, DOT_SIZE, this);
            }
        } 
        else 
        {
            //si on est pas en partie, on affiche un Game Over, le prochain niveau, ou la fin du jeu
            if ( lost ) prompt(g, "Game Over", new String[]{"Score : "+score, "Niveau : " + (level + 1)});
            else if ( level < LEVELS.length ) prompt(g, "Niveau " + (level + 1), LEVELS[level].getName());
            else prompt(g, "Fin de jeu", new String[]{"Vies : " + lives, "Score : "+score});
        }    
        
        Toolkit.getDefaultToolkit().sync();
    }

    //Raccourci pour une seule sous ligne
    private void prompt(Graphics g, String prompt, String subPrompt)
    {
        prompt(g, prompt, new String[]{subPrompt});
    }

    //affiche du texte à l'écran, utilisés comme transitions
    private void prompt(Graphics g, String prompt, String[] subPrompts)
    {
        Font promptFont = new Font("Helvetica", Font.BOLD, 2*DOT_SIZE);
        FontMetrics metr = getFontMetrics(promptFont);

        //si on doit afficher des lignes en plus on ajoute un espace entre celles-ci et le prompt
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

    //Remet le jeu a 0, utilisé avec le promptTimer, d'où l'ActionListener
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
    
    //Affiche un prompt d'un niveau et le charge depuis LEVELS, initialisant les variables nécéssaires
    private void initGame() 
    {
        Entity ent;

        System.out.println("We loadin' " + level);

        //Affichage du prompt

        promptTimer = new Timer(PROMPT_TIME * 1000, startLevel);
        promptTimer.setRepeats(false); 

        promptTimer.start();

        repaint();

        //Chargement du niveau

        goal = new Goal(0, HUD_HEIGHT, B_WIDTH, DOT_SIZE, false);

        frogger.setPosX(B_WIDTH / 2);
        frogger.setPosY(W_HEIGHT - DOT_SIZE);
        frogger.setDirection(UP);
        
        coinCount = LEVELS[level].getCoins();
        timeBonus = INITIAL_TIME_BONUS;
        secondsCounter = 0;
        noMoreTime = false;
        collectibleList = new ArrayList<Entity>();
        voitureList = new ArrayList<MovingEntity>();
        troncList = new ArrayList<Tronc>();
        obstacleList = new ArrayList<Entity>();

        if ( level < LEVELS.length )//ne devrait jamais arriver mais on vérifie si on dépasse le nombre de niveaux préparé
        {
            for ( int compt = 0; compt < GRID_HEIGHT; compt++ )
            {
                if ( compt < LEVELS[level].getLayout().length() && LEVELS[level].getLayout().charAt(compt) != GRASS)
                {
                    //Attention si ce n'est pas de l'herbe on ne vérifie pas si on est sur le goal ou l'emplacement de frogger
                    switch (LEVELS[level].getLayout().charAt(compt))
                    {    
                        case ROAD:
                            for ( int comptVoit = 0; comptVoit < LEVELS[level].getCars(); comptVoit++ )
                                voitureList.add(getRandomCar(getRandomXCoordinate(), HUD_HEIGHT + compt * DOT_SIZE, 2 * DOT_SIZE, DOT_SIZE, getRandomCarDirection(), getRandomSpeed()));
                            break;
    
                        case WATER:
                            //on sépare les tronc de leur largeur - DOT_SIZE maximum pour éviter de rendre impossible la traversée d'une rivière
                            int comptX = (int)(Math.random() * TRONC_GRID_WIDTH) * DOT_SIZE;
                            while ( comptX + TRONC_GRID_WIDTH * DOT_SIZE <= B_WIDTH )
                            {
                                troncList.add(new Tronc(comptX, HUD_HEIGHT + compt * DOT_SIZE, TRONC_GRID_WIDTH * DOT_SIZE, DOT_SIZE));
                                comptX += TRONC_GRID_WIDTH * DOT_SIZE;
                                comptX += (int)(Math.random() * TRONC_GRID_WIDTH) * DOT_SIZE;
                            }
                            break;
                    }
                }
                else
                {
                    if ( compt != 0 && compt != GRID_HEIGHT - 1 ) //pour éviter de bloquer frogger ou de superposer des buissons sur le goal on éviter les extrémités du board
                    {
                        for ( int comptBush = 0; comptBush < LEVELS[level].getBushes(); comptBush++ )
                        {
                            obstacleList.add(new Bush(getRandomXCoordinate(), HUD_HEIGHT + compt * DOT_SIZE, DOT_SIZE, DOT_SIZE));
                        }
                    }
                }
            }
        }

        for ( int compt = 0; compt < coinCount; compt++ )
        {
            ent = new Coin(0, 0, DOT_SIZE);
            placeEntity(ent);
            collectibleList.add(ent);
        }

        //La fréquence des insecte est déterminé par le nombre de pièces et leur attribut FREQUENCY
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

        ent = new Pill(0, 0, DOT_SIZE);
        placeEntity(ent);
        collectibleList.add(ent);
    }

    //Commence la partie à la fin d'une instance de promptTimer
    private ActionListener startLevel = new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            inGame = true;
            System.out.println("We startin' " + level);
            promptTimer.stop();

            setBackground(LEVELS[level].getBackcolor());

            gameTimer.start();
            repaint();
        }
    };

    //termine le niveau après avoir touché le goal (avec assez de pièces)
    public void triggerLevelEnd()
    {
        System.out.println("We stoppin' "+level);

        inGame = false;
        gameTimer.stop();

        frogger.resetInvincible();
        score += timeBonus;

        level++;

        //on va au prochain niveau s'il y en a
        if ( level < LEVELS.length ) initGame();
        else 
        {
            //sinon on donne le bonus de vie et on recommence tout
            score += lives * LIVES_POINTS;

            repaint();

            promptTimer = new Timer(PROMPT_TIME * 1000, restartGame);
            promptTimer.setRepeats(false); 
            promptTimer.start();
        }
    }

    //termine le niveau après être mouru
    public void loseLife()
    {
        inGame = false;
        gameTimer.stop();

        frogger.resetInvincible();

        //si on a assez de vie on recommence le niveau, sinon on reset le jeu
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

    //listener du gameTimer, bouge les voiture
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (inGame) 
        {
            checkCollectibles();
            moveVoiture();
            secondsCounter += DELAY;

            if ( !noMoreTime && secondsCounter >= 1000 && timeBonus > 0 )
            {
                secondsCounter -= 1000;
                timeBonus -= TIME_BONUS_PENALTY;

                if ( timeBonus <= 0 ) 
                {
                    timeBonus = 0;
                    noMoreTime = true;

                    voitureList.add(new Ovni(B_WIDTH / 2, W_HEIGHT + 2 * DOT_SIZE, DOT_SIZE, DOT_SIZE, LEVELS[level].getMinSpeed()));
                }
            }
        }

        repaint();
    }

    //vérifie si frogger est en collision avec une entité et exécute le traitement adéquoi
    //active aussi le goal si on a fait les actions nécéssaires
    private void checkCollectibles() 
    {
        for (Entity coll : collectibleList)
        {
            if (frogger.collides(coll)) {
    
                sendToVoid(coll);

                ((Itriggerable)coll).triggerAction(this, frogger);
            }
        }

        if ( coinCount <= 0 && !goal.getReady() ) goal.setReady(true);
            
        if ( frogger.collides(goal) && goal.getReady() )
        {
            sendToVoid(goal);
            goal.triggerAction(this, frogger);
        }

        for ( MovingEntity voit : voitureList )
        {
            if ( frogger.collides(voit) )
            {
                ((Itriggerable)voit).triggerAction(this, frogger);
            }
        }
    }

    //Fait avancer toutes les voitures et les ramènes de l'autre coté de l'écran si nécéssaire
    private void moveVoiture()
    {
        for ( MovingEntity voit : voitureList )
        {
            voit.move(DOT_SIZE, this);

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

    //Met une entité dans le void où elle est sensée ne plus être utilisée
    public void sendToVoid(Entity victim)
    {
        victim.setPosX(VOID_X);
        victim.setPosY(VOID_Y);
    }

    //tests de terrains utilisés par les classes implémentant iTriggerable
    public boolean isRoad(int posY)
    {
        return terrainTest(posY, ROAD);
    }

    public boolean isWater(int posY)
    {
        return terrainTest(posY, WATER);
    }

    //teste si une coordonnée "posY" se trouve dans une zone au matériel "material"
    private boolean terrainTest(int posY, char material)
    {
        int gridY = ((posY - HUD_HEIGHT) / DOT_SIZE);
        return gridY >= 0 && gridY < LEVELS[level].getLayout().length() && LEVELS[level].getLayout().charAt(gridY) == material;
    }

    //aligne une entité a un multiple de DOT_SIZE sur la hauteur
    public void alignY(Entity ent)
    {
        ent.setPosY(ent.getPosY() - ent.getPosY() % DOT_SIZE);
    }

    //aligne une entité a un multiple de DOT_SIZE sur la largeur
    public void alignX(Entity ent)
    {
        ent.setPosX(ent.getPosX() - ent.getPosX() % DOT_SIZE);
    }

    //retourne frogger
    public Frog getFrogger()
    {
        return frogger;
    }

    //incrémente le score d'un certain nombre, et le double si on est invincible
    public void incScore(int amount)
    {
        if ( frogger.isInvincible() ) amount *= 2;
        score += amount;
    }

    //décrémente le nombre de pièces
    public void decCoinCount()
    {
        coinCount--;
    }

    //retourne le quotient de nombre de pièces collectées sur le nombre total
    public double getCompletion()
    {
        return (LEVELS[level].getCoins() - coinCount) * 1.0 / LEVELS[level].getCoins();
    }

    //test si une entité ent se trouve sur un tronc
    public boolean onTronc(Entity ent)
    {
        for ( Tronc tron : troncList )
        {
            if ( ent.collides(tron) ) return true;
        }

        return false;
    }

    //test si une entité ent se trouve dans un obstacle
    public boolean onObstacle(Entity ent)
    {
        for ( Entity obs : obstacleList )
        {
            if ( ent.collides(obs) ) return true;
        }

        return false;
    }

    //place une entité ent sur un endroit viable
    private void placeEntity(Entity ent)
    {
        do
        {
            ent.setPosX(getRandomXCoordinate());
            ent.setPosY(getRandomYCoordinate());
        } 
        while ( (!onTronc(ent) && isWater(ent.getPosY())) || ent.collides(goal) || onObstacle(ent) );
    }

    //retourne une coordonée aléatoire sur la largeur
    private int getRandomXCoordinate() 
    {
        int r = (int) (Math.random() * (GRID_WIDTH));
        return (r * DOT_SIZE);
    }

    //retourne une coordonée aléatoire sur la hauteur, en prenant compte du HUD
    private int getRandomYCoordinate() 
    {
        int r = (int) (Math.random() * (GRID_HEIGHT - 1)) * DOT_SIZE;
        return HUD_HEIGHT + r;
    }

    //retourne une vitesse aléatoire basée sur l'information du niveau
    public double getRandomSpeed()
    {
        return LEVELS[level].getSpeedRange() * Math.random() + LEVELS[level].getMinSpeed();
    }

    public int getRandomCarDirection()
    {
        return (int)(Math.random()*2) > 0 ? RIGHT : LEFT;
    }
    
    //retourne une voiture de type aléatoire en fonction des chances indiquées par le niveau, prends les paramêtres pour le constructeur de la voiture
    public Voiture getRandomCar(int posX, int posY, int width, int height, int direction, double speed)
    {
        int sum = 0, rand = -1, type = -1;
        boolean chosen = false;
        Voiture voit = null;

        for ( int i : LEVELS[level].getOdds() )
        {
            sum += i;
        }

        rand = (int)(Math.random()*sum);

        for ( int compt = 0; compt < LEVELS[level].getOdds().length; compt++ )
        {
            rand -= LEVELS[level].getOdds()[compt];
            if ( rand < 0 && !chosen ) 
            {
                type = compt;
                chosen = true;
            }
        }

        switch ( type )
        {
            default:
                //par sécurité si il y a erreur quelque part notamment s'il y a plus de types indiqués dans les chances du niveau qu'ici
                //sinon ce n'est que le cas 0
                voit = new Voiture(posX, posY, width, height, direction, speed);
                break;

            case 1:
                voit = new Blinky(posX, posY, width, height, direction, speed);
                break;

            case 2:
                voit = new Pinky(posX, posY, width, height, direction, LEVELS[level].getMinSpeed(), LEVELS[level].getMinSpeed() + LEVELS[level].getSpeedRange());
                break;

            case 3:
                voit = new Inky(posX, posY, width, height, direction, speed);
                break;

            case 4:
                voit = new Clyde(posX, posY, width, height, direction, speed);
                break;
        }

        return voit;
    }

    //gère les mouvement et les collision de frogger avec le niveau
    private void move() 
    {
        //si frogger n'est pas aligné alors qu'il a une vitesse normale, on tente de le réaligner au lieu de le faire avancer
        if ( !frogger.isInvincible() && ( (frogger.getDirection() == UP || frogger.getDirection() == DOWN ) && frogger.getPosY() % DOT_SIZE != 0) )
        {
            if ( frogger.getDirection() == DOWN ) frogger.setPosY(frogger.getPosY() + DOT_SIZE);
            alignY(frogger);
        }
        else if ( !frogger.isInvincible() && ( (frogger.getDirection() == LEFT || frogger.getDirection() == RIGHT) && frogger.getPosX() % DOT_SIZE != 0) )
        {
            if ( frogger.getDirection() == RIGHT ) frogger.setPosX(frogger.getPosX() + DOT_SIZE);
            alignX(frogger);
        }
        else frogger.move(DOT_SIZE, this);

        //collision avec les bords de l'écran
        if (frogger.getPosY() + frogger.getHeight() >= W_HEIGHT) {
            frogger.setPosY(W_HEIGHT - DOT_SIZE);
        }

        if (frogger.getPosY() < HUD_HEIGHT) {
            frogger.setPosY(HUD_HEIGHT);
        }

        if (frogger.getPosX() + frogger.getWidth() >= B_WIDTH) {
            frogger.setPosX(B_WIDTH - DOT_SIZE);
        }

        if (frogger.getPosX() < 0) {
            frogger.setPosX(0);
        }

        //collision avec les buissons (et autres obstacles)
        for ( Entity obs : obstacleList )
        {
            if ( frogger.collides(obs) ) 
            {
                switch ( frogger.getDirection() )
                {
                    case LEFT:
                        frogger.setPosX(obs.getPosX() + obs.getWidth());
                        break;

                    case RIGHT:
                        frogger.setPosX(obs.getPosX() - frogger.getWidth());
                        break;

                    case UP:
                        frogger.setPosY(obs.getPosY() + obs.getHeight());
                        break;

                    case DOWN:
                        frogger.setPosY(obs.getPosY() - frogger.getHeight());
                        break;
                }
            }
        }

        //collision avec l'eau
        //le -1 est un clearfix pour éviter de prendre en compte une case en trop
        if ( isWater(frogger.getPosY()) && isWater(frogger.getPosY() + frogger.getHeight() - 1) && !onTronc(frogger) ) loseLife();
    }

    //Gère les évenement de touche de clavier
    private class TAdapter extends KeyAdapter 
    {
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
