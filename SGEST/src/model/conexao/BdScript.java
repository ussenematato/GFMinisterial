/*

-- Database: gestao_financeira
CREATE DATABASE IF NOT EXISTS gestao_financeira;
USE gestao_financeira;

-- Tabela de usuários
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    senha_hash VARCHAR(255) NOT NULL,
    perfil ENUM('SUPERADMIN', 'ADMINISTRADOR', 'CONTABILISTA', 'TESOUREIRO') DEFAULT 'TESOUREIRO',
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de contas
CREATE TABLE contas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(10,2) DEFAULT 0.00,
    saldo_atual DECIMAL(10,2) DEFAULT 0.00,
    instituicao VARCHAR(100),
    usuario_id INT NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_usuario (usuario_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabela de categorias
CREATE TABLE categorias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    descricao TEXT,
    usuario_id INT NOT NULL,
    cor VARCHAR(7) DEFAULT '#2196F3',
    ativo BOOLEAN DEFAULT TRUE,
    INDEX idx_usuario_tipo (usuario_id, tipo),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabela de transações
CREATE TABLE transacoes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(200) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    data_transacao DATE NOT NULL,
    data_vencimento DATE,
    pago BOOLEAN DEFAULT FALSE,
    recorrente BOOLEAN DEFAULT FALSE,
    frequencia VARCHAR(20),
    conta_id INT NOT NULL,
    categoria_id INT NOT NULL,
    usuario_id INT NOT NULL,
    observacoes TEXT,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conta_id) REFERENCES contas(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_usuario_data (usuario_id, data_transacao),
    INDEX idx_tipo_pago (tipo, pago)
);

-- Tabela de logs
CREATE TABLE IF NOT EXISTS logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT DEFAULT NULL,
    nome_usuario VARCHAR(255) NOT NULL,
    operacao VARCHAR(50) NOT NULL,
    descricao TEXT,
    tabela VARCHAR(50),
    registro_id INT,
    data_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    status_operacao VARCHAR(20) DEFAULT 'SUCESSO',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

INSERT INTO usuarios (nome, email, telefone, senha_hash, perfil, ativo) 
VALUES (
    'Administrador Master',
    'superadmin@gestao.com',
    '(11) 99999-8888',
    '7e5e8e7c9b4e1b8f9a3d2c1b0e9f8d7c6b5a4e3d2c1b0a9f8e7d6c5b4a3e2d1c0',
    'SUPERADMIN',
    TRUE
);

*/

// senha_digitada = 'SuperAdmin@2026';