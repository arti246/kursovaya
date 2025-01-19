package com.example.kursovaya.admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.EditPatientActivity;
import com.example.kursovaya.R;
import com.example.kursovaya.autorization.LoginActivity;
import com.example.kursovaya.autorization.RecoverPasswordActivity;
import com.example.kursovaya.cards.PatientCardActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHelper;
import Model.Patient;
import Utils.PatientAdapter;

public class AdminListPatientActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<Patient> patientList;
    private EditText searchEditText;
    private FloatingActionButton addPatientButton, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_list_patient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        addPatientButton = findViewById(R.id.addPatientButton);
        buttonBack = findViewById(R.id.buttonBack);

        DatabaseHelper db = new DatabaseHelper(this);

        patientList = db.getAllPatient();
        adapter = new PatientAdapter(patientList, new PatientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient patient) {
                openEditPatientActivity(patient);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /*Фильтр для поиска*/
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList(s.toString());
            }
        });

        /*Кнопка назад*/
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminListPatientActivity.this,
                        AdminMainActivity.class));
                finish();
            }
        });

        /*Кнопка добавления нового пациента*/
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminListPatientActivity.this,
                        PatientCardActivity.class);
                startActivity(intent);
            }
        });

        db.close();
    }

    /*Открытие карточки выбранного пациента*/
    private void openEditPatientActivity(Patient patient) {
        Intent intent = new Intent(AdminListPatientActivity.this,
                PatientCardActivity.class);
        intent.putExtra("PATIENT_EXTRA", patient);
        startActivity(intent);
    }

    /*Фильтр списка*/
    private void filterList(String text) {
        List<Patient> filteredList = new ArrayList<>();
        for (Patient patient : patientList) {
            if (patient.getSurname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(patient);
            }
        }
        adapter.filterList(filteredList);
    }
}