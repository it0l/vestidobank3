package com.example.myapplication.ui.cartoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Cartao
import com.example.myapplication.data.repository.CartaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartoesViewModel(
    private val repository: CartaoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartoesUiState())
    val uiState: StateFlow<CartoesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllCartoes().collect { cartoes ->
                _uiState.update {
                    it.copy(cartoes = cartoes)
                }
            }
        }
    }

    fun onAlternarFormulario() {
        _uiState.update {
            it.copy(
                mostrarFormulario = !it.mostrarFormulario,
                mensagemAcao = ""
            )
        }
    }

    fun onAlternarModoBloqueio() {
        _uiState.update {
            val novoModo = !it.modoBloqueio

            it.copy(
                modoBloqueio = novoModo,
                mensagemAcao = if (novoModo) {
                    "Marque abaixo os cartões que deseja bloquear."
                } else {
                    "Modo de bloqueio fechado."
                }
            )
        }
    }

    fun onCarteira() {
        _uiState.update {
            it.copy(
                mensagemAcao = "${it.cartoes.size} cartão(ões) preparado(s) para a carteira."
            )
        }
    }

    fun onNomeChange(valor: String) {
        _uiState.update { it.copy(nome = valor) }
    }

    fun onFinalNumeroChange(valor: String) {
        _uiState.update {
            it.copy(finalNumero = valor.take(4))
        }
    }

    fun onTipoChange(valor: String) {
        _uiState.update { it.copy(tipo = valor) }
    }

    fun onLimiteChange(valor: String) {
        _uiState.update { it.copy(limite = valor) }
    }

    fun onUsadoChange(valor: String) {
        _uiState.update { it.copy(usado = valor) }
    }

    fun onAdicionar() {
        val state = _uiState.value
        val limiteNumerico = state.limite.replace(",", ".").toDoubleOrNull()
        val usadoNumerico = state.usado.replace(",", ".").toDoubleOrNull()

        if (
            state.nome.isBlank() ||
            state.finalNumero.length != 4 ||
            state.tipo.isBlank() ||
            limiteNumerico == null ||
            usadoNumerico == null
        ) {
            _uiState.update {
                it.copy(erro = "Preencha os campos corretamente.")
            }
            return
        }

        val cartao = Cartao(
            nome = state.nome,
            finalNumero = state.finalNumero,
            tipo = state.tipo,
            limite = limiteNumerico,
            usado = usadoNumerico
        )

        viewModelScope.launch {
            repository.inserir(cartao)

            _uiState.update {
                it.copy(
                    mostrarFormulario = false,
                    mensagemAcao = "Cartão adicionado com sucesso.",
                    nome = "",
                    finalNumero = "",
                    tipo = "",
                    limite = "",
                    usado = "",
                    erro = ""
                )
            }
        }
    }

    fun onDeletar(cartao: Cartao) {
        viewModelScope.launch {
            repository.deletar(cartao)
        }
    }

    fun onAlternarBloqueio(cartao: Cartao) {
        viewModelScope.launch {
            repository.atualizar(
                cartao.copy(
                    bloqueado = !cartao.bloqueado
                )
            )
        }
    }

    fun carregarDetalhe(id: Int) {
        viewModelScope.launch {
            val cartao = repository.buscarPorId(id)

            _uiState.update {
                it.copy(
                    cartaoDetalhe = cartao,
                    novoLimite = cartao?.limite?.toString() ?: "",
                    mensagemDetalhe = ""
                )
            }
        }
    }

    fun onNovoLimiteChange(valor: String) {
        _uiState.update {
            it.copy(novoLimite = valor)
        }
    }

    fun onSalvarLimite() {
        val state = _uiState.value
        val cartao = state.cartaoDetalhe ?: return
        val valor = state.novoLimite.replace(",", ".").toDoubleOrNull()

        if (valor == null || valor < cartao.usado) {
            _uiState.update {
                it.copy(
                    mensagemDetalhe = "O limite deve ser válido e não pode ser menor que o valor usado."
                )
            }
            return
        }

        val atualizado = cartao.copy(
            limite = valor
        )

        viewModelScope.launch {
            repository.atualizar(atualizado)
            val recarregado = repository.buscarPorId(atualizado.id)

            _uiState.update {
                it.copy(
                    cartaoDetalhe = recarregado,
                    mensagemDetalhe = "Limite atualizado."
                )
            }
        }
    }
}
