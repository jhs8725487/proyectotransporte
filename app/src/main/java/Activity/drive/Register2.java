package Activity.drive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.material.textfield.TextInputEditText;
import com.joel.proyectogrado.R;

import Activity.client.Usuario;
import include.MyToolbar;

public class Register2 extends AppCompatActivity {

    Button mButonRegister2;
    TextInputEditText mTextInputName;
    TextInputEditText mTextInputLastname;
    RadioButton rbtMasculino;
    RadioButton rbtFemenino;
    TextInputEditText mTextInputPhone;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Usuario usuario;
    Bundle miBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        MyToolbar.show(this,"Registro conductor",true);
        mTextInputName=(TextInputEditText)findViewById(R.id.textInputName);
        mTextInputLastname=(TextInputEditText)findViewById(R.id.textInputLastname);
        rbtMasculino=(RadioButton)findViewById(R.id.rbtMasculino);
        rbtFemenino=(RadioButton)findViewById(R.id.rbtFemenino);
        mTextInputPhone=(TextInputEditText)findViewById(R.id.textInputPhone);
        mTextInputEmail=(TextInputEditText) findViewById(R.id.textInputEmail);
        mTextInputPassword=(TextInputEditText)findViewById(R.id.textInputPassword);
        mButonRegister2=(Button) findViewById(R.id.btnGoToConfirm);

        mButonRegister2=(Button) findViewById(R.id.btnGoToConfirm);
        mButonRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToConfirm();
            }
        });
    }
    public void goToConfirm(){
        if (rbtFemenino.isChecked()) {
            usuario = new Usuario(mTextInputName.getText().toString(), mTextInputLastname.getText().toString(), "F", mTextInputPhone.getText().toString(), mTextInputEmail.getText().toString(), mTextInputPassword.getText().toString());
        }else{
            usuario = new Usuario(mTextInputName.getText().toString(), mTextInputLastname.getText().toString(), "M", mTextInputPhone.getText().toString(), mTextInputEmail.getText().toString(), mTextInputPassword.getText().toString());
        }
        Intent miIntent=new Intent(Register2.this, ConfirmPassActivity2.class);
        miBundle=new Bundle();
        miBundle.putString("Nombre",usuario.getNombre());
        miBundle.putString("Apellidos",usuario.getApellidos());
        miBundle.putString("Genero",usuario.getGenero());
        miBundle.putString("Telefono",usuario.getTelefono());
        miBundle.putString("Email",usuario.getCorreo());
        miBundle.putString("Password",usuario.getContra());
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }
}