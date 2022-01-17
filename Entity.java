//classe de base pour les entités
public abstract class Entity 
{
    private int posX;
    private int posY;
    private int width;
    private int height;
    private int offsetX;
    private int offsetY;

    public Entity(int posX, int posY, int width, int height, int offsetX, int offsetY) 
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public Entity(int posX, int posY, int width, int height) 
    {
        this(posX, posY, width, height, 0, 0);
    }

    public Entity(int posX, int posY, int size) 
    {
        this(posX, posY, size, size);
    }

    //vérifie une collision avec un point (X; Y)
    public boolean collides(int X, int Y)
    {
        return X >= posX && X < posX + width && Y >= posY && Y < posY + height;
    }

    //vérifie une collision avec une autre entité ent
    public boolean collides(Entity ent)
    {
        return posX < ent.posX + ent.width 
        && posX + width > ent.posX
        && posY < ent.posY + ent.height
        && posY + height > ent.posY;
    }

    public int getPosX() { return posX; }
    public void setPosX(int posX) { this.posX = posX; }

    public int getPosY() { return posY; }
    public void setPosY(int posY) { this.posY = posY; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getOffsetX() { return offsetX; }
    public void setOffsetX(int offsetX) { this.offsetX = offsetX; }

    public int getOffsetY() { return offsetY; }
    public void setOffsetY(int offsetY) { this.offsetY = offsetY; }

    public int getDisplayX() { return posX + offsetX; }
    public int getDisplayY() { return posY + offsetY; }

    public abstract String getType();
}
