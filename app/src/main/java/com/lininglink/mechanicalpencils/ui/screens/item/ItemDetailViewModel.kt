package com.lininglink.mechanicalpencils.ui.screens.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lininglink.mechanicalpencils.data.model.ItemDetail
import com.lininglink.mechanicalpencils.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ItemDetailUiState {
    data object Loading : ItemDetailUiState()
    data class Success(val item: ItemDetail) : ItemDetailUiState()
    data class Error(val message: String) : ItemDetailUiState()
}

class ItemDetailViewModel(
    private val itemId: Int,
    private val itemRepository: ItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ItemDetailUiState>(ItemDetailUiState.Loading)
    val uiState: StateFlow<ItemDetailUiState> = _uiState.asStateFlow()

    private val _isTogglingOwnership = MutableStateFlow(false)
    val isTogglingOwnership: StateFlow<Boolean> = _isTogglingOwnership.asStateFlow()

    init {
        loadItem()
    }

    fun loadItem() {
        viewModelScope.launch {
            _uiState.value = ItemDetailUiState.Loading
            itemRepository.getItem(itemId)
                .onSuccess { item ->
                    _uiState.value = ItemDetailUiState.Success(item)
                }
                .onFailure { error ->
                    _uiState.value = ItemDetailUiState.Error(error.message ?: "Failed to load item")
                }
        }
    }

    fun toggleOwnership() {
        val currentState = _uiState.value
        if (currentState !is ItemDetailUiState.Success) return
        if (_isTogglingOwnership.value) return

        viewModelScope.launch {
            _isTogglingOwnership.value = true
            val item = currentState.item

            val result = if (item.owned) {
                itemRepository.unownItem(item.id)
            } else {
                itemRepository.ownItem(item.id)
            }

            result.onSuccess { updatedItem ->
                _uiState.value = ItemDetailUiState.Success(updatedItem)
            }

            _isTogglingOwnership.value = false
        }
    }
}
