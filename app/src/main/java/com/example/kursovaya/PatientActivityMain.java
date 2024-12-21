package com.example.kursovaya;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHelper;
import Model.BloodTest;
import Model.ChronicPatient;
import Model.Diagnosis;
import Model.MedicalProcedure;
import Model.Patient;
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

        List<Diagnosis> diagnoses = new ArrayList<>();
        diagnoses.add(new Diagnosis("Кашель", "21.12.2024",
                "..."));

        ChronicPatient patient1 = new ChronicPatient(1, 1, "John", "Doe",
                "N/A", "1990-01-15", "Male", "123 Main St",
                "555-1212", "12345", "2024-03-15", diagnoses);
        patient1.addChronicDiagnosis(new Diagnosis(
                "Шишка на голове, довольно большая",
                "21.12.2024", "..."));

        patient1.printSummary();

        patient1.printSummaryWithoutBase();

        ChronicPatient clonedPatient = patient1.clone();
        Log.d("Main", "Original Patient: " + patient1.getName() + " " + patient1.getSurname());
        Log.d("Main", "Cloned Patient: " + clonedPatient.getName() + " " + clonedPatient.getSurname());
        patient1.setName("Jane"); //Изменяем оригинал
        Log.d("Main", "Original Patient after change: " + patient1.getName() + " " +
                patient1.getSurname());
        Log.d("Main", "Cloned Patient after change: " + clonedPatient.getName() + " " +
                clonedPatient.getSurname());

        BloodTest bloodTest = new BloodTest(1, 1, "Complete Blood Count",
                "WBC: 7.5 x 10^9/L");
        Log.d("Main", "\nBlood Test:\n" + bloodTest);

        patient1.receiveTreatment(bloodTest);

        dbHelper.close();
    }
}