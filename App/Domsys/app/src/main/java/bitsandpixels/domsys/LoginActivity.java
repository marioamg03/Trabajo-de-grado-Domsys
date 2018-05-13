package bitsandpixels.domsys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText pLogueoEmail;
    private EditText pLogueoClave;
    private Button pLogueoBoton;

    private FirebaseAuth Autenticacion;

    private ProgressDialog pProgreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Autenticacion = FirebaseAuth.getInstance();

        pProgreso = new ProgressDialog(this);

        pLogueoEmail = (EditText) findViewById(R.id.LogueoEmail);
        pLogueoClave = (EditText) findViewById(R.id.LogueoClave);

        pLogueoBoton = (Button) findViewById(R.id.LogueoBoton);

        pLogueoBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });
    }

    private void checkLogin() {
        String Email = pLogueoEmail.getText().toString().trim();
        String Clave = pLogueoClave.getText().toString().trim();

        if(!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Clave)){

            pProgreso.setMessage("Iniciando Sesion ...");
            pProgreso.show();

            Autenticacion.signInWithEmailAndPassword(Email,Clave).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        pProgreso.dismiss();

                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    } else {

                        pProgreso.dismiss();

                        Toast.makeText(LoginActivity.this,"Usuario o Clave Incorrecto",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }
}

