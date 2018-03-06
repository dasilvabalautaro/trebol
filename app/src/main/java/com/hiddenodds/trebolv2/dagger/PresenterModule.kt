package com.hiddenodds.trebolv2.dagger

import com.hiddenodds.trebolv2.domain.UIThread
import com.hiddenodds.trebolv2.domain.interactor.*
import com.hiddenodds.trebolv2.model.executor.*
import com.hiddenodds.trebolv2.model.interfaces.*
import com.hiddenodds.trebolv2.presentation.presenter.*
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun provideJobExecutor(): JobExecutor{
        return JobExecutor()
    }

    @Provides
    fun provideThreadExecutor(jobExecutor: JobExecutor): IThreadExecutor {
        return jobExecutor
    }

    @Provides
    fun provideUIThread(): UIThread{
        return UIThread()
    }
    @Provides
    fun providePostExecutionThread(uiThread: UIThread): IPostExecutionThread {
        return uiThread
    }

    //////////////////////

    @Provides
    fun provideCustomerExecutor(): CustomerExecutor{
        return CustomerExecutor()
    }

    @Provides
    fun provideCustomerRepository(customerExecutor: CustomerExecutor):
            ICustomerRepository {
        return customerExecutor
    }

    @Provides
    fun provideSaveCustomerUseCase(uiThread: UIThread,
                                   jobExecutor: JobExecutor,
                                   customerExecutor: CustomerExecutor):
            SaveCustomerUseCase {
        return SaveCustomerUseCase(jobExecutor, uiThread, customerExecutor)
    }

    @Provides
    fun provideSaveCustomerPresenter(saveCustomerUseCase: SaveCustomerUseCase):
            SaveCustomerPresenter {
        return SaveCustomerPresenter(saveCustomerUseCase)
    }

    /////////////////

    @Provides
    fun provideTechnicalExecutor(): TechnicalExecutor{
        return TechnicalExecutor()
    }

    @Provides
    fun provideTechnicalRepository(technicalExecutor: TechnicalExecutor):
            ITechnicalRepository {
        return technicalExecutor
    }

    @Provides
    fun provideSaveListTechnicalUseCase(uiThread: UIThread,
                                        jobExecutor: JobExecutor,
                                        technicalExecutor: TechnicalExecutor):
            SaveListTechnicalUseCase{
        return SaveListTechnicalUseCase(jobExecutor, uiThread, technicalExecutor)
    }

    @Provides
    fun provideDeleteNotificationsOfTechnicalUseCase(uiThread: UIThread,
                                                     jobExecutor: JobExecutor,
                                                     technicalExecutor: TechnicalExecutor):
            DeleteNotificationsOfTechnicalUseCase{
        return DeleteNotificationsOfTechnicalUseCase(jobExecutor, uiThread, technicalExecutor)
    }
    @Provides
    fun provideSaveDependentTechnicalUseCase(uiThread: UIThread,
                                             jobExecutor: JobExecutor,
                                             technicalExecutor: TechnicalExecutor): SaveDependentTechnicalUseCase{
        return SaveDependentTechnicalUseCase(jobExecutor, uiThread, technicalExecutor)
    }

    @Provides
    fun provideGetTechnicalMasterUseCase(uiThread: UIThread,
                                         jobExecutor: JobExecutor,
                                         technicalExecutor: TechnicalExecutor): GetTechnicalMasterUseCase{
        return GetTechnicalMasterUseCase(jobExecutor, uiThread, technicalExecutor)
    }

    ///////////////////

    @Provides
    fun provideGetRemoteDataUseCase(uiThread: UIThread,
                                    jobExecutor: JobExecutor):
            GetRemoteDataUseCase{
        return GetRemoteDataUseCase(jobExecutor, uiThread)
    }

    @Provides
    fun provideTechnicalRemotePresenter(getRemoteDataUseCase: GetRemoteDataUseCase,
                                        saveListTechnicalUseCase: SaveListTechnicalUseCase,
                                        saveDependentTechnicalUseCase: SaveDependentTechnicalUseCase): TechnicalRemotePresenter{
        return TechnicalRemotePresenter(getRemoteDataUseCase, saveListTechnicalUseCase,
                saveDependentTechnicalUseCase)
    }

    @Provides
    fun provideTechnicalMasterPresenter(getTechnicalMasterUseCase:
                                        GetTechnicalMasterUseCase):
            TechnicalMasterPresenter{
        return TechnicalMasterPresenter(getTechnicalMasterUseCase)
    }

    ///////////////////

    @Provides
    fun provideMaterialExecutor(): MaterialExecutor{
        return MaterialExecutor()
    }

    @Provides
    fun provideIMaterialRepository(materialExecutor: MaterialExecutor):
            IMaterialRepository{
        return materialExecutor
    }

    @Provides
    fun provideSaveListMaterialUseCase(uiThread: UIThread,
                                       jobExecutor: JobExecutor,
                                       materialExecutor: MaterialExecutor):
            SaveListMaterialUseCase{
        return SaveListMaterialUseCase(jobExecutor, uiThread, materialExecutor)
    }

    @Provides
    fun provideGetListMaterialUseCase(uiThread: UIThread,
                                      jobExecutor: JobExecutor,
                                      materialExecutor: MaterialExecutor):
            GetListMaterialUseCase{
        return GetListMaterialUseCase(jobExecutor, uiThread, materialExecutor)
    }

    @Provides
    fun provideMaterialRemotePresenter(getRemoteDataUseCase:
                                       GetRemoteDataUseCase,
                                       saveListMaterialUseCase:
                                       SaveListMaterialUseCase): MaterialRemotePresenter{
        return MaterialRemotePresenter(getRemoteDataUseCase, saveListMaterialUseCase)
    }

    ////////////////

    @Provides
    fun provideTypeNotificationExecutor(): TypeNotificationExecutor{
        return TypeNotificationExecutor()
    }

    @Provides
    fun provideITypeNotificationRepository(typeNotificationExecutor:
                                           TypeNotificationExecutor):
            ITypeNotificationRepository{
        return typeNotificationExecutor
    }

    @Provides
    fun provideSaveListTypeNotificationUseCase(uiThread: UIThread,
                                               jobExecutor: JobExecutor,
                                               typeNotificationExecutor:
                                               TypeNotificationExecutor): SaveListTypeNotificationUseCase{
        return SaveListTypeNotificationUseCase(jobExecutor, uiThread, typeNotificationExecutor)
    }

    @Provides
    fun provideTypeNotificationRemotePresenter(getRemoteDataUseCase:
                                               GetRemoteDataUseCase,
                                               saveListTypeNotificationUseCase:
                                               SaveListTypeNotificationUseCase):
            TypeNotificationRemotePresenter{
        return TypeNotificationRemotePresenter(getRemoteDataUseCase,
                saveListTypeNotificationUseCase)
    }

    ////////////////////////

    @Provides
    fun provideNotificationExecutor(): NotificationExecutor{
        return NotificationExecutor()
    }

    @Provides
    fun provideINotificationRepository(notificationExecutor:
                                       NotificationExecutor):
            INotificationRepository{
        return notificationExecutor
    }

    @Provides
    fun provideSaveListNotificationUseCase(uiThread: UIThread,
                                           jobExecutor: JobExecutor,
                                           notificationExecutor:
                                           NotificationExecutor):
            SaveListNotificationUseCase{
        return SaveListNotificationUseCase(jobExecutor, uiThread,
                notificationExecutor)
    }

    @Provides
    fun provideAddNotificationToTechnicalUseCase(uiThread: UIThread,
                                                 jobExecutor: JobExecutor,
                                                 notificationExecutor:
                                                 NotificationExecutor): AddNotificationToTechnicalUseCase{
        return AddNotificationToTechnicalUseCase(jobExecutor, uiThread,
                notificationExecutor)
    }

    @Provides
    fun provideNotificationRemotePresenter(getRemoteDataUseCase:
                                           GetRemoteDataUseCase,
                                           saveListNotificationUseCase:
                                           SaveListNotificationUseCase,
                                           getTechnicalMasterUseCase:
                                           GetTechnicalMasterUseCase,
                                           deleteNotificationsOfTechnicalUseCase:
                                           DeleteNotificationsOfTechnicalUseCase,
                                           addNotificationToTechnicalUseCase:
                                           AddNotificationToTechnicalUseCase):
            NotificationRemotePresenter{
        return NotificationRemotePresenter(getRemoteDataUseCase,
                saveListNotificationUseCase, getTechnicalMasterUseCase,
                deleteNotificationsOfTechnicalUseCase,
                addNotificationToTechnicalUseCase)
    }
}