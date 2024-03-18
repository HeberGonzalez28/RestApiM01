package com.example.restapim01;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.restapim01.Config.Personas;
import com.example.restapim01.Config.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityGet extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView getListView;
    private List<Personas> mList = new ArrayList<>();
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        getListView = findViewById(R.id.listView);
        getListView.setOnItemClickListener(this);

        // Inicializar Volley
        RequestQueue queue = Volley.newRequestQueue(this);

        // Realizar la solicitud JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RestApiMethods.EndpointGetPerson, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Parsear los datos JSON y añadirlos a la lista
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String nombre = jsonObject.getString("nombres");
                                String apellido = jsonObject.getString("apellidos");
                                String telefono = jsonObject.getString("telefono");
                                String fechanac= jsonObject.getString("fechanac");
                                String imagenBase64 = jsonObject.getString("foto");

                               // Decodificar la imagen Base64 a un Bitmap
                                /*byte[] decodedString = Base64.decode(imagenBase64, Base64.DEFAULT);
                                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);*/

                                mList.add(new Personas(id,nombre, apellido, telefono, fechanac, imagenBase64));
                            }
                            // Inicializar el adaptador con los datos obtenidos
                            mAdapter = new CustomAdapter(ActivityGet.this, R.layout.item_row, mList);
                            getListView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Error en la solicitud HTTP");
                Toast.makeText(ActivityGet.this, "Error en la solicitud HTTP", Toast.LENGTH_SHORT).show();
            }
        });

        // Añadir la solicitud a la cola
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Elemento Seleccionado: " + position, Toast.LENGTH_SHORT).show();
    }
}