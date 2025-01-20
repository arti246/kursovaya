package Utils.AdaptersAdmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.R;

import java.util.List;

import Data.DatabaseHelper;
import Model.Doctor;

public class DoctorAdminAdapter extends RecyclerView.Adapter<DoctorAdminAdapter.DoctorViewHolder> {

    private List<Doctor> doctors;
    private OnItemClickListener listener;
    private DatabaseHelper db;

    public DoctorAdminAdapter(List<Doctor> doctors, OnItemClickListener listener,
                              DatabaseHelper db) {
        this.doctors = doctors;
        this.listener = listener;
        this.db = db;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list_item,
                parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        String spec = db.getSpecializationById(doctor.getIdSpecialization());

        holder.textView1.setText("ФИО: " + doctor.getSurname() + " " + doctor.getName() +
                " " + doctor.getPatronymic());
        holder.textView2.setText("Специализация: " + spec);
        holder.textView3.setText("№ кабинета: " + doctor.getOffice_number());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
        notifyDataSetChanged();
    }

    public void filterList(List<Doctor> filteredList) {
        doctors = filteredList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Doctor doctor);
    }
}