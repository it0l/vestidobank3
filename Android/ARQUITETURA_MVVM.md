# Vestido Bank - Organização MVVM

A organização segue o padrão mostrado na aula de MVVM.

## Model

`data/local`
- `Transacao.kt`
- `Cartao.kt`
- `TransacaoDao.kt`
- `CartaoDao.kt`
- `AppDatabase.kt`

`data/repository`
- `TransacaoRepository.kt`
- `CartaoRepository.kt`

## ViewModel

`ui/transacoes`
- `TransacoesUiState.kt`
- `TransacoesViewModel.kt`
- `TransacoesViewModelFactory.kt`

`ui/cartoes`
- `CartoesUiState.kt`
- `CartoesViewModel.kt`
- `CartoesViewModelFactory.kt`

`ui/fatura`
- `FaturaUiState.kt`
- `FaturaViewModel.kt`
- `FaturaViewModelFactory.kt`

## View

As funções `@Composable` ficam nas pastas de `ui`.

As telas leem `uiState` com `collectAsStateWithLifecycle()` e chamam funções do ViewModel.

## Fluxo

Room -> DAO -> Repository -> ViewModel -> UiState -> Tela

Os eventos fazem o caminho contrário:

Tela -> ViewModel -> Repository -> DAO -> Room

## Fatura e Movimentações

A tela de Movimentações e a tela de Fatura usam o mesmo `TransacaoRepository`.

Quando uma movimentação é inserida, atualizada ou excluída, o Room atualiza o `Flow`.

O ViewModel da Fatura recebe a nova lista automaticamente.

O total da fatura é calculado com:

```kotlin
transacoes.sumOf { it.valor }
```

O limite disponível e o percentual utilizado são propriedades calculadas no `FaturaUiState`.
