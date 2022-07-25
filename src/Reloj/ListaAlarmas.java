package Reloj;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class ListaAlarmas extends JFrame {
    
    private int coordinateX=30, coordinateY=10, accountant=0;
    private final LinkedList<AlarmaGrafica> AGList;
    private boolean wasCalledByRebuildMethod;
    private MensajeDeAyuda Help_Message=null;
    private JLabel exitButton, minimizeButton, backgroundButton;
    private JPanel InputPanel;
    
    public ListaAlarmas(){
        InitComponents();
        setFocusableWindowState(false);
        setSize(500,500);
        setTitle("Lista de alarmas");
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setBackground(Color.CYAN);
        setUndecorated(true);
        AGList = new LinkedList<>();
        wasCalledByRebuildMethod=false;
        
    }
    
    private void InitComponents(){
        InputPanel();
        Close_Button();
        Minimize_Button();
        Background_Button();
    }
    private void InputPanel(){
        InputPanel = new JPanel();
        InputPanel.setLocation(0,0);
        InputPanel.setSize(500,500);
        InputPanel.setPreferredSize(new Dimension(400, 200));
        InputPanel.setLayout(null);
        InputPanel.setBackground(Color.decode("#202225"));
        InputPanel.setBorder(new LineBorder(new Color(72,71,73)));
        this.add(InputPanel);
    }
    
    private void Close_Button() {
        exitButton=new JLabel("•");
        exitButton.setForeground(Color.decode("#c20000"));
        exitButton.setFont(new Font("Spectral",1,25));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setVerticalAlignment(SwingConstants.CENTER);
        exitButton.setBounds(475,4,18,26);
        mouseListenerExitButton();
        InputPanel.add(exitButton);
    }
    private void Minimize_Button() {
        minimizeButton=new JLabel("•");
        minimizeButton.setForeground(Color.decode("#efb810"));
        minimizeButton.setFont(new Font("Spectral",1,25));
        minimizeButton.setLocation(exitButton.getBounds().x-20, exitButton.getBounds().y);
        minimizeButton.setHorizontalAlignment(SwingConstants.CENTER);
        minimizeButton.setVerticalAlignment(SwingConstants.CENTER);
        minimizeButton.setSize(18,26);
        mouseListenerMinimizeButton();
        InputPanel.add(minimizeButton);
        
    }
    private void Background_Button(){
        backgroundButton=new JLabel("•");
        backgroundButton.setForeground(Color.gray);
        backgroundButton.setFont(new Font("Spectral",1,25));
        backgroundButton.setLocation(exitButton.getBounds().x-40 , exitButton.getBounds().y);
        backgroundButton.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundButton.setVerticalAlignment(SwingConstants.CENTER);    
        backgroundButton.setSize(18,26);
        mouseListenerBackgroundButton();
        InputPanel.add(backgroundButton);
    }
    
    private void mouseListenerExitButton(){
        MouseAdapter MA=new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                setVisible(false);
            }
            @Override
            public void mousePressed(MouseEvent me) {
                exitButton.setForeground(Color.RED);
                exitButton.setFont(new Font("Spectral",1,27));
//             
            }
            @Override
            public void mouseEntered(MouseEvent me) {
                exitButton.setForeground(Color.decode("#ff0000"));
                exitButton.setFont(new Font("Spectral",1,25));
                if(Help_Message==null) Help_Message=new MensajeDeAyuda("Cerrar");
                if(!Help_Message.isVisible()) Help_Message.setVisible(true);
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                exitButton.setForeground(Color.decode("#c20000"));
                exitButton.setFont(new Font("Spectral",1,25));
                if(Help_Message!=null){
                    Help_Message.setVisible(false);
                    Help_Message=null;
                }
            }
        };
        exitButton.addMouseListener(MA);
    }
    private void mouseListenerMinimizeButton()  {
        MouseAdapter MA=new MouseAdapter() {
                 @Override
                 public void mouseEntered(MouseEvent me) {
                    minimizeButton.setForeground(Color.decode("#EDEBE6"));
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    minimizeButton.setForeground(Color.decode("#efb810"));  
                }
        };
        minimizeButton.addMouseListener(MA);
    }
    private void mouseListenerBackgroundButton() {
        MouseAdapter MA=new MouseAdapter() {
             @Override
             public void mouseEntered(MouseEvent me) {
                backgroundButton.setForeground(Color.decode("#EDEBE6"));
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                backgroundButton.setForeground(Color.gray);  
            }
        };
        backgroundButton.addMouseListener(MA);
    }
    
    public void addAlarm(AlarmaGrafica AG){
        AG.setLocation(coordinateX,coordinateY);
        switch(AG.getDay()){
            case "Domingo" -> AG.setBackground(Color.decode("#610404"));
            case "Lunes" -> AG.setBackground(Color.decode("#5C6104"));
            case "Martes" -> AG.setBackground(Color.decode("#046122"));
            case "Miercoles" -> AG.setBackground(Color.decode("#045F61"));
            case "Jueves" -> AG.setBackground(Color.decode("#041161"));
            case "Viernes" -> AG.setBackground(Color.decode("#250461"));
            case "Sabado" -> AG.setBackground(Color.decode("#61045B"));
        }
        if(!wasCalledByRebuildMethod){
            if(AGList.isEmpty()){
                AGList.addFirst(AG);
            }else{
                AGList.addLast(AG);
            }
        }
        AG.setVisible(true);
        coordinateY+=65;
        accountant++;
        InputPanel.add(AG);
    }
    public synchronized void rebuildGraphicalList(){
        ListIterator<Alarma> ita=Componentes.alarm_list.listIterator();
        ListIterator<AlarmaGrafica> itag=AGList.listIterator();
        while(ita.hasNext() && itag.hasNext()){
            Alarma a=ita.next();
            AlarmaGrafica ag=itag.next();
            if(!a.isAlarmOn() && a.getAlarmName().equals(ag.getTitle())){
                itag.remove();
            }
        }
        InputPanel.removeAll();
        Close_Button();
        Minimize_Button();
        Background_Button();
        coordinateY=10;
        for(AlarmaGrafica ag: AGList){
            wasCalledByRebuildMethod=true;
            addAlarm(ag);
        }
        wasCalledByRebuildMethod=false;
        if(this.isVisible()){
            this.setVisible(false);
            this.setVisible(true);
        }
        
    }
    
}
   
    


