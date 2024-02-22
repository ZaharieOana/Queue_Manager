package org.example.Model;

public class Task implements Comparable<Task>{
    private static int total = 0;
    private int id;
    private int arrivalTime;
    private int serviceTime;

    public static void setTotal(){
        total=0;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }

    public Task(int arrT, int servT){
        arrivalTime = arrT;
        serviceTime = servT;
        total++;
        id = total;
    }

    @Override
    public int compareTo(Task o) {
        if(this.arrivalTime == o.arrivalTime)
            return this.id - o.id;
        return this.arrivalTime - o.arrivalTime;
    }

    public String toString(){
        return String.format("(%d,%d,%d)",id, arrivalTime, serviceTime);
    }
    public void decServTime(){
        serviceTime--;
    }
}
