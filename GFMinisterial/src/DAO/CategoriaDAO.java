package DAO;
/**
 *
 * @author ussene
 */
import Model.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {
    private Connection conexao;
    
    public CategoriaDAO(Connection conexao) {
        this.conexao = conexao;
    }
    
    // Criar categoria
    public boolean criar(Categoria categoria) {
        String sql = "INSERT INTO categorias (nome, tipo, descricao, usuario_id, cor) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.setString(3, categoria.getDescricao());
            stmt.setInt(4, categoria.getUsuarioId());
            stmt.setString(5, categoria.getCor());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao criar categoria: " + e.getMessage());
        }
        return false;
    }
    
    // Listar categorias do usuário por tipo
    public List<Categoria> listarPorUsuario(int usuarioId, String tipo) {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE usuario_id = ? AND tipo = ? AND ativo = TRUE ORDER BY nome";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, tipo);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                categorias.add(montarCategoria(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }
        return categorias;
    }
    
    // Buscar categoria por ID
    public Categoria buscarPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return montarCategoria(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria: " + e.getMessage());
        }
        return null;
    }
    
    private Categoria montarCategoria(ResultSet rs) throws SQLException {
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
