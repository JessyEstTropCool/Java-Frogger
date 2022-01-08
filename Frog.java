import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frog extends MovingEntity implements ActionListener
{
    //invincTimer sert a afficher l'invincibilité dans le HUD en plus de la gerer ici
    private int invincSeconds;
    private double speedFactor;
    private Timer invincTimer = new Timer(1000, this);

    public Frog(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void move(int distance, Board board)
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
    
    //déduit de invincSeconds toutes les secondes jusqu'à ce qu'on arrive à 0
    //après quoi on arrête l'invincibilité
    public void actionPerformed(ActionEvent e)
    {
        invincSeconds--;
        
        //ne devrait pas aller en dessous de 0, la comparaison est plus générale par sécurité
        if ( invincSeconds <= 0 )
        {
            resetInvincible();
        }
    }

    //commence l'invincibilité pendant le temps en secondes time et en alterant la vitesse par le speedFactor
    public void triggerInvincible(int time, double speedFactor)
    {
        invincSeconds = time;
        invincTimer.start();

        this.speedFactor = speedFactor;

        setSpeed(getSpeed() * speedFactor);
    }

    //termine l'invincibilité si elle est encore en route et remet la vitesse à sa place
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

    //ajoute un listener a invincTimer
    public void addListener(ActionListener listener)
    {
        invincTimer.addActionListener(listener);
    }

    @Override
    public String getType() { return "Frogger" + getDirection() + isInvincible(); }
}