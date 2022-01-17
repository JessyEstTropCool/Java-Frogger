//classe de base d'une entité pouvant bouger
public abstract class MovingEntity extends Entity implements Idirectional
{
    private int direction;
    private double speed;

    public MovingEntity(int posX, int posY, int width, int height, int offsetX, int offsetY, int direction, double speed)
    {
        super(posX, posY, width, height, offsetX, offsetY);

        this.direction = direction;
        this.speed = speed;
    }

    public MovingEntity(int posX, int posY, int width, int height, int direction, double speed)
    {
        this(posX, posY, width, height, 0, 0, direction, speed);
    }

    public int getDirection() { return direction; }
    public void setDirection(int newDir) { this.direction = newDir; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public abstract void move(int distance, Board board);
}
