package view.telas;

import model.entity.Log;
import model.dao.LogDAO;
import util.UIStyler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogsView extends JDialog {
    private LogDAO logDAO;
    private JTable tabelaLogs;
    private DefaultTableModel modeloTabela;
    private JComboBox<String> cmbOperacao;
    private JComboBox<String> cmbUsuario;
    private JComboBox<String> cmbPeriodo;
    private JLabel lblTotalRegistros;
    
    public LogsView(JFrame parent, Integer usuarioId) {
        super(parent, "Gestão de Logs - Auditoria", true);
        this.logDAO = new LogDAO();
        
        // Garantir que a tabela de logs existe
        logDAO.criarTabela();
        
        initComponents();
        carregarDados();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setSize(1100, 650);
        setLocationRelativeTo(getParent());
        
        // Painel de filtros
        JPanel painelFiltros = criarPainelFiltros();
        add(painelFiltros, BorderLayout.NORTH);
        
        // Tabela de logs
        criarTabela();
        JScrollPane scrollPane = new JScrollPane(tabelaLogs);
        add(scrollPane, BorderLayout.CENTER);
        
        // Painel inferior
        JPanel painelInferior = criarPainelInferior();
        add(painelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelFiltros() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        panel.setBackground(new Color(240, 240, 240));
        
        // Filtro de período
        panel.add(new JLabel("Período:"));
        cmbPeriodo = new JComboBox<>(new String[]{
            "Todas",
            "Hoje",
            "Últimos 7 dias",
            "Últimos 30 dias",
            "Este mês",
            "Este ano"
        });
        panel.add(cmbPeriodo);
        
        // Filtro de operação
        panel.add(new JLabel("Operação:"));
        cmbOperacao = new JComboBox<>(new String[]{
            "Todas",
            "CRIAR",
            "ATUALIZAR",
            "DELETAR",
            "VISUALIZAR"
        });
        panel.add(cmbOperacao);
        
        // Filtro de usuário
        panel.add(new JLabel("Usuário:"));
        cmbUsuario = new JComboBox<>();
        cmbUsuario.addItem("Todos");
        carregarUsuariosUnicos();
        panel.add(cmbUsuario);
        
        // Botão aplicar filtros
        JButton btnAplicarFiltros = new JButton("Aplicar Filtros");
        UIStyler.styleSuccessButton(btnAplicarFiltros);
        btnAplicarFiltros.addActionListener(e -> aplicarFiltros());
        panel.add(btnAplicarFiltros);
        
        // Botão limpar filtros
        JButton btnLimparFiltros = new JButton("Limpar Filtros");
        UIStyler.styleNeutralButton(btnLimparFiltros);
        btnLimparFiltros.addActionListener(e -> limparFiltros());
        panel.add(btnLimparFiltros);
        
        panel.add(Box.createHorizontalGlue());
        
        // Label de total de registros
        lblTotalRegistros = new JLabel("Total: 0 registros");
        lblTotalRegistros.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblTotalRegistros);
        
        return panel;
    }
    
    private void criarTabela() {
        String[] colunas = {"ID", "Data/Hora", "Usuário", "Operação", "Tabela", "Registro ID", "Descrição", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaLogs = new JTable(modeloTabela);
        tabelaLogs.setFont(new Font("Arial", Font.PLAIN, 11));
        tabelaLogs.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tabelaLogs.setRowHeight(25);
        tabelaLogs.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Ajustar largura das colunas
        tabelaLogs.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tabelaLogs.getColumnModel().getColumn(1).setPreferredWidth(150); // Data/Hora
        tabelaLogs.getColumnModel().getColumn(2).setPreferredWidth(120); // Usuário
        tabelaLogs.getColumnModel().getColumn(3).setPreferredWidth(100); // Operação
        tabelaLogs.getColumnModel().getColumn(4).setPreferredWidth(100); // Tabela
        tabelaLogs.getColumnModel().getColumn(5).setPreferredWidth(80);  // Registro ID
        tabelaLogs.getColumnModel().getColumn(6).setPreferredWidth(250); // Descrição
        tabelaLogs.getColumnModel().getColumn(7).setPreferredWidth(80);  // Status
    }
    
    private JPanel criarPainelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));
        
        JButton btnExportarLogs = new JButton("Exportar para Excel");
        JButton btnLimparLogosAntigos = new JButton("Limpar Logs Antigos");
        JButton btnFechar = new JButton("Fechar");
        
        UIStyler.styleSuccessButton(btnExportarLogs);
        UIStyler.styleWarningButton(btnLimparLogosAntigos);
        UIStyler.styleNeutralButton(btnFechar);
        
        btnExportarLogs.addActionListener(e -> exportarLogsParaExcel());
        btnLimparLogosAntigos.addActionListener(e -> limparLogosAntigos());
        btnFechar.addActionListener(e -> dispose());
        
        panel.add(btnExportarLogs);
        panel.add(btnLimparLogosAntigos);
        panel.add(btnFechar);
        
        return panel;
    }
    
    private void carregarUsuariosUnicos() {
        List<Log> todosLogs = logDAO.obterTodos();
        java.util.Set<String> usuariosUnicos = new java.util.HashSet<>();
        
        for (Log log : todosLogs) {
            usuariosUnicos.add(log.getNomeUsuario());
        }
        
        java.util.List<String> usuariosOrdenados = new java.util.ArrayList<>(usuariosUnicos);
        java.util.Collections.sort(usuariosOrdenados);
        
        for (String usuario : usuariosOrdenados) {
            cmbUsuario.addItem(usuario);
        }
    }
    
    private void carregarDados() {
        List<Log> logs = logDAO.obterTodos();
        modeloTabela.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        for (Log log : logs) {
            Object[] row = {
                log.getId(),
                log.getDataHora().format(formatter),
                log.getNomeUsuario(),
                log.getOperacao(),
                log.getTabela(),
                log.getRegistroId(),
                log.getDescricao(),
                log.getStatusOperacao()
            };
            modeloTabela.addRow(row);
        }
        
        lblTotalRegistros.setText("Total: " + logs.size() + " registros");
    }
    
    private void aplicarFiltros() {
        String periodo = (String) cmbPeriodo.getSelectedItem();
        String operacao = (String) cmbOperacao.getSelectedItem();
        String usuario = (String) cmbUsuario.getSelectedItem();
        
        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = LocalDateTime.now();
        
        // Determinar período
        LocalDate hoje = LocalDate.now();
        switch (periodo) {
            case "Hoje":
                dataInicio = hoje.atStartOfDay();
                break;
            case "Últimos 7 dias":
                dataInicio = hoje.minusDays(7).atStartOfDay();
                break;
            case "Últimos 30 dias":
                dataInicio = hoje.minusDays(30).atStartOfDay();
                break;
            case "Este mês":
                dataInicio = hoje.withDayOfMonth(1).atStartOfDay();
                break;
            case "Este ano":
                dataInicio = hoje.withDayOfYear(1).atStartOfDay();
                break;
        }
        
        // Obter logs com filtros
        List<Log> logsFiltrados;
        
        if (periodo.equals("Todas")) {
            logsFiltrados = logDAO.obterTodos();
        } else {
            logsFiltrados = logDAO.obterPorPeriodo(dataInicio, dataFim);
        }
        
        // Filtrar por operação
        if (!operacao.equals("Todas")) {
            logsFiltrados.removeIf(log -> !log.getOperacao().equals(operacao));
        }
        
        // Filtrar por usuário
        if (!usuario.equals("Todos")) {
            logsFiltrados.removeIf(log -> !log.getNomeUsuario().equals(usuario));
        }
        
        // Atualizar tabela
        modeloTabela.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        for (Log log : logsFiltrados) {
            Object[] row = {
                log.getId(),
                log.getDataHora().format(formatter),
                log.getNomeUsuario(),
                log.getOperacao(),
                log.getTabela(),
                log.getRegistroId(),
                log.getDescricao(),
                log.getStatusOperacao()
            };
            modeloTabela.addRow(row);
        }
        
        lblTotalRegistros.setText("Total: " + logsFiltrados.size() + " registros");
    }
    
    private void limparFiltros() {
        cmbPeriodo.setSelectedIndex(0);
        cmbOperacao.setSelectedIndex(0);
        cmbUsuario.setSelectedIndex(0);
        carregarDados();
    }
    
    private void exportarLogsParaExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new java.io.File("Logs_" + LocalDate.now() + ".xlsx"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Logs");
                
                // Criar cabeçalho
                org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
                String[] headers = {"ID", "Data/Hora", "Usuário", "Operação", "Tabela", "Registro ID", "Descrição", "Status"};
                
                org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.LIGHT_BLUE.getIndex());
                headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
                
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                
                for (int i = 0; i < headers.length; i++) {
                    org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // Preencher dados
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < modeloTabela.getColumnCount(); j++) {
                        Object valor = modeloTabela.getValueAt(i, j);
                        row.createCell(j).setCellValue(valor != null ? valor.toString() : "");
                    }
                }
                
                // Ajustar largura das colunas
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                // Salvar arquivo
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(fileChooser.getSelectedFile())) {
                    workbook.write(fos);
                    workbook.close();
                    JOptionPane.showMessageDialog(this, "Logs exportados com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar logs: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void limparLogosAntigos() {
        String input = JOptionPane.showInputDialog(this, "Informe a quantidade de dias de retenção (ex: 30):", "30");
        
        if (input != null && !input.isEmpty()) {
            try {
                int dias = Integer.parseInt(input);
                if (logDAO.limparLogosAntigos(dias)) {
                    JOptionPane.showMessageDialog(this, "Logs antigos removidos com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarDados();
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum log foi removido.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
