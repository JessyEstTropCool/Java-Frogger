public class BugUnique extends Bug 
{
    private static final int POINTS_AWARDED = 5;
    private static final int FREQUENCY = 5;

    public BugUnique(int posX, int posY, int size)
    {
        super(posX, posY, size, POINTS_AWARDED);
    }

    public static int getFrenquency()
    {
        return FREQUENCY;
    }
    
    @Override
    public String getType() { return "BugUnique"; }
}
