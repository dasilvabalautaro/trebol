package com.hiddenodds.trebol.dagger

import android.app.Activity
import android.content.Context
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.model.interfaces.IPersistent
import com.hiddenodds.trebol.model.persistent.database.InstanceRealm
import com.hiddenodds.trebol.model.persistent.network.ServiceRemote
import com.hiddenodds.trebol.presentation.view.activities.MainActivity
import com.hiddenodds.trebol.tools.ConnectionNetwork
import com.hiddenodds.trebol.tools.LocaleUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton



@Module
class AppModule(private val app: App) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return this.app
    }

    @Provides
    fun provideInstanceRealm(): IPersistent{
        return InstanceRealm(app)
    }

    @Provides
    fun provideServiceRemote(): ServiceRemote {
        return ServiceRemote()
    }

    @Provides
    fun provideConnectionNetwork(): ConnectionNetwork {
        return ConnectionNetwork(app as Context)
    }

    @Provides
    fun provideActivity(): Activity{
        return MainActivity()
    }

    @Provides
    fun provideLocaleConfiguration(): LocaleUtils{
        return LocaleUtils()
    }
}