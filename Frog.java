import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frog extends MovingEntity implements ActionListener
{
    private int invincSeconds;
    private double speedFactor;
    private Timer invincTimer = new Timer(1000, this);

    public Frog(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
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
            resetInvincible();
        }
    }

    public void triggerInvincible(int time, double speedFactor)
    {
        invincSeconds = time;
        invincTimer.start();

        this.speedFactor = speedFactor;

        setSpeed(getSpeed() * speedFactor);
    }

    public void resetInvincible()
    {
        if ( invincTimer.isRunning() )
        {
            invincTimer.stop();
            invincSeconds = 0;
            setSpeed(getSpeed() / speedFactor);
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