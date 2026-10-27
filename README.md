# Vestido Bank — Trabalho 3

Aplicativo bancário desenvolvido em Kotlin com Jetpack Compose para o Trabalho 3 da disciplina de Desenvolvimento de Aplicativos Móveis.

O projeto dá continuidade ao Trabalho 2, mantendo a navegação, as listas, os formulários e as telas de detalhes, mas agora utilizando persistência local com Room.

## Integrantes

- Lucas Ito
- Rafael Moritz
- Pedro

## Evolução do projeto

### Trabalho 1
Criação das primeiras telas do aplicativo, com foco em interface, organização visual e componentes do Jetpack Compose.

### Trabalho 2
Evolução para um aplicativo funcional, com:

- Navegação real entre telas
- NavHost e NavController
- Barra de navegação inferior
- Listas dinâmicas
- Formulários
- Adição e remoção de itens
- Telas de detalhes
- Passagem de ID entre telas

### Trabalho 3
Nesta etapa foi adicionada persistência local utilizando Room.

Agora os dados de movimentações e cartões são armazenados em um banco SQLite local e permanecem salvos mesmo após fechar e abrir o aplicativo novamente.

## Tecnologias utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Material 3
- Navigation Compose
- Room
- SQLite
- KSP
- Coroutines

## Persistência com Room

O projeto possui duas entidades:

- `Transacao`
- `Cartao`

Cada entidade possui seu próprio DAO:

- `TransacaoDao`
- `CartaoDao`

O banco é centralizado pela classe:

- `AppDatabase`

O `AppDatabase` utiliza o padrão Singleton com `@Volatile` e `synchronized`, evitando a criação de várias instâncias do banco.

## CRUD implementado

As duas entidades possuem operações completas de CRUD:

- **Create:** inserção de novos registros com `@Insert`
- **Read:** consulta dos registros com `@Query`
- **Update:** atualização com `@Update`
- **Delete:** exclusão com `@Delete`

Todos os métodos de acesso ao banco foram implementados como `suspend fun`.

## Consulta filtrada

Os dois DAOs possuem consulta por ID.

Exemplo:

```kotlin
@Query("SELECT * FROM transacoes WHERE id = :id LIMIT 1")
suspend fun buscarPorId(id: Int): Transacao?
```

Essa consulta é utilizada nas telas de detalhes.

Ao clicar em uma movimentação ou cartão, a navegação envia somente o `id`. A tela de detalhes consulta o Room diretamente para carregar o registro correspondente.

## Execução fora da Main Thread

As operações de banco são executadas utilizando corrotinas e `Dispatchers.IO`.

Exemplo:

```kotlin
LaunchedEffect(Unit) {
    transacoes = withContext(Dispatchers.IO) {
        transacaoDao.buscarTodos()
    }
}
```

Após operações de inserção, atualização ou exclusão, a lista é carregada novamente para refletir os dados atuais do banco.

## Funcionalidades

O aplicativo possui:

- Tela inicial
- Tela de fatura
- Tela de movimentações
- Tela de cartões
- Tela Pix
- Tela de detalhes da movimentação
- Tela de detalhes do cartão
- Navegação inferior
- Navegação entre telas
- Cadastro de movimentações
- Edição de movimentações
- Exclusão de movimentações
- Cadastro de cartões
- Atualização de cartões
- Bloqueio e desbloqueio de cartões
- Exclusão de cartões
- Consulta de registros por ID
- Persistência local com Room

## Persistência

Os dados são armazenados no banco local:

```text
vestidobank.db
```

Após cadastrar uma movimentação ou cartão, fechar completamente o aplicativo e abrir novamente, o registro continua disponível.

## Estrutura principal

```text
model/
├── Transacao.kt
└── Cartao.kt

database/
├── AppDatabase.kt
├── TransacaoDao.kt
└── CartaoDao.kt

navigation/
├── AppNavigation.kt
└── Rotas.kt

telas/
├── fatura/
├── transacoes/
├── cartoes/
└── pix/
```

## Como executar

1. Clone ou baixe este repositório.
2. Abra a pasta `Android` no Android Studio.
3. Aguarde a sincronização do Gradle.
4. Certifique-se de que as dependências foram instaladas corretamente.
5. Conecte um aparelho Android ou inicie um emulador.
6. Execute o projeto pelo botão **Run** do Android Studio.

## Requisitos do Trabalho 3 atendidos

- 2 entidades com `@Entity`
- Chaves primárias com `autoGenerate = true`
- 2 DAOs
- CRUD completo nos dois DAOs
- Métodos dos DAOs como `suspend fun`
- Consulta filtrada por ID
- `AppDatabase`
- Singleton do banco
- Room + KSP
- Operações em `Dispatchers.IO`
- Leitura do banco ao entrar nas telas
- Tela de detalhes buscando diretamente pelo ID
- Persistência após fechar e abrir o aplicativo novamente

## Repositório

Projeto desenvolvido para fins acadêmicos como evolução do Vestido Bank ao longo dos Trabalhos 1, 2 e 3.
