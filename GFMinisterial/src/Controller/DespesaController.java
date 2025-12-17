package Controller;
/**
 *
 * @author ussene
 */
import DAO.TransacaoDAO;
import Model.Transacao;
import model.Transacao;
import model.TransacaoDAO;
import model.Conexao;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.math.BigDecimal;
import javax.swing.JOptionPane;

public class DespesaController {
    private Connection conexao;
    
    public DespesaController() {
        this.conexao = Conexao.getConexao();
    }
    
    // Registrar nova despesa
    public boolean registrarDespesa(String descricao, double valor, LocalDate data, 
                                   LocalDate vencimento, int contaId, int categoriaId, 
                                   int usuarioId, String observacoes, boolean pago) {
        if (descricao.isEmpty() || valor <= 0) {
            JOptionPane.showMessageDialog(null, 
                "Descrição e valor são obrigatórios!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        Transacao despesa = new Transacao();
        despesa.setDescricao(descricao);
        despesa.setValor(BigDecimal.valueOf(valor));
        despesa.setTipo("Despesa");
        despesa.setDataTransacao(data);
        despesa.setDataVencimento(vencimento);
        despesa.setContaId(contaId);
        despesa.setCategoriaId(categoriaId);
        despesa.setUsuarioId(usuarioId);
        despesa.setObservacoes(observacoes);
        despesa.setPago(pago);
        
        TransacaoDAO dao = new TransacaoDAO(conexao);
        boolean sucesso = dao.registrar(despesa);
        
        if (sucesso) {
            JOptionPane.showMessageDialog(null, 
                "Despesa registrada com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "Erro ao registrar despesa!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return sucesso;
    }
    
    // Registrar nova receita
    public boolean registrarReceita(String descricao, double valor, LocalDate data, 
                                   int contaId, int categoriaId, int usuarioId, 
                                   String observacoes) {
        if (descricao.isEmpty() || valor <= 0) {
            JOptionPane.showMessageDialog(null, 
                "Descrição e valor são obrigatórios!", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        Transacao receita = new Transacao();
        receita.setDescricao(descricao);
        receita.setValor(BigDecimal.valueOf(valor));
        receita.setTipo("Receita");
        receita.setDataTransacao(data);
        receita.setDataVencimento(data);
        receita.setContaId(contaId);
        receita.setCategoriaId(categoriaId);
        receita.setUsuarioId(usuarioId);
        receita.setObservacoes(observacoes);
        receita.setPago(true); // Receitas geralmente são pagas
        
        TransacaoDAO dao = new TransacaoDAO(conexao);
        boolean sucesso = dao.registrar(receita);
        
        if (sucesso) {
            JOptionPane.showMessageDialog(null, 
                "Receita registrada com sucesso!", 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "Erro ao registrar receita!", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return sucesso;
    }
    
    // Listar transações do mês atual
    public List<Transacao> listarTransacoesMesAtual(int usuarioId) {
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        
        TransacaoDAO dao = new TransacaoDAO(conexao);
        return dao.listarPorUsuario(usuarioId, inicio, fim);
    }
    
    // Listar despesas do mês atual
    public List<Transacao> listarDespesasMesAtual(int usuarioId) {
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        
        TransacaoDAO dao = new TransacaoDAO(conexao);
        return dao.listarDespesas(usuarioId, inicio, fim);
    }
    
    // Obter resumo financeiro do mês
    public String obterResumoMes(int usuarioId) {
        YearMonth mesAtual = YearMonth.now();
        LocalDate inicio = mesAtual.atDay(1);
        LocalDate fim = mesAtual.atEndOfMonth();
        
        TransacaoDAO dao = new TransacaoDAO(conexao);
        BigDecimal totalDespesas = dao.obterTotalDespesas(usuarioId, inicio, fim);
        BigDecimal totalReceitas = dao.obterTotalReceitas(usuarioId, inicio, fim);
        BigDecimal saldo = totalReceitas.subtract(totalDespesas);
        
        return String.format(
            "Resumo do Mês:\n" +
            "Receitas: R$ %.2f\n" +
            "Despesas: R$ %.2f\n" +
            "Saldo: R$ %.2f",
            totalReceitas, totalDespesas, saldo
        );
    }
    
    // Fechar conexão
    public void fechar() {
        Conexao.fecharConexao(conexao);
    }
}
