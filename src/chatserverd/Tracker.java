/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserverd;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author dstok
 */
public class Tracker {
    
    private ArrayList<Room> rooms = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private final ExecutorService manager =  Executors.newFixedThreadPool(50);
	private List<ArrayList<User>> usersInRoom = Collections.synchronizedList(new ArrayList<>());
	private boolean isFirstRun = true;

    public Tracker() { 
    }
    
    public void init(){
       //current issue is this starts create thread meaninng join never passes new user gets hijacked
	   isFirstRun = false;
        Room stockRoom = new Room("Default Room", users.get(0), this);
		
        rooms.add(stockRoom);
		manager.submit(stockRoom);
		//usersInRoom.add(stockRoom.getUsers());
    }
    
    
    
    public void displayRooms(PrintWriter out){
		if(isFirstRun){
        init();
		}
       
        for(Room i: rooms){
            System.out.println(i.getRoomName());
            out.print("Room Name: " + i.getRoomName());
            users  = i.getUsers();
            out.print(" " + "Users Online: ");
            int numOnline = 0;
            for(User j: users){
                out.print(j.getUsername() + ", ");
                numOnline++;
            }
            out.println("Online: " + numOnline + " /100");
        }
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(User user) {
        users.add(user);
        
        
    }
    public void createRoom(Room passed){
        rooms.add(passed);
		//usersInRoom.add(passed.getUsers());
		
    }

    public ExecutorService getManager() {
        return manager;
    }
	
	public ArrayList<User> getUserFromRoom(Room room){
		int index = 0;
		for (Room x : rooms) {
			if(x.getName().equals(room.getName())){
				break;
			}
			index++;
		}
		return usersInRoom.get(index);
			
		
	}
	
	public void addUserRoom(User user, Room room){
		int index = 0;
		for (Room x : rooms) {
			if(x.getName().equals(room.getName())){
				break;
			}
			index++;
		
	}
		usersInRoom.get(index).add(user);
    
    
    
    
    
    
    
    
    
    
}
}
