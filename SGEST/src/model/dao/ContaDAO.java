package model.dao;

import java.math.BigDecimal;
import model.entity.Conta;
import model.conexao.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {
    private Connection conexao;
    
    public ContaDAO() {
        this.conexao = Conexao.getConexao();
    }
    
    public ContaDAO(Connection conexao) {
        this.conexao = conexao;
    }
    
    // CRUD
    public void criar(Conta conta) throws SQLException {
        String sql = "INSERT INTO contas (nome, tipo, saldo_inicial, saldo_atual, "
                   + "instituicao, usuario_id, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, conta.getNome());
            stmt.setString(2, conta.getTipo());
            stmt.setBigDecimal(3, conta.getSaldoInicial());
            stmt.setBigDecimal(4, conta.getSaldoAtual());
            stmt.setString(5, conta.getInstituicao());
            stmt.setInt(6, conta.getUsuarioId());
            stmt.setBoolean(7, conta.getAtivo());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                conta.setId(rs.getInt(1));
            }
        }
    }
    
    public Conta buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM contas WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearConta(rs);
            }
        }
        return null;
    }
    
    public List<Conta> listarPorUsuario(Integer usuarioId) throws SQLException {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT * FROM contas WHERE usuario_id = ? AND ativo = TRUE ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                contas.add(mapearConta(rs));
            }
        }
        return contas;
    }
    
    public void atualizar(Conta conta) throws SQLException {
        String sql = "UPDATE contas SET nome = ?, tipo = ?, saldo_atual = ?, "
                   + "instituicao = ?, ativo = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, conta.getNome());
            stmt.setString(2, conta.getTipo());
            stmt.setBigDecimal(3, conta.getSaldoAtual());
            stmt.setString(4, conta.getInstituicao());
            stmt.setBoolean(5, conta.getAtivo());
            stmt.setInt(6, conta.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void desativar(Integer id) throws SQLException {
        String sql = "UPDATE contas SET ativo = FALSE WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public void atualizarSaldo(Integer contaId, BigDecimal novoSaldo) throws SQLException {
        String sql = "UPDATE contas SET saldo_atual = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setBigDecimal(1, novoSaldo);
            stmt.setInt(2, contaId);
            stmt.executeUpdate();
        }
    }
    
    public List<String> listarNomesPorUsuario(Integer usuarioId) throws SQLException {
        List<String> nomes = new ArrayList<>();
        String sql = "SELECT nome FROM contas WHERE usuario_id = ? AND ativo = TRUE ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                nomes.add(rs.getString("nome"));
            }
        }
        return nomes;
    }
    
    public BigDecimal obterSaldoTotalPorUsuario(Integer usuarioId) throws SQLException {
        String sql = "SELECT SUM(saldo_atual) as total FROM contas WHERE usuario_id = ? AND ativo = TRUE";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
    
    private Conta mapearConta(ResultSet rs) throws SQLException {
        Conta conta = new Conta();
        conta.setId(rs.getInt("id"));
        conta.setNome(rs.getString("nome"));
        conta.setTipo(rs.getString("tipo"));
        conta.setSaldoInicial(rs.getBigDecimal("saldo_inicial"));
        conta.setSaldoAtual(rs.getBigDecimal("saldo_atual"));
        conta.setInstituicao(rs.getString("instituicao"));
        conta.setUsuarioId(rs.getInt("usuario_id"));
        conta.setAtivo(rs.getBoolean("ativo"));
        conta.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        return conta;
    }

    // Métodos para listar TODAS as contas (compartilhadas entre usuários)
    public List<Conta> listarTodasContas() throws SQLException {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT * FROM contas WHERE ativo = TRUE ORDER BY nome";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contas.add(mapearConta(rs));
            }
        }
        return contas;
    }

    public BigDecimal obterSaldoTotalTodas() throws SQLException {
        String sql = "SELECT SUM(saldo_atual) as total FROM contas WHERE ativo = TRUE";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }
}