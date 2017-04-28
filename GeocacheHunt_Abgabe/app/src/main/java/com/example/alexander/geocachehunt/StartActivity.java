package com.example.alexander.geocachehunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartActivity extends AppCompatActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        // "Player"
        playerName = (TextView) findViewById(R.id.CharacterName);
        playerName.setText(PlayerClass.getPlayerName(getApplicationContext()));
        playerScore = (TextView) findViewById(R.id.Score);
        playerScore.setText(PlayerClass.getPlayerScore(getApplicationContext()));

        // "Character"
        // Face
        imgButtonCharacter = (ImageButton) findViewById(R.id.CharacterButton);
        playerColorResource = getResources().getIdentifier(PlayerClass.getPlayerColor(getApplicationContext()), null, getPackageName());
        Drawable playerColorRes = getResources().getDrawable(playerColorResource);
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
        Drawable playerEyesRes = getResources().getDrawable(playerEyesResource);
        imgViewEyes.setImageDrawable(playerEyesRes);
        // Nose
        imgViewNose = (ImageView) findViewById(R.id.CharacterNose);
        playerNoseResource = getResources().getIdentifier(PlayerClass.getPlayerEyes(getApplicationContext()), null, getPackageName());
        Drawable playerNoseRes = getResources().getDrawable(playerNoseResource);
        imgViewNose.setImageDrawable(playerNoseRes);
        // Mouth
        imgViewMouth = (ImageView) findViewById(R.id.CharacterMouth);
        playerMouthResource = getResources().getIdentifier(PlayerClass.getPlayerEyes(getApplicationContext()), null, getPackageName());
        Drawable playerMouthRes = getResources().getDrawable(playerMouthResource);
        imgViewMouth.setImageDrawable(playerMouthRes);


        // "Map"
        Button buttonMap = (Button) findViewById(R.id.MapButton);
        assert buttonMap != null;
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MapsActivity.class));
            }
        });

        // "Shop"
        Button buttonShop = (Button) findViewById(R.id.ShopButton);
        assert buttonShop != null;
        buttonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "This is an alpha. Button not supported", Toast.LENGTH_SHORT).show();
            }
        });

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

        /**
         * Dialog to get PlayerName, if first started
          */
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
        Drawable playerColorRes = getResources().getDrawable(playerColorResource);
        imgButtonCharacter.setImageDrawable(playerColorRes);
        // Eyes
        playerEyesResource = getResources().getIdentifier(PlayerClass.getPlayerEyes(getApplicationContext()), null, getPackageName());
        Drawable playerEyesRes = getResources().getDrawable(playerEyesResource);
        imgViewEyes.setImageDrawable(playerEyesRes);
        // Nose
        playerNoseResource = getResources().getIdentifier(PlayerClass.getPlayerNose(getApplicationContext()), null, getPackageName());
        Drawable playerNoseRes = getResources().getDrawable(playerNoseResource);
        imgViewNose.setImageDrawable(playerNoseRes);
        // Mouth
        playerMouthResource = getResources().getIdentifier(PlayerClass.getPlayerMouth(getApplicationContext()), null, getPackageName());
        Drawable playerMouthRes = getResources().getDrawable(playerMouthResource);
        imgViewMouth.setImageDrawable(playerMouthRes);

        arrayAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ab_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu:
                Toast.makeText(getApplicationContext(), "This is an alpha. Button not supported", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_synchronize:
                Toast.makeText(getApplicationContext(), "This is an alpha. Button not supported", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_addFriend:
                Toast.makeText(getApplicationContext(), "This is an alpha. Button not supported", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
