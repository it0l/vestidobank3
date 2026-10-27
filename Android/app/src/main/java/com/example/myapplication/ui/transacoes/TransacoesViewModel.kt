package com.example.myapplication.ui.transacoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Transacao
import com.example.myapplication.data.repository.TransacaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransacoesViewModel(
    private val repository: TransacaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransacoesUiState())
    val uiState: StateFlow<TransacoesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllTransacoes().collect { transacoes ->
                _uiState.update {
                    it.copy(transacoes = transacoes)
                }
            }
        }
    }

    fun onDescricaoChange(valor: String) {
        _uiState.update { it.copy(descricao = valor) }
    }

    fun onValorChange(valor: String) {
        _uiState.update { it.copy(valor = valor) }
    }

    fun onCategoriaChange(valor: String) {
        _uiState.update { it.copy(categoria = valor) }
    }

    fun onDataChange(valor: String) {
        _uiState.update { it.copy(data = valor) }
    }

    fun onAdicionar() {
        val state = _uiState.value
        val valorNumerico = state.valor.replace(",", ".").toDoubleOrNull()

        if (
            state.descricao.isBlank() ||
            state.categoria.isBlank() ||
            state.data.isBlank() ||
            valorNumerico == null ||
            valorNumerico <= 0
        ) {
            _uiState.update {
                it.copy(erro = "Preencha todos os campos corretamente.")
            }
            return
        }

        val transacao = Transacao(
            descricao = state.descricao,
            valor = valorNumerico,
            categoria = state.categoria,
            data = state.data
        )

        viewModelScope.launch {
            repository.inserir(transacao)
            _uiState.update {
                it.copy(
                    descricao = "",
                    valor = "",
                    categoria = "",
                    data = "",
                    erro = ""
                )
            }
        }
    }

    fun onDeletar(transacao: Transacao) {
        viewModelScope.launch {
            repository.deletar(transacao)
        }
    }

    fun carregarDetalhe(id: Int) {
        viewModelScope.launch {
            val transacao = repository.buscarPorId(id)

            _uiState.update {
                it.copy(
                    transacaoDetalhe = transacao,
                    detalheDescricao = transacao?.descricao ?: "",
                    detalheValor = transacao?.valor?.toString() ?: "",
                    detalheCategoria = transacao?.categoria ?: "",
                    detalheData = transacao?.data ?: "",
                    mensagemDetalhe = ""
                )
            }
        }
    }

    fun onDetalheDescricaoChange(valor: String) {
        _uiState.update { it.copy(detalheDescricao = valor) }
    }

    fun onDetalheValorChange(valor: String) {
        _uiState.update { it.copy(detalheValor = valor) }
    }

    fun onDetalheCategoriaChange(valor: String) {
        _uiState.update { it.copy(detalheCategoria = valor) }
    }

    fun onDetalheDataChange(valor: String) {
        _uiState.update { it.copy(detalheData = valor) }
    }

    fun onSalvarDetalhe() {
        val state = _uiState.value
        val transacaoAtual = state.transacaoDetalhe ?: return
        val valorNumerico = state.detalheValor.replace(",", ".").toDoubleOrNull()

        if (
            state.detalheDescricao.isBlank() ||
            state.detalheCategoria.isBlank() ||
            state.detalheData.isBlank() ||
            valorNumerico == null ||
            valorNumerico <= 0
        ) {
            _uiState.update {
                it.copy(mensagemDetalhe = "Preencha todos os campos corretamente.")
            }
            return
        }

        val atualizada = transacaoAtual.copy(
            descricao = state.detalheDescricao,
            valor = valorNumerico,
            categoria = state.detalheCategoria,
            data = state.detalheData
        )

        viewModelScope.launch {
            repository.atualizar(atualizada)
            val recarregada = repository.buscarPorId(atualizada.id)

            _uiState.update {
                it.copy(
                    transacaoDetalhe = recarregada,
                    mensagemDetalhe = "Movimentação atualizada no banco."
                )
            }
        }
    }
}
