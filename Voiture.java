public class Voiture extends MovingEntity
{
    public Voiture(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void Move(int distance, Board board)
    {
        switch (getDirection())
        {
            case LEFT:
                setPosX(getPosX() - (int)(distance * getSpeed()));
                break;

            case RIGHT:
                setPosX(getPosX() + (int)(distance * getSpeed()));
                break;
        }
    }

    @Override
    public String getType() { return "Voiture"; }

    @Override
    public void triggerAction(Board board)
    {
        if ( board.isInvincible() )
        {
            board.SendToVoid(this);
            setDirection(-1);
        }
        else board.triggerGameOver();
    }
}
