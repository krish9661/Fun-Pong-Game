import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
public class GameFrame extends JFrame{
    GamePanel2 panel;

    GameFrame(){
        panel = new GamePanel2();
        this.add(panel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("PongGame");
        this.setBackground(Color.BLACK);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
