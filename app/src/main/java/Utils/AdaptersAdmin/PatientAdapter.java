package Utils.AdaptersAdmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovaya.R;

import java.util.List;

import Model.Patient;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder>{

    private List<Patient> patients;
    private OnItemClickListener listener;

    public PatientAdapter(List<Patient> patients, OnItemClickListener listener) {
        this.patients = patients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list_item, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.textView1.setText("ФИО: " + patient.getSurname() + " " + patient.getName() +
                " " + patient.getPatronymic());
        holder.textView2.setText("Дата рождения: " + patient.getDataBirth());
        holder.textView3.setText("№ полиса: " + patient.getInsurance());
        holder.itemView.setOnClickListener(v -> { // устанавливаем слушателя нажатий
            if(listener != null) {
                listener.onItemClick(patient);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
        notifyDataSetChanged();
    }

    public void filterList(List<Patient> filteredList) {
        patients = filteredList;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(Patient patient);
    }
}