package alarma;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Lista extends JFrame {
    private JPanel InputPanel, ButtonsPanel;
    private JLabel ChanguePagesButtons;
    public Lista(){
        InitComponents();
        setSize(500, 500);
        setTitle("Lista de alarmas");
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void InitComponents(){
        InputPanel();
        ButtonsPanel();
        addPage();
    }
    private void InputPanel(){
        InputPanel = new JPanel();
        InputPanel.setSize(500,465);
        InputPanel.setLayout(null);
        InputPanel.setBackground(Color.decode("#0B0705"));
        InputPanel.setBorder(new LineBorder(new Color(72,71,73)));
        this.add(InputPanel);
    }
    private void ButtonsPanel(){
        ButtonsPanel = new JPanel();
        ButtonsPanel.setSize(500,35);
        ButtonsPanel.setBorder(new LineBorder(new Color(72,71,73)));
        ButtonsPanel.setBackground(Color.decode("#242424"));
        this.add(ButtonsPanel);
    }
    private void Pages_of_Panel(){}
    private void changue_page_button(){
        ChanguePagesButtons = new JLabel();
        ChanguePagesButtons.setVisible(true);
        ChanguePagesButtons.setSize(500,35);
        ChanguePagesButtons.setLocation(0,430);
        ChanguePagesButtons.setOpaque(true);
        ChanguePagesButtons.setBorder(new LineBorder(new Color(72,71,73)));
        ChanguePagesButtons.setForeground(Color.decode("#F4F4F4"));
        ChanguePagesButtons.setBackground(Color.decode("#242424"));
        ChanguePagesButtons.setFont(new Font("Verdana",1,24));
        ChanguePagesButtons.setHorizontalAlignment(SwingConstants.CENTER);
        ChanguePagesButtons.setVerticalAlignment(SwingConstants.CENTER);
        ChanguePagesButtons.setText("•");
        InputPanel.add(ChanguePagesButtons);
    }
    private void addPage(){
        JLabel page = new JLabel("•");
        page.setSize(10,10);
        page.setLocation(245,17);
        page.setVisible(true);
        page.setFont(new Font("Verdana",1,24));
        page.setForeground(Color.decode("#F4F4F4"));
        page.setHorizontalAlignment(SwingConstants.CENTER);
        page.setVerticalAlignment(SwingConstants.CENTER);
        ButtonsPanel.add(page);
//        return page;
    }
    
    
}

