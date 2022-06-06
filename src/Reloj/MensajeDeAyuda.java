package Reloj;

import java.awt.Color;
import java.awt.Font;
import java.awt.MouseInfo;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class MensajeDeAyuda extends JFrame {
    
    private final JLabel MESSAGE;
    
    public MensajeDeAyuda(String message){
        this.setLocation(MouseInfo.getPointerInfo().getLocation());
        this.setUndecorated(true);
        this.setSize(message.length()*6,20);
        this.setFocusable(false);
        this.setFocusableWindowState(false);
        this.setAlwaysOnTop(true);
                
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.decode("#D5B59C"));
        panel.setBorder(new LineBorder(Color.GRAY));
        panel.setSize(this.getSize());
        this.add(panel);
        
        MESSAGE = new JLabel(message);
        MESSAGE.setForeground(Color.BLACK);
        MESSAGE.setAlignmentX(SwingConstants.CENTER);
        MESSAGE.setAlignmentY(SwingConstants.CENTER);
        MESSAGE.setFont(new Font("Calibri",1 ,11));
        MESSAGE.setSize(this.getSize().width-4,this.getSize().height-4);
        MESSAGE.setLocation(2,2);
        panel.add(MESSAGE);
    }
}
