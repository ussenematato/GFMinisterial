package controller;

import model.entity.Conta;
import model.dao.ContaDAO;
import model.dao.UsuarioDAO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ContaController {
    private ContaDAO contaDAO;
    private LogController logController;
    private UsuarioDAO usuarioDAO;
    private Integer usuarioLogadoId;
    
    public ContaController(Integer usuarioLogadoId) {
        this.contaDAO = new ContaDAO();
        this.logController = new LogController();
        this.usuarioDAO = new UsuarioDAO();
        this.usuarioLogadoId = usuarioLogadoId;
    }
    
    private String obterNomeUsuario() {
        try {
            return usuarioDAO.buscarPorId(usuarioLogadoId).getNome();
        } catch (SQLException e) {
            return "Usuário Desconhecido";
        }
    }
    
    // Operações de negócio
    public boolean criarConta(String nome, String tipo, BigDecimal saldoInicial, String instituicao) {
        try {
            Conta conta = new Conta(nome, tipo, saldoInicial, instituicao, usuarioLogadoId);
            contaDAO.criar(conta);
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "CRIAR", 
                "Nova conta criada: " + nome + " (" + tipo + ")", "Conta", conta.getId());
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao criar conta: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "CRIAR", 
                "Falha ao criar conta: " + e.getMessage(), "Conta", null, "FALHA");
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
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "ATUALIZAR", 
                "Conta atualizada: " + nome, "Conta", id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar conta: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "ATUALIZAR", 
                "Falha ao atualizar conta: " + e.getMessage(), "Conta", id, "FALHA");
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
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "DELETAR", 
                "Conta desativada: " + conta.getNome(), "Conta", id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao desativar conta: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "DELETAR", 
                "Falha ao desativar conta: " + e.getMessage(), "Conta", id, "FALHA");
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
    
    public List<String> listarNomesContas() {
        try {
            return contaDAO.listarNomesPorUsuario(usuarioLogadoId);
        } catch (SQLException e) {
            System.err.println("Erro ao listar nomes de contas: " + e.getMessage());
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
        try {
            return contaDAO.obterSaldoTotalPorUsuario(usuarioLogadoId);
        } catch (SQLException e) {
            System.err.println("Erro ao obter saldo total: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    // Métodos para TODAS as contas (compartilhadas entre usuários)
    public List<Conta> listarTodasContas() {
        try {
            return contaDAO.listarTodasContas();
        } catch (SQLException e) {
            System.err.println("Erro ao listar todas as contas: " + e.getMessage());
            return List.of();
        }
    }

    public BigDecimal obterSaldoTotalTodas() {
        try {
            return contaDAO.obterSaldoTotalTodas();
        } catch (SQLException e) {
            System.err.println("Erro ao obter saldo total: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}