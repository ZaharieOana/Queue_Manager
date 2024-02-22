package org.example.GUI;

import org.example.Logic.Scheduler;
import org.example.Model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationFrame extends JFrame {
    private JPanel generalPanel;
    private JPanel inputData;
    private JPanel clientsPanel;
    private JPanel queuesPanel;
    private JLabel[] queueLabels;
    private JLabel[] labelsInput;
    private JTextField[] textsInput;
    private JButton startB = new JButton("START");
    private JLabel labelClients = new JLabel();
    private JLabel timeLabel = new JLabel("Time");

    public SimulationFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setTitle("Queue Manager");

        setInputDataPanel();
        setClientsPanel();
        queuesPanel = new JPanel();
        queuesPanel.setBackground(Color.lightGray);
        setGeneralPanel();

        add(generalPanel);
        setVisible(true);
    }
    private void setGeneralPanel(){
        SpringLayout springLayout = new SpringLayout();
        generalPanel = new JPanel(springLayout);
        generalPanel.add(inputData);
        generalPanel.add(clientsPanel);
        generalPanel.add(queuesPanel);

        springLayout.putConstraint(SpringLayout.NORTH, inputData, 0, SpringLayout.NORTH, generalPanel);
        springLayout.putConstraint(SpringLayout.WEST, inputData, 0, SpringLayout.WEST, generalPanel);
        springLayout.putConstraint(SpringLayout.EAST, inputData, 0, SpringLayout.EAST, generalPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, inputData, 90, SpringLayout.NORTH, generalPanel);

        springLayout.putConstraint(SpringLayout.SOUTH, clientsPanel, 0, SpringLayout.SOUTH, generalPanel);
        springLayout.putConstraint(SpringLayout.NORTH, clientsPanel, -130, SpringLayout.SOUTH, generalPanel);
        springLayout.putConstraint(SpringLayout.WEST, clientsPanel, 0, SpringLayout.WEST, generalPanel);
        springLayout.putConstraint(SpringLayout.EAST, clientsPanel, 0, SpringLayout.EAST, generalPanel);

        springLayout.putConstraint(SpringLayout.SOUTH, queuesPanel, 0, SpringLayout.NORTH, clientsPanel);
        springLayout.putConstraint(SpringLayout.NORTH, queuesPanel, 0, SpringLayout.SOUTH, inputData);
        springLayout.putConstraint(SpringLayout.WEST, queuesPanel, 0, SpringLayout.WEST, generalPanel);
        springLayout.putConstraint(SpringLayout.EAST, queuesPanel, 0, SpringLayout.EAST, generalPanel);
    }
    private void setInputDataPanel(){
        labelsInput = new JLabel[7];
        textsInput = new JTextField[7];
        for(int i=0;i<7;i++){
            labelsInput[i] = new JLabel();
            labelsInput[i].setFont(new Font("Arial", Font.PLAIN, 15));
            textsInput[i] = new JTextField();
            textsInput[i].setFont(new Font("Arial", Font.PLAIN, 15));
            textsInput[i].setBorder(BorderFactory.createLineBorder(new Color(155, 236, 163)));
        }
        labelsInput[0].setText("Nr of Clients");
        labelsInput[4].setText("Nr of Servers");
        labelsInput[1].setText("Minim Arrival Time");
        labelsInput[5].setText("Maxim Arrival Time");
        labelsInput[2].setText("Minim Waiting Time");
        labelsInput[6].setText("Maxim Waiting Time");
        labelsInput[3].setText("Time Limit");
        startB.setBackground(new Color(71, 229, 51));
        startB.setBorder(BorderFactory.createLineBorder(new Color(155, 236, 163)));
        inputData = new JPanel(new GridLayout(2, 8, 5, 10));
        inputData.setBackground(new Color(155, 236, 163));
        inputData.setBorder(BorderFactory.createLineBorder(new Color(155, 236, 163), 10));
        for(int i=0;i<7;i++){
            inputData.add(labelsInput[i]);
            inputData.add(textsInput[i]);
        }
        inputData.add(new JLabel(""));
        inputData.add(startB);
    }
    private void setClientsPanel(){
        SpringLayout layoutClients = new SpringLayout();
        clientsPanel = new JPanel(layoutClients);
        clientsPanel.setBackground(new Color(90, 117, 224));
        JLabel waiting = new JLabel("Waiting Clients:");
        waiting.setFont(new Font("Arial", Font.PLAIN, 15));
        labelClients.setFont(new Font("Arial", Font.PLAIN, 15));
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        clientsPanel.add(waiting);
        clientsPanel.add(labelClients);
        clientsPanel.add(timeLabel);
        layoutClients.putConstraint(SpringLayout.NORTH, waiting, 5, SpringLayout.NORTH, clientsPanel);
        layoutClients.putConstraint(SpringLayout.HORIZONTAL_CENTER, waiting, 0, SpringLayout.HORIZONTAL_CENTER, clientsPanel);
        layoutClients.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelClients, 0, SpringLayout.HORIZONTAL_CENTER, clientsPanel);
        layoutClients.putConstraint(SpringLayout.NORTH, labelClients, 5, SpringLayout.NORTH, waiting);
        layoutClients.putConstraint(SpringLayout.SOUTH, labelClients, 5, SpringLayout.SOUTH, clientsPanel);
        layoutClients.putConstraint(SpringLayout.NORTH, timeLabel, 5, SpringLayout.NORTH, clientsPanel);
        layoutClients.putConstraint(SpringLayout.EAST, timeLabel, -15, SpringLayout.EAST, clientsPanel);
    }
    public void setQueues(int N){
        queuesPanel.removeAll();
        queuesPanel.setLayout(new GridLayout(N,1));
        queueLabels = new JLabel[N];
        for(int i=0;i<N;i++){
            queueLabels[i] = new JLabel("Queue "+(i+1));
            queuesPanel.add(queueLabels[i]);
        }
    }

    public void updateFrame(int crtTime, Scheduler scheduler, ArrayList<Task> tasks){
        timeLabel.setText("Time "+crtTime);
        StringBuilder s = new StringBuilder();
        s.append("<html>");
        for(int i=0;i<tasks.size();i++) {
            s.append(tasks.get(i)).append("; ");
            if(i>0 && i%18==0)
                s.append("<br>");
        }
        s.append("</html>");
        labelClients.setText(s.toString());
        for(int i=0;i<scheduler.getServers().size();i++){
            queueLabels[i].setText("Queue "+(i+1)+" : "+scheduler.getServers().get(i));
        }
    }
    public void setInfoFrame(float avgW, float avgF, int peakH){
        InformationFrame infoFrame = new InformationFrame();
        infoFrame.setInfo(avgW, avgF, peakH);
    }
    public JButton getStartB() {
        return startB;
    }
    public ArrayList<Integer> getInputData(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(Integer.valueOf(textsInput[0].getText()));
        list.add(Integer.valueOf(textsInput[4].getText()));
        list.add(Integer.valueOf(textsInput[1].getText()));
        list.add(Integer.valueOf(textsInput[5].getText()));
        list.add(Integer.valueOf(textsInput[2].getText()));
        list.add(Integer.valueOf(textsInput[6].getText()));
        list.add(Integer.valueOf(textsInput[3].getText()));
        return list;
    }
}
