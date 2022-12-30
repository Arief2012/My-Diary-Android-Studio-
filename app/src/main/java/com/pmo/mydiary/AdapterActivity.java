package com.pmo.mydiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterActivity extends RecyclerView.Adapter<AdapterActivity.MyViewHolder> {
    private List<ModelActivity> mlist;
    private Activity activity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterActivity(List<ModelActivity> mlist, Activity activity) {
        this.mlist = mlist;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ModelActivity data = mlist.get(position);
        holder.tv_date.setText(data.getTanggal());
        holder.tvpagi.setText(data.getPagi());
        holder.tvsiang.setText(data.getSiang());
        holder.tvmalam.setText(data.getMalam());
        holder.tv_radio.setText(data.getRbgrup());
        holder.tv_kategori.setText(data.getSpkategori());
        holder.tv_judul.setText(data.getJudul());
        holder.tv_catatan.setText(data.getCatatan());

        holder.btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        DatabaseReference referenceprofil = FirebaseDatabase.getInstance().getReference("Data User");

                        referenceprofil.child(firebaseUser.getUid()).child("Kegiatan").child(data.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(activity, "Catatan berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Gagal menghapus Catatan", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setMessage("Apakan catatan " + data.getJudul() + " ingin dihapus ?");
                builder.show();
            }
        });

        holder.card_hasil.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
                DialogForm dialog = new DialogForm(
                        data.getTanggal(),
                        data.getPagi(),
                        data.getSiang(),
                        data.getMalam(),
                        data.getRbgrup(),
                        data.getSpkategori(),
                        data.getJudul(),
                        data.getCatatan(),
                        data.getKey(),
                        "ubah"

                );
                dialog.show(manager, "form");
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_date, tvpagi, tvsiang, tvmalam, tv_radio, tv_kategori, tv_judul, tv_catatan;
        ImageView btn_hapus;
        CardView card_hasil;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            tvpagi = itemView.findViewById(R.id.tv_checkboxpagi);
            tvsiang = itemView.findViewById(R.id.tv_checkboxsiang);
            tvmalam = itemView.findViewById(R.id.tv_checkboxmalam);
            tv_radio = itemView.findViewById(R.id.tv_radio);
            tv_kategori = itemView.findViewById(R.id.tv_kategori);
            tv_judul = itemView.findViewById(R.id.tv_title);
            tv_catatan = itemView.findViewById(R.id.tv_subject);
            btn_hapus = itemView.findViewById(R.id.hapus);
            card_hasil = itemView.findViewById(R.id.card_hasil);


        }
    }
}
