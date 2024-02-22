package org.example.GUI;

import javax.swing.*;
import java.awt.*;

public class InformationFrame extends JFrame {
    private SpringLayout springLayout = new SpringLayout();
    private JPanel generalPanel = new JPanel();
    private JLabel avgWait = new JLabel();
    private JLabel avgService = new JLabel();
    private JLabel peakHour = new JLabel();

    InformationFrame(){
        setSize(400, 300);
        setLocationRelativeTo(null);
        setTitle("Additional Information");

        generalPanel.setLayout(springLayout);
        generalPanel.add(avgWait);
        generalPanel.add(avgService);
        generalPanel.add(peakHour);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, avgWait, 0, SpringLayout.HORIZONTAL_CENTER, generalPanel);
        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, avgService, 0, SpringLayout.HORIZONTAL_CENTER, generalPanel);
        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, peakHour, 0, SpringLayout.HORIZONTAL_CENTER, generalPanel);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, avgService, 0, SpringLayout.VERTICAL_CENTER, generalPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, avgWait, -15, SpringLayout.NORTH, avgService);
        springLayout.putConstraint(SpringLayout.NORTH, peakHour, 15, SpringLayout.SOUTH, avgService);

        add(generalPanel);
        setVisible(true);
    }
    public void setInfo(float avgW, float avgF, int peakH){
        avgWait.setText("Average waiting time: " + avgW);
        avgWait.setFont(new Font("Arial", Font.PLAIN, 20));
        avgService.setText("Average service time: " + avgF);
        avgService.setFont(new Font("Arial", Font.PLAIN, 20));
        peakHour.setText("Peak hour: " + peakH);
        peakHour.setFont(new Font("Arial", Font.PLAIN, 20));
    }
}
