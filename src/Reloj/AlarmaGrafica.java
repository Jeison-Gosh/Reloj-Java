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
        setSize(100,100);
        setLayout(null);
        setBorder(new LineBorder(Color.decode("#4A5053")));
        setBackground(Color.decode("#091C17"));
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
        JLabel title=new JLabel(this.title.toUpperCase());
        JLabel time=new JLabel(this.time);
        JLabel day=new JLabel("• "+this.day+" •");
        
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Constantia",1,18));
        title.setForeground(Color.cyan);
        title.setOpaque(true);
        title.setBackground(Color.decode("#4A5053"));
        title.setBounds(0,0,99,20);
        
        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setFont(new Font("Franklin Gothic",1,18));
        time.setForeground(Color.decode("#E9BD15"));
        time.setBounds(25,35,60,25);
        
        day.setHorizontalAlignment(SwingConstants.CENTER);
        day.setFont(new Font("Comic Sans MS",0,14));
        day.setForeground(Color.decode("#CCCDCA"));
        day.setBounds(5,70,90,20);
        
        add(title);
        add(time);
        add(day);
        
    }
    
}
