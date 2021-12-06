public class Goal extends Entity 
{
    private boolean ready;

    public Goal(int posX, int posY, int width, int height, boolean ready)
    {
        super(posX, posY, width, height);
        this.ready = ready;
    }

    public static String getPathToImage() { return "goal.png"; }

    public boolean getReady() { return ready; }
    public void setReady(boolean ready) { this.ready = ready; }
    
    @Override
    public String getType() 
    {
        if ( ready ) return "Goal"; 
        else return "GoalDown";
    }

    @Override
    public void triggerAction(Board board)
    {
        board.triggerLevelEnd();
    }
}
