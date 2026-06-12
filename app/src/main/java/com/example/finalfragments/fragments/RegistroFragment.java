package com.example.finalfragments.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalfragments.R;

import org.json.JSONObject;

import java.lang.reflect.Method;

public class RegistroFragment extends Fragment {

    Button btnGuardarPelicula, btnResetPelicula;
    RequestQueue requestQueue; // Cola de solicitudes

    private final String URL = "http://192.168.101.15:3000/api/peliculas/";

    EditText edtTitulo, edtDuracion, edtLanzamiento;

    Spinner spinner;
    String genero = "";

    public RegistroFragment(){}

    // Asociamos el fragment con el XML

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registrar_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias
        btnGuardarPelicula = view.findViewById(R.id.btnGuardarPeliculas);
        btnResetPelicula = view.findViewById(R.id.btnResetPeliculas);

        edtTitulo = view.findViewById(R.id.edtTitulo);
        edtDuracion = view.findViewById(R.id.edtDuracion);
        //edtGenero = view.findViewById(R.id.edtGenero);
        edtLanzamiento = view.findViewById(R.id.edtLanzamiento);
        //Obtenemos el genero

        spinner = view.findViewById(R.id.spinnerGeneros);
        String[] opciones = {"animado", "drama", "comedia", "accion", "terror"};
        // Programacion del spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                opciones
        );
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(adapter);

        // Obtenemos la seleccion
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genero = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //genero = "accion";
            }
        });


        btnGuardarPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Peliculas");
                builder.setMessage("¿Estas seguro de Agregar la Pelicula?");
                builder.setNegativeButton("Cancelar", null);
                builder.setPositiveButton("Si", (a,b)->{
                   registarPelicula();
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        btnResetPelicula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetUI();
            }
        });
    }

    private void registarPelicula(){
        JSONObject newPeli = new JSONObject();

        try {
            newPeli.put("titulo", edtTitulo.getText().toString());
            newPeli.put("duracion", Integer.parseInt(edtDuracion.getText().toString()));
            newPeli.put("genero", genero);
            newPeli.put("lanzamiento", edtLanzamiento.getText().toString());


        } catch (Exception e) {
            Log.e("ErrorJSON", e.toString());
        }

        // Canal de comunicacion
        requestQueue = Volley.newRequestQueue(requireContext().getApplicationContext());

        // Consumimos el ws
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                newPeli,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");
                            int id = jsonObject.getInt("id");

                            if(success){
                                resetUI();
                                Toast.makeText(getContext(), message+" ID: "+id, Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Log.e("ErrorWs", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("ERRORWS", volleyError.toString());
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
    private void resetUI(){
        // Limpaimos los editText
        edtLanzamiento.setText(null);
        edtDuracion.setText(null);
        edtTitulo.setText(null);
    }

}
