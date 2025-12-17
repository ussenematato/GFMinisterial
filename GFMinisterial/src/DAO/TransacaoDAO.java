package DAO;
/**
 *
 * @author ussene
 */
import Model.Conta;
import Model.Transacao;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {
    private Connection conexao;
    
    public TransacaoDAO(Connection conexao) {
        this.conexao = conexao;
    }
    
    // Registrar nova transação
    public boolean registrar(Transacao transacao) {
        String sql = "INSERT INTO transacoes (descricao, valor, tipo, data_transacao, " +
                    "data_vencimento, pago, recorrente, frequencia, conta_id, " +
                    "categoria_id, usuario_id, observacoes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, transacao.getDescricao());
            stmt.setBigDecimal(2, transacao.getValor());
            stmt.setString(3, transacao.getTipo());
            stmt.setDate(4, Date.valueOf(transacao.getDataTransacao()));
            stmt.setDate(5, Date.valueOf(transacao.getDataVencimento()));
            stmt.setBoolean(6, transacao.isPago());
            stmt.setBoolean(7, transacao.isRecorrente());
            stmt.setString(8, transacao.getFrequencia());
            stmt.setInt(9, transacao.getContaId());
            stmt.setInt(10, transacao.getCategoriaId());
            stmt.setInt(11, transacao.getUsuarioId());
            stmt.setString(12, transacao.getObservacoes());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    transacao.setId(rs.getInt(1));
                }
                
                // Atualizar saldo da conta
                atualizarSaldoConta(transacao);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao registrar transação: " + e.getMessage());
        }
        return false;
    }
    
    // Atualizar saldo da conta após transação
    private void atualizarSaldoConta(Transacao transacao) {
        ContaDAO contaDAO = new ContaDAO(conexao);
        Conta conta = contaDAO.buscarPorId(transacao.getContaId());
        
        if (conta != null) {
            BigDecimal novoSaldo = conta.getSaldoAtual();
            
            if ("Receita".equals(transacao.getTipo())) {
                novoSaldo = novoSaldo.add(transacao.getValor());
            } else if ("Despesa".equals(transacao.getTipo())) {
                novoSaldo = novoSaldo.subtract(transacao.getValor());
            }
            
            contaDAO.atualizarSaldo(conta.getId(), novoSaldo);
        }
    }
    
    // Listar transações do usuário
    public List<Transacao> listarPorUsuario(int usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT t.*, c.nome as nome_conta, cat.nome as nome_categoria, cat.cor as cor_categoria " +
                    "FROM transacoes t " +
                    "LEFT JOIN contas c ON t.conta_id = c.id " +
                    "LEFT JOIN categorias cat ON t.categoria_id = cat.id " +
                    "WHERE t.usuario_id = ? " +
                    "AND t.data_transacao BETWEEN ? AND ? " +
                    "ORDER BY t.data_transacao DESC, t.id DESC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(dataInicio));
            stmt.setDate(3, Date.valueOf(dataFim));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                transacoes.add(montarTransacao(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar transações: " + e.getMessage());
        }
        return transacoes;
    }
    
    // Listar despesas por período
    public List<Transacao> listarDespesas(int usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        List<Transacao> despesas = new ArrayList<>();
        String sql = "SELECT t.*, c.nome as nome_conta, cat.nome as nome_categoria, cat.cor as cor_categoria " +
                    "FROM transacoes t " +
                    "LEFT JOIN contas c ON t.conta_id = c.id " +
                    "LEFT JOIN categorias cat ON t.categoria_id = cat.id " +
                    "WHERE t.usuario_id = ? AND t.tipo = 'Despesa' " +
                    "AND t.data_transacao BETWEEN ? AND ? " +
                    "ORDER BY t.data_transacao DESC";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(dataInicio));
            stmt.setDate(3, Date.valueOf(dataFim));
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                despesas.add(montarTransacao(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar despesas: " + e.getMessage());
        }
        return despesas;
    }
    
    // Obter total de despesas por período
    public BigDecimal obterTotalDespesas(int usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT SUM(valor) as total FROM transacoes " +
                    "WHERE usuario_id = ? AND tipo = 'Despesa' " +
                    "AND data_transacao BETWEEN ? AND ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(dataInicio));
            stmt.setDate(3, Date.valueOf(dataFim));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter total de despesas: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    // Obter total de receitas por período
    public BigDecimal obterTotalReceitas(int usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT SUM(valor) as total FROM transacoes " +
                    "WHERE usuario_id = ? AND tipo = 'Receita' " +
                    "AND data_transacao BETWEEN ? AND ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(dataInicio));
            stmt.setDate(3, Date.valueOf(dataFim));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao obter total de receitas: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    // Método auxiliar para montar objeto Transacao
    private Transacao montarTransacao(ResultSet rs) throws SQLException {
        Transacao transacao = new Transacao();
        transacao.setId(rs.getInt("id"));
        transacao.setDescricao(rs.getString("descricao"));
        transacao.setValor(rs.getBigDecimal("valor"));
        transacao.setTipo(rs.getString("tipo"));
        transacao.setDataTransacao(rs.getDate("data_transacao").toLocalDate());
        
        if (rs.getDate("data_vencimento") != null) {
            transacao.setDataVencimento(rs.getDate("data_vencimento").toLocalDate());
        }
        
        transacao.setPago(rs.getBoolean("pago"));
        transacao.setRecorrente(rs.getBoolean("recorrente"));
        transacao.setFrequencia(rs.getString("frequencia"));
        transacao.setContaId(rs.getInt("conta_id"));
        transacao.setCategoriaId(rs.getInt("categoria_id"));
        transacao.setUsuarioId(rs.getInt("usuario_id"));
        transacao.setObservacoes(rs.getString("observacoes"));
        
        // Dados das joins
        transacao.setNomeConta(rs.getString("nome_conta"));
        transacao.setNomeCategoria(rs.getString("nome_categoria"));
        transacao.setCorCategoria(rs.getString("cor_categoria"));
        
        return transacao;
    }
}
