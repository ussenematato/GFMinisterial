package model.entity;

import java.time.LocalDateTime;

public class Usuario {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private String senhaHash;
    private String perfil; // SUPERADMIN, ADMINISTRADOR, CONTABILISTA, TESOUREIRO
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    
    public Usuario() {
        this.ativo = true;
        this.dataCadastro = LocalDateTime.now();
        this.perfil = "TESOUREIRO";
    }
    
    public Usuario(String nome, String email, String telefone, String senhaHash, String perfil) {
        this();
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senhaHash = senhaHash;
        this.perfil = perfil != null ? perfil : "TESOUREIRO";
    }
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
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
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getSenhaHash() {
        return senhaHash;
    }
    
    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }
    
    public String getPerfil() {
        return perfil;
    }
    
    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    @Override
    public String toString() {
        return nome + " (" + email + ") - " + perfil;
    }
}