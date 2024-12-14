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

public class AdminActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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

        //Добавление нового пациента
/*        User userAdd = new User("artur2002", "12345123");
        int idUserAdd = dbHelper.addUser(userAdd);
        Patient patientAdd = new Patient(idUserAdd, "Пётр", "Петров",
                "Петрович", "02.02.1993", "м",
                "ул. Гоголя, д. 34", "88005553536", "9876543210147852");
        dbHelper.addPatient(patientAdd);
        patientList = dbHelper.getAllPatient();
        displayPatientList(patientList);*/

        //Изменение данных пациента

/*        Patient updatePatient = dbHelper.getPatient(2);
        updatePatient.setName("Григорий");
        updatePatient.setDataBirth("02.02.2003");
        dbHelper.updatePatient(updatePatient);
        patientList = dbHelper.getAllPatient();
        displayPatientList(patientList);*/

        //Удаление пациента
/*        dbHelper.deletePatient(dbHelper.getPatient(2));
        patientList = dbHelper.getAllPatient();
        displayPatientList(patientList);*/
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