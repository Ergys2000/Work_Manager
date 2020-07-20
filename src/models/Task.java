package models;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Task implements Serializable {
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private String name;
    private int minutesLeft;


    public Task(LocalTime s_h, LocalTime e_h, String name, LocalDate date) {
        this.start = s_h;
        this.end = e_h;
        this.name = name;
        this.date = date;
        this.minutesLeft = (e_h.getHour() - s_h.getHour()) * 60 + (end.getMinute() - start.getMinute());
    }

    public void setMinutesLeft(int minutesDone){
        this.minutesLeft -= minutesDone;
    }

    public int getMinutesLeft(){
        return this.minutesLeft;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
        this.minutesLeft = (end.getHour() - start.getHour()) * 60 + (end.getMinute() - start.getMinute());
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
        this.minutesLeft = (end.getHour() - start.getHour()) * 60 + (end.getMinute() - start.getMinute());
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public double getTotalLength(){
        int minutes = (end.getHour() - start.getHour()) * 60 + (end.getMinute() - start.getMinute());
        return (double)minutes/60;
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "\ndate=" + date +
                "\n, start=" + start +
                "\n, end=" + end +
                "\n, name='" + name + '\'' +
                '}';
    }

}