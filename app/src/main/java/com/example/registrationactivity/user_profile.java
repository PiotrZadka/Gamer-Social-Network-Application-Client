package com.example.registrationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class user_profile extends AppCompatActivity {

    EditText nameEdit, emailEdit;
    Button saveButton, editButton, exitButton;
    SessionManager sessionManager;
    ListView collectionList;
    ArrayAdapter adapter;
    ArrayList collectionArray = new ArrayList();

    private static String URL = "http://13.59.14.52/editUserDetails.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        this.getSupportActionBar().hide();

        nameEdit = findViewById(R.id.nameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        saveButton = findViewById(R.id.buttonSave);
        editButton = findViewById(R.id.buttonEdit);
        exitButton = findViewById(R.id.exitButton);
        collectionList = findViewById(R.id.userListGames);


        // Populate editText with user data
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(sessionManager.NAME);
        String email = user.get(sessionManager.EMAIL);
        final String id = user.get(sessionManager.ID);
        nameEdit.setText(name);
        emailEdit.setText(email);

        retrieveCollection(id);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEdit.setEnabled(true);
                emailEdit.setEnabled(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEdit.setEnabled(false);
                nameEdit.setEnabled(false);
                emailEdit.getText();

                final String getName = nameEdit.getText().toString();
                final String getEmail = emailEdit.getText().toString();
                saveChanges(getName,getEmail,id);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_profile.this, main_content.class);
                startActivity(intent);
            }
        });


    }

    private void saveChanges(String name, String email, String id){
        final String sName = name;
        final String sEmail = email;
        final String sId = id;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(user_profile.this,"Saved", Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(sName,sEmail,sId);
                            }
                        }catch(JSONException e){
                            System.out.print(e);
                            Toast.makeText(user_profile.this,"Error "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(user_profile.this,"Error "+error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("name",sName);
                params.put("email",sEmail);
                params.put("id",sId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void retrieveCollection(String id){

        final String user_id = id;
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

                                adapter = new ArrayAdapter(user_profile.this,android.R.layout.simple_dropdown_item_1line,collectionArray);
                                collectionList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }
                        }catch(JSONException e){
                            System.out.print(e);
                            Toast.makeText(user_profile.this,"Error "+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(user_profile.this,"Error "+error, Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("id",user_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
