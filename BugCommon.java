//type d'insecte commun donnant peut de points
public class BugCommon extends Bug
{
    private static final int POINTS_AWARDED = 2;
    private static final int FREQUENCY = 1;

    public BugCommon(int posX, int posY, int size)
    {
        super(posX, posY, size, POINTS_AWARDED);
    }

    public static int getFrenquency() { return FREQUENCY; }
    
    @Override
    public String getType() { return "BugCommon"; }
}
