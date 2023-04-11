package com.example.crud_php;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdmMatriculas extends AppCompatActivity {

    ListView listView;
    Adapter adapter;
    public static ArrayList<Usuarios> employeeArrayList3 = new ArrayList<>();
    String url = "https://tigerish-parallels.000webhostapp.com/crud/MostrarMatriculas.php";
    Usuarios estudiantes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.myListView);
        adapter = new Adapter(this,employeeArrayList3);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                ProgressDialog progressDialog = new ProgressDialog(view.getContext());

                CharSequence[] dialogItem = {"Eliminar Datos"};
                builder.setTitle(employeeArrayList3.get(position).getNombre());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        switch (i){

                            case 0:
                                deleteData(employeeArrayList3.get(position).getId());
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        retrieveData();
    }

    private void deleteData(final String id) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://tigerish-parallels.000webhostapp.com/crud/EliminarMatricula.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Registro eliminado correctamente.")){
                            Toast.makeText(com.example.crud_php.AdmMatriculas.this, "Registro eliminado correctamente.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), com.example.crud_php.AdmMatriculas.class));

                        }
                        else{
                            Toast.makeText(com.example.crud_php.AdmMatriculas.this, "No se puedo eliminar", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), com.example.crud_php.AdmMatriculas.class));
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.example.crud_php.AdmMatriculas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    public void retrieveData(){

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        employeeArrayList3.clear();
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            String exito = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("estudiantes");

                            if(exito.equals("1")){


                                for(int i=0;i<jsonArray.length();i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String nombre = object.getString("nombre");
                                    String correo = object.getString("email");

                                    estudiantes = new Usuarios(id,nombre+" - "+correo,correo);
                                    employeeArrayList3.add(estudiantes);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(com.example.crud_php.AdmMatriculas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    public void agregar(View view) {
        startActivity(new Intent(getApplicationContext(),agregar.class));
    }

    public void cursos(View view) {
        startActivity(new Intent(getApplicationContext(),AdmCursos.class));
    }

    public void agregarCurso(View view) {
        startActivity(new Intent(getApplicationContext(),AgregarCursos.class));
    }

    public void matriculas(View view) {
        startActivity(new Intent(getApplicationContext(),AdmMatriculas.class));
    }

    public void matricular(View view) {
        startActivity(new Intent(getApplicationContext(),agregarMatricula.class));
    }

    public void usuarios(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

}