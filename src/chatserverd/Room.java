/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserverd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author dstok
 */
public class Room extends Thread implements Runnable {

    private List<User> users;

    private String roomName;
    private List<BufferedReader> in = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<PrintWriter> out = new ArrayList<>();
    private Tracker track;
    public boolean flag = false;
    private double instanceTime;

    public Room(User newUser) {
        this.users = Collections.synchronizedList(new ArrayList<>());
        users.add(newUser);
        for (User i : users) {
            out.add(i.getOut());
            in.add(i.getIn());
        }
        instanceTime = System.currentTimeMillis() / 1000.0;

    }

    public Room(String roomName, User newUser, Tracker track) {
        this.users = Collections.synchronizedList(new ArrayList<>());
        this.roomName = roomName;
        this.track = track;

        users.add(newUser);
        for (User i : users) {
            out.add(i.getOut());
            in.add(i.getIn());
        }
        instanceTime = System.currentTimeMillis() / 1000.0;

    }

    public List<User> getUsers() {
        return users;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public void run() {
        for (int i = 0; i < users.size(); i++) {
            System.out.println(users.get(i).getUsername());
        }
        flag = false;
        this.exe();

    }

    public void addUser(User user) {

        users.add(user);
        flag = true;

    }

    private void exe() {

        //add the initial users to input and output stream array lists
        int totalUsers = 0;
        int i = 0;
        int size = users.size();
        int j = 0;

        int v = i;
        Runnable first = () -> {
            dist(v);
        };
        track.getManager().execute(first);

        //exe only if there are more users since the start
        int oldSize = users.size();
        while (true) {

            if (flag) {
          
                i++;
                out.add(users.get(i).getOut());
                in.add(users.get(i).getIn());

                int x = i;
                flag = false;
                Runnable runnable = () -> {
     

                    dist(x);
                };

                track.getManager().execute(runnable);
            }
            oldSize = users.size();

        }
    }

    private void dist(int i) {

 
        
        

        try {

            //if an input stream has input then execute distribute method ready should not block
            String line;
            //synchronized (users) {
            while ((line = in.get(i).readLine()) != null) {

                System.out.println("ready: " + i);
                System.out.println("in" + in.get(i).toString());
                String input = line; //in.get(i).readLine();

                //if user exits clear out space in the room
                if (input.equals("exit")) {
                    out.get(i).println("Goodbye");
                    users.get(i).getConnection().close();
                    out.remove(i);
                    in.remove(i);
                    break;
                } else {
                    double current = System.currentTimeMillis() / 1000.0;
                    users.get(i).messagesSent++;
                    int sent = users.get(i).messagesSent;
                    double elapsed = Math.abs(instanceTime - current);
                   
                    //max messages per second 7
                    if(sent/elapsed < 7){
                    users.get(i).messagesSent--;
                    Room.this.distribute(users.get(i).getConnection(), input);
                    instanceTime = current;
                    }
                    else{
                        (users.get(i).getOut()).println("to many Messages send exceeded 7 msg's per second CLOSING CONNECTION");
                        users.get(i).getConnection().close();  
                        users.remove(i);
                        in.remove(i);
                        out.remove(i);
                    }
                }
            }
            //}

        } catch (IOException ex) {
            System.out.println("exveption in room 157");
        }
    }

    //I shouldnt have to check all users everytime, gotta be a better way
    /**
     *
     * @param sender
     * @param msg
     * @throws IOException
     */
    public void distribute(Socket sender, String msg) throws IOException {
        System.out.println("the size is :" + users.size());
        
        int size = users.size();
        
        String sendersName = "";
        for (int i = 0; i < size; i++) {
            System.out.println(sendersName);
            if (users.get(i).doesUserOwnSocket(sender)) {
                sendersName = users.get(i).getUsername();
                
                        
                break;
            }
        }
        
        for (int i = 0; i < users.size(); i++) {
            System.out.println(i);
            (users.get(i).getOut()).println(sendersName + ": " + msg);

        }

    }
}
