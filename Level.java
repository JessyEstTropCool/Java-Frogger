import java.awt.Color;

public class Level 
{
    private final Color BACKCOLOR;
    private final String LAYOUT;
    private final String NAME;
    private final int[] ODDS;
    private final int COINS;
    private final int CARS;
    private final int BUSHES;

    public Level(String name, String layout, int[] odds, int coins, int cars, int bushes, Color backcolor)
    {
        NAME = name;
        LAYOUT = layout;
        ODDS = odds.clone();
        COINS = coins;
        CARS = cars;
        BUSHES = bushes;
        BACKCOLOR = backcolor;
    }

    public Color getBackcolor() { return BACKCOLOR; }
    public String getName() { return NAME; }
    public String getLayout() { return LAYOUT; }
    public int[] getOdds() { return ODDS.clone(); }
    public int getCoins() { return COINS; }
    public int getCars() { return CARS; }
    public int getBushes() { return BUSHES; }
}
