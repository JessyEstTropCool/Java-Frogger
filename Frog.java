import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frog extends MovingEntity implements ActionListener
{
    private final double FULL_SPEED;
    private final double SLOW_SPEED;
    private final int INVINCIBLE_TIME;

    private int invincSeconds;
    private Timer invincTimer = new Timer(1000, this);

    public Frog(int posX, int posY, int width, int height, int direction, double fullSpeed, double slowSpeed, int invincTime)
    {
        super(posX, posY, width, height, direction, fullSpeed);
        this.FULL_SPEED = fullSpeed;
        this.SLOW_SPEED = slowSpeed;
        this.INVINCIBLE_TIME = invincTime;
    }

    @Override
    public void Move(int distance, Board board)
    {
        switch (getDirection())
        {
            case LEFT:
                setPosX(getPosX()-(int)(distance * getSpeed()));
                break;

            case RIGHT:
                setPosX(getPosX()+(int)(distance * getSpeed()));
                break;

            case UP:
                setPosY(getPosY()-(int)(distance * getSpeed()));
                break;

            case DOWN:
                setPosY(getPosY()+(int)(distance * getSpeed()));
            break;
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        invincSeconds--;
        
        if ( invincSeconds <= 0 )
        {
            setSpeed(FULL_SPEED);
            invincTimer.stop();
        }
    }

    public void triggerInvincible()
    {
        invincSeconds = INVINCIBLE_TIME;
        invincTimer.start();
        setSpeed(SLOW_SPEED);
    }

    public void resetInvincible()
    {
        if ( invincTimer.isRunning() )
        {
            invincTimer.stop();
            invincSeconds = 0;
            setSpeed(FULL_SPEED);
        }
    }

    public int getInvincibleTime() { return invincSeconds; }
    public void setInvincibleTime(int seconds) { this.invincSeconds = seconds; }

    public boolean isInvincible()
    {
        return invincSeconds > 0;
    }

    public void addListener(ActionListener listener)
    {
        invincTimer.addActionListener(listener);
    }

    @Override
    public String getType() { return "Frogger" + getDirection() + isInvincible(); }
}