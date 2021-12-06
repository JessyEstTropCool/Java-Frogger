public class Blinky extends Voiture 
{
    public Blinky(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void Move(int distance, Board board)
    {
        if ( Math.abs(getPosY() - board.getFrogger().getPosY()) < distance * getSpeed() && board.isRoad(board.getFrogger().getPosY()) && board.isRoad(board.getFrogger().getPosY() + board.getFrogger().getHeight()) ) 
        {
            setPosY(board.getFrogger().getPosY());
        }
        else
        {
            if ( board.getFrogger().getPosY() > getPosY() ) 
            {
                setPosY(getPosY() + (int)(distance * getSpeed()));
    
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
                else setPosY(getPosY() - (int)(distance * getSpeed()));
            }
        }
        
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
    public String getType() { return "Blinky"; }
}