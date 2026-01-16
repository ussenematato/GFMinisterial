-- ============================================
-- QUERY SQL PARA CRIAÇÃO DA TABELA USUARIOS
-- ============================================
-- Execute este script no seu banco de dados para atualizar a estrutura da tabela usuarios

-- Opção 1: Se a tabela NÃO existe, criar nova tabela com toda a estrutura
-- Se a tabela ja existe e precisa ser atualizada, veja a Opção 2

-- OPÇÃO 1: CRIAR TABELA NOVA (Se não existe)
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    senha_hash VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL DEFAULT 'TESOUREIRO',
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_perfil CHECK (perfil IN ('SUPERADMIN', 'ADMINISTRADOR', 'CONTABILISTA', 'TESOUREIRO')),
    INDEX idx_email (email),
    INDEX idx_ativo (ativo)
);

-- ============================================
-- OPÇÃO 2: ATUALIZAR TABELA EXISTENTE
-- ============================================
-- Se a tabela usuarios JÁ EXISTE e precisa ser atualizada, execute os comandos abaixo:

-- Adicionar coluna telefone (se não existir)
ALTER TABLE usuarios ADD COLUMN telefone VARCHAR(20) IF NOT EXISTS;

-- Atualizar coluna perfil para aceitar novos valores
ALTER TABLE usuarios MODIFY COLUMN perfil VARCHAR(50) NOT NULL DEFAULT 'TESOUREIRO';

-- Adicionar constraint de check se não existir
-- (Nota: Esta sintaxe varia conforme o banco de dados)
-- Para MySQL 8.0+:
-- ALTER TABLE usuarios ADD CONSTRAINT chk_perfil_values 
-- CHECK (perfil IN ('SUPERADMIN', 'ADMINISTRADOR', 'CONTABILISTA', 'TESOUREIRO'));

-- Criar índices para melhor performance
ALTER TABLE usuarios ADD INDEX IF NOT EXISTS idx_email (email);
ALTER TABLE usuarios ADD INDEX IF NOT EXISTS idx_ativo (ativo);

-- ============================================
-- INSERIR USUÁRIO SUPERADMIN PADRÃO
-- ============================================
-- Senha padrão: "admin123" (MD5 hash)
INSERT IGNORE INTO usuarios (nome, email, telefone, senha_hash, perfil, ativo) 
VALUES ('Administrador Sistema', 'admin@ministerial.gov.mz', '+258 82 123 4567', '0192023a7bbd73250516f069df18b500', 'SUPERADMIN', TRUE);

-- ============================================
-- VERIFICAR ESTRUTURA (SELECT para confirmar)
-- ============================================
-- Execute este SELECT para confirmar que a tabela está corretamente estruturada:
-- SELECT * FROM usuarios;
-- DESCRIBE usuarios;  -- Mostra estrutura da tabela

-- ============================================
-- NOTES:
-- ============================================
-- 1. Valores de PERFIL permitidos:
--    - SUPERADMIN: Acesso total ao sistema
--    - ADMINISTRADOR: Gerenciamento de usuários e configurações
--    - CONTABILISTA: Visualização e registro de transações
--    - TESOUREIRO: Gerenciamento de contas e tesouraria
--
-- 2. A senha "admin123" em MD5 é: 0192023a7bbd73250516f069df18b500
--    Para criar outras senhas em MD5, use uma ferramenta online ou seu cliente SQL
--
-- 3. O campo telefone é opcional (VARCHAR 20)
--    Exemplo de formato: "+258 82 123 4567" ou "82-123-4567"
--
-- 4. Emails devem ser únicos (UNIQUE constraint)
--
-- 5. Todos os novos usuários começam como ATIVO = TRUE
-- ============================================
