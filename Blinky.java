public class Blinky extends Voiture 
{
    public Blinky(int posX, int posY, int width, int height, int direction, double speed)
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

        if ( Math.abs(getPosY() - board.getFrogger().getPosY()) < distance * getSpeed() ) setPosY(board.getFrogger().getPosY());
        else 
        {
            if ( board.getFrogger().getPosY() > getPosY() ) 
            {
                setPosY(getPosY() + (int)(distance * getSpeed()));
            }
            else if ( board.getFrogger().getPosY() < getPosY() ) 
            {
                setPosY(getPosY() - (int)(distance * getSpeed()));
            }
        }
    }

    @Override
    public String getType() { return "Blinky"; }
}
