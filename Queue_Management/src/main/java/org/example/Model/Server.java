package org.example.Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private AtomicInteger totalWaitingTime;
    private static int ok = 1;

    public Server(int maxTasksPerServers){
        tasks = new ArrayBlockingQueue<>(maxTasksPerServers);
        waitingPeriod = new AtomicInteger(0);
        totalWaitingTime = new AtomicInteger(0);
    }

    public void addTask(Task newTask){
        tasks.add(newTask);
        totalWaitingTime.getAndAdd(waitingPeriod.get());
        waitingPeriod.getAndAdd(newTask.getServiceTime());
    }

    @Override
    public void run() {
        while(ok==1){
            try {
                if(!tasks.isEmpty()) {
                    Task t = tasks.element();
                    while(t.getServiceTime()>0){
                        Thread.sleep(1000);
                        waitingPeriod.getAndDecrement();
                        t.decServTime();
                    }
                    tasks.remove();
                }
            } catch (Exception e) {
                System.out.println("ExecptionServer");
                e.printStackTrace();
            }
        }
    }
    public String toString(){
        StringBuilder s = new StringBuilder();
        if(tasks.isEmpty())
            s.append("closed");
        else {
            tasks.forEach(task -> s.append(task).append("; "));
        }
        return s.toString();
    }
    public static void stop(){
        ok=0;
    }
    public static void start(){
        ok=1;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }
    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }
    public int getTotalWaitingPeriod() {
        return totalWaitingTime.get();
    }
}
