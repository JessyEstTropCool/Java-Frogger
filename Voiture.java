//voiture de base, mais peu aussi être mise en jeu
public class Voiture extends MovingEntity implements Itriggerable
{
    public Voiture(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void move(int distance, Board board)
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
    public String getType() { return "Voiture" + getDirection(); }

    @Override
    public void triggerAction(Board board, Entity other)
    {
        if ( ((Frog)other).isInvincible() )
        {
            board.sendToVoid(this);
            setDirection(STOP);

            board.incScore(1);
        }
        else board.loseLife();
    }
}
