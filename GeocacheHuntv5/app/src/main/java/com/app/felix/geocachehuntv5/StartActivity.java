package com.app.felix.geocachehuntv5;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup;

import com.google.android.gms.games.Player;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The StartActivity is the first view of the app.
 * There is a face of the users which the user can edit.
 * He can go to the map and view for marker or set marker.
 *
 * There are interfaces for the communication with the backend.
 *
 */
public class StartActivity extends Activity {

    private EditText input;
    private TextView playerName;
    private TextView playerScore;
    private ImageButton imgButtonCharacter;
    private int playerColorResource;
    private ImageView imgViewEyes;
    private int playerEyesResource;
    private ImageView imgViewNose;
    private int playerNoseResource;
    private ImageView imgViewMouth;
    private int playerMouthResource;
    private ListView markerListView;
    private List<String> playerMarkerArray;
    private ArrayAdapter<String> arrayAdapter;
    private Socket mySocket = null;
    private String globalServerResponse = "";
    private String IP = "192.168.137.1";
    //private String IP = "127.0.0.1";
    private static final int PORTNUMBER = 60123;
    private static final String debugString = "debug";

    /**
     * OnCreate method from the activity shows the first view of the app
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        //setting up the socket
        new Thread() {
            @Override
            public void run() {
                try {
                    mySocket = new Socket(IP, PORTNUMBER);

                } catch(Exception e) {
                    System.err.println(e);
                }
            }
        }.start();
        while(mySocket == null) {}
        while(mySocket.isConnected() != true) {}

        // "Player"
        playerName = (TextView) findViewById(R.id.CharacterName);
        playerName.setText(PlayerClass.getPlayerName(getApplicationContext()));
        playerScore = (TextView) findViewById(R.id.Score);
        playerScore.setText(PlayerClass.getPlayerScore(getApplicationContext()));

        // "Character"
        // Face
        imgButtonCharacter = (ImageButton) findViewById(R.id.CharacterButton);
        playerColorResource = getResources().getIdentifier(PlayerClass.getPlayerColor(getApplicationContext()), null, getPackageName());
        Drawable playerColorRes = getDrawable(playerColorResource);//alt: getResources().getDrawable(playerColorResource);
        imgButtonCharacter.setImageDrawable(playerColorRes);
        imgButtonCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, CharacterActivity.class));
            }
        });
        // Eyes
        imgViewEyes = (ImageView) findViewById(R.id.CharacterEyes);
        playerEyesResource = getResources().getIdentifier(PlayerClass.getPlayerEyes(getApplicationContext()), null, getPackageName());
        Drawable playerEyesRes = getDrawable(playerEyesResource);
        imgViewEyes.setImageDrawable(playerEyesRes);
        // Nose
        imgViewNose = (ImageView) findViewById(R.id.CharacterNose);
        playerNoseResource = getResources().getIdentifier(PlayerClass.getPlayerEyes(getApplicationContext()), null, getPackageName());
        Drawable playerNoseRes = getDrawable(playerNoseResource);
        imgViewNose.setImageDrawable(playerNoseRes);
        // Mouth
        imgViewMouth = (ImageView) findViewById(R.id.CharacterMouth);
        playerMouthResource = getResources().getIdentifier(PlayerClass.getPlayerEyes(getApplicationContext()), null, getPackageName());
        Drawable playerMouthRes = getDrawable(playerMouthResource);
        imgViewMouth.setImageDrawable(playerMouthRes);

        // "Map"
        Button buttonMap = (Button) findViewById(R.id.MapButton);
        assert buttonMap != null;
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MapsActivity.class));
                //startActivity(new Intent(StartActivity.this, CameraActivity.class));
            }
        });

        // "Shop"
        Button buttonShop = (Button) findViewById(R.id.ShopButton);
        assert buttonShop != null;
        buttonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, CameraActivity.class)); // Test Kamera
                //Toast.makeText(getApplicationContext(), "This is an alpha. Button not supported", Toast.LENGTH_SHORT).show();
            }
        });

        // a list for the markers in an array
        markerListView = (ListView) findViewById(R.id.MarkerListView);
        playerMarkerArray = new ArrayList<String>(Arrays.asList(PlayerClass.getMarkerList(getApplicationContext())));
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, playerMarkerArray){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };
        markerListView.setAdapter(arrayAdapter);

        if (playerName.getText().toString().equals("PlayerName")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter a playername");
            input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface Dialog, int which) {
                    PlayerClass.setPlayerName(getApplicationContext(), input.getText().toString());
                    playerName.setText(PlayerClass.getPlayerName(getApplicationContext()));
                    createUser(PlayerClass.getPlayerName(getApplicationContext()));
                }
            });
            // Show dialog
            AlertDialog ad = builder.create();
            ad.show();
        }
    }



    @Override
    protected void onResume(){
        super.onResume();
        // update PlayerScore
        playerScore.setText(PlayerClass.getPlayerScore(getApplicationContext()));
        // update Character
        // Color
        playerColorResource = getResources().getIdentifier(PlayerClass.getPlayerColor(getApplicationContext()), null, getPackageName());
        //Drawable playerColorRes = getResources().getDrawable(playerColorResource);
        // new ? ContextCompat.getDrawable(getApplicationContext(),R.drawable.back_arrow)
        // new ? ResourcesCompat.getDrawable(getResources(),R.drawable.name, null);
        // ResourcesCompat.getDrawable(getResources(),R.drawable.playerColorResource, null);
        Drawable playerColorRes = getDrawable(playerColorResource);
        imgButtonCharacter.setImageDrawable(playerColorRes);
        // Eyes
        playerEyesResource = getResources().getIdentifier(PlayerClass.getPlayerEyes(getApplicationContext()), null, getPackageName());
        Drawable playerEyesRes = getDrawable(playerEyesResource);
        imgViewEyes.setImageDrawable(playerEyesRes);
        // Nose
        playerNoseResource = getResources().getIdentifier(PlayerClass.getPlayerNose(getApplicationContext()), null, getPackageName());
        Drawable playerNoseRes = getDrawable(playerNoseResource);
        imgViewNose.setImageDrawable(playerNoseRes);
        // Mouth
        playerMouthResource = getResources().getIdentifier(PlayerClass.getPlayerMouth(getApplicationContext()), null, getPackageName());
        Drawable playerMouthRes = getDrawable(playerMouthResource);
        imgViewMouth.setImageDrawable(playerMouthRes);

        syncUserCredits((int)Integer.parseInt(PlayerClass.getPlayerID(getApplicationContext())), (int)Integer.parseInt(PlayerClass.getPlayerScore(getApplicationContext())));
        //syncUserCredits(1, (int)Integer.parseInt(PlayerClass.getPlayerScore(getApplicationContext())));

        arrayAdapter.notifyDataSetChanged();
    }


    // Code von Alex

    /**
     * creates an User entry on the Server Database and sets the Player ID
     *
     * @param username the name the Player has entered
     */
    public void createUser(String username) {
        try {
            String Username = username;
            JSONObject newUser = new JSONObject();
            // fill JSON-Object
            newUser.put("keyWord", "createUser");
            newUser.put("Username", Username);
            newUser.put("Credits", 0);
            newUser.put("Admin", true);
            // send User to Server
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
            bw.write(newUser.toString());
            bw.newLine();
            bw.flush();
            // wait for Server response
            waitForResponse();
            // Checks if the recieved answer can be converted to a JSON-Object or if errors have to be handled
            if (errorHandler(globalServerResponse) == false) {
                Log.d(StartActivity.debugString, globalServerResponse);
                JSONObject User = new JSONObject(globalServerResponse);
                PlayerClass.setPlayerID(getApplicationContext(), User.getInt("ID"));
            }
            //setting back globalServerResponse so that waitForResponse will work again
            globalServerResponse = "";
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * updates the credits of the Player
     *
     * @param id the id of the player whos credits should be updated
     * @param credits the amount of credits which should be written to the Database
     */
    private void syncUserCredits(int id, int credits) {
        try {
            JSONObject syncUser = new JSONObject();
            // fill JSON-Object
            syncUser.put("keyWord", "syncUserCredits");
            syncUser.put("ID", id);
            syncUser.put("Credits", credits);
            // send User to Server
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
            bw.write(syncUser.toString());
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * A method for waiting for server response
     */
    private void waitForResponse() {
        getResponse();
        //really sloppy Solution for forcing the Code to wait
        while(globalServerResponse == "") {}
    }

    /**
     * A method for getting the server response. The global String globalServerResponse is set an can be read
     * to achieve access to different JSONObjects.
     */
    private void getResponse() {
        new Thread() {
            @Override
            public void run() {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                    globalServerResponse = br.readLine();
                } catch (Exception e) {
                    globalServerResponse = "SomethingWentWrong";
                    System.err.println(e);
                }
            }
        }.start();
    }

    /**
     * A switch case method to decide if the server responds with an error
     *
     * @param error String that indicates the type of error
     */
    private boolean errorHandler(String error) {
        switch(error) {
            case "SomethingWentWrong":
                return true;
            case "invalidUsername":
                handleInvalidUsername();
                return true;
            default:
                return false;
        }
    }

    /**
     * Handels an invlid Username and infrom the User with a short AlertDialog
     */
    private void handleInvalidUsername() {
        AlertDialog alertDialog = new AlertDialog.Builder(StartActivity.this).create();
        alertDialog.setTitle("Invalid Username");
        alertDialog.setMessage("Please enter a new Username!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }



    //possible create Cache method tested in extern Activity for Database testing
    //can't be used because frontend errors in MapsActivity
    //
    //    public void createCache(View view) {
    //        try{
    //            String cacheName = "";
    //            JSONObject newCache = new JSONObject();
    //            // Read Name
    //            cacheName = cacheInput.getText().toString();
    //            newSomething.put("keyWord", "createCache");
    //            newSomething.put("Cachename", cacheName);
    //            newSomething.put("PositionX", getNorthernCoordinate());
    //            newSomething.put("PositionY", getEasternCoordinate());
    //            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream()));
    //            bw.write(newCache.toString());
    //            bw.newLine();
    //            bw.flush();
    //        } catch (Exception e) {
    //            System.err.println(e);
    //        }
    //    }
}