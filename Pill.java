//pillule, power up d'invincibilité pour frogger
public class Pill extends Entity implements Itriggerable
{
    private static final int INVINCIBLE_TIME = 10;
    private static final double SPEED_FACTOR = 0.5;
    private static final int PIERRE_CHANCE = 20;
    private boolean pierre;

    //a une chance de changer d'image et de devenir le célèbre pierre
    public Pill(int posX, int posY, int size)
    {
        super(posX, posY, size);

        if ( (int)(Math.random() * PIERRE_CHANCE) == 0 ) pierre = true;
        else pierre = false;
    }

    public static double getSpeedFactor() { return SPEED_FACTOR; }
    
    @Override
    public String getType() { return "Pill" + pierre; }

    @Override
    public void triggerAction(Board board, Entity other)
    {
        ((Frog)other).triggerInvincible(INVINCIBLE_TIME, SPEED_FACTOR);
    }
}
