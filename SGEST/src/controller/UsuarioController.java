package controller;

import model.entity.Usuario;
import model.dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    // Validação de email
    public static boolean isEmailValido(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    // Validação de senha
    public static String validarSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            return "Senha não pode estar vazia";
        }
        if (senha.length() < 6) {
            return "Senha deve ter no mínimo 6 caracteres";
        }
        return null;
    }
    
    // Hash da senha (simple MD5 - em produção usar bcrypt ou similar)
    public static String hashSenha(String senha) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    // Verificar senha
    public static boolean verificarSenha(String senha, String senhaHash) {
        return hashSenha(senha).equals(senhaHash);
    }
    
    // Operações
    public boolean criarUsuario(String nome, String email, String telefone, String senha, String perfil) {
        try {
            // Validações
            if (nome == null || nome.trim().isEmpty()) {
                System.err.println("Nome é obrigatório");
                return false;
            }
            
            if (!isEmailValido(email)) {
                System.err.println("Email inválido");
                return false;
            }
            
            if (usuarioDAO.buscarPorEmail(email) != null) {
                System.err.println("Email já cadastrado");
                return false;
            }
            
            String erroSenha = validarSenha(senha);
            if (erroSenha != null) {
                System.err.println(erroSenha);
                return false;
            }
            
            if (perfil == null || perfil.isEmpty()) {
                perfil = "TESOUREIRO";
            }
            
            String senhaHash = hashSenha(senha);
            Usuario usuario = new Usuario(nome, email, telefone, senhaHash, perfil);
            usuarioDAO.criar(usuario);
            
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
            return false;
        }
    }
    
    public boolean atualizarUsuario(Integer id, String nome, String email, String telefone, String perfil) {
        try {
            Usuario usuario = usuarioDAO.buscarPorId(id);
            if (usuario == null) {
                return false;
            }
            
            if (nome != null && !nome.trim().isEmpty()) {
                usuario.setNome(nome);
            }
            
            if (email != null && !email.isEmpty()) {
                if (!isEmailValido(email)) {
                    System.err.println("Email inválido");
                    return false;
                }
                Usuario usuarioEmail = usuarioDAO.buscarPorEmail(email);
                if (usuarioEmail != null && !usuarioEmail.getId().equals(id)) {
                    System.err.println("Email já cadastrado");
                    return false;
                }
                usuario.setEmail(email);
            }
            
            if (telefone != null) {
                usuario.setTelefone(telefone);
            }
            
            if (perfil != null && !perfil.isEmpty()) {
                usuario.setPerfil(perfil);
            }
            
            usuarioDAO.atualizar(usuario);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }
    
    public boolean atualizarSenha(Integer id, String novaSenha) {
        try {
            String erroSenha = validarSenha(novaSenha);
            if (erroSenha != null) {
                System.err.println(erroSenha);
                return false;
            }
            
            String senhaHash = hashSenha(novaSenha);
            usuarioDAO.atualizarSenha(id, senhaHash);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar senha: " + e.getMessage());
            return false;
        }
    }
    
    public boolean desativarUsuario(Integer id) {
        try {
            usuarioDAO.desativar(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao desativar usuário: " + e.getMessage());
            return false;
        }
    }
    
    public boolean ativarUsuario(Integer id) {
        try {
            usuarioDAO.ativar(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao ativar usuário: " + e.getMessage());
            return false;
        }
    }
    
    public List<Usuario> listarUsuarios() {
        try {
            return usuarioDAO.listarTodos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários: " + e.getMessage());
            return List.of();
        }
    }
    
    public List<Usuario> listarUsuariosAtivos() {
        try {
            return usuarioDAO.listarAtivos();
        } catch (SQLException e) {
            System.err.println("Erro ao listar usuários ativos: " + e.getMessage());
            return List.of();
        }
    }
    
    public Usuario buscarPorId(Integer id) {
        try {
            return usuarioDAO.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
            return null;
        }
    }
}
