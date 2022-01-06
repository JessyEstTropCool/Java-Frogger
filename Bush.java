public class Bush extends Entity
{
    //TYPE détermine l'image qui sera utilisé par le buisson, TYPE_AMOUNT en indiquant le nombre
    private static final int TYPE_AMOUNT = 3;
    private final int TYPE;

    public Bush(int posX, int posY, int width, int height)
    {
        super(posX, posY, width, height);
        TYPE = (int)(Math.random()*TYPE_AMOUNT);
    }

    @Override
    public String getType() { return "Bush" + TYPE; }
}
