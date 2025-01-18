import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Score extends Rectangle{

    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    int Player1;
    int Player2;
    String P1_Controll= "W and S key";
    String P2_Controll= "Up and Down Key";

    Score(int GAME_WIDTH, int GAME_HEIGHT){
        Score.GAME_WIDTH = GAME_WIDTH;
        Score.GAME_HEIGHT = GAME_HEIGHT;
    }
    public void draw(Graphics g){
        // g.setColor(Color.white);
        // g.setFont(new Font("Consolas",Font.PLAIN,60));
        // g.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);
        /*OR
         * we can make a dashed line to divide two half of the pong tabel
         */
        Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.white);
		g.setFont(new Font("Consolas",Font.PLAIN,30));
		
        Stroke dashed = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{25}, 30);
        g2d.setStroke(dashed);
        g2d.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);

        
        g.drawString(String.valueOf(Player1/10)+String.valueOf(Player1%10),(GAME_WIDTH/2)-55, 50);
        g.drawString(String.valueOf(Player2/10)+String.valueOf(Player2%10),(GAME_WIDTH/2)+20, 50);

        g.setColor(Color.decode("#1E90FF"));
        g.drawString(P1_Controll,0, 50);
        g.drawString(P2_Controll,(GAME_WIDTH)-260, 50);
    }
}
