public class BugRare extends Bug 
{
    private static final int POINTS_AWARDED = 3;
    private static final int FREQUENCY = 3;

    public BugRare(int posX, int posY, int size)
    {
        super(posX, posY, size, POINTS_AWARDED);
    }

    public static int getFrenquency()
    {
        return FREQUENCY;
    }
    
    @Override
    public String getType() { return "BugRare"; }
}
