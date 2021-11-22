public class Coin extends Collectible
{
    public Coin(int posX, int posY)
    {
        super(posX, posY);
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
