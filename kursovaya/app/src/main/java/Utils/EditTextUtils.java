package Utils;

import android.widget.EditText;

public class EditTextUtils {
    public static String checkEditTextIsEmpty(EditText editText) {
        String login = editText.getText().toString();
        if (login.isEmpty()) {
            setErrorForEditText(editText, "Заполните поле!");
            return "";
        } else {
            editText.setError(null);
            return login;
        }
    }

    public static String checkEditTextPassword(EditText editText) {
        String password = editText.getText().toString();
        if (password.length() < 4) {
            setErrorForEditText(editText, "Заполните поле!");
            return "";
        } else {
            editText.setError(null);
            return password;
        }
    }

    public static void setErrorForEditText(EditText editText, String errorText) {
        editText.setError(errorText, null);
        editText.requestFocus();
    }
}
