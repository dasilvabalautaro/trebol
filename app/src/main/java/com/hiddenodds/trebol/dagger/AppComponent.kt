package com.hiddenodds.trebol.dagger

import android.app.Activity
import android.content.Context
import com.hiddenodds.trebol.App
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