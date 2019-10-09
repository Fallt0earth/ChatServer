/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserverd;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author dstok
 */
public class Distribution extends Thread implements Runnable{

	public Distribution() {
	}
	

	

	//CopyOnWriteArrayList<User> users
	public void sendToOne(User user,  PrintWriter out, String msg){
		System.out.println(user.getUsername());
		
		 (user.getOut()).println(user.getUsername() + ": " + msg + "\n");
		
	}
	
	
}
