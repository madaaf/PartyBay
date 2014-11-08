package com.example.mada.partybay.Class;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mada on 08/11/2014.
 */
public class MyDate {

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public MyDate(int year, int month, int day, int hour, int minute){

        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getDifferenceDateToday(){
        String resultat = null;

        java.util.Date heure = new GregorianCalendar(year,month,day,hour,minute).getTime();
        System.out.println("heure du post = "+ year +" "+month+" "+ day+" "+hour+" "+minute+" ");

        // heure de mtn yyyy-MM-dd HH:mm:ss

        java.util.Date date2 = new java.util.Date(); // your date
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);

        int year2 = cal.get(Calendar.YEAR);
        int month2 = cal.get(Calendar.MONTH)+1;
        int day2 = cal.get(Calendar.DAY_OF_MONTH);
        int hour2 = cal.get(Calendar.HOUR_OF_DAY);
        int min2 = cal.get(Calendar.MINUTE);

        java.util.Date today= new GregorianCalendar(year2,month2,day2,hour2,min2).getTime();

        long diff = today.getTime( ) - heure.getTime( );

        diff = diff/(1000*60);
        long minutes = diff % 60;
        long hours = diff / 60;
        if (minutes<1){
            resultat="à l'instant";
        }  else if(hours==0){
             resultat ="il y a "+ minutes+"m";
         }else if(hours<24){
             resultat ="il y a "+ hours+"h";
         }else if(hours>=24){
             resultat ="il y a "+ hours/24+"j";
         }

        return resultat;
    }



    public void setMonth(int month) {
        this.month = month;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }


}
