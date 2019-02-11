package com.example.registrationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class login_user extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button login_button;
    private TextView notRegisterLink;
    private static String URL_LOGIN = "http://13.59.14.52/login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        loginEmail = findViewById(R.id.emailLoginBox);
        loginPassword = findViewById(R.id.passwordLoginBox);
        login_button = findViewById(R.id.loginButton);
        notRegisterLink = findViewById(R.id.notUserRegister);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lEmail = loginEmail.getText().toString().trim();
                String lPassword = loginPassword.getText().toString().trim();

                if(lEmail.isEmpty() || !lPassword.isEmpty()){
                    Login(lEmail,lPassword);
                }
                else{
                    loginEmail.setError("Email can't be empty");
                    loginPassword.setError("Password can't be empty");
                }
            }
        });
    }

    private void Login(final String email, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if(success.equals("1")){
                                for(int i = 0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();

                                    sessionManager.createSession(name,email);

                                    Intent intent = new Intent(login_user.this, main_content.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", name);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            System.out.print(e);
                            Toast.makeText(login_user.this, "Error: "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(login_user.this, "Error: "+error.toString(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            protected Map<String, String> getParams(){
                Map<String, String>params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void returnRegister(View v){
        Intent registerView = new Intent(login_user.this, register_user.class);
        startActivity(registerView);
    }
}
