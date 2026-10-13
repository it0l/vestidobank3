# Vestido Bank — MAF

Projeto desenvolvido para o Trabalho 2 — MAF (Mínimo Aplicativo Funcional), dando continuidade ao aplicativo desenvolvido no Trabalho 1.

## Integrantes

Lucas Ito
Rafael Moritz
Pedro

## Sobre o projeto

O Vestido Bank é um aplicativo bancário desenvolvido em Kotlin utilizando Jetpack Compose.

No Trabalho 1, o projeto possuía três telas principais com foco na interface visual e estilização.

No Trabalho 2, o aplicativo foi evoluído para possuir navegação real entre as telas, listas dinâmicas, formulários para adicionar dados, remoção de itens e telas de detalhes.

## Tecnologias utilizadas

- Kotlin
- Android Studio
- Jetpack Compose
- Navigation Compose
- Material 3
- Gradle

## Funcionalidades

O aplicativo possui:

- Tela inicial com informações da conta
- Tela de fatura
- Tela de movimentações
- Tela de cartões
- Tela de detalhes da movimentação
- Tela de detalhes do cartão
- Tela Pix
- Barra de navegação inferior
- Navegação real entre as telas
- Adição de movimentações
- Remoção de movimentações
- Adição de cartões
- Remoção de cartões
- Bloqueio e desbloqueio de cartão
- Visualização dos detalhes de cada item
- Alteração do limite do cartão
- Cálculo do limite disponível
- Cálculo da porcentagem do limite utilizado

## Estrutura de navegação

A navegação do aplicativo foi centralizada no arquivo:

`AppNavigation.kt`

As rotas foram organizadas através do objeto:

`Rotas.kt`

Exemplo:

```kotlin
object Rotas {
    const val HOME = "home"
    const val FATURA = "fatura"
    const val CARTOES = "cartoes"
    const val TRANSACOES = "transacoes"
    const val PIX = "pix"
}