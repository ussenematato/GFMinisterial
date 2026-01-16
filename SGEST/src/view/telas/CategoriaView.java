package view.telas;

import controller.CategoriaController;
import model.entity.Categoria;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaView extends JDialog {
    private Integer usuarioId;
    private CategoriaController categoriaController;
    private DashboardView dashboardView;
    
    // Componentes
    private JTextField txtNome;
    private JComboBox<String> cmbTipo;
    private JTextArea txtDescricao;
    private JButton btnCor;
    private JTable tblCategorias;
    private DefaultTableModel modelCategorias;
    private JButton btnSalvar;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnCancelar;
    
    private Color corSelecionada;
    private Categoria categoriaEditando;
    
    public CategoriaView(Integer usuarioId, DashboardView dashboardView) {
        super(dashboardView, "Gerenciar Categorias", true);
        this.usuarioId = usuarioId;
        this.dashboardView = dashboardView;
        this.categoriaController = new CategoriaController(usuarioId);
        this.corSelecionada = new Color(33, 150, 243); // Azul padrão
        this.categoriaEditando = null;
        
        initComponents();
        carregarCategorias();
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
        setSize(900, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }
    
    private JPanel criarPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cadastrar/Editar Categoria"));
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
        
        String[] tipos = {"DESPESA", "RECEITA"};
        cmbTipo = new JComboBox<>(tipos);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(cmbTipo, gbc);
        
        // Cor
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Cor:"), gbc);
        
        btnCor = new JButton("Selecionar Cor");
        btnCor.setBackground(corSelecionada);
        btnCor.setForeground(Color.WHITE);
        btnCor.addActionListener(e -> selecionarCor());
        gbc.gridx = 1;
        panel.add(btnCor, gbc);
        
        // Descrição
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Descrição:"), gbc);
        
        txtDescricao = new JTextArea(3, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollDescricao, gbc);
        
        return panel;
    }
    
    private JPanel criarPanelTabela() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Categorias Cadastradas"));
        
        // Modelo da tabela
        String[] colunas = {"ID", "Nome", "Tipo", "Descrição", "Cor", "Status"};
        modelCategorias = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCategorias = new JTable(modelCategorias);
        tblCategorias.setRowHeight(25);
        tblCategorias.setShowGrid(true);
        tblCategorias.setGridColor(new Color(220, 220, 220));
        tblCategorias.getColumnModel().getColumn(4).setCellRenderer(new CorCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblCategorias);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblFiltro = new JLabel("Filtrar por tipo:");
        JComboBox<String> cmbFiltro = new JComboBox<>(new String[]{"TODOS", "DESPESA", "RECEITA"});
        cmbFiltro.addActionListener(e -> filtrarCategorias(cmbFiltro.getSelectedItem().toString()));
        
        panelFiltros.add(lblFiltro);
        panelFiltros.add(cmbFiltro);
        panel.add(panelFiltros, BorderLayout.NORTH);
        
        // Seleção da tabela
        tblCategorias.getSelectionModel().addListSelectionListener(e -> {
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
        btnSalvar.addActionListener(e -> salvarCategoria());
        btnEditar.addActionListener(e -> editarCategoria());
        btnExcluir.addActionListener(e -> excluirCategoria());
        btnCancelar.addActionListener(e -> cancelar());
        
        // Estado inicial
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        
        return panel;
    }
    
    private void carregarCategorias() {
        carregarCategorias("TODOS");
    }
    
    private void carregarCategorias(String tipo) {
        modelCategorias.setRowCount(0);
        
        List<Categoria> categorias;
        if ("TODOS".equals(tipo)) {
            categorias = categoriaController.listarCategorias(null);
        } else {
            categorias = categoriaController.listarCategorias(tipo);
        }
        
        for (Categoria categoria : categorias) {
            Object[] linha = {
                categoria.getId(),
                categoria.getNome(),
                categoria.getTipo(),
                categoria.getDescricao(),
                categoria.getCor(),
                categoria.getAtivo() ? "Ativa" : "Inativa"
            };
            modelCategorias.addRow(linha);
        }
    }
    
    private void filtrarCategorias(String tipo) {
        if ("TODOS".equals(tipo)) {
            carregarCategorias(null);
        } else {
            carregarCategorias(tipo);
        }
    }
    
    private void selecionarCor() {
        Color novaCor = JColorChooser.showDialog(this, "Selecionar Cor", corSelecionada);
        if (novaCor != null) {
            corSelecionada = novaCor;
            btnCor.setBackground(corSelecionada);
        }
    }
    
    private void salvarCategoria() {
        try {
            // Validações
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome da categoria é obrigatório.");
                txtNome.requestFocus();
                return;
            }
            
            String corHex = String.format("#%02x%02x%02x", 
                corSelecionada.getRed(), 
                corSelecionada.getGreen(), 
                corSelecionada.getBlue()
            ).toUpperCase();
            
            if (categoriaEditando == null) {
                // Nova categoria
                boolean sucesso = categoriaController.criarCategoria(
                    txtNome.getText().trim(),
                    cmbTipo.getSelectedItem().toString(),
                    txtDescricao.getText().trim(),
                    corHex
                );
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Categoria criada com sucesso!");
                    limparFormulario();
                    carregarCategorias();
                }
            } else {
                // Editar categoria
                boolean sucesso = categoriaController.atualizarCategoria(
                    categoriaEditando.getId(),
                    txtNome.getText().trim(),
                    cmbTipo.getSelectedItem().toString(),
                    txtDescricao.getText().trim(),
                    corHex
                );
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!");
                    limparFormulario();
                    carregarCategorias();
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar categoria: " + e.getMessage());
        }
    }
    
    private void editarCategoria() {
        int linha = tblCategorias.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelCategorias.getValueAt(linha, 0);
            categoriaEditando = categoriaController.buscarCategoriaPorId(id);
            
            if (categoriaEditando != null) {
                txtNome.setText(categoriaEditando.getNome());
                cmbTipo.setSelectedItem(categoriaEditando.getTipo());
                txtDescricao.setText(categoriaEditando.getDescricao());
                
                // Converter cor hex para Color
                try {
                    String corHex = categoriaEditando.getCor();
                    if (corHex.startsWith("#")) {
                        corSelecionada = Color.decode(corHex);
                    } else {
                        corSelecionada = Color.decode("#" + corHex);
                    }
                    btnCor.setBackground(corSelecionada);
                } catch (Exception e) {
                    corSelecionada = new Color(33, 150, 243);
                    btnCor.setBackground(corSelecionada);
                }
                
                btnSalvar.setText("Atualizar");
            }
        }
    }
    
    private void excluirCategoria() {
        int linha = tblCategorias.getSelectedRow();
        if (linha >= 0) {
            Integer id = (Integer) modelCategorias.getValueAt(linha, 0);
            String nome = (String) modelCategorias.getValueAt(linha, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja desativar a categoria '" + nome + "'?\n" +
                "A categoria será marcada como inativa.",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean sucesso = categoriaController.desativarCategoria(id);
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Categoria desativada com sucesso!");
                    carregarCategorias();
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
        txtDescricao.setText("");
        corSelecionada = new Color(33, 150, 243);
        btnCor.setBackground(corSelecionada);
        categoriaEditando = null;
        btnSalvar.setText("Salvar");
        tblCategorias.clearSelection();
        habilitarBotoesEdicao();
    }
    
    private void habilitarBotoesEdicao() {
        int linhaSelecionada = tblCategorias.getSelectedRow();
        boolean habilitar = linhaSelecionada >= 0;
        
        btnEditar.setEnabled(habilitar);
        btnExcluir.setEnabled(habilitar);
    }
    
    // Renderer para células de cor
    class CorCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof String) {
                String corHex = (String) value;
                try {
                    Color cor;
                    if (corHex.startsWith("#")) {
                        cor = Color.decode(corHex);
                    } else {
                        cor = Color.decode("#" + corHex);
                    }
                    
                    // Criar um círculo colorido
                    JPanel panelCor = new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            g.setColor(cor);
                            g.fillOval(5, 2, 20, 20);
                            g.setColor(Color.BLACK);
                            g.drawOval(5, 2, 20, 20);
                        }
                    };
                    panelCor.setPreferredSize(new Dimension(30, 25));
                    panelCor.setToolTipText(corHex);
                    
                    // Retornar o panel personalizado
                    return panelCor;
                    
                } catch (Exception e) {
                    label.setText(corHex);
                }
            }
            
            return label;
        }
    }
}