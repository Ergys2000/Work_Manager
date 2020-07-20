package models;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String name;
    public String surname;
    private String username;
    private String password;
    private ArrayList<Task> tasks;
    private ArrayList<Task> undoneTasks;
    private ColorScheme colorScheme = null;

    public User(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        tasks = new ArrayList<>();
        undoneTasks = new ArrayList<>();
    }

    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void printTasks(){
        for(Task t: tasks){
            System.out.println(t);
        }
    }

    public void add(Task t) throws IOException {
        for(Task task: tasks){
            if(t.getDate().equals(task.getDate())){
                if(t.getStart().isBefore( task.getEnd()) && t.getEnd().isAfter(task.getStart())){
                    throw new IOException("The task you want to add interferes with another task.");
                }
            }
        }
        tasks.add(t);
    }

    public void remove(Task t){
        tasks.remove(t);
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public void toDo(Task t) throws IOException{

        for(Task task: undoneTasks){
            if(t.getDate().equals(task.getDate())){
                if(t.getStart().isBefore( task.getEnd()) && t.getEnd().isAfter(task.getStart())){
                    throw new IOException("Another task is scheduled until " + task.getEnd());
                }
            }
        }
        undoneTasks.add(t);
    }

    public void did(Task t){
        undoneTasks.remove(t);
        tasks.add(t);
    }

    public ArrayList<Task> getUndoneTasks() {
        return undoneTasks;
    }
}
