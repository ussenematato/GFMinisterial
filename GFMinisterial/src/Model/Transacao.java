package Model;
/**
 *
 * @author ussene
 */
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transacao {
    private int id;
    private String descricao;
    private BigDecimal valor;
    private String tipo; // Despesa ou Receita
    private LocalDate dataTransacao;
    private LocalDate dataVencimento;
    private boolean pago;
    private boolean recorrente;
    private String frequencia; // Mensal, Anual, Semanal
    private int contaId;
    private int categoriaId;
    private int usuarioId;
    private String observacoes;
    private LocalDateTime dataRegistro;
    
    // Para joins com outras tabelas
    private String nomeConta;
    private String nomeCategoria;
    private String corCategoria;
    
    public Transacao() {
        this.dataTransacao = LocalDate.now();
        this.dataVencimento = LocalDate.now();
        this.pago = false;
        this.recorrente = false;
        this.valor = BigDecimal.ZERO;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public LocalDate getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(LocalDate dataTransacao) { 
        this.dataTransacao = dataTransacao; 
    }
    
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { 
        this.dataVencimento = dataVencimento; 
    }
    
    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }
    
    public boolean isRecorrente() { return recorrente; }
    public void setRecorrente(boolean recorrente) { this.recorrente = recorrente; }
    
    public String getFrequencia() { return frequencia; }
    public void setFrequencia(String frequencia) { this.frequencia = frequencia; }
    
    public int getContaId() { return contaId; }
    public void setContaId(int contaId) { this.contaId = contaId; }
    
    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }
    
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { 
        this.dataRegistro = dataRegistro; 
    }
    
    // Getters e Setters para joins
    public String getNomeConta() { return nomeConta; }
    public void setNomeConta(String nomeConta) { this.nomeConta = nomeConta; }
    
    public String getNomeCategoria() { return nomeCategoria; }
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }
    
    public String getCorCategoria() { return corCategoria; }
    public void setCorCategoria(String corCategoria) { this.corCategoria = corCategoria; }
}
