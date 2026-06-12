package com.example.finalfragments.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalfragments.R;
import com.example.finalfragments.adapters.PeliculaAdapter;
import com.example.finalfragments.models.Pelicula;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListadoFragment extends Fragment {

    private final String URL = "http://192.168.18.40:3000/api/peliculas/";
    private RequestQueue requestQueue;
    private PeliculaAdapter adapter;
    private final List<Pelicula> listaPeliculas = new ArrayList<>();

    public ListadoFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycleyPeliculas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PeliculaAdapter(listaPeliculas, this::eliminarPelicula);
        recyclerView.setAdapter(adapter);

        listarPeliculas();
    }

    private void listarPeliculas() {
        requestQueue = Volley.newRequestQueue(requireContext().getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            listaPeliculas.clear();
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                listaPeliculas.add(new Pelicula(
                                        obj.getInt("idpelicula"),
                                        obj.getString("titulo"),
                                        obj.getInt("duracion"),
                                        obj.getString("genero"),
                                        obj.getString("lanzamiento")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e("ListadoFragment", e.toString());
                    }
                },
                error -> {
                    Log.e("ListadoFragment", error.toString());
                    Toast.makeText(getContext(), "Error al cargar las peliculas", Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(request);
    }

    private void eliminarPelicula(Pelicula pelicula, int position) {
        String endpoint = URL + pelicula.getId();
        requestQueue = Volley.newRequestQueue(requireContext().getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                endpoint,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        if (success) {
                            adapter.eliminarItem(position);
                        }
                    } catch (Exception e) {
                        Log.e("ListadoFragment", e.toString());
                    }
                },
                error -> {
                    Log.e("ListadoFragment", error.toString());
                    Toast.makeText(getContext(), "Error al eliminar la pelicula", Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(request);
    }
}
