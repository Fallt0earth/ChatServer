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

    public Room(User newUser) {
        this.users = Collections.synchronizedList(new ArrayList<>());
        users.add(newUser);
        for (User i : users) {
            out.add(i.getOut());
            in.add(i.getIn());
        }

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
                System.out.println("new in");
                i++;
                out.add(users.get(i).getOut());
                in.add(users.get(i).getIn());

                int x = i;
                flag = false;
                Runnable runnable = () -> {
                    System.out.println("lambda");

                    dist(x);
                };

                track.getManager().execute(runnable);
            }
            oldSize = users.size();

        }
    }

    private void dist(int i) {

        System.out.println("dist method run 135");

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

                    Room.this.distribute(users.get(i).getConnection(), input);
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
        int size = users.size();
        String sendersName = "";
        for (int i = 0; i < size; i++) {
            if (users.get(i).doesUserOwnSocket(sender)) {
                sendersName = users.get(i).getUsername();
                break;
            }
        }

        for (int i = 0; i < size; i++) {

            (users.get(i).getOut()).println(sendersName + ": " + msg);

        }

    }
}
