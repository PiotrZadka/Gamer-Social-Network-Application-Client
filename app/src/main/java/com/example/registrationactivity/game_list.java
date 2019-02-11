package com.example.registrationactivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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



public class game_list extends AppCompatActivity {

    ArrayList gamesArray = new ArrayList();
    SearchView searchGame;
    Button clickMe;
    ListView gameList;

    // https://api.thegamesdb.net/key.php
    String API_KEY = "eb8118a629a5c370b1ec29b2a9c6b24730daa44219efdc56756425747ba195fd ";
    String URL = "https://api.thegamesdb.net/Games/ByGameName?apikey="+API_KEY+"&name=Zelda";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        gameList = findViewById(R.id.gameList);
        clickMe = findViewById(R.id.retrieveGames);
        searchGame = findViewById(R.id.searchGame);
        searchGame.setQueryHint("Search game");
        clickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(game_list.this, "CLICKING", Toast.LENGTH_SHORT).show();
                fetchGames();


            }
        });


    }

    private void fetchGames(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String jsonRESULT = jsonObject.getString("data");
                            jsonObject = new JSONObject(jsonRESULT);
                            JSONArray resultsArray = jsonObject.getJSONArray("games");
                            for(int i = 0; i<resultsArray.length(); i++){
                                JSONObject game_title = resultsArray.getJSONObject(i);
                                gamesArray.add(game_title.getString("game_title"));
                            }
                            // Print array list of games
                            System.out.println(gamesArray);



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
    }

}
