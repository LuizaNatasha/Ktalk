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

import com.example.k_talk.Helper.Base64Custom;
import com.example.k_talk.Helper.UsuarioFireBase;
import com.example.k_talk.Java.ConfiguracaoFireBase;
import com.example.k_talk.Java.Usuario;
import com.example.k_talk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private EditText txt_nome, txt_email, txt_password;
    private Button btn_registrar;
    private TextView txt_goToLogin;
    private ProgressBar progressBar;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();

        progressBar.setVisibility(View.GONE);

        txt_goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();

            }
        });
    }

    private void inicializarComponentes() {
        txt_nome = (EditText) findViewById(R.id.txt_nome_CadastroActivity);
        txt_email = (EditText) findViewById(R.id.txt_email_CadastroActivity);
        txt_password = (EditText) findViewById(R.id.txt_senha_CadastroActivity);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_CadastroActivity);

        txt_goToLogin = (TextView) findViewById(R.id.textView_gotoLogin);
        txt_nome.requestFocus();

        btn_registrar = (Button) findViewById(R.id.btn_cadastrar_CadastroActivity);
    }

    private void registerNewUser(){

        String nome = txt_nome.getText().toString();
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();

        if(nome.length()>0){

                if(email.length()>0){
                    if(password.length()>0){

                        usuario = new Usuario();
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setSenha(password);
                        cadastrar(usuario);
                    }else{
                        Toast.makeText(this, "Preencha sua senha!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Preencha seu E-mail!", Toast.LENGTH_SHORT).show();
                }
        }else{
            Toast.makeText(this, "Preencha seu nome!", Toast.LENGTH_SHORT).show();
        }

    }

    public void cadastrar(final Usuario usuario){

        progressBar.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    try{
                        progressBar.setVisibility(View.GONE);

                        String  identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                        usuario.setId(identificadorUsuario);
                        usuario.salvar();

                        Toast.makeText(CadastroActivity.this,
                                "Cadastro feito com Sucesso!",
                                Toast.LENGTH_SHORT).show();

                        UsuarioFireBase.atualizarNomeUsuario(usuario.getNome());

                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                } else {
                    progressBar.setVisibility(View.GONE);
                    String erroExcecao ="";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao="Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao="Por favor, digite um e-mail válido!";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao="Essa conta já foi cadastrada!";
                    }catch (Exception e){
                        erroExcecao="ao cadastrar usuário:" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,
                            "Erro: " +erroExcecao,
                            Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        });
    }
}
