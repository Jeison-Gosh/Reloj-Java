package Reloj;

import java.awt.Color;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Alarma implements Runnable{
    
    private static long uniqueCode = 0;
    private static int count;
    private final int hour;
    private final int min;
    private final String day;
    private final String alarmName;
    private boolean isAlarmOn;
    private Calendar calendar;
    private SClip alarmClockSound;
    private String code;

    
    public Alarma(String AlarmName, String day, int hour, int min) {
        Alarma.count = Alarma.count;
        Alarma.uniqueCode++;
        this.alarmName = AlarmName;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.isAlarmOn = true;
        this.alarmClockSound=new SClip("src/sounds/alarmClock.wav");
        this.code = uniqueCode+alarmName+day+hour+min;

    }
    
    @Override
    public void run() {
        while(isAlarmOn){
            if(check_day() && check_time()){
                run_alarm();
            }else{
                try{
                    Thread.sleep(getDiff());
                }catch (InterruptedException ex){
                   Logger.getLogger(Alarma.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public String toString(){
        String string=this.alarmName+" "+this.day+" "+this.hour+" "+this.min;
        return string;
    }
    public void cancel_alarm(){
        this.isAlarmOn = false;
    }
    public boolean isAlarmOn(){
        return this.isAlarmOn;
    }
    public static void setCount(int count){
        if(count<=7 && count>=1){
            Alarma.count = count;
        }else{
            Alarma.count = 1;
        }
    }
    public static int getCount(){
        return Alarma.count;
    }
    public int getHour() {
        return hour;
    }
    public int getMin() {
        return min;
    }
    public String getDay(){
        return day; 
    }
    public String getTime(){
        if(min<10){
            return hour+":"+"0"+min;
        }else{
            return hour+":"+min;
        }
    }
    public String getCode(){
        return code;
    }
    public String getAlarmName(){
        return this.alarmName;
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
        if(check_day() && check_time() && count<=7){
            alarmClockSound.play();
            isAlarmOn=false;  
            count--;
            ListIterator<Thread> itt=Main.aplicacion.alarm_list_thread.listIterator();
            ListIterator<Alarma> ita=Componentes.alarm_list.listIterator();
            while(itt.hasNext() && ita.hasNext()){
                Alarma a=ita.next();
                Thread t=itt.next();
                if(!a.isAlarmOn && !a.getCode().equals(this.getCode())){
                    System.out.println("eliminado el :"+a.getCode());
                    ita.remove();
                    itt.remove();
                }else{System.out.println("no se elimino el Thread: "+a.getCode());}
            }
            //menor o igual a uno, porque no se puede eliminar un hilo en ejecucion.
            if(Componentes.alarm_list.size()<=1){ 
                Main.aplicacion.alarmShowListButton.setForeground(Color.decode("#9F6469"));
                Main.aplicacion.alarmShowListButton.setEnabled(false);
            }
            Componentes.list_of_alarms.rebuildGraphicalList();
        }
        
    }
    
  
}
