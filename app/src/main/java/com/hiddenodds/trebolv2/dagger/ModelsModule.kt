package com.hiddenodds.trebolv2.dagger

import android.content.Context
import com.hiddenodds.trebolv2.model.executor.TaskListenerExecutor
import com.hiddenodds.trebolv2.presentation.mapper.*
import dagger.Module
import dagger.Provides

@Module
class ModelsModule(val context: Context) {

    @Provides
    fun provideITaskCompleteListener(): TaskListenerExecutor {
        return TaskListenerExecutor()
    }

    @Provides
    fun provideCustomerModelDataMapper(): CustomerModelDataMapper{
        return CustomerModelDataMapper(context)
    }

    @Provides
    fun provideMaterialModelDataMapper(): MaterialModelDataMapper{
        return MaterialModelDataMapper(context)
    }

    @Provides
    fun provideTypeNotificationModelDataMapper(): TypeNotificationModelDataMapper{
        return TypeNotificationModelDataMapper(context)
    }

    @Provides
    fun provideAssignedMaterialModelDataMapper(materialModelDataMapper:
                                               MaterialModelDataMapper):
            AssignedMaterialModelDataMapper{
        return AssignedMaterialModelDataMapper(context, materialModelDataMapper)
    }

    @Provides
    fun provideNotificationModelDataMapper(customerModelDataMapper:
                                           CustomerModelDataMapper,
                                           assignedMaterialModelDataMapper:
                                           AssignedMaterialModelDataMapper): NotificationModelDataMapper{
        return NotificationModelDataMapper(context,
                customerModelDataMapper, assignedMaterialModelDataMapper)
    }

    @Provides
    fun provideTechnicalModelDataMapper(notificationModelDataMapper:
                                        NotificationModelDataMapper): TechnicalModelDataMapper{
        return TechnicalModelDataMapper(context, notificationModelDataMapper)
    }

    @Provides
    fun provideDownloadModelDataMapper(): DownloadModelDataMapper{
        return DownloadModelDataMapper(context)
    }
}