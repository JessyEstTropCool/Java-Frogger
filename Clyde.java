//Voiture orange, changeant de vitesse ou de sens de mainère aléatoire
public class Clyde extends Voiture
{
    //le changement de direction devrait être plus rare pour éviter trop de surprise
    private static final double SPEED_CHANGE_CHANCE = 0.05;
    private static final double DIRECTION_CHANGE_CHANCE = 0.001;

    public Clyde(int posX, int posY, int width, int height, int direction, double speed)
    {
        super(posX, posY, width, height, direction, speed);
    }

    @Override
    public void move(int distance, Board board)
    {
        //ne change de direction/vitesse seulement si elle est encore en jeu
        if ( Math.random() < SPEED_CHANGE_CHANCE && getDirection() != STOP ) 
        {
            setSpeed( board.getRandomSpeed() );
        }
        
        if ( Math.random() < DIRECTION_CHANGE_CHANCE && getDirection() != STOP ) 
        {
            if ( getDirection() == LEFT ) setDirection(RIGHT);
            else setDirection(LEFT);
        }

        super.move(distance, board);
    }

    @Override
    public String getType() { return "Clyde" + getDirection(); }
}
