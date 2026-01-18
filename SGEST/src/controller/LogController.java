package controller;

import model.entity.Log;
import model.dao.LogDAO;
import java.time.LocalDateTime;
import java.util.List;

public class LogController {
    private LogDAO logDAO;
    
    public LogController() {
        this.logDAO = new LogDAO();
        // Garantir que a tabela existe
        logDAO.criarTabela();
    }
    
    public void registrarOperacao(Integer usuarioId, String nomeUsuario, String operacao, 
                                  String descricao, String tabela, Integer registroId) {
        Log log = new Log(usuarioId, nomeUsuario, operacao, descricao, tabela, registroId);
        logDAO.salvar(log);
    }
    
    public void registrarOperacao(Integer usuarioId, String nomeUsuario, String operacao, 
                                  String descricao, String tabela, Integer registroId, String statusOperacao) {
        Log log = new Log(usuarioId, nomeUsuario, operacao, descricao, tabela, registroId);
        log.setStatusOperacao(statusOperacao);
        logDAO.salvar(log);
    }
    
    public List<Log> obterTodosLogs() {
        return logDAO.obterTodos();
    }
    
    public List<Log> obterLogsPorUsuario(Integer usuarioId) {
        return logDAO.obterPorUsuario(usuarioId);
    }
    
    public List<Log> obterLogsPorOperacao(String operacao) {
        return logDAO.obterPorOperacao(operacao);
    }
    
    public List<Log> obterLogsPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return logDAO.obterPorPeriodo(dataInicio, dataFim);
    }
    
    public List<Log> obterLogsPorFiltros(Integer usuarioId, String operacao, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return logDAO.obterPorFiltros(usuarioId, operacao, dataInicio, dataFim);
    }
    
    public Log obterLogPorId(Integer id) {
        return logDAO.obterPorId(id);
    }
    
    public boolean deletarLog(Integer id) {
        return logDAO.deletar(id);
    }
    
    public boolean limparLogosAntigos(int diasRetencao) {
        return logDAO.limparLogosAntigos(diasRetencao);
    }
}
