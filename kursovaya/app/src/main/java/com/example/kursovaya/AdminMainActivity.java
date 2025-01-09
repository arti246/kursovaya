package com.example.kursovaya;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import Data.DatabaseHelper;
import Model.Patient;

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

        Intent intent = getIntent();
        int iserId = intent.getIntExtra("idUser", -1);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.openDatabase();

        List<Patient> patientList;

        //Вывод всех записей из таблицы Patient
        patientList = dbHelper.getAllPatient();
        displayPatientList(patientList);

        db.close();
    }

    public void displayPatientList(List<Patient> patients) {
        StringBuilder sb = new StringBuilder();
        for (Patient patient : patients) {
            sb.append("ID Пациента: ").append(patient.getIdPatient()).append("\n");
            sb.append("Имя: ").append(patient.getName()).append("\n");
            sb.append("Фамилия: ").append(patient.getSurname()).append("\n");
            sb.append("Год рождения: ").append(patient.getDataBirth()).append("\n");
            sb.append("\n"); // Отступ между пациентами
        }
        TextView textView = findViewById(R.id.textView);
        textView.setText(sb.toString());
    }
}