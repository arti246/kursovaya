package com.example.kursovaya.cards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursovaya.R;
import com.example.kursovaya.admin.AdminListDoctorActivity;
import com.example.kursovaya.admin.AdminListPatientActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import Data.DatabaseHelper;
import Model.Doctor;
import Model.Patient;
import Model.SpecializationDoctors;
import Model.User;
import Utils.EditTextUtils;

public class DoctorCardActivity extends AppCompatActivity {

    private EditText editTextName, editTextSurname, editTextPatronymic,
            editTextOfficeNumber, editTextLogin, editTextPassword;
    private FloatingActionButton buttonBack;
    private Button buttonAdd, buttonDelete;
    private Spinner spinnerSpec;
    private int typeOperation; // 0 - добавление, 1 - изменение
    private DatabaseHelper db;
    private String originalLogin;
    private String spinnerValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews(); // Инициализация объектов
        db = new DatabaseHelper(this);

        loadDoctorData();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    saveChanges();
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorCardActivity.this,
                        AdminListDoctorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDoctor();
            }
        });

        spinnerSpec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerValue = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        db.close();
    }

    public void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextPatronymic = findViewById(R.id.editTextPatronymic);
        spinnerSpec = findViewById(R.id.spinnerSpec);
        editTextOfficeNumber = findViewById(R.id.editTextOfficeNumber);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonBack = findViewById(R.id.buttonBack);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);
    }

    public void loadDoctorData() {
        Intent intent = getIntent();

        List<String> spec = db.getAllSpecName();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                spec
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpec.setAdapter(adapter);

        if (intent != null && intent.hasExtra("DOCTOR_EXTRA")) {
            typeOperation = 1;
            Doctor doctor = (Doctor) intent.getSerializableExtra("DOCTOR_EXTRA");
            if (doctor != null) {
                editTextName.setText(doctor.getName());
                editTextSurname.setText(doctor.getSurname());
                editTextPatronymic.setText(doctor.getPatronymic());
                editTextOfficeNumber.setText(doctor.getOffice_number());

                int itemPosition = spec.indexOf(db.getSpecializationById(doctor.getIdSpecialization()));
                if (itemPosition != -1) {
                    spinnerSpec.setSelection(itemPosition);
                }

                User user = db.checkUserByUserID(doctor.getIdUser());
                if (user != null) {
                    editTextLogin.setText(user.getLogin());
                    originalLogin = user.getLogin();
                    editTextPassword.setText(user.getPassword());
                }

                typeOperation = 1;
                buttonAdd.setText("СОХРАНИТЬ");
                buttonDelete.setVisibility(View.VISIBLE);
            }
        } else {
            typeOperation = 0;
            originalLogin = "";
            buttonAdd.setText("ДОБАВИТЬ");
            buttonDelete.setVisibility(View.GONE);
        }
    }

    private boolean isFormValid() {
        return !EditTextUtils.checkEditTextIsEmpty(editTextName).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextSurname).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextPatronymic).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextOfficeNumber).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextLogin).isEmpty() &&
                !EditTextUtils.checkEditTextPassword(editTextPassword).isEmpty();
    }

    private void saveChanges(){
        String currentLogin = editTextLogin.getText().toString();
        boolean loginChanged = !currentLogin.equals(originalLogin);
        int checkId = db.checkUserByLogin(currentLogin);
        int idUser = db.checkUserByLogin(originalLogin);

        if(loginChanged){
            // Проверяем логин в базе данных, только если он изменился
            if(checkId != -1){
                editTextLogin.setError("Логин уже занят");
                return;
            }
        }

        if(typeOperation == 1){
            if (updateDoctor(idUser)){
                Toast.makeText(this,"Данные обновлены!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DoctorCardActivity.this,
                        AdminListDoctorActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,"Не удалось обновить данные", Toast.LENGTH_SHORT).show();
            }
        } else {
            if(addDoctor(idUser)){
                Toast.makeText(this,"Данные добавлены!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DoctorCardActivity.this,
                        AdminListDoctorActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,"Не удалось добавить данные", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean updateDoctor(int idUser){
        User user = createUser(idUser);
        Doctor doctor = createDoctor(db.checkIDDoctorByIdUser(idUser));

        return db.updateUser(user) && db.updateDoctor(doctor);
    }

    private boolean addDoctor(int idUser){
        User user = createUser(idUser);
        Doctor doctor = createDoctor(db.checkIDDoctorByIdUser(idUser));

        return db.addNewDoctorAndUser(user, doctor) != 1;
    }

    private void deleteDoctor() {
        int idUser = db.checkUserByLogin(originalLogin);
        User user = createUser(idUser);
        Doctor doctor = createDoctor(db.checkIDDoctorByIdUser(idUser));

        if (db.deleteDoctor(doctor) && db.deleteUser(user)) {
            Toast.makeText(this,"Доктор удалён!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DoctorCardActivity.this,
                    AdminListDoctorActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private User createUser(int idUser){
        User user = new User();
        user.setLogin(editTextLogin.getText().toString().trim());
        user.setPassword(editTextPassword.getText().toString().trim());
        user.setIdUserType(2);
        if (idUser != -1) user.setId(idUser);
        return user;
    }

    private Doctor createDoctor(int idDoctor){
        Doctor doctor = new Doctor();
        doctor.setIdDoctor(idDoctor);
        doctor.setName(editTextName.getText().toString().trim());
        doctor.setSurname(editTextSurname.getText().toString().trim());
        doctor.setPatronymic(editTextPatronymic.getText().toString().trim());
        doctor.setOffice_number(editTextOfficeNumber.getText().toString().trim());

        String spec = spinnerValue;
        int idSpec = db.getIdSpecBySpecialization(spec);
        doctor.setIdSpecialization(idSpec);

        return doctor;
    }

    private void showDeleteDoctor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление доктора");
        builder.setMessage("Вы уверены, что хотите удалить выбранного доктора?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDoctor();
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}