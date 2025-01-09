package Model;

public class Prescription {
    private int idPrescription;
    private int idDoctor;
    private String namePrescription;
    private String date;
    private String time_start;
    private String time_end;

    public static final String TABLE_NAME = "prescription";
    public static final String KEY_ID_PRESCRIPTION = "idPrescription";
    public static final String KEY_ID_DOCTOR = "idDoctor";
    public static final String KEY_NAME_PRESCRIPTION = "namePrescription";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME_START = "time_start";
    public static final String KEY_TIME_END = "time_end";

    public Prescription() {
    }

    public Prescription(int idPrescription, int idDoctor, String namePrescription, String date,
                        String time_start, String time_end) {
        this.idPrescription = idPrescription;
        this.idDoctor = idDoctor;
        this.namePrescription = namePrescription;
        this.date = date;
        this.time_start = time_start;
        this.time_end = time_end;
    }

    public Prescription(int idDoctor, String namePrescription, String date, String time_start,
                        String time_end) {
        this.idDoctor = idDoctor;
        this.namePrescription = namePrescription;
        this.date = date;
        this.time_start = time_start;
        this.time_end = time_end;
    }

    public int getIdPrescription() {
        return idPrescription;
    }

    public void setIdPrescription(int idPrescription) {
        this.idPrescription = idPrescription;
    }

    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getNamePrescription() {
        return namePrescription;
    }

    public void setNamePrescription(String namePrescription) {
        this.namePrescription = namePrescription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }
}
