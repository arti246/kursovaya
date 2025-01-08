package Model;

public class User {
    private int id;
    private int idUserType;
    private String login;
    private String password;

    public static final String TABLE_NAME = "users";
    public static final String KEY_ID_USER = "idUser";
    public static final String KEY_ID_USER_TYPE = "idTypeUser";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";

    public User() {
    }

    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public User(int id, String login, String password, int idUserType) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.idUserType = idUserType;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }



    public int getIdUserType() {
        return idUserType;
    }

    public void setIdUserType(int idUserType) {
        this.idUserType = idUserType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}