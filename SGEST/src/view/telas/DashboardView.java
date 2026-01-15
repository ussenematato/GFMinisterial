package view.telas;

import controller.ContaController;
import controller.CategoriaController;
import controller.TransacaoController;
import model.entity.Conta;
import model.entity.Transacao;
import util.CurrencyUtils;
import util.UIStyler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardView extends JFrame {
    private Integer usuarioId;
    private ContaController contaController;
    private CategoriaController categoriaController;
    private TransacaoController transacaoController;
    
    // Componentes
    private JLabel lblSaldoTotal;
    private JLabel lblReceitasMes;
    private JLabel lblDespesasMes;
    private JLabel lblSaldoMes;
    private JTable tblUltimasTransacoes;
    private JTable tblContas;
    private DefaultTableModel modelTransacoes;
    private DefaultTableModel modelContas;
    
    public DashboardView(Integer usuarioId) {
        this.usuarioId = usuarioId;
        this.contaController = new ContaController(usuarioId);
        this.categoriaController = new CategoriaController(usuarioId);
        this.transacaoController = new TransacaoController(usuarioId);
        
        initComponents();
        carregarDados();
        configurarEventos();
    }
    
    private void initComponents() {
        setTitle("Dashboard - Gestão Financeira");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Painel superior - Resumo
        JPanel panelResumo = criarPanelResumo();
        
        // Painel central - Contas e Transações
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Painel de Contas
        JPanel panelContas = criarPanelContas();
        
        // Painel de Transações Recentes
        JPanel panelTransacoes = criarPanelTransacoes();
        
        panelCentral.add(panelContas);
        panelCentral.add(panelTransacoes);
        
        // Painel inferior - Botões de ação
        JPanel panelBotoes = criarPanelBotoes();
        
        // Adicionando componentes ao frame
        add(panelResumo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
        
        // Configurações da janela
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private JPanel criarPanelResumo() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Resumo Financeiro"));
        panel.setBackground(new Color(240, 240, 240));
        
        // Labels
        JLabel lblTituloSaldo = new JLabel("Saldo Total:");
        lblTituloSaldo.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblSaldoTotal = new JLabel("MT 0,00");
        lblSaldoTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblSaldoTotal.setForeground(new Color(0, 100, 0));
        
        JLabel lblTituloReceitas = new JLabel("Receitas do Mês:");
        lblTituloReceitas.setFont(new Font("Arial", Font.PLAIN, 12));
        
        lblReceitasMes = new JLabel("MT 0,00");
        lblReceitasMes.setFont(new Font("Arial", Font.BOLD, 16));
        lblReceitasMes.setForeground(new Color(0, 150, 0));
        
        JLabel lblTituloDespesas = new JLabel("Despesas do Mês:");
        lblTituloDespesas.setFont(new Font("Arial", Font.PLAIN, 12));
        
        lblDespesasMes = new JLabel("MT 0,00");
        lblDespesasMes.setFont(new Font("Arial", Font.BOLD, 16));
        lblDespesasMes.setForeground(new Color(200, 0, 0));
        
        JLabel lblTituloSaldoMes = new JLabel("Saldo do Mês:");
        lblTituloSaldoMes.setFont(new Font("Arial", Font.PLAIN, 12));
        
        lblSaldoMes = new JLabel("MT 0,00");
        lblSaldoMes.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Adicionando ao painel
        panel.add(lblTituloSaldo);
        panel.add(lblSaldoTotal);
        panel.add(lblTituloReceitas);
        panel.add(lblReceitasMes);
        panel.add(new JLabel()); // Espaço vazio
        panel.add(new JLabel()); // Espaço vazio
        panel.add(lblTituloDespesas);
        panel.add(lblDespesasMes);
        panel.add(new JLabel()); // Espaço vazio
        panel.add(new JLabel()); // Espaço vazio
        panel.add(lblTituloSaldoMes);
        panel.add(lblSaldoMes);
        
        return panel;
    }
    
    private JPanel criarPanelContas() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Contas Bancárias"));
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Tipo", "Saldo", "Instituição"};
        modelContas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblContas = new JTable(modelContas);
        tblContas.setRowHeight(25);
        tblContas.getColumnModel().getColumn(2).setCellRenderer(new SaldoCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblContas);
        
        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNovaConta = new JButton("Nova Conta");
        JButton btnEditarConta = new JButton("Editar");
        JButton btnExcluirConta = new JButton("Excluir");
        
        panelBotoes.add(btnNovaConta);
        panelBotoes.add(btnEditarConta);
        panelBotoes.add(btnExcluirConta);
        
        // Adicionando ao painel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotoes, BorderLayout.SOUTH);
        
        // Ações dos botões
        btnNovaConta.addActionListener(e -> abrirCadastroConta());
        btnEditarConta.addActionListener(e -> editarConta());
        btnExcluirConta.addActionListener(e -> excluirConta());
        
        return panel;
    }
    
    private JPanel criarPanelTransacoes() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Últimas Transações"));
        
        // Modelo da tabela
        String[] colunas = {"Data", "Descrição", "Categoria", "Valor", "Status"};
        modelTransacoes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) return BigDecimal.class;
                return String.class;
            }
        };
        
        tblUltimasTransacoes = new JTable(modelTransacoes);
        tblUltimasTransacoes.setRowHeight(25);
        tblUltimasTransacoes.getColumnModel().getColumn(3).setCellRenderer(new ValorCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblUltimasTransacoes);
        
        // Botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNovaTransacao = new JButton("Nova Transação");
        JButton btnMarcarPago = new JButton("Marcar como Pago");
        JButton btnVerTodas = new JButton("Ver Todas");
        
        panelBotoes.add(btnNovaTransacao);
        panelBotoes.add(btnMarcarPago);
        panelBotoes.add(btnVerTodas);
        
        // Adicionando ao painel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelBotoes, BorderLayout.SOUTH);
        
        // Ações dos botões
        btnNovaTransacao.addActionListener(e -> abrirCadastroTransacao());
        btnMarcarPago.addActionListener(e -> marcarComoPago());
        btnVerTodas.addActionListener(e -> abrirRelatorioTransacoes());
        
        return panel;
    }
    
    private JPanel criarPanelBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        
        JButton btnMenuCompleto = new JButton("Menu Completo");
        JButton btnGerenciarCategorias = new JButton("Gerenciar Categorias");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnSair = new JButton("Sair");
        
        // Estilização usando UIStyler
        UIStyler.stylePrimaryButton(btnMenuCompleto);
        UIStyler.styleSecondaryButton(btnGerenciarCategorias);
        UIStyler.styleSuccessButton(btnRelatorios);
        UIStyler.styleDangerButton(btnSair);
        
        panel.add(btnMenuCompleto);
        panel.add(btnGerenciarCategorias);
        panel.add(btnRelatorios);
        panel.add(btnSair);
        
        // Ações dos botões
        btnMenuCompleto.addActionListener(e -> abrirMenuCompleto());
        btnGerenciarCategorias.addActionListener(e -> abrirGerenciadorCategorias());
        btnRelatorios.addActionListener(e -> abrirRelatorios());
        btnSair.addActionListener(e -> sair());
        
        return panel;
    }
    
    private void carregarDados() {
        carregarResumo();
        carregarContas();
        carregarUltimasTransacoes();
    }
    
    private void carregarResumo() {
        // Saldo total
        BigDecimal saldoTotal = contaController.obterSaldoTotal();
        lblSaldoTotal.setText(formatarMoeda(saldoTotal));
        
        // Período do mês atual
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        
        BigDecimal receitas = transacaoController.obterTotalReceitas(inicio, fim);
        BigDecimal despesas = transacaoController.obterTotalDespesas(inicio, fim);
        BigDecimal saldoMes = receitas.subtract(despesas);
        
        lblReceitasMes.setText(formatarMoeda(receitas));
        lblDespesasMes.setText(formatarMoeda(despesas));
        lblSaldoMes.setText(formatarMoeda(saldoMes));
        
        // Cor do saldo do mês
        if (saldoMes.compareTo(BigDecimal.ZERO) >= 0) {
            lblSaldoMes.setForeground(new Color(0, 150, 0));
        } else {
            lblSaldoMes.setForeground(new Color(200, 0, 0));
        }
    }
    
    private void carregarContas() {
        modelContas.setRowCount(0);
        List<Conta> contas = contaController.listarContasAtivas();
        
        for (Conta conta : contas) {
            Object[] linha = {
                conta.getNome(),
                formatarTipoConta(conta.getTipo()),
                conta.getSaldoAtual(),
                conta.getInstituicao()
            };
            modelContas.addRow(linha);
        }
    }
    
    private void carregarUltimasTransacoes() {
        modelTransacoes.setRowCount(0);
        
        // Últimos 30 dias
        LocalDate fim = LocalDate.now();
        LocalDate inicio = fim.minusDays(30);
        
        List<Transacao> transacoes = transacaoController.listarTransacoesPeriodo(inicio, fim);
        
        // Limitar a 20 transações
        int limite = Math.min(transacoes.size(), 20);
        for (int i = 0; i < limite; i++) {
            Transacao t = transacoes.get(i);
            Object[] linha = {
                t.getDataTransacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                t.getDescricao(),
                t.getNomeCategoria(),
                t.getValor(),
                t.getPago() ? "Pago" : "Pendente"
            };
            modelTransacoes.addRow(linha);
        }
    }
    
    private void configurarEventos() {
        // Atualizar dados automaticamente
        Timer timer = new Timer(60000, e -> carregarDados()); // Atualiza a cada minuto
        timer.start();
    }
    
    // Métodos de navegação
    private void abrirMenuCompleto() {
        MenuPrincipal menu = new MenuPrincipal(usuarioId);
        menu.setVisible(true);
    }
    
    private void abrirCadastroConta() {
        new ContaView(usuarioId, this).setVisible(true);
    }
    
    private void abrirCadastroTransacao() {
        new TransacaoView(usuarioId, this).setVisible(true);
    }
    
    private void abrirGerenciadorCategorias() {
        new CategoriaView(usuarioId, this).setVisible(true);
    }
    
    private void abrirRelatorios() {
        new RelatoriosView(usuarioId).setVisible(true);
    }
    
    private void abrirRelatorioTransacoes() {
        new TransacaoView(usuarioId, this).abrirConsulta();
    }
    
    private void editarConta() {
        int linha = tblContas.getSelectedRow();
        if (linha >= 0) {
            String nomeConta = (String) modelContas.getValueAt(linha, 0);
            // Buscar conta pelo nome e abrir edição
            // Implementação detalhada na ContaView
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma conta para editar.");
        }
    }
    
    private void excluirConta() {
        int linha = tblContas.getSelectedRow();
        if (linha >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir esta conta?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Implementar exclusão
                carregarContas();
                carregarResumo();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma conta para excluir.");
        }
    }
    
    private void marcarComoPago() {
        int linha = tblUltimasTransacoes.getSelectedRow();
        if (linha >= 0) {
            // Implementar marcação como pago
            carregarUltimasTransacoes();
            carregarResumo();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma transação para marcar como pago.");
        }
    }
    
    private void sair() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    // Métodos auxiliares
    private String formatarMoeda(BigDecimal valor) {
        return CurrencyUtils.formatCurrency(valor);
    }
    
    private String formatarTipoConta(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "CORRENTE" -> "Conta Corrente";
            case "POUPANCA" -> "Poupança";
            case "INVESTIMENTO" -> "Investimento";
            case "CARTEIRA" -> "Carteira";
            default -> tipo;
        };
    }
    
    // Renderers para células
    class SaldoCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                BigDecimal saldo = (BigDecimal) value;
                setText(formatarMoeda(saldo));
                
                if (saldo.compareTo(BigDecimal.ZERO) >= 0) {
                    setForeground(new Color(0, 100, 0));
                } else {
                    setForeground(new Color(200, 0, 0));
                }
            }
            
            return c;
        }
    }
    
    class ValorCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                BigDecimal valor = (BigDecimal) value;
                setText(formatarMoeda(valor));
                
                // Verificar se é despesa ou receita (baseado na linha da tabela)
                String tipo = "";
                if (row < modelTransacoes.getRowCount()) {
                    Object tipoObj = table.getValueAt(row, 4); // Coluna status
                    // Lógica para determinar tipo
                }
            }
            
            return c;
        }
    }
    
    public void atualizarDashboard() {
        carregarDados();
    }
    
    public static void main(String[] args) {
        // Para teste
        SwingUtilities.invokeLater(() -> {
            new DashboardView(1).setVisible(true);
        });
    }
}