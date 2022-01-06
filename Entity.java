//classe de base pour les entités
public abstract class Entity 
{
    private int posX;
    private int posY;
    private int width;
    private int height;

    public Entity(int posX, int posY, int width, int height) 
    {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
    }

    public Entity(int posX, int posY, int size) 
    {
        this.posX = posX;
        this.posY = posY;
        this.width = size;
        this.height = size;
    }

    //vérifie une collision avec un point (X; Y)
    public boolean Collides(int X, int Y)
    {
        return X >= posX && X < posX + width && Y >= posY && Y < posY + height;
    }

    //vérifie une collision avec une autre entité ent
    public boolean Collides(Entity ent)
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

    public abstract String getType();
}
