package model.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/gestao_financeira";
    private static final String USUARIO = "root";
    private static final String SENHA = "";
    private static Connection conexao;
    
    private Conexao() {}
    
    public static Connection getConexao() {
        if (conexao == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão estabelecida com sucesso!");
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Erro na conexão: " + e.getMessage());
                throw new RuntimeException("Falha ao conectar ao banco de dados");
            }
        }
        return conexao;
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