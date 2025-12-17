//-- Database: gestao_financeira
//CREATE DATABASE IF NOT EXISTS gestao_financeira;
//USE gestao_financeira;
//
//-- Tabela de contas
//CREATE TABLE contas (
//    id INT PRIMARY KEY AUTO_INCREMENT,
//    nome VARCHAR(100) NOT NULL,
//    tipo VARCHAR(50) NOT NULL,
//    saldo_inicial DECIMAL(10,2) DEFAULT 0.00,
//    saldo_atual DECIMAL(10,2) DEFAULT 0.00,
//    instituicao VARCHAR(100),
//    usuario_id INT NOT NULL,
//    ativo BOOLEAN DEFAULT TRUE,
//    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//    INDEX idx_usuario (usuario_id)
//);
//
//-- Tabela de categorias
//CREATE TABLE categorias (
//    id INT PRIMARY KEY AUTO_INCREMENT,
//    nome VARCHAR(100) NOT NULL,
//    tipo VARCHAR(20) NOT NULL,
//    descricao TEXT,
//    usuario_id INT NOT NULL,
//    cor VARCHAR(7) DEFAULT '#2196F3',
//    ativo BOOLEAN DEFAULT TRUE,
//    INDEX idx_usuario_tipo (usuario_id, tipo)
//);
//
//-- Tabela de transações
//CREATE TABLE transacoes (
//    id INT PRIMARY KEY AUTO_INCREMENT,
//    descricao VARCHAR(200) NOT NULL,
//    valor DECIMAL(10,2) NOT NULL,
//    tipo VARCHAR(20) NOT NULL,
//    data_transacao DATE NOT NULL,
//    data_vencimento DATE,
//    pago BOOLEAN DEFAULT FALSE,
//    recorrente BOOLEAN DEFAULT FALSE,
//    frequencia VARCHAR(20),
//    conta_id INT NOT NULL,
//    categoria_id INT NOT NULL,
//    usuario_id INT NOT NULL,
//    observacoes TEXT,
//    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//    FOREIGN KEY (conta_id) REFERENCES contas(id),
//    FOREIGN KEY (categoria_id) REFERENCES categorias(id),
//    INDEX idx_usuario_data (usuario_id, data_transacao),
//    INDEX idx_tipo_pago (tipo, pago)
//);
//
//-- Dados iniciais
//INSERT INTO categorias (nome, tipo, descricao, usuario_id, cor) VALUES
//('Alimentação', 'DESPESA', 'Gastos com supermercado e comida', 1, '#FF5722'),
//('Moradia', 'DESPESA', 'Aluguel, condomínio, IPTU', 1, '#673AB7'),
//('Transporte', 'DESPESA', 'Combustível, transporte público', 1, '#009688'),
//('Saúde', 'DESPESA', 'Médico, remédios, plano de saúde', 1, '#E91E63'),
//('Lazer', 'DESPESA', 'Cinema, restaurantes, viagens', 1, '#3F51B5'),
//('Educação', 'DESPESA', 'Cursos, livros, escola', 1, '#9C27B0'),
//('Salário', 'RECEITA', 'Rendimentos do trabalho', 1, '#4CAF50'),
//('Freelance', 'RECEITA', 'Trabalhos avulsos', 1, '#8BC34A'),
//('Investimentos', 'RECEITA', 'Rendimentos de investimentos', 1, '#CDDC39');
//
//INSERT INTO contas (nome, tipo, saldo_inicial, saldo_atual, instituicao, usuario_id) VALUES
//('Conta Corrente', 'CORRENTE', 5000.00, 5000.00, 'Banco do Brasil', 1),
//('Carteira', 'CARTEIRA', 500.00, 500.00, 'Dinheiro físico', 1),
//('Poupança', 'POUPANCA', 10000.00, 10000.00, 'Caixa Econômica', 1);