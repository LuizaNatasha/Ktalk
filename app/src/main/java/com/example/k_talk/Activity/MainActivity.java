package com.example.k_talk.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.k_talk.Java.ConfiguracaoFireBase;
import com.example.k_talk.Java.Usuario;
import com.example.k_talk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;
    private EditText txt_email, txt_password;
    private Button btn_login;
    private TextView txt_cadastrar;
    private Usuario usuario;
    private ProgressBar progressBar_MainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verificarUsuarioLogado();
        inicializarComponentes();

        txt_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();

                if (email.length() > 0) {
                    if (password.length() > 0) {

                        usuario = new Usuario();
                        usuario.setEmail(email);
                        usuario.setSenha(password);
                        validarLogin(usuario);
                        progressBar_MainActivity.setVisibility(View.VISIBLE);


                    }else{
                        Toast.makeText(MainActivity.this, "Por favor! Preencha sua Senha.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Por favor! Preencha seu E-mail.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }
    }

    public void validarLogin(Usuario usuario){
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }else {
                    Toast.makeText(MainActivity.this, "Erro ao fazer Login! Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                    progressBar_MainActivity.setVisibility(View.GONE);
                }
            }
        });
    }

    public void inicializarComponentes(){
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_password = (EditText) findViewById(R.id.txt_senha);
        btn_login = (Button) findViewById(R.id.btn_entrar);
        txt_cadastrar = (TextView) findViewById(R.id.txt_cadastrar);
        progressBar_MainActivity = (ProgressBar) findViewById(R.id.progress_bar_MainActivity);
        txt_email.requestFocus();
    }
}
