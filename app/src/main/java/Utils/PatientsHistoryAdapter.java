package Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.R;

import java.util.List;

import Data.DatabaseHelper;
import Model.Appointment;
import Model.Doctor;
import Utils.AdaptersAdmin.DoctorAdminAdapter;

public class PatientsHistoryAdapter  extends RecyclerView.Adapter<PatientsHistoryAdapter.PatientsHistoryViewHolder> {

    private List<Appointment> appointmentList;
    private DatabaseHelper db;
    private OnItemClickListener listener;

    public PatientsHistoryAdapter(List<Appointment> appointmentList, OnItemClickListener listener,
                                  DatabaseHelper db) {
        this.appointmentList = appointmentList;
        this.listener = listener;
        this.db = db;
    }

    @NonNull
    @Override
    public PatientsHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history_patient,
                parent, false);
        return new PatientsHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsHistoryViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.statusTextView.setText("Статус: " + appointment.getStatus());
        Doctor doctor = (Doctor) db.getDoctor(appointment.getIdDoctor());
        holder.doctorNameTextView.setText("Доктор: " + doctor.getSurname() + " " +
                doctor.getName() + " " + doctor.getPatronymic());
        holder.dateTimeTextView.setText("Дата и время: " + appointment.getDate() + ", " +
                appointment.getTime());

        //  определяем иконку
        int iconResource;
        switch (appointment.getStatus().toLowerCase()) {
            case "проведён":
                iconResource = R.drawable.ic_mark;
                break;
            case "записан":
                iconResource = R.drawable.ic_clock;
                break;
            case "отменён":
                iconResource = R.drawable.ic_cancel;
                break;
            default:
                iconResource = R.drawable.ic_clock;
                break;
        }
        holder.statusIconImageView.setImageResource(iconResource);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }


    public class PatientsHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView statusTextView;
        TextView doctorNameTextView;
        TextView dateTimeTextView;
        ImageView statusIconImageView;

        public PatientsHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            statusTextView = itemView.findViewById(R.id.appointment_status);
            doctorNameTextView = itemView.findViewById(R.id.appointment_doctor_name);
            dateTimeTextView = itemView.findViewById(R.id.appointment_date_time);
            statusIconImageView = itemView.findViewById(R.id.appointment_status_icon);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }
}
