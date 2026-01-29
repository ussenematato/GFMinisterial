package controller;

import model.entity.Transacao;
import model.dao.TransacaoDAO;
import model.dao.ContaDAO;
import model.conexao.Conexao;
import model.dao.UsuarioDAO;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class TransacaoController {

    private TransacaoDAO transacaoDAO;
    private ContaDAO contaDAO;
    private LogController logController;
    private UsuarioDAO usuarioDAO;
    private Integer usuarioLogadoId;

    public TransacaoController(Integer usuarioLogadoId) {
        this.transacaoDAO = new TransacaoDAO();
        this.contaDAO = new ContaDAO();
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
    public boolean registrarTransacao(String descricao, BigDecimal valor, String tipo,
            LocalDate dataTransacao, Integer contaId,
            Integer categoriaId, String observacoes) {
        try {
            // Validações de negócio
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor deve ser maior que zero");
            }

            if ("DESPESA".equalsIgnoreCase(tipo)) {
                // Verificar se há saldo suficiente
                var conta = contaDAO.buscarPorId(contaId);
                if (conta != null && conta.getSaldoAtual().compareTo(valor) < 0) {
                    throw new IllegalStateException("Saldo insuficiente na conta");
                }
            }

            Transacao transacao = new Transacao(descricao, valor, tipo.toUpperCase(),
                    dataTransacao, contaId, categoriaId,
                    usuarioLogadoId);
            transacao.setObservacoes(observacoes);
            transacao.setPago("RECEITA".equalsIgnoreCase(tipo)); // Receitas são pagas automaticamente

            transacaoDAO.criar(transacao);
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "CRIAR", 
                "Transação registrada: " + descricao + " (" + tipo + ") - " + valor, "Transacao", transacao.getId());

            // Atualizar saldo da conta
            // Para RECEITA: deduz imediatamente (pago automaticamente)
            // Para DESPESA: só deduz quando for marcada como paga
            if ("RECEITA".equalsIgnoreCase(tipo)) {
                atualizarSaldoConta(contaId, valor, tipo);
            }

            return true;
        } catch (SQLException | IllegalArgumentException | IllegalStateException e) {
            System.err.println("Erro ao registrar transação: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "CRIAR", 
                "Falha ao registrar transação: " + e.getMessage(), "Transacao", null, "FALHA");
            return false;
        }
    }

    public boolean atualizarTransacao(Integer id, String descricao, BigDecimal valor,
            LocalDate dataTransacao, Integer contaId,
            Integer categoriaId, String observacoes) {
        try {
            Transacao transacao = transacaoDAO.buscarPorId(id);
            if (transacao == null || !transacao.getUsuarioId().equals(usuarioLogadoId)) {
                return false;
            }

            // Reverter saldo antigo (apenas se transação estava paga ou é receita)
            BigDecimal valorAntigo = transacao.getValor();
            String tipoAntigo = transacao.getTipo();
            boolean transacaoAntigaPaga = transacao.getPago();
            
            if ("RECEITA".equals(tipoAntigo) || transacaoAntigaPaga) {
                atualizarSaldoConta(transacao.getContaId(), valorAntigo,
                        "DESPESA".equals(tipoAntigo) ? "RECEITA" : "DESPESA");
            }

            // Atualizar transação
            transacao.setDescricao(descricao);
            transacao.setValor(valor);
            transacao.setDataTransacao(dataTransacao);
            transacao.setContaId(contaId);
            transacao.setCategoriaId(categoriaId);
            transacao.setObservacoes(observacoes);

            transacaoDAO.atualizar(transacao);
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "ATUALIZAR", 
                "Transação atualizada: " + descricao, "Transacao", id);

            // Aplicar novo saldo (apenas se transação está paga ou é receita)
            if ("RECEITA".equals(transacao.getTipo()) || transacaoAntigaPaga) {
                atualizarSaldoConta(contaId, valor, transacao.getTipo());
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar transação: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "ATUALIZAR", 
                "Falha ao atualizar transação: " + e.getMessage(), "Transacao", id, "FALHA");
            return false;
        }
    }

    public boolean registrarTransferencia(String descricao, BigDecimal valor, LocalDate dataTransacao,
            Integer contaOrigemId, Integer contaDestinoId,
            String observacoes) {
        Connection conn = null;
        try {
            conn = Conexao.getConexao();
            conn.setAutoCommit(false);

            TransacaoDAO transacaoDAO = new TransacaoDAO(conn);
            ContaDAO contaDAO = new ContaDAO(conn);

            // Verificar saldo
            var contaOrigem = contaDAO.buscarPorId(contaOrigemId);
            if (contaOrigem == null || contaOrigem.getSaldoAtual().compareTo(valor) < 0) {
                throw new IllegalStateException("Saldo insuficiente na conta de origem");
            }

            // Criar transação de transferência (origem) - tipo TRANSFERENCIA, categoria NULL
            Transacao origem = new Transacao(descricao, valor, "TRANSFERENCIA",
                    dataTransacao, contaOrigemId, null, usuarioLogadoId);
            origem.setPago(true);
            origem.setObservacoes("Transferência para " + contaDestinoId + ". " + observacoes);

            transacaoDAO.criar(origem);

            // Usar id gerado como grupo de transferência
            Integer grupo = origem.getId();
            origem.setTransferenciaId(grupo);
            transacaoDAO.atualizar(origem);

            // Criar transação de transferência (destino) - mesmo tipo, categoria NULL
            Transacao destino = new Transacao(descricao, valor, "TRANSFERENCIA",
                    dataTransacao, contaDestinoId, null, usuarioLogadoId);
            destino.setPago(true);
            destino.setObservacoes("Transferência recebida de " + contaOrigemId + ". " + observacoes);
            destino.setTransferenciaId(grupo);

            transacaoDAO.criar(destino);

            // Atualizar saldos
            var contaDestino = contaDAO.buscarPorId(contaDestinoId);
            contaDAO.atualizarSaldo(contaOrigemId, contaOrigem.getSaldoAtual().subtract(valor));
            contaDAO.atualizarSaldo(contaDestinoId, contaDestino.getSaldoAtual().add(valor));

            conn.commit();

            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "CRIAR",
                    "Transferência: " + descricao + " - " + valor, "Transacao", grupo);

            return true;
        } catch (SQLException | IllegalStateException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                // ignore
            }
            System.err.println("Erro ao registrar transferência: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "CRIAR",
                    "Falha ao registrar transferência: " + e.getMessage(), "Transacao", null, "FALHA");
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ex) {
                // ignore
            }
        }
    }

    public boolean excluirTransacao(Integer id) {
        try {
            Transacao transacao = transacaoDAO.buscarPorId(id);
            if (transacao == null || !transacao.getUsuarioId().equals(usuarioLogadoId)) {
                return false;
            }

            // Reverter saldo apenas se transação estava paga ou é receita
            if ("RECEITA".equals(transacao.getTipo()) || transacao.getPago()) {
                atualizarSaldoConta(transacao.getContaId(), transacao.getValor(),
                        "DESPESA".equals(transacao.getTipo()) ? "RECEITA" : "DESPESA");
            }

            transacaoDAO.excluir(id);
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "DELETAR", 
                "Transação deletada: " + transacao.getDescricao(), "Transacao", id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir transação: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "DELETAR", 
                "Falha ao deletar transação: " + e.getMessage(), "Transacao", id, "FALHA");
            return false;
        }
    }

    public boolean marcarComoPago(Integer id) {
        try {
            Transacao transacao = transacaoDAO.buscarPorId(id);
            if (transacao == null || !transacao.getUsuarioId().equals(usuarioLogadoId)) {
                return false;
            }

            if (!transacao.getPago()) {
                transacaoDAO.marcarComoPago(id);
                logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "ATUALIZAR", 
                    "Transação marcada como paga: " + transacao.getDescricao(), "Transacao", id);

                // Atualizar saldo APENAS para despesas quando marcadas como pagas
                // Receitas já foram contabilizadas ao criar
                if ("DESPESA".equals(transacao.getTipo())) {
                    atualizarSaldoConta(transacao.getContaId(), transacao.getValor(), "DESPESA");
                }
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao marcar como pago: " + e.getMessage());
            logController.registrarOperacao(usuarioLogadoId, obterNomeUsuario(), "ATUALIZAR", 
                "Falha ao marcar como pago: " + e.getMessage(), "Transacao", id, "FALHA");
            return false;
        }
    }

    public boolean desmarcarComoPago(Integer id) {
    try {
        Transacao transacao = transacaoDAO.buscarPorId(id);
        if (transacao == null || !transacao.getUsuarioId().equals(usuarioLogadoId)) {
            return false;
        }
        
        // Só desmarcar se estiver pago
        if (transacao.getPago()) {
            transacaoDAO.desmarcarComoPago(id);
            
            // Se for despesa, reverter o saldo (adicionar de volta)
            if ("DESPESA".equals(transacao.getTipo())) {
                atualizarSaldoConta(transacao.getContaId(), transacao.getValor(), "RECEITA");
            }
        }
        
        return true;
    } catch (SQLException e) {
        System.err.println("Erro ao desmarcar como pago: " + e.getMessage());
        return false;
    }
}

    // Consultas
    public List<Transacao> listarTransacoesPeriodo(LocalDate inicio, LocalDate fim) {
        try {
            return transacaoDAO.listarPorUsuario(usuarioLogadoId, inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao listar transações: " + e.getMessage());
            return List.of();
        }
    }

    public List<Transacao> listarDespesasPeriodo(LocalDate inicio, LocalDate fim) {
        try {
            return transacaoDAO.listarPorTipo(usuarioLogadoId, "DESPESA", inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao listar despesas: " + e.getMessage());
            return List.of();
        }
    }

    public List<Transacao> listarReceitasPeriodo(LocalDate inicio, LocalDate fim) {
        try {
            return transacaoDAO.listarPorTipo(usuarioLogadoId, "RECEITA", inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao listar receitas: " + e.getMessage());
            return List.of();
        }
    }

    public Transacao buscarTransacaoPorId(Integer id) {
        try {
            Transacao transacao = transacaoDAO.buscarPorId(id);
            if (transacao != null && transacao.getUsuarioId().equals(usuarioLogadoId)) {
                return transacao;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar transação: " + e.getMessage());
            return null;
        }
    }

    // Métodos de relatório
    public BigDecimal obterTotalDespesas(LocalDate inicio, LocalDate fim) {
        try {
            Double total = transacaoDAO.obterTotalPorTipo(usuarioLogadoId, "DESPESA", inicio, fim);
            return BigDecimal.valueOf(total != null ? total : 0.0);
        } catch (SQLException e) {
            System.err.println("Erro ao obter total despesas: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal obterTotalReceitas(LocalDate inicio, LocalDate fim) {
        try {
            Double total = transacaoDAO.obterTotalPorTipo(usuarioLogadoId, "RECEITA", inicio, fim);
            return BigDecimal.valueOf(total != null ? total : 0.0);
        } catch (SQLException e) {
            System.err.println("Erro ao obter total receitas: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal obterSaldoPeriodo(LocalDate inicio, LocalDate fim) {
        BigDecimal receitas = obterTotalReceitas(inicio, fim);
        BigDecimal despesas = obterTotalDespesas(inicio, fim);
        return receitas.subtract(despesas);
    }

    public List<Object[]> obterDespesasPorCategoria(LocalDate inicio, LocalDate fim) {
        try {
            return transacaoDAO.obterDespesasPorCategoria(usuarioLogadoId, inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao obter despesas por categoria: " + e.getMessage());
            return List.of();
        }
    }

    // Método auxiliar privado
    private void atualizarSaldoConta(Integer contaId, BigDecimal valor, String tipo) throws SQLException {
        var conta = contaDAO.buscarPorId(contaId);
        if (conta != null && conta.getUsuarioId().equals(usuarioLogadoId)) {
            BigDecimal novoSaldo = conta.getSaldoAtual();

            if ("DESPESA".equalsIgnoreCase(tipo)) {
                novoSaldo = novoSaldo.subtract(valor);
            } else if ("RECEITA".equalsIgnoreCase(tipo)) {
                novoSaldo = novoSaldo.add(valor);
            }

            contaDAO.atualizarSaldo(contaId, novoSaldo);
        }
    }

    // NOVO: Obter transações do dia
    public List<Transacao> obterTransacoesHoje() {
        LocalDate hoje = LocalDate.now();
        return listarTransacoesPeriodo(hoje, hoje);
    }

    // NOVO: Obter transações da semana
    public List<Transacao> obterTransacoesEstaSemana() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
        return listarTransacoesPeriodo(inicioSemana, hoje);
    }

    // NOVO: Obter transações deste mês
    public List<Transacao> obterTransacoesEsteMes() {
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        return listarTransacoesPeriodo(inicio, fim);
    }

    // NOVO: Obter resumo rápido
    public String obterResumoRapido() {
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();

        BigDecimal receitas = obterTotalReceitas(inicio, fim);
        BigDecimal despesas = obterTotalDespesas(inicio, fim);
        BigDecimal saldo = receitas.subtract(despesas);

        return String.format(
                "📊 Resumo do Mês\n"
                + "━━━━━━━━━━━━━━━━━━━━\n"
                + "💰 Receitas: MT %,.2f\n"
                + "💸 Despesas: MT %,.2f\n"
                + "📈 Saldo: MT %,.2f\n"
                + "━━━━━━━━━━━━━━━━━━━━",
                receitas, despesas, saldo
        );
    }

    // NOVO: Verificar alertas
    public List<Transacao> obterTransacoesVencidas() {
        try {
            LocalDate hoje = LocalDate.now();
            List<Transacao> todas = transacaoDAO.listarPorUsuario(usuarioLogadoId,
                    hoje.minusDays(30), hoje);

            return todas.stream()
                    .filter(t -> "DESPESA".equals(t.getTipo()))
                    .filter(t -> !t.getPago())
                    .filter(t -> t.getDataVencimento().isBefore(hoje))
                    .toList();
        } catch (SQLException e) {
            System.err.println("Erro ao obter transações vencidas: " + e.getMessage());
            return List.of();
        }
    }

    // NOVO: Obter receitas por categoria
    public List<Object[]> obterReceitasPorCategoria(LocalDate inicio, LocalDate fim) {
        try {
            return transacaoDAO.obterReceitasPorCategoria(usuarioLogadoId, inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao obter receitas por categoria: " + e.getMessage());
            return List.of();
        }
    }

    // Métodos para TODAS as transações (compartilhadas entre usuários)
    public List<Transacao> listarTodasTransacoesPeriodo(LocalDate inicio, LocalDate fim) {
        try {
            return transacaoDAO.listarTodasTransacoes(inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao listar todas as transações: " + e.getMessage());
            return List.of();
        }
    }

    public BigDecimal obterTotalTodasReceitas(LocalDate inicio, LocalDate fim) {
        try {
            Double total = transacaoDAO.obterTotalTodosReceitas(inicio, fim);
            return BigDecimal.valueOf(total != null ? total : 0.0);
        } catch (SQLException e) {
            System.err.println("Erro ao obter total de receitas: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal obterTotalTodasDespesas(LocalDate inicio, LocalDate fim) {
        try {
            Double total = transacaoDAO.obterTotalTodasDespesas(inicio, fim);
            return BigDecimal.valueOf(total != null ? total : 0.0);
        } catch (SQLException e) {
            System.err.println("Erro ao obter total de despesas: " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    public List<Object[]> obterTodasReceitasPorCategoria(LocalDate inicio, LocalDate fim) {
        try {
            return transacaoDAO.obterTodasReceitas(inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao obter receitas por categoria: " + e.getMessage());
            return List.of();
        }
    }

    public List<Object[]> obterTodasDespesasPorCategoria(LocalDate inicio, LocalDate fim) {
        try {
            return transacaoDAO.obterTodasDespesas(inicio, fim);
        } catch (SQLException e) {
            System.err.println("Erro ao obter despesas por categoria: " + e.getMessage());
            return List.of();
        }
    }
}
