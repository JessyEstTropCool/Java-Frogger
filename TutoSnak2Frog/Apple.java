public class Apple 
{
    private int posX;
    private int posY;

    private String pathToImage = "apple.png";

    public Apple(int posX, int posY)
    {
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }

    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }

    public String getPathToImage() { return pathToImage; }
}
