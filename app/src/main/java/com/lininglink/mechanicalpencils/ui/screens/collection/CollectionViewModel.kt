package com.lininglink.mechanicalpencils.ui.screens.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lininglink.mechanicalpencils.data.model.CollectionItem
import com.lininglink.mechanicalpencils.data.repository.CollectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CollectionUiState(
    val items: List<CollectionItem> = emptyList(),
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ProofUploadState(
    val isUploading: Boolean = false,
    val uploadSuccess: Boolean = false,
    val error: String? = null
)

class CollectionViewModel(private val collectionRepository: CollectionRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()

    private val _proofUploadState = MutableStateFlow(ProofUploadState())
    val proofUploadState: StateFlow<ProofUploadState> = _proofUploadState.asStateFlow()

    init {
        loadCollection()
    }

    fun loadCollection() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            collectionRepository.getCollection()
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        items = response.items,
                        totalCount = response.totalCount,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to load collection",
                        isLoading = false
                    )
                }
        }
    }

    fun uploadProof(ownershipId: Int, imageBytes: ByteArray, fileName: String) {
        viewModelScope.launch {
            _proofUploadState.value = ProofUploadState(isUploading = true)
            collectionRepository.uploadProof(ownershipId, imageBytes, fileName)
                .onSuccess { ownership ->
                    _proofUploadState.value = ProofUploadState(uploadSuccess = true)
                    // Update the item in the list
                    _uiState.value = _uiState.value.copy(
                        items = _uiState.value.items.map { item ->
                            if (item.ownershipId == ownershipId) {
                                item.copy(hasProof = ownership.hasProof, proofUrl = ownership.proofUrl)
                            } else item
                        }
                    )
                }
                .onFailure { error ->
                    _proofUploadState.value = ProofUploadState(
                        error = error.message ?: "Failed to upload proof"
                    )
                }
        }
    }

    fun resetProofUploadState() {
        _proofUploadState.value = ProofUploadState()
    }
}
