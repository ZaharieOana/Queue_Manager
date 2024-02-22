package org.example.Logic;

import org.example.Model.*;
import java.util.ArrayList;

public class Scheduler {
    private ArrayList<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServers;
    private Strategy strategy;

    public Scheduler(int maxNoServ, int maxTasksPerServ){
        maxNoServers = maxNoServ;
        maxTasksPerServers = maxTasksPerServ;
        servers = new ArrayList<>();
        for(int i=0;i<maxNoServers; i++){
            Server s = new Server(maxTasksPerServers);
            servers.add(s);
            Thread t = new Thread(s);
            t.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }

    public void dispatchTask(Task t){
        strategy.addTask(servers, t);
    }
    public ArrayList<Server> getServers(){
        return servers;
    }
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(int i=1;i<=servers.size();i++){
            s.append(String.format("\nQueue %d (%d): ", i, servers.get(i - 1).getWaitingPeriod())).append(servers.get(i - 1));
        }
        return s.toString();
    }
    public int nrOfClients(){
        int nr=0;
        for (Server server : servers)
            nr += server.getTasks().size();
        return nr;
    }
}
