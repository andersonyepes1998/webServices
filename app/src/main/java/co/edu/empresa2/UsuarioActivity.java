package co.edu.empresa2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {

    EditText etusuario, etnombre, etcorreo, etclave;
    CheckBox jetactivo;
    RequestQueue rq;
    JsonRequest jrq;
    String usr, nombre, correo, clave;
    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        //ocultar la barra , asociar objetos Java con objetos xml

        getSupportActionBar().hide();
        etusuario=findViewById(R.id.etusuario);
        etnombre=findViewById(R.id.etnombre);
        etcorreo=findViewById(R.id.etcorreo);
        etclave=findViewById(R.id.etclave);
        jetactivo=findViewById(R.id.jetactivo);
        rq = Volley.newRequestQueue(this);
        sw=0;
    }

    public  void Guardar(View view){
        usr=etusuario.getText().toString();
        nombre=etnombre.getText().toString();
        correo=etcorreo.getText().toString();
        clave=etclave.getText().toString();
        if (usr.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()){
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            etusuario.requestFocus();
        }
        else{

        }
    }

    public void Consultar(View view){
        usr=etusuario.getText().toString();
        if(usr.isEmpty()){
            Toast.makeText(this, "El usuario es requerido para la busqueda", Toast.LENGTH_SHORT).show();
            etusuario.requestFocus();
        }
        else{
            String url = "http://172.18.59.67:80/WebService/consulta.php?usr=" + usr;
            jrq = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
            rq.add(jrq);
        }
    }

    public void Limpiar(View view){
        Limpiar_campos();
    }

    public void Regresar(View view){
        Intent intmain = new Intent(this,MainActivity.class);
        startActivity(intmain);
    }

    private void Limpiar_campos(){
        etnombre.setText("");
        etcorreo.setText("");
        etclave.setText("");
        etusuario.setText("");
        jetactivo.setChecked(false);
        etusuario.requestFocus();
        sw=0;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        sw=1;
        Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
            etnombre.setText(jsonObject.optString("nombre"));
            etcorreo.setText(jsonObject.optString("correo"));
            etclave.setText(jsonObject.optString("clave"));
            if (jsonObject.optString("activo").equals("si")){
                jetactivo.setChecked(true);
            }else{
                jetactivo.setChecked(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}