package com.example.kursovaya.cards;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursovaya.patient.PatientHistoryActivity;
import com.example.kursovaya.patient.PatientMainActivity;
import com.example.kursovaya.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Data.DatabaseHelper;
import Model.Appointment;
import Model.Doctor;
import Model.Patient;

public class AppointmentCardActivity extends AppCompatActivity {

    private Button buttonAdd;
    private EditText editTextOfficeNumber, dateEditText, editTextInsurance, editTextFIO;
    private ImageButton dateButton;
    private Spinner spinnerDoctor, spinnerSpec, spinnerTime;
    private FloatingActionButton buttonBack;
    private DatabaseHelper db;
    private SimpleDateFormat dateFormat;
    private int idUser;
    private List<String> specList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        loadUserData();
        db = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        spinnerSpec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spec = spinnerSpec.getSelectedItem().toString();
                updateSpinnerSpec(spec);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String doctor = spinnerDoctor.getSelectedItem().toString();
                updateSpinnerDoctor(doctor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentCardActivity.this,
                        PatientMainActivity.class);
                intent.putExtra("ID_EXTRA", idUser);
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataForAdd();
            }
        });

        db.close();
    }

    private void initViews() {
        buttonAdd = findViewById(R.id.buttonAdd);
        editTextOfficeNumber = findViewById(R.id.editTextOfficeNumber);
        dateEditText = findViewById(R.id.dateEditText);
        editTextInsurance = findViewById(R.id.editTextInsurance);
        editTextFIO = findViewById(R.id.editTextFIO);
        dateButton = findViewById(R.id.dateButton);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        spinnerSpec = findViewById(R.id.spinnerSpec);
        buttonBack = findViewById(R.id.buttonBack);
        spinnerTime = findViewById(R.id.spinnerTime);
    }

    private void loadUserData() {
        Intent intent = getIntent();
        db = new DatabaseHelper(this);
        if (intent != null && intent.hasExtra("ID_EXTRA")) {
            idUser = intent.getIntExtra("ID_EXTRA", -1);
            Patient patient = (Patient) db.getPatientByIdPatient(idUser);

            if (patient != null) {
                editTextFIO.setText(patient.getSurname() + " " + patient.getName() + " "
                        + patient.getPatronymic());
                editTextInsurance.setText(patient.getInsurance());
            }

            specList = db.getAllSpecName();

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, specList);
            spinnerSpec.setAdapter(adapter);
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, month1, dayOfMonth);
                    String formattedDate = dateFormat.format(selectedCalendar.getTime());
                    dateEditText.setText(formattedDate);
                    loadAvailableTimes(formattedDate);
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void loadAvailableTimes(String date) {
        String fioDoctor = spinnerDoctor.getSelectedItem().toString();
        Doctor doctor = (Doctor) db.getDoctorByFIO(SplitFIO(fioDoctor));

        List<String> availableSlots = db.getAvailableSlots(doctor.getIdDoctor(), date);

        if (availableSlots.size() == 0) {
            availableSlots.add("Свободного времени нет!");
            //Вывод сообщения для выбора другой даты
        }

        String[] timeSlots = new String[availableSlots.size()];
        for (int i = 0; i < availableSlots.size(); i++) {
            timeSlots[i] = availableSlots.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeSlots);
        spinnerTime.setAdapter(adapter);

    }

    private void updateSpinnerSpec(String spec) {
        List<Doctor> allDoctors = db.getAllDoctors();
        List<String> doctorsFIO = new ArrayList<>();
        int idSpec = db.getIdSpecBySpecialization(spec);

        for (int i = 0; i < allDoctors.size(); i++) {
            if (idSpec == allDoctors.get(i).getIdSpecialization()) {
                doctorsFIO.add(allDoctors.get(i).getSurname() + " " + allDoctors.get(i).getName() +
                        " " + allDoctors.get(i).getPatronymic());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorsFIO);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctor.setAdapter(adapter);
    }

    private void updateSpinnerDoctor(String doctor) {
        String[] fio = SplitFIO(doctor);
        if (fio != null) {
            Doctor getdoctor = (Doctor) db.getDoctorByFIO(fio);
            if (getdoctor != null)
                editTextOfficeNumber.setText(getdoctor.getOffice_number());
        }
    }

    private String[] SplitFIO(String FIO) {
        String[] parts = FIO.split(" ");
        if (parts.length == 3) {
            return parts;
        }
        return null;
    }

    private void checkDataForAdd() {
        String dataGet = dateEditText.getText().toString();
        if (dataGet.isEmpty()) {
            Toast.makeText(AppointmentCardActivity.this,
                    "Выберите дату приёма!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Patient patient = (Patient) db.getPatientByIdPatient(idUser);
        String fioDoctor = spinnerDoctor.getSelectedItem().toString();
        Doctor doctor = (Doctor) db.getDoctorByFIO(SplitFIO(fioDoctor));
        String timeGet = spinnerTime.getSelectedItem().toString();

        Appointment appointment = new Appointment(patient.getIdPatient(), doctor.getIdDoctor(),
                -1, dataGet, timeGet, "Записан");

        if(db.addAppointment(appointment) != -1) {
            Toast.makeText(AppointmentCardActivity.this,
                    "Вы записаны на " + dataGet + " в " + timeGet + "!",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AppointmentCardActivity.this,
                    PatientMainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(AppointmentCardActivity.this,
                    "Возникли проблемы с записью!",
                    Toast.LENGTH_SHORT).show();
        }

    }
}