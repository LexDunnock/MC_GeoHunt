/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Alexander Perschke
 */
public class Database {
    
    private static final String HOST = "jdbc:derby://localhost:1527/GeoHuntDatabase";
    private static final String USERNAME = "AlexFelix";
    private static final String PASSWORD = "PersUnth";
    
    private Connection con = null;
    
    public Database() {
        try {
            //Establish connection
            con = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * ensures an unique Username by scanning the recent Database
     * 
     * @param username the username to look for
     * @return returns false if the username already exists and true if it is valid
     */
    public boolean ensureUniqueUsername(String username) {
        try {
            String selectQuery = "SELECT * FROM users WHERE username = '" + username + "'";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if(resultSet.next()) {
                    if(resultSet.getString("username").equals(username) || resultSet.getString("username").equals("")){
                        System.out.println("Sorry! This username allready exists.");
                        return false;
           }
         }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return true;
    }
    
    /**
     * get last inserted ID
     * 
     * @param selectQuery  
     * @param databaseName name of the Database
     * @return returns the last inserted id
     */
    private int getLastInsertedID(String selectQuery, String databaseName)
    {
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            int lastInsertedID = 0;
            if(resultSet != null) {
                while(resultSet.next()) {
                    lastInsertedID = resultSet.getInt("id");
                }
            }
            resultSet.close();
            statement.close();
            System.out.println(databaseName+ "ID:" + (lastInsertedID + 1) + " generated");
            return lastInsertedID;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }
    
    //Last inserted ID's for all databases
    private int getLastInsertedUserID()
    {
        String selectQuery = "select id from users";
        return getLastInsertedID(selectQuery, "User");
    }
    
    private int getLastInsertedCacheID()
    {
        String selectQuery = "select id from caches";
        return getLastInsertedID(selectQuery, "Cache");
    }
    
    /**
     * creates an User
     * 
     * @param username the Name which the User choose, all other variabels are set by default
     * @return returns the created User
     */
    public JSONObject createUser(String username) {
        JSONObject newUser = null;
        //increasing id
        if(ensureUniqueUsername(username)) {
            int newID = getLastInsertedUserID() + 1;

            String insertQuery = "INSERT INTO users (id, username) values (?, ?)";
            PreparedStatement preparedStatement;

            try {
                newUser = new JSONObject();
                preparedStatement = con.prepareStatement(insertQuery);
                preparedStatement.setInt(1, newID);
                preparedStatement.setString(2, username);
                newUser.put("ID", newID);
                newUser.put("Username", username);
                newUser.put("Credits", 0);
                newUser.put("Admin", false);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                
            } catch(SQLException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("Please enter a new Username");
        }
        return newUser;
    }
    
    /**
     * select an User
     * 
     * @param id id of the User that should be selected
     * @return returns the selected user
     */
    public JSONObject selectUser(int id) {
        JSONObject selectedUser = null;
        try {
            String selectQuery = "SELECT * FROM users WHERE id = " + id + "";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            selectedUser = new JSONObject();
            selectedUser.put("ID", resultSet.getInt("id"));
            selectedUser.put("Username", resultSet.getString("username"));
            selectedUser.put("Credits", resultSet.getInt("credits"));
            selectedUser.put("Admin", resultSet.getBoolean("admin"));
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return selectedUser;
    }
    
    /**
     * updates the credits of the Player
     * 
     * @param id the id of the player whos credits should be updated
     * @param credits the amount of credits which should be written to the Database
     */
    public void syncUserCredits(int id, int credits) {
        String updateQuery = "UPDATE users SET credits = ? WHERE id = ?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement(updateQuery);
            preparedStatement.setInt(1, credits);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch(SQLException e) {
                System.err.println(e.getMessage());
        }
    }
    
    
    /**
     * creates a Cache
     * 
     * @param cachename the Name for the Cache
     * @param posX decimal value of the northern coordinate 
     * @param posY decimal value of the eastern coordinate
     */
    public void createCache(String cachename, double posX, double posY) {
        //increasing id
        System.out.println("Tada");
        int newID = getLastInsertedCacheID() + 1;
        
        String insertQuery = "INSERT INTO caches (id, positionx, positiony, name) values (?, ?, ?, ?)";
        PreparedStatement preparedStatement;
        
        try {
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setInt(1, newID);
            preparedStatement.setDouble(2, posX);
            preparedStatement.setDouble(3, posY);
            preparedStatement.setString(4, cachename);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * slects all caches in a radius arround the current position and saves it to an array
     * 
     * @param posX northern coordinate of the player
     * @param posY eastern coordinate of the player
     * @return returns an JSON-Object array with all nearby caches
     */
    public JSONArray syncCaches(double posX, double posY) {
        JSONArray nearbyCaches = new JSONArray();
        try {
            String selectQuery = "SELECT * FROM caches WHERE positionX  < " + (posX + 1) + "AND positionX > " + (posX - 1) + "AND positionY < " + (posY + 1) + "AND positionY > " + (posY - 1) + "";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if(resultSet.next()) {
                JSONObject selectedCache = new JSONObject();
                selectedCache.put("ID", resultSet.getInt("id"));
                selectedCache.put("PositionX", resultSet.getDouble("positionx"));
                selectedCache.put("PositionY", resultSet.getDouble("positiony"));
                selectedCache.put("Name", resultSet.getString("name"));
                nearbyCaches.add(selectedCache);
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return nearbyCaches;
    }
    
    /**
     * writes a collected Cache to the FoundCaches Table
     * 
     * @param userID 
     * @param cacheID
     */
    public void collectCache(int userID, int cacheID) {
        String insertQuery = "INSERT INTO foundcaches (userid, cacheid) values (?, ?)";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, cacheID);
            preparedStatement.close();
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * shows the Player all of his found caches, by listing all caches according to its own id
     * 
     * @param id the id of the Player
     * @return returns an JSON-Object array with all found caches
     */
    public JSONArray showFoundCaches(int id) {
        JSONArray foundCaches = new JSONArray();
        try {
            String selectQuery = "SELECT * FROM foundcaches WHERE userid = " + id + "";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if(resultSet.next()) {
                String selectQueryCaches = "SELECT * FROM caches WHERE id = " + resultSet.getInt("cacheid") + "";
                Statement statementCaches = con.createStatement();
                ResultSet resultSetCaches = statementCaches.executeQuery(selectQueryCaches);
                if(resultSetCaches.next()) {
                    JSONObject selectedCache = new JSONObject();
                    selectedCache.put("ID", resultSetCaches.getInt("id"));
                    selectedCache.put("PositionX", resultSetCaches.getDouble("positionx"));
                    selectedCache.put("PositionY", resultSetCaches.getDouble("positiony"));
                    selectedCache.put("Name", resultSetCaches.getString("name"));
                    foundCaches.add(selectedCache);
                }
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return foundCaches;
    }
    
    //delete
    private void deleteEntry(int id, String deleteOrder, String databaseName) {
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(deleteOrder);
            System.out.println("Entry " + databaseName + "ID:" + id + " deleted");
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    //delete User
    public void deleteUser(int id) {
         String deleteOrder = "DELETE FROM users " + "WHERE id = " + id;
         deleteEntry(id, deleteOrder, "User");
    }
    
    //delete User
    public void deleteCache(int id) {
         String deleteOrder = "DELETE FROM caches " + "WHERE id = " + id;
         deleteEntry(id, deleteOrder, "Caches");
    }
}
