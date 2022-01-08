//interface pour toute entité qui font quelque chose une foit en collision avec une autre
public interface Itriggerable 
{
    public abstract void triggerAction(Board board, Entity other);
}
