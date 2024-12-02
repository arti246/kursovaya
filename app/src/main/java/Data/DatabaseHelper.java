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
import java.util.ArrayList;
import java.util.List;

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
            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
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

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User.KEY_LOGIN, user.getLogin());
        contentValues.put(User.KEY_PASSWORD, user.getPassword());

        return db.update(User.TABLE_NAME, contentValues, User.KEY_ID_USER + "=?",
                new String[]{String.valueOf(user.getId())});
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TABLE_NAME, User.KEY_ID_USER + "=?", new String[]{String.valueOf(user.getId())});
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
        contentValues.put("gender", String.valueOf(patient.getGender()));
        contentValues.put(Patient.KEY_ADDRESS, patient.getAddress());
        contentValues.put(Patient.KEY_PHONE, patient.getPhone());
        contentValues.put(Patient.KEY_INSURANCE, patient.getInsurance());

        return (int) db.insert(Patient.TABLE_NAME, null, contentValues);
    }

    public Patient getPatient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Patient.TABLE_NAME, new String[] {Patient.KEY_ID_USER,
                        Patient.KEY_NAME, Patient.KEY_SURNAME, Patient.KEY_PATRONYMIC,
                        Patient.KEY_DATA_BIRTH, Patient.KEY_GENDER, Patient.KEY_ADDRESS,
                        Patient.KEY_PHONE, Patient.KEY_INSURANCE},
                Patient.KEY_ID_PATIENT + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Patient getPatient = new Patient(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5),
                cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9));

        return getPatient;
    }

    public int updatePatient(Patient patient) {
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

        return db.update(Patient.TABLE_NAME, contentValues, Patient.KEY_ID_PATIENT + "=?",
                new String[]{String.valueOf(patient.getIdPatient())});
    }

    public void deletePatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Patient.TABLE_NAME, Patient.KEY_ID_PATIENT + "=?",
                new String[]{String.valueOf(patient.getIdPatient())});
        db.close();
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

    public List<SpecializationDoctors> getAllSpec() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<SpecializationDoctors> specList = new ArrayList<>();

        String query = "SELECT * FROM " + SpecializationDoctors.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SpecializationDoctors spec = new SpecializationDoctors();
                spec.setIdSpec(Integer.parseInt(cursor.getString(0)));
                spec.setNameSpec(cursor.getString(1));

                specList.add(spec);
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
                doctor.setName(cursor.getString(2));
                doctor.setSurname(cursor.getString(3));
                doctor.setPatronymic(cursor.getString(5));
                doctor.setIdSpecialization(Integer.parseInt(cursor.getString(6)));
                doctor.setOffice_number(cursor.getString(7));

                doctorsList.add(doctor);
            } while (cursor.moveToNext());
        }
        return doctorsList;
    }

    public int addDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
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
                cursor.getString(2), cursor.getString(3),
                cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                cursor.getString(6));

        return getDoctor;
    }

    public int updateDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Doctor.KEY_NAME, doctor.getName());
        contentValues.put(Doctor.KEY_SURNAME, doctor.getSurname());
        contentValues.put(Doctor.KEY_PATRONYMIC, doctor.getPatronymic());
        contentValues.put(Doctor.KEY_ID_SPEC, doctor.getIdSpecialization());
        contentValues.put(Doctor.KEY_OFFICE_NUM, doctor.getOffice_number());

        return db.update(Doctor.TABLE_NAME, contentValues, Doctor.KEY_ID_DOCTOR + "=?",
                new String[]{String.valueOf(doctor.getIdDoctor())});
    }

    public void deleteDoctor(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Doctor.TABLE_NAME, Doctor.KEY_ID_DOCTOR + "=?",
                new String[]{String.valueOf(doctor.getIdDoctor())});
        db.close();
    }
}