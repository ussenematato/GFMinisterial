package model.entity;

import java.time.LocalDateTime;

public class Log {
    private Integer id;
    private Integer usuarioId;
    private String nomeUsuario;
    private String operacao;
    private String descricao;
    private String tabela;
    private Integer registroId;
    private LocalDateTime dataHora;
    private String statusOperacao; // SUCESSO, FALHA, AVISO
    
    public Log() {
        this.dataHora = LocalDateTime.now();
        this.statusOperacao = "SUCESSO";
    }
    
    public Log(Integer usuarioId, String nomeUsuario, String operacao, String descricao, String tabela, Integer registroId) {
        this();
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.operacao = operacao;
        this.descricao = descricao;
        this.tabela = tabela;
        this.registroId = registroId;
    }
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
    
    public String getOperacao() {
        return operacao;
    }
    
    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getTabela() {
        return tabela;
    }
    
    public void setTabela(String tabela) {
        this.tabela = tabela;
    }
    
    public Integer getRegistroId() {
        return registroId;
    }
    
    public void setRegistroId(Integer registroId) {
        this.registroId = registroId;
    }
    
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
    public String getStatusOperacao() {
        return statusOperacao;
    }
    
    public void setStatusOperacao(String statusOperacao) {
        this.statusOperacao = statusOperacao;
    }
    
    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", operacao='" + operacao + '\'' +
                ", descricao='" + descricao + '\'' +
                ", tabela='" + tabela + '\'' +
                ", registroId=" + registroId +
                ", dataHora=" + dataHora +
                ", statusOperacao='" + statusOperacao + '\'' +
                '}';
    }
}
