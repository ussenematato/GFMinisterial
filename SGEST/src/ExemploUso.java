
import controller.CategoriaController;
import controller.ContaController;
import controller.TransacaoController;
import java.math.BigDecimal;
import java.time.LocalDate;

// Exemplo de uso dos controllers
public class ExemploUso {
    public static void main(String[] args) {
        Integer usuarioId = 1; // ID do usuário logado
        
        // Gerenciar Contas
        ContaController contaController = new ContaController(usuarioId);
        
        // Criar nova conta
        contaController.criarConta("Nubank", "CORRENTE", 
                                  new BigDecimal("1000.00"), "Nubank");
        
        // Listar contas
        var contas = contaController.listarContasAtivas();
        contas.forEach(System.out::println);
        
        // Gerenciar Categorias
        CategoriaController categoriaController = new CategoriaController(usuarioId);
        
        // Criar categoria
        categoriaController.criarCategoria("Alimentação", "DESPESA", 
                                          "Gastos com comida", "#FF5722");
        
        // Listar categorias de despesa
        var categoriasDespesa = categoriaController.listarCategoriasDespesa();
        
        // Gerenciar Transações
        TransacaoController transacaoController = new TransacaoController(usuarioId);
        
        // Registrar despesa
        transacaoController.registrarTransacao(
            "Supermercado",
            new BigDecimal("150.50"),
            "DESPESA",
            LocalDate.now(),
            1, // ID da conta
            1, // ID da categoria
            "Compra mensal"
        );
        
        // Obter relatório do mês
        LocalDate inicio = LocalDate.now().withDayOfMonth(1);
        LocalDate fim = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        
        BigDecimal totalDespesas = transacaoController.obterTotalDespesas(inicio, fim);
        BigDecimal totalReceitas = transacaoController.obterTotalReceitas(inicio, fim);
        
        System.out.println("Despesas: R$ " + totalDespesas);
        System.out.println("Receitas: R$ " + totalReceitas);
        System.out.println("Saldo: R$ " + totalReceitas.subtract(totalDespesas));
    }
}