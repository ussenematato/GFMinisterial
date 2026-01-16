package view.telas.componentes;

import controller.UsuarioController;
import model.entity.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import view.telas.MenuPrincipal;
import util.UIStyler;

public class AdmiSystemCard extends CardBase {
    private UsuarioController usuarioController;
    
    // Componentes
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JPasswordField txtSenha;
    private JCheckBox chkMostrarSenha;
    private JComboBox<String> cmbPerfil;
    private JTable tblUsuarios;
    private DefaultTableModel modelUsuarios;
    private JButton btnSalvar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnCancelar;
    private JButton btnAlterarSenha;
    
    private Usuario usuarioEditando;
    
    public AdmiSystemCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.usuarioController = new UsuarioController();
        this.usuarioEditando = null;
        
        initComponents();
        carregarUsuarios();
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
        add(panelTabela, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
    }
    
    private JPanel criarPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cadastrar/Editar Usuário"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nome Completo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome Completo:*"), gbc);
        
        txtNome = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtNome, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Email:*"), gbc);
        
        txtEmail = new JTextField(25);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtEmail, gbc);
        
        // Telefone
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Telefone:"), gbc);
        
        txtTelefone = new JTextField(15);
        gbc.gridx = 1;
        panel.add(txtTelefone, gbc);
        
        // Perfil
        gbc.gridx = 2;
        panel.add(new JLabel("Perfil:*"), gbc);
        
        String[] perfis = {"SUPERADMIN", "ADMINISTRADOR", "CONTABILISTA", "TESOUREIRO"};
        cmbPerfil = new JComboBox<>(perfis);
        gbc.gridx = 3;
        panel.add(cmbPerfil, gbc);
        
        // Senha
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Senha:*"), gbc);
        
        txtSenha = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(txtSenha, gbc);
        
        // Checkbox mostrar senha
        chkMostrarSenha = new JCheckBox("Mostrar Senha");
        chkMostrarSenha.addActionListener(e -> alternarExibicaoSenha());
        gbc.gridx = 2;
        panel.add(chkMostrarSenha, gbc);
        
        return panel;
    }
    
    private JPanel criarPanelTabela() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Usuários Cadastrados"));
        
        // Modelo da tabela
        String[] colunas = {"ID", "Nome", "Email", "Telefone", "Perfil", "Status", "Data Cadastro"};
        modelUsuarios = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblUsuarios = new JTable(modelUsuarios);
        tblUsuarios.setRowHeight(25);        tblUsuarios.setShowGrid(true);
        tblUsuarios.setGridColor(new Color(220, 220, 220));        
        JScrollPane scrollPane = new JScrollPane(tblUsuarios);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Seleção da tabela
        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
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
        btnAlterarSenha = new JButton("Alterar Senha");
        btnCancelar = new JButton("Cancelar");
        
        // Estilização simples
        UIStyler.styleSuccessButton(btnSalvar);
        UIStyler.styleSecondaryButton(btnEditar);
        UIStyler.styleDangerButton(btnExcluir);
        UIStyler.styleWarningButton(btnAlterarSenha);
        UIStyler.styleNeutralButton(btnCancelar);
        
        panel.add(btnSalvar);
        panel.add(btnEditar);
        panel.add(btnAlterarSenha);
        panel.add(btnExcluir);
        panel.add(btnCancelar);
        
        // Ações dos botões
        btnSalvar.addActionListener(e -> salvarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnExcluir.addActionListener(e -> excluirUsuario());
        btnAlterarSenha.addActionListener(e -> alterarSenha());
        btnCancelar.addActionListener(e -> cancelar());
        
        // Estado inicial
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnAlterarSenha.setEnabled(false);
        
        return panel;
    }
    
    private void carregarUsuarios() {
        modelUsuarios.setRowCount(0);
        List<Usuario> usuarios = usuarioController.listarUsuarios();
        
        for (Usuario usuario : usuarios) {
            Object[] linha = {
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getPerfil(),
                usuario.getAtivo() ? "Ativo" : "Inativo",
                usuario.getDataCadastro().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            };
            modelUsuarios.addRow(linha);
        }
    }
    
    private void salvarUsuario() {
        try {
            // Validações
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome é obrigatório.", "Validação", JOptionPane.WARNING_MESSAGE);
                txtNome.requestFocus();
                return;
            }
            
            if (txtEmail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email é obrigatório.", "Validação", JOptionPane.WARNING_MESSAGE);
                txtEmail.requestFocus();
                return;
            }
            
            String senha = new String(txtSenha.getPassword());
            
            if (usuarioEditando == null && senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Senha é obrigatória para novo usuário.", "Validação", JOptionPane.WARNING_MESSAGE);
                txtSenha.requestFocus();
                return;
            }
            
            if (usuarioEditando == null) {
                // Novo usuário
                boolean sucesso = usuarioController.criarUsuario(
                    txtNome.getText().trim(),
                    txtEmail.getText().trim(),
                    txtTelefone.getText().trim(),
                    senha,
                    (String) cmbPerfil.getSelectedItem()
                );
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Usuário criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                    carregarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao criar usuário. Verifique os dados.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Editar usuário
                boolean sucesso = usuarioController.atualizarUsuario(
                    usuarioEditando.getId(),
                    txtNome.getText().trim(),
                    txtEmail.getText().trim(),
                    txtTelefone.getText().trim(),
                    (String) cmbPerfil.getSelectedItem()
                );
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparFormulario();
                    carregarUsuarios();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarUsuario() {
        int linha = tblUsuarios.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelUsuarios.getValueAt(linha, 0);
            Usuario usuario = usuarioController.buscarPorId(id);
            
            if (usuario != null) {
                usuarioEditando = usuario;
                txtNome.setText(usuario.getNome());
                txtEmail.setText(usuario.getEmail());
                txtTelefone.setText(usuario.getTelefone() != null ? usuario.getTelefone() : "");
                cmbPerfil.setSelectedItem(usuario.getPerfil());
                txtSenha.setText("");
                chkMostrarSenha.setSelected(false);
                
                btnSalvar.setText("Atualizar");
                txtNome.requestFocus();
            }
        }
    }
    
    private void excluirUsuario() {
        int linha = tblUsuarios.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelUsuarios.getValueAt(linha, 0);
            String nome = (String) modelUsuarios.getValueAt(linha, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Desativar o usuário '" + nome + "'?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (usuarioController.desativarUsuario(id)) {
                    JOptionPane.showMessageDialog(this, "Usuário desativado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarUsuarios();
                }
            }
        }
    }
    
    private void alterarSenha() {
        int linha = tblUsuarios.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelUsuarios.getValueAt(linha, 0);
            String nome = (String) modelUsuarios.getValueAt(linha, 1);
            
            JPasswordField passwordField = new JPasswordField();
            JCheckBox showPassword = new JCheckBox("Mostrar Senha");
            showPassword.addActionListener(e -> {
                if (showPassword.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('•');
                }
            });
            
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("Nova Senha:"), gbc);
            
            gbc.gridx = 1;
            panel.add(passwordField, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            panel.add(showPassword, gbc);
            
            int result = JOptionPane.showConfirmDialog(this, panel, 
                "Alterar Senha de " + nome, JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                String novaSenha = new String(passwordField.getPassword());
                if (usuarioController.atualizarSenha(id, novaSenha)) {
                    JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao alterar senha.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void cancelar() {
        limparFormulario();
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtSenha.setText("");
        cmbPerfil.setSelectedIndex(3); // TESOUREIRO por padrão
        chkMostrarSenha.setSelected(false);
        usuarioEditando = null;
        btnSalvar.setText("Salvar");
        tblUsuarios.clearSelection();
        habilitarBotoesEdicao();
    }
    
    private void habilitarBotoesEdicao() {
        int linhaSelecionada = tblUsuarios.getSelectedRow();
        boolean habilitar = linhaSelecionada >= 0;
        
        btnEditar.setEnabled(habilitar);
        btnExcluir.setEnabled(habilitar);
        btnAlterarSenha.setEnabled(habilitar);
    }
    
    private void alternarExibicaoSenha() {
        if (chkMostrarSenha.isSelected()) {
            txtSenha.setEchoChar((char) 0);
        } else {
            txtSenha.setEchoChar('•');
        }
    }
    
    @Override
    public void carregarDados() {
        carregarUsuarios();
    }
}
