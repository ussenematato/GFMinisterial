# LoginView - Documentação da Tela de Autenticação

## 📋 Visão Geral

O **LoginView** é a tela principal de entrada do SGEST que substitui as classes Main.java e MainSimples.java (agora removidas).

---

## 🎯 Funcionalidades

### 1. Autenticação de Usuários
- Campo de entrada para **Email**
- Campo de entrada para **Senha**
- Validação de credenciais contra o banco de dados
- Verificação de usuário ativo/inativo
- Feedback de erros em tempo real

### 2. Recuperação de Senha
- Diálogo para solicitar email
- Geração de senha temporária (8 caracteres)
- Atualização da senha no banco
- Notificação ao usuário
- **Nota:** Implementação atual é local. Em produção, implementar envio de email real.

### 3. Botões Principais
- **Entrar**: Autentica o usuário
- **Recuperar Senha**: Abre diálogo para recuperação
- **Sair**: Fecha a aplicação com confirmação

### 4. Interface Dividida
- **Painel Esquerdo**: Logo da empresa (azul escuro)
- **Painel Direito**: Formulário de login (claro)

---

## 🏗️ Estrutura Técnica

### Classe Principal: LoginView

```java
public class LoginView extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnRecuperarSenha;
    private JButton btnSair;
    private JLabel lblMensagem;
    private UsuarioController usuarioController;
    private Usuario usuarioLogado;
}
```

### Métodos Principais

#### `autenticar()`
1. Valida campos vazios
2. Busca usuário no banco por email
3. Verifica se usuário está ativo
4. Compara hash da senha
5. Se tudo OK: abre MenuPrincipal
6. Se erro: exibe mensagem de erro

#### `recuperarSenha()`
1. Abre diálogo para solicitar email
2. Busca usuário no banco
3. Gera nova senha temporária
4. Atualiza no banco
5. Exibe nova senha ao usuário

#### `gerarSenhaTemporaria()`
- Gera 8 caracteres aleatórios
- Mistura letras maiúsculas, minúsculas e números

#### `sair()`
- Pede confirmação
- Fecha aplicação com `System.exit(0)`

---

## 🖼️ Logos Disponíveis

A aplicação procura pelos arquivos nesta ordem:
1. `src/view/telas/logos/LogoOficial.jpeg`
2. `src/view/telas/logos/Logo.jpeg`
3. Fallback: Texto "SGEST" em branco

Se nenhuma imagem for encontrada, exibe texto.

---

## 🔐 Segurança Implementada

✅ **Hash MD5** - Senhas armazenadas como hash (não em texto plano)
✅ **Validação de Status** - Usuários inativos não conseguem fazer login
✅ **Email Único** - Busca por email valida existência
✅ **Feedback Mínimo** - "Email ou senha inválidos" (não diferencia qual está errado)
✅ **Limpeza de Campo** - Senha é apagada após falha de autenticação

---

## 📂 Arquivos Modificados

### Removidos
- ❌ `MainSimples.java` (duplicado, não mais necessário)

### Criados
- ✅ `LoginView.java` (nova tela de autenticação)

### Atualizados
- ✅ `Main.java` (agora inicia com LoginView ao invés de DashboardView)

---

## 🚀 Como Usar

### Iniciar a Aplicação

```bash
java -cp "lib/*:build/classes" view.telas.Main
```

Ou use a IDE para executar `Main.main()`

### Fazer Login

**Usuário Padrão (criado pelo script SQL):**
- Email: `admin@ministerial.gov.mz`
- Senha: `admin123`

### Criar Novo Usuário

1. Já deve estar logado como admin
2. Ir para AdmiSystem no MenuPrincipal
3. Criar novo usuário com email e senha

### Recuperar Senha Esquecida

1. Clicar em "Recuperar Senha"
2. Inserir email da conta
3. Receber nova senha temporária
4. Usar para fazer login
5. (Opcional) Alterar senha em Perfil

---

## 🎨 Layout

```
┌─────────────────────────────────────────────────┐
│                                                   │
│  ┌──────────────────┬──────────────────────────┐│
│  │                  │  Bem-vindo ao SGEST      ││
│  │     SGEST        │                          ││
│  │                  │  Email: [__________]    ││
│  │   [LOGO/IMG]     │  Senha: [__________]    ││
│  │                  │                          ││
│  │  "Sistema de     │  [ Entrar ]  [Recuperar]││
│  │   Gestão..."     │                          ││
│  │                  │  [  Sair  ]              ││
│  └──────────────────┴──────────────────────────┘│
│                                                   │
└─────────────────────────────────────────────────┘
```

---

## 🔄 Fluxo de Autenticação

```
LoginView
    ↓
[Usuário insere credenciais]
    ↓
autenticar()
    ↓
    ├─→ Email vazio? → Erro
    ├─→ Senha vazia? → Erro
    ├─→ Email não existe? → Erro
    ├─→ Usuário inativo? → Erro
    ├─→ Senha incorreta? → Erro + limpar campo
    └─→ Tudo OK?
        ↓
    Salvar usuário logado
        ↓
    Abrir MenuPrincipal(userId)
        ↓
    Fechar LoginView
        ↓
        MenuPrincipal (com todas as funcionalidades)
```

---

## 📊 Cores e Estilo

### Painel Esquerdo
- Fundo: Azul escuro `rgb(25, 25, 112)`
- Imagem: Escalada para 400x400px

### Painel Direito
- Fundo: Cinza claro `rgb(240, 240, 240)`
- Título: Azul escuro, 28pt, Bold
- Botões: UIStyler (Success, Secondary, Danger)

### Mensagens
- Erro: Vermelho `rgb(200, 0, 0)`
- Fonte: Arial 12pt

---

## 🐛 Troubleshooting

### Problema: "Email ou senha inválidos"
**Verificar:**
- Email está correto?
- Usuário existe no banco?
- Usuário está ativo (não desativado)?
- Senha está correta?

### Problema: Logo não aparece
**Verificar:**
- Arquivo existe em `src/view/telas/logos/LogoOficial.jpeg`?
- Arquivo é uma imagem JPEG válida?
- Aplicação rodando desde a raiz do projeto?

### Problema: "Erro ao autenticar: ..."
**Verificar:**
- Banco de dados está rodando?
- Tabela `usuarios` foi criada?
- Conexão com banco está OK?

### Problema: Recuperação de senha não funciona
**Verificar:**
- Email existe no banco?
- Usuário tem permissão para atualizar senha?
- Mensagem de sucesso aparece?

---

## 🔮 Melhorias Futuras

1. **Envio de Email Real**
   - Integrar SMTP
   - Enviar senha por email ao invés de exibir na tela

2. **Autenticação 2FA**
   - Verificação por SMS ou App
   - Código temporário por email

3. **Logs de Autenticação**
   - Registrar tentativas de login
   - Alertar sobre atividades suspeitas

4. **Validação de Força de Senha**
   - Exigir caracteres especiais
   - Prevenir senhas fracas

5. **Recuperação por Perguntas de Segurança**
   - Alternativa a email
   - Maior segurança

6. **Interface de Cadastro**
   - Permitir auto-registro (com aprovação)
   - Validação de email

---

## 📝 Notas Importantes

### Classe Main.java
- Agora é a classe de entrada principal
- Inicializa Look and Feel
- Cria e exibe LoginView
- Não precisa mais fornecer usuário ID (feito pelo login)

### UsuarioDAO
- Método `buscarPorEmail(String)` é crucial
- Método `atualizarSenha(Integer, String)` para recuperação

### UsuarioController
- Método `verificarSenha()` usado para comparar hashes
- Método `hashSenha()` gera MD5

---

## 🎯 Resumo das Mudanças

| Item | Antes | Depois |
|------|-------|--------|
| Entrada | MainSimples.java / Main.java | Main.java → LoginView |
| Usuário | Hardcoded ID 1 | Login obrigatório |
| Segurança | Sem autenticação | Email + Senha |
| Interface | Direto no Dashboard | Tela de Login |
| Recuperação | Não havia | Implementada |

