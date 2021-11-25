public class Voiture 
{
    private int posX;
    private int posY;
    private int width;
    private int height;
    private int direction;

    public Voiture(int posX, int posY, int width, int height, int direction)
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public boolean inCar(int X, int Y)
    {
        return X >= posX && X < posX + width && Y >= posY && Y < posY + height;
    }

    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }

    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }

    public int getDirection() { return direction; }
    public void setDirection(int newDir) { this.direction = newDir; }
}
