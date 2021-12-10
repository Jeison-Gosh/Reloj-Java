 package alarma;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;  
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Componentes extends JFrame {
    
    
    private JPanel MainPanel;
    private JLabel alarmButton, timerButton, clockButton, exitButton, minimizeButton, backgroundButton;
    private JLabel  alarmPickDay, alarmPickHour, alarmPickMinute, alarmPickMeridian, alarmTwoPointsMeridian, alarmScheduleButton, alarmShowListButton;
    private Thread timer_thread , clock1_thread, clock2_thread;
    private TrayIcon trayicon;
    private MenuItem TI_tooltip_open, TI_tooltip_close;
    private SystemTray systemtray;
    private Runnable timer_runnable, clock1_runnable, clock2_runnable;
    private int alarm_day_count, alarm_hour_count, alarm_minute_count;
    private boolean clock_format_status, clock_default_color_buttons=true, alarmStatusMeridian=true, alarmListIsEmpity=true;
    private boolean timer_default_startstopbutton=true, timer_default_resetbutton=false, timer_thread_is_not_start=true;
    private String [] Smins, Shours, Sdays;
    private Integer []mins, hours;
    public ArrayList<Alarma> alarm_list;
    public LinkedList<Thread> alarm_list_thread;
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
        
        InputPanel();
        CloseButton();
        MinimizeButton();
        BackgroundButton();
        InitTrayIcon();
        
        Alarm_Button();
        Timer_Button();
        Clock_Button();
        
        Alarm_Interface();
        Timer_Interface();
        Clock_Interface();
        
        //creacion y activacion de hilos
        activeTimer_Threads();
        activeClock_Threads();
        
//        watchLayouts();
    } 
    private void InputPanel(){
        MainPanel=new JPanel();
        MainPanel.setSize(500,500);
        MainPanel.setBackground(Color.decode("#0B0705"));
        MainPanel.setLayout(null);
        MainPanel.setBorder(new LineBorder(new Color(72,71,73)));
        add(MainPanel); //Solamente para versiones de java superiores a la 5.0
//        this.getContentPane().add(ventana); // para versiones de java inferiores a 5.0 (aun sirve en versiones actuales)
        
    }
    private void CloseButton() {
        exitButton=new JLabel("X");
        exitButton.setForeground(Color.white);
        exitButton.setFont(new Font("Spectral", 2, 20));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setBounds(475,2,20,20);
        mouseListenerExitButton();
        MainPanel.add(exitButton);
    }
    private void MinimizeButton() {
        minimizeButton=new JLabel("-");
        minimizeButton.setForeground(Color.white);
        minimizeButton.setFont(new Font("Spectral", 2, 50));
        minimizeButton.setHorizontalAlignment(SwingConstants.CENTER);
        minimizeButton.setLocation(exitButton.getBounds().x-30, exitButton.getBounds().y-5);
        minimizeButton.setSize(20,20);
        mouseListenerMinimizeButton();
        MainPanel.add(minimizeButton);
        
        
    }
    private void BackgroundButton(){
        backgroundButton=new JLabel("Segundo Plano");
        backgroundButton.setLocation(30,(exitButton.getBounds().y)+5);
        backgroundButton.setSize(120,20);
        backgroundButton.setFont(new Font("Spectral",1,12));
        backgroundButton.setForeground(Color.gray);
        mouseListenerBackgroundButton();
        MainPanel.add(backgroundButton);
    }
   
    private void Alarm_Button(){
        alarmButton=new JLabel("Alarma");
        alarmButton.setFont(new Font("Segoe Print",1,15));
        alarmButton.setForeground(Color.gray);
        alarmButton.setHorizontalAlignment(SwingConstants.CENTER);
        alarmButton.setBounds(50,425,70,25);
        mouseListenerAlarmButton();
        MainPanel.add(alarmButton);
    }
    private void Timer_Button(){
       timerButton=new JLabel("Temporizador");
       timerButton.setFont(new Font("Segoe Print",1,18));
       timerButton.setBounds(177,425,165,25);
       timerButton.setForeground(Color.cyan);
       timerButton.setHorizontalAlignment(SwingConstants.CENTER);
       mouseListenerTimerButton();
       MainPanel.add(timerButton);
    }
    private void Clock_Button(){
        clockButton=new JLabel("Reloj");
        clockButton.setFont(new Font("Segoe Print",1,15));
        clockButton.setForeground(Color.gray);
        clockButton.setHorizontalAlignment(SwingConstants.CENTER);
        clockButton.setBounds(410,425,43,25);
        mouseListenerClockButton();
        MainPanel.add(clockButton);
    }
    
    private void Alarm_Interface(){
        //Instancias
        Sdays=new String[]{"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"};
        Smins=new String [60];
        mins=new Integer[60];
        Shours=new String [12];
        hours=new Integer[24];
        for(int i=0;i<60;i++) mins[i]=i;
        for(int i=0;i<24;i++) hours[i]=i+1;
        for(int i=0;i<12;i++) Shours[i]=alarm_time_toString(i+1);
        for(int i=0;i<mins.length;i++) Smins[i]=alarm_time_toString(i);
        alarm_list_thread=new LinkedList<>();
        alarm_list=new ArrayList<>();
        alarmTwoPointsMeridian=new JLabel(":");
        alarmScheduleButton=new JLabel("PROGRAMAR");
        alarmShowListButton=new JLabel("MOSTRAR ALARMAS");
        alarmPickMeridian=new JLabel("A.M");
        alarmPickMinute=new JLabel(Smins[0]);
        alarmPickHour=new JLabel(Shours[5]);
        alarmPickDay=new JLabel(Sdays[1]);
        alarm_day_count=1;
        alarm_hour_count=5;
        alarm_minute_count=0;
        
        //schedule Button
        alarmScheduleButton.setBounds(173,270,154,24);
        alarmScheduleButton.setOpaque(true);
        alarmScheduleButton.setFont(new Font("Cambria",1,22));
        alarmScheduleButton.setForeground(Color.WHITE);
        alarmScheduleButton.setBackground(Color.CYAN);
        alarmScheduleButton.setHorizontalAlignment(SwingConstants.CENTER);
        alarmScheduleButton.setVisible(false);
        mouseListenerAlarmScheduleButton();
        MainPanel.add(alarmScheduleButton);
        
        //showList Button
        alarmShowListButton.setBounds(185,320,130,22);
        alarmShowListButton.setOpaque(true);
        alarmShowListButton.setFont(new Font("Verdana",1,10));
        alarmShowListButton.setForeground(Color.decode("#9F6469"));
        alarmShowListButton.setBackground(Color.decode("#454546"));
        alarmShowListButton.setHorizontalAlignment(SwingConstants.CENTER);
        alarmShowListButton.setBorder(new LineBorder(Color.decode("#8A8D8F")));
        mouseListenerAlarmShowListButton();
        alarmShowListButton.setVisible(false);
        MainPanel.add(alarmShowListButton);
        
        //pickDay Button
        alarmPickDay.setBounds(30,205,160,35);
        alarmPickDay.setFont(new Font("Verdana",1,30));
        alarmPickDay.setForeground(new Color(124,242,224));
        alarmPickDay.setHorizontalAlignment(SwingConstants.CENTER);
        alarmPickDay.setVisible(false);
        mouseListenerAlarmPickDay();
        MainPanel.add(alarmPickDay);
        
        //pickHour Button
        alarmPickHour.setBounds(195,200,60,45);
        alarmPickHour.setFont(new Font("Verdana",1,40));
        alarmPickHour.setForeground(new Color(193,222,221));
        alarmPickHour.setHorizontalAlignment(SwingConstants.CENTER);
        alarmPickHour.setVisible(false);
        mouseListenerAlarmPickHour();
        MainPanel.add(alarmPickHour);
        
        //twoPointsMeridian Button
        alarmTwoPointsMeridian.setBounds(260,200,20,45);
        alarmTwoPointsMeridian.setFont(new Font("Verdana",1,40));
        alarmTwoPointsMeridian.setForeground(new Color(193,222,221));
        alarmTwoPointsMeridian.setHorizontalAlignment(SwingConstants.CENTER);
        alarmTwoPointsMeridian.setVisible(false);
        MainPanel.add(alarmTwoPointsMeridian);
        
        //pickMinute Button
        alarmPickMinute.setBounds(285,200,60,45);
        alarmPickMinute.setFont(new Font("Verdana",1,40));
        alarmPickMinute.setForeground(new Color(193,222,221));
        alarmPickMinute.setHorizontalAlignment(SwingConstants.CENTER);
        alarmPickMinute.setVisible(false);
        mouseListenerAlarmPickMinute();
        MainPanel.add(alarmPickMinute);
        
        //pickMeridian Button
        alarmPickMeridian.setBounds(355,210,40,35);
        alarmPickMeridian.setFont(new Font("Times",1,22));
        alarmPickMeridian.setForeground(Color.MAGENTA);
        alarmPickMeridian.setHorizontalAlignment(SwingConstants.CENTER);
        alarmPickMeridian.setVisible(false);
        mouseListenerAlarmPickMeridian();
        MainPanel.add(alarmPickMeridian);

    }
    private void Timer_Interface(){
        //Instancias
        int x, y, x1, y1, x2, y2;
        timer=new JLabel("00:00:00:00");
        timer_dhms=new JLabel("DD        HH         MM        SS");
        timer_reset_button=new JLabel("Reset");
        timer_start_stop_button= new JLabel("Start");

        //Timer
        timer.setHorizontalAlignment(SwingConstants.CENTER);
        timer.setForeground(Color.white);
        timer.setFont(new Font("Pacifico",1,40));
        timer.setBounds(110,135,280,45);
        MainPanel.add(timer);
        
        //Timer day hour minute secounds
        x=(timer.getLocation().x)+20;
        y=(timer.getLocation().y)+50;
        timer_dhms.setHorizontalAlignment(SwingConstants.CENTER);
        timer_dhms.setForeground(Color.gray);
        timer_dhms.setFont(new Font("Times",1,15));
        timer_dhms.setBounds(x, y, 240, 20);
        MainPanel.add(timer_dhms);
        
        //Timer reset Button
        x1=(timer_dhms.getLocation().x+140);
        y1=(timer_dhms.getLocation().y+65);
        timer_reset_button.setHorizontalAlignment(SwingConstants.CENTER);
        timer_reset_button.setFont(new Font("Times",1,19));
        timer_reset_button.setForeground(new Color(73,6,187));
        timer_reset_button.setLocation(x1,y1);
        timer_reset_button.setSize(65,30);
        mouseListenerTimerResetButton();
        MainPanel.add(timer_reset_button);
        
        //Timer star and stop Button
        x2=(timer_dhms.getLocation().x+40);
        y2=(timer_dhms.getLocation().y+65);
        timer_start_stop_button.setHorizontalAlignment(SwingConstants.CENTER);
        timer_start_stop_button.setFont(new Font("Times",1,19));
        timer_start_stop_button.setForeground(new Color(73,6,187));
        timer_start_stop_button.setLocation(x2,y2);
        timer_start_stop_button.setSize(65,30);
        mouseListenerTimerStartStopButton();
        MainPanel.add(timer_start_stop_button);
    }
    private void Clock_Interface(){
        //Instancias
        clock1=new JLabel("Reloj");
        clock2=new JLabel("Reloj");
        clock_button_12h=new JLabel("[12H");
        clock_button_24h=new JLabel("24H]");

        //Clock
        clock1.setHorizontalAlignment(SwingConstants.CENTER);
        clock1.setForeground(Color.magenta);
        clock1.setFont(new Font("Pacifico",1,40));
        clock1.setBounds(110,135,280,45);
        clock1.setVisible(false);
        MainPanel.add(clock1);
        
        clock2.setHorizontalAlignment(SwingConstants.CENTER);
        clock2.setForeground(Color.magenta);
        clock2.setFont(new Font("Pacifico",1,40));
        clock2.setBounds(110,135,280,45);
        clock2.setVisible(false);
        MainPanel.add(clock2);
        
        //Format Buttons
        clock_button_12h.setSize(55,25);
        clock_button_12h.setFont(new Font("Sitka",1,20));
        clock_button_12h.setHorizontalAlignment(SwingConstants.CENTER);
        clock_button_12h.setForeground(Color.gray);
        clock_button_12h.setLocation(clock1.getLocation().x+87, clock1.getLocation().y+55);
        clock_button_12h.setVisible(false);
        mouseListenerClockButton12h();
        MainPanel.add(clock_button_12h);
        
        clock_button_24h.setSize(55,25);
        clock_button_24h.setFont(new Font("Sitka",1,20));
        clock_button_24h.setHorizontalAlignment(SwingConstants.CENTER);
        clock_button_24h.setForeground(Color.gray);
        clock_button_24h.setLocation(clock1.getLocation().x+142, clock1.getLocation().y+55);
        clock_button_24h.setVisible(false);
        mouseListenerClockButton24h();
        MainPanel.add(clock_button_24h);
    }
    
    private void Alarm_Schedule(){
        if(alarmStatusMeridian){
            Alarma alarma=activeAlarm_Threads("nombre",Sdays[alarm_day_count],hours[alarm_hour_count],mins[alarm_minute_count]);
//            System.out.println("dia: "+Sdays[alarm_day_count]+"\nhour: "+hours[alarm_hour_count]+"\nminute: "+mins[alarm_minute_count]+"\nMERDIANO: "+alarmStatusMeridian);
            Thread alarma_Thread=new Thread(alarma,alarma.getAlarmName());
            alarma_Thread.start();
            alarm_list_thread.add(alarma_Thread);
        }else{
            Alarma alarma=activeAlarm_Threads("nombre",Sdays[alarm_day_count],hours[alarm_hour_count+12],mins[alarm_minute_count]);
//            System.out.println("dia: "+Sdays[alarm_day_count]+"\nhour: "+hours[alarm_hour_count+12]+"\nminute: "+mins[alarm_minute_count]);
            Thread alarma_Thread=new Thread(alarma,alarma.getAlarmName());
            alarma_Thread.start();
            alarm_list_thread.add(alarma_Thread);
        }
        
        if(!alarm_list_thread.isEmpty()) alarmShowListButton.setForeground(Color.decode("#E3242B"));

    }
    private String alarm_time_toString(int valor){
        String time_toString="";
        if(valor<10){
            time_toString+="0"+String.valueOf(valor);
        }else{
            time_toString=String.valueOf(valor);
        }
        return time_toString;
    }

    private void InitTrayIcon(){
        
        //Imagen del TrayIcon
        ImageIcon SystemImageIcon=new ImageIcon(this.getClass().getResource("/images/RelojIcon.png"));
        Image image=SystemImageIcon.getImage();
        
        //Menu del TrayIcon
        PopupMenu menu_for_TrayIcon=new PopupMenu();
        TI_tooltip_open=new MenuItem("Abrir");
        TI_tooltip_close=new MenuItem("Cerrar");
        menu_for_TrayIcon.add(TI_tooltip_open);
        menu_for_TrayIcon.addSeparator();
        menu_for_TrayIcon.add(TI_tooltip_close);
        
        //Instancia del TrayIcon
        trayicon=new TrayIcon(image,"Alarma",menu_for_TrayIcon);
        trayicon.setImageAutoSize(true);
        systemtray = SystemTray.getSystemTray();
        
        //ActionListeners del TrayIcon
        ActionListenerTrayIconOpenButton();
        ActionListenerTrayIconCloseButton();
        
        
    }
    
    //activacion de Threads
    private Alarma activeAlarm_Threads(String name, String day, int hour, int min){
        Alarma alarma=new Alarma(name, day, hour, min);
        return alarma;
    }
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
    
    public void setVisibleAlarmInterface(boolean status){
        if(status){
            alarmTwoPointsMeridian.setVisible(status);
            alarmScheduleButton.setVisible(status);   
            alarmShowListButton.setVisible(status);
            alarmPickMeridian.setVisible(status);
            alarmPickMinute.setVisible(status);
            alarmPickHour.setVisible(status);
            alarmPickDay.setVisible(status);
        }else{
            alarmTwoPointsMeridian.setVisible(status);
            alarmScheduleButton.setVisible(status);
            alarmShowListButton.setVisible(status);
            alarmPickMeridian.setVisible(status);  
            alarmPickMinute.setVisible(status);
            alarmPickHour.setVisible(status);
            alarmPickDay.setVisible(status);
            
        }
    }
    public void setVisibleTimerInterface(boolean status){
        if(status){
            timer.setVisible(status);
            timer_dhms.setVisible(status);
            timer_reset_button.setVisible(status);
            timer_start_stop_button.setVisible(status);
        }else{
            timer.setVisible(status);
            timer_dhms.setVisible(status);
            timer_reset_button.setVisible(status);
            timer_start_stop_button.setVisible(status);
        }
    }
    public void setVisibleClockInterface(boolean status){
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
    
    //actionlisteners
    private void ActionListenerTrayIconOpenButton(){
        TI_tooltip_open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                systemtray.remove(trayicon);
                setVisible(true);
            }
        });
    }
    private void ActionListenerTrayIconCloseButton(){
        TI_tooltip_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
    }
    
    //mouselisteners
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
    private void mouseListenerBackgroundButton(){
        MouseListener botonsegundoplano=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                try {
                    if(SystemTray.isSupported()){
                        systemtray.add(trayicon);
                        setVisible(false);
                    }
                } catch (AWTException ex) {
                    Logger.getLogger(Componentes.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                backgroundButton.setForeground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                backgroundButton.setForeground(Color.GRAY);
            }
        };
        backgroundButton.addMouseListener(botonsegundoplano);
    }
    
    private void mouseListenerAlarmButton(){
        MouseListener MLalarmL=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                timerButton.setFont(new Font("Segoe Print",1,15));
                clockButton.setFont(new Font("Segoe Print",1,15));
                if(!timerButton.getForeground().equals(Color.CYAN) || !clockButton.getForeground().equals(Color.CYAN)){
                    alarmButton.setForeground(Color.cyan);
                    timerButton.setForeground(Color.gray);
                    clockButton.setForeground(Color.gray);
                }
                if(alarmButton.getForeground().equals(Color.CYAN)){
                    setVisibleTimerInterface(false);
                    setVisibleClockInterface(false);
                    setVisibleAlarmInterface(true);
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
                    setVisibleAlarmInterface(false);
                    setVisibleClockInterface(false);
                    setVisibleTimerInterface(true);
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
    private void mouseListenerClockButton(){
        MouseListener MLclockL=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                timerButton.setFont(new Font("Segoe Print",1,15));
                alarmButton.setFont(new Font("Segoe Print",1,15));
                if(!timerButton.getForeground().equals(Color.CYAN) || !alarmButton.getForeground().equals(Color.CYAN)){
                    clockButton.setForeground(Color.cyan);
                    alarmButton.setForeground(Color.gray);
                    timerButton.setForeground(Color.gray);
                }
                if(clockButton.getForeground().equals(Color.CYAN)){
                    setVisibleAlarmInterface(false);
                    setVisibleTimerInterface(false);
                    if(clock_format_status) {
                        clock1.setVisible(true);
                    }else{
                        clock2.setVisible(true);
                    }
                    clock_button_24h.setVisible(true);
                    clock_button_12h.setVisible(true);
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
    
    private void mouseListenerAlarmPickDay(){
        MouseListener alarmpickday = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(alarm_day_count<6){
                    alarm_day_count++;
                    alarmPickDay.setText(Sdays[alarm_day_count]);
                }else{
                    alarm_day_count=0;
                    alarmPickDay.setText(Sdays[alarm_day_count]);
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        };
        alarmPickDay.addMouseListener(alarmpickday);
    }
    private void mouseListenerAlarmPickHour(){
        MouseListener alarmpickhour = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(alarm_hour_count<11){
                    alarm_hour_count++;
                    alarmPickHour.setText(Shours[alarm_hour_count]);
                }else{
                    alarm_hour_count=0;
                    alarmPickHour.setText(Shours[alarm_hour_count]);
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        };
        alarmPickHour.addMouseListener(alarmpickhour);

      
    }
    private void mouseListenerAlarmPickMinute(){
        MouseListener alarmpickminute = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(alarm_minute_count<59){
                    alarm_minute_count++;
                    alarmPickMinute.setText(Smins[alarm_minute_count]);
                }else{
                    alarm_minute_count=0;
                    alarmPickMinute.setText(Smins[alarm_minute_count]);
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        };
        alarmPickMinute.addMouseListener(alarmpickminute);
    }
    private void mouseListenerAlarmPickMeridian(){
        MouseListener alarmpickmeridan = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                 if(alarmStatusMeridian){
                    alarmStatusMeridian=false;
                    alarmPickMeridian.setText("P.M");
                }else{
                    alarmStatusMeridian=true;
                    alarmPickMeridian.setText("A.M");
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }

           
        };
        alarmPickMeridian.addMouseListener(alarmpickmeridan);
    }
    private void mouseListenerAlarmScheduleButton(){
        MouseListener alarmschedulebutton = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Mensaje m=new Mensaje();
                m.setVisible(true);
                Alarm_Schedule();
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                alarmScheduleButton.setFont(new Font("Cambria",1,24));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                alarmScheduleButton.setFont(new Font("Cambria",1,22));
            }
        };
        alarmScheduleButton.addMouseListener(alarmschedulebutton);
    }
    private void mouseListenerAlarmShowListButton(){
        MouseListener showlistbutton = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(alarm_list_thread.isEmpty()){
                    alarmShowListButton.setForeground(Color.decode("#9F6469"));
                }else{
                    alarmShowListButton.setForeground(Color.decode("#E3242B"));
                }
                new Lista().setVisible(true);
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        };
        
        alarmShowListButton.addMouseListener(showlistbutton);
    }
    
    private void mouseListenerTimerResetButton(){
        MouseListener resetbutton=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                if(timer_default_resetbutton && !"00:00:00:00".equals(timer.getText())){
                    Temporizador.Reset();
                }
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(timer_default_resetbutton && !"00:00:00:00".equals(timer.getText()) ){
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
    private void mouseListenerTimerStartStopButton(){
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
    
    private void mouseListenerClockButton12h(){
        MouseListener Button12h=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                clock_format_status=false;
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
    private void mouseListenerClockButton24h(){
        MouseListener Button24h=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                clock_format_status=true;
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
    
    private void watchLayouts(){
        alarmButton.setOpaque(true);
        alarm.setOpaque(true);
        alarmPickDay.setOpaque(true);
        alarmPickHour.setOpaque(true);
        alarmPickMinute.setOpaque(true);
        alarmPickMeridian.setOpaque(true);
        alarmTwoPointsMeridian.setOpaque(true);
        clockButton.setOpaque(true);
        clock1.setOpaque(true);
        clock2.setOpaque(true);
        timerButton.setOpaque(true);
        timer.setOpaque(true);
        timer_dhms.setOpaque(true);
        timer_reset_button.setOpaque(true);
        timer_start_stop_button.setOpaque(true);
        exitButton.setOpaque(true);
        minimizeButton.setOpaque(true);
        clock_button_12h.setOpaque(true);
        clock_button_24h.setOpaque(true);
        
    }
    
}