package view.telas;

import controller.UsuarioController;
import model.entity.Usuario;
import model.dao.UsuarioDAO;
import util.UIStyler;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class UserProfileView extends JDialog {
    private Integer usuarioId;
    private UsuarioController usuarioController;
    private UsuarioDAO usuarioDAO;
    
    // Componentes
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JLabel lblPerfil;
    private JPasswordField txtSenhaAtual;
    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmaSenha;
    
    public UserProfileView(Integer usuarioId, JFrame parent) {
        super(parent, "Meu Perfil", true);
        this.usuarioId = usuarioId;
        this.usuarioController = new UsuarioController();
        this.usuarioDAO = new UsuarioDAO();
        
        initComponents();
        carregarDadosUsuario();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        
        // Painel com abas
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Aba 1: Dados Pessoais
        tabbedPane.addTab("Dados Pessoais", criarPainelDadosPessoais());
        
        // Aba 2: Segurança
        tabbedPane.addTab("Segurança", criarPainelSeguranca());
        
        add(tabbedPane, BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelDadosPessoais() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nome
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(lblNome, gbc);
        
        txtNome = new JTextField(30);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtNome, gbc);
        
        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(lblEmail, gbc);
        
        txtEmail = new JTextField(30);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtEmail, gbc);
        
        // Telefone
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(lblTelefone, gbc);
        
        txtTelefone = new JTextField(30);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtTelefone, gbc);
        
        // Perfil
        JLabel lblPerfilLabel = new JLabel("Perfil:");
        lblPerfilLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(lblPerfilLabel, gbc);
        
        lblPerfil = new JLabel("");
        lblPerfil.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(lblPerfil, gbc);
        
        // Espaço em branco
        gbc.gridy = 4;
        gbc.weighty = 1;
        panel.add(new JLabel(), gbc);
        
        return panel;
    }
    
    private JPanel criarPainelSeguranca() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Informação
        JLabel lblInfo = new JLabel("Altere sua senha com segurança");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblInfo, gbc);
        
        // Linha separadora
        gbc.gridy = 1;
        JSeparator separator = new JSeparator();
        panel.add(separator, gbc);
        
        // Senha Atual
        JLabel lblSenhaAtual = new JLabel("Senha Atual:");
        lblSenhaAtual.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(lblSenhaAtual, gbc);
        
        txtSenhaAtual = new JPasswordField(30);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtSenhaAtual, gbc);
        
        // Nova Senha
        JLabel lblNovaSenha = new JLabel("Nova Senha:");
        lblNovaSenha.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(lblNovaSenha, gbc);
        
        txtNovaSenha = new JPasswordField(30);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtNovaSenha, gbc);
        
        // Confirma Senha
        JLabel lblConfirmaSenha = new JLabel("Confirmar Senha:");
        lblConfirmaSenha.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        panel.add(lblConfirmaSenha, gbc);
        
        txtConfirmaSenha = new JPasswordField(30);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(txtConfirmaSenha, gbc);
        
        // Requisitos
        JLabel lblRequisitos = new JLabel("<html><b>Requisitos:</b><br>• Mínimo de 6 caracteres<br>• Deve conter letra e número</html>");
        lblRequisitos.setFont(new Font("Arial", Font.PLAIN, 11));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(lblRequisitos, gbc);
        
        // Espaço em branco
        gbc.gridy = 6;
        gbc.weighty = 1;
        panel.add(new JLabel(), gbc);
        
        return panel;
    }
    
    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnSalvar = new JButton("Salvar Alterações");
        JButton btnCancelar = new JButton("Cancelar");
        
        UIStyler.styleSuccessButton(btnSalvar);
        UIStyler.styleNeutralButton(btnCancelar);
        
        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());
        
        panel.add(btnSalvar);
        panel.add(btnCancelar);
        
        return panel;
    }
    
    private void carregarDadosUsuario() {
        try {
            Usuario usuario = usuarioDAO.buscarPorId(usuarioId);
            if (usuario != null) {
                txtNome.setText(usuario.getNome());
                txtEmail.setText(usuario.getEmail());
                txtTelefone.setText(usuario.getTelefone() != null ? usuario.getTelefone() : "");
                lblPerfil.setText(usuario.getPerfil() != null ? usuario.getPerfil() : "N/A");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do usuário: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void salvarAlteracoes() {
        // Validar campos
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome não pode estar vazio", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email não pode estar vazio", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Atualizar dados pessoais
        if (usuarioController.atualizarUsuario(usuarioId, 
            txtNome.getText().trim(),
            txtEmail.getText().trim(),
            txtTelefone.getText().trim(),
            null)) {
            
            // Se houver alteração de senha
            if (txtNovaSenha.getPassword().length > 0) {
                // Verificar se senhas conferem
                if (!new String(txtNovaSenha.getPassword()).equals(new String(txtConfirmaSenha.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "As senhas não conferem", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Atualizar senha
                if (usuarioController.atualizarSenha(usuarioId, new String(txtNovaSenha.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar senha", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar dados", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
