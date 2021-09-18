package alarma;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimerTask;


public class Alarma implements Runnable{
    private boolean AlarmStatus;
    private int hour,min;
    private String day;
    private TimerTask task;
    private Calendar calendar;
    public static Thread threadname;
    
    public Alarma(String day, int hour, int min) {
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.calendar = new GregorianCalendar();
        
        
    }
    @Override
    public void run() {
        run_alarm();
    }
    
    private String getDay(){
        int number_of_day;
        String day="";
        number_of_day=calendar.get(Calendar.DAY_OF_WEEK);
        if(number_of_day==1) day="Domingo";
        if(number_of_day==2) day="Lunes";
        if(number_of_day==3) day="Martes";
        if(number_of_day==4) day="Miercoles";
        if(number_of_day==5) day="Jueves";
        if(number_of_day==6) day="Viernes";
        if(number_of_day==7) day="Sabado";
        return day;
    }
    
    private boolean check_day(){
        boolean is_the_day=false;
        if(getDay().equals(day)){
            is_the_day=true;
        }
        return is_the_day;
    }
    
    private boolean check_time(){
        boolean is_the_time=false;
        if(hour==calendar.get(Calendar.HOUR_OF_DAY)){
            if(min==calendar.get(Calendar.MINUTE)){
                is_the_time=true;
            }
        }
        return is_the_time;
    }
    private long getDiff(){
        long diff=0,hour,min;
        if(check_day()){
            hour=(this.hour-(calendar.get(Calendar.HOUR_OF_DAY))*3600000);
            min=this.min-(calendar.get(Calendar.MINUTE)*60000);
            diff=min+hour;
        }else{
            hour=(24-(calendar.get(Calendar.HOUR_OF_DAY))*3600000);
            min=(60-calendar.get(Calendar.MINUTE)*60000);
            diff=min+hour;
        }
        return diff;
    }
    
    private void run_alarm (){
        if(check_day() && check_time()){
            System.out.println("Alarma esta sonando");
            System.out.println("Alarma esta sonando");
            System.out.println("Alarma esta sonando");
            System.out.println("Alarma esta sonando");
        }
    }
  
}
