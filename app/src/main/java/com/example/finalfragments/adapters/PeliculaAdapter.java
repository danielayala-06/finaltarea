package com.example.finalfragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalfragments.R;
import com.example.finalfragments.models.Pelicula;

import java.util.List;

public class PeliculaAdapter extends RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder> {

    public interface OnEliminarListener {
        void onEliminar(Pelicula pelicula, int position);
    }

    private final List<Pelicula> peliculas;
    private final OnEliminarListener listener;

    public PeliculaAdapter(List<Pelicula> peliculas, OnEliminarListener listener) {
        this.peliculas = peliculas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeliculaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_herramienta, parent, false);
        return new PeliculaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeliculaViewHolder holder, int position) {
        Pelicula p = peliculas.get(position);
        holder.txtTitulo.setText(p.getTitulo());
        holder.txtGenero.setText(p.getGenero());
        holder.txtLanzamiento.setText(p.getLanzamiento() + " · " + p.getDuracion() + " min");
        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) listener.onEliminar(p, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return peliculas.size();
    }

    public void eliminarItem(int position) {
        peliculas.remove(position);
        notifyItemRemoved(position);
    }

    static class PeliculaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtGenero, txtLanzamiento;
        Button btnEliminar;

        PeliculaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloRV);
            txtGenero = itemView.findViewById(R.id.txtGeneroRV);
            txtLanzamiento = itemView.findViewById(R.id.txtLanzamientoRV);
            btnEliminar = itemView.findViewById(R.id.btnEliminarRV);
        }
    }
}
