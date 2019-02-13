package com.example.registrationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register_user extends AppCompatActivity {

    private EditText username, password, c_password, email;
    private Button regButton;
    private static String URL_REG = "http://13.59.14.52/register.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().hide();

        username = findViewById(R.id.emailLoginBox);
        email = findViewById(R.id.passwordLoginBox);
        password = findViewById(R.id.passwordBox);
        c_password = findViewById(R.id.passwordConfirmBox);
        regButton = findViewById(R.id.loginButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("Registering");
                Register();
            }
        });

    }

    private void Register(){
        regButton.setVisibility(View.GONE);

        final String name = this.username.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String c_password = this.c_password.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, URL_REG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(register_user.this,"Register Success!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(register_user.this, login_user.class);
                                startActivity(intent);
                            }

                        }catch (JSONException e){
                            System.out.println(e);
                            Toast.makeText(register_user.this,"Register Error!", Toast.LENGTH_LONG).show();
                            regButton.setVisibility(View.VISIBLE);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            protected Map<String, String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void returnLogin(View v){
        Intent intent = new Intent(register_user.this, login_user.class);
        startActivity(intent);
    }
}
