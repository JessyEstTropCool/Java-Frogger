public class Clyde extends Voiture
{
    private static final double SPEED_CHANGE_CHANCE = 0.05;
    private static final double DIRECTION_CHANGE_CHANCE = 0.001;

    public Clyde(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void Move(int distance, Board board)
    {
        if ( Math.random() < SPEED_CHANGE_CHANCE && getDirection() != STOP ) 
        {
            setSpeed( Math.random()/2 + 0.25 );
        }
        
        if ( Math.random() < DIRECTION_CHANGE_CHANCE && getDirection() != STOP ) 
        {
            if ( getDirection() == LEFT ) setDirection(RIGHT);
            else setDirection(LEFT);
        }

        super.Move(distance, board);
    }

    @Override
    public String getType() { return "Clyde" + getDirection(); }
}
