package com.example.registrationactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class main_content extends AppCompatActivity {

    private Button logout;
    private TextView usernameWelcome;
    SessionManager sessionManager;
    ImageView gameList;
    ImageView userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        logout = findViewById(R.id.logout_main);
        usernameWelcome = findViewById(R.id.userWelcomeLabel);
        gameList = findViewById(R.id.gameListImage);
        userProfile = findViewById(R.id.userProfileImage);

        HashMap<String, String> user = sessionManager.getUserDetails();
        String sessionName = user.get(sessionManager.NAME);
        String sessionEmail = user.get(sessionManager.EMAIL);

        usernameWelcome.setText(sessionName);

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


    }
}
