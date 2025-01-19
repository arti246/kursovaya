package com.example.kursovaya.autorization;

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

import com.example.kursovaya.PatientMainActivity;
import com.example.kursovaya.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import Data.DatabaseHelper;
import Model.Patient;
import Model.User;
import Utils.EditTextUtils;

public class RegistrationActivity extends AppCompatActivity {

    Button buttonRegister;
    FloatingActionButton buttonBack;
    EditText editTextName, editTextSurname, editTextPatronymic, editTextDataBirth, editTextAddress,
            editTextPhone, editTextInsurance;
    RadioGroup genderRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextPatronymic = findViewById(R.id.editTextPatronymic);
        editTextDataBirth = findViewById(R.id.editTextDataBirth);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextInsurance = findViewById(R.id.editTextInsurance);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        buttonBack = findViewById(R.id.buttonBack);
        buttonRegister = findViewById(R.id.buttonRegister);

        Intent intentReg = getIntent();
        String newUserLogin = intentReg.getStringExtra("newUserLogin");
        String newUserPassword = intentReg.getStringExtra("newUserPassword");

        DatabaseHelper db = new DatabaseHelper(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = EditTextUtils.checkEditTextIsEmpty(editTextName);
                String surname = EditTextUtils.checkEditTextIsEmpty(editTextSurname);
                String patronymic = EditTextUtils.checkEditTextIsEmpty(editTextPatronymic);
                String dataBirth = EditTextUtils.checkEditTextIsEmpty(editTextDataBirth);
                String address = EditTextUtils.checkEditTextIsEmpty(editTextAddress);
                String phone = EditTextUtils.checkEditTextIsEmpty(editTextPhone);
                String insurance = EditTextUtils.checkEditTextIsEmpty(editTextInsurance);
                String selectedGender;

                int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);

                if (!name.isEmpty() || !surname.isEmpty() || !patronymic.isEmpty() ||
                        !dataBirth.isEmpty() || !address.isEmpty() || !phone.isEmpty() ||
                        !insurance.isEmpty()) {
                    if (selectedRadioButton != null) {
                        selectedGender = Objects.equals(selectedRadioButton.getText().toString(),
                                "Мужской") ? "м" : "ж";

                        /*добавление пользователя и пациента*/
                        int idPatient = db.addNewPatientAndUser(new User(newUserLogin, newUserPassword),
                                new Patient(name, surname, patronymic, dataBirth, selectedGender,
                                        address, phone, insurance));

                        if (idPatient != -1) {
                            Toast.makeText(RegistrationActivity.this,
                                    "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                            Intent intentPatien = new Intent(RegistrationActivity.this, PatientMainActivity.class);
                            intentPatien.putExtra("idNewPatient", idPatient);
                            startActivity(intentPatien);
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this,
                                    "Ошибка с регистрацией!", Toast.LENGTH_SHORT).show();
                        }

                    } else Toast.makeText(RegistrationActivity.this, "Выберите пол!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        db.close();
    }
}