package alarma;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Mensaje extends JFrame {
    public JPanel panel;
    private JLabel message,botonSalir;
    
    public Mensaje(){
        InitComponents();
        setSize(330,100);
        setResizable(false);
        setUndecorated(true);
        setTitle("Mensaje");
        setLocationRelativeTo(null);
    }

    public JPanel getPanel() {
        return panel;
    }
    private void InitComponents(){
        Panel();
        Message();
        CloseMessage();
    }
    private void Panel(){
        panel = new JPanel();
        panel.setSize(330,100);
        panel.setBackground(Color.decode("#0B0705"));
        panel.setLayout(null);
        panel.setBorder(new LineBorder(new Color(62,60,63)));
        add(panel);
    }
    private void Message(){
        message = new JLabel("La alarma se ha programado");
        message.setBounds(28,30,274,25);
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setForeground(Color.white);
        message.setFont(new Font("Espectral",1,18));
        panel.add(message);
    }
    private void CloseMessage(){
        botonSalir = new JLabel("OK");
        botonSalir.setBounds(130,65,70,21);
        botonSalir.setOpaque(true);
        botonSalir.setHorizontalAlignment(SwingConstants.CENTER);
        botonSalir.setForeground(Color.gray);
        botonSalir.setBackground(Color.cyan);
        botonSalir.setFont(new Font("Cambria",1,18));
        mouseListenerCloseButton();
        panel.add(botonSalir);
    }
    private void mouseListenerCloseButton(){
        MouseListener botonsalir = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                botonSalir.setFont(new Font("Cambria",1,24));
                botonSalir.setForeground(Color.white);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                botonSalir.setFont(new Font("Cambria",1,21));
                botonSalir.setForeground(Color.gray);
            }
        };
        botonSalir.addMouseListener(botonsalir);
    }
    
}
