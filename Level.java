import java.awt.Color;

//Contient les informations d'un niveau, immutable (normalement)

public class Level 
{
    private final Color BACKCOLOR;
    private final String LAYOUT;
    private final String NAME;
    private final int[] ODDS;
    private final int COINS;
    private final int CARS;
    private final int BUSHES;
    //la vitesse est calculée ainsi -> MIN_SPEED + (RANDOM * SPEED_RANGE)
    private final double MIN_SPEED;
    private final double SPEED_RANGE;

    public Level(String name, String layout, int[] odds, int coins, int cars, int bushes, double minSpeed, double speedRange, Color backcolor)
    {
        NAME = name;
        LAYOUT = layout;
        ODDS = odds.clone();
        COINS = coins;
        CARS = cars;
        BUSHES = bushes;
        BACKCOLOR = backcolor;
        MIN_SPEED = minSpeed;
        SPEED_RANGE = speedRange;
    }

    public Color getBackcolor() { return BACKCOLOR; }
    public String getName() { return NAME; }
    public String getLayout() { return LAYOUT; }
    public int[] getOdds() { return ODDS.clone(); }
    public int getCoins() { return COINS; }
    public int getCars() { return CARS; }
    public int getBushes() { return BUSHES; }
    public double getMinSpeed() { return MIN_SPEED; }
    public double getSpeedRange() { return SPEED_RANGE; }
}
