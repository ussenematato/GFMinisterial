package model.dao;

import model.entity.Categoria;
import model.conexao.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private Connection conexao;
    
    public CategoriaDAO() {
        this.conexao = Conexao.getConexao();
    }
    
    public CategoriaDAO(Connection conexao) {
        this.conexao = conexao;
    }
    
    // CRUD
    public void criar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nome, tipo, descricao, usuario_id, cor, ativo) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.setString(3, categoria.getDescricao());
            stmt.setInt(4, categoria.getUsuarioId());
            stmt.setString(5, categoria.getCor());
            stmt.setBoolean(6, categoria.getAtivo());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                categoria.setId(rs.getInt(1));
            }
        }
    }
    
    public Categoria buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearCategoria(rs);
            }
        }
        return null;
    }
    
    public List<Categoria> listarPorUsuario(Integer usuarioId, String tipo) throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM categorias WHERE usuario_id = ? AND ativo = TRUE");
        
        if (tipo != null && !tipo.isEmpty() && !tipo.equals("TODOS")) {
            sql.append(" AND tipo = ?");
        }
        sql.append(" ORDER BY nome");
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql.toString())) {
            stmt.setInt(1, usuarioId);
            if (tipo != null && !tipo.isEmpty() && !tipo.equals("TODOS")) {
                stmt.setString(2, tipo);
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }
        }
        return categorias;
    }
    
    public void atualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categorias SET nome = ?, tipo = ?, descricao = ?, "
                   + "cor = ?, ativo = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.setString(3, categoria.getDescricao());
            stmt.setString(4, categoria.getCor());
            stmt.setBoolean(5, categoria.getAtivo());
            stmt.setInt(6, categoria.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void desativar(Integer id) throws SQLException {
        String sql = "UPDATE categorias SET ativo = FALSE WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public List<String> listarNomesPorUsuario(Integer usuarioId) throws SQLException {
        List<String> nomes = new ArrayList<>();
        String sql = "SELECT nome FROM categorias WHERE usuario_id = ? AND ativo = TRUE ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                nomes.add(rs.getString("nome"));
            }
        }
        return nomes;
    }
    
    private Categoria mapearCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id"));
        categoria.setNome(rs.getString("nome"));
        categoria.setTipo(rs.getString("tipo"));
        categoria.setDescricao(rs.getString("descricao"));
        categoria.setUsuarioId(rs.getInt("usuario_id"));
        categoria.setCor(rs.getString("cor"));
        categoria.setAtivo(rs.getBoolean("ativo"));
        return categoria;
    }
}