package com.example.kursovaya;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Data.DatabaseHelper;
import Model.User;
import Utils.Result;

public class PatientActivityMain extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        User[] users = new User[]{new User(1, "user1", "pass1", 3),
                new User(2, "user2", "pass2", 3)};

        // Получение пользователя по логину
        Result<User> userResult = dbHelper.getUserByLogin("user1", users);
        User user = userResult.getData();
        if (userResult.isSuccess()) {
            Log.d("CheckLogin", "User found: login - " + user.getLogin());
        } else {
            Log.d("CheckLogin", "Error: " + userResult.getError());
        }



        dbHelper.close();
    }
}