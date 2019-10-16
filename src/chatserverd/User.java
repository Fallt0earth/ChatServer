/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserverd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dstok This only supports one socket per user instance, eventually
 * will figure out how to have multiple, big issue is figured out active
 */
public class User implements Runnable {

    private Socket connection;
    private InetAddress ip;
    private String username;
    private PrintWriter out;
    private BufferedReader in;
    private Tracker track;
    public int messagesSent = 0;

    // private Room 
    public User(Socket socket, String name, Tracker track) {
        connection = socket;
        this.ip = connection.getInetAddress();
        username = name;
        this.track = track;
    }

    public User(Socket socket, Tracker track) {
        connection = socket;
        this.ip = connection.getInetAddress();
        this.track = track;
    }

    public void init() {
        System.out.println("in init");
        try {
            this.menu(connection, track);
        } catch (IOException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean doesUserOwnSocket(Socket socket) {

        if (connection.equals(socket)) {
            return true;
        }

        return false;
    }

    public Socket getConnection() {
        return connection;
    }

    public String getUsername() {
        return username;
    }

    public void menu(Socket socket, Tracker track) throws IOException {

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("connection established");

        if (username == null) {
            out.println("Please enter a username");
            username = in.readLine();
            System.out.println(username);

        }
        out.println("Create a room or Join a room? (C/J)");
        System.out.println(track);
        track.displayRooms(out);
        char input;
        //loop so if user does not enter proper cmd 
        while (true) {
            input = (char) in.read();
            if (input == 'C') {
                this.createRoom();

                break;
            } else if (input == 'J') {
                this.joinRoom();

                break;
            } else {
                out.println("Please enter a valid option");
                out.println("Create a room or Join a room? (C/J)");
            }
        }

    }

    public InetAddress getIp() {
        return ip;
    }

    public void addSocket(Socket socket) {
        connection = socket;
    }

    @Override
    public void run() {

    }

    private void createRoom() throws IOException {
        String roomName = "";
        while (true) {
            out.println("What would you like to call the room");
            in.readLine();
            roomName = in.readLine();
            System.out.println(roomName);
            boolean flag = false;
            for (Room i : track.getRooms()) {
                if (i.getRoomName().equalsIgnoreCase(roomName)) {
                    out.println("Sorry, That name is in use please enter a different name");
                    flag = true;
                    break;
                }

            }
            //there is not another room with same room so creation can continue
            if (!flag) {
                break;
            }
        }
        //create new room obj
        Room created = new Room(roomName, this, track);
        //overall tracker is added 
        track.createRoom(created);
        //create thread
        //Thread roomRun = new Thread(created);
        //start 
        //created.start();
        track.getManager().submit(created);
        System.out.println("flag");

    }

    private void joinRoom() throws IOException {

        Room join = null;
        while (true) {
            out.println("What would you like to join");
            in.readLine();
            String roomName = in.readLine();
            System.out.println(roomName);
            boolean flag = false;
            for (Room i : track.getRooms()) {
                if (i.getRoomName().equalsIgnoreCase(roomName)) {
                    out.println("Joining.....");
                    join = i;
                    flag = true;
                    break;
                }
            }
            //room found so exit loop
            if (flag) {
                break;

            }
            out.println("Room not Found please try again");
        }
        //null pointer should never occur
        //track.addUserRoom(this, join);
        //join.flag.set(true);
        //	synchronized(this){
        join.addUser(this);
        //}

        //track.getManager().execute(join);
    }

    @Override
    public String toString() {
        return "User{" + "ip=" + ip + '}';
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public Tracker getTrack() {
        return track;
    }

    public void setTrack(Tracker track) {
        this.track = track;
    }

}
