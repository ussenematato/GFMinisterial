package model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Conta {
    private Integer id;
    private String nome;
    private String tipo;
    private BigDecimal saldoInicial;
    private BigDecimal saldoAtual;
    private String instituicao;
    private Integer usuarioId;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    
    // Construtores
    public Conta() {
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
        this.saldoInicial = BigDecimal.ZERO;
        this.saldoAtual = BigDecimal.ZERO;
    }
    
    public Conta(String nome, String tipo, BigDecimal saldoInicial, String instituicao, Integer usuarioId) {
        this();
        this.nome = nome;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial != null ? saldoInicial : BigDecimal.ZERO;
        this.saldoAtual = this.saldoInicial;
        this.instituicao = instituicao;
        this.usuarioId = usuarioId;
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
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }
    
    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }
    
    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }
    
    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }
    
    public String getInstituicao() {
        return instituicao;
    }
    
    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }
    
    public Integer getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    // Métodos utilitários
    @Override
    public String toString() {
        return nome + " (" + tipo + ") - MT " + String.format("%.2f", saldoAtual);
    }
}