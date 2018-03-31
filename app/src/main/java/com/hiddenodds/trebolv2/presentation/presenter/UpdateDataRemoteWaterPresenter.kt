package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.DeleteNotificationUseCase
import com.hiddenodds.trebolv2.domain.interactor.GetFinishedNotificationUseCase
import com.hiddenodds.trebolv2.domain.interactor.SetRemoteNotificationDataUseCase
import com.hiddenodds.trebolv2.model.persistent.file.ManageFile
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class UpdateDataRemoteWaterPresenter @Inject constructor(private val getFinishedNotificationUseCase:
                                                         GetFinishedNotificationUseCase,
                                                         private val setRemoteNotificationDataUseCase:
                                                         SetRemoteNotificationDataUseCase,
                                                         private val deleteNotificationUseCase:
                                                         DeleteNotificationUseCase):
        BasePresenter(){

    private var listNotification: ArrayList<NotificationModel> = ArrayList()
    private var notificationModel: NotificationModel? = null

    fun executeUpdateRemoteWater(){
        if ((context as App).connectionNetwork.isOnline()) {
            getFinishedNotificationUseCase.execute(NotificationObserver())
        }else{
            showError(context.resources.getString(R.string.network_not_found))
        }
    }

    private fun getUpdateNext(){
        if (listNotification.isNotEmpty()){
            val notify = listNotification.last()
            listNotification.remove(notify)
            this.notificationModel = notify
            setRemoteNotificationDataUseCase.notification = this.notificationModel
            setRemoteNotificationDataUseCase.execute(UpdateNotificationObserver())
        }else{
            view!!.executeTask(7)
        }
    }

    override fun destroy() {
        super.destroy()
        getFinishedNotificationUseCase.dispose()
        setRemoteNotificationDataUseCase.dispose()
        deleteNotificationUseCase.dispose()
    }

    private fun deleteNotification(){
        deleteNotificationUseCase.id = notificationModel!!.id
        deleteNotificationUseCase.codeTech = notificationModel!!.idTech
        deleteNotificationUseCase.execute(DeleteNotificationObserver())
    }

    inner class NotificationObserver: DisposableObserver<List<NotificationModel>>(){
        override fun onNext(t: List<NotificationModel>) {
            listNotification = ArrayList(t)
            getUpdateNext()
        }

        override fun onComplete() {
            println("Get List Notification OK")
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class UpdateNotificationObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            deleteNotification()
        }

        override fun onComplete() {
            println("Update Notification : Complete")
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class DeleteNotificationObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            async {
                ManageFile.deleteFileOfWork(notificationModel!!.code)
            }
            getUpdateNext()
        }

        override fun onComplete() {
            println("Delete Notification : Complete")
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }
}