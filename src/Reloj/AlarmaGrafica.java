package Reloj;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class AlarmaGrafica extends JPanel {
    
    private String title;
    private String time;
    private String day;

    public AlarmaGrafica(String title, String time, String day) {
        this.title = title; 
        this.time = time;
        this.day = day;
        setSize(375,50);
        setLayout(null);
        setBorder(new LineBorder(Color.decode("#4A5053")));
        setBackground(Color.decode("#010118"));
        putLabels();
    }

    public String getTitle() {
        return title;
    }
    public String getTime() {
        return time;
    }
    public String getDay() {
        return day;
    }
    public void putLabels(){
        JLabel title=new JLabel(this.title);
        JLabel time=new JLabel(this.time);
        JLabel day=new JLabel("• "+this.day+" •");
        JLabel text1=new JLabel("Hora: ");
        JLabel text2=new JLabel("Dia: ");
        
//        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Cambria Math",1,18));
        title.setForeground(Color.cyan);
        title.setOpaque(true);
        title.setBackground(Color.decode("#4A5053"));
        title.setBounds(1,1,374,20);
        
        text1.setHorizontalAlignment(SwingConstants.CENTER);
        text1.setFont(new Font("Comic Sans MS",0,14));
        text1.setForeground(Color.decode("#B8D186"));
        text1.setBounds(15,25,90,20);
        
        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setFont(new Font("Franklin Gothic",1,18));
        time.setForeground(Color.decode("#E9BD15"));
        time.setBounds(75,25,60,25);
        
        text2.setHorizontalAlignment(SwingConstants.CENTER);
        text2.setFont(new Font("Palatino Linotype",0,14));
        text2.setForeground(Color.decode("#B8D186"));
        text2.setBounds(130,30,90,20);
        
        day.setHorizontalAlignment(SwingConstants.CENTER);
        day.setFont(new Font("Comic Sans MS",0,14));
        day.setForeground(Color.decode("#CCCDCA"));
        day.setBounds(185,25,90,20);
        
        add(title);
        add(time);
        add(day);
        add(text1);
        add(text2);
        
    }
    
}
