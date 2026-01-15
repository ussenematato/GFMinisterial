package model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transacao {
    private Integer id;
    private String descricao;
    private BigDecimal valor;
    private String tipo; // DESPESA ou RECEITA
    private LocalDate dataTransacao;
    private LocalDate dataVencimento;
    private Boolean pago;
    private Boolean recorrente;
    private String frequencia;
    private Integer contaId;
    private Integer categoriaId;
    private Integer usuarioId;
    private String observacoes;
    private LocalDateTime dataRegistro;
    
    // Campos das joins (para exibição)
    private String nomeConta;
    private String nomeCategoria;
    private String corCategoria;
    
    // Construtores
    public Transacao() {
        this.pago = false;
        this.recorrente = false;
        this.dataTransacao = LocalDate.now();
        this.dataVencimento = LocalDate.now();
        this.dataRegistro = LocalDateTime.now();
    }
    
    public Transacao(String descricao, BigDecimal valor, String tipo, 
                    LocalDate dataTransacao, Integer contaId, 
                    Integer categoriaId, Integer usuarioId) {
        this();
        this.descricao = descricao;
        this.valor = valor != null ? valor : BigDecimal.ZERO;
        this.tipo = tipo != null ? tipo.toUpperCase() : "DESPESA";
        this.dataTransacao = dataTransacao != null ? dataTransacao : LocalDate.now();
        this.dataVencimento = this.dataTransacao;
        this.contaId = contaId;
        this.categoriaId = categoriaId;
        this.usuarioId = usuarioId;
    }
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo != null ? tipo.toUpperCase() : "DESPESA";
    }
    
    public LocalDate getDataTransacao() {
        return dataTransacao;
    }
    
    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }
    
    public LocalDate getDataVencimento() {
        return dataVencimento;
    }
    
    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }
    
    public Boolean getPago() {
        return pago;
    }
    
    public void setPago(Boolean pago) {
        this.pago = pago;
    }
    
    public Boolean getRecorrente() {
        return recorrente;
    }
    
    public void setRecorrente(Boolean recorrente) {
        this.recorrente = recorrente;
    }
    
    public String getFrequencia() {
        return frequencia;
    }
    
    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }
    
    public Integer getContaId() {
        return contaId;
    }
    
    public void setContaId(Integer contaId) {
        this.contaId = contaId;
    }
    
    public Integer getCategoriaId() {
        return categoriaId;
    }
    
    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }
    
    public Integer getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
    
    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    
    public String getNomeConta() {
        return nomeConta;
    }
    
    public void setNomeConta(String nomeConta) {
        this.nomeConta = nomeConta;
    }
    
    public String getNomeCategoria() {
        return nomeCategoria;
    }
    
    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
    
    public String getCorCategoria() {
        return corCategoria;
    }
    
    public void setCorCategoria(String corCategoria) {
        this.corCategoria = corCategoria;
    }
    
    // Métodos utilitários
    @Override
    public String toString() {
        return descricao + " - MT " + String.format("%.2f", valor) + " (" + tipo + ")";
    }
}