package DataHandling;

import models.ColorScheme;
import models.Task;
import models.User;

import java.io.*;
import java.util.ArrayList;

public class RWUser {
    private User user;
    private String username;
    private File file;
    public RWUser(String name, String password) throws IOException{
        user = null;
        username = name;
        file = new File("src/Data/" + username + ".bin");
        if (file.exists()){
            readWork(password);
        }else throw new FileNotFoundException("The user does not exist");
    }

    private void readWork(String password) throws IOException{
        try {
            FileInputStream fis=new FileInputStream(file);
            ObjectInputStream ois=new ObjectInputStream(fis);
            user=(User)ois.readObject();
            ois.close();fis.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!!!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class does not match");
        }
        if(!user.getPassword().equals(password)){
            user = null;
            throw new IOException("The password is wrong!");
        }
    }

    private void writeWork(){
        try {
            FileOutputStream fos=new FileOutputStream(file);
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            oos.writeObject(user);
            oos.close();fos.close();
        } catch (FileNotFoundException e) {
            System.err.println("File cannot be written!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public User getUser(){
        return user;
    }

    public void addDone(Task t) throws IOException {
        user.add(t);
    }
    public void toDo(Task t) throws IOException {
        user.toDo(t);
    }
    public void did(Task t){
        user.did(t);
    }

    public ArrayList<Task> getTasks(){
        return user.getTasks();
    }

    public ArrayList<Task> getUndoneTasks(){
        return user.getUndoneTasks();
    }

    public void removeTask(Task t){
        user.remove(t);
    }

    public void setName(String name){
        user.setName(name);
    }
    public void setSurname(String s){
        user.setSurname(s);
    }
    public void setUsername(String s)throws IOException{
        File newFile = new File("src/Data/" + s + ".bin");
        if(newFile.exists()){
            throw new IOException("Username already exists!");
        }else{
            if(file.delete()){
                file = newFile;
                file.createNewFile();
                writeWork();
            }else throw new IOException("Could not access the file to change it!");
        }
    }
    public void setPassword(String s){
        user.setPassword(s);
    }

    public ColorScheme getColorScheme(){
        return user.getColorScheme();
    }
    public void setColorScheme(ColorScheme cs){
        user.setColorScheme(cs);
    }

    public String toString(){
        return "Username:" + user.getUsername();
    }

    public void update(){
        this.writeWork();
    }
}
