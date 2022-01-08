//voiture bleue, change de vitesse si frogger est sur la même voie qu'elle
public class Inky extends Voiture 
{
    private final static double SAME_LANE_FACTOR = 0.5;

    public Inky(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void move(int distance, Board board)
    {
        if ( collides(getPosX(), board.getFrogger().getPosY()) || collides(getPosX(), board.getFrogger().getPosY() + board.getFrogger().getHeight() - 1) ) distance *= SAME_LANE_FACTOR;

        super.move(distance, board);
    }

    @Override
    public String getType() { return "Inky" + getDirection(); }
}
