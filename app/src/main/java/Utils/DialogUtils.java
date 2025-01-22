package Utils;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

public class DialogUtils {
    public void showSignOutDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Выход из приложения");
        builder.setMessage("Вы уверены, что хотите выйти?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).finishAffinity();
                }
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
