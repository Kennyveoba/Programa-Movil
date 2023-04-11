package com.example.crud_php;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class editar_cursos extends AppCompatActivity {
    EditText edId, edNombre, edCorreo;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);
        edId = findViewById(R.id.id);
        edNombre = findViewById(R.id.nombre);
        edCorreo = findViewById(R.id.correo);
        Intent intent = getIntent();
        position = intent.getExtras().getInt("position");
        edId.setText(AdmCursos.employeeArrayList2.get(position).getId());
        edNombre.setText(AdmCursos.employeeArrayList2.get(position).getNombre());
        edCorreo.setText(AdmCursos.employeeArrayList2.get(position).getCorreo());
    }

    public void actualizar(View view) {
        final String id = edId.getText().toString();
        final String nombre = edNombre.getText().toString();
        final String correo = edCorreo.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Actualizando....");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, "https://tigerish-parallels.000webhostapp.com/crud/ModificarCurso.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(editar_cursos.this, response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),AdmCursos.class));
                        finish();
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(editar_cursos.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("id",id);
                params.put("nombre",nombre);
                params.put("correo",correo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(editar_cursos.this);
        requestQueue.add(request);
    }
}