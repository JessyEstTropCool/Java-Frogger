public class Blinky extends Voiture 
{
    private static final double SIDE_MOUVEMENT_FACTOR = 0.5;

    public Blinky(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void Move(int distance, Board board)
    {
        super.Move(distance, board);

        if ( getDirection() != STOP )
        {
            if ( Math.abs(getPosY() - board.getFrogger().getPosY()) < distance * getSpeed() && board.isRoad(board.getFrogger().getPosY()) && board.isRoad(board.getFrogger().getPosY() + board.getFrogger().getHeight()) ) 
            {
                setPosY(board.getFrogger().getPosY());
            }
            else
            {
                if ( board.getFrogger().getPosY() > getPosY() ) 
                {
                    setPosY(getPosY() + (int)(distance * getSpeed() * SIDE_MOUVEMENT_FACTOR));
        
                    if ( board.isRoad(getPosY()) && !board.isRoad(getHeight() + getPosY()) )
                    {
                        board.alignY(this);
                    }
                }
                else if ( board.getFrogger().getPosY() < getPosY() ) 
                {
                    if ( board.isRoad(getPosY()) && !board.isRoad(getPosY() - (int)(distance * getSpeed())) )
                    {
                        board.alignY(this);
                    }
                    else setPosY(getPosY() - (int)(distance * getSpeed() * SIDE_MOUVEMENT_FACTOR));
                }
            }
        }
    }

    @Override
    public String getType() { return "Blinky" + getDirection(); }
}
