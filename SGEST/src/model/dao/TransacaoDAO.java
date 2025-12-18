package model.dao;

import model.entity.Transacao;
import model.conexao.Conexao;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    private Connection conexao;

    public TransacaoDAO() {
        this.conexao = Conexao.getConexao();
    }

    public TransacaoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // CRUD
    public void criar(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO transacoes (descricao, valor, tipo, data_transacao, "
                + "data_vencimento, pago, recorrente, frequencia, conta_id, "
                + "categoria_id, usuario_id, observacoes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, transacao.getDescricao());
            stmt.setBigDecimal(2, transacao.getValor());
            stmt.setString(3, transacao.getTipo());
            stmt.setDate(4, Date.valueOf(transacao.getDataTransacao()));
            stmt.setDate(5, Date.valueOf(transacao.getDataVencimento()));
            stmt.setBoolean(6, transacao.getPago());
            stmt.setBoolean(7, transacao.getRecorrente());
            stmt.setString(8, transacao.getFrequencia());
            stmt.setInt(9, transacao.getContaId());
            stmt.setInt(10, transacao.getCategoriaId());
            stmt.setInt(11, transacao.getUsuarioId());
            stmt.setString(12, transacao.getObservacoes());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transacao.setId(rs.getInt(1));
            }
        }
    }

    public Transacao buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT t.*, c.nome as nome_conta, cat.nome as nome_categoria, cat.cor as cor_categoria "
                + "FROM transacoes t "
                + "LEFT JOIN contas c ON t.conta_id = c.id "
                + "LEFT JOIN categorias cat ON t.categoria_id = cat.id "
                + "WHERE t.id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearTransacao(rs);
            }
        }
        return null;
    }

    public List<Transacao> listarPorUsuario(Integer usuarioId, LocalDate inicio, LocalDate fim) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT t.*, c.nome as nome_conta, cat.nome as nome_categoria, cat.cor as cor_categoria "
                + "FROM transacoes t "
                + "LEFT JOIN contas c ON t.conta_id = c.id "
                + "LEFT JOIN categorias cat ON t.categoria_id = cat.id "
                + "WHERE t.usuario_id = ? AND t.data_transacao BETWEEN ? AND ? "
                + "ORDER BY t.data_transacao DESC, t.id DESC";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(inicio));
            stmt.setDate(3, Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transacoes.add(mapearTransacao(rs));
            }
        }
        return transacoes;
    }

    public List<Transacao> listarPorTipo(Integer usuarioId, String tipo, LocalDate inicio, LocalDate fim) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT t.*, c.nome as nome_conta, cat.nome as nome_categoria, cat.cor as cor_categoria "
                + "FROM transacoes t "
                + "LEFT JOIN contas c ON t.conta_id = c.id "
                + "LEFT JOIN categorias cat ON t.categoria_id = cat.id "
                + "WHERE t.usuario_id = ? AND t.tipo = ? AND t.data_transacao BETWEEN ? AND ? "
                + "ORDER BY t.data_transacao DESC";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, tipo);
            stmt.setDate(3, Date.valueOf(inicio));
            stmt.setDate(4, Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transacoes.add(mapearTransacao(rs));
            }
        }
        return transacoes;
    }

    public void atualizar(Transacao transacao) throws SQLException {
        String sql = "UPDATE transacoes SET descricao = ?, valor = ?, tipo = ?, "
                + "data_transacao = ?, data_vencimento = ?, pago = ?, "
                + "recorrente = ?, frequencia = ?, conta_id = ?, "
                + "categoria_id = ?, observacoes = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, transacao.getDescricao());
            stmt.setBigDecimal(2, transacao.getValor());
            stmt.setString(3, transacao.getTipo());
            stmt.setDate(4, Date.valueOf(transacao.getDataTransacao()));
            stmt.setDate(5, Date.valueOf(transacao.getDataVencimento()));
            stmt.setBoolean(6, transacao.getPago());
            stmt.setBoolean(7, transacao.getRecorrente());
            stmt.setString(8, transacao.getFrequencia());
            stmt.setInt(9, transacao.getContaId());
            stmt.setInt(10, transacao.getCategoriaId());
            stmt.setString(11, transacao.getObservacoes());
            stmt.setInt(12, transacao.getId());

            stmt.executeUpdate();
        }
    }

    public void excluir(Integer id) throws SQLException {
        String sql = "DELETE FROM transacoes WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void marcarComoPago(Integer id) throws SQLException {
        String sql = "UPDATE transacoes SET pago = TRUE WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void desmarcarComoPago(Integer id) throws SQLException {
        String sql = "UPDATE transacoes SET pago = FALSE WHERE id = ?";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Métodos de agregação
    public Double obterTotalPorTipo(Integer usuarioId, String tipo, LocalDate inicio, LocalDate fim) throws SQLException {
        String sql = "SELECT SUM(valor) as total FROM transacoes "
                + "WHERE usuario_id = ? AND tipo = ? AND data_transacao BETWEEN ? AND ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, tipo);
            stmt.setDate(3, Date.valueOf(inicio));
            stmt.setDate(4, Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }

    public List<Object[]> obterDespesasPorCategoria(Integer usuarioId, LocalDate inicio, LocalDate fim) throws SQLException {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT cat.nome, SUM(t.valor) as total, COUNT(t.id) as quantidade "
                + "FROM transacoes t "
                + "JOIN categorias cat ON t.categoria_id = cat.id "
                + "WHERE t.usuario_id = ? AND t.tipo = 'DESPESA' "
                + "AND t.data_transacao BETWEEN ? AND ? "
                + "GROUP BY cat.nome "
                + "ORDER BY total DESC";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setDate(2, Date.valueOf(inicio));
            stmt.setDate(3, Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] linha = new Object[3];
                linha[0] = rs.getString("nome");
                linha[1] = rs.getDouble("total");
                linha[2] = rs.getLong("quantidade");
                resultados.add(linha);
            }
        }
        return resultados;
    }

    public List<Transacao> listarTransacoesRecentes(Integer usuarioId, int limite) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT t.*, c.nome as nome_conta, cat.nome as nome_categoria, cat.cor as cor_categoria "
                + "FROM transacoes t "
                + "LEFT JOIN contas c ON t.conta_id = c.id "
                + "LEFT JOIN categorias cat ON t.categoria_id = cat.id "
                + "WHERE t.usuario_id = ? "
                + "ORDER BY t.data_transacao DESC, t.id DESC "
                + "LIMIT ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setInt(2, limite);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transacoes.add(mapearTransacao(rs));
            }
        }
        return transacoes;
    }

    private Transacao mapearTransacao(ResultSet rs) throws SQLException {
        Transacao transacao = new Transacao();
        transacao.setId(rs.getInt("id"));
        transacao.setDescricao(rs.getString("descricao"));
        transacao.setValor(rs.getBigDecimal("valor"));
        transacao.setTipo(rs.getString("tipo"));

        // Tratar datas nulas
        Date dataTransacao = rs.getDate("data_transacao");
        if (dataTransacao != null) {
            transacao.setDataTransacao(dataTransacao.toLocalDate());
        }

        Date dataVencimento = rs.getDate("data_vencimento");
        if (dataVencimento != null) {
            transacao.setDataVencimento(dataVencimento.toLocalDate());
        }

        transacao.setPago(rs.getBoolean("pago"));
        transacao.setRecorrente(rs.getBoolean("recorrente"));
        transacao.setFrequencia(rs.getString("frequencia"));
        transacao.setContaId(rs.getInt("conta_id"));
        transacao.setCategoriaId(rs.getInt("categoria_id"));
        transacao.setUsuarioId(rs.getInt("usuario_id"));
        transacao.setObservacoes(rs.getString("observacoes"));

        // Tratar data de registro (pode ser nula)
        Timestamp dataRegistro = rs.getTimestamp("data_registro");
        if (dataRegistro != null) {
            transacao.setDataRegistro(dataRegistro.toLocalDateTime());
        }

        // Dados das joins
        transacao.setNomeConta(rs.getString("nome_conta"));
        transacao.setNomeCategoria(rs.getString("nome_categoria"));
        transacao.setCorCategoria(rs.getString("cor_categoria"));

        return transacao;
    }
}
