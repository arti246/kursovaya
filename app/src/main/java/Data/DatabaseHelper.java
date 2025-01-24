package Data;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Model.Appointment;
import Model.Doctor;
import Model.Patient;
import Model.SpecializationDoctors;
import Model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "polyclinic.db";
    private static Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void copyDatabase() throws IOException {
        InputStream myInput = context.getAssets().open(DATABASE_NAME);
        String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDatabase() {
        try {
            String dbPath = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
            File dbFile = new File(dbPath);
            if (!dbFile.exists()){
                copyDatabase();
            }
            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
            return db;
        } catch (IOException | SQLiteException e) {
            Log.e(TAG, "Error opening database: " + e.getMessage());
            return null;
        }
    }

    public void closeDatabase(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }

    public int addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(User.KEY_LOGIN, user.getLogin());
        contentValues.put(User.KEY_PASSWORD, user.getPassword());
        contentValues.put(User.KEY_ID_USER_TYPE, 3);

        return (int) db.insert(User.TABLE_NAME, null, contentValues);
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(User.TABLE_NAME, new String[] {User.KEY_ID_USER, User.KEY_LOGIN,
                        User.KEY_PASSWORD}, User.KEY_ID_USER + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        User user = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return user;
    }

    public List<User> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<User> userList = new ArrayList<>();

        String query = "SELECT * FROM " + User.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setLogin(cursor.getString(1));
                user.setPassword(cursor.getString(2));

                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    public User checkUserForAuthorization(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = User.KEY_LOGIN + " = ?";
        Cursor cursor = db.query(User.TABLE_NAME, new String[] {User.KEY_ID_USER, User.KEY_PASSWORD,
                User.KEY_ID_USER_TYPE, User.KEY_LOGIN}, selection, new String[] {user.getLogin()},
                null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                String checkPas = cursor.getString(1);
                if (checkPas.equals(user.getPassword())) {
                    user.setId(Integer.parseInt(cursor.getString(0)));
                    user.setIdUserType(Integer.parseInt(cursor.getString(2)));
                    return user;
                }
            } else return null;
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public int checkUserByLogin(String login) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = User.KEY_LOGIN + " = ?";
        Cursor cursor = db.query(User.TABLE_NAME, new String[] {User.KEY_ID_USER}, selection,
                new String[] {login}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst())
            return Integer.parseInt(cursor.getString(0));
        else return -1;
    }

    public User checkUserByUserID(int id) {
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = User.KEY_ID_USER + " = ?";
        Cursor cursor = db.query(User.TABLE_NAME, new String[] {User.KEY_LOGIN, User.KEY_PASSWORD},
                selection, new String[] {Integer.toString(id)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            user.setLogin(cursor.getString(0));
            user.setPassword(cursor.getString(1));
        }
        cursor.close();
        return user;
    }

    public int addNewPatientAndUser(User user, Patient patient) {
        int idUser;
        if ((idUser = checkUserByLogin(user.getLogin())) == -1) {
            idUser = addUser(user);
        }

        patient.setIdUser(idUser);
        int idPatient = addPatient(patient);
        if (idPatient != -1) {
            return idPatient;
        }

        return -1;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User.KEY_LOGIN, user.getLogin());
        contentValues.put(User.KEY_PASSWORD, user.getPassword());

        int updatedRows = db.update(User.TABLE_NAME, contentValues, User.KEY_ID_USER + "=?",
                new String[]{String.valueOf(user.getId())});
        return updatedRows > 0;
    }

    public boolean deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        int countRows = db.delete(User.TABLE_NAME, User.KEY_ID_USER + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return countRows > 0;
    }

    public List<Patient> getAllPatient() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Patient> patientList = new ArrayList<>();

        String query = "SELECT * FROM " + Patient.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Patient patient = new Patient();
                patient.setIdPatient(Integer.parseInt(cursor.getString(0)));
                patient.setIdUser(Integer.parseInt(cursor.getString(1)));
                patient.setName(cursor.getString(2));
                patient.setSurname(cursor.getString(3));
                patient.setPatronymic(cursor.getString(4));
                patient.setDataBirth(cursor.getString(5));
                patient.setGender(cursor.getString(6));
                patient.setAddress(cursor.getString(7));
                patient.setPhone(cursor.getString(8));
                patient.setInsurance(cursor.getString(9));

                patientList.add(patient);
            } while (cursor.moveToNext());
        }
        return patientList;
    }

    public int addPatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Patient.KEY_ID_USER, patient.getIdUser());
        contentValues.put(Patient.KEY_NAME, patient.getName());
        contentValues.put(Patient.KEY_SURNAME, patient.getSurname());
        contentValues.put(Patient.KEY_PATRONYMIC, patient.getPatronymic());
        contentValues.put(Patient.KEY_DATA_BIRTH, patient.getDataBirth());
        contentValues.put(Patient.KEY_GENDER, String.valueOf(patient.getGender()));
        contentValues.put(Patient.KEY_ADDRESS, patient.getAddress());
        contentValues.put(Patient.KEY_PHONE, patient.getPhone());
        contentValues.put(Patient.KEY_INSURANCE, patient.getInsurance());

        return (int) db.insert(Patient.TABLE_NAME, null, contentValues);
    }

    public int addNewDoctorAndUser(User user, Doctor doctor) {
        int idUser;
        if ((idUser = checkUserByLogin(user.getLogin())) == -1) {
            idUser = addUser(user);
        }

        doctor.setIdUser(idUser);
        int idDoctor = addDoctor(doctor);
        if (idDoctor != -1) {
            return idDoctor;
        }

        return -1;
    }

    public Patient getPatientByIdPatient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Patient.TABLE_NAME, new String[]{
                        Patient.KEY_ID_PATIENT,
                        Patient.KEY_ID_USER,
                        Patient.KEY_NAME,
                        Patient.KEY_SURNAME,
                        Patient.KEY_PATRONYMIC,
                        Patient.KEY_DATA_BIRTH,
                        Patient.KEY_GENDER,
                        Patient.KEY_ADDRESS,
                        Patient.KEY_PHONE,
                        Patient.KEY_INSURANCE
                },
                Patient.KEY_ID_USER + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        Patient patient = null;
        if (cursor != null && cursor.moveToFirst()) {
            patient = new Patient(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Patient.KEY_ID_PATIENT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Patient.KEY_ID_USER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Patient.KEY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Patient.KEY_SURNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Patient.KEY_PATRONYMIC)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Patient.KEY_DATA_BIRTH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Patient.KEY_GENDER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Patient.KEY_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Patient.KEY_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Patient.KEY_INSURANCE))
            );
        }
        cursor.close();
        return patient;
    }

    public boolean updatePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Patient.KEY_NAME, patient.getName());
        contentValues.put(Patient.KEY_SURNAME, patient.getSurname());
        contentValues.put(Patient.KEY_PATRONYMIC, patient.getPatronymic());
        contentValues.put(Patient.KEY_DATA_BIRTH, patient.getDataBirth());
        contentValues.put(Patient.KEY_GENDER, patient.getGender());
        contentValues.put(Patient.KEY_ADDRESS, patient.getAddress());
        contentValues.put(Patient.KEY_PHONE, patient.getPhone());
        contentValues.put(Patient.KEY_INSURANCE, patient.getInsurance());

        int updatedRows = db.update(Patient.TABLE_NAME, contentValues, Patient.KEY_ID_PATIENT + "=?",
                new String[]{String.valueOf(patient.getIdPatient())});
        return updatedRows > 0;
    }

    public boolean deletePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        int countRows = db.delete(Patient.TABLE_NAME, Patient.KEY_ID_PATIENT + "=?",
                new String[]{String.valueOf(patient.getIdPatient())});
        db.close();

        return countRows > 0;
    }

    public String checkPatientByIdUserForDate(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = Patient.KEY_ID_USER + " = ?";
        Cursor cursor = db.query(Patient.TABLE_NAME, new String[] {Patient.KEY_DATA_BIRTH}, selection,
                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String date = cursor.getString(0);
            return date;
        }
        else return "";
    }

    public int checkIDPatientByIdUser(int idUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = Patient.KEY_ID_USER + " = ?";
        Cursor cursor = db.query(Patient.TABLE_NAME, new String[] {Patient.KEY_ID_PATIENT},
                selection, new String[] {Integer.toString(idUser)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return Integer.parseInt(cursor.getString(0));
        }

        cursor.close();
        return -1;
    }

    public int addSpec(SpecializationDoctors spec) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SpecializationDoctors.KEY_NAME_SPEC, spec.getNameSpec());

        return (int) db.insert(SpecializationDoctors.TABLE_NAME, null, contentValues);
    }

    public int updateSpec(SpecializationDoctors spec) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SpecializationDoctors.KEY_NAME_SPEC, spec.getNameSpec());

        return db.update(SpecializationDoctors.TABLE_NAME, contentValues,
                SpecializationDoctors.KEY_ID_SPEC + "=?",
                new String[]{String.valueOf(spec.getIdSpec())});
    }

    public void deleteSpec(SpecializationDoctors spec) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SpecializationDoctors.TABLE_NAME, SpecializationDoctors.KEY_ID_SPEC + "=?",
                new String[]{String.valueOf(spec.getIdSpec())});
        db.close();
    }

    public List<String> getAllSpecName() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> specList = new ArrayList<>();

        String query = "SELECT * FROM " + SpecializationDoctors.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                 specList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return specList;
    }

    public List<Doctor> getAllDoctors() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Doctor> doctorsList = new ArrayList<>();

        String query = "SELECT * FROM " + Doctor.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Doctor doctor = new Doctor();
                doctor.setIdDoctor(Integer.parseInt(cursor.getString(0)));
                doctor.setIdUser(Integer.parseInt(cursor.getString(1)));
                doctor.setName(cursor.getString(2));
                doctor.setSurname(cursor.getString(3));
                doctor.setPatronymic(cursor.getString(4));
                doctor.setIdSpecialization(Integer.parseInt(cursor.getString(5)));
                doctor.setOffice_number(cursor.getString(6));

                doctorsList.add(doctor);
            } while (cursor.moveToNext());
        }
        return doctorsList;
    }

    public int addDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Doctor.KEY_ID_USER, doctor.getIdUser());
        contentValues.put(Doctor.KEY_NAME, doctor.getName());
        contentValues.put(Doctor.KEY_SURNAME, doctor.getSurname());
        contentValues.put(Doctor.KEY_PATRONYMIC, doctor.getPatronymic());
        contentValues.put(Doctor.KEY_ID_SPEC, doctor.getIdSpecialization());
        contentValues.put(Doctor.KEY_OFFICE_NUM, doctor.getOffice_number());

        return  (int) db.insert(Doctor.TABLE_NAME, null, contentValues);
    }

    public Doctor getDoctor(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Doctor.TABLE_NAME, new String[] {Doctor.KEY_ID_DOCTOR,
                        Doctor.KEY_NAME, Doctor.KEY_SURNAME, Doctor.KEY_PATRONYMIC,
                        Doctor.KEY_ID_SPEC, Doctor.KEY_OFFICE_NUM},
                Doctor.KEY_ID_DOCTOR + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Doctor getDoctor = new Doctor(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3), Integer.parseInt(cursor.getString(4)),
                cursor.getString(5));

        return getDoctor;
    }

    public boolean updateDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Doctor.KEY_NAME, doctor.getName());
        contentValues.put(Doctor.KEY_SURNAME, doctor.getSurname());
        contentValues.put(Doctor.KEY_PATRONYMIC, doctor.getPatronymic());
        contentValues.put(Doctor.KEY_ID_SPEC, doctor.getIdSpecialization());
        contentValues.put(Doctor.KEY_OFFICE_NUM, doctor.getOffice_number());

        int countRows = db.update(Doctor.TABLE_NAME, contentValues, Doctor.KEY_ID_DOCTOR + "=?",
                new String[]{String.valueOf(doctor.getIdDoctor())});

        return countRows > 0;
    }

    public boolean deleteDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        int countRows = db.delete(Doctor.TABLE_NAME, Doctor.KEY_ID_DOCTOR + "=?",
                new String[]{String.valueOf(doctor.getIdDoctor())});
        db.close();

        return countRows > 0;
    }

    public int checkIDDoctorByIdUser(int idUser) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = Doctor.KEY_ID_USER + " = ?";
        Cursor cursor = db.query(Doctor.TABLE_NAME, new String[] {Doctor.KEY_ID_DOCTOR},
                selection, new String[] {Integer.toString(idUser)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return Integer.parseInt(cursor.getString(0));
        }

        cursor.close();
        return -1;
    }

    public Doctor getDoctorByFIO(String[] fio) {
        SQLiteDatabase db = this.getReadableDatabase();
        Doctor doctor = null;
        Cursor cursor = db.query(
                Doctor.TABLE_NAME,
                null,
                Doctor.KEY_NAME + " = ? AND " + Doctor.KEY_SURNAME + " = ? AND " +
                        Doctor.KEY_PATRONYMIC + " = ?", new String[]{fio[1], fio[0], fio[2]},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Doctor getDoctor = new Doctor();
            getDoctor.setIdDoctor(cursor.getInt(0));
            getDoctor.setIdUser(cursor.getInt(1));
            getDoctor.setName(cursor.getString(2));
            getDoctor.setSurname(cursor.getString(3));
            getDoctor.setPatronymic(cursor.getString(4));
            getDoctor.setIdSpecialization(cursor.getInt(5));
            getDoctor.setOffice_number(cursor.getString(6));
            return getDoctor;
        }

        cursor.close();
        return null;
    }

    public String getSpecializationById(int idSpec) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = SpecializationDoctors.KEY_ID_SPEC + " = ?";
        Cursor cursor = db.query(SpecializationDoctors.TABLE_NAME,
                new String[] {SpecializationDoctors.KEY_NAME_SPEC},
                selection, new String[] {Integer.toString(idSpec)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }

        cursor.close();
        return "";
    }

    public int getIdSpecBySpecialization(String spec) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = SpecializationDoctors.KEY_NAME_SPEC + " = ?";
        Cursor cursor = db.query(SpecializationDoctors.TABLE_NAME,
                new String[] {SpecializationDoctors.KEY_ID_SPEC},
                selection, new String[] {spec},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            return Integer.parseInt(cursor.getString(0));
        }

        cursor.close();
        return -1;
    }

    public int addAppointment(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Appointment.KEY_ID_PATIENT, appointment.getIdPatient());
        contentValues.put(Appointment.KEY_ID_DOCTOR, appointment.getIdDoctor());
        contentValues.put(Appointment.KEY_DATE, appointment.getDate());
        contentValues.put(Appointment.KEY_TIME, appointment.getTime());
        contentValues.put(Appointment.KEY_STATUS, appointment.getStatus());
        contentValues.put(Appointment.KEY_ID_DIAGNOSIS, appointment.getIdDiagnosis());

        return  (int) db.insert(Appointment.TABLE_NAME, null, contentValues);
    }

    public List<Appointment> getAllAppointmentByIdPatietn(int idPatient) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Appointment> appointmentList = new ArrayList<>();

        Cursor cursor = null;
        cursor = db.query(
                Appointment.TABLE_NAME,
                null,
                Appointment.KEY_ID_PATIENT + " = ?",
                new String[]{String.valueOf(idPatient)},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setIdAppointment(cursor.getInt(0));
                appointment.setIdPatient(cursor.getInt(1));
                appointment.setIdDoctor(cursor.getInt(2));
                appointment.setIdDiagnosis(cursor.getInt(3));
                appointment.setDate(cursor.getString(4));
                appointment.setTime(cursor.getString(5));
                appointment.setStatus(cursor.getString(6));

                appointmentList.add(appointment);
            } while (cursor.moveToNext());
        }
        return appointmentList;
    }

    private List<String[]> generateTimeSlots(String startTime, String endTime, int intervalMinutes) {
        List<String[]> timeSlots = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(startTime)); // Устанавливаем начальное время
            Date endTimeDate = sdf.parse(endTime); // Конечное время
            while (calendar.getTime().before(endTimeDate)){
                String start = sdf.format(calendar.getTime()); // Текущее время
                calendar.add(Calendar.MINUTE, intervalMinutes); // Добавляем интервал
                timeSlots.add(new String[]{start});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeSlots;
    }

    // Метод для получения доступного времени для врача в выбранную дату
    public List<String> getAvailableSlots(int doctorId, String date) {
        List<String> availableSlots = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        String startTimeDoctor = "09:00";
        String endTimeDoctor = "18:00";

        Appointment appointment = new Appointment();

        // Генерация всех возможных слотов времени на основе рабочего времени
        List<String[]> allTimeSlots = generateTimeSlots(startTimeDoctor, endTimeDoctor, 30);

        // Запрос для получения занятых слотов
        String selectQueryForAppointments = "SELECT " + appointment.KEY_TIME + " FROM "
                + appointment.TABLE_NAME +
                " WHERE " + appointment.KEY_ID_DOCTOR + " = ? " +
                " AND \"" + appointment.KEY_DATE + "\" >= ?";

        String[] selectionArgs = new String[]{String.valueOf(doctorId),  date };
        Cursor appointmentCursor = db.rawQuery(selectQueryForAppointments, selectionArgs);

        List<String[]> busyTimeSlots = new ArrayList<>();

        if(appointmentCursor.moveToFirst()){
            do {
                busyTimeSlots.add(new String[]{ appointmentCursor.getString(0) });
            } while (appointmentCursor.moveToNext());
        }
        appointmentCursor.close();

        // Сравнение всех слотов с занятыми и фильтрация доступных
        for(String[] timeSlot : allTimeSlots){
            boolean isBusy = false;
            for(String[] busyTime : busyTimeSlots){
                if(timeSlot[0].equals(busyTime[0])){
                    isBusy = true;
                    break;
                }
            }
            if(!isBusy){
               availableSlots.add(timeSlot[0]);
            }
        }
        db.close();
        return availableSlots;
    }
}