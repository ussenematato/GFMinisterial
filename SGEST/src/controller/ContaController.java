package controller;

import model.entity.Conta;
import model.dao.ContaDAO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ContaController {
    private ContaDAO contaDAO;
    private Integer usuarioLogadoId;
    
    public ContaController(Integer usuarioLogadoId) {
        this.contaDAO = new ContaDAO();
        this.usuarioLogadoId = usuarioLogadoId;
    }
    
    // Operações de negócio
    public boolean criarConta(String nome, String tipo, BigDecimal saldoInicial, String instituicao) {
        try {
            Conta conta = new Conta(nome, tipo, saldoInicial, instituicao, usuarioLogadoId);
            contaDAO.criar(conta);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao criar conta: " + e.getMessage());
            return false;
        }
    }
    
    public boolean atualizarConta(Integer id, String nome, String tipo, BigDecimal saldoAtual, String instituicao) {
        try {
            Conta conta = contaDAO.buscarPorId(id);
            if (conta == null || !conta.getUsuarioId().equals(usuarioLogadoId)) {
                return false;
            }
            
            conta.setNome(nome);
            conta.setTipo(tipo);
            conta.setSaldoAtual(saldoAtual);
            conta.setInstituicao(instituicao);
            
            contaDAO.atualizar(conta);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar conta: " + e.getMessage());
            return false;
        }
    }
    
    public boolean desativarConta(Integer id) {
        try {
            Conta conta = contaDAO.buscarPorId(id);
            if (conta == null || !conta.getUsuarioId().equals(usuarioLogadoId)) {
                return false;
            }
            
            contaDAO.desativar(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao desativar conta: " + e.getMessage());
            return false;
        }
    }
    
    public List<Conta> listarContasAtivas() {
        try {
            return contaDAO.listarPorUsuario(usuarioLogadoId);
        } catch (SQLException e) {
            System.err.println("Erro ao listar contas: " + e.getMessage());
            return List.of();
        }
    }
    
    public Conta buscarContaPorId(Integer id) {
        try {
            Conta conta = contaDAO.buscarPorId(id);
            if (conta != null && conta.getUsuarioId().equals(usuarioLogadoId)) {
                return conta;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar conta: " + e.getMessage());
            return null;
        }
    }
    
    public boolean atualizarSaldo(Integer contaId, BigDecimal valor, String operacao) {
        try {
            Conta conta = contaDAO.buscarPorId(contaId);
            if (conta == null || !conta.getUsuarioId().equals(usuarioLogadoId)) {
                return false;
            }
            
            BigDecimal novoSaldo = conta.getSaldoAtual();
            if ("CREDITO".equalsIgnoreCase(operacao)) {
                novoSaldo = novoSaldo.add(valor);
            } else if ("DEBITO".equalsIgnoreCase(operacao)) {
                novoSaldo = novoSaldo.subtract(valor);
            } else {
                return false;
            }
            
            contaDAO.atualizarSaldo(contaId, novoSaldo);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar saldo: " + e.getMessage());
            return false;
        }
    }
    
    public BigDecimal obterSaldoTotal() {
        List<Conta> contas = listarContasAtivas();
        return contas.stream()
                .map(Conta::getSaldoAtual)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}