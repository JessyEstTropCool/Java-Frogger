public abstract class Collectible 
{
    private int posX;
    private int posY;

    public Collectible(int posX, int posY)
    {
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }

    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }

    public abstract String getType();
    public abstract void triggerAction(Board board);
}
