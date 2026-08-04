package model.dao;

import model.entity.Usuario;
import model.conexao.Conexao;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    // Formato usado pelo DEFAULT CURRENT_TIMESTAMP do SQLite (sem frações de segundo)
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Connection conexao;
    
    public UsuarioDAO() {
        this.conexao = Conexao.getConexao();
    }
    
    public UsuarioDAO(Connection conexao) {
        this.conexao = conexao;
    }
    
    // CRUD
    public void criar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, telefone, senha_hash, perfil, ativo) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getSenhaHash());
            stmt.setString(5, usuario.getPerfil());
            stmt.setBoolean(6, usuario.getAtivo());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
        }
    }
    
    public Usuario buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }
        return null;
    }
    
    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        }
        return null;
    }
    
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }
    
    public List<Usuario> listarAtivos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE ativo = TRUE ORDER BY nome";
        
        try (Statement stmt = conexao.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }
    
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, telefone = ?, perfil = ?, ativo = ? "
                   + "WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getPerfil());
            stmt.setBoolean(5, usuario.getAtivo());
            stmt.setInt(6, usuario.getId());
            
            stmt.executeUpdate();
        }
    }
    
    public void atualizarSenha(Integer id, String novaSenhaHash) throws SQLException {
        String sql = "UPDATE usuarios SET senha_hash = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, novaSenhaHash);
            stmt.setInt(2, id);
            
            stmt.executeUpdate();
        }
    }
    
    public void desativar(Integer id) throws SQLException {
        String sql = "UPDATE usuarios SET ativo = FALSE WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public void ativar(Integer id) throws SQLException {
        String sql = "UPDATE usuarios SET ativo = TRUE WHERE id = ?";
        
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setSenhaHash(rs.getString("senha_hash"));
        usuario.setPerfil(rs.getString("perfil"));
        usuario.setAtivo(rs.getBoolean("ativo"));
        usuario.setDataCadastro(java.time.LocalDateTime.parse(rs.getString("data_cadastro"), FORMATO_DATA_HORA));
        return usuario;
    }
}
