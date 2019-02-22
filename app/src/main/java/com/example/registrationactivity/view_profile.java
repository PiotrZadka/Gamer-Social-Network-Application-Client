package com.example.registrationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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


public class view_profile extends AppCompatActivity {

    Button exitButton;
    EditText nameEdit, emailEdit;
    ListView userListGames;
    String user_id;
    ArrayList collectionArray = new ArrayList();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        this.getSupportActionBar().hide();


        exitButton = findViewById(R.id.exitButton);
        nameEdit = findViewById(R.id.nameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        userListGames = findViewById(R.id.userListGames);


        Intent intent = getIntent();
        String listElement = intent.getStringExtra("element");
        String usersJsonArray = intent.getStringExtra("usersArrayJson");

        try{
            JSONArray usersArray = new JSONArray(usersJsonArray);
            JSONObject object = usersArray.getJSONObject(Integer.parseInt(listElement));
            user_id = object.getString("id");
            nameEdit.setText(object.getString("name"));
            emailEdit.setText(object.getString("email"));

        }catch(JSONException e){
            System.out.print("ERROR"+e);
        }

        retrieveCollection(user_id);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view_profile.this, users_profiles.class);
                startActivity(intent);
            }
        });

    }


    // Retrieving collection of games per profile
    private void retrieveCollection(String user_id){

        final String searchID = user_id;
        final String URL_COLLECTION = "http://13.59.14.52/retrieveGameCollection.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_COLLECTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("collection");

                            if(success.equals("1")){
                                // Populate list view
                                for(int i = 0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String gameName = object.getString("game_name").trim();
                                    collectionArray.add(gameName);
                                }

                                adapter = new ArrayAdapter(view_profile.this,android.R.layout.simple_dropdown_item_1line,collectionArray);
                                userListGames.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        }catch(JSONException e){
                            System.out.print(e);
                            Toast.makeText(view_profile.this,"Error "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(view_profile.this,"Error "+error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",searchID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
