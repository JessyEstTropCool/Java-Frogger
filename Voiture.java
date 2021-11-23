public class Voiture 
{
    private int posX;
    private int posY;
    private int direction;

    public Voiture(int posX, int posY, int direction)
    {
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;
    }

    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }

    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }

    public int getDirection() { return direction; }
    public void setDirection(int newDir) { this.direction = newDir; }
}
