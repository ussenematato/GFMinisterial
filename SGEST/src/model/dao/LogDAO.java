package model.dao;

import model.conexao.Conexao;
import model.entity.Log;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {
    
    public LogDAO() {
    }
    
    public void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS logs (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "usuario_id INT NOT NULL," +
                "nome_usuario VARCHAR(255) NOT NULL," +
                "operacao VARCHAR(50) NOT NULL," +
                "descricao TEXT," +
                "tabela VARCHAR(50)," +
                "registro_id INT," +
                "data_hora DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "status_operacao VARCHAR(20) DEFAULT 'SUCESSO'," +
                "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)" +
                ")";
        
        try {
            Connection conn = Conexao.getConexao();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("Tabela 'logs' criada ou já existe.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela de logs: " + e.getMessage());
        }
    }
    
    public boolean salvar(Log log) {
        String sql = "INSERT INTO logs (usuario_id, nome_usuario, operacao, descricao, tabela, registro_id, data_hora, status_operacao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, log.getUsuarioId());
            pstmt.setString(2, log.getNomeUsuario());
            pstmt.setString(3, log.getOperacao());
            pstmt.setString(4, log.getDescricao());
            pstmt.setString(5, log.getTabela());
            pstmt.setObject(6, log.getRegistroId());
            pstmt.setTimestamp(7, Timestamp.valueOf(log.getDataHora()));
            pstmt.setString(8, log.getStatusOperacao());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        log.setId(rs.getInt(1));
                    }
                }
                pstmt.close();
                return true;
            }
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar log: " + e.getMessage());
        }
        
        return false;
    }
    
    public List<Log> obterTodos() {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs ORDER BY data_hora DESC";
        
        try {
            Connection conn = Conexao.getConexao();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                logs.add(extrairDoResultSet(rs));
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao obter logs: " + e.getMessage());
        }
        
        return logs;
    }
    
    public List<Log> obterPorUsuario(Integer usuarioId) {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs WHERE usuario_id = ? ORDER BY data_hora DESC";
        
        try {
            Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, usuarioId);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(extrairDoResultSet(rs));
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao obter logs do usuário: " + e.getMessage());
        }
        
        return logs;
    }
    
    public List<Log> obterPorOperacao(String operacao) {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs WHERE operacao = ? ORDER BY data_hora DESC";
        
        try {
            Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, operacao);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(extrairDoResultSet(rs));
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao obter logs por operação: " + e.getMessage());
        }
        
        return logs;
    }
    
    public List<Log> obterPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs WHERE data_hora BETWEEN ? AND ? ORDER BY data_hora DESC";
        
        try {
            Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(dataInicio));
            pstmt.setTimestamp(2, Timestamp.valueOf(dataFim));
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(extrairDoResultSet(rs));
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao obter logs por período: " + e.getMessage());
        }
        
        return logs;
    }
    
    public List<Log> obterPorFiltros(Integer usuarioId, String operacao, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Log> logs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM logs WHERE 1=1");
        
        if (usuarioId != null) {
            sql.append(" AND usuario_id = ?");
        }
        if (operacao != null && !operacao.isEmpty()) {
            sql.append(" AND operacao = ?");
        }
        if (dataInicio != null && dataFim != null) {
            sql.append(" AND data_hora BETWEEN ? AND ?");
        }
        
        sql.append(" ORDER BY data_hora DESC");
        
        try {
            Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            
            int index = 1;
            if (usuarioId != null) {
                pstmt.setInt(index++, usuarioId);
            }
            if (operacao != null && !operacao.isEmpty()) {
                pstmt.setString(index++, operacao);
            }
            if (dataInicio != null && dataFim != null) {
                pstmt.setTimestamp(index++, Timestamp.valueOf(dataInicio));
                pstmt.setTimestamp(index, Timestamp.valueOf(dataFim));
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                logs.add(extrairDoResultSet(rs));
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao obter logs com filtros: " + e.getMessage());
        }
        
        return logs;
    }
    
    public Log obterPorId(Integer id) {
        String sql = "SELECT * FROM logs WHERE id = ?";
        
        try {
            Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Log log = extrairDoResultSet(rs);
                rs.close();
                pstmt.close();
                return log;
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao obter log por ID: " + e.getMessage());
        }
        
        return null;
    }
    
    public boolean deletar(Integer id) {
        String sql = "DELETE FROM logs WHERE id = ?";
        
        try {
            Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            boolean result = pstmt.executeUpdate() > 0;
            pstmt.close();
            return result;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar log: " + e.getMessage());
        }
        
        return false;
    }
    
    public boolean limparLogosAntigos(int diasRetencao) {
        String sql = "DELETE FROM logs WHERE data_hora < DATE_SUB(NOW(), INTERVAL ? DAY)";
        
        try {
            Connection conn = Conexao.getConexao();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, diasRetencao);
            
            boolean result = pstmt.executeUpdate() > 0;
            pstmt.close();
            return result;
        } catch (SQLException e) {
            System.err.println("Erro ao limpar logs antigos: " + e.getMessage());
        }
        
        return false;
    }
    
    private Log extrairDoResultSet(ResultSet rs) throws SQLException {
        Log log = new Log();
        log.setId(rs.getInt("id"));
        log.setUsuarioId(rs.getInt("usuario_id"));
        log.setNomeUsuario(rs.getString("nome_usuario"));
        log.setOperacao(rs.getString("operacao"));
        log.setDescricao(rs.getString("descricao"));
        log.setTabela(rs.getString("tabela"));
        log.setRegistroId(rs.getInt("registro_id"));
        log.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        log.setStatusOperacao(rs.getString("status_operacao"));
        return log;
    }
}
