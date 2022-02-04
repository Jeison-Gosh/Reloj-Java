package Reloj;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ListIterator;


public class Alarma implements Runnable{

    private static long counter;
    private boolean AlarmIsOn;
    private final int hour;
    private final int min;
    private final String day;
    private final String AlarmName;
    private Calendar calendar;
    
    public Alarma(String AlarmName, String day, int hour, int min) {
        Alarma.counter = 0;
        this.AlarmName = AlarmName;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.AlarmIsOn = true;
    }
    
    @Override
    public void run() {
        while(AlarmIsOn){
            if(check_day() && check_time()){
                run_alarm();
            }else{
                try{
                    Thread.sleep(getDiff());
                }catch (Exception e){}
            }
        }
    }

    @Override
    public String toString(){
        String string=this.AlarmName+" "+this.day+" "+this.hour+" "+this.min;
        return string;
    }
    public void cancel_alarm(){
        this.AlarmIsOn = false;
    }
    public String getCode(){
        return AlarmName+day+hour+min;
    }
    public String getAlarmName(){
        return this.AlarmName;
    }
    public String getDay(){
        return day; 
    }
    public int getHour() {
        return hour;
    }
    public int getMin() {
        return min;
    }
    public String getTime(){
        if(min<10){
            return hour+":"+"0"+min;
        }else{
            return hour+":"+min;
        }
    }
    
    private String getToday(){
        int number_of_day;
        String day="";
        calendar = new GregorianCalendar();
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
    private long getDiff(){
        long diff=0,hour,min;
        calendar = new GregorianCalendar();
        if(check_day()){
            //horas y minutos faltantes para que suene la alarma
            hour=(this.hour-calendar.get(Calendar.HOUR_OF_DAY))*3600000;
            min=(this.min-calendar.get(Calendar.MINUTE))*60000;
            diff=min+hour;
            if(diff<0){diff=(((24-calendar.get(Calendar.HOUR_OF_DAY))*3600000)-(calendar.get(Calendar.MINUTE)*60000));}
            else if(diff<=60000){ diff=5000;}
            else{diff-=60000;}
        }else{
            //horas y minutos faltantes para llegar a las 24H
            hour=(24-calendar.get(Calendar.HOUR_OF_DAY))*3600000;
            min=calendar.get(Calendar.MINUTE)*60000;
            diff=hour-min;
        }
        System.out.println("la diferencia de tiempo es: "+diff);
        return diff;
    }
    private boolean check_day(){
        boolean is_the_day=false;
        if(getToday().equals(day)){
            is_the_day=true;
        }
        return is_the_day;
    }
    private boolean check_time(){
        boolean is_the_time=false;
        calendar = new GregorianCalendar();
        if(hour==calendar.get(Calendar.HOUR_OF_DAY)){
            if(min==calendar.get(Calendar.MINUTE)){
                is_the_time=true;
            }
        }
        return is_the_time;
    }
    
    private synchronized void run_alarm (){
        if(check_day() && check_time()){
            System.out.println("Alarma esta sonando");
            System.out.println("Alarma esta sonando");
            System.out.println("Alarma esta sonando");
            System.out.println("Alarma esta sonando");
            AlarmIsOn=false;            
//            boolean DeadThreadExist=true;
//            while(DeadThreadExist){
//                Iterator<Thread> itt=Main.aplicacion.alarm_list_thread.iterator();
//                Iterator<Alarma> ita=Main.aplicacion.alarm_list.iterator();
//                boolean FoundDeadThread=false;
//                while(itt.hasNext()){
//                    itt.next();
//                    ita.next();
//                    if(!ita.AlarmIsOn && !ita.getCode().equals(this.getCode())){
//                        FoundDeadThread=true;
//                        break;
//                    }
//                }
//                if(FoundDeadThread){
//                    itt.remove();
//                    ita.remove();
//                }
//                int count=0;
//                for(Alarma a: Main.aplicacion.alarm_list){
//                    if(!a.AlarmIsOn) count++;
////                    System.out.println(a.getCode());
//                }
////                System.out.println("tamaño "+Main.aplicacion.alarm_list.size());
//                if(count==0 || Main.aplicacion.alarm_list.size()==1) DeadThreadExist=false;
//            }
            ListIterator<Thread> itt=Main.aplicacion.alarm_list_thread.listIterator();
            ListIterator<Alarma> ita=Main.aplicacion.alarm_list.listIterator();
            while(itt.hasNext()){
                
                if(!ita.next().getCode().equals(this.getCode()) && !ita.next().AlarmIsOn){
                    System.out.println(ita.next().getAlarmName());
                    System.out.println(itt.next().getName());
                }
            }
            
            System.out.println("asi quedo la lista T: ");
            for(Thread t: Main.aplicacion.alarm_list_thread){
                System.out.print(" "+t+" ");
            }
        }
        
    }
    
  
}
