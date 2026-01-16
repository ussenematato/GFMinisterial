-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 16/01/2026 às 17:09
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
(2, 'Moradia', 'DESPESA', 'Aluguel, condomínio, IPTU', 1, '#673AB7', 1),
(3, 'Transporte', 'DESPESA', 'Combustível, transporte público', 1, '#009688', 1),
(4, 'Saúde', 'DESPESA', 'Médico, remédios, plano de saúde', 1, '#E91E63', 1),
(5, 'Lazer', 'DESPESA', 'Cinema, restaurantes, viagens', 1, '#3F51B5', 1),
(6, 'Educação', 'DESPESA', 'Cursos, livros, escola', 1, '#9C27B0', 1),
(7, 'Escola dominical', 'RECEITA', 'Colectas nas turmas da escola dominical', 1, '#4CAF50', 1),
(8, 'Dizimos', 'RECEITA', 'Dizimos dos menbros em Comunhao', 1, '#8BC34A', 1),
(9, 'Colectas', 'RECEITA', 'Recebimentos de colectas', 1, '#CDDC39', 1),
(10, 'Encontro das Senhoras', 'RECEITA', 'Colectas recebidas no encontro das senhoras', 1, '#336600', 1),
(11, 'Tafula Geral', 'RECEITA', 'Tafulas dos 2 domingos', 1, '#FFFFCC', 1);

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
(1, 'Conta Corrente', 'CORRENTE', 5000.00, 1220.00, 'Millenium Bim', 1, 1, '2026-01-09 14:24:24'),
(2, 'Fundo de Maneio', 'CARTEIRA', 500.00, 2000.00, 'Dinheiro físico', 1, 1, '2026-01-09 14:24:24'),
(3, 'MPesa', 'CARTEIRA', 10000.00, 500.00, 'Vodacom, SA', 1, 1, '2026-01-09 14:24:24'),
(4, 'E-Mola', 'CARTEIRA', 500.00, 300.00, 'Movitel', 1, 1, '2026-01-15 15:41:55');

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
  `categoria_id` int(11) NOT NULL,
  `usuario_id` int(11) NOT NULL,
  `observacoes` text DEFAULT NULL,
  `data_registro` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `transacoes`
--

INSERT INTO `transacoes` (`id`, `descricao`, `valor`, `tipo`, `data_transacao`, `data_vencimento`, `pago`, `recorrente`, `frequencia`, `conta_id`, `categoria_id`, `usuario_id`, `observacoes`, `data_registro`) VALUES
(1, 'Dizimos dos dizimos', 4890.00, 'DESPESA', '2026-01-09', '2026-01-09', 1, 0, NULL, 1, 1, 1, '', '2026-01-09 14:43:00'),
(2, 'Contribuicao', 6000.00, 'RECEITA', '2026-01-09', '2026-01-09', 1, 0, NULL, 1, 8, 1, '', '2026-01-09 15:12:42'),
(3, 'Entrega de documentos', 100.00, 'DESPESA', '2026-01-16', '2026-01-16', 1, 0, NULL, 3, 3, 1, '', '2026-01-16 12:01:39'),
(4, 'Pao para lanches', 250.00, 'DESPESA', '2026-01-16', '2026-01-16', 1, 0, NULL, 3, 5, 1, '', '2026-01-16 12:03:58'),
(5, 'Transporte', 50.00, 'DESPESA', '2026-01-16', '2026-01-16', 1, 0, NULL, 3, 3, 1, '', '2026-01-16 12:52:54'),
(6, 'Colecta Semanal', 300.00, 'RECEITA', '2026-01-16', '2026-01-16', 1, 0, NULL, 3, 9, 1, '', '2026-01-16 13:02:56'),
(7, 'Medicamentos', 200.00, 'DESPESA', '2026-01-16', '2026-01-16', 1, 0, NULL, 4, 4, 1, '', '2026-01-16 13:15:31'),
(8, 'Cadeado', 150.00, 'DESPESA', '2026-01-16', '2026-01-16', 1, 0, NULL, 3, 2, 1, '', '2026-01-16 13:17:06'),
(9, '[REVERSÃO] Cadeado', 150.00, 'RECEITA', '2026-01-16', '2026-01-16', 1, 0, NULL, 3, 2, 1, 'Reversão de transação desmarcada como paga', '2026-01-16 14:49:20');

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
(2, 'Julieta Sambo', 'julietasambo@gmail.com', '+2588787878', 'b67ca1a7fc49100e18cfbb1d947a9f3d', 'ADMINISTRADOR', 1, '2026-01-16 15:39:43');

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
-- Índices de tabela `transacoes`
--
ALTER TABLE `transacoes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `conta_id` (`conta_id`),
  ADD KEY `categoria_id` (`categoria_id`),
  ADD KEY `idx_usuario_data` (`usuario_id`,`data_transacao`),
  ADD KEY `idx_tipo_pago` (`tipo`,`pago`);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de tabela `contas`
--
ALTER TABLE `contas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de tabela `transacoes`
--
ALTER TABLE `transacoes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de tabela `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restrições para tabelas despejadas
--

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
