package com.example.finalfragments.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalfragments.R;

import org.json.JSONObject;

public class BuscadorFragment extends Fragment {

    RequestQueue requestQueue; // Cola de solicitudes

    private final String URL = "http://192.168.18.40:3000/api/peliculas/";

    EditText edtIdH, edtTituloH, edtDuracionH, edtGeneroH, edtLanzamientoH;

    Button btnBuscarH,btnActualizarH;

    public BuscadorFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.buscar_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtIdH = view.findViewById(R.id.edtIdH);
        edtTituloH = view.findViewById(R.id.edtTituloH);
        edtDuracionH = view.findViewById(R.id.edtDuracionH);
        edtGeneroH = view.findViewById(R.id.edtGeneroH);
        edtLanzamientoH = view.findViewById(R.id.edtLanzamientoH);

        btnActualizarH = view.findViewById(R.id.btnActualizarH);
        btnBuscarH = view.findViewById(R.id.btnBuscarH);

        btnBuscarH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {buscarPeli();}
        });

        btnActualizarH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarPeli();
            }
        });

    }

    public void buscarPeli(){
        String idStr = edtIdH.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(getContext(), "Ingresa un ID", Toast.LENGTH_SHORT).show();
            return;
        }
        int idpeli = Integer.parseInt(idStr);
        String endpoint = URL + idpeli;

        try {
            requestQueue = Volley.newRequestQueue(requireContext().getApplicationContext());

            // Consumos el ws
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    endpoint,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                boolean success = jsonObject.getBoolean("success");

                                if(success){
                                    JSONObject registro = jsonObject.getJSONObject("data");
                                    edtTituloH.setText(registro.getString("titulo"));
                                    edtDuracionH.setText(registro.getString("duracion"));
                                    edtGeneroH.setText(registro.getString("genero"));
                                    edtLanzamientoH.setText(registro.getString("lanzamiento"));
                                }else{
                                    Toast.makeText(getContext(), "No se encontro la peli", Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Log.e("errorJSON", e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            NetworkResponse response = volleyError.networkResponse;

                            if (response == null) {
                                Toast.makeText(getContext(), "Error de red o sin conexión", Toast.LENGTH_LONG).show();
                                return;
                            }

                            int statusCode = response.statusCode;
                            // Contenido (String)
                            String messageJSON = new String(response.data);

                            try {
                                JSONObject jsonWS = new JSONObject(messageJSON);
                                String message = jsonWS.getString("message");

                                // Enviamos un mensaje
                                if(statusCode == 404){
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Log.e("ErrorJSON", e.toString());
                            }
                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("ErrorJSON", e.toString());
        }
    }

    public void actualizarPeli(){
        String idStr = edtIdH.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(getContext(), "Ingresa un ID", Toast.LENGTH_SHORT).show();
            return;
        }
        int idpeli = Integer.parseInt(idStr);
        String endpoint = URL + idpeli;
        try {
            // Obtenemos los datos de los edittext
            JSONObject data = new JSONObject();
            data.put("titulo", edtTituloH.getText().toString());
            data.put("duracion", edtDuracionH.getText().toString());
            data.put("genero", edtGeneroH.getText().toString());
            data.put("duracion", Integer.parseInt(edtDuracionH.getText().toString()));
            data.put("lanzamiento", edtLanzamientoH.getText().toString());

            requestQueue = Volley.newRequestQueue(requireContext().getApplicationContext());

            // Consumos el ws
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    endpoint,
                    data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                boolean success = jsonObject.getBoolean("success");
                                // Enviamos el mensaje recibido del ws
                                Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e("errorJSON", e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            NetworkResponse response = volleyError.networkResponse;

                            if (response == null) {
                                Toast.makeText(getContext(), "Error de red o sin conexión", Toast.LENGTH_LONG).show();
                                return;
                            }

                            int statusCode = response.statusCode;
                            // Contenido (String)
                            String messageJSON = new String(response.data);

                            try {
                                JSONObject jsonWS = new JSONObject(messageJSON);
                                String message = jsonWS.getString("message");

                                // Enviamos un mensaje
                                if(statusCode == 404){
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                }
                                if(statusCode > 405){
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                }

                            } catch (Exception e) {
                                Log.e("ErrorJSON", e.toString());
                            }
                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e("ErrorJSON", e.toString());
        }
    }
}
