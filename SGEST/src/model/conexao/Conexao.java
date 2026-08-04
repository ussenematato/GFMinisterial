package model.conexao;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    private static final String NOME_BASE = "database.db";
    private static Connection conexao;

    private Conexao() {}

    public static Connection getConexao() {
        if (conexao == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:" + caminhoBaseDados();
                conexao = DriverManager.getConnection(url);

                try (Statement stmt = conexao.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }

                DatabaseInitializer.inicializar(conexao);

                System.out.println("Conexão estabelecida com sucesso! (" + url + ")");
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Erro na conexão: " + e.getMessage());
                throw new RuntimeException("Falha ao conectar ao banco de dados", e);
            }
        }
        return conexao;
    }

    /**
     * Resolve o caminho do ficheiro database.db na mesma pasta onde o
     * programa está a ser executado (pasta do .jar), para que a base
     * acompanhe a aplicação em qualquer computador.
     */
    private static String caminhoBaseDados() {
        try {
            File local = new File(Conexao.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            // Se a aplicação corre a partir de um .jar, a pasta do programa é a
            // que contém o .jar; se corre a partir de classes soltas (ex.: build/classes,
            // como no "Run" do NetBeans), a própria pasta de classes é usada.
            File pastaPrograma = local.isDirectory() ? local : local.getParentFile();
            return new File(pastaPrograma, NOME_BASE).getAbsolutePath();
        } catch (URISyntaxException | NullPointerException e) {
            return new File(NOME_BASE).getAbsolutePath();
        }
    }

    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null;
                System.out.println("Conexão fechada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}
