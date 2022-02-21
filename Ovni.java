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
			//on calcule à quel point l'ovni va dans chaque direction
			double dirX = board.getFrogger().getPosX() - getPosX() , dirY = board.getFrogger().getPosY() - getPosY(), dirTot = Math.abs(dirX) + Math.abs(dirY);
			
			dirX /= dirTot;
			dirY /= dirTot;
			
            //s'aligne sur frogger s'il est assez proche et qu'il est sur la route
            if ( Math.abs(getPosY() - board.getFrogger().getPosY()) < distance * getSpeed() * dirY ) 
            {
                setPosY(board.getFrogger().getPosY());
            }
            else
            {
                //s'approche de frogger et s'aligne sur la route s'il est arrivé trop loin
                setPosY(getPosY() + (int)(distance * getSpeed() * dirY));
            }

            //s'aligne sur frogger s'il est assez proche et qu'il est sur la route
            if ( Math.abs(getPosX() - board.getFrogger().getPosX()) < distance * getSpeed() * dirX ) 
            {
                setPosX(board.getFrogger().getPosX());
            }
            else
            {
                //s'approche de frogger et s'aligne sur la route s'il est arrivé trop loin
                setPosX(getPosX() + (int)(distance * getSpeed() * dirX));
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
