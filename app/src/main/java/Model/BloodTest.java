package Model;

public class BloodTest extends MedicalProcedure {
    private String testResults;

    public BloodTest(int patientId, int doctorId, String description, String testResults) {
        super(patientId, doctorId, description);
        this.testResults = testResults;
    }

    @Override
    public String getProcedureType() { return "Blood Test"; }

    @Override
    public double getCost() { return 50.0; } //Пример стоимости

    @Override
    public String toString() {
        return super.toString() + "\nTest Results: " + testResults;
    }
}
