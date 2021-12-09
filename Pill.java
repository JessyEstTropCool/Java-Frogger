public class Pill extends Entity implements Itriggerable
{
    public Pill(int posX, int posY, int size)
    {
        super(posX, posY, size);
    }

    public static String getPathToImage() { return "pill.png"; }
    
    @Override
    public String getType() { return "Pill"; }

    @Override
    public void triggerAction(Board board)
    {
        board.getFrogger().triggerInvincible();
    }
}
