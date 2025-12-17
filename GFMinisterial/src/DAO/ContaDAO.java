package DAO;
/**
 *
 * @author ussene
 */
import Model.Conta;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {
    private Connection conexao;
    
    public ContaDAO(Connection conexao) {
        this.conexao = conexao;
    }
    
    // Criar nova conta
    public boolean criar(Conta conta) {
        String sql = "INSERT INTO contas (nome, tipo, saldo_inicial, saldo_atual, instituicao, usuario_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, conta.getNome());
            stmt.setString(2, conta.getTipo());
            stmt.setBigDecimal(3, conta.getSaldoInicial());
            stmt.setBigDecimal(4, conta.getSaldoInicial()); // Saldo atual = saldo inicial
            stmt.setString(5, conta.getInstituicao());
            stmt.setInt(6, conta.getUsuarioId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    conta.setId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao criar conta: " + e.getMessage());
        }
        return false;
    }
    
    // Listar contas do usuário
    public List<Conta> listarPorUsuario(int usuarioId) {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT * FROM contas WHERE usuario_id = ? AND ativo = TRUE ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
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
                
                contas.add(conta);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar contas: " + e.getMessage());
        }
        return contas;
    }
    
    // Atualizar saldo da conta
    public boolean atualizarSaldo(int contaId, BigDecimal novoSaldo) {
        String sql = "UPDATE contas SET saldo_atual = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setBigDecimal(1, novoSaldo);
            stmt.setInt(2, contaId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar saldo: " + e.getMessage());
        }
        return false;
    }
    
    // Buscar conta por ID
    public Conta buscarPorId(int id) {
        String sql = "SELECT * FROM contas WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Conta conta = new Conta();
                conta.setId(rs.getInt("id"));
                conta.setNome(rs.getString("nome"));
                conta.setTipo(rs.getString("tipo"));
                conta.setSaldoInicial(rs.getBigDecimal("saldo_inicial"));
                conta.setSaldoAtual(rs.getBigDecimal("saldo_atual"));
                conta.setInstituicao(rs.getString("instituicao"));
                conta.setUsuarioId(rs.getInt("usuario_id"));
                conta.setAtivo(rs.getBoolean("ativo"));
                
                return conta;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar conta: " + e.getMessage());
        }
        return null;
    }
}