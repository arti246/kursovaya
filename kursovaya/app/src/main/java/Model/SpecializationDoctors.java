package Model;

public class SpecializationDoctors {
    private int idSpec;
    private String nameSpec;

    public static final String TABLE_NAME = "specializations";
    public static final String KEY_ID_SPEC = "idSpecialization";
    public static final String KEY_NAME_SPEC = "nameSpecialization";

    public SpecializationDoctors() {
    }

    public SpecializationDoctors(int idSpec, String nameSpec) {
        this.idSpec = idSpec;
        this.nameSpec = nameSpec;
    }

    public SpecializationDoctors(String nameSpec) {
        this.nameSpec = nameSpec;
    }

    public int getIdSpec() {
        return idSpec;
    }

    public void setIdSpec(int idSpec) {
        this.idSpec = idSpec;
    }

    public String getNameSpec() {
        return nameSpec;
    }

    public void setNameSpec(String nameSpec) {
        this.nameSpec = nameSpec;
    }
}