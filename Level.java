import java.awt.Color;

public class Level 
{
    public final Color BACKCOLOR;
    public final String LAYOUT;
    public final int[] ODDS;
    public final int COINS;
    public final int CARS_PER_LANE;
    public final int BUSHES;

    public Level(String layout, int[] odds, int coins, int cars, int bushes, Color backcolor)
    {
        LAYOUT = layout;
        ODDS = odds.clone();
        COINS = coins;
        CARS_PER_LANE = cars;
        BUSHES = bushes;
        BACKCOLOR = backcolor;
    }

    public Voiture GetRandomCar(int posX, int posY, int width, int height, int direction, double speed)
    {
        int sum = 0, rand = -1, type = -1;
        boolean chosen = false;
        Voiture voit = null;

        for ( int i : ODDS )
        {
            sum += i;
        }

        rand = (int)(Math.random()*sum);

        for ( int compt = 0; compt < ODDS.length; compt++ )
        {
            rand -= ODDS[compt];
            if ( rand < 0 && !chosen ) 
            {
                type = compt;
                chosen = true;
            }
        }

        System.out.println(type);

        switch ( type )
        {
            case 0:
                voit = new Voiture(posX, posY, width, height, direction, speed);
                break;

            case 1:
                voit = new Blinky(posX, posY, width, height, direction, speed);
                break;

            case 2:
                voit = new Pinky(posX, posY, width, height, direction, speed);
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
}
