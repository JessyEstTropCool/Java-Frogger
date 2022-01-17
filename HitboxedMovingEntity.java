//entité qui bouge et fait une différence entre le visuel et collision
public abstract class HitboxedMovingEntity extends MovingEntity 
{
    private int hitX, hitY, hitW, hitH;

    public HitboxedMovingEntity(int posX, int posY, int width, int height, int hitX, int hitY, int hitW, int hitH, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);

        this.hitX = hitX;
        this.hitY = hitY;
        this.hitW = hitW;
        this.hitH = hitH;
    }

    //vérifie une collision avec un point (X; Y)
    public boolean collides(int X, int Y)
    {
        return X >= getPosX() + hitX && X < getPosX() + hitX + hitW && Y >= getPosY() + hitY && Y < getPosY() + hitY + hitH;
    }

    //vérifie une collision avec une autre entité ent
    public boolean collides(Entity ent)
    {
        return getPosX() + hitX < ent.getPosX() + ent.getWidth()
        && getPosX() + hitX + hitW > ent.getPosX()
        && getPosY() + hitY < ent.getPosY() + ent.getHeight()
        && getPosY() + hitY + hitH > ent.getPosY();
    }
}
