//classe de base d'une entité pouvant bouger
public abstract class MovingEntity extends Entity implements Idirectional
{
    private int direction;
    private double speed;

    public MovingEntity(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height);

        this.direction = direction;
        this.speed = speed;
    }

    public int getDirection() { return direction; }
    public void setDirection(int newDir) { this.direction = newDir; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public abstract String getType();
    public abstract void move(int distance, Board board);
}
