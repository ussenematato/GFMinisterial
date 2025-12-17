package Controller;
/**
 *
 * @author ussene
 */
import model.Conta;
import model.ContaDAO;
import model.Conexao;
import java.sql.Connection;
import java.util.List;
import javax.swing.JOptionPane;

public class ContaController {
    private Connection conexao;
    
    public ContaController() {
        this.conexao = Conexao.getConexao();
    }
    
    // Criar nova conta
    public boolean criarConta(String nome, String tipo, double saldoInicial, 
                             String instituicao, int usuarioId) {
        if (nome.isEmpty() || tipo.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Nome e tipo são obrigatórios!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        Conta conta = new Conta();
        conta.setNome(nome);
        conta.setTipo(tipo);
        conta.setSaldoInicial(java.math.BigDecimal.valueOf(saldoInicial));
        conta.setInstituicao(instituicao);
        conta.setUsuarioId(usuarioId);
        
        ContaDAO dao = new ContaDAO(conexao);
        boolean sucesso = dao.criar(conta);
        
        if (sucesso) {
            JOptionPane.showMessageDialog(null, 
                "Conta criada com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "Erro ao criar conta!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return sucesso;
    }
    
    // Listar contas do usuário
    public List<Conta> listarContas(int usuarioId) {
        ContaDAO dao = new ContaDAO(conexao);
        return dao.listarPorUsuario(usuarioId);
    }
    
    // Obter saldo total do usuário
    public double obterSaldoTotal(int usuarioId) {
        List<Conta> contas = listarContas(usuarioId);
        double total = 0;
        
        for (Conta conta : contas) {
            total += conta.getSaldoAtual().doubleValue();
        }
        
        return total;
    }
    
    // Fechar conexão
    public void fechar() {
        Conexao.fecharConexao(conexao);
    }
}
