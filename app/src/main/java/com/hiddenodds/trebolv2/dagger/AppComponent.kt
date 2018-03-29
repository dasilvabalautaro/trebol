package com.hiddenodds.trebolv2.dagger

import android.app.Activity
import android.content.Context
import com.hiddenodds.trebolv2.App
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: App)
    fun context(): Context
    fun activity(): Activity
    fun plus(presenterModule: PresenterModule): PresenterComponent
    fun plus(modelsModule: ModelsModule): ModelsComponent
}