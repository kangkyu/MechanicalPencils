package com.lininglink.mechanicalpencils

import android.app.Application
import com.lininglink.mechanicalpencils.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MechanicalPencilsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MechanicalPencilsApp)
            modules(appModule)
        }
    }
}
