/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dstok
 */
public class active {
    
    private String[] args;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private Scanner input;
    private BufferedReader stdIn;

    public active(String[] args) {
        this.args = args;
        try {
            this.executeComms();
            
        } catch (IOException ex) {
          
        }
    }

   
    
    private void executeComms() throws IOException{
        System.out.println("start exe");
        
        String serverHostname = new String ("127.0.0.1");
		
		 input = new Scanner(System.in);
		
		String portPass = new String("10007");
                
                if(args.length > 1){
                    portPass = args[1];
                }else{
                    System.out.println("please enter a port to connect to");
                    
                    portPass = input.nextLine();
                    
                    
                    
                }
                
                
		
		int port = Integer.parseInt(portPass);
                
                
		
		

        if (args.length > 0) {
           //pass the hsotname through cmd argument
           serverHostname = args[0];
        }
        System.out.println ("Attemping to connect to host " + serverHostname + " on port " + port);

       
        

        try {
            //Connect to server and open IO stream
            socket = new Socket(serverHostname, port);
			
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for " + "the connection to: " + serverHostname);
            System.exit(1);
        }

	stdIn = new BufferedReader( new InputStreamReader(System.in));
	String userInput;
      
        new Thread(() -> {
            while(!socket.isClosed()){
                try {
                    String msg = in.readLine();
             
                    if(msg.equals("goodbye")){
                        System.out.println("exit");
                        socket.close();
                    }
                    System.out.print("\n" + msg);
                } catch (IOException ex) {
                  
                }
                //when user exits nullpointer will be thrown, catch and exit as thats what user wants
                catch (NullPointerException e){
                    System.out.println("Goodbye");
                    System.exit(0);
                }
            }
        }).start();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!socket.isClosed()){
                   
                    String userInput = null;
                    userInput = input.nextLine();
                   
                    out.println(userInput);
                }
            }
        }).start();
        
        while(!socket.isClosed()){
            
        }

        out.close();
	in.close();
	stdIn.close();
	
    }
}
    
    
    
    

