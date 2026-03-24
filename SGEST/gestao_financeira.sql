-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 24/03/2026 às 13:54
-- Versão do servidor: 10.4.28-MariaDB
-- Versão do PHP: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `gestao_financeira`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `categorias`
--

CREATE TABLE `categorias` (
  `id` int(11) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `descricao` text DEFAULT NULL,
  `usuario_id` int(11) NOT NULL,
  `cor` varchar(7) DEFAULT '#2196F3',
  `ativo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `categorias`
--

INSERT INTO `categorias` (`id`, `nome`, `tipo`, `descricao`, `usuario_id`, `cor`, `ativo`) VALUES
(1, 'Dizimos dos dizimos', 'DESPESA', 'Comparticipacao em 14% dos dizimos', 1, '#990000', 1),
(2, 'Comunicação', 'DESPESA', 'Creditos para comunicação', 1, '#673AB7', 1),
(3, 'Transporte', 'DESPESA', 'Combustível, transporte público', 1, '#009688', 1),
(4, 'Assistencias', 'DESPESA', 'Médico, remédios, plano de saúde, subsidios', 1, '#E91E63', 1),
(5, 'Lazer', 'DESPESA', 'Cinema, restaurantes, viagens', 1, '#3F51B5', 1),
(6, 'Educação', 'DESPESA', 'Cursos, livros, escola', 1, '#9C27B0', 1),
(7, 'Escola dominical', 'RECEITA', 'Colectas nas turmas da escola dominical', 1, '#4CAF50', 1),
(8, 'Dizimos', 'RECEITA', 'Dizimos dos menbros em Comunhao', 1, '#8BC34A', 1),
(9, 'Colectas', 'RECEITA', 'Recebimentos de colectas', 1, '#CDDC39', 1),
(10, 'Encontro das Senhoras', 'RECEITA', 'Colectas recebidas no encontro das senhoras', 1, '#336600', 1),
(11, 'Tafula Geral', 'RECEITA', 'Tafulas dos 2 domingos', 1, '#FFFFCC', 1),
(12, 'Tafula das Senhoras', 'RECEITA', 'Tafulas ref.ao cultos das Mulheres', 1, '#66FF66', 1),
(13, 'Água FIPAG', 'DESPESA', 'Facturas de Agua canalizada', 1, '#FF99CC', 1),
(14, 'Energia', 'DESPESA', 'Compra de energia credelec', 1, '#FF3333', 1),
(15, 'Limpeza e Higiene', 'DESPESA', 'Materiais de limpeza (Detergentes, papelhigienico, ect)', 1, '#FF3333', 1),
(16, 'Águas', 'DESPESA', 'Galões, Caixas de agua pequena', 1, '#2196F3', 1),
(17, 'Consumiveis', 'DESPESA', 'Resmas A4, envelopes', 1, '#2196F3', 1),
(18, 'Administração', 'DESPESA', 'Impressões, Cópias, Creditos para o staff', 1, '#2196F3', 1),
(19, 'Departamento de Som', 'DESPESA', 'Equipamentos, cabos, pilhas para micros', 1, '#2196F3', 1),
(20, 'Departamento de Mídia e Comunicação', 'DESPESA', 'Aquisição de cameras, pilhas, panfletos', 1, '#2196F3', 1),
(21, 'Aluguer de Materiais', 'DESPESA', 'Aluguer de carrinhas de mão, materiais', 1, '#2196F3', 1),
(22, 'Simbolos da Ceia', 'DESPESA', 'Pães, sumos', 1, '#FF3333', 1),
(23, 'Manutenções do Templo', 'DESPESA', 'Matérial eléctrico, canalização, pintura, mão-de-obra', 1, '#FF3333', 1),
(24, 'Seminários', 'DESPESA', 'Lanches, Contribuições para participação', 1, '#FF0000', 1),
(25, 'Visitações', 'DESPESA', 'Visitas a igrejas, congregações, celulas', 1, '#2196F3', 1),
(26, 'Contribuições aos Orgãos Ministerias', 'DESPESA', 'Concilio, Acção Social, Reuniões, DML', 1, '#FF0000', 1),
(27, 'Taxas e Impostos', 'DESPESA', 'Transferencia de valores, taxas municipais e impostos', 1, '#CC0000', 1),
(28, 'Fundo de maneio', 'DESPESA', 'Despesas pequenas correntes', 1, '#FF0000', 1),
(29, 'Transferência - Saída', 'DESPESA', 'Transferência entre contas (saída)', 1, '#9E9E9E', 1),
(30, 'Transferência - Entrada', 'RECEITA', 'Transferência entre contas (entrada)', 1, '#4CAF50', 1),
(31, 'Dizimos Regiões da Polana', 'RECEITA', '14% dos Dizimos das regiões eclesiasticas da Polana Caniço', 1, '#00CC99', 1),
(32, 'Contribuição Para templos Regiao Polana', 'DESPESA', '', 1, '#2196F3', 1);

-- --------------------------------------------------------

--
-- Estrutura para tabela `contas`
--

CREATE TABLE `contas` (
  `id` int(11) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `saldo_inicial` decimal(10,2) DEFAULT 0.00,
  `saldo_atual` decimal(10,2) DEFAULT 0.00,
  `instituicao` varchar(100) DEFAULT NULL,
  `usuario_id` int(11) NOT NULL,
  `ativo` tinyint(1) DEFAULT 1,
  `data_criacao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `contas`
--

INSERT INTO `contas` (`id`, `nome`, `tipo`, `saldo_inicial`, `saldo_atual`, `instituicao`, `usuario_id`, `ativo`, `data_criacao`) VALUES
(1, 'Conta Bancaria', 'CORRENTE', 5000.00, 45667.54, 'Millenium Bim', 1, 1, '2026-01-09 14:24:24'),
(2, 'Caixa', 'CARTEIRA', 500.00, 0.00, 'Dinheiro físico', 1, 1, '2026-01-09 14:24:24'),
(3, 'MPesa', 'CARTEIRA', 10000.00, 51.00, 'Vodacom, SA', 1, 0, '2026-01-09 14:24:24'),
(4, 'E-Mola/MPESA', 'CARTEIRA', 500.00, 2437.00, 'Movitel e Vodacom', 1, 1, '2026-01-15 15:41:55');

-- --------------------------------------------------------

--
-- Estrutura para tabela `logs`
--

CREATE TABLE `logs` (
  `id` int(11) NOT NULL,
  `usuario_id` int(11) DEFAULT NULL,
  `nome_usuario` varchar(255) NOT NULL,
  `operacao` varchar(50) NOT NULL COMMENT 'CRIAR, ATUALIZAR, DELETAR, VISUALIZAR',
  `descricao` text DEFAULT NULL COMMENT 'Detalhes da operação realizada',
  `tabela` varchar(50) DEFAULT NULL COMMENT 'Nome da tabela afetada',
  `registro_id` int(11) DEFAULT NULL COMMENT 'ID do registro afetado',
  `data_hora` datetime DEFAULT current_timestamp() COMMENT 'Data e hora da operação',
  `status_operacao` varchar(20) DEFAULT 'SUCESSO' COMMENT 'SUCESSO, FALHA, AVISO'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Despejando dados para a tabela `logs`
--

INSERT INTO `logs` (`id`, `usuario_id`, `nome_usuario`, `operacao`, `descricao`, `tabela`, `registro_id`, `data_hora`, `status_operacao`) VALUES
(1, 2, 'Julieta Sambo', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 2, '2026-01-22 17:33:41', 'FALHA'),
(2, 2, 'Julieta Sambo', 'LOGIN', 'Login bem-sucedido', 'usuarios', 2, '2026-01-22 17:33:46', 'SUCESSO'),
(3, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-22 17:34:10', 'SUCESSO'),
(4, 2, 'Julieta Sambo', 'LOGIN', 'Login bem-sucedido', 'usuarios', 2, '2026-01-25 12:18:43', 'SUCESSO'),
(5, 2, 'Julieta Sambo', 'LOGIN', 'Login bem-sucedido', 'usuarios', 2, '2026-01-25 12:19:01', 'SUCESSO'),
(6, 2, 'Julieta Sambo', 'LOGIN', 'Login bem-sucedido', 'usuarios', 2, '2026-01-25 13:09:49', 'SUCESSO'),
(7, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-25 13:56:35', 'SUCESSO'),
(8, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-26 10:10:31', 'SUCESSO'),
(9, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Encontro das Senhoras (RECEITA)', 'Categoria', 12, '2026-01-26 10:26:21', 'SUCESSO'),
(10, 1, 'Ussene Matato', 'ATUALIZAR', 'Categoria atualizada: Tafula das Senhoras', 'Categoria', 12, '2026-01-26 10:28:03', 'SUCESSO'),
(11, 1, 'Ussene Matato', 'ATUALIZAR', 'Categoria atualizada: Comunicação', 'Categoria', 2, '2026-01-26 10:31:28', 'SUCESSO'),
(12, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Água (DESPESA)', 'Categoria', 13, '2026-01-26 10:33:59', 'SUCESSO'),
(13, 1, 'Ussene Matato', 'ATUALIZAR', 'Categoria atualizada: Água FIPAG', 'Categoria', 13, '2026-01-26 10:34:25', 'SUCESSO'),
(14, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Energia (DESPESA)', 'Categoria', 14, '2026-01-26 10:35:21', 'SUCESSO'),
(15, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Limpeza e Higiene (DESPESA)', 'Categoria', 15, '2026-01-26 10:36:45', 'SUCESSO'),
(16, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Águas (DESPESA)', 'Categoria', 16, '2026-01-26 10:37:31', 'SUCESSO'),
(17, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Consumiveis (DESPESA)', 'Categoria', 17, '2026-01-26 10:38:33', 'SUCESSO'),
(18, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Administração (DESPESA)', 'Categoria', 18, '2026-01-26 10:40:04', 'SUCESSO'),
(19, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Departamento de Som (DESPESA)', 'Categoria', 19, '2026-01-26 10:41:20', 'SUCESSO'),
(20, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Departamento de Mídia e Comunicação (DESPESA)', 'Categoria', 20, '2026-01-26 10:42:28', 'SUCESSO'),
(21, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Aluguer de Materiais (DESPESA)', 'Categoria', 21, '2026-01-26 10:43:23', 'SUCESSO'),
(22, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Simbolos da Ceia (DESPESA)', 'Categoria', 22, '2026-01-26 10:45:26', 'SUCESSO'),
(23, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Manutenções do Templo (DESPESA)', 'Categoria', 23, '2026-01-26 10:47:52', 'SUCESSO'),
(24, 1, 'Ussene Matato', 'ATUALIZAR', 'Categoria atualizada: Assistencias', 'Categoria', 4, '2026-01-26 10:49:43', 'SUCESSO'),
(25, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Seminários (DESPESA)', 'Categoria', 24, '2026-01-26 10:57:41', 'SUCESSO'),
(26, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Visitações (DESPESA)', 'Categoria', 25, '2026-01-26 10:58:47', 'SUCESSO'),
(27, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Contribuições aos Orgãos Ministerias (DESPESA)', 'Categoria', 26, '2026-01-26 11:00:14', 'SUCESSO'),
(28, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Taxas e Impostos (DESPESA)', 'Categoria', 27, '2026-01-26 11:01:36', 'SUCESSO'),
(29, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: [REVERSÃO] Cadeado', 'Transacao', 9, '2026-01-26 11:02:09', 'SUCESSO'),
(30, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: Cadeado', 'Transacao', 8, '2026-01-26 11:02:16', 'SUCESSO'),
(31, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: Medicamentos', 'Transacao', 7, '2026-01-26 11:02:21', 'SUCESSO'),
(32, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: Colecta Semanal', 'Transacao', 6, '2026-01-26 11:02:27', 'SUCESSO'),
(33, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: Transporte', 'Transacao', 5, '2026-01-26 11:02:32', 'SUCESSO'),
(34, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: Pao para lanches', 'Transacao', 4, '2026-01-26 11:02:37', 'SUCESSO'),
(35, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: Entrega de documentos', 'Transacao', 3, '2026-01-26 11:02:43', 'SUCESSO'),
(36, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: Contribuicao', 'Transacao', 2, '2026-01-26 11:02:49', 'SUCESSO'),
(37, 1, 'Ussene Matato', 'DELETAR', 'Transação deletada: Dizimos dos dizimos', 'Transacao', 1, '2026-01-26 11:02:55', 'SUCESSO'),
(38, 1, 'Ussene Matato', 'ATUALIZAR', 'Conta atualizada: Conta Corrente', 'Conta', 1, '2026-01-26 11:04:21', 'SUCESSO'),
(39, 1, 'Ussene Matato', 'ATUALIZAR', 'Conta atualizada: E-Mola', 'Conta', 4, '2026-01-26 11:06:42', 'SUCESSO'),
(40, 1, 'Ussene Matato', 'ATUALIZAR', 'Conta atualizada: MPesa', 'Conta', 3, '2026-01-26 11:06:54', 'SUCESSO'),
(41, 1, 'Ussene Matato', 'ATUALIZAR', 'Conta atualizada: Fundo de Maneio', 'Conta', 2, '2026-01-26 11:07:40', 'SUCESSO'),
(42, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-26 11:11:27', 'SUCESSO'),
(43, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colecta do culto (RECEITA) - 1930', 'Transacao', 10, '2026-01-26 11:13:59', 'SUCESSO'),
(44, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos (RECEITA) - 34535', 'Transacao', 11, '2026-01-26 11:15:47', 'SUCESSO'),
(45, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Compra de Credelec (DESPESA) - 110', 'Transacao', 12, '2026-01-26 11:18:53', 'SUCESSO'),
(46, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Material de limpeza (DESPESA) - 770.00', 'Transacao', 13, '2026-01-26 11:19:51', 'SUCESSO'),
(47, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimo dos dizimos (DESPESA) - 4827', 'Transacao', 14, '2026-01-26 11:20:40', 'SUCESSO'),
(48, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Apoio a avo Lidia (DESPESA) - 1500', 'Transacao', 15, '2026-01-26 11:21:25', 'SUCESSO'),
(49, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Subsidio de transporte ao Pastor (DESPESA) - 5000', 'Transacao', 16, '2026-01-26 11:22:33', 'SUCESSO'),
(50, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Subsidiode transporte ao Presbitero (DESPESA) - 2000', 'Transacao', 17, '2026-01-26 11:23:10', 'SUCESSO'),
(51, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Creditos para comunicação (DESPESA) - 2000', 'Transacao', 18, '2026-01-26 11:24:03', 'SUCESSO'),
(52, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Cooperadores Administração (DESPESA) - 4500', 'Transacao', 19, '2026-01-26 11:24:48', 'SUCESSO'),
(53, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Apoio ao Pai Jose (DESPESA) - 500.00', 'Transacao', 20, '2026-01-26 11:25:49', 'SUCESSO'),
(54, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Acrescimo ao subsidio do Pastor (DESPESA) - 3000', 'Transacao', 21, '2026-01-26 11:26:54', 'SUCESSO'),
(55, 1, 'Ussene Matato', 'ATUALIZAR', 'Conta atualizada: Caixa', 'Conta', 2, '2026-01-26 11:27:40', 'SUCESSO'),
(56, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Fundo de maneio (DESPESA)', 'Categoria', 28, '2026-01-26 11:36:11', 'SUCESSO'),
(57, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Criacao do fundo de maneio para pequenas despesas (DESPESA) - 2000', 'Transacao', 22, '2026-01-26 11:37:00', 'SUCESSO'),
(58, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colecta (RECEITA) - 425', 'Transacao', 23, '2026-01-26 11:38:11', 'SUCESSO'),
(59, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Tafula geral (RECEITA) - 1805', 'Transacao', 24, '2026-01-26 11:39:09', 'SUCESSO'),
(60, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Caixa de agua (DESPESA) - 350', 'Transacao', 25, '2026-01-26 11:39:52', 'SUCESSO'),
(61, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuicao do valor da tafula para  alpendre em Montanhana (DESPESA) - 1805', 'Transacao', 26, '2026-01-26 11:40:43', 'SUCESSO'),
(62, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação atualizada: Contribuicao do valor da tafula para  alpendre em Montanhana', 'Transacao', 26, '2026-01-26 11:41:32', 'SUCESSO'),
(63, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Contribuicao do valor da tafula para  alpendre em Montanhana', 'Transacao', 26, '2026-01-26 11:41:32', 'SUCESSO'),
(64, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Caixa de agua', 'Transacao', 25, '2026-01-26 11:42:04', 'SUCESSO'),
(65, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colecta (RECEITA) - 1114', 'Transacao', 27, '2026-01-26 11:43:23', 'SUCESSO'),
(66, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos (RECEITA) - 600', 'Transacao', 28, '2026-01-26 11:43:57', 'SUCESSO'),
(67, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Transporte para velorio em Zimpeto de 3 Ancias (DESPESA) - 300', 'Transacao', 29, '2026-01-26 11:44:56', 'SUCESSO'),
(68, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Ajuda para cerimonia funebres em Zimpeto (DESPESA) - 1400', 'Transacao', 30, '2026-01-26 11:46:25', 'SUCESSO'),
(69, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colecta (RECEITA) - 852', 'Transacao', 31, '2026-01-26 11:47:36', 'SUCESSO'),
(70, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimo (RECEITA) - 250', 'Transacao', 32, '2026-01-26 11:48:08', 'SUCESSO'),
(71, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Caixa de agua e galao (DESPESA) - 560', 'Transacao', 33, '2026-01-26 11:49:01', 'SUCESSO'),
(72, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Caixa de agua e galao', 'Transacao', 33, '2026-01-26 11:54:00', 'SUCESSO'),
(73, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Ajuda para cerimonia funebres em Zimpeto', 'Transacao', 30, '2026-01-26 11:54:07', 'SUCESSO'),
(74, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Transporte para velorio em Zimpeto de 3 Ancias', 'Transacao', 29, '2026-01-26 11:54:14', 'SUCESSO'),
(75, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Criacao do fundo de maneio para pequenas despesas', 'Transacao', 22, '2026-01-26 11:54:22', 'SUCESSO'),
(76, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Acrescimo ao subsidio do Pastor', 'Transacao', 21, '2026-01-26 11:54:29', 'SUCESSO'),
(77, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Apoio ao Pai Jose', 'Transacao', 20, '2026-01-26 11:54:36', 'SUCESSO'),
(78, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Cooperadores Administração', 'Transacao', 19, '2026-01-26 11:54:42', 'SUCESSO'),
(79, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Creditos para comunicação', 'Transacao', 18, '2026-01-26 11:54:50', 'SUCESSO'),
(80, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Subsidiode transporte ao Presbitero', 'Transacao', 17, '2026-01-26 11:54:55', 'SUCESSO'),
(81, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Subsidio de transporte ao Pastor', 'Transacao', 16, '2026-01-26 11:55:01', 'SUCESSO'),
(82, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Apoio a avo Lidia', 'Transacao', 15, '2026-01-26 11:55:07', 'SUCESSO'),
(83, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Dizimo dos dizimos', 'Transacao', 14, '2026-01-26 11:55:12', 'SUCESSO'),
(84, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Material de limpeza', 'Transacao', 13, '2026-01-26 11:55:17', 'SUCESSO'),
(85, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Compra de Credelec', 'Transacao', 12, '2026-01-26 11:55:23', 'SUCESSO'),
(86, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Subsidio Pastor (DESPESA) - 4000', 'Transacao', 34, '2026-01-26 11:57:34', 'SUCESSO'),
(87, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Subsidio Pastor', 'Transacao', 34, '2026-01-26 11:57:34', 'SUCESSO'),
(88, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Taxa de Transferencia do Subsidio (DESPESA) - 130', 'Transacao', 35, '2026-01-26 11:58:38', 'SUCESSO'),
(89, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-27 13:50:00', 'SUCESSO'),
(90, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-27 13:58:43', 'SUCESSO'),
(91, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-27 14:25:43', 'SUCESSO'),
(92, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-29 21:16:53', 'SUCESSO'),
(93, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-29 21:28:56', 'SUCESSO'),
(94, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-29 21:48:05', 'SUCESSO'),
(95, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-29 21:58:04', 'SUCESSO'),
(96, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-29 22:09:08', 'SUCESSO'),
(97, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-29 22:16:19', 'SUCESSO'),
(98, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-29 22:46:59', 'SUCESSO'),
(99, 1, 'Ussene Matato', 'CRIAR', 'Transferência: Deposito na inicial - 29.00', 'Transacao', 36, '2026-01-29 22:47:32', 'SUCESSO'),
(100, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 11:37:27', 'SUCESSO'),
(101, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 11:43:03', 'SUCESSO'),
(102, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 11:49:48', 'SUCESSO'),
(103, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 11:54:09', 'SUCESSO'),
(104, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 11:57:02', 'SUCESSO'),
(105, 2, 'Julieta Sambo', 'LOGIN', 'Login bem-sucedido', 'usuarios', 2, '2026-01-30 12:09:40', 'SUCESSO'),
(106, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:25:01', 'SUCESSO'),
(107, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:35:50', 'SUCESSO'),
(108, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:38:50', 'SUCESSO'),
(109, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:41:38', 'SUCESSO'),
(110, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:45:38', 'SUCESSO'),
(111, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:45:42', 'SUCESSO'),
(112, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:45:44', 'SUCESSO'),
(113, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:46:11', 'SUCESSO'),
(114, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:46:13', 'SUCESSO'),
(115, 1, 'Ussene Matato', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 1, '2026-01-30 12:46:41', 'FALHA'),
(116, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:46:47', 'SUCESSO'),
(117, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:47:44', 'SUCESSO'),
(118, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 12:59:04', 'SUCESSO'),
(119, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 13:10:17', 'SUCESSO'),
(120, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 13:19:30', 'SUCESSO'),
(121, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 13:21:21', 'SUCESSO'),
(122, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 13:25:10', 'SUCESSO'),
(123, 2, 'Julieta Sambo', 'LOGIN', 'Login bem-sucedido', 'usuarios', 2, '2026-01-30 13:25:37', 'SUCESSO'),
(124, 2, 'Julieta Sambo', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 2, '2026-01-30 13:28:30', 'FALHA'),
(125, 2, 'Julieta Sambo', 'LOGIN', 'Login bem-sucedido', 'usuarios', 2, '2026-01-30 13:28:36', 'SUCESSO'),
(126, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 13:29:09', 'SUCESSO'),
(127, 3, 'Ussene Carlos', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 3, '2026-01-30 13:30:38', 'FALHA'),
(128, 3, 'Ussene Carlos', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 3, '2026-01-30 13:30:46', 'FALHA'),
(129, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 13:31:12', 'SUCESSO'),
(130, 3, 'Ussene Carlos', 'LOGIN', 'Login bem-sucedido', 'usuarios', 3, '2026-01-30 13:32:10', 'SUCESSO'),
(131, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 13:33:12', 'SUCESSO'),
(132, 4, 'Eugenio Cabiço', 'LOGIN', 'Login bem-sucedido', 'usuarios', 4, '2026-01-30 13:36:29', 'SUCESSO'),
(133, 3, 'Ussene Carlos', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 3, '2026-01-30 13:42:07', 'FALHA'),
(134, 2, 'Julieta Sambo', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 2, '2026-01-30 13:43:05', 'FALHA'),
(135, 2, 'Julieta Sambo', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 2, '2026-01-30 13:43:19', 'FALHA'),
(136, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-30 13:43:45', 'SUCESSO'),
(137, 2, 'Julieta Sambo', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 2, '2026-01-30 13:44:29', 'FALHA'),
(138, 4, 'Eugenio Cabiço', 'LOGIN', 'Login bem-sucedido', 'usuarios', 4, '2026-01-30 13:44:54', 'SUCESSO'),
(139, 3, 'Ussene Carlos', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 3, '2026-01-31 16:17:37', 'FALHA'),
(140, 3, 'Ussene Carlos', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 3, '2026-01-31 16:18:06', 'FALHA'),
(141, 3, 'Ussene Carlos', 'LOGIN', 'Login bem-sucedido', 'usuarios', 3, '2026-01-31 16:18:12', 'SUCESSO'),
(142, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-31 16:21:54', 'SUCESSO'),
(143, 1, 'Ussene Matato', 'ATUALIZAR', 'Conta atualizada: Conta Bancaria', 'Conta', 1, '2026-01-31 16:22:38', 'SUCESSO'),
(144, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Dizimos Regiões da Polana (RECEITA)', 'Categoria', 31, '2026-01-31 16:29:52', 'SUCESSO'),
(145, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos 14% da Congregação de Matlemele (RECEITA) - 3241', 'Transacao', 38, '2026-01-31 16:46:11', 'SUCESSO'),
(146, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos 14% da Congregação de Ngolhoza (RECEITA) - 763', 'Transacao', 39, '2026-01-31 16:47:39', 'SUCESSO'),
(147, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos 14% da Congregação de Pessene (RECEITA) - 540', 'Transacao', 40, '2026-01-31 16:48:14', 'SUCESSO'),
(148, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuição para cerimonias funenbres de Simbeze (RECEITA) - 1000', 'Transacao', 41, '2026-01-31 16:50:05', 'SUCESSO'),
(149, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuição para cerimonias funenbres (Matlemele) (RECEITA) - 2500', 'Transacao', 42, '2026-01-31 16:52:19', 'SUCESSO'),
(150, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Transporte para velorio em Zimpeto (DESPESA) - 510', 'Transacao', 43, '2026-01-31 16:54:07', 'SUCESSO'),
(151, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuição para ajuda no velorio em Zimpeto (DESPESA) - 1250', 'Transacao', 44, '2026-01-31 16:55:22', 'SUCESSO'),
(152, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Acrescimo para 14% a sede (DESPESA) - 1000', 'Transacao', 45, '2026-01-31 16:56:41', 'SUCESSO'),
(153, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuição para o espaço em Montanhana por parte de Matlemele (RECEITA) - 1110', 'Transacao', 46, '2026-01-31 16:58:10', 'SUCESSO'),
(154, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuição de pastores para vitimas da cheias (RECEITA) - 500', 'Transacao', 47, '2026-01-31 16:59:29', 'SUCESSO'),
(155, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Pagamento referente a troca da mesa de som por uma mas recente (DESPESA) - 4020', 'Transacao', 48, '2026-01-31 17:00:45', 'SUCESSO'),
(156, 1, 'Ussene Matato', 'CRIAR', 'Nova categoria criada: Contribuição Para templos Regiao Polana (DESPESA)', 'Categoria', 32, '2026-01-31 17:02:14', 'SUCESSO'),
(157, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuição dos templos para Mutanhane (DESPESA) - 600', 'Transacao', 49, '2026-01-31 17:02:28', 'SUCESSO'),
(158, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Transporte para velorio em Zimpeto', 'Transacao', 43, '2026-01-31 17:03:35', 'SUCESSO'),
(159, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Contribuição para ajuda no velorio em Zimpeto', 'Transacao', 44, '2026-01-31 17:03:41', 'SUCESSO'),
(160, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Acrescimo para 14% a sede', 'Transacao', 45, '2026-01-31 17:03:54', 'SUCESSO'),
(161, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Pagamento referente a troca da mesa de som por uma mas recente', 'Transacao', 48, '2026-01-31 17:03:59', 'SUCESSO'),
(162, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Contribuição dos templos para Mutanhane', 'Transacao', 49, '2026-01-31 17:04:04', 'SUCESSO'),
(163, 1, 'Ussene Matato', 'ATUALIZAR', 'Conta atualizada: E-Mola/MPESA', 'Conta', 4, '2026-01-31 17:05:02', 'SUCESSO'),
(164, 1, 'Ussene Matato', 'DELETAR', 'Conta desativada: MPesa', 'Conta', 3, '2026-01-31 17:05:16', 'SUCESSO'),
(165, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-02-01 07:47:08', 'SUCESSO'),
(166, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-31 07:49:11', 'SUCESSO'),
(167, 1, 'Ussene Matato', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 1, '2026-02-01 10:28:18', 'FALHA'),
(168, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-02-01 10:28:41', 'SUCESSO'),
(169, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-31 10:29:55', 'SUCESSO'),
(170, 1, 'Ussene Matato', 'CRIAR', 'Transferência: Depositos do valores em caixa (coletas e dizimos) - 10889', 'Transacao', 50, '2026-01-31 10:33:11', 'SUCESSO'),
(171, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos transferidos (RECEITA) - 8783.04', 'Transacao', 52, '2026-01-31 10:36:23', 'SUCESSO'),
(172, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Taxa para levantamento (DESPESA) - 20', 'Transacao', 53, '2026-01-31 10:59:39', 'SUCESSO'),
(173, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Taxa para levantamento', 'Transacao', 53, '2026-01-31 11:00:42', 'SUCESSO'),
(174, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-31 13:06:44', 'SUCESSO'),
(175, NULL, 'ussene.c.matato@gmailcom', 'LOGIN', 'Tentativa de login falhada: email não encontrado', 'usuarios', NULL, '2026-01-31 13:07:41', 'FALHA'),
(176, NULL, 'ussene.c.matato@gmailcom', 'LOGIN', 'Tentativa de login falhada: email não encontrado', 'usuarios', NULL, '2026-01-31 13:07:48', 'FALHA'),
(177, NULL, 'ussene.c.matato@gmailcom', 'LOGIN', 'Tentativa de login falhada: email não encontrado', 'usuarios', NULL, '2026-01-31 13:07:58', 'FALHA'),
(178, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-01-31 13:08:11', 'SUCESSO'),
(179, NULL, 'ussene.c.matato@g,mail.com', 'LOGIN', 'Tentativa de login falhada: email não encontrado', 'usuarios', NULL, '2026-02-28 22:22:39', 'FALHA'),
(180, 1, 'Ussene Matato', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 1, '2026-02-28 22:22:51', 'FALHA'),
(181, 1, 'Ussene Matato', 'LOGIN', 'Tentativa de login falhada: senha inválida', 'usuarios', 1, '2026-02-28 22:22:59', 'FALHA'),
(182, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-02-28 22:23:52', 'SUCESSO'),
(183, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos Fev (RECEITA) - 48840', 'Transacao', 54, '2026-02-28 22:29:58', 'SUCESSO'),
(184, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação atualizada: Dizimos Fev', 'Transacao', 54, '2026-02-28 22:30:30', 'SUCESSO'),
(185, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colecta (RECEITA) - 1316', 'Transacao', 55, '2026-02-28 22:31:20', 'SUCESSO'),
(186, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Energia (DESPESA) - 110', 'Transacao', 56, '2026-02-28 22:40:01', 'SUCESSO'),
(187, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuições dos dizimos dos dizimos (DESPESA) - 6837', 'Transacao', 57, '2026-02-28 22:41:11', 'SUCESSO'),
(188, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Apoio avó Lidia (DESPESA) - 1500', 'Transacao', 58, '2026-02-28 22:41:46', 'SUCESSO'),
(189, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação atualizada: Apoio avó Lidia', 'Transacao', 58, '2026-02-28 22:42:11', 'SUCESSO'),
(190, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Apoio avó Lidia', 'Transacao', 58, '2026-02-28 22:42:11', 'SUCESSO'),
(191, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Subsidio de transporte a cooperadores (DESPESA) - 11000', 'Transacao', 59, '2026-02-28 22:43:20', 'SUCESSO'),
(192, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Subsidio de obreiros e cooperadores (DESPESA) - 4500', 'Transacao', 60, '2026-02-28 22:44:32', 'SUCESSO'),
(193, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Credito Mae Ana (DESPESA) - 500', 'Transacao', 61, '2026-02-28 22:46:14', 'SUCESSO'),
(194, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Credito Irma Catarina (DESPESA) - 500', 'Transacao', 62, '2026-02-28 22:46:57', 'SUCESSO'),
(195, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Apoio Pai Jóse (DESPESA) - 500', 'Transacao', 63, '2026-02-28 22:47:47', 'SUCESSO'),
(196, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Transporte Para Ev. a Sidwava (DESPESA) - 1000', 'Transacao', 64, '2026-02-28 22:49:51', 'SUCESSO'),
(197, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuição Para Seminario DML (DESPESA) - 1800', 'Transacao', 65, '2026-02-28 22:50:44', 'SUCESSO'),
(198, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Reembolso do valor da tafula usado p/despesas (DESPESA) - 3315', 'Transacao', 66, '2026-02-28 23:01:25', 'SUCESSO'),
(199, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Compra de telemóvel p/mãe Ana (DESPESA) - 5000', 'Transacao', 67, '2026-02-28 23:02:42', 'SUCESSO'),
(200, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Chá p/crianças (DESPESA) - 200', 'Transacao', 68, '2026-02-28 23:03:43', 'SUCESSO'),
(201, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Energia', 'Transacao', 56, '2026-02-28 23:04:06', 'SUCESSO'),
(202, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Contribuições dos dizimos dos dizimos', 'Transacao', 57, '2026-02-28 23:04:14', 'SUCESSO'),
(203, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Subsidio de transporte a cooperadores', 'Transacao', 59, '2026-02-28 23:04:20', 'SUCESSO'),
(204, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Subsidio de obreiros e cooperadores', 'Transacao', 60, '2026-02-28 23:04:25', 'SUCESSO'),
(205, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Credito Mae Ana', 'Transacao', 61, '2026-02-28 23:04:31', 'SUCESSO'),
(206, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Credito Irma Catarina', 'Transacao', 62, '2026-02-28 23:04:36', 'SUCESSO'),
(207, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Apoio Pai Jóse', 'Transacao', 63, '2026-02-28 23:04:40', 'SUCESSO'),
(208, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Transporte Para Ev. a Sidwava', 'Transacao', 64, '2026-02-28 23:04:47', 'SUCESSO'),
(209, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Contribuição Para Seminario DML', 'Transacao', 65, '2026-02-28 23:04:51', 'SUCESSO'),
(210, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Reembolso do valor da tafula usado p/despesas', 'Transacao', 66, '2026-02-28 23:04:57', 'SUCESSO'),
(211, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Compra de telemóvel p/mãe Ana', 'Transacao', 67, '2026-02-28 23:05:01', 'SUCESSO'),
(212, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Chá p/crianças', 'Transacao', 68, '2026-02-28 23:05:06', 'SUCESSO'),
(213, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos (RECEITA) - 650', 'Transacao', 69, '2026-02-28 23:06:34', 'SUCESSO'),
(214, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colectas (RECEITA) - 600', 'Transacao', 70, '2026-02-28 23:07:12', 'SUCESSO'),
(215, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Tafula Geral (RECEITA) - 904', 'Transacao', 71, '2026-02-28 23:07:44', 'SUCESSO'),
(216, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Medicamentos p/Martinha (DESPESA) - 600', 'Transacao', 72, '2026-02-28 23:09:29', 'SUCESSO'),
(217, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Chapa p/escrita do nome da Igreja (DESPESA) - 650', 'Transacao', 73, '2026-02-28 23:12:36', 'SUCESSO'),
(218, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Agua para consumo (DESPESA) - 650', 'Transacao', 74, '2026-02-28 23:13:10', 'SUCESSO'),
(219, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colectas (RECEITA) - 1441', 'Transacao', 75, '2026-02-28 23:14:41', 'SUCESSO'),
(220, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimos (RECEITA) - 4800', 'Transacao', 76, '2026-02-28 23:15:10', 'SUCESSO'),
(221, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Água (DESPESA) - 370', 'Transacao', 77, '2026-02-28 23:15:45', 'SUCESSO'),
(222, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Cha p/crianças (DESPESA) - 250', 'Transacao', 78, '2026-02-28 23:16:20', 'SUCESSO'),
(223, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Transporte (DESPESA) - 1000', 'Transacao', 79, '2026-02-28 23:16:55', 'SUCESSO'),
(224, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Envelopes (DESPESA) - 210', 'Transacao', 80, '2026-02-28 23:17:23', 'SUCESSO'),
(225, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Protector e capa p/telefone Mãe Ana (DESPESA) - 510', 'Transacao', 81, '2026-02-28 23:18:17', 'SUCESSO'),
(226, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colecta (RECEITA) - 956.50', 'Transacao', 82, '2026-02-28 23:19:08', 'SUCESSO'),
(227, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colecta Escola Dominical (RECEITA) - 88', 'Transacao', 83, '2026-02-28 23:19:45', 'SUCESSO'),
(228, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Colecta do Encontro das senhoras (RECEITA) - 45', 'Transacao', 84, '2026-02-28 23:20:33', 'SUCESSO'),
(229, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Água (DESPESA) - 400', 'Transacao', 85, '2026-02-28 23:21:10', 'SUCESSO'),
(230, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Pilhas (DESPESA) - 510', 'Transacao', 86, '2026-02-28 23:21:45', 'SUCESSO'),
(231, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Água', 'Transacao', 77, '2026-02-28 23:24:34', 'SUCESSO'),
(232, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Cha p/crianças', 'Transacao', 78, '2026-02-28 23:24:39', 'SUCESSO'),
(233, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Transporte', 'Transacao', 79, '2026-02-28 23:24:43', 'SUCESSO'),
(234, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Envelopes', 'Transacao', 80, '2026-02-28 23:24:47', 'SUCESSO'),
(235, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Protector e capa p/telefone Mãe Ana', 'Transacao', 81, '2026-02-28 23:24:52', 'SUCESSO'),
(236, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Água', 'Transacao', 85, '2026-02-28 23:24:57', 'SUCESSO'),
(237, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Pilhas', 'Transacao', 86, '2026-02-28 23:25:03', 'SUCESSO'),
(238, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Medicamentos p/Martinha', 'Transacao', 72, '2026-02-28 23:25:09', 'SUCESSO'),
(239, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Chapa p/escrita do nome da Igreja', 'Transacao', 73, '2026-02-28 23:25:14', 'SUCESSO'),
(240, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Agua para consumo', 'Transacao', 74, '2026-02-28 23:25:18', 'SUCESSO'),
(241, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Reparação do teclado (DESPESA) - 760', 'Transacao', 87, '2026-02-28 23:37:33', 'SUCESSO'),
(242, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Contribuição de apoio das cheias para pastores Catela e Bila (DESPESA) - 1000', 'Transacao', 88, '2026-02-28 23:41:21', 'SUCESSO'),
(243, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimo transferido (RECEITA) - 1850', 'Transacao', 89, '2026-02-28 23:44:42', 'SUCESSO'),
(244, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: 14% dizimo de Matlemele (RECEITA) - 1794', 'Transacao', 90, '2026-02-28 23:47:00', 'SUCESSO'),
(245, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Emprestimo p/pastor Catela (DESPESA) - 2020', 'Transacao', 91, '2026-02-28 23:48:51', 'SUCESSO'),
(246, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Emprestimo p/pastor Catela', 'Transacao', 91, '2026-02-28 23:48:51', 'SUCESSO'),
(247, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Dizimo 14% Ngolhoza (RECEITA) - 742', 'Transacao', 92, '2026-02-28 23:53:40', 'SUCESSO'),
(248, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-03-01 00:01:30', 'SUCESSO'),
(249, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-02-28 00:03:04', 'SUCESSO'),
(250, 1, 'Ussene Matato', 'CRIAR', 'Transferência: Remanescente dos 14% - 1825', 'Transacao', 93, '2026-02-28 00:04:17', 'SUCESSO'),
(251, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Envelope para o pregador (DESPESA) - 1000', 'Transacao', 95, '2026-02-28 00:06:01', 'SUCESSO'),
(252, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Envelope para o pregador (João Paulo) (DESPESA) - 1000', 'Transacao', 96, '2026-02-28 00:07:52', 'SUCESSO'),
(253, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Subsidio para Pastor Catela (DESPESA) - 5000', 'Transacao', 97, '2026-02-28 00:11:31', 'SUCESSO'),
(254, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Subsidio para Pastor Catela', 'Transacao', 97, '2026-02-28 00:11:31', 'SUCESSO'),
(255, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Soupa p/consagração (DESPESA) - 1520', 'Transacao', 98, '2026-02-28 00:19:36', 'SUCESSO'),
(256, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Soupa p/consagração', 'Transacao', 98, '2026-02-28 00:19:36', 'SUCESSO'),
(257, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Impressões (DESPESA) - 85', 'Transacao', 99, '2026-02-28 00:21:27', 'SUCESSO'),
(258, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Galão de água (DESPESA) - 230', 'Transacao', 100, '2026-02-28 00:21:57', 'SUCESSO'),
(259, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Transferencia (RECEITA) - 170', 'Transacao', 101, '2026-02-28 00:23:18', 'SUCESSO'),
(260, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Caderno (DESPESA) - 75', 'Transacao', 102, '2026-02-28 00:23:53', 'SUCESSO'),
(261, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Cópias (DESPESA) - 32', 'Transacao', 103, '2026-02-28 00:24:41', 'SUCESSO'),
(262, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Cópias', 'Transacao', 103, '2026-02-28 00:24:41', 'SUCESSO'),
(263, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Envelope para o pregador', 'Transacao', 95, '2026-02-28 00:24:55', 'SUCESSO'),
(264, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Reparação do teclado', 'Transacao', 87, '2026-02-28 00:25:02', 'SUCESSO'),
(265, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Contribuição de apoio das cheias para pastores Catela e Bila', 'Transacao', 88, '2026-02-28 00:25:07', 'SUCESSO'),
(266, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Envelope para o pregador (João Paulo)', 'Transacao', 96, '2026-02-28 00:25:15', 'SUCESSO'),
(267, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Impressões', 'Transacao', 99, '2026-02-28 00:25:21', 'SUCESSO'),
(268, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Galão de água', 'Transacao', 100, '2026-02-28 00:25:25', 'SUCESSO'),
(269, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Caderno', 'Transacao', 102, '2026-02-28 00:25:31', 'SUCESSO'),
(270, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Simbolos da ceia (DESPESA) - 245', 'Transacao', 104, '2026-02-28 00:26:59', 'SUCESSO'),
(271, 1, 'Ussene Matato', 'CRIAR', 'Transação registrada: Plastica (DESPESA) - 20', 'Transacao', 105, '2026-02-28 00:28:15', 'SUCESSO'),
(272, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Simbolos da ceia', 'Transacao', 104, '2026-02-28 00:28:27', 'SUCESSO'),
(273, 1, 'Ussene Matato', 'ATUALIZAR', 'Transação marcada como paga: Plastica', 'Transacao', 105, '2026-02-28 00:28:33', 'SUCESSO'),
(274, 1, 'Ussene Matato', 'CRIAR', 'Transferência: Depositos de colectas e dizimos - 14133.50', 'Transacao', 106, '2026-02-28 00:47:39', 'SUCESSO'),
(275, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-03-01 08:25:40', 'SUCESSO'),
(276, 1, 'Ussene Matato', 'LOGIN', 'Login bem-sucedido', 'usuarios', 1, '2026-02-28 08:27:00', 'SUCESSO');

-- --------------------------------------------------------

--
-- Estrutura para tabela `transacoes`
--

CREATE TABLE `transacoes` (
  `id` int(11) NOT NULL,
  `descricao` varchar(200) NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `data_transacao` date NOT NULL,
  `data_vencimento` date DEFAULT NULL,
  `pago` tinyint(1) DEFAULT 0,
  `recorrente` tinyint(1) DEFAULT 0,
  `frequencia` varchar(20) DEFAULT NULL,
  `conta_id` int(11) NOT NULL,
  `categoria_id` int(11) DEFAULT NULL,
  `usuario_id` int(11) NOT NULL,
  `observacoes` text DEFAULT NULL,
  `data_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  `transferencia_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `transacoes`
--

INSERT INTO `transacoes` (`id`, `descricao`, `valor`, `tipo`, `data_transacao`, `data_vencimento`, `pago`, `recorrente`, `frequencia`, `conta_id`, `categoria_id`, `usuario_id`, `observacoes`, `data_registro`, `transferencia_id`) VALUES
(10, 'Colecta do culto', 1930.00, 'RECEITA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 9, 1, '', '2026-01-26 09:13:59', NULL),
(11, 'Dizimos', 34535.00, 'RECEITA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 8, 1, '', '2026-01-26 09:15:47', NULL),
(12, 'Compra de Credelec', 110.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 14, 1, '', '2026-01-26 09:18:53', NULL),
(13, 'Material de limpeza', 770.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 15, 1, '', '2026-01-26 09:19:51', NULL),
(14, 'Dizimo dos dizimos', 4827.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 1, 1, '', '2026-01-26 09:20:40', NULL),
(15, 'Apoio a avo Lidia', 1500.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 4, 1, '', '2026-01-26 09:21:25', NULL),
(16, 'Subsidio de transporte ao Pastor', 5000.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 3, 1, '', '2026-01-26 09:22:33', NULL),
(17, 'Subsidiode transporte ao Presbitero', 2000.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 3, 1, '', '2026-01-26 09:23:10', NULL),
(18, 'Creditos para comunicação', 2000.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 2, 1, '', '2026-01-26 09:24:03', NULL),
(19, 'Cooperadores Administração', 4500.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 18, 1, '', '2026-01-26 09:24:48', NULL),
(20, 'Apoio ao Pai Jose', 500.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 15, 1, '', '2026-01-26 09:25:49', NULL),
(21, 'Acrescimo ao subsidio do Pastor', 3000.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 4, 1, 'Diferença do subsidio que devia ser descontado na conta', '2026-01-26 09:26:54', NULL),
(22, 'Criacao do fundo de maneio para pequenas despesas', 2000.00, 'DESPESA', '2026-01-04', '2026-01-04', 1, 0, NULL, 2, 28, 1, '', '2026-01-26 09:37:00', NULL),
(23, 'Colecta', 425.00, 'RECEITA', '2026-01-11', '2026-01-11', 1, 0, NULL, 2, 9, 1, '', '2026-01-26 09:38:11', NULL),
(24, 'Tafula geral', 1805.00, 'RECEITA', '2026-01-11', '2026-01-11', 1, 0, NULL, 2, 11, 1, '', '2026-01-26 09:39:09', NULL),
(25, 'Caixa de agua', 350.00, 'DESPESA', '2026-01-11', '2026-01-11', 1, 0, NULL, 2, 16, 1, '', '2026-01-26 09:39:52', NULL),
(26, 'Contribuicao do valor da tafula para  alpendre em Montanhana', 1805.00, 'DESPESA', '2026-01-11', '2026-01-11', 1, 0, NULL, 2, 26, 1, '', '2026-01-26 09:40:43', NULL),
(27, 'Colecta', 1114.00, 'RECEITA', '2026-01-18', '2026-01-18', 1, 0, NULL, 2, 9, 1, '', '2026-01-26 09:43:23', NULL),
(28, 'Dizimos', 600.00, 'RECEITA', '2026-01-18', '2026-01-18', 1, 0, NULL, 2, 8, 1, '', '2026-01-26 09:43:57', NULL),
(29, 'Transporte para velorio em Zimpeto de 3 Ancias', 300.00, 'DESPESA', '2026-01-18', '2026-01-18', 1, 0, NULL, 2, 3, 1, '', '2026-01-26 09:44:56', NULL),
(30, 'Ajuda para cerimonia funebres em Zimpeto', 1400.00, 'DESPESA', '2026-01-18', '2026-01-18', 1, 0, NULL, 2, 4, 1, '', '2026-01-26 09:46:25', NULL),
(31, 'Colecta', 852.00, 'RECEITA', '2026-01-25', '2026-01-25', 1, 0, NULL, 2, 9, 1, '', '2026-01-26 09:47:36', NULL),
(32, 'Dizimo', 250.00, 'RECEITA', '2026-01-25', '2026-01-25', 1, 0, NULL, 2, 8, 1, '', '2026-01-26 09:48:08', NULL),
(33, 'Caixa de agua e galao', 560.00, 'DESPESA', '2026-01-25', '2026-01-25', 1, 0, NULL, 2, 16, 1, '', '2026-01-26 09:49:01', NULL),
(34, 'Subsidio Pastor', 4000.00, 'DESPESA', '2026-01-15', '2026-01-15', 1, 0, NULL, 1, 4, 1, '', '2026-01-26 09:57:34', NULL),
(35, 'Taxa de Transferencia do Subsidio', 130.00, 'DESPESA', '2026-01-15', '2026-01-15', 0, 0, NULL, 1, 27, 1, '', '2026-01-26 09:58:38', NULL),
(36, 'Deposito na inicial', 29.00, 'TRANSFERENCIA', '2026-01-29', '2026-01-29', 1, 0, NULL, 3, NULL, 1, 'Transferência para 4. ', '2026-01-29 20:47:32', 36),
(37, 'Deposito na inicial', 29.00, 'TRANSFERENCIA', '2026-01-29', '2026-01-29', 1, 0, NULL, 4, NULL, 1, 'Transferência recebida de 3. ', '2026-01-29 20:47:32', 36),
(38, 'Dizimos 14% da Congregação de Matlemele', 3241.00, 'RECEITA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 31, 1, '', '2026-01-31 14:46:11', NULL),
(39, 'Dizimos 14% da Congregação de Ngolhoza', 763.00, 'RECEITA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 31, 1, '', '2026-01-31 14:47:39', NULL),
(40, 'Dizimos 14% da Congregação de Pessene', 540.00, 'RECEITA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 31, 1, '', '2026-01-31 14:48:14', NULL),
(41, 'Contribuição para cerimonias funenbres de Simbeze', 1000.00, 'RECEITA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 30, 1, '', '2026-01-31 14:50:05', NULL),
(42, 'Contribuição para cerimonias funenbres (Matlemele)', 2500.00, 'RECEITA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 30, 1, '', '2026-01-31 14:52:19', NULL),
(43, 'Transporte para velorio em Zimpeto', 510.00, 'DESPESA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 29, 1, '', '2026-01-31 14:54:07', NULL),
(44, 'Contribuição para ajuda no velorio em Zimpeto', 1250.00, 'DESPESA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 4, 1, '', '2026-01-31 14:55:22', NULL),
(45, 'Acrescimo para 14% a sede', 1000.00, 'DESPESA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 1, 1, '', '2026-01-31 14:56:41', NULL),
(46, 'Contribuição para o espaço em Montanhana por parte de Matlemele', 1110.00, 'RECEITA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 30, 1, '', '2026-01-31 14:58:10', NULL),
(47, 'Contribuição de pastores para vitimas da cheias', 500.00, 'RECEITA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 30, 1, '', '2026-01-31 14:59:29', NULL),
(48, 'Pagamento referente a troca da mesa de som por uma mas recente', 4020.00, 'DESPESA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 19, 1, '', '2026-01-31 15:00:45', NULL),
(49, 'Contribuição dos templos para Mutanhane', 600.00, 'DESPESA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 32, 1, '', '2026-01-31 15:02:28', NULL),
(50, 'Depositos do valores em caixa (coletas e dizimos)', 10889.00, 'TRANSFERENCIA', '2026-01-31', '2026-01-31', 1, 0, NULL, 2, NULL, 1, 'Transferência para 1. ', '2026-01-31 08:33:11', 50),
(51, 'Depositos do valores em caixa (coletas e dizimos)', 10889.00, 'TRANSFERENCIA', '2026-01-31', '2026-01-31', 1, 0, NULL, 1, NULL, 1, 'Transferência recebida de 2. ', '2026-01-31 08:33:11', 50),
(52, 'Dizimos transferidos', 8783.04, 'RECEITA', '2026-01-31', '2026-01-31', 1, 0, NULL, 1, 8, 1, '', '2026-01-31 08:36:23', NULL),
(53, 'Taxa para levantamento', 20.00, 'DESPESA', '2026-01-31', '2026-01-31', 1, 0, NULL, 4, 27, 1, '', '2026-01-31 08:59:39', NULL),
(54, 'Dizimos Fev', 48840.00, 'RECEITA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 8, 1, '', '2026-02-28 20:29:58', NULL),
(55, 'Colecta', 1316.00, 'RECEITA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 9, 1, '', '2026-02-28 20:31:20', NULL),
(56, 'Energia', 110.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 14, 1, '', '2026-02-28 20:40:01', NULL),
(57, 'Contribuições dos dizimos dos dizimos', 6837.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 1, 1, '', '2026-02-28 20:41:11', NULL),
(58, 'Apoio avó Lidia', 1510.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 4, 1, '', '2026-02-28 20:41:46', NULL),
(59, 'Subsidio de transporte a cooperadores', 11000.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 3, 1, '', '2026-02-28 20:43:20', NULL),
(60, 'Subsidio de obreiros e cooperadores', 4500.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 18, 1, '', '2026-02-28 20:44:32', NULL),
(61, 'Credito Mae Ana', 500.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 2, 1, '', '2026-02-28 20:46:14', NULL),
(62, 'Credito Irma Catarina', 500.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 2, 1, '', '2026-02-28 20:46:57', NULL),
(63, 'Apoio Pai Jóse', 500.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 23, 1, '', '2026-02-28 20:47:47', NULL),
(64, 'Transporte Para Ev. a Sidwava', 1000.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 25, 1, '', '2026-02-28 20:49:51', NULL),
(65, 'Contribuição Para Seminario DML', 1800.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 24, 1, '', '2026-02-28 20:50:44', NULL),
(66, 'Reembolso do valor da tafula usado p/despesas', 3315.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 29, 1, '', '2026-02-28 21:01:25', NULL),
(67, 'Compra de telemóvel p/mãe Ana', 5000.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 4, 1, '', '2026-02-28 21:02:42', NULL),
(68, 'Chá p/crianças', 200.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 17, 1, '', '2026-02-28 21:03:43', NULL),
(69, 'Dizimos', 650.00, 'RECEITA', '2026-02-08', '2026-02-08', 1, 0, NULL, 2, 8, 1, '', '2026-02-28 21:06:34', NULL),
(70, 'Colectas', 600.00, 'RECEITA', '2026-02-08', '2026-02-08', 1, 0, NULL, 2, 9, 1, '', '2026-02-28 21:07:12', NULL),
(71, 'Tafula Geral', 904.00, 'RECEITA', '2026-02-08', '2026-02-08', 1, 0, NULL, 2, 11, 1, '', '2026-02-28 21:07:44', NULL),
(72, 'Medicamentos p/Martinha', 600.00, 'DESPESA', '2026-02-08', '2026-02-08', 1, 0, NULL, 2, 4, 1, '', '2026-02-28 21:09:29', NULL),
(73, 'Chapa p/escrita do nome da Igreja', 650.00, 'DESPESA', '2026-02-08', '2026-02-08', 1, 0, NULL, 2, 32, 1, '', '2026-02-28 21:12:36', NULL),
(74, 'Agua para consumo', 650.00, 'DESPESA', '2026-02-08', '2026-02-08', 1, 0, NULL, 2, 18, 1, '', '2026-02-28 21:13:10', NULL),
(75, 'Colectas', 1441.00, 'RECEITA', '2026-02-15', '2026-02-15', 1, 0, NULL, 2, 9, 1, '', '2026-02-28 21:14:41', NULL),
(76, 'Dizimos', 4800.00, 'RECEITA', '2026-02-15', '2026-02-15', 1, 0, NULL, 2, 8, 1, '', '2026-02-28 21:15:10', NULL),
(77, 'Água', 370.00, 'DESPESA', '2026-02-15', '2026-02-15', 1, 0, NULL, 2, 16, 1, '', '2026-02-28 21:15:44', NULL),
(78, 'Cha p/crianças', 250.00, 'DESPESA', '2026-02-15', '2026-02-15', 1, 0, NULL, 2, 17, 1, '', '2026-02-28 21:16:20', NULL),
(79, 'Transporte', 1000.00, 'DESPESA', '2026-02-15', '2026-02-15', 1, 0, NULL, 2, 3, 1, '', '2026-02-28 21:16:55', NULL),
(80, 'Envelopes', 210.00, 'DESPESA', '2026-02-15', '2026-02-15', 1, 0, NULL, 2, 17, 1, '', '2026-02-28 21:17:23', NULL),
(81, 'Protector e capa p/telefone Mãe Ana', 510.00, 'DESPESA', '2026-02-15', '2026-02-15', 1, 0, NULL, 2, 4, 1, '', '2026-02-28 21:18:17', NULL),
(82, 'Colecta', 956.50, 'RECEITA', '2026-02-22', '2026-02-22', 1, 0, NULL, 2, 9, 1, '', '2026-02-28 21:19:08', NULL),
(83, 'Colecta Escola Dominical', 88.00, 'RECEITA', '2026-02-22', '2026-02-22', 1, 0, NULL, 2, 7, 1, '', '2026-02-28 21:19:45', NULL),
(84, 'Colecta do Encontro das senhoras', 45.00, 'RECEITA', '2026-02-22', '2026-02-22', 1, 0, NULL, 2, 10, 1, '', '2026-02-28 21:20:33', NULL),
(85, 'Água', 400.00, 'DESPESA', '2026-02-22', '2026-02-22', 1, 0, NULL, 2, 16, 1, '', '2026-02-28 21:21:10', NULL),
(86, 'Pilhas', 510.00, 'DESPESA', '2026-02-22', '2026-02-22', 1, 0, NULL, 2, 19, 1, '', '2026-02-28 21:21:45', NULL),
(87, 'Reparação do teclado', 760.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 19, 1, '', '2026-02-28 21:37:33', NULL),
(88, 'Contribuição de apoio das cheias para pastores Catela e Bila', 1000.00, 'DESPESA', '2026-02-01', '2026-02-01', 1, 0, NULL, 2, 26, 1, '', '2026-02-28 21:41:21', NULL),
(89, 'Dizimo transferido', 1850.00, 'RECEITA', '2026-02-01', '2026-02-01', 1, 0, NULL, 4, 8, 1, '', '2026-02-28 21:44:42', NULL),
(90, '14% dizimo de Matlemele', 1794.00, 'RECEITA', '2026-02-01', '2026-02-01', 1, 0, NULL, 4, 31, 1, '', '2026-02-28 21:47:00', NULL),
(91, 'Emprestimo p/pastor Catela', 2020.00, 'DESPESA', '2026-02-04', '2026-02-04', 1, 0, NULL, 4, 29, 1, '', '2026-02-28 21:48:51', NULL),
(92, 'Dizimo 14% Ngolhoza', 742.00, 'RECEITA', '2026-02-15', '2026-02-15', 1, 0, NULL, 4, 31, 1, '', '2026-02-28 21:53:40', NULL),
(93, 'Remanescente dos 14%', 1825.00, 'TRANSFERENCIA', '2026-02-15', '2026-02-15', 1, 0, NULL, 2, NULL, 1, 'Transferência para 4. ', '2026-02-27 22:04:17', 93),
(94, 'Remanescente dos 14%', 1825.00, 'TRANSFERENCIA', '2026-02-15', '2026-02-15', 1, 0, NULL, 4, NULL, 1, 'Transferência recebida de 2. ', '2026-02-27 22:04:17', 93),
(95, 'Envelope para o pregador', 1000.00, 'DESPESA', '2026-02-15', '2026-02-15', 1, 0, NULL, 4, 25, 1, '', '2026-02-27 22:06:01', NULL),
(96, 'Envelope para o pregador (João Paulo)', 1000.00, 'DESPESA', '2026-02-22', '2026-02-22', 1, 0, NULL, 4, 25, 1, '', '2026-02-27 22:07:52', NULL),
(97, 'Subsidio para Pastor Catela', 5000.00, 'DESPESA', '2026-02-16', '2026-02-16', 1, 0, NULL, 1, 4, 1, '', '2026-02-27 22:11:31', NULL),
(98, 'Soupa p/consagração', 1520.00, 'DESPESA', '2026-02-28', '2026-02-28', 1, 0, NULL, 4, 17, 1, '', '2026-02-27 22:19:36', NULL),
(99, 'Impressões', 85.00, 'DESPESA', '2026-02-28', '2026-02-28', 1, 0, NULL, 4, 2, 1, '', '2026-02-27 22:21:27', NULL),
(100, 'Galão de água', 230.00, 'DESPESA', '2026-02-28', '2026-02-28', 1, 0, NULL, 4, 16, 1, '', '2026-02-27 22:21:57', NULL),
(101, 'Transferencia', 170.00, 'RECEITA', '2026-02-28', '2026-02-28', 1, 0, NULL, 4, 30, 1, '', '2026-02-27 22:23:18', NULL),
(102, 'Caderno', 75.00, 'DESPESA', '2026-02-28', '2026-02-28', 1, 0, NULL, 4, 17, 1, '', '2026-02-27 22:23:53', NULL),
(103, 'Cópias', 32.00, 'DESPESA', '2026-02-17', '2026-02-17', 1, 0, NULL, 4, 17, 1, '', '2026-02-27 22:24:41', NULL),
(104, 'Simbolos da ceia', 245.00, 'DESPESA', '2026-02-28', '2026-02-28', 1, 0, NULL, 4, 22, 1, '', '2026-02-27 22:26:59', NULL),
(105, 'Plastica', 20.00, 'DESPESA', '2026-02-28', '2026-02-28', 1, 0, NULL, 4, 15, 1, '', '2026-02-27 22:28:15', NULL),
(106, 'Depositos de colectas e dizimos', 14133.50, 'TRANSFERENCIA', '2026-02-28', '2026-02-28', 1, 0, NULL, 2, NULL, 1, 'Transferência para 1. ', '2026-02-27 22:47:39', 106),
(107, 'Depositos de colectas e dizimos', 14133.50, 'TRANSFERENCIA', '2026-02-28', '2026-02-28', 1, 0, NULL, 1, NULL, 1, 'Transferência recebida de 2. ', '2026-02-27 22:47:39', 106);

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `senha_hash` varchar(255) NOT NULL,
  `perfil` varchar(50) NOT NULL DEFAULT 'TESOUREIRO',
  `ativo` tinyint(1) DEFAULT 1,
  `data_cadastro` timestamp NOT NULL DEFAULT current_timestamp()
) ;

--
-- Despejando dados para a tabela `usuarios`
--

INSERT INTO `usuarios` (`id`, `nome`, `email`, `telefone`, `senha_hash`, `perfil`, `ativo`, `data_cadastro`) VALUES
(1, 'Ussene Matato', 'ussene.c.matato@gmail.com', '+258 82 123 4567', '0192023a7bbd73250516f069df18b500', 'SUPERADMIN', 1, '2026-01-16 15:22:28'),
(2, 'Julieta Sambo', 'julietasambo@gmail.com', '+2588787878', '0192023a7bbd73250516f069df18b500', 'ADMINISTRADOR', 1, '2026-01-16 15:39:43'),
(3, 'Ussene Carlos', 'ussene.matato@uem.ac.mz', '848954706', '3ec87da2f0285fce03f2515e7fe37253', 'CONTABILISTA', 1, '2026-01-19 13:47:21'),
(4, 'Eugenio Cabiço', 'eugeniocabico@gmail.com', '848460249', '9db78db7578ea5d0acc83bcbc88a327f', 'TESOUREIRO', 1, '2026-01-30 11:34:36');

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_usuario_tipo` (`usuario_id`,`tipo`);

--
-- Índices de tabela `contas`
--
ALTER TABLE `contas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_usuario` (`usuario_id`);

--
-- Índices de tabela `logs`
--
ALTER TABLE `logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_usuario_id` (`usuario_id`),
  ADD KEY `idx_operacao` (`operacao`),
  ADD KEY `idx_data_hora` (`data_hora`),
  ADD KEY `idx_tabela` (`tabela`),
  ADD KEY `idx_usuario_data` (`usuario_id`,`data_hora`),
  ADD KEY `idx_operacao_data` (`operacao`,`data_hora`);

--
-- Índices de tabela `transacoes`
--
ALTER TABLE `transacoes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `conta_id` (`conta_id`),
  ADD KEY `categoria_id` (`categoria_id`),
  ADD KEY `idx_usuario_data` (`usuario_id`,`data_transacao`),
  ADD KEY `idx_tipo_pago` (`tipo`,`pago`),
  ADD KEY `idx_transferencia_id` (`transferencia_id`),
  ADD KEY `idx_tipo_data` (`tipo`,`data_transacao`),
  ADD KEY `idx_usuario_tipo` (`usuario_id`,`tipo`);

--
-- Índices de tabela `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_email` (`email`),
  ADD KEY `idx_ativo` (`ativo`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT de tabela `contas`
--
ALTER TABLE `contas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de tabela `logs`
--
ALTER TABLE `logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=277;

--
-- AUTO_INCREMENT de tabela `transacoes`
--
ALTER TABLE `transacoes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=108;

--
-- AUTO_INCREMENT de tabela `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `logs`
--
ALTER TABLE `logs`
  ADD CONSTRAINT `fk_log_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL;

--
-- Restrições para tabelas `transacoes`
--
ALTER TABLE `transacoes`
  ADD CONSTRAINT `transacoes_ibfk_1` FOREIGN KEY (`conta_id`) REFERENCES `contas` (`id`),
  ADD CONSTRAINT `transacoes_ibfk_2` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
