package com.lininglink.mechanicalpencils.ui.screens.groups

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lininglink.mechanicalpencils.data.model.Item
import com.lininglink.mechanicalpencils.data.model.ItemGroup
import com.lininglink.mechanicalpencils.data.repository.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GroupsUiState(
    val groups: List<ItemGroup> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class GroupDetailUiState(
    val group: ItemGroup? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class GroupsViewModel(private val groupRepository: GroupRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupsUiState())
    val uiState: StateFlow<GroupsUiState> = _uiState.asStateFlow()

    private val _detailState = MutableStateFlow(GroupDetailUiState())
    val detailState: StateFlow<GroupDetailUiState> = _detailState.asStateFlow()

    init {
        loadGroups()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            groupRepository.getGroups()
                .onSuccess { groups ->
                    _uiState.value = _uiState.value.copy(
                        groups = groups,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to load groups",
                        isLoading = false
                    )
                }
        }
    }

    fun loadGroupDetail(groupId: Int) {
        viewModelScope.launch {
            _detailState.value = GroupDetailUiState(isLoading = true)
            groupRepository.getGroup(groupId)
                .onSuccess { group ->
                    _detailState.value = GroupDetailUiState(group = group)
                }
                .onFailure { error ->
                    _detailState.value = GroupDetailUiState(
                        error = error.message ?: "Failed to load group"
                    )
                }
        }
    }

    fun updateItemOwned(itemId: Int, owned: Boolean) {
        val currentGroup = _detailState.value.group ?: return
        val updatedItems = currentGroup.items?.map { item ->
            if (item.id == itemId) item.copy(owned = owned) else item
        }
        _detailState.value = _detailState.value.copy(
            group = currentGroup.copy(items = updatedItems)
        )
    }
}
