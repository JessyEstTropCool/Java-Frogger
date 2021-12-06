public class Inky extends Voiture 
{
    public Inky(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void Move(int distance, Board board)
    {
        super.Move(distance, board);
    }

    @Override
    public String getType() { return "Inky"; }
}
