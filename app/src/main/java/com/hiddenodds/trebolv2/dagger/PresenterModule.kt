package com.hiddenodds.trebolv2.dagger

import com.hiddenodds.trebolv2.domain.UIThread
import com.hiddenodds.trebolv2.domain.interactor.*
import com.hiddenodds.trebolv2.model.executor.*
import com.hiddenodds.trebolv2.model.interfaces.*
import com.hiddenodds.trebolv2.presentation.components.PdfEndTask
import com.hiddenodds.trebolv2.presentation.components.PdfNotification
import com.hiddenodds.trebolv2.presentation.presenter.*
import com.hiddenodds.trebolv2.tools.ManageImage
import com.hiddenodds.trebolv2.tools.PermissionUtils
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

    @Provides
    fun providePermissionUtils(): PermissionUtils {
        return PermissionUtils()
    }

    @Provides
    fun provideManageImage(permissionUtils: PermissionUtils): ManageImage{
        return ManageImage(permissionUtils)
    }

    @Provides
    fun providePdfNotification(): PdfNotification{
        return PdfNotification()
    }

    @Provides
    fun providePdfEndTask(): PdfEndTask{
        return PdfEndTask()
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
    fun provideSaveListCustomerUseCase(uiThread: UIThread,
                                   jobExecutor: JobExecutor,
                                   customerExecutor: CustomerExecutor):
            SaveListCustomerUseCase {
        return SaveListCustomerUseCase(jobExecutor, uiThread, customerExecutor)
    }

    @Provides
    fun provideDeleteCustomersUseCase(uiThread: UIThread,
                                      jobExecutor: JobExecutor,
                                      customerExecutor: CustomerExecutor): DeleteCustomersUseCase{
        return DeleteCustomersUseCase(jobExecutor, uiThread, customerExecutor)
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

    @Provides
    fun provideGetTechnicalUseCase(uiThread: UIThread,
                                   jobExecutor: JobExecutor,
                                   technicalExecutor: TechnicalExecutor): GetTechnicalUseCase{
        return GetTechnicalUseCase(jobExecutor, uiThread, technicalExecutor)
    }

    ///////////////////

    @Provides
    fun provideGetRemoteDataUseCase(uiThread: UIThread,
                                    jobExecutor: JobExecutor):
            GetRemoteDataUseCase{
        return GetRemoteDataUseCase(jobExecutor, uiThread)
    }

    @Provides
    fun provideSetRemoteDataUseCase(uiThread: UIThread,
                                    jobExecutor: JobExecutor): SetRemoteNotificationDataUseCase{
        return SetRemoteNotificationDataUseCase(jobExecutor, uiThread)
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

    @Provides
    fun provideTechnicalPresenter(getTechnicalUseCase:
                                  GetTechnicalUseCase): TechnicalPresenter{
        return TechnicalPresenter(getTechnicalUseCase)
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
    fun provideDeleteListMaterialUseCase(uiThread: UIThread,
                                         jobExecutor: JobExecutor,
                                         materialExecutor: MaterialExecutor): DeleteListMaterialUseCase{
        return DeleteListMaterialUseCase(jobExecutor, uiThread, materialExecutor)
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
                                       SaveListMaterialUseCase,
                                       deleteListMaterialUseCase:
                                       DeleteListMaterialUseCase):
            MaterialRemotePresenter{
        return MaterialRemotePresenter(getRemoteDataUseCase,
                saveListMaterialUseCase, deleteListMaterialUseCase)
    }

    @Provides
    fun provideMaterialPresenter(getListMaterialUseCase:
                                 GetListMaterialUseCase): MaterialPresenter{
        return MaterialPresenter(getListMaterialUseCase)
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
    fun provideGetLisTypeNotificationUseCase(uiThread: UIThread,
                                             jobExecutor: JobExecutor,
                                             typeNotificationExecutor:
                                             TypeNotificationExecutor): GetLisTypeNotificationUseCase{
        return GetLisTypeNotificationUseCase(jobExecutor, uiThread, typeNotificationExecutor)
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
    fun provideAssignedMaterialExecutor(): AssignedMaterialExecutor{
        return AssignedMaterialExecutor()
    }

    @Provides
    fun provideIAssignedMaterialRepository(assignedMaterialExecutor:
                                           AssignedMaterialExecutor): IAssignedMaterialRepository{
        return assignedMaterialExecutor
    }

    @Provides
    fun provideAddAssignedMaterialToNotificationUseCase(uiThread: UIThread,
                                                        jobExecutor: JobExecutor,
                                                        assignedMaterialExecutor:
                                                        AssignedMaterialExecutor):
            AddAssignedMaterialToNotificationUseCase{
        return AddAssignedMaterialToNotificationUseCase(jobExecutor,
                uiThread, assignedMaterialExecutor)
    }

    @Provides
    fun provideUpdateAssignedMaterialUseCase(uiThread: UIThread,
                                             jobExecutor: JobExecutor,
                                             assignedMaterialExecutor:
                                             AssignedMaterialExecutor): UpdateAssignedMaterialUseCase{
        return UpdateAssignedMaterialUseCase(jobExecutor,
                uiThread, assignedMaterialExecutor)
    }

    @Provides
    fun provideDeleteAssignedMaterialUseCase(uiThread: UIThread,
                                             jobExecutor: JobExecutor,
                                             assignedMaterialExecutor:
                                             AssignedMaterialExecutor): DeleteAssignedMaterialUseCase{
        return DeleteAssignedMaterialUseCase(jobExecutor,
                uiThread, assignedMaterialExecutor)
    }
    @Provides
    fun provideUpdateAssignedMaterialPresenter(updateAssignedMaterialUseCase:
                                               UpdateAssignedMaterialUseCase): UpdateAssignedMaterialPresenter{
        return UpdateAssignedMaterialPresenter(updateAssignedMaterialUseCase)
    }

    @Provides
    fun provideAddAssignedMaterialToNotificationPresenter(addAssignedMaterialToNotificationUseCase:
                                                          AddAssignedMaterialToNotificationUseCase): AddAssignedMaterialToNotificationPresenter{
        return AddAssignedMaterialToNotificationPresenter(addAssignedMaterialToNotificationUseCase)
    }

    @Provides
    fun provideDeleteAssignedMaterialPresenter(deleteAssignedMaterialUseCase:
                                               DeleteAssignedMaterialUseCase):
            DeleteAssignedMaterialPresenter{
        return DeleteAssignedMaterialPresenter(deleteAssignedMaterialUseCase)
    }
    /////////////////////

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
    fun provideUpdateFieldNotificationUseCase(uiThread: UIThread,
                                              jobExecutor: JobExecutor,
                                              notificationExecutor:
                                              NotificationExecutor):
            UpdateFieldNotificationUseCase{
        return UpdateFieldNotificationUseCase(jobExecutor, uiThread,
                notificationExecutor)
    }

    @Provides
    fun provideDeleteNotificationUseCase(uiThread: UIThread,
                                         jobExecutor: JobExecutor,
                                         notificationExecutor:
                                         NotificationExecutor): DeleteNotificationUseCase{
        return DeleteNotificationUseCase(jobExecutor, uiThread,
                notificationExecutor)
    }


    @Provides
    fun provideUpdateFieldNotificationPresenter(updateFieldNotificationUseCase:
                                                UpdateFieldNotificationUseCase):
            UpdateFieldNotificationPresenter{
        return UpdateFieldNotificationPresenter(updateFieldNotificationUseCase)
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
    fun provideAddCustomerToNotificationUseCase(uiThread: UIThread,
                                                jobExecutor: JobExecutor,
                                                notificationExecutor:
                                                NotificationExecutor): AddCustomerToNotificationUseCase{
        return AddCustomerToNotificationUseCase(jobExecutor, uiThread,
                notificationExecutor)
    }

    @Provides
    fun provideGetFinishedNotificationUseCase(uiThread: UIThread,
                                              jobExecutor: JobExecutor,
                                              notificationExecutor:
                                              NotificationExecutor): GetFinishedNotificationUseCase{
        return GetFinishedNotificationUseCase(jobExecutor, uiThread,
                notificationExecutor)
    }

    @Provides
    fun provideUpdateDataRemoteWaterPresenter(getFinishedNotificationUseCase:
                                              GetFinishedNotificationUseCase,
                                              setRemoteNotificationDataUseCase:
                                              SetRemoteNotificationDataUseCase,
                                              deleteNotificationUseCase:
                                              DeleteNotificationUseCase): UpdateDataRemoteWaterPresenter{
        return UpdateDataRemoteWaterPresenter(getFinishedNotificationUseCase,
                setRemoteNotificationDataUseCase, deleteNotificationUseCase)
    }

    @Provides
    fun provideAddNotificationToTechnicalPresenter(addNotificationToTechnicalUseCase:
                                                   AddNotificationToTechnicalUseCase,
                                                   addCustomerToNotificationUseCase:
                                                   AddCustomerToNotificationUseCase):
            AddNotificationToTechnicalPresenter{
        return AddNotificationToTechnicalPresenter(addNotificationToTechnicalUseCase,
                addCustomerToNotificationUseCase)
    }

    //////////////////

    @Provides
    fun provideDownloadExecutor(): DownloadExecutor{
        return DownloadExecutor()
    }

    @Provides
    fun provideIDownloadRepository(downloadExecutor: DownloadExecutor):
            IDownloadRepository{
        return downloadExecutor
    }

    @Provides
    fun provideCreateDownloadsUseCase(uiThread: UIThread,
                                      jobExecutor: JobExecutor,
                                      downloadExecutor:
                                      DownloadExecutor): CreateDownloadsUseCase{
        return CreateDownloadsUseCase(jobExecutor, uiThread,
                downloadExecutor)
    }

    @Provides
    fun provideUpdateDownloadUseCase(uiThread: UIThread,
                                     jobExecutor: JobExecutor,
                                     downloadExecutor:
                                     DownloadExecutor): UpdateDownloadUseCase{
        return UpdateDownloadUseCase(jobExecutor, uiThread,
                downloadExecutor)
    }

    @Provides
    fun provideGetDownloadUseCase(uiThread: UIThread,
                                  jobExecutor: JobExecutor,
                                  downloadExecutor:
                                  DownloadExecutor): GetDownloadUseCase{
        return GetDownloadUseCase(jobExecutor, uiThread,
                downloadExecutor)
    }

    @Provides
    fun provideDeleteDownloadsUseCase(uiThread: UIThread,
                                      jobExecutor: JobExecutor,
                                      downloadExecutor:
                                      DownloadExecutor): DeleteDownloadsUseCase{
        return DeleteDownloadsUseCase(jobExecutor, uiThread,
                downloadExecutor)
    }

    @Provides
    fun provideDownloadNotificationUseCase(getRemoteDataUseCase:
                                            GetRemoteDataUseCase,
                                            createDownloadsUseCase:
                                            CreateDownloadsUseCase,
                                            updateDownloadUseCase:
                                            UpdateDownloadUseCase,
                                            deleteDownloadsUseCase:
                                            DeleteDownloadsUseCase):
            DownloadNotificationUseCase {
        return DownloadNotificationUseCase(getRemoteDataUseCase,
                createDownloadsUseCase,
                updateDownloadUseCase, deleteDownloadsUseCase)
    }

    @Provides
    fun provideNotificationDownloadPresenter(downloadNotificationUseCase:
                                             DownloadNotificationUseCase):
            NotificationDownloadPresenter{
        return NotificationDownloadPresenter(downloadNotificationUseCase)
    }

    @Provides
    fun provideDownloadCustomerUseCase(getRemoteDataUseCase:
                                        GetRemoteDataUseCase,
                                        updateDownloadUseCase:
                                        UpdateDownloadUseCase):
            DownloadCustomerUseCase {
        return DownloadCustomerUseCase(getRemoteDataUseCase,
                updateDownloadUseCase)
    }

    @Provides
    fun provideCustomerDownloadPresenter(downloadCustomerUseCase:
                                         DownloadCustomerUseCase): CustomerDownloadPresenter{
        return CustomerDownloadPresenter(downloadCustomerUseCase)
    }

    @Provides
    fun provideSaveNotificationPresenter(getDownloadUseCase:
                                         GetDownloadUseCase,
                                         saveListNotificationUseCase:
                                         SaveListNotificationUseCase,
                                         deleteNotificationsOfTechnicalUseCase:
                                         DeleteNotificationsOfTechnicalUseCase,
                                         getLisTypeNotificationUseCase:
                                         GetLisTypeNotificationUseCase):
            SaveNotificationPresenter{
        return SaveNotificationPresenter(getDownloadUseCase,
                saveListNotificationUseCase,
                deleteNotificationsOfTechnicalUseCase,
                getLisTypeNotificationUseCase)
    }

    @Provides
    fun provideSaveCustomerPresenter(getDownloadUseCase:
                                     GetDownloadUseCase,
                                     saveCustomerUseCase:
                                     SaveListCustomerUseCase,
                                     deleteCustomersUseCase:
                                     DeleteCustomersUseCase):
            SaveCustomerPresenter {
        return SaveCustomerPresenter(getDownloadUseCase,
                saveCustomerUseCase,
                deleteCustomersUseCase)
    }

    @Provides
    fun provideSendEmailPresenter(): SendEmailPresenter{
        return SendEmailPresenter()
    }

}