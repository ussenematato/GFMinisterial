package model.conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Cria o esquema da base de dados SQLite (tabelas e índices) caso ainda não exista.
 * Executado automaticamente ao estabelecer a primeira ligação, tornando o
 * ficheiro database.db autossuficiente em qualquer computador.
 */
public final class DatabaseInitializer {

    private static final String[] SCHEMA = {
        "CREATE TABLE IF NOT EXISTS usuarios (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT NOT NULL," +
            "email TEXT NOT NULL UNIQUE," +
            "telefone TEXT," +
            "senha_hash TEXT NOT NULL," +
            "perfil TEXT NOT NULL DEFAULT 'TESOUREIRO'," +
            "ativo INTEGER DEFAULT 1," +
            "data_cadastro TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP" +
        ")",
        "CREATE INDEX IF NOT EXISTS idx_usuarios_ativo ON usuarios(ativo)",

        "CREATE TABLE IF NOT EXISTS contas (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT NOT NULL," +
            "tipo TEXT NOT NULL," +
            "saldo_inicial NUMERIC DEFAULT 0.00," +
            "saldo_atual NUMERIC DEFAULT 0.00," +
            "instituicao TEXT," +
            "usuario_id INTEGER NOT NULL," +
            "ativo INTEGER DEFAULT 1," +
            "data_criacao TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP" +
        ")",
        "CREATE INDEX IF NOT EXISTS idx_contas_usuario ON contas(usuario_id)",

        "CREATE TABLE IF NOT EXISTS categorias (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nome TEXT NOT NULL," +
            "tipo TEXT NOT NULL," +
            "descricao TEXT," +
            "usuario_id INTEGER NOT NULL," +
            "cor TEXT DEFAULT '#2196F3'," +
            "ativo INTEGER DEFAULT 1" +
        ")",
        "CREATE INDEX IF NOT EXISTS idx_categorias_usuario_tipo ON categorias(usuario_id, tipo)",

        "CREATE TABLE IF NOT EXISTS transacoes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "descricao TEXT NOT NULL," +
            "valor NUMERIC NOT NULL," +
            "tipo TEXT NOT NULL," +
            "data_transacao TEXT NOT NULL," +
            "data_vencimento TEXT," +
            "pago INTEGER DEFAULT 0," +
            "recorrente INTEGER DEFAULT 0," +
            "frequencia TEXT," +
            "conta_id INTEGER NOT NULL," +
            "categoria_id INTEGER," +
            "usuario_id INTEGER NOT NULL," +
            "observacoes TEXT," +
            "data_registro TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
            "transferencia_id INTEGER," +
            "FOREIGN KEY (conta_id) REFERENCES contas(id)," +
            "FOREIGN KEY (categoria_id) REFERENCES categorias(id)" +
        ")",
        "CREATE INDEX IF NOT EXISTS idx_transacoes_conta ON transacoes(conta_id)",
        "CREATE INDEX IF NOT EXISTS idx_transacoes_categoria ON transacoes(categoria_id)",
        "CREATE INDEX IF NOT EXISTS idx_transacoes_usuario_data ON transacoes(usuario_id, data_transacao)",
        "CREATE INDEX IF NOT EXISTS idx_transacoes_tipo_pago ON transacoes(tipo, pago)",
        "CREATE INDEX IF NOT EXISTS idx_transacoes_transferencia ON transacoes(transferencia_id)",
        "CREATE INDEX IF NOT EXISTS idx_transacoes_tipo_data ON transacoes(tipo, data_transacao)",
        "CREATE INDEX IF NOT EXISTS idx_transacoes_usuario_tipo ON transacoes(usuario_id, tipo)",

        "CREATE TABLE IF NOT EXISTS logs (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "usuario_id INTEGER," +
            "nome_usuario TEXT NOT NULL," +
            "operacao TEXT NOT NULL," +
            "descricao TEXT," +
            "tabela TEXT," +
            "registro_id INTEGER," +
            "data_hora TEXT DEFAULT CURRENT_TIMESTAMP," +
            "status_operacao TEXT DEFAULT 'SUCESSO'," +
            "FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL" +
        ")",
        "CREATE INDEX IF NOT EXISTS idx_logs_usuario_id ON logs(usuario_id)",
        "CREATE INDEX IF NOT EXISTS idx_logs_operacao ON logs(operacao)",
        "CREATE INDEX IF NOT EXISTS idx_logs_data_hora ON logs(data_hora)",
        "CREATE INDEX IF NOT EXISTS idx_logs_tabela ON logs(tabela)",
        "CREATE INDEX IF NOT EXISTS idx_logs_usuario_data ON logs(usuario_id, data_hora)",
        "CREATE INDEX IF NOT EXISTS idx_logs_operacao_data ON logs(operacao, data_hora)"
    };

    // Conta SuperAdmin padrão, criada automaticamente se ainda não existir.
    // Senha igual ao email; hash MD5 (mesmo esquema usado em UsuarioController.hashSenha).
    private static final String SUPERADMIN_EMAIL = "superAdmin@mail.com";
    private static final String SUPERADMIN_SENHA_HASH = "e946ce6108c01272baad0cf544c9655c";

    // Categorias padrão (mesmas de gestao_financeira.sql): nome, tipo, descricao, cor
    private static final String[][] CATEGORIAS_PADRAO = {
        {"Dizimos dos dizimos", "DESPESA", "Comparticipacao em 14% dos dizimos", "#990000"},
        {"Comunicação", "DESPESA", "Creditos para comunicação", "#673AB7"},
        {"Transporte", "DESPESA", "Combustível, transporte público", "#009688"},
        {"Assistencias", "DESPESA", "Médico, remédios, plano de saúde, subsidios", "#E91E63"},
        {"Lazer", "DESPESA", "Cinema, restaurantes, viagens", "#3F51B5"},
        {"Educação", "DESPESA", "Cursos, livros, escola", "#9C27B0"},
        {"Escola dominical", "RECEITA", "Colectas nas turmas da escola dominical", "#4CAF50"},
        {"Dizimos", "RECEITA", "Dizimos dos menbros em Comunhao", "#8BC34A"},
        {"Colectas", "RECEITA", "Recebimentos de colectas", "#CDDC39"},
        {"Encontro das Senhoras", "RECEITA", "Colectas recebidas no encontro das senhoras", "#336600"},
        {"Tafula Geral", "RECEITA", "Tafulas dos 2 domingos", "#FFFFCC"},
        {"Tafula das Senhoras", "RECEITA", "Tafulas ref.ao cultos das Mulheres", "#66FF66"},
        {"Água FIPAG", "DESPESA", "Facturas de Agua canalizada", "#FF99CC"},
        {"Energia", "DESPESA", "Compra de energia credelec", "#FF3333"},
        {"Limpeza e Higiene", "DESPESA", "Materiais de limpeza (Detergentes, papelhigienico, ect)", "#FF3333"},
        {"Águas", "DESPESA", "Galões, Caixas de agua pequena", "#2196F3"},
        {"Consumiveis", "DESPESA", "Resmas A4, envelopes", "#2196F3"},
        {"Administração", "DESPESA", "Impressões, Cópias, Creditos para o staff", "#2196F3"},
        {"Departamento de Som", "DESPESA", "Equipamentos, cabos, pilhas para micros", "#2196F3"},
        {"Departamento de Mídia e Comunicação", "DESPESA", "Aquisição de cameras, pilhas, panfletos", "#2196F3"},
        {"Aluguer de Materiais", "DESPESA", "Aluguer de carrinhas de mão, materiais", "#2196F3"},
        {"Simbolos da Ceia", "DESPESA", "Pães, sumos", "#FF3333"},
        {"Manutenções do Templo", "DESPESA", "Matérial eléctrico, canalização, pintura, mão-de-obra", "#FF3333"},
        {"Seminários", "DESPESA", "Lanches, Contribuições para participação", "#FF0000"},
        {"Visitações", "DESPESA", "Visitas a igrejas, congregações, celulas", "#2196F3"},
        {"Contribuições aos Orgãos Ministerias", "DESPESA", "Concilio, Acção Social, Reuniões, DML", "#FF0000"},
        {"Taxas e Impostos", "DESPESA", "Transferencia de valores, taxas municipais e impostos", "#CC0000"},
        {"Fundo de maneio", "DESPESA", "Despesas pequenas correntes", "#FF0000"},
        {"Transferência - Saída", "DESPESA", "Transferência entre contas (saída)", "#9E9E9E"},
        {"Transferência - Entrada", "RECEITA", "Transferência entre contas (entrada)", "#4CAF50"},
        {"Dizimos Regiões da Polana", "RECEITA", "14% dos Dizimos das regiões eclesiasticas da Polana Caniço", "#00CC99"},
        {"Contribuição Para templos Regiao Polana", "DESPESA", "", "#2196F3"}
    };

    // Contas padrão (mesmas de gestao_financeira.sql): nome, tipo, saldo_inicial, saldo_atual, instituicao, ativo
    private static final Object[][] CONTAS_PADRAO = {
        {"Conta Bancaria", "CORRENTE", "5000.00", "45667.54", "Millenium Bim", 1},
        {"Caixa", "CARTEIRA", "500.00", "0.00", "Dinheiro físico", 1},
        {"MPesa", "CARTEIRA", "10000.00", "51.00", "Vodacom, SA", 0},
        {"E-Mola/MPESA", "CARTEIRA", "500.00", "2437.00", "Movitel e Vodacom", 1}
    };

    private DatabaseInitializer() {}

    public static void inicializar(Connection conexao) throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            for (String sql : SCHEMA) {
                stmt.executeUpdate(sql);
            }
        }
        int superAdminId = garantirSuperAdminPadrao(conexao);
        garantirCategoriasPadrao(conexao, superAdminId);
        garantirContasPadrao(conexao, superAdminId);
    }

    private static int garantirSuperAdminPadrao(Connection conexao) throws SQLException {
        try (PreparedStatement verificar = conexao.prepareStatement(
                "SELECT id FROM usuarios WHERE email = ?")) {
            verificar.setString(1, SUPERADMIN_EMAIL);
            try (ResultSet rs = verificar.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        try (PreparedStatement inserir = conexao.prepareStatement(
                "INSERT INTO usuarios (nome, email, senha_hash, perfil, ativo) VALUES (?, ?, ?, ?, 1)",
                Statement.RETURN_GENERATED_KEYS)) {
            inserir.setString(1, "Super Admin");
            inserir.setString(2, SUPERADMIN_EMAIL);
            inserir.setString(3, SUPERADMIN_SENHA_HASH);
            inserir.setString(4, "SUPERADMIN");
            inserir.executeUpdate();

            try (ResultSet rs = inserir.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private static void garantirCategoriasPadrao(Connection conexao, int usuarioId) throws SQLException {
        try (Statement contagem = conexao.createStatement();
             ResultSet rs = contagem.executeQuery("SELECT COUNT(*) FROM categorias")) {
            rs.next();
            if (rs.getInt(1) > 0) {
                return;
            }
        }

        try (PreparedStatement inserir = conexao.prepareStatement(
                "INSERT INTO categorias (nome, tipo, descricao, usuario_id, cor, ativo) VALUES (?, ?, ?, ?, ?, 1)")) {
            for (String[] categoria : CATEGORIAS_PADRAO) {
                inserir.setString(1, categoria[0]);
                inserir.setString(2, categoria[1]);
                inserir.setString(3, categoria[2]);
                inserir.setInt(4, usuarioId);
                inserir.setString(5, categoria[3]);
                inserir.addBatch();
            }
            inserir.executeBatch();
        }
    }

    private static void garantirContasPadrao(Connection conexao, int usuarioId) throws SQLException {
        try (Statement contagem = conexao.createStatement();
             ResultSet rs = contagem.executeQuery("SELECT COUNT(*) FROM contas")) {
            rs.next();
            if (rs.getInt(1) > 0) {
                return;
            }
        }

        try (PreparedStatement inserir = conexao.prepareStatement(
                "INSERT INTO contas (nome, tipo, saldo_inicial, saldo_atual, instituicao, usuario_id, ativo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            for (Object[] conta : CONTAS_PADRAO) {
                inserir.setString(1, (String) conta[0]);
                inserir.setString(2, (String) conta[1]);
                inserir.setBigDecimal(3, new java.math.BigDecimal((String) conta[2]));
                inserir.setBigDecimal(4, new java.math.BigDecimal((String) conta[3]));
                inserir.setString(5, (String) conta[4]);
                inserir.setInt(6, usuarioId);
                inserir.setInt(7, (Integer) conta[5]);
                inserir.addBatch();
            }
            inserir.executeBatch();
        }
    }
}
