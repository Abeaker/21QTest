package com.example.a21q;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.view.Gravity;

public class displayUsers extends AppCompatActivity {
    private String[] users;
    private final int NAME_INDEX = 0;
    private final int OCCUPATION_INDEX = 1;
    private final String NO_PUBLIC_USERS = "No public users found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);

        Intent intent = getIntent();
        users = intent.getStringArrayExtra(main.USERS);

        displayUsers(users);
    }

    //Displaying user data to the screen.
    //The layout is in a scroll view so there is no limit to the amount users.
    private void displayUsers(String[] users) {
        LinearLayout linearLayout = findViewById(R.id.display);
        if (users != null) {
            for (String user : users) {
                String[] userInfo = user.split(",");
                String userName = userInfo[NAME_INDEX];
                String userOccupation = userInfo[OCCUPATION_INDEX];

                TextView textView = new TextView(this);
                LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.LEFT;
                layoutParams.setMargins(10, 10, 10, 10);
                textView.setLayoutParams(layoutParams);
                textView.setPadding(10, 10, 10, 10);
                textView.setTextSize(20);
                textView.setBackgroundColor(0xffd3d3d3);
                textView.setText("Name: " + userName + ", Occupation: " + userOccupation);
                linearLayout.addView(textView);
            }
        } else {
            TextView textView = new TextView(this);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.LEFT;
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10, 10, 10, 10);
            textView.setTextSize(20);
            textView.setBackgroundColor(0xffd3d3d3);
            textView.setText(NO_PUBLIC_USERS);
            linearLayout.addView(textView);
        }
    }
}
