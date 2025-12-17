package model.entity;

public class Categoria {
    private Integer id;
    private String nome;
    private String tipo; // DESPESA ou RECEITA
    private String descricao;
    private Integer usuarioId;
    private String cor;
    private Boolean ativo;
    
    public Categoria() {
        this.ativo = true;
        this.cor = "#2196F3";
    }
    
    public Categoria(String nome, String tipo, String descricao, 
                    Integer usuarioId, String cor) {
        this();
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.usuarioId = usuarioId;
        this.cor = cor != null ? cor : "#2196F3";
    }
    
    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }
    
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    @Override
    public String toString() {
        return nome + " (" + tipo + ")";
    }
}