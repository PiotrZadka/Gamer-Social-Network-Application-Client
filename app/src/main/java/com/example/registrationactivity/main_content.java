package com.example.registrationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class main_content extends AppCompatActivity {

    private Button logout;
    private TextView usernameWelcome;
    private static String URL = "http://13.59.14.52/userDetails.php";
    SessionManager sessionManager;
    ImageView gameList,userProfile, usersProfiles, chatImage;
    String getId;
    TextView hyperlinkDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        this.getSupportActionBar().hide();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        logout = findViewById(R.id.logout_main);
        usernameWelcome = findViewById(R.id.userWelcomeLabel);
        gameList = findViewById(R.id.gameListImage);
        userProfile = findViewById(R.id.userProfileImage);
        usersProfiles = findViewById(R.id.usersProfilesImage);
        chatImage = findViewById(R.id.chatImage);
        hyperlinkDB = findViewById(R.id.hyperlinkDB);

        HashMap<String, String> user = sessionManager.getUserDetails();
        getId = user.get(sessionManager.ID);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logout();
            }
        });

        gameList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_content.this, game_list.class);
                startActivity(intent);
            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_content.this, user_profile.class);
                startActivity(intent);
            }
        });

        usersProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_content.this, users_profiles.class);
                startActivity(intent);
            }
        });

        chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(main_content.this, chat.class);
                startActivity(intent);
            }
        });

        Linkify.addLinks(hyperlinkDB,Linkify.ALL);
    }

    private void getUserDetails(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if(success.equals("1")){

                                for(int i = 0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();
                                    usernameWelcome.setText(strName);

                                }
                            }
                        }catch (JSONException e){
                            System.out.print(e);

                            Toast.makeText(main_content.this,"Error "+e.toString(),Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.print(error);
                        Toast.makeText(main_content.this,"Error "+error.toString(),Toast.LENGTH_SHORT);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume(){
        super.onResume();
        getUserDetails();
    }

}
