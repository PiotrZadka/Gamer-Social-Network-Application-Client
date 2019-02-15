package com.example.registrationactivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class game_list extends AppCompatActivity {

    ArrayList gamesArray = new ArrayList();
    ArrayList platformArray = new ArrayList();
    SearchView searchGame;
    Button clickMe ,exitButton;
    ListView gameList;
    ArrayAdapter adapter;

    // API FOR -> https://api.thegamesdb.net/key.php
    String API_KEY = "ede83ff4569068653e175443c7380a8745cb63db71f81d3facbb09ada726eb39 ";  // PUBLIC
   // String API_KEY = "eb8118a629a5c370b1ec29b2a9c6b24730daa44219efdc56756425747ba195fd "; // PRIVATE



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        this.getSupportActionBar().hide();

        gameList = findViewById(R.id.gameList);
        clickMe = findViewById(R.id.retrieveGames);
        searchGame = findViewById(R.id.searchGame);
        searchGame.setQueryHint("Search game");
        exitButton = findViewById(R.id.exitButton);

        clickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get name to search
                String gameName = searchGame.getQuery().toString();

                // Search for games
                // https://api.thegamesdb.net/
                String URL = "https://api.thegamesdb.net/Games/ByGameName?apikey="+API_KEY+"&name="+gameName;
                platformArray = getGamePlatforms();
                fetchGames(URL);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(game_list.this, main_content.class);
                startActivity(intent);
            }
        });
    }

    // Browse online database for a specified game
    private void fetchGames(String URL){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            // Extract game data from jsonObject
                            JSONObject jsonObject = new JSONObject(response);
                            String jsonRESULT = jsonObject.getString("data");
                            jsonObject = new JSONObject(jsonRESULT);
                            JSONArray resultsArray = jsonObject.getJSONArray("games");

                            // Itterate through json object and add game titles to array
                            for(int i = 0; i<resultsArray.length(); i++){
                                JSONObject game_title = resultsArray.getJSONObject(i);
                                String platformID = game_title.get("platform").toString();

                                gamesArray.add(game_title.getString("game_title")+", "+getPlatformName(platformID));

                            }

                            adapter = new ArrayAdapter(game_list.this,android.R.layout.simple_dropdown_item_1line,gamesArray);
                            gameList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();




                        }catch (JSONException e) {
                            Toast.makeText(game_list.this, "ERROR "+e, Toast.LENGTH_SHORT).show();
                            System.out.println(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       System.out.print(error);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        gamesArray.clear();
    }

    private String getPlatformName(String id){
        String name = "TEST";
        System.out.println(platformArray);

        try{

            for(int i = 0; i < platformArray.size(); i++){
                JSONObject jsonObj = new JSONObject(platformArray.get(i).toString());
                if(jsonObj.getString("id").equals(id)){
                    name = jsonObj.getString("name");
                }
            }


        }catch(JSONException e){
            System.out.print(e);
        }
        return name;
    }

    private ArrayList getGamePlatforms(){
        String URL = "https://api.thegamesdb.net/Platforms?apikey="+API_KEY;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            // Extract game data from jsonObject
                            JSONObject jsonObject = new JSONObject(response);
                            String jsonRESULT = jsonObject.getString("data");
                            jsonObject = new JSONObject(jsonRESULT);
                            jsonRESULT = jsonObject.getString("platforms");
                            jsonObject = new JSONObject(jsonRESULT.trim());

                            Iterator<String> keys = jsonObject.keys();

                            int i = 0;
                            while(keys.hasNext()) {
                                String key = keys.next();
                                if (jsonObject.get(key) instanceof JSONObject) {
                                    JSONObject jsonObj = new JSONObject(jsonObject.getString(key));
                                    platformArray.add(jsonObj);
                                }
                            }
                        }catch (JSONException e) {
                            System.out.println("ERROR "+e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.print(error);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        return platformArray;
    }
}
