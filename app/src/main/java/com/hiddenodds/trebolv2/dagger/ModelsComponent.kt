package com.hiddenodds.trebolv2.dagger

import com.hiddenodds.trebolv2.model.executor.CustomerExecutor
import com.hiddenodds.trebolv2.model.executor.MaterialExecutor
import com.hiddenodds.trebolv2.model.executor.TechnicalExecutor
import com.hiddenodds.trebolv2.model.executor.TypeNotificationExecutor
import dagger.Subcomponent

@Subcomponent(modules = [ModelsModule::class])
interface ModelsComponent {
    fun inject(customerExecutor: CustomerExecutor)
    fun inject(technicalExecutor: TechnicalExecutor)
    fun inject(materialExecutor: MaterialExecutor)
    fun inject(typeNotificationExecutor: TypeNotificationExecutor)
}