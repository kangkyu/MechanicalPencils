package com.lininglink.mechanicalpencils.ui.screens.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lininglink.mechanicalpencils.data.model.ItemDetail
import com.lininglink.mechanicalpencils.data.model.ItemProof
import com.lininglink.mechanicalpencils.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    viewModel: ItemDetailViewModel,
    onNavigateBack: () -> Unit,
    onOwnershipChanged: (itemId: Int, owned: Boolean) -> Unit = { _, _ -> },
    onUserClick: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val isTogglingOwnership by viewModel.isTogglingOwnership.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is ItemDetailUiState.Success) {
            val item = (uiState as ItemDetailUiState.Success).item
            onOwnershipChanged(item.id, item.owned)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (uiState) {
                            is ItemDetailUiState.Success -> (uiState as ItemDetailUiState.Success).item.title
                            else -> "Item Details"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is ItemDetailUiState.Loading -> {
                    LoadingIndicator()
                }
                is ItemDetailUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (uiState as ItemDetailUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is ItemDetailUiState.Success -> {
                    ItemDetailContent(
                        item = (uiState as ItemDetailUiState.Success).item,
                        isTogglingOwnership = isTogglingOwnership,
                        onToggleOwnership = viewModel::toggleOwnership,
                        onUserClick = onUserClick
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemDetailContent(
    item: ItemDetail,
    isTogglingOwnership: Boolean,
    onToggleOwnership: () -> Unit,
    onUserClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        item.imageUrl?.let { url ->
            AsyncImage(
                model = url,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            item.maker?.let { maker ->
                DetailRow(label = "Maker", value = maker.title)
            }

            item.modelNumber?.let { modelNumber ->
                DetailRow(label = "Model Number", value = modelNumber)
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (item.owned) {
                OutlinedButton(
                    onClick = onToggleOwnership,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isTogglingOwnership,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isTogglingOwnership) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(20.dp)
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
                            )
                            Text("In Collection")
                        }
                    }
                }
            } else {
                Button(
                    onClick = onToggleOwnership,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isTogglingOwnership
                ) {
                    if (isTogglingOwnership) {
                        CircularProgressIndicator(
                            modifier = Modifier.height(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Add to Collection")
                    }
                }
            }

            // Proofs Section
            if (!item.proofs.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Proof Photos",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                item.proofs.forEach { proof ->
                    ProofCard(
                        proof = proof,
                        onUserClick = onUserClick
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ProofCard(
    proof: ItemProof,
    onUserClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onUserClick(proof.userId) }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = proof.proofUrl,
                contentDescription = "Proof photo",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = proof.userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                proof.createdAt?.let { date ->
                    Text(
                        text = date.take(10),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
