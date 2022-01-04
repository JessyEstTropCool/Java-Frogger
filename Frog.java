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
            //TODO fix collision when invincible + move to Board class 
            case LEFT:
                if (!board.isWall(getPosX()-(int)(distance * getSpeed()), getPosY())
                && !board.isWall(getPosX()+getWidth()-1-(int)(distance * getSpeed()), getPosY()))
                    setPosX(getPosX()-(int)(distance * getSpeed()));
                else
                    board.alignX(this);
                break;

            case RIGHT:
                if (!board.isWall(getPosX()+(int)(distance * getSpeed()), getPosY())
                && !board.isWall(getPosX()+getWidth()-1+(int)(distance * getSpeed()), getPosY()))
                    setPosX(getPosX()+(int)(distance * getSpeed()));
                else
                    board.alignX(this);
                break;

            case UP:
                if (!board.isWall(getPosX(), getPosY()-(int)(distance * getSpeed()))
                && !board.isWall(getPosX(), getPosY()+getHeight()-1-(int)(distance * getSpeed())))
                    setPosY(getPosY()-(int)(distance * getSpeed()));
                else
                    board.alignY(this);
                break;

            case DOWN:
                if (!board.isWall(getPosX(), getPosY()+(int)(distance * getSpeed()))
                && !board.isWall(getPosX(), getPosY()+getHeight()-1+(int)(distance * getSpeed())))
                    setPosY(getPosY()+(int)(distance * getSpeed()));
                else
                    board.alignY(this);
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