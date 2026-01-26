package com.lininglink.mechanicalpencils.ui.screens.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lininglink.mechanicalpencils.data.model.Item
import com.lininglink.mechanicalpencils.data.model.Pagination
import com.lininglink.mechanicalpencils.data.repository.ItemRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BrowseUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val pagination: Pagination? = null,
    val searchQuery: String = ""
)

class BrowseViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BrowseUiState())
    val uiState: StateFlow<BrowseUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val query = _uiState.value.searchQuery.takeIf { it.isNotBlank() }
            itemRepository.getItems(page = 1, query = query)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        items = response.items,
                        pagination = response.pagination,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to load items",
                        isLoading = false
                    )
                }
        }
    }

    fun loadMoreItems() {
        val pagination = _uiState.value.pagination ?: return
        if (_uiState.value.isLoadingMore) return
        if (pagination.currentPage >= pagination.totalPages) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingMore = true)

            val nextPage = pagination.currentPage + 1
            val query = _uiState.value.searchQuery.takeIf { it.isNotBlank() }
            itemRepository.getItems(page = nextPage, query = query)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        items = _uiState.value.items + response.items,
                        pagination = response.pagination,
                        isLoadingMore = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to load more items",
                        isLoadingMore = false
                    )
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            loadItems()
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchQuery = "")
        loadItems()
    }

    fun updateItemOwned(itemId: Int, owned: Boolean) {
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.map { item ->
                if (item.id == itemId) item.copy(owned = owned) else item
            }
        )
    }
}
