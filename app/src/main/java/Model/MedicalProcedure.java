package Model;

import java.util.Date;

public abstract class MedicalProcedure {
    protected int patientId;
    protected int doctorId;
    protected String description;
    protected Date procedureDate;

    public MedicalProcedure(int patientId, int doctorId, String description) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.description = description;
        this.procedureDate = new Date(); // Текущая дата
    }

    public abstract String getProcedureType(); // Абстрактный метод - тип процедуры
    public abstract double getCost(); // Абстрактный метод - стоимость процедуры

    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getDescription() { return description; }
    public Date getProcedureDate() { return procedureDate; }

    @Override
    public String toString() {
        return "Procedure Type: " + getProcedureType() +
                "\nPatient ID: " + patientId +
                "\nDoctor ID: " + doctorId +
                "\nDescription: " + description +
                "\nDate: " + procedureDate +
                "\nCost: " + getCost();
    }
}