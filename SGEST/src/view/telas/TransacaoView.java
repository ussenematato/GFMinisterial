package view.telas;

import controller.TransacaoController;
import controller.ContaController;
import controller.CategoriaController;
import model.entity.Transacao;
import model.entity.Conta;
import model.entity.Categoria;
import util.CurrencyUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransacaoView extends JDialog {
    private Integer usuarioId;
    private TransacaoController transacaoController;
    private ContaController contaController;
    private CategoriaController categoriaController;
    private DashboardView dashboardView;
    
    // Componentes
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JComboBox<String> cmbTipo;
    private JComboBox<Conta> cmbConta;
    private JComboBox<Categoria> cmbCategoria;
    private JTextArea txtObservacoes;
    private JCheckBox chkPago;
    private JCheckBox chkRecorrente;
    private JComboBox<String> cmbFrequencia;
    private JFormattedTextField txtDataTransacao;
    private JFormattedTextField txtDataVencimento;
    
    private JTable tblTransacoes;
    private DefaultTableModel modelTransacoes;
    private JButton btnSalvar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnCancelar;
    private JButton btnFiltrar;
    
    private Transacao transacaoEditando;
    
    public TransacaoView(Integer usuarioId, DashboardView dashboardView) {
        super(dashboardView, "Gerenciar Transações", true);
        this.usuarioId = usuarioId;
        this.dashboardView = dashboardView;
        this.transacaoController = new TransacaoController(usuarioId);
        this.contaController = new ContaController(usuarioId);
        this.categoriaController = new CategoriaController(usuarioId);
        this.transacaoEditando = null;
        
        initComponents();
        carregarCombos();
        carregarTransacoesPeriodo();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel de formulário
        JPanel panelForm = criarPanelFormulario();
        
        // Painel da tabela
        JPanel panelTabela = criarPanelTabela();
        
        // Painel de botões
        JPanel panelBotoes = criarPanelBotoes();
        
        // Adicionando componentes
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Nova Transação", panelForm);
        tabbedPane.addTab("Consulta", panelTabela);
        
        add(tabbedPane, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
        
        // Configurações da janela
        setSize(1000, 700);
        setLocationRelativeTo(getParent());
        setResizable(true);
    }
    
    private JPanel criarPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Descrição
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Descrição:*"), gbc);
        
        txtDescricao = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(txtDescricao, gbc);
        
        // Valor
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Valor:*"), gbc);
        
        txtValor = new JTextField(15);
        txtValor.setText("0.00");
        gbc.gridx = 1;
        panel.add(txtValor, gbc);
        
        // Tipo
        gbc.gridx = 2;
        panel.add(new JLabel("Tipo:*"), gbc);
        
        String[] tipos = {"DESPESA", "RECEITA"};
        cmbTipo = new JComboBox<>(tipos);
        cmbTipo.addActionListener(e -> atualizarCategoriasPorTipo());
        gbc.gridx = 3;
        panel.add(cmbTipo, gbc);
        
        // Conta
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Conta:*"), gbc);
        
        cmbConta = new JComboBox<>();
        gbc.gridx = 1;
        panel.add(cmbConta, gbc);
        
        // Categoria
        gbc.gridx = 2;
        panel.add(new JLabel("Categoria:*"), gbc);
        
        cmbCategoria = new JComboBox<>();
        gbc.gridx = 3;
        panel.add(cmbCategoria, gbc);
        
        // Data Transação
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Data:*"), gbc);
        
        txtDataTransacao = new JFormattedTextField(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        txtDataTransacao.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        gbc.gridx = 1;
        panel.add(txtDataTransacao, gbc);
        
        // Data Vencimento
        gbc.gridx = 2;
        panel.add(new JLabel("Vencimento:"), gbc);
        
        txtDataVencimento = new JFormattedTextField(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        txtDataVencimento.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        gbc.gridx = 3;
        panel.add(txtDataVencimento, gbc);
        
        // Checkboxes
        gbc.gridx = 0;
        gbc.gridy = 4;
        chkPago = new JCheckBox("Pago");
        panel.add(chkPago, gbc);
        
        gbc.gridx = 1;
        chkRecorrente = new JCheckBox("Recorrente");
        chkRecorrente.addActionListener(e -> habilitarFrequencia());
        panel.add(chkRecorrente, gbc);
        
        // Frequencia
        gbc.gridx = 2;
        panel.add(new JLabel("Frequência:"), gbc);
        
        String[] frequencias = {"MENSAL", "SEMANAL", "ANUAL", "QUINZENAL"};
        cmbFrequencia = new JComboBox<>(frequencias);
        cmbFrequencia.setEnabled(false);
        gbc.gridx = 3;
        panel.add(cmbFrequencia, gbc);
        
        // Observações
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Observações:"), gbc);
        
        txtObservacoes = new JTextArea(4, 30);
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        JScrollPane scrollObservacoes = new JScrollPane(txtObservacoes);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollObservacoes, gbc);
        
        return panel;
    }
    
    private JPanel criarPanelTabela() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        
        // Painel de filtros
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFiltros.add(new JLabel("Tipo:"), gbc);
        
        JComboBox<String> cmbFiltroTipo = new JComboBox<>(new String[]{"TODOS", "DESPESA", "RECEITA"});
        gbc.gridx = 1;
        panelFiltros.add(cmbFiltroTipo, gbc);
        
        // Data Início
        gbc.gridx = 2;
        panelFiltros.add(new JLabel("Data Início:"), gbc);
        
        JFormattedTextField txtDataInicio = new JFormattedTextField(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        txtDataInicio.setText(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        gbc.gridx = 3;
        panelFiltros.add(txtDataInicio, gbc);
        
        // Data Fim
        gbc.gridx = 4;
        panelFiltros.add(new JLabel("Data Fim:"), gbc);
        
        JFormattedTextField txtDataFim = new JFormattedTextField(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        txtDataFim.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        gbc.gridx = 5;
        panelFiltros.add(txtDataFim, gbc);
        
        // Botão Filtrar
        btnFiltrar = new JButton("Filtrar");
        util.UIStyler.stylePrimaryButton(btnFiltrar);
        btnFiltrar.addActionListener(e -> {
            LocalDate inicio = LocalDate.parse(txtDataInicio.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate fim = LocalDate.parse(txtDataFim.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String tipo = cmbFiltroTipo.getSelectedItem().toString();
            filtrarTransacoes(inicio, fim, tipo);
        });
        gbc.gridx = 6;
        panelFiltros.add(btnFiltrar, gbc);
        
        panel.add(panelFiltros, BorderLayout.NORTH);
        
        // Tabela
        String[] colunas = {"ID", "Data", "Descrição", "Categoria", "Conta", "Valor", "Status", "Pago"};
        modelTransacoes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) return BigDecimal.class;
                return String.class;
            }
        };
        
        tblTransacoes = new JTable(modelTransacoes);
        tblTransacoes.setRowHeight(25);
        tblTransacoes.setShowGrid(true);
        tblTransacoes.setGridColor(new Color(220, 220, 220));
        tblTransacoes.getColumnModel().getColumn(5).setCellRenderer(new ValorCellRenderer());
        tblTransacoes.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblTransacoes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPanelBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnSalvar = new JButton("Salvar");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");
        btnCancelar = new JButton("Cancelar");
        
        // Estilização simples
        util.UIStyler.styleSuccessButton(btnSalvar);
        util.UIStyler.styleSecondaryButton(btnEditar);
        util.UIStyler.styleDangerButton(btnExcluir);
        util.UIStyler.styleNeutralButton(btnCancelar);
        
        panel.add(btnSalvar);
        panel.add(btnEditar);
        panel.add(btnExcluir);
        panel.add(btnCancelar);
        
        // Ações dos botões
        btnSalvar.addActionListener(e -> salvarTransacao());
        btnEditar.addActionListener(e -> editarTransacao());
        btnExcluir.addActionListener(e -> excluirTransacao());
        btnCancelar.addActionListener(e -> cancelar());
        
        // Estado inicial
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        
        return panel;
    }
    
    private void carregarCombos() {
        // Carregar contas
        List<Conta> contas = contaController.listarContasAtivas();
        cmbConta.removeAllItems();
        for (Conta conta : contas) {
            cmbConta.addItem(conta);
        }
        
        // Carregar categorias por tipo inicial
        atualizarCategoriasPorTipo();
    }
    
    // Método para recarregar combos após alterações de saldo
    public void atualizarComboContas() {
        List<Conta> contas = contaController.listarContasAtivas();
        cmbConta.removeAllItems();
        for (Conta conta : contas) {
            cmbConta.addItem(conta);
        }
    }
    
    private void atualizarCategoriasPorTipo() {
        String tipo = cmbTipo.getSelectedItem().toString();
        List<Categoria> categorias = categoriaController.listarCategorias(tipo);
        
        cmbCategoria.removeAllItems();
        for (Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
    }
    
    private void carregarTransacoesPeriodo() {
        LocalDate inicio = LocalDate.now().withDayOfMonth(1);
        LocalDate fim = LocalDate.now();
        filtrarTransacoes(inicio, fim, "TODOS");
    }
    
    private void filtrarTransacoes(LocalDate inicio, LocalDate fim, String tipo) {
        modelTransacoes.setRowCount(0);
        
        List<Transacao> transacoes;
        if ("TODOS".equals(tipo)) {
            transacoes = transacaoController.listarTransacoesPeriodo(inicio, fim);
        } else {
            transacoes = transacaoController.listarTransacoesPeriodo(inicio, fim);
            // Filtrar por tipo
            transacoes = transacoes.stream()
                .filter(t -> t.getTipo().equals(tipo))
                .toList();
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Transacao t : transacoes) {
            Object[] linha = {
                t.getId(),
                t.getDataTransacao().format(formatter),
                t.getDescricao(),
                t.getNomeCategoria(),
                t.getNomeConta(),
                t.getValor(),
                t.getTipo(),
                t.getPago() ? "Sim" : "Não"
            };
            modelTransacoes.addRow(linha);
        }
    }
    
    private void habilitarFrequencia() {
        cmbFrequencia.setEnabled(chkRecorrente.isSelected());
    }
    
    private void salvarTransacao() {
        try {
            // Validações
            if (txtDescricao.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "A descrição é obrigatória.");
                txtDescricao.requestFocus();
                return;
            }
            
            BigDecimal valor;
            try {
                valor = new BigDecimal(txtValor.getText().trim());
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "O valor deve ser maior que zero.");
                    txtValor.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
                txtValor.requestFocus();
                return;
            }
            
            if (cmbConta.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma conta.");
                return;
            }
            
            if (cmbCategoria.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma categoria.");
                return;
            }
            
            LocalDate dataTransacao;
            try {
                dataTransacao = LocalDate.parse(txtDataTransacao.getText(), 
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy.");
                txtDataTransacao.requestFocus();
                return;
            }
            
            LocalDate dataVencimento = dataTransacao;
            if (!txtDataVencimento.getText().trim().isEmpty()) {
                try {
                    dataVencimento = LocalDate.parse(txtDataVencimento.getText(), 
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Data de vencimento inválida. Use o formato dd/MM/yyyy.");
                    txtDataVencimento.requestFocus();
                    return;
                }
            }
            
            Conta contaSelecionada = (Conta) cmbConta.getSelectedItem();
            Categoria categoriaSelecionada = (Categoria) cmbCategoria.getSelectedItem();
            
            if (transacaoEditando == null) {
                // Nova transação
                boolean sucesso = transacaoController.registrarTransacao(
                    txtDescricao.getText().trim(),
                    valor,
                    cmbTipo.getSelectedItem().toString(),
                    dataTransacao,
                    contaSelecionada.getId(),
                    categoriaSelecionada.getId(),
                    txtObservacoes.getText().trim()
                );
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Transação registrada com sucesso!");
                    limparFormulario();
                    carregarTransacoesPeriodo();
                    atualizarComboContas();
                    if (dashboardView != null) {
                        dashboardView.atualizarDashboard();
                    }
                }
            } else {
                // Editar transação
                boolean sucesso = transacaoController.atualizarTransacao(
                    transacaoEditando.getId(),
                    txtDescricao.getText().trim(),
                    valor,
                    dataTransacao,
                    contaSelecionada.getId(),
                    categoriaSelecionada.getId(),
                    txtObservacoes.getText().trim()
                );
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Transação atualizada com sucesso!");
                    limparFormulario();
                    carregarTransacoesPeriodo();
                    atualizarComboContas();
                    if (dashboardView != null) {
                        dashboardView.atualizarDashboard();
                    }
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar transação: " + e.getMessage());
        }
    }
    
    private void editarTransacao() {
        int linha = tblTransacoes.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelTransacoes.getValueAt(linha, 0);
            transacaoEditando = transacaoController.buscarTransacaoPorId(id);
            
            if (transacaoEditando != null) {
                txtDescricao.setText(transacaoEditando.getDescricao());
                txtValor.setText(transacaoEditando.getValor().toString());
                cmbTipo.setSelectedItem(transacaoEditando.getTipo());
                
                // Selecionar conta
                for (int i = 0; i < cmbConta.getItemCount(); i++) {
                    Conta conta = cmbConta.getItemAt(i);
                    if (conta.getId().equals(transacaoEditando.getContaId())) {
                        cmbConta.setSelectedIndex(i);
                        break;
                    }
                }
                
                // Atualizar categorias e selecionar
                atualizarCategoriasPorTipo();
                for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
                    Categoria categoria = cmbCategoria.getItemAt(i);
                    if (categoria.getId().equals(transacaoEditando.getCategoriaId())) {
                        cmbCategoria.setSelectedIndex(i);
                        break;
                    }
                }
                
                txtDataTransacao.setText(transacaoEditando.getDataTransacao()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                txtDataVencimento.setText(transacaoEditando.getDataVencimento()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                txtObservacoes.setText(transacaoEditando.getObservacoes());
                chkPago.setSelected(transacaoEditando.getPago());
                chkRecorrente.setSelected(transacaoEditando.getRecorrente());
                
                if (transacaoEditando.getRecorrente()) {
                    cmbFrequencia.setSelectedItem(transacaoEditando.getFrequencia());
                }
                
                btnSalvar.setText("Atualizar");
                
                // Mudar para a aba de edição
                ((JTabbedPane) getContentPane().getComponent(0)).setSelectedIndex(0);
            }
        }
    }
    
    private void excluirTransacao() {
        int linha = tblTransacoes.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelTransacoes.getValueAt(linha, 0);
            String descricao = (String) modelTransacoes.getValueAt(linha, 2);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a transação '" + descricao + "'?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean sucesso = transacaoController.excluirTransacao(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Transação excluída com sucesso!");
                    carregarTransacoesPeriodo();
                    atualizarComboContas();
                    if (dashboardView != null) {
                        dashboardView.atualizarDashboard();
                    }
                }
            }
        }
    }
    
    private void cancelar() {
        limparFormulario();
        dispose();
    }
    
    private void limparFormulario() {
        txtDescricao.setText("");
        txtValor.setText("0.00");
        cmbTipo.setSelectedIndex(0);
        txtDataTransacao.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDataVencimento.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtObservacoes.setText("");
        chkPago.setSelected(false);
        chkRecorrente.setSelected(false);
        cmbFrequencia.setEnabled(false);
        transacaoEditando = null;
        btnSalvar.setText("Salvar");
        tblTransacoes.clearSelection();
        habilitarBotoesEdicao();
    }
    
    private void habilitarBotoesEdicao() {
        int linhaSelecionada = tblTransacoes.getSelectedRow();
        boolean habilitar = linhaSelecionada >= 0;
        
        btnEditar.setEnabled(habilitar);
        btnExcluir.setEnabled(habilitar);
    }
    
    public void abrirConsulta() {
        ((JTabbedPane) getContentPane().getComponent(0)).setSelectedIndex(1);
        setVisible(true);
    }
    
    // Renderers para células
    class ValorCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                BigDecimal valor = (BigDecimal) value;
                setText(CurrencyUtils.formatCurrency(valor));
                
                // Verificar se é despesa ou receita
                String tipo = (String) table.getValueAt(row, 6);
                if ("DESPESA".equals(tipo)) {
                    setForeground(new Color(200, 0, 0));
                } else {
                    setForeground(new Color(0, 150, 0));
                }
                
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
    }
    
    class StatusCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof String) {
                String status = (String) value;
                
                if ("DESPESA".equals(status)) {
                    label.setForeground(new Color(200, 0, 0));
                    label.setText("Despesa");
                } else if ("RECEITA".equals(status)) {
                    label.setForeground(new Color(0, 150, 0));
                    label.setText("Receita");
                }
            }
            
            return label;
        }
    }
}