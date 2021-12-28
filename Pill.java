public class Pill extends Entity implements Itriggerable
{
    private static final int PIERRE_CHANCE = 1;
    private boolean pierre;

    public Pill(int posX, int posY, int size)
    {
        super(posX, posY, size);
        
        if ( (int)(Math.random() * PIERRE_CHANCE) == 0 ) pierre = true;
        else pierre = false;
    }

    public static String getPathToImage() { return "pill.png"; }
    
    @Override
    public String getType() { return "Pill" + pierre; }

    @Override
    public void triggerAction(Board board)
    {
        board.getFrogger().triggerInvincible();
    }
}
