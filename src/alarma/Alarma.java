package alarma;

import java.util.TimerTask;


public class Alarma implements Runnable{
    private boolean AlarmStatus;
    private TimerTask task;
    
    public Alarma(long hora){
        
    }
    @Override
    public void run() {
        task = new TimerTask() {
            @Override
            public void run() {
                
            }
        };
    }
}
