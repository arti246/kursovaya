package Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ChronicPatient extends Patient implements Serializable, Cloneable {
    private List<Diagnosis> chronicDiagnoses; // Список хронических диагнозов
    private List<Prescription> chronicPrescriptions; // Список хронических рецептов
    private String lastCheckupDate; // Дата последнего осмотра


    public ChronicPatient(int idPatient, int idUser, String name, String surname, String patronymic,
                          String dataBirth, String gender, String address, String phone, String insurance,
                          String lastCheckupDate, List<Diagnosis> chronicDiagnoses) {
        super(idPatient, idUser, name, surname, patronymic, dataBirth, gender, address, phone, insurance);
        this.lastCheckupDate = lastCheckupDate;
        this.chronicDiagnoses = chronicDiagnoses; //Теперь всё ок
    }


    public ChronicPatient(int idPatient, int idUser, String name, String surname, String patronymic,
                          String dataBirth, String gender, String address, String phone,
                          String insurance, List<Diagnosis> chronicDiagnoses,
                          List<Prescription> chronicPrescriptions, String lastCheckupDate) {
        super(idPatient, idUser, name, surname, patronymic, dataBirth, gender, address, phone, insurance);
        this.chronicDiagnoses = chronicDiagnoses;
        this.chronicPrescriptions = chronicPrescriptions;
        this.lastCheckupDate = lastCheckupDate;
    }

    public List<Diagnosis> getChronicDiagnoses() {
        return chronicDiagnoses;
    }

    public void setChronicDiagnoses(List<Diagnosis> chronicDiagnoses) {
        this.chronicDiagnoses = chronicDiagnoses;
    }

    public List<Prescription> getChronicPrescriptions() {
        return chronicPrescriptions;
    }

    public void setChronicPrescriptions(List<Prescription> chronicPrescriptions) {
        this.chronicPrescriptions = chronicPrescriptions;
    }

    public String getLastCheckupDate() {
        return lastCheckupDate;
    }

    public void setLastCheckupDate(String lastCheckupDate) {
        this.lastCheckupDate = lastCheckupDate;
    }

    public void addChronicDiagnosis(Diagnosis diagnosis) {
        if (chronicDiagnoses == null) chronicDiagnoses = new ArrayList<>();
        chronicDiagnoses.add(diagnosis);
    }

    public void addChronicPrescription(Prescription prescription) {
        if (chronicPrescriptions == null) chronicPrescriptions = new ArrayList<>();
        chronicPrescriptions.add(prescription);
    }

    public void addMedicalHistoryEntry(String entry){
        this.medicalHistory += entry + "\n"; // Доступ к protected полю medicalHistory
    }

    @Override
    public void printSummary() {
        super.printSummary();
        Log.d("ChronicPatient", "\nChronic Patient Specifics:");
        if (chronicDiagnoses != null && !chronicDiagnoses.isEmpty()) {
            for (Diagnosis diagnosis : chronicDiagnoses) {
                Log.d("chronicDiagnoses", "- " + diagnosis.getDescriptionSymptoms());
            }
        } else {
            Log.d("chronicDiagnoses", "chronicDiagnoses - None");
        }

        if (chronicPrescriptions != null && !chronicPrescriptions.isEmpty()) {
            for (Prescription prescription : chronicPrescriptions) {
                Log.d("chronicPrescriptions", " - " + prescription.getNamePrescription());
            }
        } else {
            Log.d("chronicPrescriptions", "chronicPrescriptions - None");
        }
    }

    public void printSummaryWithoutBase() {
        Log.d("ChronicPatient", "Хронический пациент:");
        Log.d("ChronicPatient", "ФИО: " + getSurname() + " " + getName() + " " +
                getPatronymic());
        if (chronicDiagnoses != null && !chronicDiagnoses.isEmpty()) {
            for (Diagnosis diagnosis : chronicDiagnoses) {
                Log.d("chronicDiagnoses", "- " + diagnosis.getDescriptionSymptoms());
            }
        } else {
            Log.d("chronicDiagnoses", "Диагнозов - нет");
        }

        if (chronicPrescriptions != null && !chronicPrescriptions.isEmpty()) {
            for (Prescription prescription : chronicPrescriptions) {
                Log.d("chronicPrescriptions", "- " + prescription.getNamePrescription());
            }
        } else {
            Log.d("chronicPrescriptions", "Рецепты - нет");
        }
    }

    @Override
    public ChronicPatient clone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            ChronicPatient copy = (ChronicPatient) ois.readObject();
            ois.close();
            return copy;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
