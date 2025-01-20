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

import com.example.kursovaya.R;
import com.example.kursovaya.cards.DoctorCardActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHelper;
import Model.Doctor;
import Utils.AdaptersAdmin.DoctorAdminAdapter;

public class AdminListDoctorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorAdminAdapter adapter;
    private List<Doctor> doctorList;
    private EditText searchEditText;
    private FloatingActionButton addDoctorButton, buttonBack;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_list_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews(); // Инициализация объектов
        db = new DatabaseHelper(this);

        doctorList = db.getAllDoctors();

        adapter = new DoctorAdminAdapter(doctorList, new DoctorAdminAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Doctor doctor) {
                openEditDoctorActivity(doctor);
            }
        }, db);
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
                startActivity(new Intent(AdminListDoctorActivity.this,
                        AdminMainActivity.class));
                finish();
            }
        });

        /*Кнопка добавления нового пациента*/
        addDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminListDoctorActivity.this,
                        DoctorCardActivity.class);
                startActivity(intent);
            }
        });

        db.close();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        addDoctorButton = findViewById(R.id.addPatientButton);
        buttonBack = findViewById(R.id.buttonBack);
    }

    /*Открытие карточки выбранного доктора*/
    private void openEditDoctorActivity(Doctor doctor) {
        Intent intent = new Intent(AdminListDoctorActivity.this,
                DoctorCardActivity.class);
        intent.putExtra("DOCTOR_EXTRA", doctor);
        startActivity(intent);
    }

    /*Фильтр списка*/
    private void filterList(String text) {
        List<Doctor> filteredList = new ArrayList<>();
        for (Doctor doctor : doctorList) {
            if (doctor.getSurname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(doctor);
            }
        }
        adapter.filterList(filteredList);
    }
}