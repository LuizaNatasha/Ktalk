package com.example.k_talk.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.k_talk.Activity.MainActivity;
import com.example.k_talk.Activity.EditarPerfilActivity;
import com.example.k_talk.Helper.UsuarioFireBase;
import com.example.k_talk.Java.ConfiguracaoFireBase;
import com.example.k_talk.Java.Usuario;
import com.example.k_talk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private CircleImageView circleImageView_perfil;
    private TextView txt_nome, txt_email;
    private FirebaseAuth mAuth;
    private Button btn_editar, btn_sair;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private Usuario usuarioLogado;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        mAuth = FirebaseAuth.getInstance();

        storageReference = ConfiguracaoFireBase.getFirebaseStorage();
        identificadorUsuario = UsuarioFireBase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFireBase.getDadosUsuarioLogado();

        btn_sair = (Button) view.findViewById(R.id.btn_sair_PerfilFragment);
        btn_editar = (Button) view.findViewById(R.id.btn_editar_PerfilFragment);
        circleImageView_perfil = (CircleImageView) view.findViewById(R.id.circleImg_PerfilFragment);
        txt_nome = (TextView) view.findViewById(R.id.txt_nome_PerfilFragment);
        txt_email= (TextView) view.findViewById(R.id.txt_email_PerfilFragment);

        final FirebaseUser usuario = UsuarioFireBase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();

        if(url != null){
            Glide.with(PerfilFragment.this)
                    .load(url)
                    .into(circleImageView_perfil);
        }else {
            circleImageView_perfil.setImageResource(R.drawable.avatar);
        }

        txt_nome.setText(usuario.getDisplayName());
        txt_email.setText(usuario.getEmail());

        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(intent);
            }
        });


        btn_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mAuth.signOut();
                }catch (Exception e){
                    e.printStackTrace();
                }

                Toast.makeText(getActivity(), "Usuário Desconectado!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
