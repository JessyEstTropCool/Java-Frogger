
public class Insect extends FixedGameElement {


    public Insect(int pos_x, int pos_y) {
        
        super(pos_x, pos_y);
    }
    
    public static String getPathToImage(){
        return "insect.png";
    }
    
    public String getType(){
        return "insect";
    }
    
    public void triggerAction(Board board){
        board.incScore(2);
    }
}





