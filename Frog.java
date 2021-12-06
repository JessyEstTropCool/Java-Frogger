public class Frog extends MovingEntity
{
    public Frog(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void Move(int distance, Board board)
    {
        switch (getDirection())
        {
            case LEFT:
                setPosX(getPosX()-(int)(distance * getSpeed()));
                break;

            case RIGHT:
                setPosX(getPosX()+(int)(distance * getSpeed()));
                break;

            case UP:
                setPosY(getPosY()-(int)(distance * getSpeed()));
                break;

            case DOWN:
                setPosY(getPosY()+(int)(distance * getSpeed()));
            break;
        }
    }

    @Override
    public String getType() { return "Frogger"; }
}