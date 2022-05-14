package Reloj;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ListaAlarmas extends JFrame {
    
    private int coordinateX=30, coordinateY=10, accountant=0;
    private JPanel InputPanel;
    
    public ListaAlarmas(){
        InitComponents();
        setSize(500, 500);
        setTitle("Lista de alarmas");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setBackground(Color.CYAN);
        setUndecorated(true);
    }
    
    private void InitComponents(){
        InputPanel();
    }
    private void InputPanel(){
        InputPanel = new JPanel();
        InputPanel.setLocation(0,0);
        InputPanel.setSize(500,500);
        InputPanel.setLayout(null);
        InputPanel.setBackground(Color.decode("#202225"));
        InputPanel.setBorder(new LineBorder(new Color(72,71,73)));
        this.add(InputPanel);
    }
    public void addAlarm(AlarmaGrafica AG){
        AG.setLocation(coordinateX,coordinateY);
        String condition=AG.getDay();
        switch(condition){
            case "Domingo":
                AG.setBackground(Color.decode("#610404"));
                break;
            case "Lunes":
                AG.setBackground(Color.decode("#5C6104"));
                break;
            case "Martes":
                AG.setBackground(Color.decode("#046122"));
                break;
            case "Miercoles":
                AG.setBackground(Color.decode("#045F61"));
                break;
            case "Jueves":
              AG.setBackground(Color.decode("#041161"));
                break;
            case "Viernes":
                AG.setBackground(Color.decode("#250461"));
                break;
            case "Sabado":
                AG.setBackground(Color.decode("#61045B"));
                break;
        }
        coordinateY+=65;
        accountant++;
        AG.setVisible(true);
        InputPanel.add(AG);
    }
    
}
   
    


