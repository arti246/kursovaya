package com.example.kursovaya.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.R;
import com.example.kursovaya.admin.AdminListPatientActivity;
import com.example.kursovaya.admin.AdminMainActivity;
import com.example.kursovaya.cards.PatientCardActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Data.DatabaseHelper;
import Model.Appointment;
import Model.Patient;
import Utils.AdaptersAdmin.PatientAdapter;
import Utils.PatientsHistoryAdapter;

public class PatientHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientsHistoryAdapter adapter;
    private FloatingActionButton buttonBack;
    private List<Appointment> appointmentList;
    private int idUser;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        db = new DatabaseHelper(this);
        loadUserData();

        int idPatient = db.checkIDPatientByIdUser(idUser);
        appointmentList = db.getAllAppointmentByIdPatietn(idPatient);
        adapter = new PatientsHistoryAdapter(appointmentList, new PatientsHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Appointment appointment) {
                openEditPatientHistoryCardActivity(appointment);
            }
        }, db);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientHistoryActivity.this,
                        PatientMainActivity .class);
                intent.putExtra("ID_EXTRA", idUser);
                startActivity(intent);
            }
        });

        db.close();
    }

    public void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        buttonBack = findViewById(R.id.buttonBack);
    }

    public void openEditPatientHistoryCardActivity(Appointment appointment) {

    }

    private void loadUserData() {
        Intent intent = getIntent();
        db = new DatabaseHelper(this);
        if (intent != null && intent.hasExtra("ID_EXTRA")) {
            idUser = intent.getIntExtra("ID_EXTRA", -1);
        }
    }
}