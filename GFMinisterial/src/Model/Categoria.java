package Model;
/**
 *
 * @author ussene
 */
public class Categoria {
    private int id;
    private String nome;
    private String tipo; // Despesa ou Receita
    private String descricao;
    private int usuarioId;
    private String cor; // Hexadecimal
    private boolean ativo;
    
    public Categoria() {
        this.ativo = true;
        this.cor = "#2196F3"; // Azul padrão
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
