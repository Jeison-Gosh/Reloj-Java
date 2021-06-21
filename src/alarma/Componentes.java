package alarma;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;  
import javax.swing.SwingConstants;

public class Componentes extends JFrame {
    
    private JPanel ventana ;
    private JLabel timerButton, alarmButton, clockButton, exitButton, minimizeButton;
    private Thread timer_thread , clock1_thread, clock2_thread;
    private Runnable timer_runnable, clock1_runnable, clock2_runnable;
    private boolean reloj_formato_estado, clock_default_color_buttons=true;
    private boolean timer_default_startstopbutton=true, timer_default_resetbutton=false, timer_default_image_status=true, timer_thread_is_not_start=true;
    public JLabel alarm, clock1, clock2, clock_button_12h, clock_button_24h;
    public JLabel timer, timer_image, timer_reset_button, timer_start_stop_button, timer_dhms;

    
    public Componentes(){
        InitComponents(); // al estar en primera linea del metodo precarga los componentes
        setSize(500,500);
        setResizable(false);
        setUndecorated(true);
        setVisible(true);
        setLocationRelativeTo(null);
        setTitle("Alarma");
        setIconImage(new ImageIcon(getClass().getResource("/images/RelojIcon.png")).getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
    private void InitComponents(){
        
        PanelDeEntrada();
        BotonDeCierre();
        BotonDeMinimizar();
        
        alarm_button();
        timer_button();
        clock_button();
        
        alarm();
        timer();
        clock1();
        clock2();
        
        timer_image();
        timer_dhms();
        timer_reset_button();
        timer_start_and_stop_button();
        
        clock_button_12h();
        clock_button_24h();
        
        //creacion y activacion de hilos
        activeClock_Threads();
        activeTimer_Threads();
        
//        watchLayouts();
    } 
    public void PanelDeEntrada(){
        ventana=new JPanel();
        ventana.setSize(500,500);
        ventana.setBackground(Color.black);
        ventana.setLayout(null);
//        this.getContentPane().add(ventana); // para versiones de java inferiores a 5.0 (aun sirve en versiones actuales)
        add(ventana); //Solamente para versiones de java superiores a la 5.0
    }
    public void BotonDeCierre() {
        exitButton=new JLabel("X");
        exitButton.setForeground(Color.white);
        exitButton.setFont(new Font("Spectral", 2, 20));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setBounds(475,2,20,20);
        mouseListenerExitButton();
        ventana.add(exitButton);
    }
    public void BotonDeMinimizar() {
        minimizeButton=new JLabel("-");
        minimizeButton.setForeground(Color.white);
        minimizeButton.setFont(new Font("Spectral", 2, 50));
        minimizeButton.setHorizontalAlignment(SwingConstants.CENTER);
        minimizeButton.setLocation(exitButton.getBounds().x-30, exitButton.getBounds().y-5);
        minimizeButton.setSize(20,20);
        mouseListenerMinimizeButton();
        ventana.add(minimizeButton);
        
        
    }
    
    public void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(Componentes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void alarm_button(){
        alarmButton=new JLabel("Alarma");
        alarmButton.setFont(new Font("Segoe Print",1,15));
        alarmButton.setForeground(Color.gray);
        alarmButton.setHorizontalAlignment(SwingConstants.CENTER);
        alarmButton.setBounds(50,425,70,25);
        mouseListenerAlarmButton();
        ventana.add(alarmButton);
    }
    public void alarm(){
        alarm=new JLabel("Alarma");
        alarm.setHorizontalAlignment(SwingConstants.CENTER);
        alarm.setForeground(Color.white);
        alarm.setFont(new Font("Pacifico",3,40));
        alarm.setBounds(177,135,145,45);
        alarm.setVisible(true);
        mouseLstenerTitleAlarm();
        ventana.add(alarm);
    }
    
    public void clock_button(){
        clockButton=new JLabel("Reloj");
        clockButton.setFont(new Font("Segoe Print",1,15));
        clockButton.setForeground(Color.gray);
        clockButton.setHorizontalAlignment(SwingConstants.CENTER);
        clockButton.setBounds(410,425,43,25);
        mouseListenerClockButton();
        ventana.add(clockButton);
    }
    public void clock1(){
        clock1=new JLabel("Reloj");
        clock1.setHorizontalAlignment(SwingConstants.CENTER);
        clock1.setForeground(Color.magenta);
        clock1.setFont(new Font("Pacifico",1,40));
        clock1.setBounds(110,135,280,45);
        clock1.setVisible(false);
        ventana.add(clock1);
                
    }
    public void clock2(){
        clock2=new JLabel("Reloj");
        clock2.setHorizontalAlignment(SwingConstants.CENTER);
        clock2.setForeground(Color.magenta);
        clock2.setFont(new Font("Pacifico",1,40));
        clock2.setBounds(110,135,280,45);
        clock2.setVisible(false);
        ventana.add(clock2);
                
    }
    private void clock_button_12h(){
        clock_button_12h=new JLabel("[12H");
        clock_button_12h.setSize(55,25);
        clock_button_12h.setFont(new Font("Sitka",1,20));
        clock_button_12h.setHorizontalAlignment(SwingConstants.CENTER);
        clock_button_12h.setForeground(Color.gray);
        clock_button_12h.setLocation(clock1.getLocation().x+87, clock1.getLocation().y+55);
        clock_button_12h.setVisible(false);
        mouseListenerButton12h();
        ventana.add(clock_button_12h);
    }
    private void clock_button_24h(){
        clock_button_24h=new JLabel("24H]");
        clock_button_24h.setSize(55,25);
        clock_button_24h.setFont(new Font("Sitka",1,20));
        clock_button_24h.setHorizontalAlignment(SwingConstants.CENTER);
        clock_button_24h.setForeground(Color.gray);
        clock_button_24h.setLocation(clock1.getLocation().x+142, clock1.getLocation().y+55);
        clock_button_24h.setVisible(false);
        mouseListenerButton24h();
        ventana.add(clock_button_24h);
    }
    
    public void timer_button(){
       timerButton=new JLabel("Temporizador");
       timerButton.setFont(new Font("Segoe Print",1,15));
       timerButton.setBounds(177,425,165,25);
       timerButton.setForeground(Color.gray);
       timerButton.setHorizontalAlignment(SwingConstants.CENTER);
       mouseListenerTimerButton();
       ventana.add(timerButton);
    }
    public void timer(){
        timer=new JLabel("00:00:00:00");
        timer.setHorizontalAlignment(SwingConstants.CENTER);
        timer.setForeground(Color.white);
        timer.setFont(new Font("Pacifico",1,40));
        timer.setBounds(110,135,280,45);
        timer.setVisible(false);
        ventana.add(timer);
    }
    public void timer_image(){
        int width, height,putOnCenterImageX;
        timer_image=new JLabel(new ImageIcon(getClass().getResource("/images/timer.gif")), SwingConstants.CENTER);
        width=new ImageIcon(getClass().getResource("/images/timer.gif")).getIconWidth();
        height=new ImageIcon(getClass().getResource("/images/timer.gif")).getIconHeight();
        putOnCenterImageX=(((ventana.getSize().width)-width)/2);
        timer_image.setSize(width , height);
        timer_image.setLocation(putOnCenterImageX , 45);
        timer_image.setVisible(false);
        ventana.add(timer_image);
    }
    private void timer_dhms(){
        int x, y;
        timer_dhms=new JLabel("DD        HH         MM        SS");
        x=(timer.getLocation().x)+20;
        y=(timer.getLocation().y)+50;
        timer_dhms.setHorizontalAlignment(SwingConstants.CENTER);
        timer_dhms.setForeground(Color.gray);
        timer_dhms.setFont(new Font("Times",1,15));
        timer_dhms.setBounds(x, y, 240, 20);
        timer_dhms.setVisible(false);
        ventana.add(timer_dhms);
    }
    private void timer_reset_button(){
        int x, y;
        x=(timer_dhms.getLocation().x+140);
        y=(timer_dhms.getLocation().y+65);
        timer_reset_button=new JLabel("Reset");
        timer_reset_button.setVisible(false);
        timer_reset_button.setHorizontalAlignment(SwingConstants.CENTER);
        timer_reset_button.setFont(new Font("Times",1,19));
        timer_reset_button.setForeground(new Color(73,6,187));
        timer_reset_button.setLocation(x,y);
        timer_reset_button.setSize(65,30);
        mouseListenerResetButton();
        ventana.add(timer_reset_button);
        
    }
    private void timer_start_and_stop_button(){
        int x, y;
        x=(timer_dhms.getLocation().x+40);
        y=(timer_dhms.getLocation().y+65);
        timer_start_stop_button= new JLabel("Start");
        timer_start_stop_button.setVisible(false);
        timer_start_stop_button.setHorizontalAlignment(SwingConstants.CENTER);
        timer_start_stop_button.setFont(new Font("Times",1,19));
        timer_start_stop_button.setForeground(new Color(73,6,187));
        timer_start_stop_button.setLocation(x,y);
        timer_start_stop_button.setSize(65,30);
        mouseListenerStartStopButton();
        ventana.add(timer_start_stop_button);
    }
    
    // Activacion de Threads
    
    private void activeTimer_Threads(){
        timer_runnable=new Temporizador();
        timer_thread=new Thread(timer_runnable);
    }
    private void activeClock_Threads(){
        clock1_runnable=new Reloj(false);
        clock2_runnable=new Reloj(true);
        clock1_thread=new Thread(clock1_runnable);
        clock2_thread=new Thread(clock2_runnable);
        clock1_thread.start();
        clock2_thread.start();
    }
    
    public void setVisibleClockInterfaz(boolean status){
        if(status){
            clock1.setVisible(status);
            clock2.setVisible(status);
            clock_button_12h.setVisible(status);
            clock_button_24h.setVisible(status);
        }else{
            clock1.setVisible(status);
            clock2.setVisible(status);
            clock_button_12h.setVisible(status);
            clock_button_24h.setVisible(status);
        }
    }
    public void setVisibleTimerInterfaz(boolean status){
        if(status){
            timer.setVisible(status);
            timer_image.setVisible(status);
            timer_dhms.setVisible(status);
            timer_reset_button.setVisible(status);
            timer_start_stop_button.setVisible(status);
        }else{
            timer.setVisible(status);
            timer_image.setVisible(status);
            timer_dhms.setVisible(status);
            timer_reset_button.setVisible(status);
            timer_start_stop_button.setVisible(status);
        }
    }
    public void setVisibleAlarmInterfaz(boolean status){
        if(status){
            alarm.setVisible(status);
        }else{
            alarm.setVisible(status);
        }
    }
    
    //mouselisteners
    private void mouseListenerTimerButton(){
        MouseListener MLtimerL=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                alarmButton.setFont(new Font("Segoe Print",1,15));
                clockButton.setFont(new Font("Segoe Print",1,15));
                if(!alarmButton.getForeground().equals(Color.CYAN) || !clockButton.getForeground().equals(Color.CYAN)){
                    timerButton.setForeground(Color.cyan);
                    alarmButton.setForeground(Color.gray);
                    clockButton.setForeground(Color.gray);
                }
                if(timerButton.getForeground().equals(Color.CYAN)){
                    setVisibleAlarmInterfaz(false);
                    setVisibleClockInterfaz(false);
                    if(timer_default_image_status){
//                        timerImage.setVisible(true);
                        timer.setVisible(true);
                        timer_dhms.setVisible(true);
                        timer_reset_button.setVisible(true);
                        timer_start_stop_button.setVisible(true);
//                        timer_default_image_status=false;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
//                SoundBubble();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(!timerButton.getForeground().equals(Color.cyan)){
                    timerButton.setFont(new Font("Segoe Print",1,18));
                    timerButton.setForeground(Color.white);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(!timerButton.getForeground().equals(Color.cyan)){
                    timerButton.setFont(new Font("Segoe Print",1,15));
                    timerButton.setForeground(Color.gray);
                }
            }
        };
        timerButton.addMouseListener(MLtimerL);
    }
    private void mouseListenerAlarmButton(){
        MouseListener MLalarmL=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(!timerButton.getForeground().equals(Color.CYAN) || !clockButton.getForeground().equals(Color.CYAN)){
                    alarmButton.setForeground(Color.cyan);
                    timerButton.setForeground(Color.gray);
                    clockButton.setForeground(Color.gray);
                }
                if(alarmButton.getForeground().equals(Color.CYAN)){
                    setVisibleTimerInterfaz(false);
                    setVisibleClockInterfaz(false);
                    alarm.setVisible(true);
                }
                timerButton.setFont(new Font("Segoe Print",1,15));
                clockButton.setFont(new Font("Segoe Print",1,15));
                
            }

            @Override
            public void mousePressed(MouseEvent me) {
//                SoundBubble();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(!alarmButton.getForeground().equals(Color.cyan)){
                    alarmButton.setFont(new Font("Segoe Print",1,18));
                    alarmButton.setForeground(Color.white);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(!alarmButton.getForeground().equals(Color.cyan)){
                    alarmButton.setFont(new Font("Segoe Print",1,15));
                    alarmButton.setForeground(Color.gray);
                }
            }
        };
        alarmButton.addMouseListener(MLalarmL);
    }
    private void mouseListenerClockButton(){
        MouseListener MLclockL=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(!timerButton.getForeground().equals(Color.CYAN) || !alarmButton.getForeground().equals(Color.CYAN)){
                    clockButton.setForeground(Color.cyan);
                    alarmButton.setForeground(Color.gray);
                    timerButton.setForeground(Color.gray);
                }
                if(clockButton.getForeground().equals(Color.CYAN)){
                    setVisibleAlarmInterfaz(false);
                    setVisibleTimerInterfaz(false);
                    if(reloj_formato_estado) {
                        clock1.setVisible(true);
                    }else{
                        clock2.setVisible(true);
                    }
                    clock_button_24h.setVisible(true);
                    clock_button_12h.setVisible(true);
                }
                timerButton.setFont(new Font("Segoe Print",1,15));
                alarmButton.setFont(new Font("Segoe Print",1,15));
                
            }

            @Override
            public void mousePressed(MouseEvent me) {
//                SoundBubble();
            }
            
            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(!clockButton.getForeground().equals(Color.cyan)){
                    clockButton.setFont(new Font("Segoe Print",1,18));
                    clockButton.setForeground(Color.white);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(!clockButton.getForeground().equals(Color.cyan)){
                    clockButton.setFont(new Font("Segoe Print",1,15));
                    clockButton.setForeground(Color.gray);
                }
               
            }
        };
        clockButton.addMouseListener(MLclockL);
    }
    
    private void mouseListenerButton12h(){
        MouseListener Button12h=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                reloj_formato_estado=false;
                clock1.setVisible(false);
                clock2.setVisible(true);
                clock_button_12h.setFont(new Font("Stika",1,25));
                clock_button_12h.setForeground(Color.white);
                clock_button_24h.setFont(new Font("Stika",1,20));
                clock_button_24h.setForeground(Color.gray);
                clock_default_color_buttons=false;
                
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(clock_default_color_buttons){
                    clock_button_12h.setFont(new Font("Stika",1,25));
                    clock_button_12h.setForeground(Color.white);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(clock_default_color_buttons){
                    clock_button_12h.setFont(new Font("Stika",1,20));
                    clock_button_12h.setForeground(Color.gray);
                }
            }
        };
        clock_button_12h.addMouseListener(Button12h);
    }
    private void mouseListenerButton24h(){
        MouseListener Button24h=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                reloj_formato_estado=true;
                clock1.setVisible(true);
                clock2.setVisible(false);
                clock_button_24h.setFont(new Font("Stika",1,25));
                clock_button_24h.setForeground(Color.white);
                clock_button_12h.setFont(new Font("Stika",1,20));
                clock_button_12h.setForeground(Color.gray);
                clock_default_color_buttons=false;
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(clock_default_color_buttons){
                    clock_button_24h.setFont(new Font("Stika",1,25));
                    clock_button_24h.setForeground(Color.white);
                    
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(clock_default_color_buttons){
                    clock_button_24h.setFont(new Font("Stika",1,20));
                    clock_button_24h.setForeground(Color.gray);
                }
            }
        };
        clock_button_24h.addMouseListener(Button24h);
    }
    
    private void mouseListenerExitButton(){
        MouseListener botoncierrre;
        botoncierrre = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
//                SoundBubble();
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent me) {
                exitButton.setForeground(Color.RED);
                exitButton.setFont(new Font("Spectral",2,24));
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                exitButton.setForeground(Color.yellow);
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                exitButton.setForeground(Color.white);
                exitButton.setFont(new Font("Spectral",2,20));
            }
        };
        exitButton.addMouseListener(botoncierrre);
    }
    private void mouseListenerMinimizeButton(){
        MouseListener botonminimizar=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                setState(ICONIFIED);
            }

            @Override
            public void mousePressed(MouseEvent me) {
                minimizeButton.setFont(new Font("Spectral",2,57));
                minimizeButton.setForeground(Color.red);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                minimizeButton.setForeground(Color.yellow);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                minimizeButton.setForeground(Color.white);
            }
        };
        minimizeButton.addMouseListener(botonminimizar);
    }
    
    private void mouseListenerResetButton(){
        MouseListener resetbutton=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if(timer_default_resetbutton){
                    Temporizador.Reset();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(timer_default_resetbutton){
                    timer_reset_button.setFont(new Font("Times",1,24));
                    timer_reset_button.setForeground(new Color(213,51,255));
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(timer_default_resetbutton){
                    timer_reset_button.setFont(new Font("Times",1,19));
                    timer_reset_button.setForeground(new Color(73,6,187)); 
                }
            }
        };
        timer_reset_button.addMouseListener(resetbutton);
    }
    private void mouseListenerStartStopButton(){
        MouseListener startStopButton=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if(timer_default_startstopbutton){
                    timer_default_startstopbutton=false;
                    timer_default_resetbutton=false;
                    timer_start_stop_button.setText("Stop");
                    if(timer_thread_is_not_start){
                        timer_thread.start();
                        timer_thread_is_not_start=false;
                    }else{
                        System.out.println("resume");
                        Temporizador.Resume();
                    }
                }else{
                    timer_default_startstopbutton=true;
                    timer_default_resetbutton=true;
                    timer_start_stop_button.setText("Start");
                    Temporizador.Stop();
                    
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(timer_default_startstopbutton){
                    timer_start_stop_button.setFont(new Font("Times",1,24));
                    timer_start_stop_button.setForeground(new Color(213,51,255));
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(timer_default_startstopbutton){
                    timer_start_stop_button.setFont(new Font("Times",1,19));
                    timer_start_stop_button.setForeground(new Color(73,6,187));                
                }
            }
        };
        timer_start_stop_button.addMouseListener(startStopButton);
    }
    private void mouseLstenerTitleAlarm(){
        MouseListener alarmatitleML=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                setVisibleAlarmInterfaz(true);
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                alarm.setForeground(Color.cyan);
                alarm.setFont(new Font("Pacifico",3,42));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                alarm.setForeground(Color.white);
                alarm.setFont(new Font("Pacifico",3,40));
            }
        };
        alarm.addMouseListener(alarmatitleML);
    }
    
    private void watchLayouts(){
        alarmButton.setOpaque(true);
        alarm.setOpaque(true);
        clockButton.setOpaque(true);
        clock1.setOpaque(true);
        clock2.setOpaque(true);
        timerButton.setOpaque(true);
        timer.setOpaque(true);
        timer_image.setOpaque(true);
        timer_dhms.setOpaque(true);
        timer_reset_button.setOpaque(true);
        timer_start_stop_button.setOpaque(true);
        exitButton.setOpaque(true);
        minimizeButton.setOpaque(true);
        clock_button_12h.setOpaque(true);
        clock_button_24h.setOpaque(true);
        
    }
    
}