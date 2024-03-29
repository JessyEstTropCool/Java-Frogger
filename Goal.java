//sert au changement de niveau si on a collecté toute les pièces
public class Goal extends Entity implements Itriggerable
{
    private boolean ready;

    public Goal(int posX, int posY, int width, int height, boolean ready)
    {
        super(posX, posY, width, height);
        this.ready = ready;
    }

    public boolean getReady() { return ready; }
    public void setReady(boolean ready) { this.ready = ready; }
    
    @Override
    public String getType() 
    {
        return "Goal" + ready;
    }

    @Override
    public void triggerAction(Board board, Entity other)
    {
        board.triggerLevelEnd();
    }
}
