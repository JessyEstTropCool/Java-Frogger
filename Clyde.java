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
        if ( Math.random() < SPEED_CHANGE_CHANCE ) 
        {
            setSpeed( Math.random()/2 + 0.25 );
            System.out.println("And there goes Clyde again");
        }
        
        if ( Math.random() < DIRECTION_CHANGE_CHANCE ) 
        {
            if ( getDirection() == LEFT ) setDirection(RIGHT);
            else setDirection(LEFT);
            System.out.println("And switch");
        }

        super.Move(distance, board);
    }

    @Override
    public String getType() { return "Clyde"; }
}
