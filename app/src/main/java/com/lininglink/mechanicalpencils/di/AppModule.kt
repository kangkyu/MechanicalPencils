package com.lininglink.mechanicalpencils.di

import com.lininglink.mechanicalpencils.data.api.ApiService
import com.lininglink.mechanicalpencils.data.local.TokenManager
import com.lininglink.mechanicalpencils.data.repository.AuthRepository
import com.lininglink.mechanicalpencils.data.repository.CollectionRepository
import com.lininglink.mechanicalpencils.data.repository.GroupRepository
import com.lininglink.mechanicalpencils.data.repository.ItemRepository
import com.lininglink.mechanicalpencils.data.repository.UserRepository
import com.lininglink.mechanicalpencils.ui.screens.auth.AuthViewModel
import com.lininglink.mechanicalpencils.ui.screens.browse.BrowseViewModel
import com.lininglink.mechanicalpencils.ui.screens.collection.CollectionViewModel
import com.lininglink.mechanicalpencils.ui.screens.groups.GroupsViewModel
import com.lininglink.mechanicalpencils.ui.screens.item.ItemDetailViewModel
import com.lininglink.mechanicalpencils.ui.screens.profile.UserProfileViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Local
    single { TokenManager(androidContext()) }

    // Ktor HttpClient
    single {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        android.util.Log.d("KtorClient", message)
                    }
                }
                level = LogLevel.BODY
            }

            defaultRequest {
                val tokenManager: TokenManager = get()
                val token = runBlocking { tokenManager.getToken() }
                token?.let {
                    header("Authorization", "Bearer $it")
                }
            }
        }
    }

    // API Service
    single { ApiService(get()) }

    // Repositories
    single { AuthRepository(get(), get()) }
    single { ItemRepository(get()) }
    single { GroupRepository(get()) }
    single { CollectionRepository(get()) }
    single { UserRepository(get()) }

    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { BrowseViewModel(get()) }
    viewModel { params -> ItemDetailViewModel(params.get(), get()) }
    viewModel { GroupsViewModel(get()) }
    viewModel { CollectionViewModel(get()) }
    viewModel { params -> UserProfileViewModel(params.get(), get()) }
}
