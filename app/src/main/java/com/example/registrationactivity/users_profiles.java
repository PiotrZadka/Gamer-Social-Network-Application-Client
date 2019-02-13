package com.example.registrationactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class users_profiles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profiles);
        this.getSupportActionBar().hide();
    }
}
