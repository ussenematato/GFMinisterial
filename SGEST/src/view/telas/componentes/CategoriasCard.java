package view.telas.componentes;

import controller.CategoriaController;
import model.entity.Categoria;
import view.telas.utils.Renderers;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import view.telas.MenuPrincipal;

public class CategoriasCard extends CardBase {
    
    private CategoriaController categoriaController;
    
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
    
    public CategoriasCard(Integer usuarioId, MenuPrincipal menuPrincipal) {
        super(usuarioId, menuPrincipal);
        this.categoriaController = new CategoriaController(usuarioId);
        this.corSelecionada = new Color(33, 150, 243);
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
            BorderFactory.createTitledBorder("Cadastro de Categoria"),
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
        
        txtDescricao = new JTextArea(4, 20);
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
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Categorias Cadastradas"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Modelo da tabela
        String[] colunas = {"ID", "Nome", "Tipo", "Descrição", "Cor", "Status"};
        modelCategorias = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblCategorias = new JTable(modelCategorias);
        tblCategorias.setRowHeight(30);
        tblCategorias.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblCategorias.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblCategorias.getColumnModel().getColumn(4).setCellRenderer(new Renderers.CorCellRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblCategorias);
        
        // Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblFiltro = new JLabel("Filtrar por tipo:");
        JComboBox<String> cmbFiltro = new JComboBox<>(new String[]{"TODOS", "DESPESA", "RECEITA"});
        cmbFiltro.addActionListener(e -> filtrarCategorias(cmbFiltro.getSelectedItem().toString()));
        
        panelFiltros.add(lblFiltro);
        panelFiltros.add(cmbFiltro);
        
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Seleção da tabela
        tblCategorias.getSelectionModel().addListSelectionListener(e -> {
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
        btnSalvar.addActionListener(e -> salvarCategoria());
        btnEditar.addActionListener(e -> editarCategoria());
        btnExcluir.addActionListener(e -> excluirCategoria());
        btnCancelar.addActionListener(e -> cancelar());
        
        // Estado inicial
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        
        return panel;
    }
    
    @Override
    public void carregarDados() {
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
                mostrarMensagemErro("O nome da categoria é obrigatório.");
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
                    mostrarMensagemSucesso("Categoria criada com sucesso!");
                    limparFormulario();
                    carregarDados();
                    if (updateCallback != null) {
                        updateCallback.onUpdate();
                    }
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
                    mostrarMensagemSucesso("Categoria atualizada com sucesso!");
                    limparFormulario();
                    carregarDados();
                    if (updateCallback != null) {
                        updateCallback.onUpdate();
                    }
                }
            }
            
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao salvar categoria: " + e.getMessage());
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
            
            if (confirmarAcao("Tem certeza que deseja desativar a categoria '" + nome + "'?\n" +
                    "A categoria será marcada como inativa.")) {
                
                boolean sucesso = categoriaController.desativarCategoria(id);
                if (sucesso) {
                    mostrarMensagemSucesso("Categoria desativada com sucesso!");
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
}