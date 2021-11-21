import java.awt.EventQueue;
import javax.swing.JFrame;

public class Frogger extends JFrame {

    public Frogger() {
        
        initUI();
    }
    
    private void initUI() {
        
        add(new Board());
               
        setResizable(false);
        pack();
        
        setTitle("Frogger");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Frogger();
            ex.setVisible(true);
        });
    }
}
