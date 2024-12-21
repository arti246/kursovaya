package Model;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Patient implements Treatable, Serializable, Cloneable{
    private int idPatient;
    private int idUser;
    private String name;
    private String surname;
    private String patronymic;
    private String dataBirth;
    private String gender;
    private String address;
    private String phone;
    private String insurance;

    protected String medicalHistory;
    private List<MedicalProcedure> procedures;

    public static final String TABLE_NAME = "patients";
    public static final String KEY_ID_PATIENT = "idPatient";
    public static final String KEY_ID_USER = "idUser";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "suTABLE_NAMErname";
    public static final String KEY_PATRONYMIC = "patronymic";
    public static final String KEY_DATA_BIRTH = "year_birth";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_INSURANCE = "insurance";

    public Patient() {
    }

    public Patient(int idPatient, int idUser, String name, String surname, String patronymic,
                   String dataBirth, String gender, String address, String phone, String insurance) {
        this.idPatient = idPatient;
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dataBirth = dataBirth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.insurance = insurance;
        this.procedures = new ArrayList<>();
    }

    public Patient(int idUser, String name, String surname, String patronymic, String dataBirth,
                   String gender, String address, String phone, String insurance) {
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dataBirth = dataBirth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.insurance = insurance;
    }

    public Patient(int idPatient, int idUser, String name, String surname, String patronymic,
                   String dataBirth, String gender, String address, String phone, String insurance,
                   String medicalHistory, List<MedicalProcedure> procedures) {
        this.idPatient = idPatient;
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dataBirth = dataBirth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.insurance = insurance;
        this.medicalHistory = medicalHistory;
        this.procedures = procedures;
    }

    public Patient(int idUser, String name, String surname, String patronymic, String dataBirth,
                   String gender, String address, String phone, String insurance,
                   String medicalHistory) {
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dataBirth = dataBirth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.insurance = insurance;
        this.medicalHistory = medicalHistory;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getDataBirth() {
        return dataBirth;
    }

    public void setDataBirth(String dataBirth) {
        this.dataBirth = dataBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    /**/
    public void addToMedicalHistory(String entry){
        this.medicalHistory += entry + "\n";
    }

    public void printSummary() {
        Log.d("Patient", "Patient Summary:");
        Log.d("Patient", "ID: " + idPatient);
        Log.d("Patient", "Name: " + name + " " + surname + " " + patronymic);
        Log.d("Patient", "Birth Date: " + dataBirth);
        Log.d("Patient", "Gender: " + gender);
        Log.d("Patient", "Address: " + address);
        Log.d("Patient", "Phone: " + phone);
        Log.d("Patient", "Insurance: " + insurance);
    }

    public void printSummaryWithoutBase() {
        Log.d("Patient", "ID: " + idPatient);
        Log.d("Patient", "Name: " + name + " " + surname + " " + patronymic);
    }

    @Override
    public void receiveTreatment(MedicalProcedure procedure) {
        this.procedures.add(procedure);
        Log.d("Patient", "Patient received treatment: " + procedure.getDescription());
    }

    @Override
    public Patient clone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Patient copy = (Patient) ois.readObject();
            ois.close();
            return copy;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
