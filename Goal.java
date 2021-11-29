public class Goal extends Entity 
{
    public Goal(int posX, int posY, int size)
    {
        super(posX, posY, size);
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
