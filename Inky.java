public class Inky extends Voiture 
{
    private final static double SAME_LANE_FACTOR = 0.5;

    public Inky(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void Move(int distance, Board board)
    {
        if ( Collides(getPosX(), board.getFrogger().getPosY()) ) distance *= SAME_LANE_FACTOR;

        super.Move(distance, board);
    }

    @Override
    public String getType() { return "Inky"; }
}
