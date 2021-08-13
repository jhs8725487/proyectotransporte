package Activity.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.joel.proyectogrado.R;

import java.util.HashMap;
import java.util.Map;

import include.MyToolbar;

public class RegisterActivity extends AppCompatActivity {


    //vistas
    Button mButonRegister;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputPhone;
    TextInputEditText mTextInputLastname;
    RadioButton  rbtMasculino;
    RadioButton rbtFemenino;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    TextInputEditText mTextInputConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MyToolbar.show(this,"Registro usuario",true);

        mButonRegister=(Button) findViewById(R.id.btnGoToRegister);
        mTextInputEmail=(TextInputEditText) findViewById(R.id.textInputEmail);
        mTextInputPassword=(TextInputEditText)findViewById(R.id.textInputPassword);
        mTextInputConfirmPassword=(TextInputEditText)findViewById(R.id.textInputConfirmPassword);
        mTextInputName=(TextInputEditText)findViewById(R.id.textInputName);
        mTextInputLastname=(TextInputEditText)findViewById(R.id.textInputLastname);
        mTextInputPhone=(TextInputEditText)findViewById(R.id.textInputPhone);
        rbtMasculino=(RadioButton)findViewById(R.id.rbtMasculino);
        rbtFemenino=(RadioButton)findViewById(R.id.rbtFemenino);
        //mDialog= new SpotsDialog.Builder().setContext(RegisterActivity.this).setMessage("Espere un momento").build();


        mButonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTextInputPassword.equals(mTextInputConfirmPassword)) {
                    ejecutarServicio("http://192.168.0.18//ejemploBDRemota/insertar_usuario.php");
                }else{
                    Toast.makeText(RegisterActivity.this, "Las contraseñas no coiciden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
   /* void registerUser(){

    }*/
    private void ejecutarServicio(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("Nombre", mTextInputName.getText().toString());
                parametros.put("Apellidos", mTextInputLastname.getText().toString());
                if (rbtMasculino.isChecked()) {
                    parametros.put("Sexo", "M");
                }else{
                    parametros.put("Sexo","F");
                }
                parametros.put("Telefono", mTextInputPhone.getText().toString());
                parametros.put("Correo", mTextInputEmail.getText().toString());
                parametros.put("usu_password", mTextInputPassword.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}