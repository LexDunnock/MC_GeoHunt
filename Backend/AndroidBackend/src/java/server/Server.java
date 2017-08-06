/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.json.simple.JSONArray;



/**
 *
 * @author Alexander Perschke
 */
public class Server {
    private static final int PORTNUMBER = 60123;
    

    public static void main(String[] args) throws ParseException {
        ServerSocket serverSocket = null;

        try {
            System.out.println("Server starting at port number " + PORTNUMBER);
            serverSocket = new ServerSocket(PORTNUMBER);
            
            Database db = new Database();
            System.out.println("Databaseoperations enabled");
            
            
            //Client connecting
            System.out.println("Waiting for clients to connect.");
            Socket socket = serverSocket.accept();
            System.out.println("A client has connected.");
            
            
            JSONParser jsonParser = new JSONParser();

            while(true){
            // Receive message from client
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            JSONObject jsonObjectRecive = new JSONObject();
            String jsonString = br.readLine();
            jsonObjectRecive = (JSONObject)jsonParser.parse(jsonString);
            
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    
            switch((String)jsonObjectRecive.get("keyWord")) {
                case "createUser":
                    JSONObject newUser = db.createUser((String)jsonObjectRecive.get("Username")); 
                    // Send message to client
                    if(newUser != null) {
                        bw.write(newUser.toString());
                        bw.newLine();
                        bw.flush();
                    } else {
                        bw.write("invalidUsername");
                        bw.newLine();
                        bw.flush(); 
                    }
                    break;
                case "selectUser":
                    JSONObject selectedUser = db.selectUser((int)(long)jsonObjectRecive.get("ID")); 
                    // Send message to client
                    if(selectedUser != null) {
                        bw.write(selectedUser.toString());
                        bw.newLine();
                        bw.flush();
                    } else {
                        bw.write("noSuchUser");
                        bw.newLine();
                        bw.flush(); 
                    }
                    break;
                case "syncUserCredits":
                    db.syncUserCredits((int)(long)jsonObjectRecive.get("ID"), (int)(long)jsonObjectRecive.get("Credits"));
                    break;
                case "createCache":
                    db.createCache((String)jsonObjectRecive.get("Cachename"), (double)jsonObjectRecive.get("PositionX"), (double)jsonObjectRecive.get("PositionY"));
                    break;
                case "collectCache":
                    db.collectCache((int)(long)jsonObjectRecive.get("UserID"), (int)(long)jsonObjectRecive.get("CacheID"));
                case "syncCaches":
                    JSONArray caches = db.syncCaches((double)jsonObjectRecive.get("PositionX"), (double)jsonObjectRecive.get("PositionX")); 
                    // Send message to client
                    if(caches != null) {
                        bw.write(caches.toString());
                        bw.newLine();
                        bw.flush();
                    } else {
                        bw.write("cantSyncCaches");
                        bw.newLine();
                        bw.flush(); 
                    }
                    break;
                case "showFoundCaches":
                    JSONArray foundCaches = db.showFoundCaches((int)(long)jsonObjectRecive.get("ID")); 
                    // Send message to client
                    if(foundCaches != null) {
                        bw.write(foundCaches.toString());
                        bw.newLine();
                        bw.flush();
                    } else {
                        bw.write("noCachesFound");
                        bw.newLine();
                        bw.flush(); 
                    }
                    break;
                case "deleteCache":
                    db.deleteCache((int)(long)jsonObjectRecive.get("ID"));
                    break;
                default: 
                    System.out.println("DEFAULT!!!");
                    break;
            }
           
        }
        // socket.close();

        } catch(IOException e) {
            
            e.printStackTrace();
        }   
    }
}
