//classe de base des insectes donnant des points déterminé à la contruction
public abstract class Bug extends Entity implements Itriggerable
{
    private int points;

    public Bug(int posX, int posY, int size, int points)
    {
        super(posX, posY, size);
        this.points = points;
    }

    @Override
    public void triggerAction(Board board, Entity other)
    {
        board.incScore(points);
    }
}
