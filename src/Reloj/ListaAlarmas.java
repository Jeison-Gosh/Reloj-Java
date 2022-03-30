package Reloj;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ListaAlarmas extends JFrame {
    
    private int coordinateX, coordinateY, accountant=0;
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
        InputPanel.setBackground(Color.decode("#0B0705"));
        InputPanel.setBorder(new LineBorder(new Color(72,71,73)));
        this.add(InputPanel);
    }
    public void addAlarm(AlarmaGrafica AG){
        if(Componentes.alarm_list.isEmpty()){
            coordinateX=50;
            coordinateY=30;
            AG.setLocation(coordinateX,coordinateY);
            accountant++;
        }else{
            switch (accountant) {
                case 0:
                    AG.setLocation(50,coordinateY);
//                    System.out.println("es el 0");
                    accountant++;
                    break;
                case 1:
                    AG.setLocation(200,coordinateY);
//                    System.out.println("es el 1");
                    accountant++;
                    break;
                case 2:
                    AG.setLocation(350,coordinateY);
//                    System.out.println("es el 2");
                    coordinateY+=130;
                    accountant=0;
                    break;
            }
        }
        AG.setVisible(true);
        InputPanel.add(AG);
    }
    
}
   
    


