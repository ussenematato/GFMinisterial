# AdmiSystem - Documentação Completa

## 📋 Visão Geral

O **AdmiSystem** é um novo módulo do SGEST que permite gerenciar usuários do sistema com diferentes níveis de acesso. Permite criar, editar, desativar usuários e alterar suas senhas.

---

## 🎯 Funcionalidades

### Criar Usuário
- Nome Completo (obrigatório)
- Email (obrigatório, único)
- Telefone (opcional)
- Perfil de Acesso:
  - **SUPERADMIN**: Acesso total ao sistema
  - **ADMINISTRADOR**: Gerenciamento de usuários e configurações
  - **CONTABILISTA**: Visualização e registro de transações
  - **TESOUREIRO**: Gerenciamento de contas e tesouraria
- Senha (obrigatória, mínimo 6 caracteres)
- Visualizar senha enquanto digita (checkbox)

### Editar Usuário
- Modificar nome, email, telefone e perfil
- Senha separada (botão "Alterar Senha")

### Gerenciar Senhas
- Alterar senha via dialog específico
- Visualizar senha durante a alteração
- Validação automática (mínimo 6 caracteres)

### Desativar Usuário
- Desativar sem deletar (soft delete)
- Pode ser reativado futuramente
- Usuários desativados aparecem na listagem

### Listar Usuários
- Tabela com todos os usuários (ativos e inativos)
- Informações: ID, Nome, Email, Telefone, Perfil, Status, Data de Cadastro
- Seleção para operações de edição/exclusão

---

## 🏗️ Arquitetura

### Estrutura de Arquivos Criados

```
src/
├── model/
│   ├── entity/
│   │   └── Usuario.java (ATUALIZADO - adicionado campo telefone)
│   └── dao/
│       └── UsuarioDAO.java (NOVO)
├── controller/
│   └── UsuarioController.java (NOVO)
└── view/
    └── telas/
        └── componentes/
            ├── AdmiSystemCard.java (NOVO)
            └── MenuPrincipal.java (ATUALIZADO)
```

### Entidade: Usuario

```java
public class Usuario {
    private Integer id;
    private String nome;              // Nome completo
    private String email;             // Email único
    private String telefone;          // Telefone (novo)
    private String senhaHash;         // Hash MD5 da senha
    private String perfil;            // SUPERADMIN, ADMINISTRADOR, CONTABILISTA, TESOUREIRO
    private Boolean ativo;            // Status ativo/inativo
    private LocalDateTime dataCadastro; // Data de criação
}
```

### DAO: UsuarioDAO

Métodos principais:
- `criar(Usuario)` - Inserir novo usuário
- `buscarPorId(Integer)` - Buscar por ID
- `buscarPorEmail(String)` - Buscar por email
- `listarTodos()` - Listar todos os usuários
- `listarAtivos()` - Listar apenas ativos
- `atualizar(Usuario)` - Atualizar dados
- `atualizarSenha(Integer, String)` - Alterar senha
- `desativar(Integer)` - Desativar usuário
- `ativar(Integer)` - Ativar usuário

### Controller: UsuarioController

Métodos principais:
- `criarUsuario(nome, email, telefone, senha, perfil)` - Criar com validações
- `atualizarUsuario(id, nome, email, telefone, perfil)` - Atualizar
- `atualizarSenha(id, novaSenha)` - Alterar senha
- `desativarUsuario(id)` - Desativar
- `ativarUsuario(id)` - Ativar
- `listarUsuarios()` - Listar todos
- `listarUsuariosAtivos()` - Listar ativos
- `buscarPorId(id)` - Buscar específico

**Utilities:**
- `isEmailValido(email)` - Validação de email via regex
- `validarSenha(senha)` - Validação de força de senha
- `hashSenha(senha)` - Hash MD5
- `verificarSenha(senha, hash)` - Comparar senha com hash

### View: AdmiSystemCard

Componente que estende `CardBase` com:
- Painel de Formulário (entrada de dados)
- Painel de Tabela (listagem)
- Painel de Botões (ações)
- Dialog para alteração de senha

**Funcionalidades UI:**
- Validação em tempo real
- Mensagens de feedback
- Tabela com seleção
- Visualização de senha (checkbox)
- Dialog customizado para alteração de senha

### Integração: MenuPrincipal

Adicionado:
- Card `admiSystemCard` no sistema de navegação
- Botão "AdmiSystem" na barra de navegação (destacado em vermelho)
- Ação do botão: `mostrarTela("ADMISYSTEM")`

---

## 🗄️ Script SQL

Arquivo: `CREATE_USUARIOS_TABLE.sql`

### Criar Tabela Completa

```sql
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
```

### Atualizar Tabela Existente

Se a tabela já existe:
```sql
ALTER TABLE usuarios ADD COLUMN telefone VARCHAR(20);
ALTER TABLE usuarios MODIFY COLUMN perfil VARCHAR(50) NOT NULL DEFAULT 'TESOUREIRO';
ALTER TABLE usuarios ADD INDEX IF NOT EXISTS idx_email (email);
ALTER TABLE usuarios ADD INDEX IF NOT EXISTS idx_ativo (ativo);
```

### Inserir Admin Padrão

```sql
INSERT IGNORE INTO usuarios (nome, email, telefone, senha_hash, perfil, ativo) 
VALUES ('Administrador Sistema', 'admin@ministerial.gov.mz', '+258 82 123 4567', '0192023a7bbd73250516f069df18b500', 'SUPERADMIN', TRUE);
```

**Credenciais Admin:**
- Email: `admin@ministerial.gov.mz`
- Senha: `admin123`

---

## 🔐 Segurança

### Hash de Senha
- Utiliza **MD5** (implementação simples)
- **RECOMENDAÇÃO FUTURA**: Migrar para BCrypt ou Argon2

### Validação
- Email via regex
- Senha mínimo 6 caracteres
- Email único no banco
- Perfil restrito a valores pré-definidos

### Acesso ao AdmiSystem
- Verificar se usuário é SUPERADMIN/ADMINISTRADOR (futura implementação)
- Atualmente acessível a todos (implementar validação de perfil)

---

## 🎨 Interface

### Estilo
- Botões seguem padrão UIStyler
- Salvar: Verde (Success)
- Editar: Azul (Secondary)
- Alterar Senha: Laranja (Warning)
- Excluir: Vermelho (Danger)
- Cancelar: Cinza (Neutral)

### Componentes
- GridBagLayout para formulário
- DefaultTableModel para listagem
- JPasswordField para senha com toggle
- Dialog customizado para alteração de senha

---

## 📝 Como Usar

### 1. Executar Script SQL

```bash
# No seu cliente MySQL/MariaDB
mysql -u seu_usuario -p seu_banco < CREATE_USUARIOS_TABLE.sql
```

### 2. Acessar AdmiSystem

1. Abrir aplicação SGEST
2. Fazer login
3. Clicar em "AdmiSystem" na barra de navegação
4. Gerenciar usuários

### 3. Criar Novo Usuário

1. Preencher formulário:
   - Nome Completo
   - Email
   - Telefone (opcional)
   - Selecionar Perfil
   - Digitar Senha
2. (Opcional) Marcar "Mostrar Senha" para visualizar
3. Clicar "Salvar"

### 4. Editar Usuário

1. Selecionar na tabela
2. Clicar "Editar"
3. Modificar campos (exceto senha)
4. Clicar "Atualizar"

### 5. Alterar Senha

1. Selecionar na tabela
2. Clicar "Alterar Senha"
3. Digitar nova senha no dialog
4. (Opcional) Marcar "Mostrar Senha"
5. Clicar "OK"

### 6. Desativar Usuário

1. Selecionar na tabela
2. Clicar "Excluir"
3. Confirmar
4. Usuário aparece como "Inativo"

---

## ⚙️ Configuração Futura

### Implementar Validação de Acesso

Adicionar no `MenuPrincipal`:

```java
btnAdmiSystem.addActionListener(e -> {
    Usuario usuarioLogado = // buscar usuário logado
    if ("SUPERADMIN".equals(usuarioLogado.getPerfil()) || 
        "ADMINISTRADOR".equals(usuarioLogado.getPerfil())) {
        mostrarTela("ADMISYSTEM");
    } else {
        JOptionPane.showMessageDialog(this, "Acesso negado", "Erro", JOptionPane.ERROR_MESSAGE);
    }
});
```

### Melhorar Hash de Senha

Substituir MD5 por BCrypt:

```java
// Adicionar dependência: bcrypt
// import at.favre.lib.crypto.bcrypt.BCrypt;

public static String hashSenha(String senha) {
    return BCrypt.withDefaults().hashToString(12, senha.toCharArray());
}

public static boolean verificarSenha(String senha, String hash) {
    return BCrypt.verifyer().verify(senha.toCharArray(), hash).verified;
}
```

---

## 📊 Dados de Teste

### Perfis Disponíveis

| Perfil | Acesso |
|--------|--------|
| SUPERADMIN | Acesso total, gerenciar usuários |
| ADMINISTRADOR | Gerenciar usuários, configurações |
| CONTABILISTA | Visualizar e registrar transações |
| TESOUREIRO | Gerenciar contas e tesouraria |

### Usuários Pré-carregados

Após executar o SQL:
- Admin: `admin@ministerial.gov.mz` / `admin123`

---

## 🐛 Troubleshooting

### Erro ao criar usuário

**Problema:** "Email já cadastrado"
**Solução:** Usar email diferente ou verificar email no banco

**Problema:** "Senha deve ter no mínimo 6 caracteres"
**Solução:** Digitar senha com pelo menos 6 caracteres

### Usuário não aparece na tabela

**Problema:** Usuário criado mas não lista
**Solução:** Clicar "Cancelar" para atualizar a tabela

### Não consegue alterar senha

**Problema:** Dialog não aparece
**Solução:** Garantir que usuário está selecionado na tabela

---

## 📞 Suporte

Para dúvidas sobre implementação ou melhorias, consultar documentação de código comentado em:
- `UsuarioController.java` - Lógica de negócio
- `UsuarioDAO.java` - Acesso a dados
- `AdmiSystemCard.java` - Interface gráfica

