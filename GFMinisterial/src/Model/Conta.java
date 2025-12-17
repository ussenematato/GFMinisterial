package Model;

/**
 *
 * @author ussene
 */

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Conta {
    private int id;
    private String nome;
    private String tipo; // Corrente, Poupança, Investimento, Carteira
    private BigDecimal saldoInicial;
    private BigDecimal saldoAtual;
    private String instituicao;
    private int usuarioId;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    
    public Conta() {
        this.saldoInicial = BigDecimal.ZERO;
        this.saldoAtual = BigDecimal.ZERO;
        this.ativo = true;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public BigDecimal getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(BigDecimal saldoInicial) { 
        this.saldoInicial = saldoInicial; 
    }
    
    public BigDecimal getSaldoAtual() { return saldoAtual; }
    public void setSaldoAtual(BigDecimal saldoAtual) { 
        this.saldoAtual = saldoAtual; 
    }
    
    public String getInstituicao() { return instituicao; }
    public void setInstituicao(String instituicao) { 
        this.instituicao = instituicao; 
    }
    
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { 
        this.dataCriacao = dataCriacao; 
    }
}
