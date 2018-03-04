package com.hiddenodds.trebolv2.dagger

import android.content.Context
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.model.interfaces.IPersistent
import com.hiddenodds.trebolv2.model.persistent.database.InstanceRealm
import com.hiddenodds.trebolv2.model.persistent.network.ServiceRemote
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
}