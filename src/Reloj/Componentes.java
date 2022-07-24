package Reloj;

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;  
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Componentes extends JFrame {
    
    private JPanel MainPanel;
    private JLabel alarmButton, chronometerButton, clockButton, exitButton, minimizeButton, backgroundButton;
    private JLabel alarmPickDay, alarmPickHour, alarmPickMinute, alarmPickMeridian, alarmTwoPointsMeridian, alarmScheduleButton;
    private Thread chronometer_thread , clock1_thread, clock2_thread;
    private TrayIcon trayicon;
    private MenuItem TI_tooltip_open, TI_tooltip_close;
    private SystemTray systemtray;
    private Runnable chronometer_runnable, clock1_runnable, clock2_runnable;
    private int alarm_day_count, alarm_hour_count, alarm_minute_count;
    private boolean clock_format_status, clock_default_color_buttons=true, alarmStatusMeridian=true;
    private boolean chronometer_default_startstopbutton=true, chronometer_default_resetbutton=false, chronometer_thread_is_not_start=true;
    private MensajeDeAyuda Help_Message=null;
    private String []Smins, Shours, Sdays;
    private Integer []mins, hours;
    public static ListaAlarmas list_of_alarms;
    public static JLabel clock1, clock2;
    public static LinkedList<Alarma> alarm_list;
    public static JLabel chronometer;
    public int alarmThread_count;
    public LinkedList<Thread> alarm_list_thread;
    public JLabel alarm, alarmShowListButton, alarmRoundedBorder, clock_button_12h, clock_button_24h;
    public JLabel  chronometer_reset_button, chronometer_start_stop_button, chronometer_dhms;
    public JTextField alarmGetNameJTextField;
    

    public Componentes(){
        InitComponents(); // al estar en primera linea del metodo precarga los componentes
        setSize(500,500);
        setResizable(false);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setTitle("Alarma");
        setIconImage(new ImageIcon(getClass().getResource("/images/RelojIcon.png")).getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
       
    }
    private void InitComponents(){
        
        InputPanel();
        Close_Button();
        Minimize_Button();
        Background_Button();
        InitTrayIcon();
        
        Alarm_Button();
        Chronometer_Button();
        Clock_Button();
        
        Alarm_Interface();
        Chronometer_Interface();
        Clock_Interface();
        
        //creacion y activacion de hilos
        activeChronometer_Threads();
        activeClock_Threads();
        
//        watchLayouts();
    } 
    private void InputPanel(){
        MainPanel=new JPanel();
        MainPanel.setSize(500,500);
        MainPanel.setBackground(Color.decode("#202225"));
        MainPanel.setLayout(null);
        MainPanel.setBorder(new LineBorder(new Color(72,71,73)));
        mouseListenerInputPanel();
        add(MainPanel); //Solamente para versiones de java superiores a la 5.0
//        this.getContentPane().add(ventana); // para versiones de java inferiores a 5.0 (aun sirve en versiones actuales)
        
    }
    private void Close_Button() {
        exitButton=new JLabel("•");
        exitButton.setForeground(Color.decode("#c20000"));
        exitButton.setFont(new Font("Spectral",1,25));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setVerticalAlignment(SwingConstants.CENTER);
        exitButton.setBounds(475,4,18,26);
        mouseListenerExitButton();
        MainPanel.add(exitButton);
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
        MainPanel.add(minimizeButton);
        
        
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
    private void Chronometer_Button(){
       chronometerButton=new JLabel("Cronometro");
       chronometerButton.setFont(new Font("Segoe Print",1,18));
       chronometerButton.setBounds(170,425,160,25);
       chronometerButton.setForeground(Color.cyan);
       chronometerButton.setHorizontalAlignment(SwingConstants.CENTER);
       mouseListenerChronometerButton();
       MainPanel.add(chronometerButton);
    }
    private void Clock_Button(){
        clockButton=new JLabel("Reloj");
        clockButton.setFont(new Font("Segoe Print",1,15));
        clockButton.setForeground(Color.gray);
        clockButton.setHorizontalAlignment(SwingConstants.CENTER);
        clockButton.setBounds(380,425,70,25);
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
        list_of_alarms=new ListaAlarmas();
        for(int i=0;i<60;i++) mins[i]=i;
        for(int i=0;i<24;i++) hours[i]=i+1;
        for(int i=0;i<12;i++) Shours[i]=alarm_time_toString(i+1);
        for(int i=0;i<mins.length;i++) Smins[i]=alarm_time_toString(i);
        alarm_list_thread=new LinkedList<>();
        alarm_list=new LinkedList<>();
        alarmGetNameJTextField=new JTextField("Nombre de la Alarma");        
        alarmTwoPointsMeridian=new JLabel(":");
        alarmScheduleButton=new JLabel("PROGRAMAR");
        alarmShowListButton=new JLabel("Mostrar Alarmas");
        alarmRoundedBorder=new JLabel();
        alarmPickMeridian=new JLabel("A.M");
        alarmPickMinute=new JLabel(Smins[0]);
        alarmPickHour=new JLabel(Shours[5]);
        alarmPickDay=new JLabel(Sdays[1]);
        alarm_day_count=1;
        alarm_hour_count=5;
        alarm_minute_count=0;
        alarmThread_count=0;
        
        //get name JTextField
        alarmGetNameJTextField.setBounds(130, 115, 240, 30);
        alarmGetNameJTextField.setOpaque(true);
        alarmGetNameJTextField.setFont(new Font("Roboto",100,21));
        alarmGetNameJTextField.setForeground(Color.decode("#808080"));
        alarmGetNameJTextField.setBackground(Color.decode("#393939"));
        alarmGetNameJTextField.setCaretColor(Color.decode("#808080"));
        alarmGetNameJTextField.setBorder(new LineBorder(Color.decode("#393939"), 1, true));
        alarmGetNameJTextField.setToolTipText("Etiqueta de la alarma");
        alarmGetNameJTextField.setHorizontalAlignment(SwingConstants.CENTER);
        alarmGetNameJTextField.setVisible(false);
        keyListenerAlarmGetNameField();
        mouseListenerAlarmGetNameJTextField();
        MainPanel.add(alarmGetNameJTextField);
        
        //Rounded Border
        alarmRoundedBorder.setLocation(alarmGetNameJTextField.getLocation().x-1,alarmGetNameJTextField.getLocation().y-2);
        alarmRoundedBorder.setSize(alarmGetNameJTextField.getSize().width+2, alarmGetNameJTextField.getSize().height+2);
        alarmRoundedBorder.setOpaque(true);
        alarmRoundedBorder.setBackground(Color.decode("#202225"));
        alarmRoundedBorder.setBorder(new LineBorder(Color.decode("#393939"), 2, true));
        alarmRoundedBorder.setVisible(false);
        MainPanel.add(alarmRoundedBorder);
        
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
        alarmShowListButton.setFont(new Font("Segoe UI",1,13));
        alarmShowListButton.setForeground(Color.decode("#9F6469"));
        alarmShowListButton.setHorizontalAlignment(SwingConstants.CENTER);
        alarmShowListButton.setBorder(new LineBorder(Color.decode("#8A8D8F")));
        mouseListenerAlarmShowListButton();
        alarmShowListButton.setVisible(false);
        MainPanel.add(alarmShowListButton);
        
        //pickDay Button
        alarmPickDay.setBounds(30,205,160,35);
        alarmPickDay.setFont(new Font("Verdana",1,28));
        alarmPickDay.setForeground(new Color(124,242,224));
        alarmPickDay.setHorizontalAlignment(SwingConstants.CENTER);
        alarmPickDay.setVisible(false);
        keyListenerAlarmPickDay();
        mouseListenerAlarmPickDay();
        MainPanel.add(alarmPickDay);
        
        //pickHour 
        alarmPickHour.setBounds(195,200,60,45);
        alarmPickHour.setFont(new Font("Verdana",1,38));
        alarmPickHour.setForeground(Color.decode("#C1DEDD"));
        alarmPickHour.setToolTipText("Digite con el teclado la hora de la alarma.");
        alarmPickHour.setHorizontalAlignment(SwingConstants.CENTER);
        alarmPickHour.setVisible(false);
        mouseListenerAlarmPickHour();
        keyListenerAlarmPickHour();
        MainPanel.add(alarmPickHour);
        
        //twoPointsMeridian Button
        alarmTwoPointsMeridian.setBounds(260,200,20,45);
        alarmTwoPointsMeridian.setFont(new Font("Verdana",1,40));
        alarmTwoPointsMeridian.setForeground(new Color(193,222,221));
        alarmTwoPointsMeridian.setHorizontalAlignment(SwingConstants.CENTER);
        alarmTwoPointsMeridian.setVisible(false);
        MainPanel.add(alarmTwoPointsMeridian);
        
        //pickMinute 
        alarmPickMinute.setBounds(285,200,60,45);
        alarmPickMinute.setFont(new Font("Verdana",1,38));
        alarmPickMinute.setForeground(Color.decode("#C1DEDD"));
        alarmPickMinute.setToolTipText("Digite con el teclado los minutos de la alarma.");
        alarmPickMinute.setHorizontalAlignment(SwingConstants.CENTER);
        alarmPickMinute.setVisible(false);
        mouseListenerAlarmPickMinute();
        keyListenerAlarmPickMinute();
        MainPanel.add(alarmPickMinute);
        
        //pickMeridian Button
        alarmPickMeridian.setBounds(355,210,40,35);
        alarmPickMeridian.setFont(new Font("Times",1,20));
        alarmPickMeridian.setForeground(Color.decode("#9C33FF"));
        alarmPickMeridian.setHorizontalAlignment(SwingConstants.CENTER);
        alarmPickMeridian.setVisible(false);
        keyListenerAlarmPickMeridian();
        mouseListenerAlarmPickMeridian();
        MainPanel.add(alarmPickMeridian);

    }
    private void Chronometer_Interface(){
        //Instancias
        int x, y, x1, y1, x2, y2;
        chronometer=new JLabel("00:00:00:00");
        chronometer_dhms=new JLabel("DD        HH         MM        SS");
        chronometer_reset_button=new JLabel("Reiniciar");
        chronometer_start_stop_button= new JLabel("Iniciar");

        //Timer
        chronometer.setHorizontalAlignment(SwingConstants.CENTER);
        chronometer.setForeground(Color.white);
        chronometer.setFont(new Font("Pacifico",1,40));
        chronometer.setBounds(110,135,280,45);
        MainPanel.add(chronometer);
        
        //Timer day hour minute secounds
        x=(chronometer.getLocation().x)+20;
        y=(chronometer.getLocation().y)+50;
        chronometer_dhms.setHorizontalAlignment(SwingConstants.CENTER);
        chronometer_dhms.setForeground(Color.gray);
        chronometer_dhms.setFont(new Font("Times",1,15));
        chronometer_dhms.setBounds(x, y, 240, 20);
        MainPanel.add(chronometer_dhms);
        
        //Timer reset Button
        x1=(chronometer_dhms.getLocation().x+150);
        y1=(chronometer_dhms.getLocation().y+65);
        chronometer_reset_button.setHorizontalAlignment(SwingConstants.CENTER);
        chronometer_reset_button.setFont(new Font("Times",1,19));
        chronometer_reset_button.setForeground(Color.decode("#5858C9"));
        chronometer_reset_button.setLocation(x1,y1);
        chronometer_reset_button.setSize(110,30);
        mouseListenerChronometerResetButton();
        MainPanel.add(chronometer_reset_button);
        
        //Timer star and stop Button
        x2=(chronometer_dhms.getLocation().x-20);
        y2=(chronometer_dhms.getLocation().y+65);
        chronometer_start_stop_button.setHorizontalAlignment(SwingConstants.CENTER);
        chronometer_start_stop_button.setFont(new Font("Times",1,19));
        chronometer_start_stop_button.setForeground(Color.decode("#5858C9"));
        chronometer_start_stop_button.setLocation(x2,y2);
        chronometer_start_stop_button.setSize(110,30);
        mouseListenerChronometerStartStopButton();
        MainPanel.add(chronometer_start_stop_button);
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
    private void activeChronometer_Threads(){
        chronometer_runnable=new Cronometro();
        chronometer_thread=new Thread(chronometer_runnable);
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
            alarmGetNameJTextField.setVisible(status);
            alarmTwoPointsMeridian.setVisible(status);
            alarmScheduleButton.setVisible(status);   
            alarmShowListButton.setVisible(status);
            alarmRoundedBorder.setVisible(status);
            alarmPickMeridian.setVisible(status);
            alarmPickMinute.setVisible(status);
            alarmPickHour.setVisible(status);
            alarmPickDay.setVisible(status);
        }else{
            alarmGetNameJTextField.setVisible(status);
            alarmTwoPointsMeridian.setVisible(status);
            alarmScheduleButton.setVisible(status);
            alarmShowListButton.setVisible(status);
            alarmRoundedBorder.setVisible(status);
            alarmPickMeridian.setVisible(status);  
            alarmPickMinute.setVisible(status);
            alarmPickHour.setVisible(status);
            alarmPickDay.setVisible(status);
            
        }
    }
    public void setVisibleChronometerInterface(boolean status){
        if(status){
            chronometer.setVisible(status);
            chronometer_dhms.setVisible(status);
            chronometer_reset_button.setVisible(status);
            chronometer_start_stop_button.setVisible(status);
        }else{
            chronometer.setVisible(status);
            chronometer_dhms.setVisible(status);
            chronometer_reset_button.setVisible(status);
            chronometer_start_stop_button.setVisible(status);
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
    
    //keylisteners
    private void keyListenerAlarmPickDay(){
        KeyListener KL=new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                switch(e.getKeyChar()){
                    case '1':
                        alarmPickDay.setText(Sdays[0]);
                        alarm_day_count=0;
                        break;
                    case '2':
                        alarmPickDay.setText(Sdays[1]);
                        alarm_day_count=1;
                        break;
                    case '3':
                        alarmPickDay.setText(Sdays[2]);
                        alarm_day_count=2;
                        break;
                    case '4':
                        alarmPickDay.setText(Sdays[3]);
                        alarm_day_count=3;
                        break;
                    case '5':
                        alarmPickDay.setText(Sdays[4]);
                        alarm_day_count=4;
                        break;
                    case '6':
                        alarmPickDay.setText(Sdays[5]);
                        alarm_day_count=5;
                        break;
                    case '7':
                        alarmPickDay.setText(Sdays[6]);
                        alarm_day_count=6;
                        break;
                    case 'l':
                        alarmPickDay.setText(Sdays[0]);
                        alarm_day_count=0;
                        break;
                    case 'm':
                        alarmPickDay.setText(Sdays[1]);
                        alarm_day_count=1;
                        break;
                    case 'j':
                        alarmPickDay.setText(Sdays[3]);
                        alarm_day_count=3;
                        break;
                    case 'v':
                        alarmPickDay.setText(Sdays[4]);
                        alarm_day_count=4;
                        break;
                    case 's':
                        alarmPickDay.setText(Sdays[5]);
                        alarm_day_count=5;
                        break;
                    case 'd':
                        alarmPickDay.setText(Sdays[6]);
                        alarm_day_count=6;
                        break;
                }
            }
        };
        alarmPickDay.addKeyListener(KL);
    }
    private void keyListenerAlarmPickHour(){
        KeyListener KL=new KeyAdapter() {
            int e1,ef=0;
            public void keyPressed(KeyEvent e){
                if(ef==1 &&(e.getKeyChar()=='0' || e.getKeyChar()=='1' || e.getKeyChar()=='2')){
                    switch (e.getKeyChar()) {
                        case '0':
                            alarmPickHour.setText(Shours[9]);
                            alarm_hour_count=9;
                            ef=0;
                            break;
                        case '1':
                            alarmPickHour.setText(Shours[10]);
                            alarm_hour_count=10;
                            ef=0;
                            break;
                        case '2':
                            alarmPickHour.setText(Shours[11]);
                            alarm_hour_count=11;
                            ef=0;
                            break;
                    }
                }else{
                    switch (e.getKeyChar()) {
                        case '1':
                            alarmPickHour.setText(Shours[0]);
                            alarm_hour_count=0;
                            ef=1;
                            break;
                        case '2':
                            alarmPickHour.setText(Shours[1]);
                            alarm_hour_count=1;
                            break;
                        case '3':
                            alarmPickHour.setText(Shours[2]);
                            alarm_hour_count=2;
                            break;
                        case '4':
                            alarmPickHour.setText(Shours[3]);
                            alarm_hour_count=3;
                            break;
                        case '5':
                            alarmPickHour.setText(Shours[4]);
                            alarm_hour_count=4;
                            break;
                        case '6':
                            alarmPickHour.setText(Shours[5]);
                            alarm_hour_count=5;
                            break;
                        case '7':
                            alarmPickHour.setText(Shours[6]);
                            alarm_hour_count=6;
                            break;
                        case '8':
                            alarmPickHour.setText(Shours[7]);
                            alarm_hour_count=7;
                            break;
                        case '9':
                            alarmPickHour.setText(Shours[8]);
                            alarm_hour_count=8;
                            break;
                    }
                }
                
            }
        };
        alarmPickHour.addKeyListener(KL);
    }
    private void keyListenerAlarmPickMinute(){
        KeyListener KL=new KeyAdapter() {
            int e1,ef=0;
            public void keyPressed(KeyEvent e){
                if(ef==1){
                    switch (e.getKeyChar()) {
                        case '0':
                            alarmPickMinute.setText(Smins[(e1*10)+0]);
                            alarm_minute_count=(e1*10)+0;
                            ef=0;
                            break;
                        case '1':
                            alarmPickMinute.setText(Smins[(e1*10)+1]);
                            alarm_minute_count=(e1*10)+1;
                            ef=0;
                            break;
                        case '2':
                            alarmPickMinute.setText(Smins[(e1*10)+2]);
                            alarm_minute_count=(e1*10)+2;
                            ef=0;
                            break;
                        case '3':
                            alarmPickMinute.setText(Smins[(e1*10)+3]);
                            alarm_minute_count=(e1*10)+3;
                            ef=0;
                            break;
                        case '4':
                            alarmPickMinute.setText(Smins[(e1*10)+4]);
                            alarm_minute_count=(e1*10)+4;
                            ef=0;
                            break;
                        case '5':
                            alarmPickMinute.setText(Smins[(e1*10)+5]);
                            alarm_minute_count=(e1*10)+5;
                            ef=0;
                            break;
                        case '6':
                            alarmPickMinute.setText(Smins[(e1*10)+6]);
                            alarm_minute_count=(e1*10)+6;
                            ef=0;
                            break;
                        case '7':
                            alarmPickMinute.setText(Smins[(e1*10)+7]);
                            alarm_minute_count=(e1*10)+7;
                            ef=0;
                            break;
                        case '8':
                            alarmPickMinute.setText(Smins[(e1*10)+8]);
                            alarm_minute_count=(e1*10)+8;
                            ef=0;
                            break;
                        case '9':
                            alarmPickMinute.setText(Smins[(e1*10)+9]);
                            alarm_minute_count=(e1*10)+9;
                            ef=0;
                            break;
                    }
                }else{
                    switch (e.getKeyChar()) {
                        case '0':
                            alarmPickMinute.setText(Smins[0]);
                            alarm_minute_count=0;
                            e1=0;
                            ef=1;
                            break;
                        case '1':
                            alarmPickMinute.setText(Smins[1]);
                            alarm_minute_count=1;
                            e1=1;
                            ef=1;
                            break;
                        case '2':
                            alarmPickMinute.setText(Smins[2]);
                            alarm_minute_count=2;
                            e1=2;
                            ef=1;
                            break;
                        case '3':
                            alarmPickMinute.setText(Smins[3]);
                            alarm_minute_count=3;
                            e1=3;
                            ef=1;
                            break;
                        case '4':
                            alarmPickMinute.setText(Smins[4]);
                            alarm_minute_count=4;
                            e1=4;
                            ef=1;
                            break;
                        case '5':
                            alarmPickMinute.setText(Smins[5]);
                            alarm_minute_count=5;
                            e1=5;
                            ef=1;
                            break;
                        case '6':
                            alarmPickMinute.setText(Smins[6]);
                            alarm_minute_count=6;
                            e1=6;
                            break;
                        case '7':
                            alarmPickMinute.setText(Smins[7]);
                            alarm_minute_count=7;
                            e1=7;
                            break;
                        case '8':
                            alarmPickMinute.setText(Smins[8]);
                            alarm_minute_count=8;
                            e1=8;
                            break;
                        case '9':
                            alarmPickMinute.setText(Smins[9]);
                            alarm_minute_count=9;
                            e1=9;
                            break;
                    }
                }
                
            }
        };
        alarmPickMinute.addKeyListener(KL);

    }
    private void keyListenerAlarmGetNameField(){
        KeyListener KL=new KeyAdapter() {
            public void keyTyped(KeyEvent e){
                if(alarmGetNameJTextField.getText().length()>=40){
                    e.consume();
                }
            }
            public void keyPressed(KeyEvent e){
                if(alarmGetNameJTextField.getText()!=""){
                    if(e.getKeyCode()==10){
                        alarmGetNameJTextField.setFocusable(false);
                        alarmGetNameJTextField.setFont(new Font("Roboto",100,21));
                        alarmGetNameJTextField.setCaretPosition(0);
                    }
                }
                if(alarmGetNameJTextField.getText().equals("")){
                    if(e.getKeyCode()==10){
                        alarmGetNameJTextField.setText("Nombre de la Alarma");
                        alarmGetNameJTextField.setFocusable(false);
                    }
                }
                if(alarmGetNameJTextField.getText().length()>40){
                    alarmGetNameJTextField.setText(alarmGetNameJTextField.getText().substring(0, 40));
                }
            }
        };
        alarmGetNameJTextField.addKeyListener(KL);
    }
    private void keyListenerAlarmPickMeridian(){
        KeyListener KL=new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                switch(e.getKeyChar()){
                    case 'a':
                        alarmStatusMeridian=true;
                        alarmPickMeridian.setText("A.M");
                        break;
                    case 'p':
                        alarmStatusMeridian=false;
                        alarmPickMeridian.setText("P.M");
                        break;
                }
            }
        };
        alarmPickMeridian.addKeyListener(KL);
    }
    
    //mouselisteners
    private void mouseListenerInputPanel(){
        MouseListener IPML=new MouseAdapter() {
            public void mouseClicked(MouseEvent me){
                alarmGetNameJTextField.setCaretPosition(0);
                alarmGetNameJTextField.setFont(new Font("Roboto",100,21));
                alarmGetNameJTextField.setFocusable(false);
                if(alarmGetNameJTextField.getText().equals("")){
                    alarmGetNameJTextField.setText("Nombre de la Alarma");
                }
            }
            public void mouseEntered(MouseEvent me){
                if(!alarmGetNameJTextField.isVisible()){
                    alarmGetNameJTextField.setCaretPosition(0);
                    alarmGetNameJTextField.setFont(new Font("Roboto",100,21));
                    if(alarmGetNameJTextField.getText().equals("")){
                        alarmGetNameJTextField.setText("Nombre de la Alarma");
                    }
                }
            }
        };
        MainPanel.addMouseListener(IPML);
    }
    private void mouseListenerExitButton(){
        MouseListener botoncierrre;
        botoncierrre = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(alarm_list.isEmpty()){
                    System.exit(0);
                }else{
                    Mensaje m = new Mensaje("Se han desactivado las alarmas.");
                    m.setVisible(true);
                }
            }
            @Override
            public void mousePressed(MouseEvent me) {
                exitButton.setForeground(Color.RED);
                exitButton.setFont(new Font("Spectral",1,27));
                
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
                minimizeButton.setFont(new Font("Spectral",1,27));
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                minimizeButton.setForeground(Color.decode("#ffeb00"));
                Help_Message=new MensajeDeAyuda("Minimizar");
                if(!Help_Message.isVisible()) Help_Message.setVisible(true);
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                minimizeButton.setForeground(Color.decode("#efb810"));
                minimizeButton.setFont(new Font("Spectral",1,25));
                Help_Message.setVisible(false);
                Help_Message=null;
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
                backgroundButton.setFont(new Font("Spectral",1,27));
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                backgroundButton.setForeground(Color.LIGHT_GRAY);
                Help_Message=new MensajeDeAyuda("Segundo Plano");
                if(!Help_Message.isVisible()) Help_Message.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                backgroundButton.setForeground(Color.GRAY);
                backgroundButton.setFont(new Font("Spectral",1,25));
                Help_Message.setVisible(false);
                Help_Message=null;
            }
        };
        backgroundButton.addMouseListener(botonsegundoplano);
    }
    
    private void mouseListenerAlarmButton(){
        MouseListener MLalarmL=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                chronometerButton.setFont(new Font("Segoe Print",1,15));
                clockButton.setFont(new Font("Segoe Print",1,15));
                if(!chronometerButton.getForeground().equals(Color.CYAN) || !clockButton.getForeground().equals(Color.CYAN)){
                    alarmButton.setForeground(Color.cyan);
                    chronometerButton.setForeground(Color.gray);
                    clockButton.setForeground(Color.gray);
                }
                if(alarmButton.getForeground().equals(Color.CYAN)){
                    setVisibleChronometerInterface(false);
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
    private void mouseListenerChronometerButton(){
        MouseListener MLtimerL=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                alarmButton.setFont(new Font("Segoe Print",1,15));
                clockButton.setFont(new Font("Segoe Print",1,15));
                if(!alarmButton.getForeground().equals(Color.CYAN) || !clockButton.getForeground().equals(Color.CYAN)){
                    chronometerButton.setForeground(Color.cyan);
                    alarmButton.setForeground(Color.gray);
                    clockButton.setForeground(Color.gray);
                }
                if(chronometerButton.getForeground().equals(Color.CYAN)){
                    setVisibleAlarmInterface(false);
                    setVisibleClockInterface(false);
                    setVisibleChronometerInterface(true);
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
                if(!chronometerButton.getForeground().equals(Color.cyan)){
                    chronometerButton.setFont(new Font("Segoe Print",1,18));
                    chronometerButton.setForeground(Color.white);
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(!chronometerButton.getForeground().equals(Color.cyan)){
                    chronometerButton.setFont(new Font("Segoe Print",1,15));
                    chronometerButton.setForeground(Color.gray);
                }
            }
        };
        chronometerButton.addMouseListener(MLtimerL);
    }
    private void mouseListenerClockButton(){
        MouseListener MLclockL=new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                chronometerButton.setFont(new Font("Segoe Print",1,15));
                alarmButton.setFont(new Font("Segoe Print",1,15));
                if(!chronometerButton.getForeground().equals(Color.CYAN) || !alarmButton.getForeground().equals(Color.CYAN)){
                    clockButton.setForeground(Color.cyan);
                    alarmButton.setForeground(Color.gray);
                    chronometerButton.setForeground(Color.gray);
                }
                if(clockButton.getForeground().equals(Color.CYAN)){
                    setVisibleAlarmInterface(false);
                    setVisibleChronometerInterface(false);
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
        MouseListener alarmpickday = new MouseAdapter() {
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
            public void mouseEntered(MouseEvent me) {
                alarmPickDay.grabFocus();
                alarmPickDay.setForeground(new Color(124,242,240));
                if(alarmGetNameJTextField.getText().equals("")){
                    alarmGetNameJTextField.setText("Nombre de la Alarma");
                }
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                alarmPickDay.setForeground(new Color(124,242,224));
            }
        };
        alarmPickDay.addMouseListener(alarmpickday);
    }
    private void mouseListenerAlarmPickHour(){
        MouseListener alarmpickhour = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                alarmPickHour.grabFocus();
                alarmPickHour.setFont(new Font("Verdana",1,40));
                alarmPickHour.setForeground(Color.decode("#C1DEFF"));
                if(alarmGetNameJTextField.getText().equals("")){
                    alarmGetNameJTextField.setText("Nombre de la Alarma");
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                alarmPickHour.setFont(new Font("Verdana",1,40));
                alarmPickHour.setForeground(Color.decode("#C1DEDD"));
            }
        };
        alarmPickHour.addMouseListener(alarmpickhour);

      
    }
    private void mouseListenerAlarmPickMinute(){
        MouseListener alarmpickminute = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                alarmPickMinute.grabFocus();
                alarmPickMinute.setFont(new Font("Verdana",1,40));
                alarmPickMinute.setForeground(Color.decode("#C1DEFF"));
                if(alarmGetNameJTextField.getText().equals("")){
                    alarmGetNameJTextField.setText("Nombre de la Alarma");
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                alarmPickMinute.setFont(new Font("Verdana",1,40));
                alarmPickMinute.setForeground(Color.decode("#C1DEDD"));
            }
        };
        alarmPickMinute.addMouseListener(alarmpickminute);
    }
    private void mouseListenerAlarmPickMeridian(){
        MouseListener alarmpickmeridan = new MouseAdapter() {
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
            public void mouseEntered(MouseEvent me){
                alarmPickMeridian.grabFocus();
                alarmPickMeridian.setForeground(Color.decode("#BB33FF"));
                if(alarmGetNameJTextField.getText().equals("")){
                    alarmGetNameJTextField.setText("Nombre de la Alarma");
                }
            }
            @Override
            public void mouseExited(MouseEvent me){
                alarmPickMeridian.setForeground(Color.decode("#9C33FF"));
            }
        };
        alarmPickMeridian.addMouseListener(alarmpickmeridan);
    }
    private void mouseListenerAlarmScheduleButton(){
        MouseListener alarmschedulebutton = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                alarmThread_count++;
                if(alarmThread_count<=7){
                    Alarma alarma;
                    if(alarmStatusMeridian) alarma=new Alarma(alarmGetNameJTextField.getText(),Sdays[alarm_day_count],hours[alarm_hour_count],mins[alarm_minute_count]);
                    else alarma=new Alarma(alarmGetNameJTextField.getText(),Sdays[alarm_day_count],hours[alarm_hour_count+12],mins[alarm_minute_count]);
                    
                    Alarma.setCount(alarmThread_count);
                    Thread alarma_Thread=new Thread(alarma,alarma.getAlarmName());
                    alarma_Thread.start();
                    alarm_list.add(alarma);
                    alarm_list_thread.add(alarma_Thread);
                    list_of_alarms.addAlarm(new AlarmaGrafica(alarma.getAlarmName(),alarma.getTime(),alarma.getDay()));
                    alarmShowListButton.setEnabled(true);
                    Mensaje m=new Mensaje("La alarma se ha programado");
                    m.setVisible(true); 
                }else{
                    if(Alarma.getCount()==7){
                        Mensaje m=new Mensaje("Posee suficientes alarmas programadas");
                        m.setVisible(true);                        
                    }else{
                        alarmThread_count=6;
                        mouseClicked(me);
                    }
                }
                if(!alarm_list_thread.isEmpty() || !alarm_list.isEmpty()){
                    alarmShowListButton.setForeground(Color.decode("#E3242B"));
                    alarmShowListButton.setEnabled(true);
                }else{ 
                    alarmShowListButton.setForeground(Color.decode("#9F6469"));
                    alarmShowListButton.setEnabled(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                alarmScheduleButton.setFont(new Font("Cambria",1,23));
            }

            @Override
            public void mouseExited(MouseEvent me) {
                alarmScheduleButton.setFont(new Font("Cambria",1,22));
            }
        };
        alarmScheduleButton.addMouseListener(alarmschedulebutton);
    }
    private void mouseListenerAlarmShowListButton(){
        MouseListener showlistbutton = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if(alarm_list_thread.isEmpty()) list_of_alarms.setVisible(false);
                else list_of_alarms.setVisible(true);
            }

        };
        
        alarmShowListButton.addMouseListener(showlistbutton);
    }
    private void mouseListenerAlarmGetNameJTextField(){
        MouseListener MA = new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                alarmGetNameJTextField.setFocusable(true);
                alarmGetNameJTextField.grabFocus();
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                alarmGetNameJTextField.grabFocus();
                alarmGetNameJTextField.setFocusable(true);
                if(alarmGetNameJTextField.getText().equals("Nombre de la Alarma")){
                    alarmGetNameJTextField.setText("");
                alarmGetNameJTextField.setCaretPosition(0);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                alarmGetNameJTextField.setFont(new Font("Roboto",100,22));
                alarmGetNameJTextField.setFocusable(true);
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!alarmGetNameJTextField.isFocusOwner()){
                    alarmGetNameJTextField.setFont(new Font("Roboto",100,21));
                    alarmGetNameJTextField.setFocusable(false);
                }
            }
        };
        alarmGetNameJTextField.addMouseListener(MA);
    }
    
    private void mouseListenerChronometerResetButton(){
        MouseListener resetbutton=new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if(chronometer_default_resetbutton && !"00:00:00:00".equals(chronometer.getText())){
                    Cronometro.Reset();
                    chronometer_start_stop_button.setText("Iniciar");
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(chronometer_default_resetbutton && !"00:00:00:00".equals(chronometer.getText()) ){
                    chronometer_reset_button.setFont(new Font("Times",1,22));
                    chronometer_reset_button.setForeground(new Color(213,51,255));
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(chronometer_default_resetbutton){
                    chronometer_reset_button.setFont(new Font("Times",1,19));
                    chronometer_reset_button.setForeground(Color.decode("#5858C9")); 
                }
            }
        };
        chronometer_reset_button.addMouseListener(resetbutton);
    }
    private void mouseListenerChronometerStartStopButton(){
        MouseListener startStopButton=new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if(chronometer_default_startstopbutton){
                    chronometer_default_startstopbutton=false;
                    chronometer_default_resetbutton=false;
                    chronometer_start_stop_button.setText("Detener");
                    if(chronometer_thread_is_not_start){
                        chronometer_thread.start();
                        chronometer_thread_is_not_start=false;
                    }else{   
                        Cronometro.Resume();
                    }
                }else{
                    chronometer_default_startstopbutton=true;
                    chronometer_default_resetbutton=true;
                    chronometer_start_stop_button.setText("Reanudar");
                    Cronometro.Stop();
                    
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                if(chronometer_default_startstopbutton){
                    chronometer_start_stop_button.setFont(new Font("Times",1,22));
                    chronometer_start_stop_button.setForeground(new Color(213,51,255));
                }
            }

            @Override
            public void mouseExited(MouseEvent me) {
                if(chronometer_default_startstopbutton){
                    chronometer_start_stop_button.setFont(new Font("Times",1,19));
                    chronometer_start_stop_button.setForeground(Color.decode("#5858C9"));                
                }
            }
        };
        chronometer_start_stop_button.addMouseListener(startStopButton);
    }
    
    private void mouseListenerClockButton12h(){
        MouseListener Button12h=new MouseAdapter() {
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
        MouseListener Button24h=new MouseAdapter() {
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
        try{
//        alarm.setOpaque(true);
        alarmButton.setOpaque(true);
        alarmPickDay.setOpaque(true);
        alarmPickHour.setOpaque(true);
        alarmPickMinute.setOpaque(true);
        alarmPickMeridian.setOpaque(true);
        alarmScheduleButton.setOpaque(true);
        alarmShowListButton.setOpaque(true);
        alarmTwoPointsMeridian.setOpaque(true);
        chronometer.setOpaque(true);
        chronometerButton.setOpaque(true);
        chronometer_dhms.setOpaque(true);
        chronometer_reset_button.setOpaque(true);
        chronometer_start_stop_button.setOpaque(true);
        clock1.setOpaque(true);
        clock2.setOpaque(true);
        clockButton.setOpaque(true);
        clock_button_12h.setOpaque(true);
        clock_button_24h.setOpaque(true);
        exitButton.setOpaque(true);
        minimizeButton.setOpaque(true);
        backgroundButton.setOpaque(true);
        }catch(Exception e){
            System.out.println(e);
        }
        
    }
    
}