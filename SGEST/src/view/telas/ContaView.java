package view.telas;

import controller.ContaController;
import model.entity.Conta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ContaView extends JDialog {
    private Integer usuarioId;
    private ContaController contaController;
    private DashboardView dashboardView;
    
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
    
    public ContaView(Integer usuarioId, DashboardView dashboardView) {
        super(dashboardView, "Gerenciar Contas", true);
        this.usuarioId = usuarioId;
        this.dashboardView = dashboardView;
        this.contaController = new ContaController(usuarioId);
        this.contaEditando = null;
        
        initComponents();
        carregarContas();
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
        add(panelForm, BorderLayout.NORTH);
        add(new JSeparator(), BorderLayout.CENTER);
        add(panelTabela, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
        
        // Configurações da janela
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    private JPanel criarPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cadastrar/Editar Conta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
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
        panel.setBorder(BorderFactory.createTitledBorder("Contas Cadastradas"));
        
        // Modelo da tabela
        String[] colunas = {"ID", "Nome", "Tipo", "Saldo", "Instituição", "Status"};
        modelContas = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblContas = new JTable(modelContas);
        tblContas.setRowHeight(25);
        tblContas.setShowGrid(true);
        tblContas.setGridColor(new Color(220, 220, 220));
        tblContas.getColumnModel().getColumn(3).setCellRenderer(new SaldoRenderer());
        
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
        btnSalvar.addActionListener(e -> salvarConta());
        btnEditar.addActionListener(e -> editarConta());
        btnExcluir.addActionListener(e -> excluirConta());
        btnCancelar.addActionListener(e -> cancelar());
        
        // Estado inicial
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        
        return panel;
    }
    
    private void carregarContas() {
        modelContas.setRowCount(0);
        List<Conta> contas = contaController.listarContasAtivas();
        
        for (Conta conta : contas) {
            Object[] linha = {
                conta.getId(),
                conta.getNome(),
                formatarTipoConta(conta.getTipo()),
                conta.getSaldoAtual(),
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
                JOptionPane.showMessageDialog(this, "O nome da conta é obrigatório.");
                txtNome.requestFocus();
                return;
            }
            
            BigDecimal saldoInicial;
            try {
                saldoInicial = new BigDecimal(txtSaldoInicial.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Saldo inicial inválido.");
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
                    JOptionPane.showMessageDialog(this, "Conta criada com sucesso!");
                    limparFormulario();
                    carregarContas();
                    if (dashboardView != null) {
                        dashboardView.atualizarDashboard();
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
                    JOptionPane.showMessageDialog(this, "Conta atualizada com sucesso!");
                    limparFormulario();
                    carregarContas();
                    if (dashboardView != null) {
                        dashboardView.atualizarDashboard();
                    }
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar conta: " + e.getMessage());
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
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja desativar a conta '" + nome + "'?\n" +
                "A conta será marcada como inativa.",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean sucesso = contaController.desativarConta(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Conta desativada com sucesso!");
                    carregarContas();
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
    
    // Renderer para células de saldo
    class SaldoRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                BigDecimal saldo = (BigDecimal) value;
                setText(String.format("MT %,.2f", saldo));
                
                if (saldo.compareTo(BigDecimal.ZERO) >= 0) {
                    setForeground(new Color(0, 100, 0));
                } else {
                    setForeground(new Color(200, 0, 0));
                }
                
                setHorizontalAlignment(SwingConstants.RIGHT);
            }
            
            return c;
        }
    }
}