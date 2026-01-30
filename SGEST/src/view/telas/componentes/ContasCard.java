package view.telas.componentes;

import controller.ContaController;
import controller.TransacaoController;
import model.entity.Conta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.toedter.calendar.JDateChooser;
import java.util.List;
import view.telas.MenuPrincipal;

public class ContasCard extends CardBase {
    
    private ContaController contaController;
    private TransacaoController transacaoController;
    
    // Componentes - Aba Cadastro
    private JTextField txtNome;
    private JComboBox<String> cmbTipo;
    private JTextField txtSaldoInicial;
    private JTextField txtInstituicao;
    private JTable tblContas;
    private DefaultTableModel modelContas;
    private JButton btnSalvar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnCancelar;
    
    // Componentes - Aba Transferências
    private JTextField txtDescricaoTrans;
    private JTextField txtValorTrans;
    private JComboBox<Conta> cmbContaOrigemTrans;
    private JComboBox<Conta> cmbContaDestinoTrans;
    private JDateChooser txtDataTrans;
    private JTextArea txtObservacoesTrans;
    private JButton btnRealizarTrans;
    private JButton btnLimparTrans;
    private JLabel lblSaldoOrigemTrans;
    private JLabel lblSaldoDestinoTrans;
    
    private Conta contaEditando;
    
    public ContasCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.contaController = new ContaController(usuarioId);
        this.transacaoController = new TransacaoController(usuarioId);
        this.contaEditando = null;
        initComponents();
        carregarDados();
        carregarContasComboBoxes();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Criar abas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Cadastro de Contas", criarPainelCadastro());
        tabbedPane.addTab("Transferências", criarPainelTransferencias());
        
        // Painel de botões
        JPanel panelBotoes = criarPanelBotoes();
        
        add(tabbedPane, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelCadastro() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de formulário
        JPanel panelForm = criarPanelFormulario();
        
        // Painel da tabela
        JPanel panelTabela = criarPanelTabela();
        
        // Divisor
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelForm,
            panelTabela
        );
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.4);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarPainelTransferencias() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        JLabel lblTitulo = new JLabel("Movimentação de Valores entre Contas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;
        
        // Descrição
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Descrição:*"), gbc);
        
        txtDescricaoTrans = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(txtDescricaoTrans, gbc);
        gbc.gridwidth = 1;
        
        // Valor
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Valor:*"), gbc);
        
        txtValorTrans = new JTextField(15);
        txtValorTrans.setText("0.00");
        gbc.gridx = 1;
        panel.add(txtValorTrans, gbc);
        
        // Data
        gbc.gridx = 2;
        panel.add(new JLabel("Data:"), gbc);

        txtDataTrans = new JDateChooser();
        txtDataTrans.setDateFormatString("dd/MM/yyyy");
        txtDataTrans.setDate(new Date());
        gbc.gridx = 3;
        panel.add(txtDataTrans, gbc);
        
        // Conta Origem
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Conta Origem:*"), gbc);
        
        cmbContaOrigemTrans = new JComboBox<>();
        cmbContaOrigemTrans.addActionListener(e -> atualizarSaldoOrigemTrans());
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(cmbContaOrigemTrans, gbc);
        gbc.gridwidth = 1;
        
        // Saldo Origem
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Saldo Disponível:"), gbc);
        
        lblSaldoOrigemTrans = new JLabel("MT 0.00");
        lblSaldoOrigemTrans.setForeground(new Color(0, 100, 0));
        lblSaldoOrigemTrans.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 1;
        panel.add(lblSaldoOrigemTrans, gbc);
        
        // Conta Destino
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Conta Destino:*"), gbc);
        
        cmbContaDestinoTrans = new JComboBox<>();
        cmbContaDestinoTrans.addActionListener(e -> atualizarSaldoDestinoTrans());
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(cmbContaDestinoTrans, gbc);
        gbc.gridwidth = 1;
        
        // Saldo Destino
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Saldo Disponível:"), gbc);
        
        lblSaldoDestinoTrans = new JLabel("MT 0.00");
        lblSaldoDestinoTrans.setForeground(new Color(0, 100, 0));
        lblSaldoDestinoTrans.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 1;
        panel.add(lblSaldoDestinoTrans, gbc);
        
        // Observações
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Observações:"), gbc);
        
        txtObservacoesTrans = new JTextArea(3, 30);
        txtObservacoesTrans.setLineWrap(true);
        txtObservacoesTrans.setWrapStyleWord(true);
        JScrollPane scrollObservacoesTrans = new JScrollPane(txtObservacoesTrans);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        panel.add(scrollObservacoesTrans, gbc);
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Painel de botões de transferência
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 4;
        JPanel panelBotoesTrans = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnRealizarTrans = criarBotao("Realizar Transferência", new Color(76, 175, 80));
        btnLimparTrans = criarBotao("Limpar Formulário", new Color(158, 158, 158));
        
        btnRealizarTrans.addActionListener(e -> realizarTransferenciaInterna());
        btnLimparTrans.addActionListener(e -> limparFormularioTrans());
        
        panelBotoesTrans.add(btnRealizarTrans);
        panelBotoesTrans.add(btnLimparTrans);
        
        panel.add(panelBotoesTrans, gbc);
        
        return panel;
    }
    
    private JPanel criarPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Cadastro de Conta"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(250, 250, 250));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome:*"), gbc);
        
        txtNome = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtNome, gbc);
        
        // Tipo
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Tipo:*"), gbc);
        
        String[] tipos = {"CORRENTE", "POUPANCA", "INVESTIMENTO", "CARTEIRA", "OUTRO"};
        cmbTipo = new JComboBox<>(tipos);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(cmbTipo, gbc);
        
        // Saldo Inicial
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Saldo Inicial:"), gbc);
        
        txtSaldoInicial = new JTextField(15);
        txtSaldoInicial.setText("0.00");
        gbc.gridx = 1;
        panel.add(txtSaldoInicial, gbc);
        
        // Instituição
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Instituição:"), gbc);
        
        txtInstituicao = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtInstituicao, gbc);
        
        return panel;
    }
    
    private JPanel criarPanelTabela() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Contas Cadastradas"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Modelo da tabela
        String[] colunas = {"ID", "Nome", "Tipo", "Saldo", "Instituição", "Status"};
        modelContas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblContas = new JTable(modelContas);
        tblContas.setRowHeight(30);
        tblContas.setShowGrid(true);
        tblContas.setGridColor(new Color(220, 220, 220));
        tblContas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblContas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(tblContas);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Seleção da tabela
        tblContas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                habilitarBotoesEdicao();
            }
        });
        
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
        btnSalvar.addActionListener(e -> salvarConta());
        btnEditar.addActionListener(e -> editarConta());
        btnExcluir.addActionListener(e -> excluirConta());
        btnCancelar.addActionListener(e -> cancelar());
        
        // Estado inicial
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        
        return panel;
    }
    
    @Override
    public void carregarDados() {
        modelContas.setRowCount(0);
        List<Conta> contas = contaController.listarContasAtivas();
        
        for (Conta conta : contas) {
            Object[] linha = {
                conta.getId(),
                conta.getNome(),
                formatarTipoConta(conta.getTipo()),
                String.format("MT %,.2f", conta.getSaldoAtual()),
                conta.getInstituicao(),
                conta.getAtivo() ? "Ativa" : "Inativa"
            };
            modelContas.addRow(linha);
        }
    }
    
    private void salvarConta() {
        try {
            // Validações
            if (txtNome.getText().trim().isEmpty()) {
                mostrarMensagemErro("O nome da conta é obrigatório.");
                txtNome.requestFocus();
                return;
            }
            
            BigDecimal saldoInicial;
            try {
                saldoInicial = new BigDecimal(txtSaldoInicial.getText().trim());
            } catch (NumberFormatException e) {
                mostrarMensagemErro("Saldo inicial inválido.");
                txtSaldoInicial.requestFocus();
                return;
            }
            
            if (contaEditando == null) {
                // Nova conta
                boolean sucesso = contaController.criarConta(
                    txtNome.getText().trim(),
                    cmbTipo.getSelectedItem().toString(),
                    saldoInicial,
                    txtInstituicao.getText().trim()
                );
                
                if (sucesso) {
                    mostrarMensagemSucesso("Conta criada com sucesso!");
                    limparFormulario();
                    carregarDados();
                    if (updateCallback != null) {
                        updateCallback.onUpdate();
                    }
                }
            } else {
                // Editar conta
                boolean sucesso = contaController.atualizarConta(
                    contaEditando.getId(),
                    txtNome.getText().trim(),
                    cmbTipo.getSelectedItem().toString(),
                    saldoInicial,
                    txtInstituicao.getText().trim()
                );
                
                if (sucesso) {
                    mostrarMensagemSucesso("Conta atualizada com sucesso!");
                    limparFormulario();
                    carregarDados();
                    if (updateCallback != null) {
                        updateCallback.onUpdate();
                    }
                }
            }
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao salvar conta: " + e.getMessage());
        }
    }
    
    private void editarConta() {
        int linha = tblContas.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelContas.getValueAt(linha, 0);
            contaEditando = contaController.buscarContaPorId(id);
            
            if (contaEditando != null) {
                txtNome.setText(contaEditando.getNome());
                cmbTipo.setSelectedItem(contaEditando.getTipo());
                txtSaldoInicial.setText(contaEditando.getSaldoAtual().toString());
                txtInstituicao.setText(contaEditando.getInstituicao());
                
                btnSalvar.setText("Atualizar");
            }
        }
    }
    
    private void excluirConta() {
        int linha = tblContas.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelContas.getValueAt(linha, 0);
            String nome = (String) modelContas.getValueAt(linha, 1);
            
            if (confirmarAcao("Tem certeza que deseja desativar a conta '" + nome + "'?\n" +
                    "A conta será marcada como inativa.")) {
                
                boolean sucesso = contaController.desativarConta(id);
                if (sucesso) {
                    mostrarMensagemSucesso("Conta desativada com sucesso!");
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
        txtNome.setText("");
        cmbTipo.setSelectedIndex(0);
        txtSaldoInicial.setText("0.00");
        txtInstituicao.setText("");
        contaEditando = null;
        btnSalvar.setText("Salvar");
        tblContas.clearSelection();
        habilitarBotoesEdicao();
    }
    
    private void habilitarBotoesEdicao() {
        int linhaSelecionada = tblContas.getSelectedRow();
        boolean habilitar = linhaSelecionada >= 0;
        
        btnEditar.setEnabled(habilitar);
        btnExcluir.setEnabled(habilitar);
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
    
    public void mostrarFormularioNovaConta() {
        // Foco no campo nome para nova conta
        txtNome.requestFocus();
    }
    
    private void carregarContasComboBoxes() {
        List<Conta> contas = contaController.listarContasAtivas();
        
        cmbContaOrigemTrans.removeAllItems();
        cmbContaDestinoTrans.removeAllItems();
        
        for (Conta conta : contas) {
            cmbContaOrigemTrans.addItem(conta);
            cmbContaDestinoTrans.addItem(conta);
        }
        
        if (!contas.isEmpty()) {
            atualizarSaldoOrigemTrans();
            atualizarSaldoDestinoTrans();
        }
    }
    
    private void atualizarSaldoOrigemTrans() {
        Conta conta = (Conta) cmbContaOrigemTrans.getSelectedItem();
        if (conta != null) {
            lblSaldoOrigemTrans.setText(String.format("MT %,.2f", conta.getSaldoAtual()));
        }
    }
    
    private void atualizarSaldoDestinoTrans() {
        Conta conta = (Conta) cmbContaDestinoTrans.getSelectedItem();
        if (conta != null) {
            lblSaldoDestinoTrans.setText(String.format("MT %,.2f", conta.getSaldoAtual()));
        }
    }
    
    private void realizarTransferenciaInterna() {
        try {
            // Validações
            if (txtDescricaoTrans.getText().trim().isEmpty()) {
                mostrarMensagemErro("A descrição da transferência é obrigatória.");
                txtDescricaoTrans.requestFocus();
                return;
            }
            
            BigDecimal valor;
            try {
                valor = new BigDecimal(txtValorTrans.getText().trim());
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    mostrarMensagemErro("O valor deve ser maior que zero.");
                    txtValorTrans.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarMensagemErro("Valor inválido.");
                txtValorTrans.requestFocus();
                return;
            }
            
            Conta contaOrigem = (Conta) cmbContaOrigemTrans.getSelectedItem();
            Conta contaDestino = (Conta) cmbContaDestinoTrans.getSelectedItem();
            
            if (contaOrigem == null || contaDestino == null) {
                mostrarMensagemErro("Selecione as contas de origem e destino.");
                return;
            }
            
            if (contaOrigem.getId().equals(contaDestino.getId())) {
                mostrarMensagemErro("A conta de origem não pode ser igual à conta de destino.");
                return;
            }
            
            if (contaOrigem.getSaldoAtual().compareTo(valor) < 0) {
                mostrarMensagemErro("Saldo insuficiente na conta de origem.");
                return;
            }
            
            LocalDate data;
            try {
                Date d = txtDataTrans.getDate();
                if (d != null) {
                    data = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    data = LocalDate.now();
                }
            } catch (Exception e) {
                data = LocalDate.now();
            }
            
            // Registrar a transferência
            boolean sucesso = transacaoController.registrarTransferencia(
                txtDescricaoTrans.getText().trim(),
                valor,
                data,
                contaOrigem.getId(),
                contaDestino.getId(),
                txtObservacoesTrans.getText().trim()
            );
            
            if (sucesso) {
                mostrarMensagemSucesso("Transferência realizada com sucesso!");
                limparFormularioTrans();
                carregarDados();
                carregarContasComboBoxes();
                if (updateCallback != null) {
                    updateCallback.onUpdate();
                }
            }
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao realizar transferência: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void limparFormularioTrans() {
        txtDescricaoTrans.setText("");
        txtValorTrans.setText("0.00");
        txtDataTrans.setDate(new Date());
        txtObservacoesTrans.setText("");
        if (cmbContaOrigemTrans.getItemCount() > 0) {
            cmbContaOrigemTrans.setSelectedIndex(0);
            atualizarSaldoOrigemTrans();
        }
        if (cmbContaDestinoTrans.getItemCount() > 0) {
            cmbContaDestinoTrans.setSelectedIndex(0);
            atualizarSaldoDestinoTrans();
        }
    }
}