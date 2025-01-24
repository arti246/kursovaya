package com.example.kursovaya.cards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursovaya.R;
import com.example.kursovaya.admin.AdminListPatientActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import Data.DatabaseHelper;
import Model.Patient;
import Model.User;
import Utils.EditTextUtils;

public class PatientCardActivity extends AppCompatActivity {

    private EditText editTextName, editTextSurname, editTextPatronymic, editTextDataBirth,
            editTextAddress, editTextPhone, editTextInsurance, editTextLogin, editTextPassword;
    private FloatingActionButton buttonBack;
    private Button buttonAdd, buttonDelete;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private int typeOperation; // 0 - добавление, 1 - изменение
    private DatabaseHelper db;
    private String originalLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews(); // Инициализация объектов
        db = new DatabaseHelper(this);

        loadPatientData();

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
                Intent intent = new Intent(PatientCardActivity.this,
                        AdminListPatientActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletePatient();
            }
        });

        db.close();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextPatronymic = findViewById(R.id.editTextPatronymic);
        editTextDataBirth = findViewById(R.id.editTextDataBirth);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextInsurance = findViewById(R.id.editTextInsurance);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        buttonBack = findViewById(R.id.buttonBack);
        buttonAdd = findViewById(R.id.buttonAdd);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton = findViewById(R.id.femaleRadioButton);
        buttonDelete = findViewById(R.id.buttonDelete);
    }

    private void loadPatientData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PATIENT_EXTRA")) {
            typeOperation = 1;
            Patient patient = (Patient) intent.getSerializableExtra("PATIENT_EXTRA");
            if (patient != null) {
                editTextName.setText(patient.getName());
                editTextSurname.setText(patient.getSurname());
                editTextPatronymic.setText(patient.getPatronymic());
                editTextDataBirth.setText(patient.getDataBirth());
                editTextAddress.setText(patient.getAddress());
                editTextPhone.setText(patient.getPhone());
                editTextInsurance.setText(patient.getInsurance());

                if (Objects.equals(patient.getGender(), "м")) {
                    genderRadioGroup.check(maleRadioButton.getId());
                } else {
                    genderRadioGroup.check(femaleRadioButton.getId());
                }

                User user = db.checkUserByUserID(patient.getIdUser());
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
        String selectedRadioButton = getSelectedGender();

        return !EditTextUtils.checkEditTextIsEmpty(editTextName).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextSurname).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextPatronymic).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextDataBirth).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextAddress).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextPhone).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextInsurance).isEmpty() &&
                !EditTextUtils.checkEditTextIsEmpty(editTextLogin).isEmpty() &&
                !EditTextUtils.checkEditTextPassword(editTextPassword).isEmpty() &&
                !selectedRadioButton.isEmpty();
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
            if (updatePatient(idUser)){
                Toast.makeText(this,"Данные обновлены!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatientCardActivity.this,
                        AdminListPatientActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,"Не удалось обновить данные", Toast.LENGTH_SHORT).show();
            }
        } else {
            if(addPatient(idUser)){
                Toast.makeText(this,"Данные добавлены!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatientCardActivity.this,
                        AdminListPatientActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,"Не удалось добавить данные", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean updatePatient(int idUser){
        User user = createUser(idUser);
        Patient patient = createPatient(db.checkIDPatientByIdUser(idUser));

        return db.updateUser(user) && db.updatePatient(patient);
    }

    private boolean addPatient(int idUser){
        User user = createUser(idUser);
        Patient patient = createPatient(db.checkIDPatientByIdUser(idUser));

        return db.addNewPatientAndUser(user, patient) != 1;
    }

    private void deletePatient() {
        int idUser = db.checkUserByLogin(originalLogin);
        User user = createUser(idUser);
        Patient patient = createPatient(db.checkIDPatientByIdUser(idUser));

        if (db.deletePatient(patient) && db.deleteUser(user)) {
            Toast.makeText(this,"Пациент удалён!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PatientCardActivity.this,
                    AdminListPatientActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private String getSelectedGender() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == maleRadioButton.getId()) {
            return "м";
        } else if (selectedId == femaleRadioButton.getId()) {
            return "ж";
        }
        return ""; // default
    }

    private User createUser(int idUser){
        User user = new User();
        user.setLogin(editTextLogin.getText().toString().trim());
        user.setPassword(editTextPassword.getText().toString().trim());
        if (idUser != -1) user.setId(idUser);
        return user;
    }

    private Patient createPatient(int idPatient){
        Patient patient = new Patient();
        patient.setIdPatient(idPatient);
        patient.setName(editTextName.getText().toString().trim());
        patient.setSurname(editTextSurname.getText().toString().trim());
        patient.setPatronymic(editTextPatronymic.getText().toString().trim());
        patient.setDataBirth(editTextDataBirth.getText().toString().trim());
        patient.setAddress(editTextAddress.getText().toString().trim());
        patient.setPhone(editTextPhone.getText().toString().trim());
        patient.setInsurance(editTextInsurance.getText().toString().trim());
        patient.setGender(getSelectedGender());
        return patient;
    }

    private void showDeletePatient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удаление пациента");
        builder.setMessage("Вы уверены, что хотите удалить выбранного пациента?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePatient();
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