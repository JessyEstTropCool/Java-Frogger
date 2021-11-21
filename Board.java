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

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private int pos_x;
    private int pos_y;
    
    private int coinCounter;
    private int insectCounter;
    //private ArrayList<Coin> coinList ;
    private ArrayList<FixedGameElement> fixedGameElementList ;
    HashMap<String, ImageIcon> fixedGameElementImageMap ;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image coin;
    private Image head;
    
    private int score;
    private int void_x = -1*B_WIDTH;
    private int void_y = -1*B_HEIGHT;

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
    
        fixedGameElementImageMap = new HashMap<String, ImageIcon>();

        ImageIcon iic = new ImageIcon(Coin.getPathToImage());
        //coinImage = iic.getImage();
        fixedGameElementImageMap.put("coin", iic);
        
        ImageIcon iii = new ImageIcon(Insect.getPathToImage());
        //insectImage = iii.getImage();
        fixedGameElementImageMap.put("insect", iii);

        ImageIcon iih = new ImageIcon("head.png");
        head = iih.getImage();
    }

    private void initGame() {
        
        score = 0 ;
        
        pos_x = B_WIDTH/2;
        pos_y = B_HEIGHT/2;
        
        coinCounter = 3;
        insectCounter = 2;
        fixedGameElementList = new ArrayList<FixedGameElement>();
        
        for(int i = 0; i < coinCounter ; i++){
            fixedGameElementList.add(new Coin(getRandomCoordinate(), getRandomCoordinate()));
        }
        
        for(int i = 0; i < insectCounter ; i++){
            fixedGameElementList.add(new Insect(getRandomCoordinate(), getRandomCoordinate()));
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

            for(FixedGameElement elem: fixedGameElementList){               
                g.drawImage(fixedGameElementImageMap.get(elem.getType()).getImage(), elem.getPosX(), elem.getPosY(), this);
            }      
            
            g.drawImage(head, pos_x, pos_y, this);

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

    private void checkFixedGameElementCollision() {

        for(FixedGameElement elem: fixedGameElementList){
            if ((pos_x == elem.getPosX()) && (pos_y == elem.getPosY())){
                elem.setPosX(void_x);
                elem.setPosY(void_y);
                
                elem.triggerAction(this);
                
                System.out.println(coinCounter);
                System.out.println(score);
            }
        }    
    }
    
    public void incScore(int valueToIncrease){
        score += valueToIncrease;
    } 
    
    public void decreaseCoinAmount(){
        coinCounter -=1;
    }

    private void move() {

        if (leftDirection) {
            pos_x -= DOT_SIZE;
        }

        if (rightDirection) {
            pos_x += DOT_SIZE;
        }

        if (upDirection) {
            pos_y -= DOT_SIZE;
        }

        if (downDirection) {
            pos_y += DOT_SIZE;
        }
    }

    private void checkCollision() {

        if (pos_y >= B_HEIGHT) {
            inGame = false;
        }

        if (pos_y < 0) {
            inGame = false;
        }

        if (pos_x >= B_WIDTH) {
            inGame = false;
        }

        if (pos_x < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
        }
    }
    
    private int getRandomCoordinate() {

        int r = (int) (Math.random() * RAND_POS);
        return ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkFixedGameElementCollision();
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
