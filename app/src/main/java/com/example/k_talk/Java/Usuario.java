package com.example.k_talk.Java;

import com.example.k_talk.Helper.UsuarioFireBase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String tipo;
    private String caminhoFoto;


    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebase();
        DatabaseReference usurersRef = firebaseRef.child("usuarios").child(getId());
        usurersRef.setValue(this);
    }

    public void atualizar(){
        String identificadorUsuario = UsuarioFireBase.getIdentificadorUsuario();
        DatabaseReference database = ConfiguracaoFireBase.getFirebase();

        DatabaseReference usuariosRef = database.child("usuarios")
                .child(identificadorUsuario);

        Map<String, Object> valoresUsuario = converteParaMap();


        usuariosRef.updateChildren(valoresUsuario);
    }

    @Exclude
    public Map<String, Object> converteParaMap(){

        HashMap<String, Object> usuarioMap = new HashMap<>();

        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getCaminhoFoto());
        usuarioMap.put("tipo", getTipo());

        return usuarioMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
