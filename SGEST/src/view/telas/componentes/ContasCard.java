package view.telas.componentes;

import controller.ContaController;
import model.entity.Conta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import view.telas.MenuPrincipal;

public class ContasCard extends CardBase {
    
    private ContaController contaController;
    
    // Componentes
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
    
    private Conta contaEditando;
    
    public ContasCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.contaController = new ContaController(usuarioId);
        this.contaEditando = null;
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Painel esquerdo - Formulário
        JPanel panelForm = criarPanelFormulario();
        
        // Painel direito - Tabela
        JPanel panelTabela = criarPanelTabela();
        
        // Painel inferior - Botões
        JPanel panelBotoes = criarPanelBotoes();
        
        // Divisor
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            panelForm,
            panelTabela
        );
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.4);
        
        add(splitPane, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
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
                String.format("R$ %,.2f", conta.getSaldoAtual()),
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
}