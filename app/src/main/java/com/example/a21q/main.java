package com.example.a21q;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class main extends AppCompatActivity {
    private final String[] USER_NAMES = {"Anna", "Bob", "Clint", "Doug", "Emily", "Fred",
            "Gina", "Helen"};
    private final String[] USER_OCCUPATION = {"Cook", "Teacher", "Barista", "Wine taster",
            "Musician", "Student", "Pilot", "Retailer"};
    private final boolean[] USER_PRIVATE = {false, false, true, true, false, false, false, true};
    private final String appEmail = "21qtest@test.com";
    private final String appPassword = "21qtest";
    protected static final String USERS = "USERS";
    private FirebaseAuth authLink;
    private FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        authLink = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        checkSignin();
        populateDatabase();

        final Button button = findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getUsers();
            }
        });
    }

    //Creates a new activity displaying the user info
    private void displayUsers(ArrayList<String> users) {
        String[] usersToDisplay = null;
        if(users != null) {
            Object[] temp = users.toArray();
            usersToDisplay = new String[users.size()];
            int i = 0;
            for (Object t : temp) {
                usersToDisplay[i] = (String) t;
                i++;
            }
        }
        Intent intent = new Intent(this, displayUsers.class);
        intent.putExtra(USERS, usersToDisplay);
        startActivity(intent);
    }

    //Adds data to the database
    private void populateDatabase() {
        database.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            //Checking if collection and its data already exist so it doesn't keep adding the users
                            if(task.getResult().size() == 0) {
                                for (int i = 0; i < USER_NAMES.length; i++) {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", USER_NAMES[i]);
                                    user.put("occupation", USER_OCCUPATION[i]);
                                    user.put("private", USER_PRIVATE[i]);

                                    database.collection("users")
                                            .add(user);

                                }
                            }
                        }
                    }
                });

    }

    //Getting user data from database and passing that data to the displayUsers method
    private void getUsers() {
        database.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<String> users = new ArrayList<>();
                            int i = 0;
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getBoolean("private") == false) {
                                    users.add(document.getString("name") + "," + document.getString("occupation"));
                                }
                                i++;
                            }
                            displayUsers(users);

                        } else {
                            ArrayList<String> users = null;
                            displayUsers(users);
                        }
                    }
                });
    }

    //Checks if user is singed in, calls sign in if user is not signed in
    public void checkSignin() {
        FirebaseUser user = authLink.getCurrentUser();
        if(user == null) {
            signIn(appEmail, appPassword);
        }
    }

    //Google auth sign in with email and password
    private void signIn(String email, String password) {
        authLink.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = authLink.getCurrentUser();
                        } else {
                            Toast.makeText(main.this, "Signin Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
