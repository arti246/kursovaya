package com.example.kursovaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import Data.DatabaseHelper;
import Model.User;
import Utils.EditTextUtils;

public class RecoverPasswordActivity extends AppCompatActivity {

    EditText editTextDate, editTextLogin, editTextPassword;
    FloatingActionButton buttonBack;
    Button buttonEnter;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recover_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonEnter = findViewById(R.id.buttonEnter);
        buttonBack = findViewById(R.id.buttonBack);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);

        dbHelper = new DatabaseHelper(this);

        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = EditTextUtils.checkEditTextIsEmpty(editTextLogin);
                String date = EditTextUtils.checkEditTextIsEmpty(editTextDate);
                String password = EditTextUtils.checkEditTextPassword(editTextPassword);

                if(!login.isEmpty() && !date.isEmpty() && !password.isEmpty()) {
                    User user = new User();
                    user.setLogin(login);
                    int idUser = dbHelper.checkUserByLogin(user);
                    String dateUser = dbHelper.checkPatientByIdUser(idUser);

                    if (idUser == -1) {
                        Toast.makeText(RecoverPasswordActivity.this, "Пользователя с " +
                                "таким логином не существует!", Toast.LENGTH_SHORT).show();
                    } else if (!dateUser.equals(date)) {
                        Toast.makeText(RecoverPasswordActivity.this, "Дата рождения" +
                                " не соответствует!", Toast.LENGTH_SHORT).show();
                    } else {
                        user = new User(idUser, login, password);
                        if (dbHelper.updateUser(user) == idUser) {
                            Toast.makeText(RecoverPasswordActivity.this, "Пароль " +
                                    "успешно изменён!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
  }