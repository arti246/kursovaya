package com.example.kursovaya.autorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursovaya.admin.AdminMainActivity;
import com.example.kursovaya.DoctorMainActivity;
import com.example.kursovaya.patient.PatientMainActivity;
import com.example.kursovaya.R;

import Data.DatabaseHelper;
import Model.User;
import Utils.EditTextUtils;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_FILE = "Account";
    private static final String PREF_LOGIN = "Login";
    private static final String PREF_PASSWORD = "Password";

    EditText editTextLogin, editTextPassword;
    Button buttonLogin, buttonRegister;
    CheckBox checkBoxRemember;
    TextView textViewForgotPassword;
    DatabaseHelper myDb;
    User userLogin;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        checkBoxRemember = findViewById(R.id.checkBoxRemember);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        myDb = new DatabaseHelper(this);

        settings = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        getDataUser(editTextLogin, editTextPassword);

        if (!editTextLogin.getText().toString().isEmpty() &&
                !editTextPassword.getText().toString().isEmpty()) {
            checkBoxRemember.setChecked(true);
        }

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = EditTextUtils.checkEditTextIsEmpty(editTextLogin);
                String password = EditTextUtils.checkEditTextPassword(editTextPassword);

                if (!login.isEmpty() && !password.isEmpty()) {
                    userLogin = new User(login, password);
                    int idUser = myDb.checkUserByLogin(login);

                    if (idUser == -1) {
                        if (checkBoxRemember.isChecked()) {
                            saveDataUser(editTextLogin, editTextPassword);
                        } else {
                            removeDataUser();
                        }

                        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                        intent.putExtra("newUserLogin", userLogin.getLogin());
                        intent.putExtra("newUserPassword", userLogin.getPassword());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Пользватель с таким логином уже существует!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = EditTextUtils.checkEditTextIsEmpty(editTextLogin);
                String password = EditTextUtils.checkEditTextPassword(editTextPassword);

                if (!login.isEmpty() && !password.isEmpty()) {
                    userLogin = new User(login, password);
                    userLogin = myDb.checkUserForAuthorization(userLogin);

                    if (userLogin != null) {
                        Toast.makeText(LoginActivity.this, "Успешная авторизация!", Toast.LENGTH_SHORT).show();

                        if (checkBoxRemember.isChecked()) {
                            saveDataUser(editTextLogin, editTextPassword);
                        } else {
                            removeDataUser();
                        }

                        int IdUserType = userLogin.getIdUserType();
                        Intent intent;

                        if (IdUserType == 1) {
                            intent = new Intent(LoginActivity.this,
                                    AdminMainActivity.class);
                            intent.putExtra("ID_EXTRA", userLogin.getId());
                            startActivity(intent);
                            finish();
                        } else if (IdUserType == 2) {
                            intent = new Intent(LoginActivity.this,
                                    DoctorMainActivity.class);
                            intent.putExtra("ID_EXTRA", userLogin.getId());
                            startActivity(intent);
                            finish();
                        } else if (IdUserType == 3) {
                            intent = new Intent(LoginActivity.this,
                                    PatientMainActivity.class);
                            intent.putExtra("ID_EXTRA", userLogin.getId());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Проблема с авторизацией! Обратитесь в поддержку!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Введённый логин и пароль не существуют!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,
                        RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void saveDataUser(EditText editTextLogin, EditText editTextPassword) {
        // получаем введенное имя
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        // сохраняем его в настройках
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(PREF_LOGIN, login);
        prefEditor.putString(PREF_PASSWORD, password);
        prefEditor.apply();
    }

    public void getDataUser(EditText editTextLogin, EditText editTextPassword) {
        editTextLogin.setText(settings.getString(PREF_LOGIN, null));
        editTextPassword.setText(settings.getString(PREF_PASSWORD, null));
    }

    public void removeDataUser() {
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.remove(PREF_LOGIN);
        prefEditor.remove(PREF_PASSWORD);
    }

    public void loadActivity(int id, Intent intent) {
        intent.putExtra("idUser", id);
        startActivity(intent);
        finish();
    }
}