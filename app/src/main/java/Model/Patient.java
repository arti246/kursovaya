package Model;

public class Patient {
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
}
