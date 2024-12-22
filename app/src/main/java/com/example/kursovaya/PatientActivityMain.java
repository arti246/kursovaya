package com.example.kursovaya;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Data.DatabaseHelper;
import Model.BloodTest;
import Model.ChronicPatient;
import Model.Diagnosis;
import Model.MedicalProcedure;
import Model.Patient;
import Model.Prescription;
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

        List<Patient> patients = new ArrayList<>();
        patients = dbHelper.getAllPatient();
        List<Diagnosis> chronicDiagnoses = new ArrayList<>();

        chronicDiagnoses.add(new Diagnosis("Сильно болит котелок",
                "22.12.2024", "..."));

        patients.add(new ChronicPatient(101, 101, "Николай", "Зюзин",
                "Евгеньевич", "12.14.1999", "м",
                "г.Барнаул, ул.Летняя, д. 54, кв. 777", "87777777777",
                "1111222244445555", "", chronicDiagnoses));

        // Сортировка по фамилии
        Collections.sort(patients, (p1, p2) -> p1.getSurname().compareTo(p2.getSurname()));

        // Вывод отсортированного списка
        Log.d("Patient", "Отсортированный список пациентов:");
        for (Patient p : patients) {
            p.printSummaryWithoutBase();
        }

        // Поиск по фамилии
        String surnameToFind = "Иванов";
        Patient foundPatient = null;
        for (Patient p : patients) {
            if (p.getSurname().equals(surnameToFind)) {
                foundPatient = p;
                break;
            }
        }
        if(foundPatient != null){
            Log.d("Patient", "\nНайденный пациент: ");
            foundPatient.printSummaryWithoutBase();
        } else {
            Log.d("Patient", "\nПациент не найден");
        }

        dbHelper.close();
    }
}