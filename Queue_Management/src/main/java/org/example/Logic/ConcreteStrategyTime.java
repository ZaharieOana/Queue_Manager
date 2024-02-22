package org.example.Logic;

import org.example.Model.*;

import java.util.List;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task t) {
        int minim = servers.get(0).getWaitingPeriod();
        int k=0;
        for(int i = 1; i < servers.size(); i++){
            int time = servers.get(i).getWaitingPeriod();
            if(minim>time){
                minim = time;
                k=i;
            }
        }
        servers.get(k).addTask(t);
    }
}
