public abstract class MovingEntity extends Entity
{
    protected final int LEFT = 0, UP = 1, RIGHT = 2, DOWN = 3;
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
    public abstract void Move(int distance, Board board);
}