public class Pinky extends Voiture
{
    private static final double MIN_SPEED = 0.25;
    private double fullSpeed;

    public Pinky(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, MIN_SPEED);
        fullSpeed = speed;
    }

    @Override
    public void Move(int distance, Board board)
    {
        setSpeed(board.getCompletion() * (fullSpeed - MIN_SPEED) + MIN_SPEED);

        super.Move(distance, board);
    }

    @Override
    public String getType() { return "Pinky" + getDirection(); }
}
