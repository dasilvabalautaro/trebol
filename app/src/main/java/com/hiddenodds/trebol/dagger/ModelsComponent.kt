package com.hiddenodds.trebol.dagger

import com.hiddenodds.trebol.model.executor.*
import dagger.Subcomponent

@Subcomponent(modules = [ModelsModule::class])
interface ModelsComponent {
    fun inject(customerExecutor: CustomerExecutor)
    fun inject(technicalExecutor: TechnicalExecutor)
    fun inject(materialExecutor: MaterialExecutor)
    fun inject(typeNotificationExecutor: TypeNotificationExecutor)
    fun inject(notificationExecutor: NotificationExecutor)
    fun inject(downloadExecutor: DownloadExecutor)
    fun inject(assignedMaterialExecutor: AssignedMaterialExecutor)
    fun inject(maintenanceExecutor: MaintenanceExecutor)
    fun inject(signatureExecutor: SignatureExecutor)
}