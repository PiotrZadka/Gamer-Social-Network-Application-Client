package com.example.registrationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class users_profiles extends AppCompatActivity {

    Button exitButton;
    ListView usersList;
    ArrayAdapter adapter;
    ArrayList usersArray = new ArrayList();
    String userId;
    JSONArray usersArrayJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profiles);
        this.getSupportActionBar().hide();

        exitButton = findViewById(R.id.exitButton);
        usersList = findViewById(R.id.usersList);

        retrieveUsers();

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(users_profiles.this, main_content.class);
                startActivity(intent);
            }
        });

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(users_profiles.this, view_profile.class);
                intent.putExtra("usersArrayJson",usersArrayJson.toString());
                intent.putExtra("element", String.valueOf(i));
                startActivity(intent);
            }
        });
    }

    private void retrieveUsers(){

        final String URL_COLLECTION = "http://13.59.14.52/retrieveUsers.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COLLECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("users");
                            usersArrayJson = jsonArray;

                            if(success.equals("1")){
                                // Populate list view
                                for(int i = 0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    userId = object.getString("id").trim();
                                    String userName = object.getString("name").trim();
                                    String userEmail = object.getString("email").trim();
                                    usersArray.add(userName+" "+userEmail);
                                }

                                adapter = new ArrayAdapter(users_profiles.this,android.R.layout.simple_dropdown_item_1line,usersArray);
                                usersList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        }catch(JSONException e){
                            System.out.print(e);
                            Toast.makeText(users_profiles.this,"Error "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(users_profiles.this,"Error "+error, Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
