public class Bug extends Collectible
{
    public Bug(int posX, int posY)
    {
        super(posX, posY);
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
