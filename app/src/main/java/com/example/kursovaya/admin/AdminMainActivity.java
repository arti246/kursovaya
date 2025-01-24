package com.example.kursovaya.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursovaya.R;

import Data.DatabaseHelper;
import Utils.DialogUtils;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DatabaseHelper db = new DatabaseHelper(this);

        Button patientsButton = findViewById(R.id.patientsButton);
        Button doctorsButton = findViewById(R.id.doctorsButton);
        Button scheduleButton = findViewById(R.id.scheduleButton);
        Button appointmentsButton = findViewById(R.id.appointmentsButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button exitButton = findViewById(R.id.exitButton);

        patientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this,
                        AdminListPatientActivity.class);
                startActivity(intent);
            }
        });

        doctorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this,
                        AdminListDoctorActivity.class);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.showSignOutDialog(AdminMainActivity.this);
            }
        });

        db.close();
    }
}