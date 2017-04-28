package com.example.alexander.geocachehunt;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class CharacterActivity extends AppCompatActivity {

    private ImageView imgViewFace;
    private ImageView imgViewEyes;
    private ImageView imgViewNose;
    private ImageView imgViewMouth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character_activity);

        // "Faces"
        imgViewFace = (ImageView) findViewById(R.id.faceImageView);
        ImageButton imgButtonFace1 = (ImageButton) findViewById(R.id.face1_creatorButton);
        ImageButton imgButtonFace2 = (ImageButton) findViewById(R.id.face2_creatorButton);
        ImageButton imgButtonFace3 = (ImageButton) findViewById(R.id.face3_creatorButton);

        assert imgButtonFace1 != null;
        imgButtonFace1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewFace.setImageResource(R.drawable.face1);
                PlayerClass.setPlayerColor(getApplicationContext(), "@drawable/face1");
            }
        });

        assert imgButtonFace2 != null;
        imgButtonFace2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewFace.setImageResource(R.drawable.face2);
                PlayerClass.setPlayerColor(getApplicationContext(), "@drawable/face2");
            }
        });

        assert imgButtonFace3 != null;
        imgButtonFace3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewFace.setImageResource(R.drawable.face3);
                PlayerClass.setPlayerColor(getApplicationContext(), "@drawable/face3");
            }
        });


        // "Eyes"
        imgViewEyes = (ImageView) findViewById(R.id.eyesImageView);
        ImageButton imgButtonEyes1 = (ImageButton) findViewById(R.id.eyes1_creatorButton);
        ImageButton imgButtonEyes2 = (ImageButton) findViewById(R.id.eyes2_creatorButton);
        ImageButton imgButtonEyes3 = (ImageButton) findViewById(R.id.eyes3_creatorButton);
        ImageButton imgButtonEyes4 = (ImageButton) findViewById(R.id.eyes4_creatorButton);
        ImageButton imgButtonEyes5 = (ImageButton) findViewById(R.id.eyes5_creatorButton);
        ImageButton imgButtonEyes6 = (ImageButton) findViewById(R.id.eyes6_creatorButton);
        ImageButton imgButtonEyes7 = (ImageButton) findViewById(R.id.eyes7_creatorButton);
        ImageButton imgButtonEyes8 = (ImageButton) findViewById(R.id.eyes8_creatorButton);

        assert imgButtonEyes1 != null;
        imgButtonEyes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewEyes.setImageResource(R.drawable.eyes1);
                PlayerClass.setPlayerEyes(getApplicationContext(), "@drawable/eyes1");
            }
        });

        assert imgButtonEyes2 != null;
        imgButtonEyes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewEyes.setImageResource(R.drawable.eyes2);
                PlayerClass.setPlayerEyes(getApplicationContext(), "@drawable/eyes2");
            }
        });

        assert imgButtonEyes3 != null;
        imgButtonEyes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewEyes.setImageResource(R.drawable.eyes3);
                PlayerClass.setPlayerEyes(getApplicationContext(), "@drawable/eyes3");
            }
        });

        assert imgButtonEyes4 != null;
        imgButtonEyes4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewEyes.setImageResource(R.drawable.eyes4);
                PlayerClass.setPlayerEyes(getApplicationContext(), "@drawable/eyes4");
            }
        });

        assert imgButtonEyes5 != null;
        imgButtonEyes5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewEyes.setImageResource(R.drawable.eyes5);
                PlayerClass.setPlayerEyes(getApplicationContext(), "@drawable/eyes5");
            }
        });

        assert imgButtonEyes6 != null;
        imgButtonEyes6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewEyes.setImageResource(R.drawable.eyes6);
                PlayerClass.setPlayerEyes(getApplicationContext(), "@drawable/eyes6");
            }
        });

        assert imgButtonEyes7 != null;
        imgButtonEyes7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewEyes.setImageResource(R.drawable.eyes7);
                PlayerClass.setPlayerEyes(getApplicationContext(), "@drawable/eyes7");
            }
        });

        assert imgButtonEyes8 != null;
        imgButtonEyes8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewEyes.setImageResource(R.drawable.eyes8);
                PlayerClass.setPlayerEyes(getApplicationContext(), "@drawable/eyes8");
            }
        });


        // "Nose"
        imgViewNose = (ImageView) findViewById(R.id.noseImageView);
        ImageButton imgButtonNose1 = (ImageButton) findViewById(R.id.nose1_creatorButton);
        ImageButton imgButtonNose2 = (ImageButton) findViewById(R.id.nose2_creatorButton);
        ImageButton imgButtonNose3 = (ImageButton) findViewById(R.id.nose3_creatorButton);
        ImageButton imgButtonNose4 = (ImageButton) findViewById(R.id.nose4_creatorButton);

        assert imgButtonNose1 != null;
        imgButtonNose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewNose.setImageResource(R.drawable.nose1);
                PlayerClass.setPlayerNose(getApplicationContext(), "@drawable/nose1");
            }
        });

        assert imgButtonNose2 != null;
        imgButtonNose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewNose.setImageResource(R.drawable.nose2);
                PlayerClass.setPlayerNose(getApplicationContext(), "@drawable/nose2");
            }
        });

        assert imgButtonNose3 != null;
        imgButtonNose3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewNose.setImageResource(R.drawable.nose3);
                PlayerClass.setPlayerNose(getApplicationContext(), "@drawable/nose3");
            }
        });

        assert imgButtonNose4 != null;
        imgButtonNose4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewNose.setImageResource(R.drawable.nose4);
                PlayerClass.setPlayerNose(getApplicationContext(), "@drawable/nose4");
            }
        });


        // "Mouth"
        imgViewMouth = (ImageView) findViewById(R.id.mouthImageView);
        ImageButton imgButtonMouth1 = (ImageButton) findViewById(R.id.mouth1_creatorButton);
        ImageButton imgButtonMouth2 = (ImageButton) findViewById(R.id.mouth2_creatorButton);

        assert imgButtonMouth1 != null;
        imgButtonMouth1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewMouth.setImageResource(R.drawable.mouth1);
                PlayerClass.setPlayerMouth(getApplicationContext(), "@drawable/mouth1");
            }
        });

        assert imgButtonMouth2 != null;
        imgButtonMouth2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgViewMouth.setImageResource(R.drawable.mouth2);
                PlayerClass.setPlayerMouth(getApplicationContext(), "@drawable/mouth2");
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        // update Character
        // Color
        int playerColorResource = getResources().getIdentifier(PlayerClass.getPlayerColor(getApplicationContext()), null, getPackageName());
        Drawable playerColorRes = getResources().getDrawable(playerColorResource);
        imgViewFace.setImageDrawable(playerColorRes);
        // Eyes
        int playerEyesResource = getResources().getIdentifier(PlayerClass.getPlayerEyes(getApplicationContext()), null, getPackageName());
        Drawable playerEyesRes = getResources().getDrawable(playerEyesResource);
        imgViewEyes.setImageDrawable(playerEyesRes);
        // Nose
        int playerNoseResource = getResources().getIdentifier(PlayerClass.getPlayerNose(getApplicationContext()), null, getPackageName());
        Drawable playerNoseRes = getResources().getDrawable(playerNoseResource);
        imgViewNose.setImageDrawable(playerNoseRes);
        // Mouth
        int playerMouthResource = getResources().getIdentifier(PlayerClass.getPlayerMouth(getApplicationContext()), null, getPackageName());
        Drawable playerMouthRes = getResources().getDrawable(playerMouthResource);
        imgViewMouth.setImageDrawable(playerMouthRes);
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