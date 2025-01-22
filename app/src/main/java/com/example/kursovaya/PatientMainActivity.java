package com.example.kursovaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursovaya.cards.AppointmentCardActivity;

import Data.DatabaseHelper;
import Model.Patient;
import Utils.DialogUtils;

public class PatientMainActivity extends AppCompatActivity {

    private TextView patientTitleTextView;
    private Button makeAppointmentButton, historyButton, settingsButton, exitButton;
    private DatabaseHelper db;
    private int idUser;

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

        initView();
        db = new DatabaseHelper(this);
        loadPatientData();

        makeAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientMainActivity.this,
                        AppointmentCardActivity.class);
                intent.putExtra("ID_EXTRA", idUser);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils dialogUtils = new DialogUtils();
                dialogUtils.showSignOutDialog(PatientMainActivity.this);
            }
        });
    }

    public void initView() {
        patientTitleTextView = findViewById(R.id.patientTitleTextView);
        makeAppointmentButton = findViewById(R.id.makeAppointmentButton);
        historyButton = findViewById(R.id.historyButton);
        settingsButton = findViewById(R.id.settingsButton);
        exitButton = findViewById(R.id.exitButton);
    }

    public void loadPatientData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID_EXTRA")) {
            idUser = intent.getIntExtra("ID_EXTRA", -1);
            Patient patient = (Patient) db.getPatientByIdPatient(idUser);

            if (patient != null) {
                patientTitleTextView.setText(patient.getSurname() + " " + patient.getName() + " "
                + patient.getPatronymic());
            }
        }
    }
}