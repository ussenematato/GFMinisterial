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

    private DatabaseInitializer() {}

    public static void inicializar(Connection conexao) throws SQLException {
        try (Statement stmt = conexao.createStatement()) {
            for (String sql : SCHEMA) {
                stmt.executeUpdate(sql);
            }
        }
        garantirSuperAdminPadrao(conexao);
    }

    private static void garantirSuperAdminPadrao(Connection conexao) throws SQLException {
        try (PreparedStatement verificar = conexao.prepareStatement(
                "SELECT COUNT(*) FROM usuarios WHERE email = ?")) {
            verificar.setString(1, SUPERADMIN_EMAIL);
            try (ResultSet rs = verificar.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    return;
                }
            }
        }

        try (PreparedStatement inserir = conexao.prepareStatement(
                "INSERT INTO usuarios (nome, email, senha_hash, perfil, ativo) VALUES (?, ?, ?, ?, 1)")) {
            inserir.setString(1, "Super Admin");
            inserir.setString(2, SUPERADMIN_EMAIL);
            inserir.setString(3, SUPERADMIN_SENHA_HASH);
            inserir.setString(4, "SUPERADMIN");
            inserir.executeUpdate();
        }
    }
}
