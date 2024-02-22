package org.example.Logic;

import org.example.GUI.SimulationFrame;
import org.example.Model.*;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static java.lang.Thread.sleep;

public class SimulationManager implements Runnable{
    //data from UI
    private int timeLimit = 30; //max pocessing time - red from UI
    private int maxProcessingTime = 9;
    private int minProcessingTime = 2;
    private int maxArrivalTime = 15;
    private int minArrivalTime = 2;
    private int nrOfServers = 10;
    private int nrOfClients = 20;
    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    //entity responsible with queue management and client distribution
    private Scheduler scheduler;
    //frame for display sim
    private SimulationFrame frame;
    //pool of tasks (clients shopping in the store)
    private ArrayList<Task> generatedTasks;
    private int totalServiceTime;

    public SimulationManager(){
        frame = new SimulationFrame();
        frame.getStartB().addActionListener(l -> {
            ArrayList<Integer> list;
            list=frame.getInputData();
            nrOfClients=list.get(0);
            nrOfServers=list.get(1);
            minArrivalTime=list.get(2);
            maxArrivalTime=list.get(3);
            minProcessingTime=list.get(4);
            maxProcessingTime=list.get(5);
            timeLimit=list.get(6);
            frame.setQueues(nrOfServers);
            Server.start();
            scheduler = new Scheduler(nrOfServers, nrOfClients);
            scheduler.changeStrategy(selectionPolicy);
            generatedTasks = new ArrayList<>();
            generateNRandomTasks();
            totalServiceTime=0;
            for(Task t : generatedTasks)
                totalServiceTime+=t.getServiceTime();
            Thread t = new Thread(this);
            t.start();
        });
    }

    private void generateNRandomTasks(){
        Task.setTotal();
        for(int i = 0; i < nrOfClients; i++){
            Random rand = new Random();
            Task t = new Task(rand.nextInt(maxArrivalTime - minArrivalTime+1) + minArrivalTime, rand.nextInt(maxProcessingTime - minProcessingTime+1) + minProcessingTime);
            generatedTasks.add(i, t);
        }
        Collections.sort(generatedTasks);
    }

    @Override
    public void run() {
        int currentTime = 0;
        int peakHour = 0;
        int maxClients = 0;
        try {
            FileOutputStream outputStream = new FileOutputStream("result.txt");
            while(!validStopPoint(currentTime)){
                for(int i=0;i< generatedTasks.size();i++){
                    Task t = generatedTasks.get(i);
                    if(t.getArrivalTime() == currentTime){
                        scheduler.dispatchTask(t);
                        generatedTasks.remove(t);
                        i--;
                    }
                }
                frame.updateFrame(currentTime, scheduler, generatedTasks);
                byte[] strToBytes = print(currentTime).getBytes();
                outputStream.write(strToBytes);
                if(scheduler.nrOfClients()>maxClients) {
                    maxClients = scheduler.nrOfClients();
                    peakHour=currentTime;
                }
                currentTime++;
                sleep(1000);
            }
            byte[] strToBytes = print(currentTime).getBytes();
            outputStream.write(strToBytes);
            strToBytes = finalPrint(peakHour).getBytes();
            outputStream.write(strToBytes);
            frame.updateFrame(currentTime, scheduler, generatedTasks);
            Server.stop();
        }
        catch (Exception e) {
            System.out.println("ExecptionSim");
            e.printStackTrace();
        }
    }

    public String print(int currentTime){
        StringBuilder s = new StringBuilder();
        s.append(String.format("-------------------\nTime%d\nWaiting clients:\n", currentTime));
        for(int i=0;i<generatedTasks.size();i++) {
            s.append(generatedTasks.get(i)).append("; ");
            if(i>0 && i%15==0)
                s.append("\n");
        }
        s.append(scheduler.toString());
        s.append("\n");
        return s.toString();
    }
    public String finalPrint(int peakHour){
        float avgWaitingTime = 0;
        for(int i=0;i<scheduler.getServers().size();i++){
            avgWaitingTime += (float) scheduler.getServers().get(i).getTotalWaitingPeriod();
        }
        avgWaitingTime/=nrOfClients;
        float avgServiceTime = (float) totalServiceTime;
        avgServiceTime/=nrOfClients;
        String s = "\n\n******************\n" +
                "Average waiting time is " + avgWaitingTime + "\n" +
                "Average service time is " + avgServiceTime + "\n" +
                "The peak hour is " + peakHour + "\n";
        frame.setInfoFrame(avgWaitingTime, avgServiceTime, peakHour);
        return s;
    }

    private boolean validStopPoint(int currentTime){
        if(currentTime >= timeLimit)
            return true;
        if(!generatedTasks.isEmpty())
            return false;
        for(Server s: scheduler.getServers()){
            if(s.getWaitingPeriod()>0)
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
    }
}
