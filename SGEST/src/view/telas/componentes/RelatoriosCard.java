package view.telas.componentes;

import controller.TransacaoController;
import controller.ContaController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Date;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;
import view.telas.MenuPrincipal;

public class RelatoriosCard extends CardBase {
    
    private TransacaoController transacaoController;
    private ContaController contaController;
    
    // Componentes da UI
    private JTabbedPane tabbedPane;
    private JDateChooser dateChooserInicio;
    private JDateChooser dateChooserFim;
    private JButton btnFiltrar;
    private JButton btnLimparFiltros;
    
    // Tabela
    private JTable tabelaDetalhamento;
    private DefaultTableModel modeloTabela;
    
    // Labels para estatísticas
    private JLabel lblPeriodo;
    private JLabel lblTotalReceitas;
    private JLabel lblTotalDespesas;
    private JLabel lblSaldoPeriodo;
    private JLabel lblSaldoTotal;
    
    // Filtros
    private LocalDate filtroInicio;
    private LocalDate filtroFim;
    
    public RelatoriosCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.transacaoController = new TransacaoController(usuarioId);
        this.contaController = new ContaController(usuarioId);
        
        initComponents();
        configurarFiltros();
        carregarDados();
    }
    
    @Override
    public void carregarDados() {
        aplicarFiltros();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel de filtros
        JPanel panelFiltros = criarPainelFiltros();
        
        // Painel de abas
        tabbedPane = new JTabbedPane();
        
        // Criar as abas
        tabbedPane.addTab("Resumo Geral", criarPainelResumoGeral());
        tabbedPane.addTab("Despesas por Categoria", criarPainelDespesasCategoria());
        
        // Adicionar componentes ao card
        add(panelFiltros, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelFiltros() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Selecione o Intervalo de Datas"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Data Início
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Data Início:"), gbc);
        
        dateChooserInicio = new JDateChooser();
        dateChooserInicio.setDateFormatString("dd/MM/yyyy");
        // Configurar data inicial (primeiro dia do mês)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dateChooserInicio.setDate(cal.getTime());
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(dateChooserInicio, gbc);
        
        // Data Fim
        gbc.gridx = 2; gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Data Fim:"), gbc);
        
        dateChooserFim = new JDateChooser();
        dateChooserFim.setDateFormatString("dd/MM/yyyy");
        // Configurar data final (hoje)
        dateChooserFim.setDate(new Date());
        
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.weightx = 0.5;
        panel.add(dateChooserFim, gbc);
        
        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnFiltrar = criarBotao("Aplicar Filtros", new Color(70, 130, 180));
        btnLimparFiltros = criarBotao("Limpar Filtros", new Color(169, 169, 169));
        
        btnFiltrar.addActionListener(e -> aplicarFiltros());
        btnLimparFiltros.addActionListener(e -> limparFiltros());
        
        panelBotoes.add(btnFiltrar);
        panelBotoes.add(btnLimparFiltros);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        panel.add(panelBotoes, gbc);
        
        return panel;
    }
    
    private JPanel criarPainelResumoGeral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Painel de estatísticas
        JPanel panelStats = criarPainelEstatisticas();
        
        panel.add(panelStats, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelEstatisticas() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Estatísticas"));
        panel.setPreferredSize(new Dimension(500, 200));
        
        // Criar labels
        lblPeriodo = new JLabel("---");
        lblTotalReceitas = new JLabel("---");
        lblTotalDespesas = new JLabel("---");
        lblSaldoPeriodo = new JLabel("---");
        lblSaldoTotal = new JLabel("---");
        
        // Estilização
        lblTotalReceitas.setForeground(new Color(0, 150, 0));
        lblTotalReceitas.setFont(new Font("Arial", Font.BOLD, 12));
        
        lblTotalDespesas.setForeground(new Color(200, 0, 0));
        lblTotalDespesas.setFont(new Font("Arial", Font.BOLD, 12));
        
        lblSaldoPeriodo.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoTotal.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Adicionar labels ao painel
        panel.add(new JLabel("Período:"));
        panel.add(lblPeriodo);
        
        panel.add(new JLabel("Total Receitas:"));
        panel.add(lblTotalReceitas);
        
        panel.add(new JLabel("Total Despesas:"));
        panel.add(lblTotalDespesas);
        
        panel.add(new JLabel("Saldo Período:"));
        panel.add(lblSaldoPeriodo);
        
        panel.add(new JLabel("Saldo Total:"));
        panel.add(lblSaldoTotal);
        
        return panel;
    }
    
    private JPanel criarPainelDespesasCategoria() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabela
        JPanel panelTabela = new JPanel(new BorderLayout());
        panelTabela.setBorder(BorderFactory.createTitledBorder("Despesas por Categoria"));
        
        String[] colunas = {"Categoria", "Valor (MT)", "Percentual"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaDetalhamento = new JTable(modeloTabela);
        tabelaDetalhamento.setRowHeight(25);
        tabelaDetalhamento.setShowGrid(true);
        tabelaDetalhamento.setGridColor(new Color(220, 220, 220));
        JScrollPane scrollPane = new JScrollPane(tabelaDetalhamento);
        panelTabela.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(panelTabela, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void configurarFiltros() {
        // Configurar datas padrão (mês atual) - já feito em criarPainelFiltros
    }
    

    
    private void aplicarFiltros() {
        try {
            if (dateChooserInicio.getDate() == null || dateChooserFim.getDate() == null) {
                mostrarMensagemErro("Por favor, selecione ambas as datas!");
                return;
            }
            
            // Converter Date para LocalDate
            Calendar calInicio = Calendar.getInstance();
            calInicio.setTime(dateChooserInicio.getDate());
            filtroInicio = LocalDate.of(calInicio.get(Calendar.YEAR), 
                                       calInicio.get(Calendar.MONTH) + 1, 
                                       calInicio.get(Calendar.DAY_OF_MONTH));
            
            Calendar calFim = Calendar.getInstance();
            calFim.setTime(dateChooserFim.getDate());
            filtroFim = LocalDate.of(calFim.get(Calendar.YEAR), 
                                    calFim.get(Calendar.MONTH) + 1, 
                                    calFim.get(Calendar.DAY_OF_MONTH));
            
            if (filtroInicio.isAfter(filtroFim)) {
                mostrarMensagemErro("Data início não pode ser após data fim!");
                return;
            }
            
            atualizarEstatisticas();
            atualizarTabelaDetalhamento();
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao processar as datas: " + e.getMessage());
        }
    }
    
    private void limparFiltros() {
        // Resetar para mês atual
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        dateChooserInicio.setDate(cal.getTime());
        dateChooserFim.setDate(new Date());
        aplicarFiltros();
    }
    
    private void atualizarEstatisticas() {
        try {
            // Formatar período
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            lblPeriodo.setText(filtroInicio.format(formatter) + " a " + filtroFim.format(formatter));
            
            // Obter dados do período
            BigDecimal receitas = transacaoController.obterTotalReceitas(filtroInicio, filtroFim);
            BigDecimal despesas = transacaoController.obterTotalDespesas(filtroInicio, filtroFim);
            BigDecimal saldoPeriodo = receitas.subtract(despesas);
            BigDecimal saldoTotal = contaController.obterSaldoTotal();
            
            // Atualizar labels
            lblTotalReceitas.setText(String.format("MT %,.2f", receitas));
            lblTotalDespesas.setText(String.format("MT %,.2f", despesas));
            lblSaldoPeriodo.setText(String.format("MT %,.2f", saldoPeriodo));
            lblSaldoTotal.setText(String.format("MT %,.2f", saldoTotal));
            
            // Colorir saldo do período
            if (saldoPeriodo.compareTo(BigDecimal.ZERO) >= 0) {
                lblSaldoPeriodo.setForeground(new Color(0, 150, 0));
            } else {
                lblSaldoPeriodo.setForeground(new Color(200, 0, 0));
            }
            
            // Colorir saldo total
            if (saldoTotal.compareTo(BigDecimal.ZERO) >= 0) {
                lblSaldoTotal.setForeground(new Color(0, 150, 0));
            } else {
                lblSaldoTotal.setForeground(new Color(200, 0, 0));
            }
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao carregar estatísticas: " + e.getMessage());
        }
    }
    
    private void atualizarTabelaDetalhamento() {
        try {
            modeloTabela.setRowCount(0);
            
            List<Object[]> dados = transacaoController.obterDespesasPorCategoria(filtroInicio, filtroFim);
            
            if (dados.isEmpty()) {
                mostrarMensagemSucesso("Nenhuma despesa encontrada no período selecionado.");
                return;
            }
            
            // Calcular total
            double total = 0;
            for (Object[] linha : dados) {
                Double valor = (Double) linha[1];
                total += valor;
            }
            
            // Adicionar dados à tabela
            for (Object[] linha : dados) {
                String categoria = (String) linha[0];
                Double valor = (Double) linha[1];
                Double percentual = total > 0 ? (valor / total) * 100 : 0;
                
                Object[] linhaTabela = {
                    categoria,
                    String.format("MT %,.2f", valor),
                    String.format("%.1f%%", percentual)
                };
                modeloTabela.addRow(linhaTabela);
            }
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao carregar despesas por categoria: " + e.getMessage());
        }
    }
}