package com.app.felix.geocachehuntv5;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
//import android.widget.Toast;

/**
 * The player class is setting some values of the user
 */
public class PlayerClass {

    //Playerdata
    private static final String PLAYER_SETTINGS = "PLAYER_SETTINGS";
    private static final String PlayerID = "0";
    private static final String PlayerName = "PlayerName";
    private static final String PlayerScore = "0";
    private static final String PlayerColor = "@drawable/face1";
    private static final String PlayerEyes = "@drawable/eyes1";
    private static final String PlayerNose = "@drawable/nose1";
    private static final String PlayerMouth = "@drawable/mouth1";

    //ListView
    private static final String CurrentMarker = "";
    private static final String Marker1 = "";
    //private static final String Marker2 = "";
    //private static final String Marker3 = "";
    //private static final String Marker4 = "";
    //private static final String Marker5 = "";
    //private static final String Marker6 = "";

    /**
     * instance of the PlayerClass
     */
    private PlayerClass() {
    }

    private static SharedPreferences getSharedPreferences(Context _context) {
        return _context.getSharedPreferences(PLAYER_SETTINGS, Context.MODE_PRIVATE);
    }

    //PlayerID
    public static String getPlayerID(Context _context) {
        return getSharedPreferences(_context).getString(PlayerID, "0");
    }

     public static void setPlayerID(Context _context, int _id) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        int pID = Integer.parseInt(getSharedPreferences(_context).getString(PlayerScore, "0"));
        pID = _id;
        editor.putString(PlayerID, String.valueOf(pID));
        editor.commit();
    }

    // PlayerName
    public static String getPlayerName(Context _context) {
        return getSharedPreferences(_context).getString(PlayerName, "PlayerName");
    }

    public static void setPlayerName(Context _context, String _playerName) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        editor.putString(PlayerName, _playerName);
        editor.commit();
    }

    // PlayerScore
    public static String getPlayerScore(Context _context) {
        return getSharedPreferences(_context).getString(PlayerScore, "0");
    }

    public static void setPlayerScore(Context _context, int _coins) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        int pScore = Integer.parseInt(getSharedPreferences(_context).getString(PlayerScore, "0"));
        pScore = pScore + _coins;
        editor.putString(PlayerScore, String.valueOf(pScore));
        editor.commit();
    }

    // PlayerColor
    public static String getPlayerColor(Context _context) {
        return getSharedPreferences(_context).getString(PlayerColor, "@drawable/face3");
    }

    public static void setPlayerColor(Context _context, String _playerColor) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        editor.putString(PlayerColor, _playerColor);
        editor.commit();
    }

    // PlayerEyes
    public static String getPlayerEyes(Context _context) {
        return getSharedPreferences(_context).getString(PlayerEyes, "@drawable/eyes1");
    }

    public static void setPlayerEyes(Context _context, String _playerEyes) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        editor.putString(PlayerEyes, _playerEyes);
        editor.commit();
    }

    // PlayerNose
    public static String getPlayerNose(Context _context) {
        return getSharedPreferences(_context).getString(PlayerNose, "@drawable/nose1");
    }

    public static void setPlayerNose(Context _context, String _playerNose) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        editor.putString(PlayerNose, _playerNose);
        editor.commit();
    }

    // PlayerMouth
    public static String getPlayerMouth(Context _context) {
        return getSharedPreferences(_context).getString(PlayerMouth, "@drawable/mouth1");
    }

    public static void setPlayerMouth(Context _context, String _playerMouth) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        editor.putString(PlayerMouth, _playerMouth);
        editor.commit();
    }

    // Marker
    public static String[] getMarkerList(Context _context) {
        String marker1 = getMarker1(_context);
        String[] markerList = {marker1};
        return markerList;
    }

    public static void addMarker(Context _context, String _markerName) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        editor.putString(Marker1, _markerName);
        editor.commit();
    }

    // Current Marker
    public static String getCurrentMarker(Context _context) {
        return getSharedPreferences(_context).getString(CurrentMarker, "no marker");
    }

    public static void setCurrentMarker(Context _context, String _marker) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        editor.putString(CurrentMarker, _marker);
        editor.commit();
    }

    public static String getMarker1(Context _context) {
        return getSharedPreferences(_context).getString(Marker1, "");
    }

    public static void setMarker1(Context _context, String _marker1) {
        final SharedPreferences.Editor editor = getSharedPreferences(_context).edit();
        editor.putString(Marker1, _marker1);
        editor.commit();
    }
}