public class Bug extends Entity implements Itriggerable
{
    public Bug(int posX, int posY, int size)
    {
        super(posX, posY, size);
    }

    public static String getPathToImage() { return "insect.png"; }
    
    @Override
    public String getType() { return "Bug"; }

    @Override
    public void triggerAction(Board board)
    {
        board.incScore(2);
    }
}
