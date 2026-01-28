package com.lininglink.mechanicalpencils.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lininglink.mechanicalpencils.data.model.CollectionItem
import com.lininglink.mechanicalpencils.ui.components.MaterialSymbol
import com.lininglink.mechanicalpencils.ui.components.SymbolWeight
import com.lininglink.mechanicalpencils.ui.navigation.Browse
import com.lininglink.mechanicalpencils.ui.navigation.Collection
import com.lininglink.mechanicalpencils.ui.navigation.GroupDetail
import com.lininglink.mechanicalpencils.ui.navigation.Groups
import com.lininglink.mechanicalpencils.ui.navigation.ItemDetail
import com.lininglink.mechanicalpencils.ui.navigation.ProofUpload
import com.lininglink.mechanicalpencils.ui.navigation.Settings
import com.lininglink.mechanicalpencils.ui.navigation.UserProfile
import com.lininglink.mechanicalpencils.ui.screens.browse.BrowseScreen
import com.lininglink.mechanicalpencils.ui.screens.browse.BrowseViewModel
import com.lininglink.mechanicalpencils.ui.screens.collection.CollectionScreen
import com.lininglink.mechanicalpencils.ui.screens.collection.CollectionViewModel
import com.lininglink.mechanicalpencils.ui.screens.collection.ProofUploadScreen
import com.lininglink.mechanicalpencils.ui.screens.groups.GroupDetailScreen
import com.lininglink.mechanicalpencils.ui.screens.groups.GroupsListScreen
import com.lininglink.mechanicalpencils.ui.screens.groups.GroupsViewModel
import com.lininglink.mechanicalpencils.ui.screens.item.ItemDetailScreen
import com.lininglink.mechanicalpencils.ui.screens.item.ItemDetailViewModel
import com.lininglink.mechanicalpencils.ui.screens.profile.UserProfileScreen
import com.lininglink.mechanicalpencils.ui.screens.profile.UserProfileViewModel
import com.lininglink.mechanicalpencils.ui.screens.settings.SettingsScreen
import com.lininglink.mechanicalpencils.ui.screens.settings.SettingsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

data class BottomNavItem<T : Any>(
    val route: T,
    val title: String,
    val icon: String
)

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val browseViewModel: BrowseViewModel = koinViewModel()
    val groupsViewModel: GroupsViewModel = koinViewModel()
    val collectionViewModel: CollectionViewModel = koinViewModel()

    val bottomNavItems = listOf(
        BottomNavItem(
            route = Browse,
            title = "Browse",
            icon = "search"
        ),
        BottomNavItem(
            route = Groups,
            title = "Groups",
            icon = "folder"
        ),
        BottomNavItem(
            route = Collection,
            title = "Collection",
            icon = "checklist"
        ),
        BottomNavItem(
            route = Settings,
            title = "Settings",
            icon = "settings"
        )
    )

    // Determine if we should show bottom bar (hide on detail screens)
    val showBottomBar = currentDestination?.hierarchy?.any { dest ->
        bottomNavItems.any { dest.hasRoute(it.route::class) }
    } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.hasRoute(item.route::class)
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                MaterialSymbol(
                                    name = item.icon,
                                    weight = if (selected) SymbolWeight.Bold else SymbolWeight.Regular
                                )
                            },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Browse,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Browse Tab
            composable<Browse> {
                BrowseScreen(
                    viewModel = browseViewModel,
                    onItemClick = { itemId ->
                        navController.navigate(ItemDetail(itemId))
                    },
                    onLogout = onLogout
                )
            }

            composable<ItemDetail> { backStackEntry ->
                val route: ItemDetail = backStackEntry.toRoute()
                val itemDetailViewModel: ItemDetailViewModel = koinViewModel(
                    parameters = { parametersOf(route.itemId) }
                )
                ItemDetailScreen(
                    viewModel = itemDetailViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onOwnershipChanged = { itemId, owned ->
                        browseViewModel.updateItemOwned(itemId, owned)
                        groupsViewModel.updateItemOwned(itemId, owned)
                        // Refresh collection when ownership changes
                        collectionViewModel.loadCollection()
                    },
                    onUserClick = { userId ->
                        navController.navigate(UserProfile(userId))
                    }
                )
            }

            // Groups Tab
            composable<Groups> {
                GroupsListScreen(
                    viewModel = groupsViewModel,
                    onGroupClick = { groupId ->
                        navController.navigate(GroupDetail(groupId))
                    }
                )
            }

            composable<GroupDetail> { backStackEntry ->
                val route: GroupDetail = backStackEntry.toRoute()
                GroupDetailScreen(
                    groupId = route.groupId,
                    viewModel = groupsViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onItemClick = { itemId ->
                        navController.navigate(ItemDetail(itemId))
                    }
                )
            }

            // Collection Tab
            composable<Collection> {
                CollectionScreen(
                    viewModel = collectionViewModel,
                    onItemClick = { item ->
                        navController.navigate(
                            ProofUpload(
                                itemId = item.id,
                                title = item.title,
                                maker = item.maker,
                                imageUrl = item.imageUrl,
                                ownershipId = item.ownershipId,
                                hasProof = item.hasProof,
                                proofUrl = item.proofUrl
                            )
                        )
                    }
                )
            }

            composable<ProofUpload> { backStackEntry ->
                val route: ProofUpload = backStackEntry.toRoute()
                val collectionItem = CollectionItem(
                    id = route.itemId,
                    title = route.title,
                    maker = route.maker,
                    imageUrl = route.imageUrl,
                    ownershipId = route.ownershipId,
                    hasProof = route.hasProof,
                    proofUrl = route.proofUrl
                )
                ProofUploadScreen(
                    item = collectionItem,
                    viewModel = collectionViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // Settings Tab
            composable<Settings> {
                val settingsViewModel: SettingsViewModel = koinViewModel()
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onLogout = onLogout
                )
            }

            // User Profile
            composable<UserProfile> { backStackEntry ->
                val route: UserProfile = backStackEntry.toRoute()
                val userProfileViewModel: UserProfileViewModel = koinViewModel(
                    parameters = { parametersOf(route.userId) }
                )
                UserProfileScreen(
                    viewModel = userProfileViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
