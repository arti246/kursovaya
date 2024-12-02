package com.example.kursovaya;

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
import Model.User;

public class MainActivity extends AppCompatActivity {

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

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.openDatabase();

        int idNewPatient;

        //Вывод всех записей из таблицы Patient
//        List<Patient> patientList = dbHelper.getAllPatient();
//        displayPatientList(patientList);

        //Добавление нового пациента
/*        User userAdd = new User("artur2002", "12345123");
        int idUserAdd = dbHelper.addUser(userAdd);
        Patient patientAdd = new Patient(9, "Пётр", "Петров",
                "Петрович", "02.02.1993", "м",
                "ул. Гоголя, д. 34", "88005553536", "9876543210147852");
        idNewPatient = dbHelper.addPatient(patientAdd);
        List<Patient> patientList = dbHelper.getAllPatient();
        displayPatientList(patientList);*/

        //Изменение данных пациента
/*        Patient updatePatient = dbHelper.getPatient((int) idNewUser);
        updatePatient.setName("Григорий");
        updatePatient.setDataBirth("02.02.2003");
        dbHelper.updatePatient(updatePatient);*/

        //Удаление пациента
//        dbHelper.deletePatient(updatePatient);
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