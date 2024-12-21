package Model;

import java.io.Serializable;

public class Diagnosis implements Serializable{
    private int idDiagnosis;
    private String descriptionSymptoms;
    private String dateStartSymptoms;
    private String nameDisease;

    public static final String TABLE_NAME = "diagnosis";
    public static final String KEY_ID_DIAGNOSIS = "idDiagnosis";
    public static final String KEY_DESC_SYMP = "descriptionSymptoms";
    public static final String KEY_DATE_START_SYMP = "dateStartSymptoms";
    public static final String KEY_NAME_DISEASE = "nameDisease";

    public Diagnosis() {
    }

    public Diagnosis(int idDiagnosis, String descriptionSymptoms, String dateStartSymptoms,
                     String nameDisease) {
        this.idDiagnosis = idDiagnosis;
        this.descriptionSymptoms = descriptionSymptoms;
        this.dateStartSymptoms = dateStartSymptoms;
        this.nameDisease = nameDisease;
    }

    public Diagnosis(String descriptionSymptoms, String dateStartSymptoms, String nameDisease) {
        this.descriptionSymptoms = descriptionSymptoms;
        this.dateStartSymptoms = dateStartSymptoms;
        this.nameDisease = nameDisease;
    }

    public int getIdDiagnosis() {
        return idDiagnosis;
    }

    public void setIdDiagnosis(int idDiagnosis) {
        this.idDiagnosis = idDiagnosis;
    }

    public String getDescriptionSymptoms() {
        return descriptionSymptoms;
    }

    public void setDescriptionSymptoms(String descriptionSymptoms) {
        this.descriptionSymptoms = descriptionSymptoms;
    }

    public String getDateStartSymptoms() {
        return dateStartSymptoms;
    }

    public void setDateStartSymptoms(String dateStartSymptoms) {
        this.dateStartSymptoms = dateStartSymptoms;
    }

    public String getNameDisease() {
        return nameDisease;
    }

    public void setNameDisease(String nameDisease) {
        this.nameDisease = nameDisease;
    }
}
