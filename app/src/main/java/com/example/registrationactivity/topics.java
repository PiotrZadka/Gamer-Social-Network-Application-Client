package com.example.registrationactivity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class topics extends AppCompatActivity {

    Button sendButton;
    EditText messageText;
    ListView chatBoard;
    TextView title;
    ArrayList chatList = new ArrayList();
    ArrayAdapter adapter;

    private DatabaseReference dbr;

    String userName, userEmail, topicName, userMessageKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        this.getSupportActionBar().hide();

        sendButton = findViewById(R.id.buttonSend);
        messageText = findViewById(R.id.messageText);
        chatBoard = findViewById(R.id.chatListView);
        title = findViewById(R.id.titleEdit);

        userName = getIntent().getExtras().get("user_name").toString();
        userEmail = getIntent().getExtras().get("user_email").toString();
        topicName = getIntent().getExtras().get("topic_name").toString();

        title.setText(topicName);

        dbr = FirebaseDatabase.getInstance().getReference().child(topicName);

        adapter = new ArrayAdapter(topics.this, android.R.layout.simple_dropdown_item_1line, chatList);
        chatBoard.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<>();
                userMessageKey = dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2 = dbr.child(userMessageKey);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("msg", messageText.getText().toString());
                map2.put("name", userName);
                dbr2.updateChildren(map2);
            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateConversation(DataSnapshot dataSnapshot){
        String msg, user, conversation;
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            msg = ((DataSnapshot)i.next()).getValue().toString();
            user = ((DataSnapshot)i.next()).getValue().toString();
            conversation = user + ": "+msg;
            adapter.insert(conversation, 0);
            adapter.notifyDataSetChanged();
        }
    }
}
