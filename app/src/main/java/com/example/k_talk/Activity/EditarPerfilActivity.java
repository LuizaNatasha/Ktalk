package com.example.k_talk.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.k_talk.Helper.UsuarioFireBase;
import com.example.k_talk.Java.ConfiguracaoFireBase;
import com.example.k_talk.Java.Usuario;
import com.example.k_talk.PermissaoCamera;
import com.example.k_talk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;

    private EditText txt_nome, txt_email;
    private ImageButton imgBtn_camera, imgBtn_galeria;
    private Button btn_back, btn_salvar;
    private CircleImageView circleImageView_perfil;

    private StorageReference storageReference;
    private String identificadorUsuario;

    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);


        storageReference = ConfiguracaoFireBase.getFirebaseStorage();
        identificadorUsuario = UsuarioFireBase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFireBase.getDadosUsuarioLogado();

        iniciarComponentes();

        PermissaoCamera.validarPermissoes(permissoesNecessarias, this, 1);

        final FirebaseUser usuario = UsuarioFireBase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();

        if(url != null){
            Glide.with(EditarPerfilActivity.this)
                    .load(url)
                    .into(circleImageView_perfil);
        }else {
            circleImageView_perfil.setImageResource(R.drawable.avatar);
        }

        txt_nome.setText(usuario.getDisplayName());
        txt_email.setText(usuario.getEmail());

        imgBtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_CAMERA);
                }

            }
        });

        imgBtn_galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });

        btn_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = txt_nome.getText().toString();

                boolean retorno =  UsuarioFireBase.atualizarNomeUsuario(nome);

                if (retorno){

                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualizar();

                    Toast.makeText(EditarPerfilActivity.this,
                            "Nome alterado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void iniciarComponentes() {
        txt_nome = (EditText) findViewById(R.id.txt_nome_AlterarPerfilAcivity);
        txt_email = (EditText) findViewById(R.id.txt_email_AlterarPerfilAcivity);
        imgBtn_camera = (ImageButton) findViewById(R.id.btn_camera_AlterarPerfilAcivity);
        imgBtn_galeria = (ImageButton) findViewById(R.id.btn_galery_AlterarPerfilAcivity);
        btn_salvar = (Button) findViewById(R.id.btn_salvar_AlterarPerfilAcivity);
        circleImageView_perfil = (CircleImageView) findViewById(R.id.imageView_activityAlterarPerfil);
        txt_email.setEnabled(false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK){
            Bitmap imagem = null;

            try {
                switch (requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;

                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null){
                    circleImageView_perfil.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] dadosImagem = baos.toByteArray();


                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditarPerfilActivity.this, "Erro ao fazer upload da imagem! Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditarPerfilActivity.this, "Sucesso ao fazer upload da imagem!", Toast.LENGTH_SHORT).show();

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFotoUsuario(url);
                                }
                            });

                        }
                    });

                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for ( int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidarPermissao();
            }
        }
    }

    private void alertaValidarPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Necessárias foram negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar todas as permissões, caso não ocorra o app não irá funcionar corretamente");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    public void atualizarFotoUsuario(Uri url){
        boolean retorno = UsuarioFireBase.atualizarFotoUsuario(url);
        if (retorno){
            usuarioLogado.setCaminhoFoto(url.toString());
            usuarioLogado.atualizar();
            Toast.makeText(EditarPerfilActivity.this, "Sua foto foi atualizada com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoHome(View view) {
        finish();
    }
}
