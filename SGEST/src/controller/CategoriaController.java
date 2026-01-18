package controller;

import model.entity.Categoria;
import model.dao.CategoriaDAO;
import model.dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;

public class CategoriaController {
    private CategoriaDAO categoriaDAO;
    private LogController logController;
    private UsuarioDAO usuarioDAO;
    private Integer usuarioLogadoId;
    
    public CategoriaController(Integer usuarioLogadoId) {
        this.categoriaDAO = new CategoriaDAO();
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
    public boolean criarCategoria(String nome, String tipo, String descricao, String cor) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("Nome da categoria é obrigatório");
            }
            
            Categoria categoria = new Categoria(nome.trim(), tipo.toUpperCase(), 
                                               descricao, usuarioLogadoId, cor);
            categoriaDAO.criar(categoria);
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "CRIAR", 
                "Nova categoria criada: " + nome + " (" + tipo + ")", "Categoria", categoria.getId());
            return true;
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Erro ao criar categoria: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "CRIAR", 
                "Falha ao criar categoria: " + e.getMessage(), "Categoria", null, "FALHA");
            return false;
        }
    }
    
    public boolean atualizarCategoria(Integer id, String nome, String tipo, String descricao, String cor) {
        try {
            Categoria categoria = categoriaDAO.buscarPorId(id);
            if (categoria == null || !categoria.getUsuarioId().equals(usuarioLogadoId)) {
                return false;
            }
            
            categoria.setNome(nome);
            categoria.setTipo(tipo.toUpperCase());
            categoria.setDescricao(descricao);
            categoria.setCor(cor);
            
            categoriaDAO.atualizar(categoria);
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "ATUALIZAR", 
                "Categoria atualizada: " + nome, "Categoria", id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar categoria: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "ATUALIZAR", 
                "Falha ao atualizar categoria: " + e.getMessage(), "Categoria", id, "FALHA");
            return false;
        }
    }
    
    public boolean desativarCategoria(Integer id) {
        try {
            Categoria categoria = categoriaDAO.buscarPorId(id);
            if (categoria == null || !categoria.getUsuarioId().equals(usuarioLogadoId)) {
                return false;
            }
            
            categoriaDAO.desativar(id);
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "DELETAR", 
                "Categoria desativada: " + categoria.getNome(), "Categoria", id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao desativar categoria: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "DELETAR", 
                "Falha ao desativar categoria: " + e.getMessage(), "Categoria", id, "FALHA");
            return false;
        }
    }
    
    public List<Categoria> listarCategorias(String tipo) {
        try {
            return categoriaDAO.listarPorUsuario(usuarioLogadoId, tipo);
        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
            return List.of();
        }
    }
    
    public List<Categoria> listarCategoriasDespesa() {
        return listarCategorias("DESPESA");
    }
    
    public List<Categoria> listarCategoriasReceita() {
        return listarCategorias("RECEITA");
    }
    
    public List<String> listarNomesCategorias() {
        try {
            return categoriaDAO.listarNomesPorUsuario(usuarioLogadoId);
        } catch (SQLException e) {
            System.err.println("Erro ao listar nomes de categorias: " + e.getMessage());
            return List.of();
        }
    }
    
    public Categoria buscarCategoriaPorId(Integer id) {
        try {
            Categoria categoria = categoriaDAO.buscarPorId(id);
            if (categoria != null && categoria.getUsuarioId().equals(usuarioLogadoId)) {
                return categoria;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria: " + e.getMessage());
            return null;
        }
    }
    
    public List<Categoria> buscarCategoriasPorNome(String nome) {
        try {
            List<Categoria> todas = categoriaDAO.listarPorUsuario(usuarioLogadoId, null);
            return todas.stream()
                    .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar categorias: " + e.getMessage());
            return List.of();
        }
    }
}