public class Bush extends Entity
{
    private static final int IMAGE_AMOUNT = 3;
    private final int LOOK;

    public Bush(int posX, int posY, int width, int height)
    {
        super(posX, posY, width, height);
        LOOK = (int)(Math.random()*IMAGE_AMOUNT);
    }

    @Override
    public String getType() { return "Bush" + LOOK; }
}
