package Reloj;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Mensaje extends JFrame {
    
    private JPanel panel;
    private JLabel message,closeButton;
    
    public Mensaje(String messageText){
        this.setFocusableWindowState(false);
        this.setResizable(false);
        this.setUndecorated(true);        
        this.setAlwaysOnTop(true);
        this.setSize((messageText.length()*12)+10,100);
        this.setTitle("Mensaje");
        this.setLocationRelativeTo(null);
        
        panel = new JPanel();
        panel.setSize(this.getSize());  
        panel.setLayout(null);
        panel.setBackground(Color.decode("#202225"));
        panel.setBorder(new LineBorder(new Color(62,60,63)));
        this.add(panel);
        
        message = new JLabel(messageText);
        message.setBounds(5,30,getSize().width-10,25);
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setForeground(Color.white);
        message.setFont(new Font("Espectral",1,18));
        panel.add(message);
    
        closeButton = new JLabel("OK");
        closeButton.setBounds((getSize().width/3)+5,65,(getSize().width-10)/3,21);
        closeButton.setOpaque(true);
        closeButton.setHorizontalAlignment(SwingConstants.CENTER);
        closeButton.setForeground(Color.gray);
        closeButton.setBackground(Color.cyan);
        closeButton.setFont(new Font("Cambria",1,18));
        mouseListenerCloseButton();
        panel.add(closeButton);
    }

    public JPanel getPanel() {
        return panel;
    }
    private synchronized void mouseListenerCloseButton(){
        MouseListener botonsalir = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(false);
                if(message.getText().equals("Se han desactivado las alarmas.")){
                   System.exit(0);
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                closeButton.setFont(new Font("Cambria",1,24));
                closeButton.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                closeButton.setFont(new Font("Cambria",1,21));
                closeButton.setForeground(Color.gray);
            }
        };
        closeButton.addMouseListener(botonsalir);
    }
    
}
