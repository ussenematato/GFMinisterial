package view.telas.componentes;

import controller.TransacaoController;
import controller.ContaController;
import controller.CategoriaController;
import model.entity.Transacao;
import model.entity.Conta;
import model.entity.Categoria;
import view.telas.utils.Renderers;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import view.telas.MenuPrincipal;

public class TransacoesCard extends CardBase {
    
    private TransacaoController transacaoController;
    private ContaController contaController;
    private CategoriaController categoriaController;
    
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
    
    private Transacao transacaoEditando;
    
    public TransacoesCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.transacaoController = new TransacaoController(usuarioId);
        this.contaController = new ContaController(usuarioId);
        this.categoriaController = new CategoriaController(usuarioId);
        initComponents();
        carregarCombos();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Abas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Aba 1: Cadastro
        JPanel panelCadastro = criarPanelCadastro();
        tabbedPane.addTab("Nova Transação", panelCadastro);
        
        // Aba 2: Consulta
        JPanel panelConsulta = criarPanelConsulta();
        tabbedPane.addTab("Consulta", panelConsulta);
        
        // Painel inferior - Botões (compartilhado)
        JPanel panelBotoes = criarPanelBotoes();
        
        add(tabbedPane, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
    }
    
    private JPanel criarPanelCadastro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
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
        
        txtDataTransacao = new JFormattedTextField("##/##/####");
        txtDataTransacao.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        gbc.gridx = 1;
        panel.add(txtDataTransacao, gbc);
        
        // Data Vencimento
        gbc.gridx = 2;
        panel.add(new JLabel("Vencimento:"), gbc);
        
        txtDataVencimento = new JFormattedTextField("##/##/####");
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
    
    private JPanel criarPanelConsulta() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        
        // Painel de filtros
        JPanel panelFiltros = criarPanelFiltros();
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
        tblTransacoes.setRowHeight(30);
        tblTransacoes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblTransacoes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblTransacoes.getColumnModel().getColumn(5).setCellRenderer(new Renderers.ValorCellRenderer());
        tblTransacoes.getColumnModel().getColumn(6).setCellRenderer(new Renderers.StatusCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblTransacoes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Seleção da tabela
        tblTransacoes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                habilitarBotoesEdicao();
            }
        });
        
        return panel;
    }
    
    private JPanel criarPanelFiltros() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Tipo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tipo:"), gbc);
        
        JComboBox<String> cmbFiltroTipo = new JComboBox<>(new String[]{"TODOS", "DESPESA", "RECEITA"});
        gbc.gridx = 1;
        panel.add(cmbFiltroTipo, gbc);
        
        // Data Início
        gbc.gridx = 2;
        panel.add(new JLabel("Data Início:"), gbc);
        
        JFormattedTextField txtDataInicio = new JFormattedTextField("##/##/####");
        txtDataInicio.setText(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        gbc.gridx = 3;
        panel.add(txtDataInicio, gbc);
        
        // Data Fim
        gbc.gridx = 4;
        panel.add(new JLabel("Data Fim:"), gbc);
        
        JFormattedTextField txtDataFim = new JFormattedTextField("##/##/####");
        txtDataFim.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        gbc.gridx = 5;
        panel.add(txtDataFim, gbc);
        
        // Botão Filtrar
        JButton btnFiltrar = criarBotao("Filtrar", new Color(70, 130, 180));
        btnFiltrar.addActionListener(e -> {
            try {
                LocalDate inicio = LocalDate.parse(txtDataInicio.getText(), 
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate fim = LocalDate.parse(txtDataFim.getText(), 
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String tipo = cmbFiltroTipo.getSelectedItem().toString();
                filtrarTransacoes(inicio, fim, tipo);
            } catch (Exception ex) {
                mostrarMensagemErro("Data inválida. Use o formato dd/MM/yyyy.");
            }
        });
        gbc.gridx = 6;
        panel.add(btnFiltrar, gbc);
        
        return panel;
    }
    
    private JPanel criarPanelBotoes() {
        JPanel panel = criarPainelBotoes();
        
        btnSalvar = criarBotao("Salvar", new Color(76, 175, 80));
        btnEditar = criarBotao("Editar", new Color(33, 150, 243));
        btnExcluir = criarBotao("Excluir", new Color(244, 67, 54));
        btnCancelar = criarBotao("Cancelar", new Color(158, 158, 158));
        
        // Tamanho
        Dimension btnSize = new Dimension(100, 35);
        btnSalvar.setPreferredSize(btnSize);
        btnEditar.setPreferredSize(btnSize);
        btnExcluir.setPreferredSize(btnSize);
        btnCancelar.setPreferredSize(btnSize);
        
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
    
    private void atualizarCategoriasPorTipo() {
        String tipo = cmbTipo.getSelectedItem().toString();
        List<Categoria> categorias = categoriaController.listarCategorias(tipo);
        
        cmbCategoria.removeAllItems();
        for (Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
    }
    
    @Override
    public void carregarDados() {
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
                mostrarMensagemErro("A descrição é obrigatória.");
                txtDescricao.requestFocus();
                return;
            }
            
            BigDecimal valor;
            try {
                valor = new BigDecimal(txtValor.getText().trim());
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    mostrarMensagemErro("O valor deve ser maior que zero.");
                    txtValor.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarMensagemErro("Valor inválido.");
                txtValor.requestFocus();
                return;
            }
            
            if (cmbConta.getSelectedItem() == null) {
                mostrarMensagemErro("Selecione uma conta.");
                return;
            }
            
            if (cmbCategoria.getSelectedItem() == null) {
                mostrarMensagemErro("Selecione uma categoria.");
                return;
            }
            
            LocalDate dataTransacao;
            try {
                dataTransacao = LocalDate.parse(txtDataTransacao.getText(), 
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                mostrarMensagemErro("Data inválida. Use o formato dd/MM/yyyy.");
                txtDataTransacao.requestFocus();
                return;
            }
            
            LocalDate dataVencimento = dataTransacao;
            if (!txtDataVencimento.getText().trim().isEmpty()) {
                try {
                    dataVencimento = LocalDate.parse(txtDataVencimento.getText(), 
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e) {
                    mostrarMensagemErro("Data de vencimento inválida. Use o formato dd/MM/yyyy.");
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
                    mostrarMensagemSucesso("Transação registrada com sucesso!");
                    limparFormulario();
                    carregarDados();
                    if (updateCallback != null) {
                        updateCallback.onUpdate();
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
                    mostrarMensagemSucesso("Transação atualizada com sucesso!");
                    limparFormulario();
                    carregarDados();
                    if (updateCallback != null) {
                        updateCallback.onUpdate();
                    }
                }
            }
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao salvar transação: " + e.getMessage());
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
                
                // Mudar para a aba de cadastro
                ((JTabbedPane) getParent().getParent()).setSelectedIndex(0);
            }
        }
    }
    
    private void excluirTransacao() {
        int linha = tblTransacoes.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelTransacoes.getValueAt(linha, 0);
            String descricao = (String) modelTransacoes.getValueAt(linha, 2);
            
            if (confirmarAcao("Tem certeza que deseja excluir a transação '" + descricao + "'?")) {
                boolean sucesso = transacaoController.excluirTransacao(id);
                if (sucesso) {
                    mostrarMensagemSucesso("Transação excluída com sucesso!");
                    carregarDados();
                    if (updateCallback != null) {
                        updateCallback.onUpdate();
                    }
                }
            }
        }
    }
    
    private void cancelar() {
        limparFormulario();
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
    
    public void recarregarCategorias() {
        atualizarCategoriasPorTipo();
    }
    
    public void mostrarAbaCadastro() {
        // Encontrar o JTabbedPane
        Component parent = getParent();
        while (parent != null && !(parent instanceof JTabbedPane)) {
            parent = parent.getParent();
        }
        if (parent instanceof JTabbedPane) {
            ((JTabbedPane) parent).setSelectedIndex(0);
        }
    }
}