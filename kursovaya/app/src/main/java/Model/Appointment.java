package Model;

public class Appointment {
    private int idAppointment;
    private int idPatient;
    private int idDoctor;
    private int idDiagnosis;
    private String date;
    private String time;
    private String status;

    public static final String TABLE_NAME = "appointment";
    public static final String KEY_ID_APPOINTMENT = "idAppointment";
    public static final String KEY_ID_PATIENT = "idPatient";
    public static final String KEY_ID_DOCTOR = "idDoctor";
    public static final String KEY_ID_DIAGNOSIS = "idDiagnosis";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_STATUS = "status";

    public Appointment() {
    }

    public Appointment(int idAppointment, int idPatient, int idDoctor, int idDiagnosis, String date,
                       String time, String status) {
        this.idAppointment = idAppointment;
        this.idPatient = idPatient;
        this.idDoctor = idDoctor;
        this.idDiagnosis = idDiagnosis;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Appointment(int idPatient, int idDoctor, int idDiagnosis, String date, String time,
                       String status) {
        this.idPatient = idPatient;
        this.idDoctor = idDoctor;
        this.idDiagnosis = idDiagnosis;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public int getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(int idAppointment) {
        this.idAppointment = idAppointment;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public int getIdDiagnosis() {
        return idDiagnosis;
    }

    public void setIdDiagnosis(int idDiagnosis) {
        this.idDiagnosis = idDiagnosis;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}