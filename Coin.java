public class Coin extends Entity implements Itriggerable
{
    public Coin(int posX, int posY, int size)
    {
        super(posX, posY, size);
    }

    public static String getPathToImage() { return "coin.png"; }

    @Override
    public String getType() { return "Coin"; }

    @Override
    public void triggerAction(Board board)
    {
        board.incScore(1);
        board.decCoinCount();
    }
}
