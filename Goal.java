public class Goal extends Collectible 
{
    public Goal(int posX, int posY)
    {
        super(posX, posY);
    }

    public static String getPathToImage() { return "goal.png"; }
    
    @Override
    public String getType() { return "Goal"; }

    @Override
    public void triggerAction(Board board)
    {
        board.triggerLevelEnd();
    }
}
