//Voiture mauve, change de vitesse en fonction du nombre de pièces collectées
public class Pinky extends Voiture
{
    private final double MIN_SPEED;
    private final double FULL_SPEED;

    public Pinky(int posX, int posY, int width, int height, int direction, double minSpeed, double maxSpeed)
    {
        super(posX, posY, width, height, direction, minSpeed);
        MIN_SPEED = minSpeed;
        FULL_SPEED = maxSpeed;
    }

    @Override
    public void move(int distance, Board board)
    {
        setSpeed(board.getCompletion() * (FULL_SPEED - MIN_SPEED) + MIN_SPEED);

        super.move(distance, board);
    }

    @Override
    public String getType() { return "Pinky" + getDirection(); }
}
