package Model;

import java.io.Serializable;

public class Doctor implements Serializable {
    private int idDoctor;
    private int idUser;
    private String name;
    private String surname;
    private String patronymic;
    private int idSpecialization;
    private String office_number;

    public static final String TABLE_NAME = "doctors";
    public static final String KEY_ID_DOCTOR = "idDoctor";
    public static final String KEY_ID_USER = "idUser";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_PATRONYMIC = "patronymic";
    public static final String KEY_ID_SPEC = "idSpecialization";
    public static final String KEY_OFFICE_NUM = "office_number";

    public Doctor() {
    }

    public Doctor(int idDoctor, int idUser, String name, String surname, String patronymic,
                  int idSpecialization, String office_number) {
        this.idDoctor = idDoctor;
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.idSpecialization = idSpecialization;
        this.office_number = office_number;
    }

    public Doctor(String office_number, int idSpecialization, String patronymic, String surname,
                  String name, int idUser) {
        this.office_number = office_number;
        this.idSpecialization = idSpecialization;
        this.patronymic = patronymic;
        this.surname = surname;
        this.name = name;
        this.idUser = idUser;
    }

    public Doctor(int idDoctor, String name, String surname, String patronymic,
                  int idSpecialization, String office_number) {
        this.idDoctor = idDoctor;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.idSpecialization = idSpecialization;
        this.office_number = office_number;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
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

    public int getIdSpecialization() {
        return idSpecialization;
    }

    public void setIdSpecialization(int idSpecialization) {
        this.idSpecialization = idSpecialization;
    }

    public String getOffice_number() {
        return office_number;
    }

    public void setOffice_number(String office_number) {
        this.office_number = office_number;
    }
}