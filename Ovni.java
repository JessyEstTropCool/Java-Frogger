public class Ovni extends MovingEntity implements Itriggerable
{
    public Ovni(int posX, int posY, int width, int height, double speed)
    {
        super(posX, posY, width, height, 0, -(height * 2), SHMOVE, speed);
    }

    public void move(int distance, Board board)
    {
        if ( getDirection() != STOP )
        {
            //s'aligne sur frogger s'il est assez proche et qu'il est sur la route
            if ( Math.abs(getPosY() - board.getFrogger().getPosY()) < distance * getSpeed() ) 
            {
                setPosY(board.getFrogger().getPosY());
            }
            else
            {
                //s'approche de frogger et s'aligne sur la route s'il est arrivé trop loin
                if ( board.getFrogger().getPosY() > getPosY() ) 
                {
                    setPosY(getPosY() + (int)(distance * getSpeed()));
                }
                else if ( board.getFrogger().getPosY() < getPosY() ) 
                {
                    setPosY(getPosY() - (int)(distance * getSpeed()));
                }
            }

            //s'aligne sur frogger s'il est assez proche et qu'il est sur la route
            if ( Math.abs(getPosX() - board.getFrogger().getPosX()) < distance * getSpeed() ) 
            {
                setPosX(board.getFrogger().getPosX());
            }
            else
            {
                //s'approche de frogger et s'aligne sur la route s'il est arrivé trop loin
                if ( board.getFrogger().getPosX() > getPosX() ) 
                {
                    setPosX(getPosX() + (int)(distance * getSpeed()));
                }
                else if ( board.getFrogger().getPosX() < getPosX() ) 
                {
                    setPosX(getPosX() - (int)(distance * getSpeed()));
                }
            }
        }
    }

    public String getType() { return "Ovni"; }

    @Override
    public void triggerAction(Board board, Entity other)
    {
        if ( ((Frog)other).isInvincible() )
        {
            board.sendToVoid(this);
            setDirection(STOP);

            board.incScore(5);
        }
        else board.loseLife();
    }
}
