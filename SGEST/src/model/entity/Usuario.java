package model.entity;

import java.time.LocalDateTime;

public class Usuario {
    private Integer id;
    private String nome;
    private String email;
    private String senhaHash;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
    private String perfil; // ADMIN, USER
    
    public Usuario() {
        this.ativo = true;
        this.dataCadastro = LocalDateTime.now();
        this.perfil = "USER";
    }
    
    public Usuario(String nome, String email, String senhaHash) {
        this();
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
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
    
    public String getSenhaHash() {
        return senhaHash;
    }
    
    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
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
    
    public String getPerfil() {
        return perfil;
    }
    
    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
    
    @Override
    public String toString() {
        return nome + " (" + email + ")";
    }
}